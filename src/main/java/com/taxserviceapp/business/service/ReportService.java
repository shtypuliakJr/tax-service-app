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

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    public final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report saveNewReport(Report report) {
        return reportRepository.save(report);
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

        return reportRepository.save(report);
    }

    public List<ReportDTO> getReportsByRequestParam(Long id, Date reportDate, TaxPeriod period,
                                                    Status status, SortField sortField) throws NoReportsFoundException {

        Specification<Report> specification = Specification
                .where(filterField(id, "user")
                        .and(filterField(status, "status"))
                        .and(filterField(reportDate, "reportDate"))
                        .and(filterField(period, "taxPeriod")));

        return Optional.of(Optional.ofNullable(sortField)
                        .map(sortF -> reportRepository.findAll(specification,
                                Sort.by(getDirection(sortField.direction), sortField.getFieldInTable())))
                        .orElse(reportRepository.findAll(specification)))
                .filter(reportList -> !reportList.isEmpty())
                .map((reports) -> reports.stream()
                        .map(PojoConverter::convertReportEntityToDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoReportsFoundException("No reports found"));
    }

    Sort.Direction getDirection(String direction) {
        return direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    protected  <T> Specification<Report> filterField(T param, String fieldName) {
        return (reportRoot, criteriaQuery, criteriaBuilder) -> {

            return Optional.ofNullable(param)
                    .map((p) -> criteriaBuilder.equal(reportRoot.get(fieldName), param))
                    .orElseGet(criteriaBuilder::conjunction);
//            if (param == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(reportRoot.get(fieldName), param);
        };
    }
}