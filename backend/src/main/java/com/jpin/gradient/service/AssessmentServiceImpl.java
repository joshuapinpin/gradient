package com.jpin.gradient.service;

import java.util.List;

import com.jpin.gradient.model.Assessment;
import com.jpin.gradient.repository.AssessmentRepository;
import com.jpin.gradient.dto.assessment.AssessmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssessmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssessmentResponse;
import com.jpin.gradient.dto.assessment.AssessmentUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpin.gradient.exception.ResourceNotFoundException;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public AssessmentResponse create(AssessmentCreateRequest request) {
        Assessment assessment = new Assessment();
        assessment.setName(request.getName());
        assessment.setAssessmentType(request.getAssessmentType());
        assessment.setWeight(request.getWeight());

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> list() {
        return assessmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AssessmentResponse update(Long id, AssessmentUpdateRequest request) {
        Assessment assessment = findByIdOrThrow(id);

        if (request.getName() != null) assessment.setName(request.getName());
        if (request.getWeight() != null) assessment.setWeight(request.getWeight());
        if (request.getDueDate() != null) assessment.setDueDate(request.getDueDate());
        if (request.getAssessmentType() != null) assessment.setAssessmentType(request.getAssessmentType());

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    public AssessmentResponse grade(Long id, AssessmentGradeRequest request) {
        Assessment assessment = findByIdOrThrow(id);
        assessment.setGrade(request.getGrade());

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Assessment assessment = findByIdOrThrow(id);
        assessmentRepository.delete(assessment);
    }

    private Assessment findByIdOrThrow(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private AssessmentResponse toResponse(Assessment assessment) {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(assessment.getId());
        response.setName(assessment.getName());
        response.setWeight(assessment.getWeight());
        response.setGrade(assessment.getGrade());
        response.setDueDate(assessment.getDueDate());
        response.setAssessmentType(assessment.getAssessmentType());
        return response;
    }
}