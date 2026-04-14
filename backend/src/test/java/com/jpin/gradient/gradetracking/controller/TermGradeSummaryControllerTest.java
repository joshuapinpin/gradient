package com.jpin.gradient.gradetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;
import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.term.TermGradeSummaryController;
import com.jpin.gradient.gradetracking.gradesummary.term.TermGradeSummaryService;
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
public class TermGradeSummaryControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TermGradeSummaryService termGradeSummaryService;

    @InjectMocks
    private TermGradeSummaryController termGradeSummaryController;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(termGradeSummaryController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getAverageGrade() throws Exception {
        Long termId = 1L;
        TermGradeSimpleSummary summary = new TermGradeSimpleSummary();
        summary.setTermId(termId);
        summary.setAverageGrade(BigDecimal.valueOf(82.5));
        summary.setAverageGpa(BigDecimal.valueOf(7.5));
        summary.setClassification("A-");

        Mockito.when(termGradeSummaryService.getSimpleSummary(termId)).thenReturn(summary);

        mockMvc.perform(get("/api/grade-summaries/term/{termId}/average", termId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termId").value(termId))
                .andExpect(jsonPath("$.averageGrade").value(82.5))
                .andExpect(jsonPath("$.averageGpa").value(7.5))
                .andExpect(jsonPath("$.classification").value("A-"));
    }
}
