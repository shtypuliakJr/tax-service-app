package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.UserRole;
import com.taxserviceapp.exceptions.NoReportFoundById;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<Report> reports =
                reportService.getReportsByRequestParam(id, reportDate, period, status, sortField);

        if (!reports.isEmpty()) {
            return reports.stream()
                    .map(PojoConverter::convertReportEntityToDTO)
                    .collect(Collectors.toList());
        }

        throw new NoReportsFoundException("No reports found by filter");
    }

    public ReportDTO getReportById(Long reportId) throws NoReportsFoundException {
        return reportService.findReportById(reportId);
//        return reportRepository.findById(reportId)
//                .map(PojoConverter::convertReportEntityToDTO)
//                .orElseThrow(() -> new NoReportFoundById("No report found by id"));
    }

    public List<ReportDTO> getReportsBySearchParameter(String searchParam) throws NoReportsFoundException {

        List<ReportDTO> reportDTOS = findReportsBySearchParam(searchParam).stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());

        if (reportDTOS.isEmpty())
            throw new NoReportsFoundException("No reports found by search");

        return reportDTOS;
    }
    //ToDo: refactoring
    public StatisticDTO getStatisticData() {

        List<Report> reports = reportRepository.findAll();

        Long countOfUsers = Long.valueOf(userRepository.countAllByUserRole(UserRole.USER));
        Long countOrReports = reportRepository.count();
        Long countOfInspectors = Long.valueOf(userRepository.countAllByUserRole(UserRole.INSPECTOR));

        Integer reportsProcessing = reportRepository.countAllByStatus(Status.PROCESSING);
        Integer reportsDisapproved = reportRepository.countAllByStatus(Status.DISAPPROVED);
        Integer reportsApproved = reportRepository.countAllByStatus(Status.APPROVED);

        Map<Integer, Integer> countsByYearSortedMap = new TreeMap<>(getCountByYear(reports));

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
}
