package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
