package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;

public interface CourseGradeSummaryService {
    CourseGradeSimpleSummary getSimpleSummary(Long courseId);
    CourseGradeFullSummary getFullSummary(Long courseId);
}
