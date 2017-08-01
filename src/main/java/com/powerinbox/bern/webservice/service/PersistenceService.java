package com.powerinbox.bern.webservice.service;

import com.powerinbox.bern.webservice.dao.PostgresDao;
import com.powerinbox.bern.webservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class PersistenceService {


    private static final Logger logger = LoggerFactory.getLogger(PersistenceService.class);
    @Autowired
    protected MapperService mapperService;
    @Autowired
    protected PostgresDao dao;
    // Executor Service for inserting data in parallel
    private ExecutorService insertExecutorService = Executors.newFixedThreadPool(4);

    public void processDataRequest(List<WebRequestBodyModel> dataList) {
        int size = dataList.size();
        if (size < 4) {
            logger.info("processing " + Integer.toString(size) + " entries in serial");
            // 3 or less entries, process sequentially
            dataList.forEach((entry) -> {
                persistToDatabase(entry);
            });
        } else {
            logger.info("processing " + Integer.toString(size) + " entries in parallel");
            // 4 or more - parallel
            List<Future> futures = new ArrayList<>();
            for (WebRequestBodyModel request : dataList) {
                PersistDataRunnable deviceRunnable = new PersistDataRunnable(this, request, Split.DEVICE);
                futures.add(insertExecutorService.submit(deviceRunnable));

                PersistDataRunnable countryRunnable = new PersistDataRunnable(this, request, Split.COUNTRY);
                futures.add(insertExecutorService.submit(countryRunnable));

                PersistDataRunnable itemRunnable = new PersistDataRunnable(this, request, Split.ITEM_ID);
                futures.add(insertExecutorService.submit(itemRunnable));
            }

            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Problem inserting and processing future: " + future.toString(), e);
                    insertExecutorService.shutdown();
                    throw new RuntimeException(e);
                }
            }
        }
        logger.info("finished processing");
    }

    private void persistToDatabase(WebRequestBodyModel model) {
        persistToDevice(model);
        persistToCountry(model);
        persistToItem(model);
    }

    public void persistToDevice(WebRequestBodyModel model) {
        DeviceInfo deviceRow = mapperService.mapRequestToDeviceInfo(model);
        dao.upsertDevice(deviceRow);
    }

    public void persistToCountry(WebRequestBodyModel model) {
        CountryInfo countryRow = mapperService.mapRequestToCountryInfo(model);
        dao.upsertCountry(countryRow);
    }

    public void persistToItem(WebRequestBodyModel model) {
        ItemInfo itemRow = mapperService.mapRequestToItemInfo(model);
        dao.upsertItem(itemRow);
    }

    public List<ResponseObject> processQueryRequest(QueryObject request) {
        QueryObject query = mapperService.mapQueryObjectToUnixTime(request);
        Split splitBy = query.getSplitBy();
        List<ResponseObject> output = new ArrayList<>();
        switch (splitBy) {
            case COUNTRY:
                output.addAll(getCountryResults(query));
                break;
            case DEVICE:
                output.addAll(getDeviceResults(query));
                break;
            case ITEM_ID:
                output.addAll(getItemResults(query));
                break;
        }
        return output;
    }

    public List<ResponseObject> getCountryResults(QueryObject queryObject) {
        return dao.getCountryResults(queryObject);
    }

    public List<ResponseObject> getItemResults(QueryObject queryObject) {
        return dao.getItemResults(queryObject);
    }

    public List<ResponseObject> getDeviceResults(QueryObject queryObject) {
        return dao.getDeviceResults(queryObject);
    }

    @PreDestroy
    public void shutdown() {
        if (insertExecutorService != null) {
            insertExecutorService.shutdown();
            logger.info("insertExecutorService is shutdown");
        }
    }

}
