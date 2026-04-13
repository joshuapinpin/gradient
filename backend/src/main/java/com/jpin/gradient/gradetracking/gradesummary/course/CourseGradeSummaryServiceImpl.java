package com.jpin.gradient.gradetracking.gradesummary.course;

import com.jpin.gradient.core.assessment.AssessmentService;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.gradetracking.gradeconversion.GradeConversionStrategy;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSummaryResponse;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTargetsResponse;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Setter
@Service
@Transactional(readOnly = true)
public class CourseGradeSummaryServiceImpl implements CourseGradeSummaryService {

    private final AssessmentService assessmentService;
    private final Map<String, GradeConversionStrategy> gradeConversionStrategies;

    // For now, NZ is the only strategy. Use its bean name as default.
    private static final String DEFAULT_STRATEGY = "nzGradeConversionStrategy";

    public CourseGradeSummaryServiceImpl(AssessmentService assessmentService, Map<String, GradeConversionStrategy> gradeConversionStrategies) {
        this.assessmentService = assessmentService;
        this.gradeConversionStrategies = gradeConversionStrategies;
    }

    @Override
    public CourseGradeSummaryResponse getAverageGrade(Long courseId) {
        List<AssessmentResponse> assessmentResponseList = assessmentService.getAssessmentsByCourseId(courseId);
        BigDecimal averageGrade = averageGrade(assessmentResponseList);

        // Use NZ strategy by default for now
        // In the future, you can select the strategy based on user input or course settings
        GradeConversionStrategy strategy = gradeConversionStrategies.get(DEFAULT_STRATEGY);

        CourseGradeSummaryResponse resp = new CourseGradeSummaryResponse();
        resp.setCourseId(courseId);
        resp.setAverageGrade(averageGrade);
        resp.setAverageGpa(strategy.convertGradeToGpa(averageGrade));
        resp.setClassification(strategy.convertGradeToClassification(averageGrade));

        return resp;
    }

    @Override
    public List<CourseGradeTargetsResponse> getRequiredAveragesForAllTargets(Long courseId) {
        // This method would calculate the required average grades for each classification target
        // based on the current grades and the grading scheme. The implementation would depend
        // on how you define the targets and the grading scheme, so it's left as a placeholder for now.

        return List.of(); // Placeholder - return an empty list for now
    }

    // === HELPER METHODS ===

    private BigDecimal averageGrade(List<AssessmentResponse> assessments) {
        BigDecimal totalWeightedGrade = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (AssessmentResponse assessment : assessments) {
            if (assessment.isGraded()) {
                totalWeightedGrade = totalWeightedGrade.add(assessment.getGrade().multiply(assessment.getWeight()));
                totalWeight = totalWeight.add(assessment.getWeight());
            }
        }

        return totalWeight.compareTo(BigDecimal.ZERO) > 0 ? totalWeightedGrade.divide(totalWeight, 2, RoundingMode.HALF_UP) : null;
    }
}
