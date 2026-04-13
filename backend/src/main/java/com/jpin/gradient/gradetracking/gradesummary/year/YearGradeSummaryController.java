package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.year.dto.YearGradeSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grade-summaries/years")
public class YearGradeSummaryController {

    private final YearGradeSummaryService yearGradeSummaryService;

    public YearGradeSummaryController(YearGradeSummaryService yearGradeSummaryService) {
        this.yearGradeSummaryService = yearGradeSummaryService;
    }
    
    @GetMapping("{yearId}/average")
    public YearGradeSummaryResponse getAverageGrade(@PathVariable Long yearId) {
        return yearGradeSummaryService.getAverageGradeForYear(yearId);
    }
}
