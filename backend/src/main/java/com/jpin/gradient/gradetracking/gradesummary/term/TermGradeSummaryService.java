package com.jpin.gradient.gradetracking.gradesummary.term;

import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;

public interface TermGradeSummaryService {
    TermGradeSimpleSummary getSimpleSummary(Long termId);
}
