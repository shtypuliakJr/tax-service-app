package com.taxserviceapp.web.model;

import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {

    @NonNull
//    @Size(message = "Require income")
    private Integer income;

    @NonNull
//    @NotBlank(message = "Require rate")
    private Integer taxRate;

    @NonNull
//    @NotBlank(message = "Require period")
    private TaxPeriod taxPeriod;

    @NonNull
    @Min(2010)
    @Max(2021)
//    @NotBlank(message = "Require year")
    private Integer year;

    private Long userId;

    private Status status;

    private String comment;
}
