package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.web.dto.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserPageService {

    private final ReportRepository reportRepository;

    @Autowired
    public UserPageService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportsByUserId(Long userId) throws NoResultException {
        return reportRepository
                .findReportsByUser_Id(userId)
                .orElseThrow(() -> new NoResultException("No result"));
    }

    public List<Report> getReportsByRequestParam(Long id, LocalDate date, TaxPeriod period, Status status, SortField sortField) throws NoResultException {
        Specification<Report> specification = Specification
                .where(hasId(id)
                        .and(hasStatus(status))
                        .and(hasDate(date))
                        .and(hasPeriod(period)));

        Optional<List<Report>> reports;

        if (sortField != null)
            reports = Optional.of(reportRepository
                    .findAll(specification, Sort.by(getDirection(sortField.direction), sortField.getFieldInTable())));
        else
            reports = Optional.of(reportRepository
                    .findAll(specification));

        return reports.orElseThrow(() -> new ReportNotFoundException("No result"));
    }

    Sort.Direction getDirection(String direction) {
        return direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    static Specification<Report> hasId(Long id) {

        return (reportRoot, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(reportRoot.get("user"), id);
    }

    static Specification<Report> hasDate(LocalDate date) {

        return (reportRoot, criteriaQuery, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(reportRoot.get("reportDate"), Date.valueOf(date));
        };
    }

    static Specification<Report> hasPeriod(TaxPeriod taxPeriod) {
        return (reportRoot, criteriaQuery, criteriaBuilder) -> {
            if (taxPeriod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(reportRoot.get("taxPeriod"), taxPeriod);
        };
    }

    static Specification<Report> hasStatus(Status status) {
        return (reportRoot, criteriaQuery, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(reportRoot.get("status"), status);
        };
    }
}
