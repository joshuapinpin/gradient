package com.jpin.gradient.gradetracking.gradesummary.course.dto;

import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CourseGradeFullSummary extends CourseGradeSimpleSummary {

    @NotNull
    private BigDecimal percentageGraded;

    @NotNull
    private BigDecimal minPossibleGrade;

    @NotNull
    private BigDecimal maxPossibleGrade;

    private final Map<GradeType, BigDecimal> gradeTargets = new HashMap<>();

    public void addGradeTarget(GradeType gradeType, BigDecimal target) {
        this.gradeTargets.put(gradeType, target);
    }
}
