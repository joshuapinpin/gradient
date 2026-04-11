package com.jpin.gradient.core.service;

import com.jpin.gradient.core.dto.create.TermCreateRequest;
import com.jpin.gradient.core.dto.response.TermResponse;
import com.jpin.gradient.core.exception.ResourceNotFoundException;
import com.jpin.gradient.core.model.Term;
import com.jpin.gradient.core.model.Year;
import com.jpin.gradient.core.repository.TermRepository;
import com.jpin.gradient.core.repository.YearRepository;
import com.jpin.gradient.core.service.impl.TermServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.jpin.gradient.core.dto.update.TermUpdateRequest;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TermServiceTest {
	@Mock
	private TermRepository termRepository;

	@Mock
	private YearRepository yearRepository;

	@InjectMocks
	private TermServiceImpl termService;

	// --- Test fixture helpers ---
	private Year createSampleYear() {
		Year year = new Year();
		year.setId(1L);
		year.setName("2025-2026");
		year.setStartDate(LocalDate.of(2025, 9, 1));
		year.setEndDate(LocalDate.of(2026, 8, 31));
		return year;
	}

	@Test
	void createTerm_shouldReturnResponse() {
		TermCreateRequest req = new TermCreateRequest();
		req.setName("Spring 2026");
		req.setYearId(1L);

		Year year = createSampleYear();

		Term saved = new Term();
		saved.setId(1L);
		saved.setName("Spring 2026");
		saved.setYear(year);

		Mockito.when(termRepository.save(any(Term.class))).thenReturn(saved);
		Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));

		TermResponse resp = termService.createTerm(req);
		assertNotNull(resp);
		assertEquals(1L, resp.getId());
		assertEquals("Spring 2026", resp.getName());
	}

	@Test
	void getTermById_shouldReturnTerm() {
		Term term = new Term();
		term.setId(1L);
		term.setName("Spring 2026");
		term.setYear(createSampleYear());

		Mockito.when(termRepository.findById(1L)).thenReturn(java.util.Optional.of(term));
		TermResponse resp = termService.getTermById(1L);

		assertNotNull(resp);
		assertEquals(1L, resp.getId());
		assertEquals("Spring 2026", resp.getName());
	}

	@Test
	void getTermById_shouldThrowExceptionWhenNotFound() {
		Mockito.when(termRepository.findById(1L)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> termService.getTermById(1L));
	}

	@Test
	void updateTerm_shouldUpdateAndReturnResponse() {
		Term term = new Term();
		term.setId(1L);
		term.setName("Spring 2026");
		term.setYear(createSampleYear());

		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Fall 2026");

		Mockito.when(termRepository.findById(1L)).thenReturn(java.util.Optional.of(term));
		Mockito.when(termRepository.save(any(Term.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TermResponse resp = termService.updateTerm(1L, req);
		assertNotNull(resp);
		assertEquals(1L, resp.getId());
		assertEquals("Fall 2026", resp.getName());
	}

	@Test
	void deleteTerm_shouldDeleteTerm() {
		Term term = new Term();
		term.setId(1L);
		Mockito.when(termRepository.findById(1L)).thenReturn(java.util.Optional.of(term));
		termService.deleteTerm(1L);
		Mockito.verify(termRepository).delete(term);
	}
}
