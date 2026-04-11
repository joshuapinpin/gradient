package com.jpin.gradient.core.service;

import com.jpin.gradient.core.dto.create.TermCreateRequest;
import com.jpin.gradient.core.dto.response.TermResponse;
import com.jpin.gradient.core.dto.update.TermUpdateRequest;

import java.util.List;

public interface TermService {
    TermResponse createTerm(TermCreateRequest request);
    TermResponse getTermById(Long id);
    List<TermResponse> getTerms();
    TermResponse updateTerm(Long id, TermUpdateRequest request);
    void deleteTerm(Long id);
    void removeCourseFromTerm(Long termId, Long courseId);
}
