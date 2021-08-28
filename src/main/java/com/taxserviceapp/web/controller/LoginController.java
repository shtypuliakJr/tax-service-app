package com.taxserviceapp.web.controller;

import com.taxserviceapp.config.LoginSuccessHandler;
import com.taxserviceapp.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Controller
public class LoginController {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @GetMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) {
        System.out.println(authentication);
        if (authentication != null) {
            try {
                loginSuccessHandler.onAuthenticationSuccess(req, resp, authentication);
            } catch (ServletException | IOException e) {
                return "/error/error";
            }
        }

        return "/login";

    }
}
