package com.jpin.gradient.gradetracking.gradesummary.term;

import com.jpin.gradient.core.course.CourseService;
import com.jpin.gradient.core.course.dto.CourseResponse;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryServiceImpl;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TermGradeSummaryServiceImpl implements TermGradeSummaryService {

    private final CourseService courseService;
    private final CourseGradeSummaryServiceImpl courseGradeSummaryService;
    private final GradeSchemeStrategy gradeSchemeStrategy;

    public TermGradeSummaryServiceImpl(
            CourseService courseService,
            CourseGradeSummaryServiceImpl courseGradeSummaryService,
            GradeSchemeStrategy gradeSchemeStrategy) {
        this.courseService = courseService;
        this.courseGradeSummaryService = courseGradeSummaryService;
        this.gradeSchemeStrategy = gradeSchemeStrategy;
    }

    /**
     * Get a simple summary of grades for a term
     * Includes the average grade across all courses in the term.
     *
     * @param termId
     * @return
     */
    @Override
    public TermGradeSimpleSummary getSimpleSummary(Long termId) {
        // get all the courses from course service
        List<CourseResponse> termCourses = courseService.getCoursesByTermId(termId);
        if(termCourses.isEmpty()) return emptySummary(termId);

        // get the simple summary for each course
        List<CourseGradeSimpleSummary> courseSummaries = termCourses.stream()
                .map(course -> courseGradeSummaryService.getSimpleSummary(course.getId()))
                .toList();

        // create a new simple summary based on the course summaries
        return createTermSimpleSummary(courseSummaries, termId);
    }

    private TermGradeSimpleSummary createTermSimpleSummary(List<CourseGradeSimpleSummary> courseSummaries, Long termId) {
        List<GradeConversion> conversions = courseSummaries.stream()
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

        TermGradeSimpleSummary termSummary = new TermGradeSimpleSummary();
        termSummary.setTermId(termId);
        termSummary.setAverageGrade(grade);
        termSummary.setAverageGpa(gpa);
        termSummary.setClassification(classification);

        return termSummary;
    }

    private TermGradeSimpleSummary emptySummary(Long termId) {
        TermGradeSimpleSummary termSummary = new TermGradeSimpleSummary();
        termSummary.setTermId(termId);
        termSummary.setAverageGrade(BigDecimal.ZERO);
        termSummary.setAverageGpa(BigDecimal.ZERO);
        termSummary.setClassification("N/A");
        return termSummary;
    }
}
