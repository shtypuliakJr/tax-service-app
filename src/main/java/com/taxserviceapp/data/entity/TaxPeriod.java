package com.taxserviceapp.data.entity;

public enum TaxPeriod {

    FIRST_PERIOD("First period"),
    SECOND_PERIOD("Second period"),
    THIRD_PERIOD("Third period"),
    FOURTH_PERIOD("Fourth period"),
    HALF_YEAR("Half year"),
    YEAR("Year");

    private final String period;

    TaxPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
