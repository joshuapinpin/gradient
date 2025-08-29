package com.gradient.gradetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gradient.gradetracker.model.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
