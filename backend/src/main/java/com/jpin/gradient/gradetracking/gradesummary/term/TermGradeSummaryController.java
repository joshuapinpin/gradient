package com.jpin.gradient.gradetracking.gradesummary.term;

import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grade-summaries/term")
public class TermGradeSummaryController {

    private final TermGradeSummaryService termGradeSummaryService;

    public TermGradeSummaryController(TermGradeSummaryService termGradeSummaryService) {
        this.termGradeSummaryService = termGradeSummaryService;
    }

    @GetMapping("{termId}/average")
    public TermGradeSimpleSummary getAverageGrade(@PathVariable Long termId) {
        return termGradeSummaryService.getSimpleSummary(termId);
    }
}
