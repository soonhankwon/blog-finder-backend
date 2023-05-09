package com.soon.domain;

import com.soon.exception.ErrorCode;
import com.soon.exception.ApiException;

public enum SortType {
    ACCURACY("accuracy"),
    RECENCY("recency"),
    SIM("sim"),
    DATE("date");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void validSortType() {
        if(!this.equals(SortType.ACCURACY) && !this.equals(SortType.RECENCY)) {
            throw new ApiException(ErrorCode.SORT_TYPE_INVALID);
        }
    }

    public SortType convertSortTypeForNaver() {
        if (this.equals(SortType.ACCURACY)) {
            return SortType.SIM;
        } else {
            return SortType.DATE;
        }
    }
}
