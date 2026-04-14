package com.jpin.gradient.gradetracking.gradeconversion.service.impl;

import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AusGradeScheme implements GradeSchemeStrategy {
    private static final List<GradeType> GRADE_LIST = List.of(
            new GradeType(BigDecimal.valueOf(85), BigDecimal.valueOf(100), BigDecimal.valueOf(7.0), "HD"),
            new GradeType(BigDecimal.valueOf(75), BigDecimal.valueOf(84.99), BigDecimal.valueOf(6.0), "D"),
            new GradeType(BigDecimal.valueOf(65), BigDecimal.valueOf(74.99), BigDecimal.valueOf(5.0), "C"),
            new GradeType(BigDecimal.valueOf(50), BigDecimal.valueOf(64.99), BigDecimal.valueOf(4.0), "P"),
            new GradeType(BigDecimal.ZERO, BigDecimal.valueOf(49.99), BigDecimal.ZERO, "F")
    );

    @Override
    public GradeConversion convertGrade(BigDecimal grade) {
        for (GradeType scheme : GRADE_LIST) {
            if (grade.compareTo(scheme.getMinGrade()) >= 0) {
                GradeConversion conversion = new GradeConversion();
                conversion.setGrade(grade);
                conversion.setGpaValue(scheme.getGpaValue());
                conversion.setClassification(scheme.getClassification());
                return conversion;
            }
        }
        throw new IllegalArgumentException("GradeType is out of range: " + grade);
    }

    @Override
    public String convertToClassification(BigDecimal grade) {
        for (GradeType scheme : GRADE_LIST) {
            if (grade.compareTo(scheme.getMinGrade()) >= 0) {
                return scheme.getClassification();
            }
        }
        throw new IllegalArgumentException("GradeType is out of range: " + grade);
    }

    @Override
    public List<GradeType> getGradeTypes() {
        return GRADE_LIST;
    }
}
