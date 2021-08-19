package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.UserPageService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.dto.ReportFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        model.addAttribute("filter", new ReportFilterDTO());
        return "user/user";
    }

    @PostMapping("/report-sort")
    public String getUserPageSorted(Authentication authentication, Model model) {

        Long id = ((User) authentication.getPrincipal()).getId();
        System.out.println("sort");
        List<Report> reportsByUserId = userPageService.getReportsSortedByIncome(id);
        model.addAttribute("reportList", reportsByUserId);
        return "user/user";
    }

    @PostMapping("/report-find")
    public String getReportBy(@RequestParam(name = "date", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(name = "period", required = false) TaxPeriod period,
                              @RequestParam(name = "status", required = false) Status status,
                              Authentication authentication, Model model) {
//
//        System.out.println("Date " + date);
//        System.out.println("Status " + status);
//        System.out.println("Period " + period);
//
        Long id = ((User) authentication.getPrincipal()).getId();
        List<Report> reportsByUserId = userPageService.getReportsByRequestParam(id, date, period, status);

        model.addAttribute("reportList", reportsByUserId);

        return "redirect:user/user";
    }
}
