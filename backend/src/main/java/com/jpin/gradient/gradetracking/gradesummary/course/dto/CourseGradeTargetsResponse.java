package com.jpin.gradient.gradetracking.gradesummary.course.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CourseGradeTargetsResponse {
    @NotNull
    private String targetGrade;

    @NotNull
    private BigDecimal requiredAverage;

    @NotNull
    private boolean achievable;

}
