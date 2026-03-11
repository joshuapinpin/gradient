package com.jpin.gradient;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jpin.gradient.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void cleanUp() {
        courseRepository.deleteAll();
    }

    @Test
    void test_createCourse() throws Exception {
        String courseJson = """
        {
            "name": "Test Course"
        }
        """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Course"));
    }

    @Test
    void test_createCourse_invalidRequest() throws Exception {
        String courseJson = """
        {
            "name": ""
        }
        """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void test_getCourseById_nonExistent() throws Exception {
        mockMvc.perform(get("/api/courses/%d".formatted(9999L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getCourseById() throws Exception {
        // First create a course
        String courseJson = """
        {
            "name": "Course to Retrieve"
        }
        """;

        String response = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courseId = JsonPath.parse(response).read("$.id", Long.class);

        // Now retrieve the course
        mockMvc.perform(get("/api/courses/%d".formatted(courseId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.name").value("Course to Retrieve"));
    }

    @Test
    void test_listCourses() throws Exception {
        // Create multiple courses
        String courseJson1 = """
        {
            "name": "Course 1"
        }
        """;

        String courseJson2 = """
        {
            "name": "Course 2"
        }
        """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson2))
                .andExpect(status().isCreated());

        // Now list courses
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Course 1"))
                .andExpect(jsonPath("$[1].name").value("Course 2"));
        assert courseRepository.count() == 2;
    }

    @Test
    void test_updateCourse() throws Exception {
        // First create a course
        String courseJson = """
        {
            "name": "Original Course"
        }
        """;

        String response = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courseId = JsonPath.parse(response).read("$.id", Long.class);

        // Now update the course
        String updateJson = """
        {
            "name": "Updated Course"
        }
        """;

        mockMvc.perform(put("/api/courses/%d".formatted(courseId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.name").value("Updated Course"));
    }

    @Test
    void test_updateCourse_invalidRequest_noName() throws Exception {
        // First create a course
        String courseJson = """
        {
            "name": "Another Course"
        }
        """;

        String response = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courseId = JsonPath.parse(response).read("$.id", Long.class);

        // Now attempt to update with invalid data
        String updateJson = """
        {
            "name": ""
        }
        """;

        mockMvc.perform(put("/api/courses/%d".formatted(courseId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_updateCourse_invalidRequest_nonExistentCourse() throws Exception {
        String updateJson = """
        {
            "name": "Non-existent Course"
        }
        """;

        mockMvc.perform(put("/api/courses/%d".formatted(9999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_deleteCourse() throws Exception {
        // First create a course
        String courseJson = """
        {
            "name": "Course to Delete"
        }
        """;

        String response = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courseId = JsonPath.parse(response).read("$.id", Long.class);

        // Now delete the course
        mockMvc.perform(delete("/api/courses/%d".formatted(courseId)))
                .andExpect(status().isNoContent());

        // Verify the course is deleted
        mockMvc.perform(get("/api/courses/%d".formatted(courseId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_deleteCourse_nonExistent() throws Exception {
        mockMvc.perform(delete("/api/courses/%d".formatted(9999L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_removeAssessmentFromCourse() throws Exception {
        // First create a course
        String courseJson = """
                {
                    "name": "Course with Assessment"
                }
                """;

        String courseResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courseId = JsonPath.parse(courseResponse).read("$.id", Long.class);

        // Create an assessment for the course
        String assessmentJson = """
                {
                    "name": "Assessment to Remove",
                    "assessmentType": "HOMEWORK",
                    "weight": 10.0,
                    "courseId": %d
                }
                """.formatted(courseId);

        String assessmentResponse = mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assessmentJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long assessmentId = JsonPath.parse(assessmentResponse).read("$.id", Long.class);

        // Now remove the assessment from the course
        mockMvc.perform(delete("/api/courses/%d/assessments/%d".formatted(courseId, assessmentId)))
                .andExpect(status().isNoContent());

        // Verify the assessment is removed from the course
        String updatedCourseResponse = mockMvc.perform(get("/api/courses/%d".formatted(courseId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Object assessmentsObj = null;
        try {
            assessmentsObj = JsonPath.parse(updatedCourseResponse).read("$.assessments", Object.class);
        } catch (PathNotFoundException e) {
            assessmentsObj = null;
        }
        if (assessmentsObj != null) {
            @SuppressWarnings("unchecked")
            List<Integer> assessments = JsonPath.parse(updatedCourseResponse).read("$.assessments[*].id", List.class);
            assertFalse(assessments.contains(assessmentId.intValue()));
        } else {
            // No assessments property means no assessments, which is expected
            assertTrue(true);
        }
    }
}
