package com.jpin.gradient.gradetracking;

import com.jpin.gradient.core.assessment.AssessmentService;
import com.jpin.gradient.core.assessment.dto.AssessmentResponse;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradeconversion.service.impl.NzGradeScheme;
import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryServiceImpl;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeFullSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.course.dto.CourseGradeTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CourseGradeSummaryServiceImplTest {

    @Mock
    private AssessmentService assessmentService;
    private CourseGradeSummaryServiceImpl courseGradeSummaryService;

    @BeforeEach
    void setUp() {
        GradeSchemeStrategy gradeSchemeStrategy = new NzGradeScheme(); // Use real implementation
        Map<String, GradeSchemeStrategy> strategyMap = new HashMap<>();
        strategyMap.put("nzGradeConversionStrategy", gradeSchemeStrategy);
        courseGradeSummaryService = new CourseGradeSummaryServiceImpl(assessmentService, strategyMap);
    }

    // ===== Test Helpers =====

    private List<AssessmentResponse> mockAssessments_fullCourse(){
        AssessmentResponse as1 = new AssessmentResponse();
        as1.setId(1L);
        as1.setName("Assignment 1");
        as1.setWeight(BigDecimal.valueOf(20));
        as1.setGrade(BigDecimal.valueOf(85));
        as1.setCourseId(1L);

        AssessmentResponse as2 = new AssessmentResponse();
        as2.setId(2L);
        as2.setName("Midterm Exam");
        as2.setWeight(BigDecimal.valueOf(30));
        as2.setGrade(BigDecimal.valueOf(70));
        as2.setCourseId(1L);

        AssessmentResponse as3 = new AssessmentResponse();
        as3.setId(3L);
        as3.setName("Final Exam");
        as3.setWeight(BigDecimal.valueOf(50));
        as3.setGrade(BigDecimal.valueOf(90));
        as3.setCourseId(1L);

        
//        Expected values for the full summary based on these assessments:
//        Course ID: 1L
//        Average Grade: (85*0.2 + 70*0.3 + 90*0.5) = 17 + 21 + 45 = 83
//        Average GPA: 7.0 (based on NZ grading scheme)
//        Classification: A- (based on NZ grading scheme)
//        Minimum Possible Grade: 83 (since all as. are graded)
//        Maximum Possible Grade: 83 (since all as. are graded)
//        Grade Targets:
        
        return List.of(as1, as2, as3);
    }

    private List<AssessmentResponse> mockAssessment_partial(){
        AssessmentResponse as1 = new AssessmentResponse();
        as1.setId(1L);
        as1.setName("Assignment 1");
        as1.setWeight(BigDecimal.valueOf(20));
        as1.setGrade(BigDecimal.valueOf(85));
        as1.setCourseId(1L);

        AssessmentResponse as2 = new AssessmentResponse();
        as2.setId(2L);
        as2.setName("Midterm Exam");
        as2.setWeight(BigDecimal.valueOf(30));
        as2.setGrade(BigDecimal.valueOf(70));
        as2.setCourseId(1L);

        // Expected values for the full summary based on these assessments:
        // Course     ID: 1L
        // Average Grade: (85*0.2 + 70*0.3) / 0.5 = 76.0
        // Average GPA: 6.0 (based on NZ grading scheme)
        // Classification: B+ (based on NZ grading scheme)
        // Minimum Possible Grade: 38
        // Maximum Possible Grade: 88

        return List.of(as1, as2);
    }

    // ====== Test Cases ======

    @Test
    void getFullSummary_fullCourse() {
        Long courseId = 1L;
        List<AssessmentResponse> assessments = mockAssessments_fullCourse();

        // Mock the assessment service to return our predefined assessments
        Mockito.when(assessmentService.getAssessmentsByCourseId(courseId)).thenReturn(assessments);

        // Calculate expected values using the real grade scheme
        BigDecimal expectedGrade = BigDecimal.valueOf(83);
        BigDecimal expectedGpa = BigDecimal.valueOf(7.0);
        String expectedClassification = "A-";

        CourseGradeFullSummary summary = courseGradeSummaryService.getFullSummary(courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getCourseId()).isEqualTo(courseId);
        assertThat(summary.getAverageGrade()).isEqualByComparingTo(expectedGrade);
        assertThat(summary.getAverageGpa()).isEqualByComparingTo(expectedGpa);
        assertThat(summary.getClassification()).isEqualTo(expectedClassification);
        assertThat(summary.getMinPossibleGrade()).isEqualByComparingTo(expectedGrade);
        assertThat(summary.getMaxPossibleGrade()).isEqualByComparingTo(expectedGrade);
    }

    @Test
    void getFullSummary_partialCourse() {
        Long courseId = 1L;
        BigDecimal expectedGrade = BigDecimal.valueOf(76.0);
        BigDecimal expectedGpa = BigDecimal.valueOf(6.0);
        String expectedClassification = "B+";
        Mockito.when(assessmentService.getAssessmentsByCourseId(courseId)).thenReturn(mockAssessment_partial());

        CourseGradeFullSummary summary = courseGradeSummaryService.getFullSummary(courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getCourseId()).isEqualTo(courseId);
        assertThat(summary.getAverageGrade()).isEqualByComparingTo(expectedGrade);
        assertThat(summary.getAverageGpa()).isEqualByComparingTo(expectedGpa);
        assertThat(summary.getClassification()).isEqualTo(expectedClassification);
        assertThat(summary.getMinPossibleGrade()).isEqualByComparingTo(BigDecimal.valueOf(38));
        assertThat(summary.getMaxPossibleGrade()).isEqualByComparingTo(BigDecimal.valueOf(88));
        assertThat(summary.getGradeTargets()).hasSize(9);
        assertThat(summary.getGradeTargets()).extracting(CourseGradeTarget::getClassification)
                .containsExactlyInAnyOrder("A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D");
        assertThat(summary.getGradeTargets()).extracting(CourseGradeTarget::getRequiredAverage)
                .containsExactlyInAnyOrder(BigDecimal.valueOf(94.0), BigDecimal.valueOf(84.0), BigDecimal.valueOf(74.0),
                        BigDecimal.valueOf(64.0), BigDecimal.valueOf(54.0), BigDecimal.valueOf(44.0),
                        BigDecimal.valueOf(34.0), BigDecimal.valueOf(24.0), BigDecimal.valueOf(4.0));
    }
    @Test
    void getFullSummary_noAssessments() {
        Long courseId = 1L;
        Mockito.when(assessmentService.getAssessmentsByCourseId(courseId)).thenReturn(List.of());

        CourseGradeFullSummary summary = courseGradeSummaryService.getFullSummary(courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getCourseId()).isEqualTo(courseId);
        assertThat(summary.getAverageGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getAverageGpa()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getClassification()).isEqualTo("N/A");
        assertThat(summary.getMinPossibleGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getMaxPossibleGrade()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(summary.getGradeTargets()).hasSize(0);
    }

    @Test
    void getFullSummary_allUngraded() {
        Long courseId = 1L;
        AssessmentResponse as1 = new AssessmentResponse();
        as1.setId(1L);
        as1.setName("Assignment 1");
        as1.setWeight(BigDecimal.valueOf(20));
        as1.setGrade(null); // Ungraded
        as1.setCourseId(courseId);

        AssessmentResponse as2 = new AssessmentResponse();
        as2.setId(2L);
        as2.setName("Midterm Exam");
        as2.setWeight(BigDecimal.valueOf(30));
        as2.setGrade(null); // Ungraded
        as2.setCourseId(courseId);

        AssessmentResponse as3 = new AssessmentResponse();
        as3.setId(3L);
        as3.setName("Final Exam");
        as3.setWeight(BigDecimal.valueOf(50));
        as3.setGrade(null); // Ungraded
        as3.setCourseId(courseId);

        Mockito.when(assessmentService.getAssessmentsByCourseId(courseId)).thenReturn(List.of(as1, as2, as3));

        CourseGradeFullSummary summary = courseGradeSummaryService.getFullSummary(courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getCourseId()).isEqualTo(courseId);
        assertThat(summary.getAverageGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getAverageGpa()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getClassification()).isEqualTo("N/A");
        assertThat(summary.getMinPossibleGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getMaxPossibleGrade()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void getSimpleSummary() {
        Long courseId = 1L;
        List<AssessmentResponse> assessments = mockAssessments_fullCourse();

        Mockito.when(assessmentService.getAssessmentsByCourseId(courseId)).thenReturn(assessments);

        CourseGradeSimpleSummary summary = courseGradeSummaryService.getSimpleSummary(courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getCourseId()).isEqualTo(courseId);
        assertThat(summary.getAverageGrade()).isEqualByComparingTo(BigDecimal.valueOf(83));
        assertThat(summary.getAverageGpa()).isEqualByComparingTo(BigDecimal.valueOf(7.0));
        assertThat(summary.getClassification()).isEqualTo("A-");
    }
}
