package com.taxserviceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "/main";
    }


    @GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }


    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world response body";
    }
}
