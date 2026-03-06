package com.jpin.gradient.assignment;

import com.jpin.gradient.assignment.dto.AssignmentCreateRequest;
import com.jpin.gradient.assignment.dto.AssignmentGradeRequest;
import com.jpin.gradient.assignment.dto.AssignmentResponse;
import com.jpin.gradient.assignment.dto.AssignmentUpdateRequest;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse create(AssignmentCreateRequest request);
    AssignmentResponse getById(Long id);
    List<AssignmentResponse> list();
    AssignmentResponse update(Long id, AssignmentUpdateRequest request);
    AssignmentResponse grade(Long id, AssignmentGradeRequest request);
    void delete(Long id);
}
