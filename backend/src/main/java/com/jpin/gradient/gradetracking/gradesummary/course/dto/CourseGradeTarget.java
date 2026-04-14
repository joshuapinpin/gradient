package com.jpin.gradient.gradetracking.gradesummary.course.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseGradeTarget {
    @NotBlank
    private String classification;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Average must be greater or equal to 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Average must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Average can have at most 3 digits and 2 decimal places")
    private BigDecimal requiredAverage;
}
