package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPageService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public UserPageService(UserRepository userRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }
    public Optional<List<Report>> getReportsByUserId(Long userId) {
        return reportRepository.findReportsByUser_Id(userId);
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public void deleteReportById(Long id) {
        reportRepository.deleteById(id);
    }
}
