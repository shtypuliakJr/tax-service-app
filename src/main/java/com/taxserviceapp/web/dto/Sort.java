package com.taxserviceapp.web.dto;

public enum Sort {

    PERIOD("Period"),
    DATE("Date");

    public String sortName;

    Sort(String sortName) {
        this.sortName = sortName;
    }

    public String getSortName() {
        return sortName;
    }
}