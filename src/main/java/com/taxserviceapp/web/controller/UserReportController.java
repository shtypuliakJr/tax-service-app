package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j
@Controller
@RequestMapping("/user")
public class UserReportController {

    private final ReportService reportService;

    @Autowired
    public UserReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/report-form")
    public String addReport(Model model, Authentication authentication) {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setUserId(((User) (authentication.getPrincipal())).getId());

        model.addAttribute("report", reportDTO);
        return "user/report-form";
    }

    @PostMapping(value = "/report-add")
    public String receiveReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            return "user/report-form";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportService.saveNewReport(PojoConverter.convertReportDTOToEntity(reportDTO, user));
        log.info("Added new report by " + user.getEmail());
        return "redirect:/user/reports";
    }

    @GetMapping(value = "/report-edit/{id}")
    public String getEditFormFromUserPage(@PathVariable(value = "id") Long id, Model model) {
        try {
            ReportDTO reportDTO = reportService.findReportById(id);
            model.addAttribute("report", reportDTO);
        } catch (ReportNotFoundException e) {
            model.addAttribute("reportNotFound", e.getMessage());
        }
        return "/user/report-edit";
    }


    @PostMapping(value = "/report-edit")
    public String editReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                             BindingResult result,
                             Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            log.error("Invalid input in edit form; report id = " + reportDTO.getId());

            return "user/report-edit";
        }

        reportService.updateReport(PojoConverter.convertReportDTOToEntity(reportDTO,
                (User) authentication.getPrincipal()));
        log.info("Edited report with id = " + reportDTO.getId());

        return "redirect:/user/reports";
    }

    @PostMapping(value = "/report-delete")
    public String deleteReportFromUserPage(@RequestParam("id") Long id) {

        reportService.deleteReport(id);
        log.info("Deleted report with id = " + id);

        return "redirect:/user/reports";
    }

    @GetMapping(value = "/report-view/{id}")
    public String viewReport(@PathVariable(value = "id") Long id, Model model) {

        try {
            ReportDTO reportDTO = reportService.findReportById(id);
            model.addAttribute("report", reportDTO);
        } catch (ReportNotFoundException e) {
            model.addAttribute("reportNotFound", e.getMessage());
        }

        return "user/report-view";
    }
}