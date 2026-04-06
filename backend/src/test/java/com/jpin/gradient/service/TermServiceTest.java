package com.jpin.gradient.service;

import com.jpin.gradient.dto.create.TermCreateRequest;
import com.jpin.gradient.dto.response.TermResponse;
import com.jpin.gradient.exception.ResourceNotFoundException;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.repository.TermRepository;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.service.impl.TermServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.jpin.gradient.dto.update.TermUpdateRequest;

@ExtendWith(MockitoExtension.class)
public class TermServiceTest {
	@Mock
	private TermRepository termRepository;
	@Mock
	private CourseRepository courseRepository;
	@InjectMocks
	private TermServiceImpl termService;

	@Test
	void createTerm_shouldReturnResponse() {
		TermCreateRequest req = new TermCreateRequest();
		req.setName("Spring 2026");
		Term saved = new Term();
		saved.setId(1L);
		saved.setName("Spring 2026");
		Mockito.when(termRepository.save(any(Term.class))).thenReturn(saved);
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
		Mockito.when(termRepository.findById(1L)).thenReturn(java.util.Optional.of(term));

		TermUpdateRequest req = new TermUpdateRequest();
		req.setName("Fall 2026");
		TermResponse resp = termService.updateTerm(1L, req);
		assertNotNull(resp);
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
