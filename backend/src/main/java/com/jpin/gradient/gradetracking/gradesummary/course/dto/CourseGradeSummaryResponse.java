package com.jpin.gradient.gradetracking.gradesummary.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseGradeSummaryResponse {

    @NotNull
    private Long courseId;

    @NotNull
    private BigDecimal averageGrade;

    @NotNull
    private BigDecimal averageGpa;

    @NotNull
    private String classification;
}
