package com.jpin.gradient.service;

import java.util.List;

import com.jpin.gradient.model.Assignment;
import com.jpin.gradient.repository.AssignmentRepository;
import com.jpin.gradient.dto.assessment.AssignmentCreateRequest;
import com.jpin.gradient.dto.assessment.AssignmentGradeRequest;
import com.jpin.gradient.dto.assessment.AssignmentResponse;
import com.jpin.gradient.dto.assessment.AssignmentUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpin.gradient.exception.ResourceNotFoundException;

@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public AssignmentResponse create(AssignmentCreateRequest request) {
        Assignment assignment = new Assignment();
        assignment.setName(request.getName());
        assignment.setAssignmentType(request.getAssignmentType());
        assignment.setWeight(request.getWeight());

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse getById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> list() {
        return assignmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AssignmentResponse update(Long id, AssignmentUpdateRequest request) {
        Assignment assignment = findByIdOrThrow(id);

        if (request.getName() != null) assignment.setName(request.getName());
        if (request.getWeight() != null) assignment.setWeight(request.getWeight());
        if (request.getDueDate() != null) assignment.setDueDate(request.getDueDate());
        if (request.getAssignmentType() != null) assignment.setAssignmentType(request.getAssignmentType());

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    @Override
    public AssignmentResponse grade(Long id, AssignmentGradeRequest request) {
        Assignment assignment = findByIdOrThrow(id);
        assignment.setGrade(request.getGrade());

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Assignment assignment = findByIdOrThrow(id);
        assignmentRepository.delete(assignment);
    }

    private Assignment findByIdOrThrow(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());
        response.setName(assignment.getName());
        response.setWeight(assignment.getWeight());
        response.setGrade(assignment.getGrade());
        response.setDueDate(assignment.getDueDate());
        response.setAssignmentType(assignment.getAssignmentType());
        return response;
    }
}