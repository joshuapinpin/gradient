package com.jpin.gradient.service;

import com.jpin.gradient.dto.AssignmentCreateRequest;
import com.jpin.gradient.dto.AssignmentGradeRequest;
import com.jpin.gradient.dto.AssignmentResponse;
import com.jpin.gradient.dto.AssignmentUpdateRequest;
import com.jpin.gradient.model.Assignment;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse create(AssignmentCreateRequest request);
    AssignmentResponse getById(Long id);
    List<AssignmentResponse> list();
    AssignmentResponse update(Long id, AssignmentUpdateRequest request);
    AssignmentResponse grade(Long id, AssignmentGradeRequest request);
    void delete(Long id);
}
