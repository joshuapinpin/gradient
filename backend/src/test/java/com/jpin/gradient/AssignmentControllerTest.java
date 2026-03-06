package com.jpin.gradient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String validAssignmentJson() {
        return """
        {
            "name": "Homework 1",
            "assignmentType": "HOMEWORK",
            "weight": 20.0
        }
        """;
    }

    private String validAssignmentResponse() throws Exception {
        return mockMvc.perform(post("/api/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validAssignmentJson()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void createAssignment() throws Exception {
        String json = """
        {
            "name": "Midterm",
            "assignmentType": "EXAM",
            "weight": 30.0
        }
        """;

        mockMvc.perform(post("/api/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Midterm"))
                .andExpect(jsonPath("$.assignmentType").value("EXAM"))
                .andExpect(jsonPath("$.weight").value(30.0))
                .andExpect(jsonPath("$.grade").isEmpty())
                .andExpect(jsonPath("$.dueDate").isEmpty())
                .andExpect(jsonPath("$.graded").value(false));
    }

    @Test
    void createAssignment_InvalidWeight() throws Exception {
        String json = """
        {
            "name": "Final",
            "assignmentType": "EXAM",
            "weight": 150.0
        }
        """;

        mockMvc.perform(post("/api/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssignment_MissingName() throws Exception {
        String json = """
        {
            "assignmentType": "EXAM",
            "weight": 20.0
        }
        """;

        mockMvc.perform(post("/api/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void gradeAssignment() throws Exception {
        String json = """
        {
            "grade": 85.5
        }
        """;

        String createResponse = validAssignmentResponse();

        Number assignmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assignmentId = assignmentIdNumber.longValue();

        mockMvc.perform(post("/api/assignments/{id}/grade", assignmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignmentId))
                .andExpect(jsonPath("$.grade").value(85.5))
                .andExpect(jsonPath("$.graded").value(true));
    }

    @Test
    void updateAssignment() throws Exception {
        String createResponse = validAssignmentResponse();

        Number assignmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assignmentId = assignmentIdNumber.longValue();

        String updateJson = """
        {
            "name": "Homework 1",
            "assignmentType": "QUIZ",
            "weight": 25.0,
            "dueDate": "2026-03-20T23:59:00"
        }
        """;

        mockMvc.perform(put("/api/assignments/{id}", assignmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignmentId))
                .andExpect(jsonPath("$.name").value("Homework 1"))
                .andExpect(jsonPath("$.assignmentType").value("QUIZ"))
                .andExpect(jsonPath("$.weight").value(25.0))
                .andExpect(jsonPath("$.dueDate").value("2026-03-20T23:59:00"))
                .andExpect(jsonPath("$.grade").isEmpty())
                .andExpect(jsonPath("$.graded").value(false));
    }

    @Test
    void updateAssignment_changeGrade() throws Exception{
        String createResponse = validAssignmentResponse();

        Number assignmentIdNumber = JsonPath.read(createResponse, "$.id");
        long assignmentId = assignmentIdNumber.longValue();

        String gradeJson = """
        {
            "grade": 92.0
        }
        """;

        mockMvc.perform(post("/api/assignments/{id}/grade", assignmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gradeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignmentId))
                .andExpect(jsonPath("$.name").value("Homework 1"))      // unchanged
                .andExpect(jsonPath("$.assignmentType").value("HOMEWORK")) // unchanged
                .andExpect(jsonPath("$.weight").value(20.0))            // unchanged
                .andExpect(jsonPath("$.grade").value(92.0))             // changed
                .andExpect(jsonPath("$.graded").value(true));
    }

}
