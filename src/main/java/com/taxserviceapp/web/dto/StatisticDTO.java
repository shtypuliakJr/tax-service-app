package com.taxserviceapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StatisticDTO {

    private Long countOfUsers;
    private Long countOfReports;
    private Long countOfInspectors;

    private Integer processingReports;
    private Integer approvedReports;
    private Integer disapprovedReports;

    Map<Integer, Long> countReportsPerYear;
}
