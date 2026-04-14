package com.jpin.gradient.gradetracking.gradesummary.summary;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseGradeSimpleSummary extends GradeSimpleSummary {
    @NotNull
    private Long courseId;
}
