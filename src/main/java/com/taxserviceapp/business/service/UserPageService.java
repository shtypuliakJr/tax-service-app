package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

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

    public List<Report> getReportsSortedByIncome(Long userId) throws NoResultException {
        return reportRepository
                .findReportsByUser_IdOrderByIncome(userId)
                .orElseThrow(() -> new NoResultException("No result"));
    }
}
