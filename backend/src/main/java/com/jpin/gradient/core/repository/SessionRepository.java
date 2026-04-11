package com.jpin.gradient.core.repository;

import com.jpin.gradient.core.model.SessionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<SessionSchedule, Long> {
    long countByCourseId(Long courseId);
    List<SessionSchedule> findByCourseId(Long courseId);
}
