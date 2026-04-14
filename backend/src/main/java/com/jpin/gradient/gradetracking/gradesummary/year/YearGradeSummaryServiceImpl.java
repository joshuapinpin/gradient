package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.core.term.Term;
import com.jpin.gradient.core.term.TermService;
import com.jpin.gradient.core.term.dto.TermResponse;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.summary.YearGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.term.TermGradeSummaryServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class YearGradeSummaryServiceImpl implements YearGradeSummaryService {

    private final TermService termService;
    private final TermGradeSummaryServiceImpl termGradeSummaryService;
    private final GradeSchemeStrategy gradeSchemeStrategy;

    public YearGradeSummaryServiceImpl(
            TermService termService,
            TermGradeSummaryServiceImpl termGradeSummaryService,
            GradeSchemeStrategy gradeSchemeStrategy) {
        this.termService = termService;
        this.termGradeSummaryService = termGradeSummaryService;
        this.gradeSchemeStrategy = gradeSchemeStrategy;
    }

    /**
     * Get a simple summary of grades for a year
     * Includes the average grade across all terms in the year.
     * @param yearId
     * @return
     */
    @Override
    public YearGradeSimpleSummary getSimpleSummary(Long yearId) {
        // get all terms
        List<TermResponse> yearTerms = termService.getTermsByYearId(yearId);
        if(yearTerms.isEmpty()) return emptySummary(yearId);

        // get simple summary of each term
        List<TermGradeSimpleSummary> termSummaries = yearTerms.stream()
                .map(tr -> termGradeSummaryService.getSimpleSummary(tr.getId()))
                .toList();

        // create new simple summary based on course summaries
        return createYearSimpleSummary(termSummaries, yearId);
    }

    private YearGradeSimpleSummary createYearSimpleSummary(List<TermGradeSimpleSummary> termSummaries, Long yearId) {
        List<GradeConversion> conversions = termSummaries.stream()
                .map(ss -> gradeSchemeStrategy.convertGrade(ss.getAverageGrade()))
                .toList();

        double averageGrade = conversions.stream()
                .mapToDouble(ss -> ss.getGrade().doubleValue())
                .average()
                .orElse(0.0);
        double averageGpa = conversions.stream()
                .mapToDouble(ss -> ss.getGpaValue().doubleValue())
                .average()
                .orElse(0.0);

        BigDecimal grade = BigDecimal.valueOf(averageGrade).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gpa = BigDecimal.valueOf(averageGpa).setScale(2, RoundingMode.HALF_UP);
        String classification = gradeSchemeStrategy.convertToClassification(BigDecimal.valueOf(averageGrade));

        YearGradeSimpleSummary summary = new YearGradeSimpleSummary();
        summary.setYearId(yearId);
        summary.setAverageGrade(grade);
        summary.setAverageGpa(gpa);
        summary.setClassification(classification);
        return summary;
    }

    private YearGradeSimpleSummary emptySummary(Long yearId) {
        YearGradeSimpleSummary summary = new YearGradeSimpleSummary();
        summary.setYearId(yearId);
        summary.setAverageGrade(BigDecimal.ZERO);
        summary.setAverageGpa(BigDecimal.ZERO);
        summary.setClassification("N/A");
        return summary;
    }
}
