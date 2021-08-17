package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.UserPageService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserPageController {

    @Autowired
    UserPageService userPageService;

    @GetMapping("/user")
    public String getUserPage(Authentication authentication, Model model) {
        Long id = ((User) authentication.getPrincipal()).getId();

        List<Report> reportsByUserId = userPageService.getReportsByUserId(id);
        model.addAttribute("reportList", reportsByUserId);
        return "user/user";
    }

    @PostMapping("/report-sort")
    public String getUserPageSorted(Authentication authentication, Model model) {
        Long id = ((User) authentication.getPrincipal()).getId();

        List<Report> reportsByUserId = userPageService.getReportsSortedByIncome(id);
        model.addAttribute("reportList", reportsByUserId);
        return "user/user";
    }
}
