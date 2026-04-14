package com.jpin.gradient.gradetracking.gradesummary.summary;

import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeTarget;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseGradeFullSummary extends CourseGradeSimpleSummary {

    @NotNull
    private BigDecimal percentageGraded;

    @NotNull
    private BigDecimal minPossibleGrade;

    @NotNull
    private BigDecimal maxPossibleGrade;

    private final List<CourseGradeTarget> gradeTargets = new ArrayList<>();

    public void addGradeTarget(CourseGradeTarget target) {
        this.gradeTargets.add(target);
    }
}
