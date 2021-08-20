package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
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
        Long userId = ((User)(authentication.getPrincipal())).getId();
        reportDTO.setUserId(userId);

        model.addAttribute("report", reportDTO);
        return "user/report-form";
    }

    @PostMapping(value = "/report-add")
    public String recieveReport(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("report", reportDTO);
            return "user/report-form";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportService.saveNewReport(PojoConverter.convertReportDTOToEntity(reportDTO, user));

        return "redirect:/user/user";
    }

    @GetMapping(value = "/report-edit/{id}")
    public String getEditFormFromUserPage(@PathVariable(value = "id") Long id, Model model) {

        ReportDTO reportDTO = PojoConverter.convertReportEntityToDTO(reportService.findReportById(id));
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
        reportService.updateReport(PojoConverter
                .convertReportDTOToEntity(reportDTO, (User) authentication.getPrincipal()));

        return "redirect:/user/user";
    }

    @GetMapping(value = "/report-delete/{id}")
    public String deleteReportFromUserPage(@PathVariable("id") Long id) {

        reportService.deleteReport(id);

        return "redirect:/user/user";
    }

    @GetMapping(value = "/report-view/{id}")
    public String viewReport(@PathVariable(value = "id") Long id, Model model) {

        ReportDTO reportDTO = PojoConverter.convertReportEntityToDTO(reportService.findReportById(id));

        model.addAttribute("report", reportDTO);

        return "user/report-view";
    }
}
// ToDo: add checking user's reports
// ToDo: exceptions