package com.gradient.gradetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gradient.gradetracker.model.Term;
import com.gradient.gradetracker.repository.TermRepository;

@Service
public class TermService {
    @Autowired
    private TermRepository termRepository;

    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    public Optional<Term> getTermById(Long id) {
        return termRepository.findById(id);
    }

    public Term saveTerm(Term term) {
        return termRepository.save(term);
    }

    public void deleteTerm(Long id) {
        termRepository.deleteById(id);
    }
}
