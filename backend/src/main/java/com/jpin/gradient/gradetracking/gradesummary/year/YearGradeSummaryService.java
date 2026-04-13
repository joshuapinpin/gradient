package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.year.dto.YearGradeSummaryResponse;

import java.util.List;

public interface YearGradeSummaryService {
    YearGradeSummaryResponse getAverageGradeForYear(Long yearId);
}
