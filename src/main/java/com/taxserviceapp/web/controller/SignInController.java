package com.taxserviceapp.controller;

import com.taxserviceapp.data.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/sign-in")
public class SignInController {

    @GetMapping
    public String signInPage() {
        return "sign-in";
    }

    @PostMapping
    public String userHomePage(@RequestBody User user) {
        if (user.getUsername().equals("user")) {
            System.out.println("sign-in - here 2");
            return "redirect:user/user";
        }
        return "Oops";
    }
}
