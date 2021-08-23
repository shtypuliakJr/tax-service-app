package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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
        List<Report> reports = inspectorService.getReports();
        model.addAttribute("reports", reports);
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

        List<Report> reports = inspectorService.getReportsByRequestParam(id, date, period, status, sortField);
        model.addAttribute("reports", reports);

        model.addAttribute("lastSelectedPeriod", period);
        model.addAttribute("lastSelectedStatus", status);
        model.addAttribute("lastSelectedSort", sortField);

        return "inspector/reports";
    }

    @GetMapping("/user-view")
    public String getUserInfo(@RequestParam(name = "userId") Long userId, Model model) {

        model.addAttribute("userInfo", userService.getUserInfoById(userId));

        return "inspector/user-view";
    }

    @PostMapping("/report-view")
    public String getReport(@RequestParam(name = "report") Long reportId, Model model) {

        Report report = inspectorService.getReportById(reportId);
        model.addAttribute("report", report);

        return "inspector/report-view";
    }

    @GetMapping("/statistic")
    public String getStatistic(Model model) {

        StatisticDTO statisticDTO = inspectorService.getStatisticData();

        model.addAttribute("statistic", statisticDTO);

        return "inspector/statistic";
    }

}
