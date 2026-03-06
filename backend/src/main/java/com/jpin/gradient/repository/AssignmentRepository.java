package com.jpin.gradient.repository;

import com.jpin.gradient.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // No Extra Methods Required for Basic CRUD
}
