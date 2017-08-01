package com.powerinbox.webservice.dao;

import com.powerinbox.bern.webservice.Application;
import com.powerinbox.bern.webservice.config.PostgresConfig;
import com.powerinbox.bern.webservice.config.PostgresProperties;
import com.powerinbox.bern.webservice.dao.PostgresDao;
import com.powerinbox.bern.webservice.model.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = {PostgresProperties.class, PostgresConfig.class, Application.class})
public class PostgresDaoFunctionalTest {

    @Autowired
    protected PostgresDao dao;

    final static Logger logger = LoggerFactory.getLogger(PostgresDaoFunctionalTest.class);


    @Test
    public void queryTest() {

        CountryInfo countryInfo = new CountryInfo(1501369446000L, "US", 2, BigDecimal.valueOf(1.23));
        CountryInfo countryInfo1 = new CountryInfo(1501369446000L, "GB", 3, BigDecimal.valueOf(2.43));

        dao.upsertCountry(countryInfo);
        logger.debug("Upserted Country Entry: " + countryInfo.toString());
        dao.upsertCountry(countryInfo1);
        logger.debug("Upserted Country Entry: " + countryInfo1.toString());

        ItemInfo itemInfo = new ItemInfo(1501369446000L, 1, 1, BigDecimal.valueOf(1.00));
        ItemInfo itemInfo1 = new ItemInfo(1501369446000L, 2, 2, BigDecimal.valueOf(1.43));
        ItemInfo itemInfo2 = new ItemInfo(1501369446000L, 3, 2, BigDecimal.valueOf(1.23));

        dao.upsertItem(itemInfo);
        logger.debug("Upserted Item Entry: " + itemInfo.toString());
        dao.upsertItem(itemInfo1);
        logger.debug("Upserted Item Entry: " + itemInfo1.toString());
        dao.upsertItem(itemInfo2);
        logger.debug("Upserted Item Entry: " + itemInfo2.toString());

        DeviceInfo deviceInfo = new DeviceInfo(1501369446000L, "phone", 2, BigDecimal.valueOf(1.23));
        DeviceInfo deviceInfo1 = new DeviceInfo(1501369446000L, "tablet", 1, BigDecimal.valueOf(1.20));
        DeviceInfo deviceInfo2 = new DeviceInfo(1501369446000L, "desktop", 2, BigDecimal.valueOf(1.23));

        dao.upsertDevice(deviceInfo);
        logger.debug("Upserted Devicee Entry: " + deviceInfo.toString());
        dao.upsertDevice(deviceInfo1);
        logger.debug("Upserted Devicee Entry: " + deviceInfo1.toString());
        dao.upsertDevice(deviceInfo2);
        logger.debug("Upserted Devicee Entry: " + deviceInfo2.toString());


        QueryObject queryObject = new QueryObject("2017-07-27", 12, "2017-07-30", 1, Split.COUNTRY);
        queryObject.setStartUnix(1501276650000L);
        queryObject.setEndUnix(1501376710000L);

        List<ResponseObject> actualList = dao.getCountryResults(queryObject);

        Assert.assertNotEquals(0, actualList.size());

        queryObject.setSplitBy(Split.ITEM_ID);

        List<ResponseObject> actualList1 = dao.getItemResults(queryObject);
       ;
        Assert.assertNotEquals(0, actualList1.size());

        queryObject.setSplitBy(Split.DEVICE);
        List<ResponseObject> actualList2 = dao.getDeviceResults(queryObject);

        Assert.assertNotEquals(0, actualList2.size());
    }

    @Test
    public void upsertCountryTest() {
        CountryInfo countryInfo = new CountryInfo(1501369446000L, "US", 2, BigDecimal.valueOf(1.23));
        CountryInfo countryInfo1 = new CountryInfo(1501369446000L, "GB", 3, BigDecimal.valueOf(2.43));

        dao.upsertCountry(countryInfo);
        logger.debug("Upserted Country Entry: " + countryInfo.toString());
        dao.upsertCountry(countryInfo1);
        logger.debug("Upserted Country Entry: " + countryInfo1.toString());
    }

    @Test
    public void upsertItemTest() {

        ItemInfo itemInfo = new ItemInfo(1501369446000L, 1, 1, BigDecimal.valueOf(1.00));
        ItemInfo itemInfo1 = new ItemInfo(1501369446000L, 2, 2, BigDecimal.valueOf(1.43));
        ItemInfo itemInfo2 = new ItemInfo(1501369446000L, 3, 2, BigDecimal.valueOf(1.23));

        dao.upsertItem(itemInfo);
        logger.debug("Upserted Item Entry: " + itemInfo.toString());
        dao.upsertItem(itemInfo1);
        logger.debug("Upserted Item Entry: " + itemInfo1.toString());
        dao.upsertItem(itemInfo2);
        logger.debug("Upserted Item Entry: " + itemInfo2.toString());

    }

    @Test
    public void upsertDeviceTest() {
        DeviceInfo deviceInfo = new DeviceInfo(1501369446000L, "phone", 2, BigDecimal.valueOf(1.23));
        DeviceInfo deviceInfo1 = new DeviceInfo(1501369446000L, "tablet", 1, BigDecimal.valueOf(1.20));
        DeviceInfo deviceInfo2 = new DeviceInfo(1501369446000L, "desktop", 2, BigDecimal.valueOf(1.23));

        dao.upsertDevice(deviceInfo);
        logger.debug("Upserted Devicee Entry: " + deviceInfo.toString());
        dao.upsertDevice(deviceInfo1);
        logger.debug("Upserted Devicee Entry: " + deviceInfo1.toString());
        dao.upsertDevice(deviceInfo2);
        logger.debug("Upserted Devicee Entry: " + deviceInfo2.toString());
    }

}
