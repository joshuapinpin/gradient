package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTargetsResponse;

import java.util.List;

public interface CourseGradeSummaryService {
    CourseGradeSimpleSummary getSimpleSummary(Long courseId);
    CourseGradeFullSummary getFullSummary(Long courseId);
}
