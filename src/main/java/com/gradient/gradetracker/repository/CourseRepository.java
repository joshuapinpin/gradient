package com.gradient.gradetracker.repository;

import com.gradient.gradetracker.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Additional query methods can be defined here if needed
    // For example, to find assignments by course ID or user ID
    List<Course> findByCourseId(Long courseId);
    List<Course> findByUserId(Long userId);
}
