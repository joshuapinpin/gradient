package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.summary.YearGradeSimpleSummary;

public interface YearGradeSummaryService {
    YearGradeSimpleSummary getSimpleSummary(Long yearId);
}
