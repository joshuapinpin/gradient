package com.jpin.gradient.core.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    long countByCourseId(Long courseId);
}
