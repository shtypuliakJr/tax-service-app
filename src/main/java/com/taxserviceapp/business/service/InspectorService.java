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
import java.util.function.Function;
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


        return Optional.of(reportRepository.findAll())
                .filter(collection -> !collection.isEmpty())
                .map((report) -> report.stream().map(PojoConverter::convertReportEntityToDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoReportsFoundException("No reports found"));

//        List<Report> reports = reportRepository.findAll();
//
//        if (reports.isEmpty()) {
//            throw new NoReportsFoundException("No reports Found");
//        }
//        return reports.stream()
//                .map(PojoConverter::convertReportEntityToDTO)
//                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByParameters(Long id, Date reportDate, TaxPeriod period,
                                                  Status status, SortField sortField) throws NoReportsFoundException {

        return reportService.getReportsByRequestParam(id, reportDate, period, status, sortField);
    }

    public ReportDTO getReportById(Long reportId) throws ReportNotFoundException {
        return reportService.findReportById(reportId);
    }

    public List<ReportDTO> getReportsBySearchParameter(String searchParam) throws NoReportsFoundException {

        return findReportsBySearchParam(searchParam).stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());
    }

    public StatisticDTO getStatisticData() {
// todo: rewrite

        return StatisticDTO.builder()
                .countOfReports(reportRepository.count())
                .countOfUsers(Long.valueOf(userRepository.countAllByUserRole(UserRole.USER)))
                .countOfInspectors(Long.valueOf(userRepository.countAllByUserRole(UserRole.INSPECTOR)))
                .processingReports(reportRepository.countAllByStatus(Status.PROCESSING))
                .approvedReports(reportRepository.countAllByStatus(Status.APPROVED))
                .disapprovedReports(reportRepository.countAllByStatus(Status.DISAPPROVED))
                .countReportsPerYear(new TreeMap<>(getCountByYear(reportRepository.findAll())))
                .build();
    }

    private Map<Integer, Long> getCountByYear(List<Report> mealList) {

        return mealList.stream().collect(Collectors.groupingBy(Report::getYear, Collectors.counting()));
    }

    private TreeMap<Integer, Long> getTreeMapCountByYear(List<Report> reports) {

        return reports.stream()
                .collect(Collectors.groupingBy(Report::getYear, Collectors.counting()))
                .entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, TreeMap::new,
                        Collectors.mapping(Map.Entry::getValue, Collectors.counting())));
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
