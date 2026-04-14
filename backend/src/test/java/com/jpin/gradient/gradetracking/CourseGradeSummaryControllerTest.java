package com.jpin.gradient.gradetracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryController;
import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryService;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeFullSummary;
import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;
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
public class CourseGradeSummaryControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CourseGradeSummaryService courseGradeSummaryService;

    @InjectMocks
    private CourseGradeSummaryController courseGradeSummaryController;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(courseGradeSummaryController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getFullSummary() throws Exception {
        Long courseId = 1L;
        CourseGradeFullSummary summary = new CourseGradeFullSummary();
        summary.setCourseId(courseId);
        summary.setAverageGrade(BigDecimal.valueOf(83.5)); // Example value
        summary.setAverageGpa(BigDecimal.valueOf(7.0));    // Example value
        summary.setClassification("A-");
        summary.setMinPossibleGrade(BigDecimal.valueOf(60));
        summary.setMaxPossibleGrade(BigDecimal.valueOf(95));
        // Add more fields if needed

        Mockito.when(courseGradeSummaryService.getFullSummary(courseId)).thenReturn(summary);

        mockMvc.perform(get("/api/grade-summaries/course/{courseId}/full-summary", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.averageGrade").value(83.5))
                .andExpect(jsonPath("$.averageGpa").value(7.0))
                .andExpect(jsonPath("$.classification").value("A-"));
    }

    @Test
    void getSimpleSummary() throws Exception {
        Long courseId = 1L;
        CourseGradeSimpleSummary simpleSummary = new CourseGradeSimpleSummary();
        simpleSummary.setCourseId(courseId);
        simpleSummary.setAverageGrade(BigDecimal.valueOf(83.5)); // Example value
        simpleSummary.setAverageGpa(BigDecimal.valueOf(7.0));    // Example value
        simpleSummary.setClassification("A-");

        Mockito.when(courseGradeSummaryService.getSimpleSummary(courseId)).thenReturn(simpleSummary);

        mockMvc.perform(get("/api/grade-summaries/course/{courseId}/average", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.averageGrade").value(83.5))
                .andExpect(jsonPath("$.averageGpa").value(7.0))
                .andExpect(jsonPath("$.classification").value("A-"));
    }
}
