package com.jpin.gradient.gradetracking.gradesummary.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseGradeTargetsResponse {
    @NotNull
    private String targetClassification;

    @NotNull
    private BigDecimal requiredAverage;
}
