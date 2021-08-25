package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    public final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void saveNewReport(Report report) {
        reportRepository.save(report);
    }

    public ReportDTO findReportById(Long id) throws ReportNotFoundException {

        return reportRepository.findById(id)
                .map(PojoConverter::convertReportEntityToDTO)
                .orElseThrow(() -> new ReportNotFoundException("No report found"));
    }

    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    @Transactional
    public Report updateReport(Report report) throws ReportNotFoundException {
        if (reportRepository.existsById(report.getId())) {
            return reportRepository.save(report);
        } else {
            throw new ReportNotFoundException("No report found");
        }
    }

    public List<Report> getReportsByRequestParam(Long id, Date reportDate, TaxPeriod period,
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

        return reports.orElseThrow(() -> new NoReportsFoundException("No reports found by parameters"));
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