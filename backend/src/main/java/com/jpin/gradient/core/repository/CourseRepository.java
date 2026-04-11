package com.jpin.gradient.core.repository;

import com.jpin.gradient.core.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
