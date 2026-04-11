package com.jpin.gradient.service.impl;

import com.jpin.gradient.dto.create.YearCreateRequest;
import com.jpin.gradient.dto.response.YearResponse;
import com.jpin.gradient.dto.update.YearUpdateRequest;
import com.jpin.gradient.exception.ResourceNotFoundException;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.model.Year;
import com.jpin.gradient.repository.TermRepository;
import com.jpin.gradient.repository.YearRepository;
import com.jpin.gradient.service.YearService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class YearServiceImpl implements YearService {

    private final YearRepository yearRepository;
    private final TermRepository termRepository;

    public YearServiceImpl(YearRepository yearRepository, TermRepository termRepository) {
        this.yearRepository = yearRepository;
        this.termRepository = termRepository;
    }


    @Override
    public YearResponse createYear(YearCreateRequest request) {
        Year year = new Year();

        year.setName(request.getName());
        year.setStartDate(request.getStartDate());
        year.setEndDate(request.getEndDate());

        if (!request.getStartDate().isBefore(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        year = yearRepository.save(year);
        return toResponse(year);
    }

    @Override
    public YearResponse getYearById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public List<YearResponse> getYears() {
        return yearRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public YearResponse updateYear(Long id, YearUpdateRequest request) {
        Year year = findByIdOrThrow(id);

        // Validate new dates if both are provided in the request
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (!request.getStartDate().isBefore(request.getEndDate())) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
        }

        if (request.getName() != null) year.setName(request.getName());
        if (request.getStartDate() != null) year.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) year.setEndDate(request.getEndDate());

        year = yearRepository.save(year);
        return toResponse(year);
    }

    @Override
    public void deleteYear(Long id) {
        Year year = findByIdOrThrow(id);
        yearRepository.delete(year);
    }

    @Override
    public void removeTermFromYear(Long yearId, Long termId) {
        Year year = findByIdOrThrow(yearId);
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new ResourceNotFoundException("Term not found with id: " + termId));

        if (!term.getYear().equals(year)) {
            throw new IllegalArgumentException("Term does not belong to the specified year");
        }

        year.removeTerm(term);
        yearRepository.save(year);
    }

    // =========== HELPER METHODS ==========

    private Year findByIdOrThrow(Long id) {
        return yearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Year not found with id: " + id));
    }

    private YearResponse toResponse(Year year) {
        YearResponse response = new YearResponse();
        response.setId(year.getId());
        response.setName(year.getName());
        response.setStartDate(year.getStartDate());
        response.setEndDate(year.getEndDate());
        return response;
    }
}
