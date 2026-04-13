package com.jpin.gradient.core.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    long countByCourseId(Long courseId);
    List<Assessment> findByCourseId(Long courseId);
}
