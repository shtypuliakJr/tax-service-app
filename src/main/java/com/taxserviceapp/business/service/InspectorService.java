package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.*;
import com.taxserviceapp.exceptions.NoUserFoundException;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.web.dto.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        System.out.println(reports);
        return reports.orElseThrow(() -> new ReportNotFoundException("No result"));
    }


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
}
