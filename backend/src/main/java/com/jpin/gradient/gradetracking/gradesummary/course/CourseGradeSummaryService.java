package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSummaryResponse;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTargetsResponse;

import java.util.List;

public interface CourseGradeSummaryService {
    CourseGradeSummaryResponse getAverageGrade(Long courseId);
    List<CourseGradeTargetsResponse> getRequiredAveragesForAllTargets(Long courseId);
}
