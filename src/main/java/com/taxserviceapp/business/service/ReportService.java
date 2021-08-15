package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    public ReportRepository reportRepository;

    public void saveNewReport(Report report) {
        reportRepository.save(report);
    }
}
