package com.taxserviceapp.web.dto;

import com.taxserviceapp.data.entity.*;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    Long id;

    @NotNull(message = "Require income")
    @Pattern(regexp = "^[1-9][0-9]{1,6}$",
            message = "Income must start from non zero and have minimum 2 and maximum 7 digits")
    private String income;

    @Pattern(regexp = "^[1-9][0-9]?$", message = "Require rate")
    private String taxRate;

    @NotNull
    private TaxPeriod taxPeriod;

    @NotNull(message = "Require year")
    @Min(value = 2010, message = "Set year after 2010")
    @Max(value = 2021, message = "Year is not valid")
    private Integer year;

    private Date reportDate;

    private Status status;

    private String comment;

    private User user;

    private Long userId;

}
