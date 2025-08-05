package com.gradient.gradetracker.repository;

import com.gradient.gradetracker.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Assignment, Long> {
    // List<Term> findByCourseId(Long courseId);
    // List<Term> findByUserId(Long userId);
}

