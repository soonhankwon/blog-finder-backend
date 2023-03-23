package com.soon.domain;

public enum SortType {
    ACCURACY("accuracy"),
    RECENCY("recency"),
    SIM("sim"),
    DATE("date");

    private final String value;

    SortType(String value)  {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
