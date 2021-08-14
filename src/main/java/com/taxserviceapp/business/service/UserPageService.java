package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
public class UserPageService {

    private final ReportRepository reportRepository;

    @Autowired
    public UserPageService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    public List<Report> getReportsByUserId(Long userId) {
        System.out.println("Count " + reportRepository.count());
        System.out.println("findAll");
        System.out.println(reportRepository.findAll().isEmpty());
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public void deleteReportById(Long id) {
        reportRepository.deleteById(id);
    }
}
