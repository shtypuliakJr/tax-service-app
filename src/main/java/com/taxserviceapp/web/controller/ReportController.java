package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class ReportController {

    @Autowired
    private ReportService reportService;

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

        return "redirect:/user/reports";
    }

    @GetMapping(value = "/report-edit/{id}")
    public String getEditFormFromUserPage(@PathVariable(value = "id") Long id, Model model) {

        ReportDTO reportDTO = reportService.findReportById(id);
        model.addAttribute("report", reportDTO);

        return "/user/report-edit";
    }


    @PostMapping(value = "/report-edit")
    public String editReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                             BindingResult result,
                             Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            return "user/report-edit";
        }
        reportService.updateReport(PojoConverter.convertReportDTOToEntity(reportDTO,
                (User) authentication.getPrincipal()));

        return "redirect:/user/reports";
    }

    @GetMapping(value = "/report-delete/{id}")
    public String deleteReportFromUserPage(@PathVariable("id") Long id) {

        reportService.deleteReport(id);

        return "redirect:/user/reports";
    }

    @GetMapping(value = "/report-view/{id}")
    public String viewReport(@PathVariable(value = "id") Long id, Model model) {

        try {
            ReportDTO reportDTO = reportService.findReportById(id);
            model.addAttribute("report", reportDTO);
        } catch (ReportNotFoundException e) {
            model.addAttribute("errorNoResult", e.getMessage());
        }

        return "user/report-view";
    }
}