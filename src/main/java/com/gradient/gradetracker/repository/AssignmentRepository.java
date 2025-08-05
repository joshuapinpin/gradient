package com.gradient.gradetracker.repository;


import com.gradient.gradetracker.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

     //Additional query methods can be defined here if needed
     //For example, to find assignments by course ID or user ID
     List<Assignment> findByCourseId(Long courseId);
     List<Assignment> findByUserId(Long userId);
}
