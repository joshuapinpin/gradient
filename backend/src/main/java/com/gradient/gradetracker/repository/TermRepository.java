package com.gradient.gradetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gradient.gradetracker.model.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
}
