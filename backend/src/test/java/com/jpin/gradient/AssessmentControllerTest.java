package com.jpin.gradient;

import com.jpin.gradient.model.Course;
import com.jpin.gradient.repository.AssessmentRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private AssessmentRepository assessmentRepository;

    private Long courseId;

    @BeforeEach
    void setUp() {
        Course course = new Course();
        course.setName("Test Course");
        courseId = courseRepository.save(course).getId();
    }

    @AfterEach
    void tearDown() {
        assessmentRepository.deleteAll();
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
    void test_createAssessment() throws Exception {
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
    void test_createAssessment_InvalidWeight() throws Exception {
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
    void test_createAssessment_emptyName() throws Exception{
        String json = """
        {
            "name": "",
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
    void test_createAssessment_MissingName() throws Exception {
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
    void test_gradeAssessment() throws Exception {
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
    void test_updateAssessment() throws Exception {
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
    void test_updateAssessment_changeGrade() throws Exception{
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

    @Test
    void test_removeAssessmentFromCourse_deletesAssessment() throws Exception {
        // Create assessment
        String createResponse = validAssessmentResponse();
        Number assessmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assessmentId = assessmentIdNumber.longValue();

        // Confirm assessment exists
        assert assessmentRepository.findById(assessmentId).isPresent();

        // Delete assessment via course endpoint
        mockMvc.perform(delete("/api/courses/{courseId}/assessments/{assessmentId}",
                        courseId, assessmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Confirm assessment is deleted
        assert assessmentRepository.findById(assessmentId).isEmpty();

        // Confirm no assessments remain for the course
        assert assessmentRepository.countByCourseId(courseId) == 0;

        // Confirm course still exists
        assert courseRepository.findById(courseId).isPresent();
    }

}
