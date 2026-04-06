package com.jpin.gradient.controller;

import com.jpin.gradient.dto.create.TermCreateRequest;
import com.jpin.gradient.dto.response.TermResponse;
import com.jpin.gradient.dto.update.TermUpdateRequest;
import com.jpin.gradient.service.TermService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TermResponse create(@Valid @RequestBody TermCreateRequest request) {
        return termService.createTerm(request);
    }

    @GetMapping("/{id}")
    public TermResponse getById(@PathVariable Long id) {
        return termService.getTermById(id);
    }

    @GetMapping
    public List<TermResponse> getAll() {
        return termService.getTerms();
    }

    @PutMapping("/{id}")
    public TermResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TermUpdateRequest request) {
        return termService.updateTerm(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        termService.deleteTerm(id);
    }

    @DeleteMapping("/{termId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCourseFromTerm(
            @PathVariable Long termId,
            @PathVariable Long courseId) {
        termService.removeCourseFromTerm(termId, courseId);
    }
}
