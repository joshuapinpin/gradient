package com.jpin.gradient.gradetracking.service;


import com.jpin.gradient.core.term.TermService;
import com.jpin.gradient.core.term.dto.TermResponse;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import com.jpin.gradient.gradetracking.gradeconversion.service.impl.NzGradeScheme;
import com.jpin.gradient.gradetracking.gradesummary.summary.TermGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.summary.YearGradeSimpleSummary;
import com.jpin.gradient.gradetracking.gradesummary.term.TermGradeSummaryServiceImpl;
import com.jpin.gradient.gradetracking.gradesummary.year.YearGradeSummaryServiceImpl;
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
public class YearGradeSummaryServiceImplTest {

    @Mock
    private TermService termService;

    @Mock
    private TermGradeSummaryServiceImpl termGradeSummaryService;

    @InjectMocks
    private YearGradeSummaryServiceImpl yearGradeSummaryService;

    @BeforeEach
    void setup(){
        GradeSchemeStrategy gradeSchemeStrategy = new NzGradeScheme();
        yearGradeSummaryService = new YearGradeSummaryServiceImpl(
                termService,
                termGradeSummaryService,
                gradeSchemeStrategy
        );
    }

    private List<TermResponse> mockTerms() {
        Long yearId = 1L;
        TermResponse term1 = new TermResponse();
        term1.setId(1L);
        term1.setYearId(yearId);

        TermResponse term2 = new TermResponse();
        term2.setId(2L);
        term2.setYearId(yearId);

        return List.of(term1, term2);
    }

    private List<TermGradeSimpleSummary> mockTermSummaries() {
        TermGradeSimpleSummary summary1 = new TermGradeSimpleSummary();
        summary1.setTermId(1L);
        summary1.setAverageGrade(BigDecimal.valueOf(87.5));
        summary1.setAverageGpa(BigDecimal.valueOf(8.0));
        summary1.setClassification("A");

        TermGradeSimpleSummary summary2 = new TermGradeSimpleSummary();
        summary2.setTermId(2L);
        summary2.setAverageGrade(BigDecimal.valueOf(82.5));
        summary2.setAverageGpa(BigDecimal.valueOf(7.0));
        summary2.setClassification("A-");

        return List.of(summary1, summary2);
    }

    @Test
    void getSimpleSummary_hasTerms(){
        Long yearId = 1L;

        List<TermResponse> terms = mockTerms();
        Mockito.when(termService.getTermsByYearId(yearId)).thenReturn(terms);

        List<TermGradeSimpleSummary> termSummaries = mockTermSummaries();
        Mockito.when(termGradeSummaryService.getSimpleSummary(1L)).thenReturn(termSummaries.get(0));
        Mockito.when(termGradeSummaryService.getSimpleSummary(2L)).thenReturn(termSummaries.get(1));

        YearGradeSimpleSummary yearSummary = yearGradeSummaryService.getSimpleSummary(yearId);

        assertThat(yearSummary).isNotNull();
        assertThat(yearSummary.getYearId()).isEqualTo(yearId);
        assertThat(yearSummary.getAverageGrade()).isEqualByComparingTo(BigDecimal.valueOf(85.0));
        assertThat(yearSummary.getAverageGpa()).isEqualByComparingTo(BigDecimal.valueOf(7.5));
        assertThat(yearSummary.getClassification()).isEqualTo("A");
    }

    @Test
    void getSimpleSummary_noTerms(){
        Long yearId = 1L;
        Mockito.when(termService.getTermsByYearId(yearId)).thenReturn(List.of());

        YearGradeSimpleSummary yearSummary = yearGradeSummaryService.getSimpleSummary(yearId);

        assertThat(yearSummary).isNotNull();
        assertThat(yearSummary.getYearId()).isEqualTo(yearId);
        assertThat(yearSummary.getAverageGrade()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(yearSummary.getAverageGpa()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(yearSummary.getClassification()).isEqualTo("N/A");
    }
}
