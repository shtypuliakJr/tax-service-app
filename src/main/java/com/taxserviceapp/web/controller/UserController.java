package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ReportService reportService;

    @GetMapping("reports")
    public String getUserPage(@RequestParam(name = "date", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                              @RequestParam(name = "period", required = false) TaxPeriod period,
                              @RequestParam(name = "status", required = false) Status status,
                              @RequestParam(name = "sortField", required = false) SortField sortField,
                              Authentication authentication, Model model) {

        Long id = ((User) authentication.getPrincipal()).getId();

        try {
            List<ReportDTO> reports = reportService.getReportsByRequestParam(id, date, period, status, sortField)
                    .stream().map(PojoConverter::convertReportEntityToDTO).collect(Collectors.toList());
            model.addAttribute("reportList", reports);
        } catch (NoReportsFoundException e) {
            model.addAttribute("errorNoResult", e.getMessage());
        }

        return "user/reports";
    }

    @GetMapping("/user-info")
    public String getUserInfoPage(Authentication authentication, Model model) {

        User user = ((User) authentication.getPrincipal());

        model.addAttribute("user", user);

        return "user/user-info";
    }
}
