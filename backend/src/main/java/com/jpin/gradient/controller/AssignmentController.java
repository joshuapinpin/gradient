package com.jpin.gradient.controller;

import com.jpin.gradient.dto.AssignmentCreateRequest;
import com.jpin.gradient.dto.AssignmentGradeRequest;
import com.jpin.gradient.dto.AssignmentResponse;
import com.jpin.gradient.dto.AssignmentUpdateRequest;
import com.jpin.gradient.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService){
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse create(@Valid @RequestBody AssignmentCreateRequest request){
        return assignmentService.create(request);
    }

    @GetMapping("/{id}")
    public AssignmentResponse getById(@PathVariable Long id) {
        return assignmentService.getById(id);
    }

    @GetMapping
    public List<AssignmentResponse> list(){
        return assignmentService.list();
    }

    @PutMapping("/{id}")
    public AssignmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentUpdateRequest request){
        return assignmentService.update(id, request);
    }

    @PostMapping("/{id}/score")
    public AssignmentResponse grade(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentGradeRequest request){
        return assignmentService.grade(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        assignmentService.delete(id);
    }
}
