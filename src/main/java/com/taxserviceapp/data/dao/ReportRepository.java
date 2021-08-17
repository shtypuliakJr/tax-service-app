package com.taxserviceapp.data.dao;

import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<List<Report>> findReportsByUser_Id(Long userId);

    Optional<List<Report>> findReportsByUser_IdOrderByReportDate(Long userId);

//    @Modifying
//    @Query("UPDATE Report r SET r.income = :report. where r.id = :id")
//    void updateReport(@Param(value = "id") Long id,
//                      @Param(value = "report") Report report);
//    @Modifying
//    @Query("DELETE FROM Report r WHERE r.id = :id")
//    void deleteById(@Param("id") Long id);
}
