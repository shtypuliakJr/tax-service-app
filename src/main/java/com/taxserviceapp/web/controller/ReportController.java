package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.model.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")

@Validated
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report-form")
    public String addReport(Model model, Authentication authentication) {
        model.addAttribute("report", new Report());
        return "user/report-form";
    }

    @PostMapping("/add-report")
    public String recieveReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            return "user/report-form";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportService.saveNewReport(convertReportDTOToEntity(reportDTO, user));
        return "redirect:/user/user";
    }

    @GetMapping("/report-view/{id}")
    public String viewReport(@PathVariable(value = "id") Long id, Model model) {
        Report report = reportService.findReportById(id);
        ReportDTO reportDTO = convertReportEntityToDTO(report);
        System.out.println(reportDTO);
        model.addAttribute("report", reportDTO);
        return "user/report-view";
    }

    public ReportDTO convertReportEntityToDTO(Report report) {

        return ReportDTO.builder()
                .id(report.getId())
                .income(report.getIncome())
                .taxRate(report.getTaxRate())
                .taxPeriod(report.getTaxPeriod())
                .year(report.getYear())
                .status(report.getStatus())
                .reportDate(report.getReportDate())
                .comment(report.getComment())
                .build();
    }

    public Report convertReportDTOToEntity(ReportDTO reportDTO, User user) {

        return Report.builder()
                .income(reportDTO.getIncome())
                .taxRate(reportDTO.getTaxRate())
                .taxPeriod(reportDTO.getTaxPeriod())
                .year(reportDTO.getYear())
                .status(Status.PROCESSING)
                .reportDate(Date.valueOf(LocalDate.now()))
                .user(user)
                .build();
    }
}
