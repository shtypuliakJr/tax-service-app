package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.UserRole;
import com.taxserviceapp.exceptions.*;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InspectorService {

    @Autowired
    private ReportService reportService;

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public InspectorService(UserRepository userRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    public List<ReportDTO> getReports() throws NoReportsFoundException {

        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new NoReportsFoundException("No reports Found");
        }
        return reports.stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByParameters(Long id, Date reportDate, TaxPeriod period,
                                                  Status status, SortField sortField) throws NoReportsFoundException {

//        List<ReportDTO> reportDTOS = reportService
//                .getReportsByRequestParam(id, reportDate, period, status, sortField).stream()
//                .map(PojoConverter::convertReportEntityToDTO)
//                .collect(Collectors.toList());
//
//        if (reportDTOS.isEmpty())
//            throw new NoReportsFoundException("No reports found");

        return reportService.getReportsByRequestParam(id, reportDate, period, status, sortField);
//        return reportDTOS;

    }

    public ReportDTO getReportById(Long reportId) throws ReportNotFoundException {
        return reportService.findReportById(reportId);
    }

    public List<ReportDTO> getReportsBySearchParameter(String searchParam) throws NoReportsFoundException {

        return findReportsBySearchParam(searchParam).stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());

    }

    //ToDo: refactoring
    @Transactional
    public StatisticDTO getStatisticData() {

        List<Report> reports = reportRepository.findAll();
        Map<Integer, Integer> countsByYearSortedMap = new TreeMap<>(getCountByYear(reports));

        Long countOfUsers = Long.valueOf(userRepository.countAllByUserRole(UserRole.USER));
        Long countOfInspectors = Long.valueOf(userRepository.countAllByUserRole(UserRole.INSPECTOR));

        Long countOrReports = reportRepository.count();
        Integer reportsProcessing = reportRepository.countAllByStatus(Status.PROCESSING);
        Integer reportsDisapproved = reportRepository.countAllByStatus(Status.DISAPPROVED);
        Integer reportsApproved = reportRepository.countAllByStatus(Status.APPROVED);

        return StatisticDTO.builder()
                .countOfReports(countOrReports)
                .countOfUsers(countOfUsers)
                .countOfInspectors(countOfInspectors)
                .processingReports(reportsProcessing)
                .approvedReports(reportsApproved)
                .disapprovedReports(reportsDisapproved)
                .countReportsPerYear(countsByYearSortedMap)
                .build();
    }

    private Map<Integer, Integer> getCountByYear(List<Report> mealList) {
        return mealList.stream()
                .collect(Collectors.groupingBy(Report::getYear,
                        Collectors.reducing(0, report -> 1, Integer::sum)));
    }

    private List<Report> findReportsBySearchParam(String searchParam) throws NoReportsFoundException {

        if (searchParam.isEmpty())
            return reportRepository.findAll();
        if (searchParam.matches("[0-9]+")) {
            if (searchParam.matches("^[0-9]{12}"))
                return reportRepository.findAllByUser_Ipn(searchParam);

            if (searchParam.matches("^[0-9]+"))
                return reportRepository.findAllById(Long.parseLong(searchParam));

        }

        if (searchParam.matches("[\\w]+[\\s]?[\\w]+")) {

            StringTokenizer fullName = new StringTokenizer(searchParam, " ");
            if (fullName.countTokens() == 1) {
                String nameOrSurname = fullName.nextToken();
                return reportRepository.findAllByFirstOrLastName(nameOrSurname, nameOrSurname);
            } else {
                return reportRepository.findAllByFirstAndLastName(fullName.nextToken(), fullName.nextToken());
            }
        }
        throw new NoReportsFoundException("No reports found by search");
    }

    public ReportDTO setReportStatus(Long reportId, String comment, Status status) {

        if (status == null)
            throw new ReportStatusException("Require status");

        if (status == Status.DISAPPROVED && comment == null)
            throw new ReportStatusException("Require comment if status will be disapproved");

        Optional<Report> report = reportRepository.findById(reportId);

        return report.map(rep -> {
                    rep.setComment(comment);
                    rep.setStatus(status);
                    reportRepository.save(rep);
                    return rep;
                }).map(PojoConverter::convertReportEntityToDTO)
                .orElseThrow(() -> new ReportNotFoundException("No report found"));
    }

    public ReportDTO setReportStatus(ReportDTO reportDTO) {
        if (reportDTO.getStatus() == null)
            throw new ReportStatusException("Require status");

        if (reportDTO.getStatus() == Status.DISAPPROVED &&
                (reportDTO.getComment() == null || reportDTO.getComment().equals("")))
            throw new ReportStatusException("Require comment if status will be disapproved");

        Optional<Report> report = reportRepository.findById(reportDTO.getId());

        return report.map(rep -> {
                    rep.setComment(reportDTO.getComment());
                    rep.setStatus(reportDTO.getStatus());
                    reportRepository.save(rep);
                    return rep;
                }).map(PojoConverter::convertReportEntityToDTO)
                .orElseThrow(() -> new ReportNotFoundException("No report found"));
    }
}
