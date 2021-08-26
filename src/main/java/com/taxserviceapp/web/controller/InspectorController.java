package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.*;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import com.taxserviceapp.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/inspector")
public class InspectorController {

    private final InspectorService inspectorService;
    private final ReportService reportService;
    private final UserService userService;

    @Autowired
    public InspectorController(InspectorService inspectorService, ReportService reportService, UserService userService) {
        this.inspectorService = inspectorService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping
    public String getInspectorMainPage(Model model) {

        try {
            List<ReportDTO> reports = inspectorService.getReports();
            model.addAttribute("reports", reports);
        } catch (NoReportsFoundException exception) {
            model.addAttribute("errorNoResult", exception.getMessage());
        }

        return "inspector/reports";
    }

    @GetMapping("/reports")
    public String getReportsPage(@RequestParam(name = "userId", required = false) Long id,
                                 @RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                 @RequestParam(name = "period", required = false) TaxPeriod period,
                                 @RequestParam(name = "status", required = false) Status status,
                                 @RequestParam(name = "sortField", required = false) SortField sortField,
                                 Model model) {

        try {
            List<ReportDTO> reports = inspectorService.getReportsByParameters(id, date, period, status, sortField);
            model.addAttribute("reports", reports);
        } catch (NoReportsFoundException exception) {
            model.addAttribute("errorNoResult", exception.getMessage());
        }

        model.addAttribute("lastSelectedDate", date);
        model.addAttribute("lastSelectedPeriod", period);
        model.addAttribute("lastSelectedStatus", status);
        model.addAttribute("lastSelectedSort", sortField);

        return "inspector/reports";
    }

    @GetMapping("/search")
    public String getReportsBySearch(@RequestParam(value = "search", required = false) String searchParam,
                                     Model model) {

        try {
            List<ReportDTO> reports = inspectorService.getReportsBySearchParameter(searchParam.trim());
            model.addAttribute("reports", reports);
        } catch (NoReportsFoundException exception) {
            model.addAttribute("errorNoResult", exception.getMessage());
        }

        model.addAttribute("search", searchParam);

        return "inspector/reports";
    }

    @GetMapping("/user-view")
    public String getUserInfo(@RequestParam(name = "userId") Long userId, Model model) {

        try {
            UserDTO userInfoById = userService.getUserInfoById(userId);
            model.addAttribute("userInfo", userInfoById);

        } catch (NoUserFoundException exception) {
            model.addAttribute("errorNoResult", exception.getMessage());
        }

        return "inspector/user-view";
    }

    @GetMapping("/statistic")
    public String getStatistic(Model model) {

        model.addAttribute("statistic", inspectorService.getStatisticData());

        return "inspector/statistic";
    }

    @GetMapping("/report-view")
    public String getReport(@RequestParam(name = "reportId") Long reportId, Model model) {
        try {
            ReportDTO report = inspectorService.getReportById(reportId);
            model.addAttribute("report", report);
        } catch (ReportNotFoundException e) {
            model.addAttribute("errorNoReport", e.getMessage());
        }
        return "inspector/report-view";
    }

    @PostMapping("/report-view")
    public String getReportProcess(@ModelAttribute(name = "report") ReportDTO reportDTO, Model model) {

        try {
            inspectorService.setReportStatus(reportDTO);
            return "redirect:/inspector/reports";
        } catch (ReportStatusException reportStatusException) {
            ReportDTO report = inspectorService.getReportById(reportDTO.getId());
            report.setComment(reportDTO.getComment());
            report.setStatus(reportDTO.getStatus());
            model.addAttribute("report", report);
            model.addAttribute("error", reportStatusException.getMessage());
            return "/inspector/report-view";
        }
    }
}
