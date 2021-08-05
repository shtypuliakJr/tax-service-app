package com.taxserviceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/user")
public class UserController {

    @GetMapping("user")
    public String userPage() {
        return "user-page";
    }

}
