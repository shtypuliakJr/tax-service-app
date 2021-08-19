package com.taxserviceapp.web.dto;

import com.taxserviceapp.data.entity.Status;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@ToString
public class ReportFilterDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    Status status;

    Sort sortBy;
}
