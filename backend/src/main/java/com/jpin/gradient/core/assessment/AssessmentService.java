package com.jpin.gradient.core.assessment;

import com.jpin.gradient.core.assessment.dto.AssessmentCreateRequest;
import com.jpin.gradient.core.assessment.dto.AssessmentGradeRequest;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.core.assessment.dto.AssessmentUpdateRequest;

import java.util.List;

public interface AssessmentService {
    AssessmentResponse createAssessment(AssessmentCreateRequest request);
    AssessmentResponse getAssessmentById(Long id);
    List<AssessmentResponse> getAssessments();
    AssessmentResponse updateAssessment(Long id, AssessmentUpdateRequest request);
    AssessmentResponse gradeAssessment(Long id, AssessmentGradeRequest request);
    void deleteAssessment(Long id);
}
