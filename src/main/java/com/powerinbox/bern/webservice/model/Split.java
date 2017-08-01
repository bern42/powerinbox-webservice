package com.powerinbox.bern.webservice.model;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public enum Split {
    COUNTRY("country"),
    DEVICE("device"),
    ITEM_ID("itemId");

    private String displayName;

    Split(String displayName) {
        this.displayName = displayName;
    }

    public static Split findByDisplayName(String displayName) {
        return Stream.of(Split.values())
                .filter(split -> StringUtils.equals(split.getDisplayName(), displayName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unrecognized splitBy value: " + displayName));
    }

    public String getName() {
        return name();
    }

    public String getDisplayName() {
        return displayName;
    }

}
