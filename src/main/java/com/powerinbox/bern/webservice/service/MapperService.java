package com.powerinbox.bern.webservice.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerinbox.bern.webservice.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MapperService {

    /**
     * Maps the request objects into List<WebRequestBodyModel>
     *
     * @param request
     * @return List<WebRequestBodyModel>
     */
    public List<WebRequestBodyModel> mapStringRequestToListOfRequestModels(String request) {
        String[] requests = StringUtils.split(request, "\n\r");
        return Arrays.stream(requests).map(this::mapStringToRequestBodyModel).collect(Collectors.toList());
    }

    /**
     * Helper me
     *
     * @param jsonString
     * @return
     */
    public WebRequestBodyModel mapStringToRequestBodyModel(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        try {
            WebRequestBodyModel model = mapper.readValue(jsonString, WebRequestBodyModel.class);
            return model;
        } catch (IOException e) {
            throw new RuntimeException("Error mapping jsonString: " + jsonString, e);
        }

    }

    public DeviceInfo mapRequestToDeviceInfo(WebRequestBodyModel request) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDevice(request.getDevice());
        deviceInfo.setTimestamp(request.getTimestamp());
        deviceInfo.setCount(1);
        deviceInfo.setValue(request.getValue());
        return deviceInfo;
    }

    public CountryInfo mapRequestToCountryInfo(WebRequestBodyModel request) {
        CountryInfo countryInfo = new CountryInfo();
        countryInfo.setTimestamp(request.getTimestamp());
        if (request.getCountry() != null) {
            countryInfo.setCountry(request.getCountry());
        } else {
            countryInfo.setCountry("null");
        }
        countryInfo.setCount(1);
        countryInfo.setValue(request.getValue());
        return countryInfo;
    }

    public ItemInfo mapRequestToItemInfo(WebRequestBodyModel request) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setTimestamp(request.getTimestamp());
        itemInfo.setItemId(request.getItemId());
        itemInfo.setValue(request.getValue());
        itemInfo.setCount(1);
        return itemInfo;
    }

    public QueryObject mapQueryObjectToUnixTime(QueryObject request) {
        request.setStartUnix(getUnixTimeFromString(request.getStartDay(), request.getStartHour()));
        request.setEndUnix(getUnixTimeFromString(request.getEndDay(), request.getEndHour()));
        return request;
    }

    public long getUnixTimeFromString(String day, int hour) {
        String hourStr = StringUtils.leftPad(String.valueOf(hour), 2, "0");
        String dateStr = day + "T" + hourStr + ":00:00.0Z";
        long unixT = Instant.parse(dateStr).toEpochMilli();
        return unixT;
    }
}
