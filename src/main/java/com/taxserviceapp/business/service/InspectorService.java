package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.*;
import com.taxserviceapp.exceptions.NoReportFoundById;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InspectorService {

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

    public List<ReportDTO> getReportsByRequestParam(Long id, Date reportDate, TaxPeriod period,
                                                    Status status, SortField sortField) throws NoReportsFoundException {

        Specification<Report> specification = Specification
                .where(filterField(id, "user")
                        .and(filterField(status, "status"))
                        .and(filterField(reportDate, "reportDate"))
                        .and(filterField(period, "taxPeriod")));

        Optional<List<Report>> reports;

        if (sortField != null) {
            reports = Optional.of(reportRepository.findAll(specification,
                    Sort.by(getDirection(sortField.direction), sortField.getFieldInTable())));
        } else {
            reports = Optional.of(reportRepository.findAll(specification));
        }

        List<Report> reportList = reports.get();

        if (!reportList.isEmpty())
            return reportList.stream()
                    .map(PojoConverter::convertReportEntityToDTO)
                    .collect(Collectors.toList());

        throw new ReportNotFoundException("No reports found by filter");

    }
//
//    public Report updateCommentAndStatusById(Long id) {
//        reportRepository.()
//        return
//    }

    Sort.Direction getDirection(String direction) {
        return direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }


    public ReportDTO getReportById(Long reportId) throws NoReportsFoundException {
        return reportRepository.findById(reportId)
                .map(PojoConverter::convertReportEntityToDTO)
                .orElseThrow(() -> new NoReportFoundById("No report found by id"));
    }

    public List<ReportDTO> getReportsBySearchParameter(String searchParam) throws NoReportsFoundException {

        List<ReportDTO> reportDTOS = findReportsBySearchParam(searchParam).stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());

        if (reportDTOS.isEmpty())
            throw new NoReportsFoundException("No reports found by search");

        return reportDTOS;
    }

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
//                return reportRepository.findAllByUser_FirstNameOrUser_LastName(nameOrSurname, nameOrSurname);
            } else {
                return reportRepository.findAllByFirstAndLastName(fullName.nextToken(),fullName.nextToken());
//                        .findAllByUser_FirstNameAndUser_LastName(fullName.nextToken(), fullName.nextToken());
            }
        }
        throw new NoReportsFoundException("No reports found by search");
    }

    private <T> Specification<Report> filterField(T param, String fieldName) {
        return (reportRoot, criteriaQuery, criteriaBuilder) -> {
            if (param == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(reportRoot.get(fieldName), param);
        };
    }
}
