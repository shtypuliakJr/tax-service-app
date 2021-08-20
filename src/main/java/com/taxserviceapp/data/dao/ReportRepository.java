package com.taxserviceapp.data.dao;

import com.taxserviceapp.data.entity.Report;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    Optional<List<Report>> findReportsByUser_Id(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByIncome(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByReportDate(Long userId);

    Optional<List<Report>> findAllByIncome(Integer income);

}
