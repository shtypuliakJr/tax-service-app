package com.taxserviceapp.data.dao;

import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//ToDo: type convert problem
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<List<Report>> findReportsByUser_Id(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByReportDate(Long userId);
}
