package com.taxserviceapp.exceptions;

public class NoReportFoundById extends RuntimeException {
    public NoReportFoundById(String message) {
        super(message);
    }
}
