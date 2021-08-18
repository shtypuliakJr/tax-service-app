package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/user")
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

    @GetMapping(value = "/report-view/{id}")
    public String viewReport(@PathVariable(value = "id") Long id, Model model) {

        ReportDTO reportDTO = convertReportEntityToDTO(reportService.findReportById(id));

        model.addAttribute("report", reportDTO);

        return "user/report-view";
    }

    @PostMapping(value = "/report-view/report-delete/{id}")
    public String deleteReport(@PathVariable("id") Long id) {

        reportService.deleteReport(id);

        return "redirect:/user/user";
    }

    @GetMapping("/report-view/report-edit/{id}")
    public String deleteDelete(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("here");

        ReportDTO reportDTO = convertReportEntityToDTO(reportService.findReportById(id));
        reportDTO.setId(id);
        model.addAttribute("report", reportDTO);

        return "/user/report-edit";
    }

    @PostMapping("/report-edit")
    public String editReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                             BindingResult result,
                             Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            return "user/report-edit";
        }
        reportService.updateReport(convertReportDTOToEntity(reportDTO, (User) authentication.getPrincipal()));
        return "redirect:/user/user";
    }

    //Todo: add util class converter
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
                .id(reportDTO.getId())
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
