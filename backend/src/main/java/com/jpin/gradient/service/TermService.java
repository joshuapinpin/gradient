package com.jpin.gradient.service;

import com.jpin.gradient.dto.term.TermCreateRequest;
import com.jpin.gradient.dto.term.TermResponse;
import com.jpin.gradient.dto.term.TermUpdateRequest;

import java.util.List;

public interface TermService {
    TermResponse createTerm(TermCreateRequest request);
    TermResponse getTermById(Long id);
    List<TermResponse> getTerms();
    TermResponse updateTerm(Long id, TermUpdateRequest request);
    void deleteTerm(Long id);
    void removeCourseFromTerm(Long termId, Long courseId);
}
