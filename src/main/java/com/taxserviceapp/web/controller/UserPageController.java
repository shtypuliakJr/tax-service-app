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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserPageController {

    @Autowired
    UserPageService userPageService;

    @GetMapping("/user")
    public String getUserPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getId());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            System.out.println(authentication.getPrincipal());
////
////            System.out.println(authentication.getDetails());
//        System.out.println(authentication.getCredentials());
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user.getId());
//        try {
//
//            List<Report> reportsByUserId = userPageService.getReportsByUserId(user.getId());
//            reportsByUserId.forEach(System.out::println);
//
//        } catch (Exception e) {
//            e.getStackTrace();
//            System.out.println("No result");
//        }
        return "user/user";
    }
}
