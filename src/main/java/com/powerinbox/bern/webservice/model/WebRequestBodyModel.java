package com.powerinbox.bern.webservice.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WebRequestBodyModel implements Comparable<WebRequestBodyModel> {
    private long timestamp;
    private Integer itemId;
    private String country;
    private String device;
    @JsonDeserialize(contentAs = BigDecimal.class)
    private BigDecimal value;

    public WebRequestBodyModel(long timestamp, Integer itemId, String country, String device, BigDecimal value) {
        this.timestamp = timestamp;
        this.itemId = itemId;
        this.country = country;
        this.device = device;
        this.value = value;
    }

    public WebRequestBodyModel() {
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public int compareTo(WebRequestBodyModel o) {
        return new CompareToBuilder().append(this.getTimestamp(), o.getTimestamp()).toComparison();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
