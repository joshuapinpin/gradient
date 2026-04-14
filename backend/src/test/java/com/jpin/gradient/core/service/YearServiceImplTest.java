package com.jpin.gradient.core.service;


import com.jpin.gradient.core.year.dto.YearCreateRequest;
import com.jpin.gradient.core.year.dto.YearResponse;
import com.jpin.gradient.core.year.dto.YearUpdateRequest;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
import com.jpin.gradient.core.year.Year;
import com.jpin.gradient.core.year.YearRepository;
import com.jpin.gradient.core.year.YearServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class YearServiceImplTest {
    @Mock
    private YearRepository yearRepository;

    @InjectMocks
    private YearServiceImpl yearService;

    @Test
    void createYear_shouldReturnResponse() {
        YearCreateRequest req = new YearCreateRequest();
        req.setName("2025");
        req.setStartDate(LocalDate.of(2025, 1, 1));
        req.setEndDate(LocalDate.of(2025, 12, 31));

        Year year = new Year();
        year.setId(1L);
        year.setName(req.getName());
        year.setStartDate(req.getStartDate());
        year.setEndDate(req.getEndDate());

        Mockito.when(yearRepository.save(Mockito.any(Year.class))).thenReturn(year);

        YearResponse resp = yearService.createYear(req);
        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("2025", resp.getName());
    }

    @Test
    void createYear_shouldThrowExceptionForInvalidDates() {
        YearCreateRequest req = new YearCreateRequest();
        req.setName("2025");
        req.setStartDate(LocalDate.of(2025, 12, 31));
        req.setEndDate(LocalDate.of(2025, 1, 1));

        try {
            yearService.createYear(req);
        } catch (IllegalArgumentException e) {
            assertEquals("Start date must be before end date", e.getMessage());
        }
    }

    @Test
    void createYear_shouldThrowExceptionForEqualDates() {
        YearCreateRequest req = new YearCreateRequest();
        req.setName("2025");
        req.setStartDate(LocalDate.of(2025, 1, 1));
        req.setEndDate(LocalDate.of(2025, 1, 1));

        try {
            yearService.createYear(req);
        } catch (IllegalArgumentException e) {
            assertEquals("Start date must be before end date", e.getMessage());
        }
    }

    @Test
    void getYearById_shouldReturnYear() {
        Year year = new Year();
        year.setId(1L);
        year.setName("2025");
        year.setStartDate(LocalDate.of(2025, 1, 1));
        year.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));
        YearResponse resp = yearService.getYearById(1L);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("2025", resp.getName());
    }

    @Test
    void getYearById_shouldThrowExceptionWhenNotFound() {
        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> yearService.getYearById(1L));
    }

    @Test
    void getYears_shouldReturnList() {
        Year year1 = new Year();
        year1.setId(1L);
        year1.setName("2025");
        year1.setStartDate(LocalDate.of(2025, 1, 1));
        year1.setEndDate(LocalDate.of(2025, 12, 31));

        Year year2 = new Year();
        year2.setId(2L);
        year2.setName("2026");
        year2.setStartDate(LocalDate.of(2026, 1, 1));
        year2.setEndDate(LocalDate.of(2026, 12, 31));

        Mockito.when(yearRepository.findAll()).thenReturn(java.util.List.of(year1, year2));
        var years = yearService.getYears();

        assertNotNull(years);
        assertEquals(2, years.size());
        assertEquals(1L, years.get(0).getId());
        assertEquals("2025", years.get(0).getName());
        assertEquals(2L, years.get(1).getId());
        assertEquals("2026", years.get(1).getName());
    }

    @Test
    void updateYear_shouldUpdateAndReturnResponse() {
        Year year = new Year();
        year.setId(1L);
        year.setName("2025");
        year.setStartDate(LocalDate.of(2025, 1, 1));
        year.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));

        var req = new YearUpdateRequest();
        req.setName("2025-2026");
        req.setStartDate(LocalDate.of(2025, 9, 1));
        req.setEndDate(LocalDate.of(2026, 8, 31));

        Year updatedYear = new Year();
        updatedYear.setId(1L);
        updatedYear.setName(req.getName());
        updatedYear.setStartDate(req.getStartDate());
        updatedYear.setEndDate(req.getEndDate());

        Mockito.when(yearRepository.save(Mockito.any(Year.class))).thenReturn(updatedYear);

        var resp = yearService.updateYear(1L, req);
        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("2025-2026", resp.getName());
    }

    @Test
    void updateYear_shouldThrowExceptionForInvalidDates() {
        Year year = new Year();
        year.setId(1L);
        year.setName("2025");
        year.setStartDate(LocalDate.of(2025, 1, 1));
        year.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));

        var req = new YearUpdateRequest();
        req.setName("2025-2026");
        req.setStartDate(LocalDate.of(2026, 8, 31));
        req.setEndDate(LocalDate.of(2025, 9, 1));

        assertThrows(IllegalArgumentException.class,
                () -> yearService.updateYear(1L, req));
    }

    @Test
    void updateYear_shouldThrowExceptionForEqualDates() {
        Year year = new Year();
        year.setId(1L);
        year.setName("2025");
        year.setStartDate(LocalDate.of(2025, 1, 1));
        year.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));

        var req = new YearUpdateRequest();
        req.setName("2025-2026");
        req.setStartDate(LocalDate.of(2025, 9, 1));
        req.setEndDate(LocalDate.of(2025, 9, 1));

        assertThrows(IllegalArgumentException.class,
                () -> yearService.updateYear(1L, req));
    }

    @Test
    void deleteYear_shouldDelete() {
        Year year = new Year();
        year.setId(1L);
        year.setName("2025");
        year.setStartDate(LocalDate.of(2025, 1, 1));
        year.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));
        yearService.deleteYear(1L);
        Mockito.verify(yearRepository).delete(year);
    }
    
    
}
