package com.gradient.gradetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gradient.gradetracker.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
