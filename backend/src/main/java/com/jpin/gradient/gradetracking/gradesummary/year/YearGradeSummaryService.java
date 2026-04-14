package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;

public interface YearGradeSummaryService {
    CourseGradeSimpleSummary getSimpleSummary(Long yearId);
}
