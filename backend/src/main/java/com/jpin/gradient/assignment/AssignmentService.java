package com.jpin.gradient.assignment;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse create(AssignmentCreateRequest request);
    AssignmentResponse getById(Long id);
    List<AssignmentResponse> list();
    AssignmentResponse update(Long id, AssignmentUpdateRequest request);
    AssignmentResponse grade(Long id, AssignmentGradeRequest request);
    void delete(Long id);
}
