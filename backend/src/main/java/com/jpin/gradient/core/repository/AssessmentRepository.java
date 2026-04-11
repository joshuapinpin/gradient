package com.jpin.gradient.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpin.gradient.core.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    long countByCourseId(Long courseId);
}
