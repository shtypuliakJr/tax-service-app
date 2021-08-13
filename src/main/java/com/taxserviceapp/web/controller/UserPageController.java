package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.UserPageService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserPageController {

    @Autowired
    UserPageService userPageService;

    @GetMapping("/user")
    public String getUserPage(Model model) {

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<List<Report>> reportsByUserId = userPageService.getReportsByUserId(principal.getId());
        reportsByUserId.ifPresent(reports -> model.addAttribute("reportList", reportsByUserId));

        Optionals.ifPresentOrElse( reportsByUserId,
                reports -> model.addAttribute("reportList", reportsByUserId),
                () -> model.addAttribute("reportList",  "error")
        );

        return "user/user";
    }
}
