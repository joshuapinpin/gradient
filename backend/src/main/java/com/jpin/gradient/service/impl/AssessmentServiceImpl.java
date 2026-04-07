package com.jpin.gradient.service.impl;

import java.util.List;

import com.jpin.gradient.model.Assessment;
import com.jpin.gradient.model.Course;
import com.jpin.gradient.repository.AssessmentRepository;
import com.jpin.gradient.dto.create.AssessmentCreateRequest;
import com.jpin.gradient.dto.update.AssessmentGradeRequest;
import com.jpin.gradient.dto.response.AssessmentResponse;
import com.jpin.gradient.dto.update.AssessmentUpdateRequest;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.service.AssessmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpin.gradient.exception.ResourceNotFoundException;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final CourseRepository courseRepository;

    public AssessmentServiceImpl(
            AssessmentRepository assessmentRepository,
            CourseRepository courseRepository) {
        this.assessmentRepository = assessmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public AssessmentResponse createAssessment(AssessmentCreateRequest request) {
        Assessment assessment = new Assessment();
        assessment.setName(request.getName());
        assessment.setAssessmentType(request.getAssessmentType());
        assessment.setWeight(request.getWeight());

        Course course = findCourseByIdOrThrow(request.getCourseId());
        assessment.setCourse(course);

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getAssessmentById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> getAssessments() {
        return assessmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AssessmentResponse updateAssessment(Long id, AssessmentUpdateRequest request) {
        Assessment assessment = findByIdOrThrow(id);

        if (request.getName() != null) assessment.setName(request.getName());
        if (request.getWeight() != null) assessment.setWeight(request.getWeight());
        if (request.getDueDate() != null) assessment.setDueDate(request.getDueDate());
        if (request.getAssessmentType() != null) assessment.setAssessmentType(request.getAssessmentType());

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    public AssessmentResponse gradeAssessment(Long id, AssessmentGradeRequest request) {
        Assessment assessment = findByIdOrThrow(id);
        assessment.setGrade(request.getGrade());

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Override
    public void deleteAssessment(Long id) {
        Assessment assessment = findByIdOrThrow(id);
        assessmentRepository.delete(assessment);
    }

    private Assessment findByIdOrThrow(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private AssessmentResponse toResponse(Assessment assessment) {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(assessment.getId());
        response.setName(assessment.getName());
        response.setWeight(assessment.getWeight());
        response.setGrade(assessment.getGrade());
        response.setDueDate(assessment.getDueDate());
        response.setAssessmentType(assessment.getAssessmentType());
        response.setCourseId(assessment.getCourse().getId());
        return response;
    }
}