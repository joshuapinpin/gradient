package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTargetsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grade-summaries/courses")
public class CourseGradeSummaryController {

    private final CourseGradeSummaryService courseGradeSummaryService;

    public CourseGradeSummaryController(CourseGradeSummaryService courseGradeSummaryService) {
        this.courseGradeSummaryService = courseGradeSummaryService;
    }

    @GetMapping("{courseId}/average")
    public CourseGradeSimpleSummary getAverageGrade(Long courseId) {
        return courseGradeSummaryService.getSimpleSummary(courseId);
    }

    @GetMapping("{courseId}/full-summary")
    public CourseGradeFullSummary getFullSummary(@PathVariable Long courseId){
            return courseGradeSummaryService.getFullSummary(courseId);
    }
}
