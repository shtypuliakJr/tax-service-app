package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.exceptions.ReportNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Report findReportById(Long id) throws ReportNotFoundException{
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent())
            return report.get();

        throw new ReportNotFoundException("No report found");
    }

    @Transactional
    public void deleteReport(Long id) {
        System.out.println(id);
        reportRepository.deleteById(id);
    }


}
