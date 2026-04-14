package com.jpin.gradient.gradetracking.gradeconversion.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeConversion {
    private BigDecimal grade;
    private BigDecimal gpaValue;
    private String classification;
}
