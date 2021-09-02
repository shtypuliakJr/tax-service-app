package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.exceptions.NoUserFoundException;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.exceptions.ReportStatusException;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.UserDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Log4j
@Controller
@RequestMapping("/inspector")
public class InspectorController {

    private final InspectorService inspectorService;
    private final UserService userService;

    @Autowired
    public InspectorController(InspectorService inspectorService, UserService userService) {
        this.inspectorService = inspectorService;
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
        log.info("Inspector reports page: filter by parameters: " + "id = " + id + ",date = " +
                date + ",period = " + period + ",status = " + status + ",sortField = " + sortField);

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
        log.info("Search by param: " + searchParam);

        model.addAttribute("search", searchParam);

        return "inspector/reports";
    }

    @GetMapping("/user-view/{userId}")
    public String getUserInfo(@PathVariable(name = "userId") Long userId, Model model) {

        try {
            UserDTO userInfoById = userService.getUserInfoById(userId);
            model.addAttribute("userInfo", userInfoById);

        } catch (NoUserFoundException exception) {
            model.addAttribute("errorNoResult", exception.getMessage());
        }

        return "inspector/user-view";
    }

    @GetMapping("/statistic")
    public String getStatistic(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("statistic", inspectorService.getStatisticData());
        log.info("Get statistic by inspector " +
                user.getFirstName() + " " + user.getLastName() + " with id: " + user.getId());

        return "inspector/statistic";
    }

    @GetMapping("/report-view/{reportId}")
    public String getReport(@PathVariable(name = "reportId") Long reportId, HttpServletRequest request, Model model) {
        try {
            ReportDTO report = inspectorService.getReportById(reportId);
            request.getSession().setAttribute("report", report);
            model.addAttribute("report", report);
        } catch (ReportNotFoundException e) {
            model.addAttribute("errorNoReport", e.getMessage());
        }
        return "inspector/report-view";
    }


    @PostMapping("/report-view")
    public String getReportProcess(@ModelAttribute(name = "report") ReportDTO reportDTO,
                                   HttpServletRequest request, Model model) {

        try {
            inspectorService.setReportStatus(reportDTO);
            request.getSession().removeAttribute("report");

            log.info("Set status and comment for report with id = " + reportDTO.getId());

            return "redirect:/inspector/reports";
        } catch (ReportStatusException reportStatusException) {

            ReportDTO report1 = (ReportDTO) request.getSession().getAttribute("report");
            report1.setComment(reportDTO.getComment());
            report1.setStatus(reportDTO.getStatus());
            model.addAttribute("report", report1);
            model.addAttribute("error", reportStatusException.getMessage());

            log.error("Invalid input in report view; report id = " + reportDTO.getId());

            return "/inspector/report-view";
        }
    }
}
