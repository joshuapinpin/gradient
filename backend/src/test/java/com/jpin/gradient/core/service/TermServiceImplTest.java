package com.jpin.gradient.core.service;

import com.jpin.gradient.core.term.dto.TermCreateRequest;
import com.jpin.gradient.core.term.dto.TermResponse;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
import com.jpin.gradient.core.term.Term;
import com.jpin.gradient.core.year.Year;
import com.jpin.gradient.core.term.TermRepository;
import com.jpin.gradient.core.year.YearRepository;
import com.jpin.gradient.core.term.TermServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.jpin.gradient.core.term.dto.TermUpdateRequest;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TermServiceImplTest {
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
	void getTermsByYearId_shouldReturnTerms() {
		Year year = createSampleYear();
		Term term1 = new Term();
		term1.setId(1L);
		term1.setName("Spring 2026");
		term1.setYear(year);

		Term term2 = new Term();
		term2.setId(2L);
		term2.setName("Fall 2026");
		term2.setYear(year);

		Mockito.when(termRepository.findByYearId(1L)).thenReturn(List.of(term1, term2));
		Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.of(year));

		List<TermResponse> responses = termService.getTermsByYearId(1L);

		assertNotNull(responses);
		assertEquals(2, responses.size());
		assertEquals(1L, responses.get(0).getId());
		assertEquals("Spring 2026", responses.get(0).getName());
		assertEquals(2L, responses.get(1).getId());
		assertEquals("Fall 2026", responses.get(1).getName());
	}

	@Test
	void getTermsByYearId_shouldThrowExceptionWhenYearNotFound() {
		Mockito.when(yearRepository.findById(1L)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> termService.getTermsByYearId(1L));
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
