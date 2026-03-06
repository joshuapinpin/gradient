package com.jpin.gradient.repository;

import com.jpin.gradient.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    // No Extra Methods Required for Basic CRUD
}
