package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/inspector")
public class InspectorController {

    private final InspectorService inspectorService;

    @Autowired
    public InspectorController(InspectorService inspectorService) {
        this.inspectorService = inspectorService;
    }

    @GetMapping
    public String getInspectorMainPage(Model model) {
        List<Report> reports = inspectorService.getReports();
        model.addAttribute("reports", reports);
        return "inspector/reports";
    }

    @GetMapping("/reports")
    public String getReportsPage(Model model, Authentication authentication) {

        List<Report> reports = inspectorService.getReports();
        model.addAttribute("reports", reports);
        return "inspector/reports";
    }
}
