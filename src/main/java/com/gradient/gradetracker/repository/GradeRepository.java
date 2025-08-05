package com.gradient.gradetracker.repository;

import com.gradient.gradetracker.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    // Add custom query methods if needed
}
