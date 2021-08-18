package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public Report findReportById(Long id) throws ReportNotFoundException {

        return reportRepository
                .findById(id)
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
}