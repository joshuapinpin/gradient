package com.jpin.gradient.gradetracking.gradeconversion;

import java.math.BigDecimal;

public interface GradeConversionStrategy {
    BigDecimal convertGradeToGpa(BigDecimal grade);
    String convertGradeToClassification(BigDecimal grade);
}
