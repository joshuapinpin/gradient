package com.jpin.gradient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpin.gradient.dto.create.TermCreateRequest;
import com.jpin.gradient.dto.response.TermResponse;
import com.jpin.gradient.dto.update.TermUpdateRequest;
import com.jpin.gradient.service.TermService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
		mockMvc = MockMvcBuilders.standaloneSetup(termController).build();
	}

    /** ========== CREATE TESTS ==========  **/
    @Test
//    @DisplayName("POST /api/terms - create term")
    void createTerm() throws Exception{
        TermCreateRequest req = new TermCreateRequest();
        req.setName("Spring 2026");
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
        req.setStartDate(java.time.LocalDate.of(2026, 9, 1));
        req.setEndDate(java.time.LocalDate.of(2026, 12, 31));
        TermResponse resp = new TermResponse();
        resp.setId(2L);
        resp.setName("Fall 2026");
        Mockito.when(termService.createTerm(any())).thenReturn(resp);
        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Fall 2026"));
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
	void getAllTerms() throws Exception {
		Mockito.when(termService.getTerms()).thenReturn(Collections.emptyList());
		mockMvc.perform(get("/api/terms"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
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

    /** ========== DELETE TESTS ==========  **/

	@Test
	void deleteTerm() throws Exception {
		mockMvc.perform(delete("/api/terms/1"))
				.andExpect(status().isNoContent());
		Mockito.verify(termService).deleteTerm(1L);
	}
}
