package com.jpin.gradient.core.term;

import com.jpin.gradient.core.term.dto.TermCreateRequest;
import com.jpin.gradient.core.term.dto.TermResponse;
import com.jpin.gradient.core.term.dto.TermUpdateRequest;

import java.util.List;

public interface TermService {
    TermResponse createTerm(TermCreateRequest request);
    TermResponse getTermById(Long id);
    List<TermResponse> getTerms();
    List<TermResponse> getTermsByYearId(Long yearId);
    TermResponse updateTerm(Long id, TermUpdateRequest request);
    void deleteTerm(Long id);
    void removeCourseFromTerm(Long termId, Long courseId);
}
