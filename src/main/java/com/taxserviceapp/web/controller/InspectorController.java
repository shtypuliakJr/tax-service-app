package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.business.service.ReportService;
import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.NoResultException;
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

        model.addAttribute("userInfo", userService.getUserInfoById(userId));

        return "inspector/user-view";
    }

    @GetMapping("/statistic")
    public String getStatistic(Model model) {

        StatisticDTO statisticDTO = inspectorService.getStatisticData();
        model.addAttribute("statistic", statisticDTO);

        return "inspector/statistic";
    }

    @GetMapping("/report-view")
    public String getReport(@RequestParam(name = "reportId") Long reportId, Model model) {

        try {
            Report report = inspectorService.getReportById(reportId);
            model.addAttribute("reports", report);
        } catch (NoResultException e) {
            model.addAttribute("errorNoResult", e.getMessage());
        }
        return "inspector/report-view";
    }

    @PostMapping("/report-view")
    public String getReportProcess(
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "reportId", required = false) Long reportId,
            Model model) {

//        if (status == null) {
//            model.addAttribute("previousComment", comment);
//            model.addAttribute("errorStatusNull", "error");
//        }
//        if (status.equals(Status.APPROVED) || (status.equals(Status.DISAPPROVED) && comment != null)) {
////            inspectorService.updateCommentAndStatusById(Long reportId);
//            System.out.println("save");
//            return "redirect:/inspector/reports";
//        }
//        if (comment == null && status != null) {
//            model.addAttribute("previousStatus", status);
//            model.addAttribute("errorCommentNull", "error comment");
//        }


        return "redirect:/inspector/report-view";
    }
}
