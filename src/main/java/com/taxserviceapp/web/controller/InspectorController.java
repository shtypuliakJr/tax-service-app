package com.taxserviceapp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inspector")
public class InspectorController {

    @GetMapping("/inspector")
    public String getInspectorPage() {

        return "inspector/inspector";
    }
}
