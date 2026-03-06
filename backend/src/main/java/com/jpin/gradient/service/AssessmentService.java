package com.jpin.gradient.service;

import com.jpin.gradient.dto.assessment.AssessmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssessmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssessmentResponse;
import com.jpin.gradient.dto.assessment.AssessmentUpdateRequest;

import java.util.List;

public interface AssessmentService {
    AssessmentResponse create(AssessmentCreateRequest request);
    AssessmentResponse getById(Long id);
    List<AssessmentResponse> list();
    AssessmentResponse update(Long id, AssessmentUpdateRequest request);
    AssessmentResponse grade(Long id, AssessmentGradeRequest request);
    void delete(Long id);
}
