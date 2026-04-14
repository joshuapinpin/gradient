package com.jpin.gradient.gradetracking.gradesummary.summary;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeSimpleSummary {

    @NotNull
    private BigDecimal averageGrade;

    @NotNull
    private BigDecimal averageGpa;

    @NotNull
    private String classification;

}
