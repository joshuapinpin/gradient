package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;

public interface CourseGradeSummaryService {
    CourseGradeSimpleSummary getSimpleSummary(Long courseId);
    CourseGradeFullSummary getFullSummary(Long courseId);
}
