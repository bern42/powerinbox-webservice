package com.powerinbox.webservice.service;

import com.powerinbox.bern.webservice.model.*;
import com.powerinbox.bern.webservice.service.MapperService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MapperServiceTest.class);

    private MapperService impl = new MapperService();

    @Before
    public void setup() {

    }

    @Test
    public void mapStringToRequestModelTest() {
        WebRequestBodyModel expected = new WebRequestBodyModel();
        expected.setCountry("US");
        expected.setDevice("phone");
        expected.setItemId(1);
        expected.setValue(BigDecimal.valueOf(0.005));
        expected.setTimestamp(123);

        WebRequestBodyModel actual = impl.mapStringToRequestBodyModel(buildStringRequest(true));
        logger.debug(actual.toString());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getDevice(), actual.getDevice());
        Assert.assertEquals(expected.getItemId(), actual.getItemId());
        Assert.assertEquals(expected.getValue(), actual.getValue());
        Assert.assertEquals(expected.getTimestamp(), actual.getTimestamp());
    }

    @Ignore
    @Test
    public void mapStringToListRequestModelTest() {
        List<WebRequestBodyModel> expectedList = new ArrayList<>();
        WebRequestBodyModel expected1 = new WebRequestBodyModel(123, 1, "US", "phone", BigDecimal.valueOf(0.0050));
        WebRequestBodyModel expected2 = new WebRequestBodyModel(127, 1, "US", "phone", BigDecimal.valueOf(0.125));
        WebRequestBodyModel expected3 = new WebRequestBodyModel(127, 2, "GB", "desktop", BigDecimal.valueOf(0.021));
        WebRequestBodyModel expected4 = new WebRequestBodyModel(121, 3, "US", "phone", BigDecimal.valueOf(0.10));
        WebRequestBodyModel expected5 = new WebRequestBodyModel(101, 1, null, "desktop", BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
        expectedList.addAll(Arrays.asList(expected1, expected2, expected3, expected4, expected5));
        expectedList.sort(WebRequestBodyModel::compareTo);

        List<WebRequestBodyModel> actualList = impl.mapStringRequestToListOfRequestModels(buildStringRequest(false));
        actualList.sort(WebRequestBodyModel::compareTo);
        int counter = 0;
        for (WebRequestBodyModel expected : expectedList) {
            WebRequestBodyModel actual = actualList.get(counter);
            logger.debug(actual.toString());
            Assert.assertEquals(expected.getCountry(), actual.getCountry());
            Assert.assertEquals(expected.getDevice(), actual.getDevice());
            Assert.assertEquals(expected.getItemId(), actual.getItemId());
            Assert.assertEquals(expected.getValue(), actual.getValue());
            Assert.assertEquals(expected.getTimestamp(), actual.getTimestamp());
            counter +=1;
        }
    }

    private String buildStringRequest(boolean single) {
        if (single) {
            return "{\"timestamp\":123, \"itemId\": 1, \"country\": \"US\", \"device\": \"phone\", \"value\": 0.005}";
        } else {
            return "{\"timestamp\":123, \"itemId\": 1, \"country\": \"US\", \"device\": \"phone\", \"value\": 0.005}\n" +
            "{\"timestamp\":127, \"itemId\": 1, \"country\": \"US\", \"device\": \"phone\", \"value\": 0.125}\n" +
            "{\"timestamp\":127, \"itemId\": 2, \"country\": \"GB\", \"device\": \"desktop\", \"value\": 0.021}\n" +
            "{\"timestamp\":121, \"itemId\": 3, \"country\": \"US\", \"device\": \"phone\", \"value\": 0.10}\n" +
            "{\"timestamp\":101, \"itemId\": 1, \"device\": \"desktop\", \"value\": 0}";
        }
    }

    @Test
    public void mapRequestToDeviceTest() {
        WebRequestBodyModel request = new WebRequestBodyModel(123, 1, "US", "phone", BigDecimal.valueOf(0.005));
        DeviceInfo actual = impl.mapRequestToDeviceInfo(request);
        DeviceInfo expected = new DeviceInfo(123,"phone", 1, BigDecimal.valueOf(0.005));
        logger.debug("Device Object: " + actual.toString());
        Assert.assertEquals(expected.getTimestamp(), actual.getTimestamp());
        Assert.assertEquals(expected.getDevice(), actual.getDevice());
        Assert.assertEquals(expected.getCount(), actual.getCount());
        Assert.assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void mapRequestToCountryTest() {
        WebRequestBodyModel request = new WebRequestBodyModel(123, 1, "US", "phone", BigDecimal.valueOf(0.005));
        CountryInfo actual = impl.mapRequestToCountryInfo(request);
        CountryInfo expected = new CountryInfo(123, "US", 1, BigDecimal.valueOf(0.005));
        logger.debug("Country Object: " + actual.toString());
        Assert.assertEquals(expected.getTimestamp(), actual.getTimestamp());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getCount(), actual.getCount());
        Assert.assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void mapRequestToItemTest() {
        WebRequestBodyModel request = new WebRequestBodyModel(123, 1, "US", "phone", BigDecimal.valueOf(0.005));
        ItemInfo actual = impl.mapRequestToItemInfo(request);
        ItemInfo expected = new ItemInfo(123, 1, 1, BigDecimal.valueOf(0.005));
        logger.debug("Item Object: " + actual.toString());
        Assert.assertEquals(expected.getTimestamp(), actual.getTimestamp());
        Assert.assertEquals(expected.getItemId(), actual.getItemId());
        Assert.assertEquals(expected.getCount(), actual.getCount());
        Assert.assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void getUnixTimeFromStringTest() {
        String day = "1970-01-01";
        int hour = 2;

        long actual = impl.getUnixTimeFromString(day, hour);
        long expected = 7200000;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getQueryObjectUnixTimeTest() {
        QueryObject object = new QueryObject("1970-01-01", 1, "1970-01-03", 0, Split.COUNTRY);
        QueryObject actual = impl.mapQueryObjectToUnixTime(object);

        Assert.assertEquals(3600000, actual.getStartUnix());
        Assert.assertEquals(172800000, actual.getEndUnix());
    }

}
