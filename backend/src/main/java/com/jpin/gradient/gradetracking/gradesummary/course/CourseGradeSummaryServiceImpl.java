package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.core.assessment.AssessmentService;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTarget;
import lombok.Setter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    /**
     * Get a simple course grade summary, including average grade, GPA, and classification.
     * @param courseId
     * @return CourseGradeSimpleSummary summary about the grade
     */
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

    /**
     * Get the full course grade summary, including average grade, GPA, classification,
     * percentage graded, and possible grade targets.
     * This method is cached to improve performance, as it may involve multiple database calls.
     */
    @Override
    @Cacheable(value = "courseGradeFullSummary", key = "#courseId")
    public CourseGradeFullSummary getFullSummary(Long courseId) {
        List<AssessmentResponse> assessmentResponses = assessmentService.getAssessmentsByCourseId(courseId);
        return createFullSummary(assessmentResponses, courseId);
    }

    /**
     * Evict the cache for the course grade summary when assessments are updated.
     * This method can be called by the AssessmentService after any update to assessments.
     */
    @CacheEvict(value = "courseGradeFullSummary", key = "#courseId")
    public void evictCourseGradeSummaryCache(Long courseId) {
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
                .convertGrade(BigDecimal.valueOf((totalWeightedGrade / totalWeight) * 100));

        // Build the full summary
        CourseGradeFullSummary summary = new CourseGradeFullSummary();
        summary.setCourseId(courseId);
        summary.setAverageGrade(gradeConversion.getGrade());
        summary.setAverageGpa(gradeConversion.getGpaValue());
        summary.setClassification(gradeConversion.getClassification());
        summary.setPercentageGraded(BigDecimal.valueOf(totalWeight));
        summary.setMinPossibleGrade(minPossibleGrade(totalWeightedGrade, totalWeight));
        summary.setMaxPossibleGrade(maxPossibleGrade(totalWeightedGrade, totalWeight));

        for(GradeType gradeType : strategy.getGradeTypes()) {
            Optional<CourseGradeTarget> target = createGradeTarget(gradeType, totalWeightedGrade, totalWeight);
            target.ifPresent(summary::addGradeTarget);
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

    private Optional<CourseGradeTarget> createGradeTarget(GradeType gradeType, double totalWeightedGrade, double totalWeight){
        double remainingWeight = 100.0 - totalWeight;
        if (remainingWeight <= 0.0)  return Optional.empty();

        double requiredGrade = (gradeType.getMinGrade().doubleValue() - totalWeightedGrade)
                / (remainingWeight / 100.0);

        if(requiredGrade < 0 || requiredGrade > 100) return Optional.empty();

        CourseGradeTarget target = new CourseGradeTarget();
        target.setClassification(gradeType.getClassification());
        target.setRequiredAverage(BigDecimal.valueOf(requiredGrade));

        return Optional.of(target);
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
