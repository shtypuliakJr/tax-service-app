package com.taxserviceapp.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    @Secured("authenticated")
    public String getMessage() {
        return "Hello";
    }
}
