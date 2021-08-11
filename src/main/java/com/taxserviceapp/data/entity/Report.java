package com.taxserviceapp.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_period")
    private TaxPeriod taxPeriod;

    @Column(name = "year")
    private Integer year;

    @Column(name ="income")
    private Integer income;

    @Column(name = "tax_rate")
    private Integer taxRate; // 3 or 5 or 15%

    @Column(name = "report_date")
    private LocalDate reportDate;

    @ManyToOne
    private User user;
}
