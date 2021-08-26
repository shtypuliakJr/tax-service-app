package com.taxserviceapp.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@Table(name = "report")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_period")
    @Enumerated(EnumType.STRING)
    private TaxPeriod taxPeriod;

    @Column(name = "year")
    private Integer year;

    @Column(name ="income")
    private Integer income;

    @Column(name = "tax_rate")
    private Integer taxRate;

    @Column(name = "report_date")
    private Date reportDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "comment")
    private String comment;

}
