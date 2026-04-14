package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.summary.YearGradeSimpleSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grade-summary/year")
public class YearGradeSummaryController {

    private final YearGradeSummaryService yearGradeSummaryService;

    public YearGradeSummaryController(YearGradeSummaryService yearGradeSummaryService) {
        this.yearGradeSummaryService = yearGradeSummaryService;
    }

    @GetMapping("{yearId}/average")
    public YearGradeSimpleSummary getAverageGrade(@PathVariable Long yearId) {
        return yearGradeSummaryService.getSimpleSummary(yearId);
    }
}
