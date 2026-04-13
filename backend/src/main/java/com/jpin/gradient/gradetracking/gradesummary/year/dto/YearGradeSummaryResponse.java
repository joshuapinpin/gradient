package com.jpin.gradient.gradetracking.gradesummary.year.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class YearGradeSummaryResponse {

    @NotNull
    private Long yearId;

    @NotBlank
    private String yearName;

    @NotNull
    private BigDecimal averageGrade;

    @NotNull
    private BigDecimal averageGpa;

    @NotNull
    private String averageGradeClass;
}
