package com.jpin.gradient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpin.gradient.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    long countByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
}
