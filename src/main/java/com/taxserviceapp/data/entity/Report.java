package com.taxserviceapp.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@NoArgsConstructor
@Data
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
    private Integer taxRate; // 3 or 5 or 15%

    @Column(name = "report_date")
    private Date reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private String problems;
//
    @Column(name = "comment")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", taxPeriod=" + taxPeriod +
                ", year=" + year +
                ", income=" + income +
                ", taxRate=" + taxRate +
                ", reportDate=" + reportDate +
                ", user=" + user +
                ", problems='" + problems + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
