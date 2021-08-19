package com.taxserviceapp.data.entity;

public enum Status {

    PROCESSING("Processing"),
    APPROVED("Approved"),
    DISAPPROVED("Disapproved");

    public String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
