package com.taxserviceapp.data.dao;

import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    Optional<List<Report>> findReportsByUser_Id(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByIncome(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByReportDate(Long userId);

    Optional<List<Report>> findAllByIncome(Integer income);

    Integer countAllByStatus(Status status);

    Optional<List<Report>> findAllByUser_FirstNameAndUser_LastName(String user_firstName, String user_lastName);

    Optional<List<Report>> findAllByUser_FirstNameOrUser_LastName(String user_firstName, String user_lastName);

    Optional<List<Report>> findAllById(Long parseInt);

    Optional<List<Report>> findAllByUser_Ipn(String parseInt);


}

