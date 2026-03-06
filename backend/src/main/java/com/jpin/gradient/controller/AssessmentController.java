package com.jpin.gradient.controller;

import com.jpin.gradient.dto.assessment.AssessmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssessmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssessmentResponse;
import com.jpin.gradient.dto.assessment.AssessmentUpdateRequest;
import com.jpin.gradient.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssessmentResponse create(@Valid @RequestBody AssessmentCreateRequest request){
        return assessmentService.create(request);
    }

    @GetMapping("/{id}")
    public AssessmentResponse getById(@PathVariable Long id) {
        return assessmentService.getById(id);
    }

    @GetMapping
    public List<AssessmentResponse> list(){
        return assessmentService.list();
    }

    @PutMapping("/{id}")
    public AssessmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentUpdateRequest request){
        return assessmentService.update(id, request);
    }

    @PostMapping("/{id}/grade")
    public AssessmentResponse grade(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentGradeRequest request){
        return assessmentService.grade(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        assessmentService.delete(id);
    }
}
