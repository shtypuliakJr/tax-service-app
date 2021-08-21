package com.taxserviceapp.utility;

import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.data.entity.UserRole;
import com.taxserviceapp.web.dto.ReportDTO;
import com.taxserviceapp.web.dto.UserDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PojoConverter {

    public static User convertUserDtoToEntity(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .age(userDTO.getAge())
                .ipn(userDTO.getIpn())
                .userRole(UserRole.USER)
                .dateOfRegistration(Date.valueOf(LocalDate.now()))
                .address(userDTO.getAddress())
                .personality(userDTO.getPersonality())
                .enabled(true)
                .build();
    }

    public static ReportDTO convertReportEntityToDTO(Report report) {

        return ReportDTO.builder()
                .id(report.getId())
                .income(report.getIncome())
                .taxRate(report.getTaxRate())
                .taxPeriod(report.getTaxPeriod())
                .year(report.getYear())
                .status(report.getStatus())
                .reportDate(report.getReportDate())
                .comment(report.getComment())
                .build();
    }

    public static Report convertReportDTOToEntity(ReportDTO reportDTO, User user) {

        return Report.builder()
                .id(reportDTO.getId())
                .income(reportDTO.getIncome())
                .taxRate(reportDTO.getTaxRate())
                .taxPeriod(reportDTO.getTaxPeriod())
                .year(reportDTO.getYear())
                .status(Status.PROCESSING)
                .reportDate(Date.valueOf(LocalDate.now()))
                .user(user)
                .build();
    }
}
