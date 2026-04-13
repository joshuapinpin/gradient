package com.jpin.gradient.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpin.gradient.core.term.*;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
import com.jpin.gradient.core.term.dto.TermCreateRequest;
import com.jpin.gradient.core.term.dto.TermResponse;
import com.jpin.gradient.core.term.dto.TermUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jpin.gradient.core.shared.exception.ApiExceptionHandler;

@ExtendWith(MockitoExtension.class)
class TermControllerTest {
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private TermService termService;

	@InjectMocks
	private TermController termController;

	@BeforeEach
	void setup() {
		// tells jackson how to handle Java 8 date/time types
		objectMapper.registerModule(new JavaTimeModule());

		// serialize LocalDate as ISO-8601 string instead of timestamp
		objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		mockMvc = MockMvcBuilders.standaloneSetup(termController)
				.setControllerAdvice(new ApiExceptionHandler())
				.setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
				.build();
	}

    /** ========== CREATE TESTS ==========  **/
    @Test
    void createTerm() throws Exception{
        TermCreateRequest req = new TermCreateRequest();
        req.setName("Spring 2026");
		req.setYearId(1L); // Set required yearId field
        TermResponse resp = new TermResponse();
        resp.setId(1L);
        resp.setName("Spring 2026");
        Mockito.when(termService.createTerm(any())).thenReturn(resp);
        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Spring 2026"));
    }

    @Test
    void createTerm_withDates() throws Exception{
        TermCreateRequest req = new TermCreateRequest();
        req.setName("Fall 2026");
		req.setYearId(1L); // Set required yearId field
        req.setStartDate(LocalDate.of(2026, 9, 1));
        req.setEndDate(LocalDate.of(2026, 12, 31));
        TermResponse resp = new TermResponse();
        resp.setId(2L);
        resp.setName("Fall 2026");
		resp.setStartDate(LocalDate.of(2026, 9, 1));
		resp.setEndDate(LocalDate.of(2026, 12, 31));
        Mockito.when(termService.createTerm(any())).thenReturn(resp);
        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Fall 2026"))
				.andExpect(jsonPath("$.startDate").value("2026-09-01"))
				.andExpect(jsonPath("$.endDate").value("2026-12-31"));
    }

    @Test
    void createTerm_invalidName() throws Exception {
        TermCreateRequest req = new TermCreateRequest();
        // Missing name
        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTerm_invalidDates() throws Exception {
        TermCreateRequest req = new TermCreateRequest();
        req.setName("Invalid Term");
        req.setStartDate(LocalDate.of(2026, 12, 31));
        req.setEndDate(LocalDate.of(2026, 9, 1)); // End date before start date
        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    /** ========== READ TESTS ==========  **/

	@Test
	void getTermById() throws Exception {
		TermResponse resp = new TermResponse();
		resp.setId(1L);
		resp.setName("Spring 2026");
		Mockito.when(termService.getTermById(1L)).thenReturn(resp);
		mockMvc.perform(get("/api/terms/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	void getTermById_notFound() throws Exception {
		Mockito.when(termService.getTermById(999L)).thenThrow(new ResourceNotFoundException("Term not found"));
		mockMvc.perform(get("/api/terms/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void getAllTerms() throws Exception {
		Mockito.when(termService.getTerms()).thenReturn(Collections.emptyList());
		mockMvc.perform(get("/api/terms"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void getAllTerms_withData() throws Exception {
		TermResponse resp = new TermResponse();
		resp.setId(1L);
		resp.setName("Spring 2026");
		Mockito.when(termService.getTerms()).thenReturn(Collections.singletonList(resp));
		mockMvc.perform(get("/api/terms"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Spring 2026"));
	}

	@Test
	void getTermsByYearId() throws Exception {
		TermResponse resp1 = new TermResponse();
		resp1.setId(1L);
		resp1.setName("Spring 2026");
		resp1.setYearId(1L);

		TermResponse resp2 = new TermResponse();
		resp2.setId(2L);
		resp2.setName("Fall 2026");
		resp2.setYearId(1L);

		Mockito.when(termService.getTermsByYearId(1L)).thenReturn(List.of(resp1, resp2));
		mockMvc.perform(get("/api/terms/year/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Spring 2026"))
				.andExpect(jsonPath("$[0].yearId").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].name").value("Fall 2026"))
				.andExpect(jsonPath("$[1].yearId").value(1L));
	}

	@Test
	void getTermsByYearId_noTerms() throws Exception {
		Mockito.when(termService.getTermsByYearId(999L)).thenReturn(Collections.emptyList());
		mockMvc.perform(get("/api/terms/year/999"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void getTermsByYearId_yearNotFound() throws Exception {
		Mockito.when(termService.getTermsByYearId(999L)).thenThrow(new ResourceNotFoundException("Year not found"));
		mockMvc.perform(get("/api/terms/year/999"))
				.andExpect(status().isNotFound());
	}

    /** ========== UPDATE TESTS ==========  **/

	@Test
	void updateTerm() throws Exception {
		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Updated");
		TermResponse resp = new TermResponse();
		resp.setId(1L);
		resp.setName("Updated");
		Mockito.when(termService.updateTerm(eq(1L), any())).thenReturn(resp);
		mockMvc.perform(put("/api/terms/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated"));
	}

	@Test
	void updateTerm_withDates() throws Exception {
		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Spring 2026"); // Set required name field
		req.setStartDate(LocalDate.of(2026, 3, 1));
		req.setEndDate(LocalDate.of(2026, 6, 30));
		TermResponse resp = new TermResponse();
		resp.setId(1L);
		resp.setName("Fall 2026");
		resp.setStartDate(LocalDate.of(2026, 9, 1));
		resp.setEndDate(LocalDate.of(2026, 12, 31));
		Mockito.when(termService.updateTerm(eq(1L), any())).thenReturn(resp);
		mockMvc.perform(put("/api/terms/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.startDate").value("2026-09-01"))
				.andExpect(jsonPath("$.endDate").value("2026-12-31"));
	}

	@Test
	void updateTerm_notFound() throws Exception {
		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Updated");
		Mockito.when(termService.updateTerm(eq(999L), any())).thenThrow(new ResourceNotFoundException("Term not found"));
		mockMvc.perform(put("/api/terms/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateTerm_invalidDates() throws Exception {
		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Updated");
		req.setStartDate(LocalDate.of(2026, 12, 31));
		req.setEndDate(LocalDate.of(2026, 9, 1)); // End date before start date
		mockMvc.perform(put("/api/terms/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateTerm_noName() throws Exception {
		TermUpdateRequest req = new TermUpdateRequest();
		// Missing name
		mockMvc.perform(put("/api/terms/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());
	}

    /** ========== DELETE TESTS ==========  **/

	@Test
	void deleteTerm() throws Exception {
		mockMvc.perform(delete("/api/terms/1"))
				.andExpect(status().isNoContent());
		Mockito.verify(termService).deleteTerm(1L);
	}

	@Test
	void deleteTerm_notFound() throws Exception {
		Mockito.doThrow(new ResourceNotFoundException("Term not found")).when(termService).deleteTerm(999L);
		mockMvc.perform(delete("/api/terms/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void removeCourseFromTerm() throws Exception {
		mockMvc.perform(delete("/api/terms/1/courses/2"))
				.andExpect(status().isNoContent());
		Mockito.verify(termService).removeCourseFromTerm(1L, 2L);
	}

	@Test
	void removeCourseFromTerm_notFound() throws Exception {
		Mockito.doThrow(new ResourceNotFoundException("Term not found")).when(termService).removeCourseFromTerm(999L, 2L);
		mockMvc.perform(delete("/api/terms/999/courses/2"))
				.andExpect(status().isNotFound());
	}
}
