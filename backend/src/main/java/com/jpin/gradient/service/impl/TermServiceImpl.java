package com.jpin.gradient.service.impl;

import com.jpin.gradient.dto.term.TermCreateRequest;
import com.jpin.gradient.dto.term.TermResponse;
import com.jpin.gradient.dto.term.TermUpdateRequest;
import com.jpin.gradient.model.Course;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.repository.TermRepository;
import com.jpin.gradient.service.TermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;
    private final CourseRepository courseRepository;

    public TermServiceImpl(TermRepository termRepository, CourseRepository courseRepository) {
        this.termRepository = termRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public TermResponse createTerm(TermCreateRequest request) {
        Term term = new Term();

        term.setName(request.getName());
        if (request.getStartDate() != null) term.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) term.setEndDate(request.getEndDate());

        term = termRepository.save(term);
        return toResponse(term);
    }

    @Override
    public TermResponse getTermById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public List<TermResponse> getTerms() {
        return termRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TermResponse updateTerm(Long id, TermUpdateRequest request) {
        Term term = findByIdOrThrow(id);

        if (request.getName() != null) term.setName(request.getName());
        if (request.getStartDate() != null) term.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) term.setEndDate(request.getEndDate());

        return toResponse(term);
    }

    @Override
    public void deleteTerm(Long id) {
        Term term = findByIdOrThrow(id);
        termRepository.delete(term);
    }

    @Override
    public void removeCourseFromTerm(Long termId, Long courseId) {
        Term term = findByIdOrThrow(termId);
        Course course = term.getCourses().stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course not found in term with id: " + courseId));
        term.removeCourse(course);
        termRepository.save(term);
    }

    private Term findByIdOrThrow(Long id) {
        return termRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Term not found with id: " + id));
    }

    private TermResponse toResponse(Term term){
        TermResponse response = new TermResponse();
        response.setId(term.getId());
        response.setName(term.getName());
        response.setStartDate(term.getStartDate());
        response.setEndDate(term.getEndDate());
        return response;
    }
}
