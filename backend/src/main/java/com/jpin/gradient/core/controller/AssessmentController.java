package com.jpin.gradient.core.controller;

import com.jpin.gradient.core.dto.create.AssessmentCreateRequest;
import com.jpin.gradient.core.dto.update.AssessmentGradeRequest;
import com.jpin.gradient.core.dto.response.AssessmentResponse;
import com.jpin.gradient.core.dto.update.AssessmentUpdateRequest;
import com.jpin.gradient.core.service.AssessmentService;
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
        return assessmentService.createAssessment(request);
    }

    @GetMapping("/{id}")
    public AssessmentResponse getById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id);
    }

    @GetMapping
    public List<AssessmentResponse> list(){
        return assessmentService.getAssessments();
    }

    @PutMapping("/{id}")
    public AssessmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentUpdateRequest request){
        return assessmentService.updateAssessment(id, request);
    }

    @PostMapping("/{id}/grade")
    public AssessmentResponse grade(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentGradeRequest request){
        return assessmentService.gradeAssessment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
    }
}
