package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.*;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.exceptions.NoUserFoundException;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import com.taxserviceapp.web.dto.StatisticDTO;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<User> getUsers() throws NoUserFoundException {
        Optional<List<User>> users = userRepository.findAllByUserRoleEquals(UserRole.USER);

        return users.orElseThrow(() -> new NoUserFoundException("No users found"));
    }

    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportsByRequestParam(Long id, Date reportDate, TaxPeriod period,
                                                 Status status, SortField sortField) throws NoResultException {

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
        return reports.orElseThrow(() -> new ReportNotFoundException("No result"));
    }
//
//    public Report updateCommentAndStatusById(Long id) {
//        reportRepository.()
//        return
//    }

    Sort.Direction getDirection(String direction) {
        return direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    private <T> Specification<Report> filterField(T param, String fieldName) {
        return (reportRoot, criteriaQuery, criteriaBuilder) -> {
            if (param == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(reportRoot.get(fieldName), param);
        };
    }

    public Report getReportById(Long reportId) {
        return reportRepository.getById(reportId);
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

    public List<ReportDTO> getReportsBySearchParameter(String searchParam) throws NoReportsFoundException {

        List<ReportDTO> reportDTOS = findReportsBySearchParam(searchParam).stream()
                .map(PojoConverter::convertReportEntityToDTO)
                .collect(Collectors.toList());

        if (reportDTOS.isEmpty())
            throw new NoReportsFoundException("No reports found by search");

        return reportDTOS;
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
                return reportRepository.findAllByUser_FirstNameOrUser_LastName(nameOrSurname, nameOrSurname);
            } else {
                return reportRepository
                        .findAllByUser_FirstNameAndUser_LastName(fullName.nextToken(), fullName.nextToken());
            }
        }
        throw new NoReportsFoundException("No reports found by search");
    }
}
