package com.jpin.gradient.service;

import com.jpin.gradient.dto.assessment.AssignmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssignmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssignmentResponse;
import com.jpin.gradient.dto.assessment.AssignmentUpdateRequest;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse create(AssignmentCreateRequest request);
    AssignmentResponse getById(Long id);
    List<AssignmentResponse> list();
    AssignmentResponse update(Long id, AssignmentUpdateRequest request);
    AssignmentResponse grade(Long id, AssignmentGradeRequest request);
    void delete(Long id);
}
