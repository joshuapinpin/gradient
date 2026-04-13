package com.jpin.gradient.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpin.gradient.core.assessment.AssessmentController;
import com.jpin.gradient.core.assessment.dto.AssessmentCreateRequest;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.core.assessment.dto.AssessmentGradeRequest;
import com.jpin.gradient.core.assessment.dto.AssessmentUpdateRequest;
import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
import com.jpin.gradient.core.assessment.AssessmentType;
import com.jpin.gradient.core.assessment.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AssessmentControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    @BeforeEach
    void setup() {
        // tells jackson how to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());

        // serialize LocalDate as ISO-8601 string instead of timestamp
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    /** ========== CREATE ASSESSMENT TESTS ========== **/

    @Test
    void createAssessment() throws Exception {
        AssessmentCreateRequest request = new AssessmentCreateRequest();
        request.setName("Test Assessment");
        request.setWeight(new BigDecimal("20.0"));
        request.setAssessmentType(AssessmentType.EXAM);
        request.setCourseId(1L);

        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        response.setName("Test Assessment");
        response.setWeight(new BigDecimal("20.0"));
        response.setAssessmentType(AssessmentType.EXAM);
        response.setCourseId(1L);

        Mockito.when(assessmentService.createAssessment(any(AssessmentCreateRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Assessment"))
                .andExpect(jsonPath("$.weight").value(20.0))
                .andExpect(jsonPath("$.assessmentType").value("EXAM"))
                .andExpect(jsonPath("$.courseId").value(1L));
    }

    @Test
    void createAssessment_missingRequiredFields() throws Exception {
        AssessmentCreateRequest request = new AssessmentCreateRequest();
        // missing name, weight, assessmentType, courseId

        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssessment_invalidWeight() throws Exception {
        AssessmentCreateRequest request = new AssessmentCreateRequest();
        request.setName("Invalid Weight Assessment");
        request.setWeight(new BigDecimal("-5.0")); // Invalid negative weight
        request.setAssessmentType(AssessmentType.HOMEWORK);
        request.setCourseId(1L);

        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssessment_weightExceedsMax() throws Exception {
        AssessmentCreateRequest request = new AssessmentCreateRequest();
        request.setName("Excessive Weight Assessment");
        request.setWeight(new BigDecimal("150.0")); // Invalid weight exceeding 100%
        request.setAssessmentType(AssessmentType.PROJECT);
        request.setCourseId(1L);

        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /** ========== READ ASSESSMENT TESTS ========== **/

    @Test
    void getAssessmentById() throws Exception {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        response.setName("Test Assessment");
        response.setWeight(new BigDecimal("20.0"));
        response.setAssessmentType(AssessmentType.EXAM);
        response.setCourseId(1L);

        Mockito.when(assessmentService.getAssessmentById(1L)).thenReturn(response);
        mockMvc.perform(get("/api/assessments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Assessment"))
                .andExpect(jsonPath("$.weight").value(20.0))
                .andExpect(jsonPath("$.assessmentType").value("EXAM"))
                .andExpect(jsonPath("$.courseId").value(1L));
    }

    @Test
    void getAssessmentById_notFound() throws Exception {
        Mockito.when(assessmentService.getAssessmentById(999L)).thenThrow(new ResourceNotFoundException("Assessment not found"));
        mockMvc.perform(get("/api/assessments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAssessments() throws Exception {
        AssessmentResponse response1 = new AssessmentResponse();
        response1.setId(1L);
        response1.setName("Test Assessment 1");
        response1.setWeight(new BigDecimal("20.0"));
        response1.setAssessmentType(AssessmentType.EXAM);
        response1.setCourseId(1L);

        AssessmentResponse response2 = new AssessmentResponse();
        response2.setId(2L);
        response2.setName("Test Assessment 2");
        response2.setWeight(new BigDecimal("30.0"));
        response2.setAssessmentType(AssessmentType.HOMEWORK);
        response2.setCourseId(1L);

        Mockito.when(assessmentService.getAssessments()).thenReturn(List.of(response1, response2));
        mockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Assessment 1"))
                .andExpect(jsonPath("$[0].weight").value(20.0))
                .andExpect(jsonPath("$[0].assessmentType").value("EXAM"))
                .andExpect(jsonPath("$[0].courseId").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Test Assessment 2"))
                .andExpect(jsonPath("$[1].weight").value(30.0))
                .andExpect(jsonPath("$[1].assessmentType").value("HOMEWORK"))
                .andExpect(jsonPath("$[1].courseId").value(1L));
    }

    @Test
    void getAllAssessments_empty() throws Exception {
        Mockito.when(assessmentService.getAssessments()).thenReturn(List.of());
        mockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAssessmentsByCourseId() throws Exception {
        AssessmentResponse response1 = new AssessmentResponse();
        response1.setId(1L);
        response1.setName("Course 1 Assessment");
        response1.setWeight(new BigDecimal("20.0"));
        response1.setAssessmentType(AssessmentType.EXAM);
        response1.setCourseId(1L);

        AssessmentResponse response2 = new AssessmentResponse();
        response2.setId(2L);
        response2.setName("Course 1 Assessment 2");
        response2.setWeight(new BigDecimal("30.0"));
        response2.setAssessmentType(AssessmentType.HOMEWORK);
        response2.setCourseId(1L);

        Mockito.when(assessmentService.getAssessmentsByCourseId(1L)).thenReturn(List.of(response1, response2));
        mockMvc.perform(get("/api/assessments/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Course 1 Assessment"))
                .andExpect(jsonPath("$[0].weight").value(20.0))
                .andExpect(jsonPath("$[0].assessmentType").value("EXAM"))
                .andExpect(jsonPath("$[0].courseId").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Course 1 Assessment 2"))
                .andExpect(jsonPath("$[1].weight").value(30.0))
                .andExpect(jsonPath("$[1].assessmentType").value("HOMEWORK"))
                .andExpect(jsonPath("$[1].courseId").value(1L));
    }

    @Test
    void getAssessmentsByCourseId_noAssessments() throws Exception {
        Mockito.when(assessmentService.getAssessmentsByCourseId(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/assessments/course/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAssessmentsByCourseId_courseNotFound() throws Exception {
        Mockito.when(assessmentService.getAssessmentsByCourseId(999L)).thenThrow(new ResourceNotFoundException("Course not found"));
        mockMvc.perform(get("/api/assessments/course/999"))
                .andExpect(status().isNotFound());
    }

    /** ========== UPDATE ASSESSMENT TESTS ========== **/

    @Test
    void updateAssessment() throws Exception {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest();
        updateRequest.setName("Updated Assessment");
        updateRequest.setWeight(new BigDecimal("25.0"));
        updateRequest.setAssessmentType(AssessmentType.PROJECT);

        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        response.setName("Updated Assessment");
        response.setWeight(new BigDecimal("25.0"));
        response.setAssessmentType(AssessmentType.PROJECT);
        response.setCourseId(1L);

        Mockito.when(assessmentService.updateAssessment(eq(1L), any(AssessmentUpdateRequest.class))).thenReturn(response);
        mockMvc.perform(put("/api/assessments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Assessment"))
                .andExpect(jsonPath("$.weight").value(25.0))
                .andExpect(jsonPath("$.assessmentType").value("PROJECT"))
                .andExpect(jsonPath("$.courseId").value(1L));
    }

    @Test
    void updateAssessment_withDueDate() throws Exception {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest();
        updateRequest.setName("Updated Assessment with Due Date");
        updateRequest.setWeight(new BigDecimal("25.0"));
        updateRequest.setDueDate(LocalDateTime.parse("2024-12-31T23:59:59"));

        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        response.setWeight(new BigDecimal("25.0"));
        response.setName("Updated Assessment with Due Date");
        response.setDueDate(LocalDateTime.parse("2024-12-31T23:59:59"));

        Mockito.when(assessmentService.updateAssessment(eq(1L), any(AssessmentUpdateRequest.class))).thenReturn(response);
        mockMvc.perform(put("/api/assessments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Assessment with Due Date"))
                .andExpect(jsonPath("$.weight").value(25.0))
                .andExpect(jsonPath("$.dueDate").value("2024-12-31T23:59:59"));
    }

    @Test
    void updateAssessment_notFound() throws Exception {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest();
        updateRequest.setName("Updated Assessment");

        Mockito.when(assessmentService.updateAssessment(eq(999L), any(AssessmentUpdateRequest.class)))
                .thenThrow(new ResourceNotFoundException("Assessment not found"));
        mockMvc.perform(put("/api/assessments/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAssessment_invalidWeight() throws Exception {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest();
        updateRequest.setWeight(new BigDecimal("-10.0")); // Invalid negative weight

        mockMvc.perform(put("/api/assessments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAssessment_weightExceedsMax() throws Exception {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest();
        updateRequest.setWeight(new BigDecimal("150.0")); // Invalid weight exceeding 100%

        mockMvc.perform(put("/api/assessments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAssessment_grade() throws Exception{
        AssessmentGradeRequest gradeRequest = new AssessmentGradeRequest();
        gradeRequest.setGrade(new BigDecimal("85.0"));

        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        response.setName("Graded Assessment");
        response.setWeight(new BigDecimal("20.0"));
        response.setGrade(new BigDecimal("85.0"));
        response.setAssessmentType(AssessmentType.EXAM);
        response.setCourseId(1L);

        Mockito.when(assessmentService.gradeAssessment(eq(1L), any(AssessmentGradeRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post("/api/assessments/1/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Graded Assessment"))
                .andExpect(jsonPath("$.weight").value(20.0))
                .andExpect(jsonPath("$.grade").value(85.0))
                .andExpect(jsonPath("$.assessmentType").value("EXAM"))
                .andExpect(jsonPath("$.courseId").value(1L));
    }

    /** ========== DELETE ASSESSMENT TESTS ========== **/

    @Test
    void deleteAssessment() throws Exception {
        Mockito.doNothing().when(assessmentService).deleteAssessment(1L);
        mockMvc.perform(delete("/api/assessments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAssessment_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Assessment not found")).when(assessmentService).deleteAssessment(999L);
        mockMvc.perform(delete("/api/assessments/999"))
                .andExpect(status().isNotFound());
    }
}
