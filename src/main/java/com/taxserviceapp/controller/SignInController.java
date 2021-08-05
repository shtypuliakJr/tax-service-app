package com.taxserviceapp.controller;

import com.taxserviceapp.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignInController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/sign-in")
    public String signInPage() {
        //model.addAttribute("userLogin", new User);
        return "sign-in";
    }

    @PostMapping("sign-in")
    public String userHomePage(@RequestBody User user) {
        if (user.getUsername().equals("user")) {
            return "user/user";
        }
        return "Oops";
    }
}
