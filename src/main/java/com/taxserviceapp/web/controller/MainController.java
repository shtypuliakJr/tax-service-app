package com.taxserviceapp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        String code = locale.getCountry();
        return "/main";
    }

}
