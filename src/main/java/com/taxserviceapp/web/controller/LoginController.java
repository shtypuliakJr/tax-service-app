package com.taxserviceapp.web.controller;

import com.taxserviceapp.config.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    private final LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public LoginController(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) {

        if (authentication != null) {
            try {
                loginSuccessHandler.onAuthenticationSuccess(req, resp, authentication);
            } catch (ServletException | IOException e) {
                return "/login";
            }
        }

        return "/login";
    }
}
