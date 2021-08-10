package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "/main";
    }

}
