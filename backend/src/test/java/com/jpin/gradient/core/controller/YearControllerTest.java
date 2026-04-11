package com.jpin.gradient.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpin.gradient.core.controller.YearController;
import com.jpin.gradient.core.dto.create.YearCreateRequest;
import com.jpin.gradient.core.dto.response.YearResponse;
import com.jpin.gradient.core.dto.update.YearUpdateRequest;
import com.jpin.gradient.core.exception.ApiExceptionHandler;
import com.jpin.gradient.core.exception.ResourceNotFoundException;
import com.jpin.gradient.core.service.YearService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class YearControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private YearService yearService;

    @InjectMocks
    private YearController yearController;

    @BeforeEach
    void setup(){
        // tells jackson how to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());

        // serialize LocalDate as ISO-8601 string instead of timestamp
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(yearController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

    }

    /** ========== CREATE TESTS ==========  **/

    @Test
    void createYear() throws Exception {
        YearCreateRequest req = new YearCreateRequest();
        req.setName("2024");
        req.setStartDate(LocalDate.of(2024, 1, 1));
        req.setEndDate(LocalDate.of(2024, 12, 31));

        YearResponse resp = new YearResponse();
        resp.setId(1L);
        resp.setName(req.getName());
        resp.setStartDate(req.getStartDate());
        resp.setEndDate(req.getEndDate());

        Mockito.when(yearService.createYear(any(YearCreateRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(post("/api/years")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(resp.getId()))
                .andExpect(jsonPath("$.name").value(resp.getName()))
                .andExpect(jsonPath("$.startDate").value(resp.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(resp.getEndDate().toString()));
    }

    @Test
    void createYear_invalidName() throws Exception{
        YearCreateRequest req = new YearCreateRequest();
        req.setName(""); // Invalid name
        req.setStartDate(LocalDate.of(2024, 1, 1));
        req.setEndDate(LocalDate.of(2024, 12, 31));

        mockMvc.perform(post("/api/years")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createYear_invalidDates() throws Exception{
        YearCreateRequest req = new YearCreateRequest();
        req.setName("2024");

        // Invalid: start date after end date
        req.setStartDate(LocalDate.of(2024, 12, 31));
        req.setEndDate(LocalDate.of(2024, 1, 1));

        mockMvc.perform(post("/api/years")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    /** ========== READ TESTS ==========  **/

    @Test
    void getYearById() throws Exception {
        YearResponse resp = new YearResponse();
        resp.setId(1L);
        resp.setName("2024");
        resp.setStartDate(LocalDate.of(2024, 1, 1));
        resp.setEndDate(LocalDate.of(2024, 12, 31));

        Mockito.when(yearService.getYearById(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/years/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resp.getId()))
                .andExpect(jsonPath("$.name").value(resp.getName()))
                .andExpect(jsonPath("$.startDate").value(resp.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(resp.getEndDate().toString()));
    }

    @Test
    void getYearById_notFound() throws Exception {
        Mockito.when(yearService.getYearById(999L)).thenThrow(new ResourceNotFoundException("Year not found with id: 999"));

        mockMvc.perform(get("/api/years/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Year not found with id: 999"));
    }

    @Test
    void getAllYears() throws Exception {
        YearResponse resp1 = new YearResponse();
        resp1.setId(1L);
        resp1.setName("2024");
        resp1.setStartDate(LocalDate.of(2024, 1, 1));
        resp1.setEndDate(LocalDate.of(2024, 12, 31));

        YearResponse resp2 = new YearResponse();
        resp2.setId(2L);
        resp2.setName("2025");
        resp2.setStartDate(LocalDate.of(2025, 1, 1));
        resp2.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearService.getYears()).thenReturn(List.of(resp1, resp2));

        mockMvc.perform(get("/api/years")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(resp1.getId()))
                .andExpect(jsonPath("$[0].name").value(resp1.getName()))
                .andExpect(jsonPath("$[0].startDate").value(resp1.getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(resp1.getEndDate().toString()))
                .andExpect(jsonPath("$[1].id").value(resp2.getId()))
                .andExpect(jsonPath("$[1].name").value(resp2.getName()))
                .andExpect(jsonPath("$[1].startDate").value(resp2.getStartDate().toString()))
                .andExpect(jsonPath("$[1].endDate").value(resp2.getEndDate().toString()));
    }

    @Test
    void getAllYears_empty() throws Exception {
        Mockito.when(yearService.getYears()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/api/years")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /** ========== UPDATE TESTS ==========  **/

    @Test
    void updateYear() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        req.setName("Updated 2024");
        req.setStartDate(LocalDate.of(2024, 2, 1));
        req.setEndDate(LocalDate.of(2024, 11, 30));

        YearResponse resp = new YearResponse();
        resp.setId(1L);
        resp.setName(req.getName());
        resp.setStartDate(req.getStartDate());
        resp.setEndDate(req.getEndDate());

        Mockito.when(yearService.updateYear(eq(1L), any(YearUpdateRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(put("/api/years/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resp.getId()))
                .andExpect(jsonPath("$.name").value(resp.getName()))
                .andExpect(jsonPath("$.startDate").value(resp.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(resp.getEndDate().toString()));
    }

    @Test
    void updateYear_notFound() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        req.setName("Updated 2024");
        req.setStartDate(LocalDate.of(2024, 2, 1));
        req.setEndDate(LocalDate.of(2024, 11, 30));

        Mockito.when(yearService.updateYear(eq(999L), any(YearUpdateRequest.class)))
                .thenThrow(new ResourceNotFoundException("Year not found with id: 999"));

        mockMvc.perform(put("/api/years/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Year not found with id: 999"));
    }


    @Test
    void updateYear_invalidDates() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        req.setName("Updated 2024");
        // Invalid: start date after end date
        req.setStartDate(LocalDate.of(2024, 12, 31));
        req.setEndDate(LocalDate.of(2024, 1, 1));

        mockMvc.perform(put("/api/years/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateYear_partial() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        req.setName("Partially Updated 2024");

        YearResponse resp = new YearResponse();
        resp.setId(1L);
        resp.setName(req.getName());
        resp.setStartDate(LocalDate.of(2024, 1, 1)); // Original start date
        resp.setEndDate(LocalDate.of(2024, 12, 31)); // Original end date

        Mockito.when(yearService.updateYear(eq(1L), any(YearUpdateRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(put("/api/years/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resp.getId()))
                .andExpect(jsonPath("$.name").value(resp.getName()))
                .andExpect(jsonPath("$.startDate").value(resp.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(resp.getEndDate().toString()));

    }

    @Test
    void updateYear_noName() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        // No name provided, which is required

        mockMvc.perform(put("/api/years/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void updateYear_emptyName() throws Exception {
        YearUpdateRequest req = new YearUpdateRequest();
        req.setName(""); // Invalid empty name

        mockMvc.perform(put("/api/years/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    /** ========== DELETE TESTS ==========  **/

    @Test
    void deleteYear() throws Exception {
        mockMvc.perform(delete("/api/years/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(yearService).deleteYear(1L);
    }

    @Test
    void removeTermFromYear() throws Exception {
        mockMvc.perform(delete("/api/years/1/terms/2"))
                .andExpect(status().isNoContent());
        Mockito.verify(yearService).removeTermFromYear(1L, 2L);
    }


    @Test
    void deleteYear_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Year not found with id: 999"))
                .when(yearService).deleteYear(999L);

        mockMvc.perform(delete("/api/years/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Year not found with id: 999"));
    }

     @Test
    void removeTermFromYear_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Year not found with id: 999"))
                .when(yearService).removeTermFromYear(999L, 1L);

        mockMvc.perform(delete("/api/years/999/terms/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Year not found with id: 999"));
    }
}
