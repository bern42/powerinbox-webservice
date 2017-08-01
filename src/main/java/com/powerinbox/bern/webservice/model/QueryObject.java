package com.powerinbox.bern.webservice.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QueryObject {

    private String startDay;
    private int startHour;
    private String endDay;
    private int endHour;
    private Split splitBy;
    private long startUnix;
    private long endUnix;

    public QueryObject(String startDay, int startHour, String endDay, int endHour, Split splitBy) {
        this.startDay = startDay;
        this.startHour = startHour;
        this.endDay = endDay;
        this.endHour = endHour;
        this.splitBy = splitBy;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public Split getSplitBy() {
        return splitBy;
    }

    public void setSplitBy(Split splitBy) {
        this.splitBy = splitBy;
    }

    public long getStartUnix() {
        return startUnix;
    }

    public void setStartUnix(long startUnix) {
        this.startUnix = startUnix;
    }

    public long getEndUnix() {
        return endUnix;
    }

    public void setEndUnix(long endUnix) {
        this.endUnix = endUnix;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
