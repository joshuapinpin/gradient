package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.core.assessment.AssessmentService;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;
import lombok.Setter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Setter
@Service
@Transactional(readOnly = true)
public class CourseGradeSummaryServiceImpl implements CourseGradeSummaryService {

    private final AssessmentService assessmentService;
    private final Map<String, GradeSchemeStrategy> gradeSchemeStrategyMap;

    // For now, NZ is the only strategy. Use its bean name as default.
    private static final String DEFAULT_STRATEGY = "nzGradeConversionStrategy";

    public CourseGradeSummaryServiceImpl(AssessmentService assessmentService, Map<String, GradeSchemeStrategy> gradeSchemeStrategyMap) {
        this.assessmentService = assessmentService;
        this.gradeSchemeStrategyMap = gradeSchemeStrategyMap;
    }

    @Override
    public CourseGradeSimpleSummary getSimpleSummary(Long courseId) {
        CourseGradeFullSummary fullSummary = getFullSummary(courseId);
        CourseGradeSimpleSummary simpleSummary = new CourseGradeSimpleSummary();

        simpleSummary.setCourseId(fullSummary.getCourseId());
        simpleSummary.setAverageGrade(fullSummary.getAverageGrade());
        simpleSummary.setAverageGpa(fullSummary.getAverageGpa());
        simpleSummary.setClassification(fullSummary.getClassification());
        return simpleSummary;
    }

    @Override
    @Cacheable(value = "courseGradeFullSummary", key = "#courseId")
    public CourseGradeFullSummary getFullSummary(Long courseId) {
        List<AssessmentResponse> assessmentResponses = assessmentService.getAssessmentsByCourseId(courseId);
        return createFullSummary(assessmentResponses, courseId);
    }

    @CacheEvict(value = "courseGradeFullSummary", key = "#courseId")
    public void evictCourseGradeSummaryCache(Long courseId) {
        // This method can be called after any assessment grade is updated to ensure the cache is refreshed.
        // No implementation needed, annotation handles cache eviction
    }

    // === Helper methods ===

    private CourseGradeFullSummary createFullSummary(List<AssessmentResponse> assessments, Long courseId) {
        double[] weightedGradeAndWeight = getWeightedGrade(assessments);
        double totalWeightedGrade = weightedGradeAndWeight[0];
        double totalWeight = weightedGradeAndWeight[1];

        if(totalWeight == 0.0) return emptySummary(courseId);

        GradeSchemeStrategy strategy = gradeSchemeStrategyMap.get(DEFAULT_STRATEGY);
        GradeConversion gradeConversion = strategy
                .convertGrade(BigDecimal.valueOf(totalWeightedGrade / totalWeight));

        // Build the full summary
        CourseGradeFullSummary summary = new CourseGradeFullSummary();
        summary.setCourseId(courseId);
        summary.setAverageGrade(gradeConversion.getGrade());
        summary.setAverageGpa(gradeConversion.getGpaValue());
        summary.setClassification(gradeConversion.getClassification());
        summary.setMinPossibleGrade(minPossibleGrade(totalWeightedGrade, totalWeight));
        summary.setMaxPossibleGrade(maxPossibleGrade(totalWeightedGrade, totalWeight));

        for(GradeType gradeType : strategy.getGradeTypes()) {
            summary.addGradeTarget(gradeType, gradeType.getMinGrade());
        }

        return summary;
    }

    private BigDecimal minPossibleGrade(double totalWeightedGrade, double totalWeight) {
        if(totalWeight == 0.0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(totalWeightedGrade); // Assuming 0% is the minimum possible grade
    }

    private BigDecimal maxPossibleGrade(double totalWeightedGrade, double totalWeight) {
        if(totalWeight == 0.0) return BigDecimal.valueOf(100);
        double remainingWeight = 100.0 - totalWeight; // assume 100% is the max
        double maxGrade = totalWeightedGrade + remainingWeight; // assume all remaining assessments are graded at 100%
        return BigDecimal.valueOf(Math.min(maxGrade, 100.0)); // Cap at 100%
    }

    private double[] getWeightedGrade(List<AssessmentResponse> assessments){
        double totalWeightedGrade = 0.0; // in decimal (e.g., 85.5)
        double totalWeight = 0.0; // in decimal (e.g., 20 for 20%)

        for (AssessmentResponse assessment : assessments){
            if(assessment.isGraded()) {
                double grade = assessment.getGrade().doubleValue();
                double weight = assessment.getWeight().doubleValue();
                double decimalWeight = weight / 100.0;
                totalWeightedGrade += grade * decimalWeight;
                totalWeight += weight;
            }
        }
        return new double[]{totalWeightedGrade, totalWeight};
    }

    private CourseGradeFullSummary emptySummary(Long courseId) {
        CourseGradeFullSummary summary = new CourseGradeFullSummary();
        summary.setCourseId(courseId);
        summary.setAverageGrade(BigDecimal.ZERO);
        summary.setAverageGpa(BigDecimal.ZERO);
        summary.setClassification("N/A");
        summary.setMinPossibleGrade(BigDecimal.ZERO);
        summary.setMaxPossibleGrade(BigDecimal.valueOf(100));
        return summary;
    }
}
