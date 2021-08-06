package com.taxserviceapp.controller;

import com.taxserviceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final UserService userServiceInfo;

    @Autowired
    public MainController(UserService userServiceInfo) {
        this.userServiceInfo = userServiceInfo;
    }

    @GetMapping("/")
    public String mainPage() {
        return "/main";
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }

    @GetMapping("/error")
    public String error403() {
        return "/error/403";
    }

}
