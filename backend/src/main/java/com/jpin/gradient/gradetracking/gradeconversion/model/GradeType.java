package com.jpin.gradient.gradetracking.gradeconversion.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class GradeType {
    private final BigDecimal minGrade;
    private final BigDecimal maxGrade;
    private final BigDecimal gpaValue;
    private final String classification;

    public GradeType(BigDecimal minGrade, BigDecimal maxGrade, BigDecimal gpaValue, String classification) {
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
        this.gpaValue = gpaValue;
        this.classification = classification;
    }
}
