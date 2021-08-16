package com.taxserviceapp.web.model;

import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

// ToDo: add normal income validation
@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {

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
    @Min(value = 2010)
    @Max(value = 2021, message = "Year is not valid")
    private Integer year;

    private Long userId;

    private Status status;

    private String comment;
}
