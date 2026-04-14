package com.jpin.gradient.gradetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;
import com.jpin.gradient.gradetracking.gradesummary.summary.YearGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.year.YearGradeSummaryController;
import com.jpin.gradient.gradetracking.gradesummary.year.YearGradeSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class YearGradeSummaryControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private YearGradeSummaryService yearGradeSummaryService;

    @InjectMocks
    private YearGradeSummaryController yearGradeSummaryController;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(yearGradeSummaryController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getAverageGrade() throws Exception {
        Long yearId = 1L;

        YearGradeSimpleSummary summary = new YearGradeSimpleSummary();
        summary.setYearId(yearId);
        summary.setAverageGrade(BigDecimal.valueOf(85.0));
        summary.setAverageGpa(BigDecimal.valueOf(8.0));
        summary.setClassification("A");

        Mockito.when(yearGradeSummaryService.getSimpleSummary(yearId)).thenReturn(summary);

        mockMvc.perform(get("/api/grade-summary/year/{yearId}/average", yearId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearId").value(yearId))
                .andExpect(jsonPath("$.averageGrade").value(85.0))
                .andExpect(jsonPath("$.averageGpa").value(8.0))
                .andExpect(jsonPath("$.classification").value("A"));
    }
}
