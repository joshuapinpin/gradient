package com.jpin.gradient.service;

import com.jpin.gradient.dto.assessment.AssessmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssessmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssessmentResponse;
import com.jpin.gradient.dto.assessment.AssessmentUpdateRequest;

import java.util.List;

public interface AssessmentService {
    AssessmentResponse createAssessment(AssessmentCreateRequest request);
    AssessmentResponse getAssessmentById(Long id);
    List<AssessmentResponse> getAssessments();
    AssessmentResponse updateAssessment(Long id, AssessmentUpdateRequest request);
    AssessmentResponse gradeAssessment(Long id, AssessmentGradeRequest request);
    void deleteAssessment(Long id);
}
