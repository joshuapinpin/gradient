package com.gradient.gradetracker.controller;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gradient.gradetracker.model.Grade;
import com.gradient.gradetracker.service.GradeService;

@WebMvcTest(GradeController.class)
public class GradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private GradeController gradeController;
    private GradeService gradeService;

    @Test
    public void testGetAllGrades() throws Exception {
        Grade grade1 = new Grade();
        grade1.setId(1L);
        Grade grade2 = new Grade();
        grade2.setId(2L);
        List<Grade> grades = Arrays.asList(grade1, grade2);
        Mockito.when(gradeService.getAllGrades()).thenReturn(grades);

        mockMvc.perform(get("/api/grades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
