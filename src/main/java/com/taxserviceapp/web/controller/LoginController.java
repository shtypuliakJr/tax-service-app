package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(UserDTO userDTO, Model model) {
//        model.addAttribute("user", userDTO);
        return "login";
    }

//    @PostMapping("/login")
//    public String loginCredits(UserDTO userDTO, Model model) {
//        return "main";
//    }


}
