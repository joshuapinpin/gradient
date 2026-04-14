package com.jpin.gradient.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpin.gradient.core.course.*;
import com.jpin.gradient.core.course.dto.CourseCreateRequest;
import com.jpin.gradient.core.course.dto.CourseResponse;
import com.jpin.gradient.core.course.dto.CourseUpdateRequest;
import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private CourseService courseService;

	@InjectMocks
	private CourseController courseController;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(courseController)
				.setControllerAdvice(new ApiExceptionHandler())
				.setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
				.build();
	}

	/** ========== CREATE COURSE TESTS ========== **/
	
	@Test
	void createCourse() throws Exception {
		// Arrange
		CourseCreateRequest request = new CourseCreateRequest();
		request.setName("Test Course");
		request.setTermId(1L); // Simulate a term ID

		CourseResponse response = new CourseResponse();
		response.setId(1L);
		response.setName("Test Course");
		response.setTermId(1L);

		Mockito.when(courseService.createCourse(any(CourseCreateRequest.class))).thenReturn(response);
		mockMvc.perform(post("/api/courses") // <-- updated endpoint
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Test Course"))
				.andExpect(jsonPath("$.termId").value(1L));
	}

	@Test
	void createCourse_invalidName() throws Exception{
		CourseCreateRequest request = new CourseCreateRequest();
		request.setName(""); // Invalid name
		request.setTermId(1L); // Simulate a term ID

		mockMvc.perform(post("/api/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createCourse_missingTermId() throws Exception {
		CourseCreateRequest request = new CourseCreateRequest();
		request.setName("Test Course");
		// Missing termId

		mockMvc.perform(post("/api/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	/** ========== READ COURSE TESTS ========== **/

	@Test
	void getCourseById() throws Exception {
		CourseResponse response = new CourseResponse();
		response.setId(1L);
		response.setName("Test Course");
		response.setTermId(1L);

		Mockito.when(courseService.getCourseById(1L)).thenReturn(response);

		mockMvc.perform(get("/api/courses/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Test Course"))
				.andExpect(jsonPath("$.termId").value(1L));
	}

	@Test
	void getCourseById_notFound() throws Exception{
		Mockito.when(courseService.getCourseById(999L)).thenThrow(new ResourceNotFoundException("Course not found with id: 999"));

		mockMvc.perform(get("/api/courses/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Course not found with id: 999"));
	}

	@Test
	void getAllCourses() throws Exception {
		CourseResponse course1 = new CourseResponse();
		course1.setId(1L);
		course1.setName("Course 1");
		course1.setTermId(1L);

		CourseResponse course2 = new CourseResponse();
		course2.setId(2L);
		course2.setName("Course 2");
		course2.setTermId(1L);

		Mockito.when(courseService.getCourses()).thenReturn(List.of(course1, course2));

		mockMvc.perform(get("/api/courses"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Course 1"))
				.andExpect(jsonPath("$[0].termId").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].name").value("Course 2"))
				.andExpect(jsonPath("$[1].termId").value(1L));
	}

	@Test
	void getAllCourses_empty() throws Exception {
		Mockito.when(courseService.getCourses()).thenReturn(List.of());

		mockMvc.perform(get("/api/courses"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0))
				.andExpect(content().json("[]"));
	}

	@Test
	void getCoursesByTermId() throws Exception {
		CourseResponse course1 = new CourseResponse();
		course1.setId(1L);
		course1.setName("Course 1");
		course1.setTermId(1L);

		CourseResponse course2 = new CourseResponse();
		course2.setId(2L);
		course2.setName("Course 2");
		course2.setTermId(1L);

		Mockito.when(courseService.getCoursesByTermId(1L)).thenReturn(List.of(course1, course2));

		mockMvc.perform(get("/api/courses/term/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Course 1"))
				.andExpect(jsonPath("$[0].termId").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].name").value("Course 2"))
				.andExpect(jsonPath("$[1].termId").value(1L));
	}

	@Test
	void getCoursesByTermId_noCourses() throws Exception {
		Mockito.when(courseService.getCoursesByTermId(1L)).thenReturn(List.of());
		mockMvc.perform(get("/api/courses/term/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void getCoursesByTermId_termNotFound() throws Exception {
		Mockito.when(courseService.getCoursesByTermId(999L)).thenThrow(new ResourceNotFoundException("Term not found with id: 999"));

		mockMvc.perform(get("/api/courses/term/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Term not found with id: 999"));
	}


	/** ========== UPDATE COURSE TESTS ========== **/

	@Test
	void updateCourse() throws Exception{
		CourseUpdateRequest updateRequest = new CourseUpdateRequest();
		updateRequest.setName("Updated Course");

		CourseResponse response = new CourseResponse();
		response.setId(1L);
		response.setName("Updated Course");
		response.setTermId(1L);

		Mockito.when(courseService.updateCourse(any(Long.class), any())).thenReturn(response);

		mockMvc.perform(put("/api/courses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Updated Course"))
				.andExpect(jsonPath("$.termId").value(1L));
	}

	@Test
	void updateCourse_notFound() throws Exception {
		CourseUpdateRequest updateRequest = new CourseUpdateRequest();
		updateRequest.setName("Updated Course");

		Mockito.when(courseService.updateCourse(any(Long.class), any())).thenThrow(new ResourceNotFoundException("Course not found with id: 999"));

		mockMvc.perform(put("/api/courses/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Course not found with id: 999"));
	}
	
	/** ========== DELETE COURSE TESTS ========== **/

	@Test
	void deleteCourse() throws Exception {
		mockMvc.perform(delete("/api/courses/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteCourse_notFound() throws Exception {
		Mockito.doThrow(new ResourceNotFoundException("Course not found with id: 999")).when(courseService).deleteCourse(999L);

		mockMvc.perform(delete("/api/courses/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Course not found with id: 999"));
	}

	@Test
	void removeAssessmentFromCourse() throws Exception {
		mockMvc.perform(delete("/api/courses/1/assessments/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void removeAssessmentFromCourse_notFound() throws Exception {
		Mockito.doThrow(new ResourceNotFoundException("Course not found with id: 999")).when(courseService).removeAssessmentFromCourse(999L, 1L);

		mockMvc.perform(delete("/api/courses/999/assessments/1"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Course not found with id: 999"));
	}

}
