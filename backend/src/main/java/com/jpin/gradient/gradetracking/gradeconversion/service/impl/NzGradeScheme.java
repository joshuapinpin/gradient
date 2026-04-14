package com.jpin.gradient.gradetracking.gradeconversion.service.impl;

import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;
import com.jpin.gradient.gradetracking.gradeconversion.service.GradeSchemeStrategy;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Service("nzGradeConversionStrategy")
public class NzGradeScheme implements GradeSchemeStrategy {
    public static final List<GradeType> GRADE_LIST = List.of(
            new GradeType(BigDecimal.valueOf(90), BigDecimal.valueOf(100), BigDecimal.valueOf(9.0), "A+"),
            new GradeType(BigDecimal.valueOf(85), BigDecimal.valueOf(89.99), BigDecimal.valueOf(8.0), "A"),
            new GradeType(BigDecimal.valueOf(80), BigDecimal.valueOf(84.99), BigDecimal.valueOf(7.0), "A-"),
            new GradeType(BigDecimal.valueOf(75), BigDecimal.valueOf(79.99), BigDecimal.valueOf(6.0), "B+"),
            new GradeType(BigDecimal.valueOf(70), BigDecimal.valueOf(74.99), BigDecimal.valueOf(5.0), "B"),
            new GradeType(BigDecimal.valueOf(65), BigDecimal.valueOf(69.99), BigDecimal.valueOf(4.0), "B-"),
            new GradeType(BigDecimal.valueOf(60), BigDecimal.valueOf(64.99), BigDecimal.valueOf(3.0), "C+"),
            new GradeType(BigDecimal.valueOf(55), BigDecimal.valueOf(59.99), BigDecimal.valueOf(2.0), "C"),
            new GradeType(BigDecimal.valueOf(50), BigDecimal.valueOf(54.99), BigDecimal.valueOf(1.0), "C-"),
            new GradeType(BigDecimal.valueOf(40), BigDecimal.valueOf(49.99), BigDecimal.ZERO, "D"),
            new GradeType(BigDecimal.ZERO, BigDecimal.valueOf(39.99), BigDecimal.ZERO, "F")
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
    public List<GradeType> getGradeTypes() {
        return GRADE_LIST;
    }
}
