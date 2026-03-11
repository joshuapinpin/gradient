package com.jpin.gradient.repository;

import com.jpin.gradient.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
