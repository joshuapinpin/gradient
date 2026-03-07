package com.jpin.gradient;

import com.jpin.gradient.model.Course;
import com.jpin.gradient.repository.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    private Long courseId;

    @BeforeEach
    void setUp() {
        Course course = new Course();
        course.setName("Test Course");
        courseId = courseRepository.save(course).getId();
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    private String validAssessmentJson() {
        return """
        {
            "name": "Homework 1",
            "assessmentType": "HOMEWORK",
            "weight": 20.0,
            "courseId": %d
        }
        """.formatted(courseId);
    }

    private String validAssessmentResponse() throws Exception {
        return mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validAssessmentJson()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void createAssessment() throws Exception {
        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validAssessmentJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Homework 1"))
                .andExpect(jsonPath("$.assessmentType").value("HOMEWORK"))
                .andExpect(jsonPath("$.weight").value(20.0))
                .andExpect(jsonPath("$.grade").isEmpty())
                .andExpect(jsonPath("$.dueDate").isEmpty())
                .andExpect(jsonPath("$.graded").value(false));
    }

    @Test
    void createAssessment_InvalidWeight() throws Exception {
        String json = """
        {
            "name": "Final",
            "assessmentType": "EXAM",
            "weight": 150.0
        }
        """;

        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssessment_MissingName() throws Exception {
        String json = """
        {
            "assessmentType": "EXAM",
            "weight": 20.0
        }
        """;

        mockMvc.perform(post("/api/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void gradeAssessment() throws Exception {
        String json = """
        {
            "grade": 85.5
        }
        """;

        String createResponse = validAssessmentResponse();

        Number assessmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assessmentId = assessmentIdNumber.longValue();

        mockMvc.perform(post("/api/assessments/{id}/grade", assessmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId))
                .andExpect(jsonPath("$.grade").value(85.5))
                .andExpect(jsonPath("$.graded").value(true));
    }

    @Test
    void updateAssessment() throws Exception {
        String createResponse = validAssessmentResponse();

        Number assessmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assessmentId = assessmentIdNumber.longValue();

        String updateJson = """
        {
            "name": "Homework 1",
            "assessmentType": "QUIZ",
            "weight": 25.0,
            "dueDate": "2026-03-20T23:59:00"
        }
        """;

        mockMvc.perform(put("/api/assessments/{id}", assessmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId))
                .andExpect(jsonPath("$.name").value("Homework 1"))
                .andExpect(jsonPath("$.assessmentType").value("QUIZ"))
                .andExpect(jsonPath("$.weight").value(25.0))
                .andExpect(jsonPath("$.dueDate").value("2026-03-20T23:59:00"))
                .andExpect(jsonPath("$.grade").isEmpty())
                .andExpect(jsonPath("$.graded").value(false));
    }

    @Test
    void updateAssessment_changeGrade() throws Exception{
        String createResponse = validAssessmentResponse();

        Number assessmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assessmentId = assessmentIdNumber.longValue();

        String gradeJson = """
        {
            "grade": 92.0
        }
        """;

        mockMvc.perform(post("/api/assessments/{id}/grade", assessmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gradeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId))
                .andExpect(jsonPath("$.name").value("Homework 1"))      // unchanged
                .andExpect(jsonPath("$.assessmentType").value("HOMEWORK")) // unchanged
                .andExpect(jsonPath("$.weight").value(20.0))            // unchanged
                .andExpect(jsonPath("$.grade").value(92.0))             // changed
                .andExpect(jsonPath("$.graded").value(true));
    }

}
