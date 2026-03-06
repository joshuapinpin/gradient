package com.jpin.gradient.assignment;

import com.jpin.gradient.assignment.dto.AssignmentCreateRequest;
import com.jpin.gradient.assignment.dto.AssignmentGradeRequest;
import com.jpin.gradient.assignment.dto.AssignmentResponse;
import com.jpin.gradient.assignment.dto.AssignmentUpdateRequest;
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

    @PostMapping("/{id}/grade")
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
