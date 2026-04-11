package com.jpin.gradient.core.service.impl;

import com.jpin.gradient.core.dto.create.TermCreateRequest;
import com.jpin.gradient.core.dto.response.TermResponse;
import com.jpin.gradient.core.dto.update.TermUpdateRequest;
import com.jpin.gradient.core.model.Course;
import com.jpin.gradient.core.model.Term;
import com.jpin.gradient.core.model.Year;
import com.jpin.gradient.core.repository.CourseRepository;
import com.jpin.gradient.core.repository.TermRepository;
import com.jpin.gradient.core.repository.YearRepository;
import com.jpin.gradient.core.service.TermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.jpin.gradient.core.exception.ResourceNotFoundException;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;
    private final CourseRepository courseRepository;
    private final YearRepository yearRepository;

    public TermServiceImpl(
            TermRepository termRepository,
            CourseRepository courseRepository,
            YearRepository yearRepository) {
        this.termRepository = termRepository;
        this.courseRepository = courseRepository;
        this.yearRepository = yearRepository;
    }


    @Override
    public TermResponse createTerm(TermCreateRequest request) {
        Term term = new Term();

        term.setName(request.getName());
        if (request.getStartDate() != null) term.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) term.setEndDate(request.getEndDate());
        if(request.getStartDate() != null && request.getEndDate() != null
                && !request.getStartDate().isBefore(request.getEndDate())){
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Year year = findYearByIdOrThrow(request.getYearId());
        term.setYear(year);

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

        if (request.getStartDate() != null && request.getEndDate() != null
                && !request.getStartDate().isBefore(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (request.getName() != null) term.setName(request.getName());
        if (request.getStartDate() != null) term.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) term.setEndDate(request.getEndDate());

        term = termRepository.save(term);
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
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!course.getTerm().equals(term)) {
            throw new IllegalArgumentException("Course does not belong to the specified term");
        }

        term.removeCourse(course);
        termRepository.save(term);
    }

    // =========== HELPER METHODS ==========

    private Term findByIdOrThrow(Long id) {
        return termRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Term not found with id: " + id));
    }

    private Year findYearByIdOrThrow(Long id) {
        return yearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Year not found with id: " + id));
    }

    private TermResponse toResponse(Term term){
        TermResponse response = new TermResponse();
        response.setId(term.getId());
        response.setName(term.getName());
        response.setStartDate(term.getStartDate());
        response.setEndDate(term.getEndDate());
        response.setYearId(term.getYear().getId());
        return response;
    }
}
