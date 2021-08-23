package com.taxserviceapp.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest httpRequest, Model model) {
        String attribute = null;

        Integer statusCode = (Integer) httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                attribute = "404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                attribute = "500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                attribute = "403";
            }
        }
        model.addAttribute("errorCode", attribute);

        return "error/error";
    }
}

// ToDo: error model attribute display with text

