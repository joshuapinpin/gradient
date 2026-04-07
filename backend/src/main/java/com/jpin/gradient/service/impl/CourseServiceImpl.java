package com.jpin.gradient.service.impl;

import java.util.List;

import com.jpin.gradient.model.Assessment;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.repository.TermRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpin.gradient.dto.create.CourseCreateRequest;
import com.jpin.gradient.dto.response.CourseResponse;
import com.jpin.gradient.dto.update.CourseUpdateRequest;
import com.jpin.gradient.exception.ResourceNotFoundException;
import com.jpin.gradient.model.Course;
import com.jpin.gradient.repository.AssessmentRepository;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.service.CourseService;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AssessmentRepository assessmentRepository;
    private final TermRepository termRepository;

    public CourseServiceImpl(
            CourseRepository courseRepository,
            AssessmentRepository assessmentRepository,
            TermRepository termRepository) {
        this.courseRepository = courseRepository;
        this.assessmentRepository = assessmentRepository;
        this.termRepository = termRepository;
    }

    @Override
    public CourseResponse createCourse(CourseCreateRequest request) {
        Course course = new Course();
        course.setName(request.getName());

        Term term = findTermByIdOrThrow(request.getTermId());
        course.setTerm(term);

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        Course course = findByIdOrThrow(id);

        if (request.getName() != null) {
            course.setName(request.getName());
        }

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = findByIdOrThrow(id);
        courseRepository.delete(course);
    }

    @Override
    public void removeAssessmentFromCourse(Long courseId, Long assessmentId) {
        Course course = findByIdOrThrow(courseId);
        Assessment assessment = course.getAssessments().stream()
                .filter(a -> a.getId().equals(assessmentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Assessment not found in course with id: " + assessmentId));
        course.removeAssessment(assessment);
        courseRepository.save(course);
    }

    private Course findByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private Term findTermByIdOrThrow(Long id) {
        return termRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Term not found with id: " + id));
    }


    private CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setName(course.getName());
        response.setAssessmentCount(Math.toIntExact(assessmentRepository.countByCourseId(course.getId())));
        return response;
    }
}
