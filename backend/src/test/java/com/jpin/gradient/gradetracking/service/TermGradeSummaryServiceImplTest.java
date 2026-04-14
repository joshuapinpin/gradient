package com.jpin.gradient.gradetracking.service;

import com.jpin.gradient.core.course.CourseService;
import com.jpin.gradient.core.course.dto.CourseResponse;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradeconversion.service.impl.NzGradeScheme;
import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryServiceImpl;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.term.TermGradeSummaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TermGradeSummaryServiceImplTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseGradeSummaryServiceImpl courseGradeSummaryService;

    @InjectMocks
    private TermGradeSummaryServiceImpl termGradeSummaryService;

    private GradeSchemeStrategy gradeSchemeStrategy;

    @BeforeEach
    void setup(){
        gradeSchemeStrategy = new NzGradeScheme();
        termGradeSummaryService = new TermGradeSummaryServiceImpl(
                courseService,
                courseGradeSummaryService,
                gradeSchemeStrategy
        );
    }
    
    private List<CourseResponse> mockCourses() {
        Long termId = 1L;
        CourseResponse course1 = new CourseResponse();
        course1.setId(1L);
        course1.setTermId(termId);

        CourseResponse course2 = new CourseResponse();
        course2.setId(2L);
        course2.setTermId(termId);

        return List.of(course1, course2);
    }

    private List<CourseGradeSimpleSummary> mockCourseSummaries() {
        CourseGradeSimpleSummary summary1 = new CourseGradeSimpleSummary();
        summary1.setCourseId(1L);
        summary1.setAverageGrade(new java.math.BigDecimal("85"));
        summary1.setClassification("A");

        CourseGradeSimpleSummary summary2 = new CourseGradeSimpleSummary();
        summary2.setCourseId(2L);
        summary2.setAverageGrade(new java.math.BigDecimal("80"));
        summary2.setClassification("A-");

        return List.of(summary1, summary2);
    }

    @Test
    void getSimpleSummary_hasCourses() {
        Long termId = 1L;
        List<CourseResponse> mockCourses = mockCourses();
        Mockito.when(courseService.getCoursesByTermId(termId)).thenReturn(mockCourses);

        List<CourseGradeSimpleSummary> mockSummaries = mockCourseSummaries();
        Mockito.when(courseGradeSummaryService.getSimpleSummary(1L)).thenReturn(mockSummaries.get(0));
        Mockito.when(courseGradeSummaryService.getSimpleSummary(2L)).thenReturn(mockSummaries.get(1));

        TermGradeSimpleSummary termSummary = termGradeSummaryService.getSimpleSummary(termId);

        assertThat(termSummary).isNotNull();
        assertThat(termSummary.getTermId()).isEqualTo(termId);
        assertThat(termSummary.getAverageGrade()).isEqualByComparingTo(BigDecimal.valueOf(82.5));
        assertThat(termSummary.getAverageGpa()).isEqualByComparingTo(BigDecimal.valueOf(7.5));
        assertThat(termSummary.getClassification()).isEqualTo("A-");
    }

    @Test
    void getSimpleSummary_noCourses() {
        Long termId = 1L;
        Mockito.when(courseService.getCoursesByTermId(termId)).thenReturn(List.of());

        TermGradeSimpleSummary termSummary = termGradeSummaryService.getSimpleSummary(termId);

        assertThat(termSummary).isNotNull();
        assertThat(termSummary.getTermId()).isEqualTo(termId);
        assertThat(termSummary.getAverageGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(termSummary.getAverageGpa()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(termSummary.getClassification()).isEqualTo("N/A");
    }
}
