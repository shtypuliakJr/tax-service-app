package com.taxserviceapp.web.dto;

import com.taxserviceapp.data.entity.*;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;

// ToDo: add normal income validation
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    Long id;

    @NotNull(message = "Require income")
//    @Pattern(regexp = "^[1-9][0-9]{1,6}$",message = "Income must start from non zero and have maximum 7 digits")
//    @Min(value = 1, message = "greater than 0")
//    @Max(value = 9999, message = "less than 10000")
    private Integer income;

    @NotNull(message = "Require rate")
    private Integer taxRate;

    @NotNull
    private TaxPeriod taxPeriod;

    @NotNull(message = "Require year")
    @Min(value = 2010, message = "Set year after 2010")
    @Max(value = 2021, message = "Year is not valid")
    private Integer year;

    private User user;
    private Long userId;

    Date reportDate;

    private Status status;

    private String comment;

//    @NotNull(message = "Require form type")
//    private Form form;


}
