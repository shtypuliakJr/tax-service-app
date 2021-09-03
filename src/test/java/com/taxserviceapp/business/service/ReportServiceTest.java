package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.ReportRepository;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.Status;
import com.taxserviceapp.data.entity.TaxPeriod;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.NoReportsFoundException;
import com.taxserviceapp.web.dto.ReportDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    @Test
    public void save_report_test() {

        Report report = Report.builder()
                .id(1L)
                .income(1000)
                .taxRate(12)
                .status(Status.PROCESSING)
                .taxPeriod(TaxPeriod.FIRST_PERIOD)
                .year(2019)
                .user(new User())
                .comment("new comment")
                .build();

        Mockito.when(reportRepository.save(report)).thenReturn(report);
        assertThat(reportService.saveNewReport(report)).isEqualTo(report);

    }

    @Test
    public void direction_test() {
        Sort.Direction dir = reportService.getDirection("asc");
        assertEquals(Sort.Direction.ASC, dir);
    }

}