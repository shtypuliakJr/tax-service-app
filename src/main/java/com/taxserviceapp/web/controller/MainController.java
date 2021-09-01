package com.taxserviceapp.web.controller;

import com.taxserviceapp.data.entity.User;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Log4j
@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Authentication authentication, Model model) {

        String userRole = "guest";

        if (authentication != null)
            userRole = authentication.getAuthorities().stream()
                    .findFirst().get().getAuthority().toLowerCase();
        log.info("Visit main page by: " + userRole);

        model.addAttribute("role", userRole);
        return "/main";
    }
}
