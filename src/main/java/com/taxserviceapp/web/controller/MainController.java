package com.taxserviceapp.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Authentication authentication, Model model) {

        String userRole = "guest";

        if (authentication != null)
            userRole = authentication.getAuthorities().stream()
                    .findFirst().get().getAuthority().toLowerCase();

        model.addAttribute("role", userRole);
        return "/main";
    }
}
