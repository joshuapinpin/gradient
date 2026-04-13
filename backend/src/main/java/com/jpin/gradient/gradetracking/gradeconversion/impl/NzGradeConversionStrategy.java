package com.jpin.gradient.gradetracking.gradeconversion.impl;

import com.jpin.gradient.gradetracking.gradeconversion.GradeConversionStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("nzGradeConversionStrategy")
public class NzGradeConversionStrategy implements GradeConversionStrategy {
    @Override
    public BigDecimal convertGradeToGpa(BigDecimal grade) {
        if(grade == null) throw new IllegalArgumentException("Grade cannot be null");
        else if(grade.compareTo(BigDecimal.valueOf(90)) >= 0) return BigDecimal.valueOf(9.0);
        else if(grade.compareTo(BigDecimal.valueOf(85)) >= 0) return BigDecimal.valueOf(8.0);
        else if(grade.compareTo(BigDecimal.valueOf(80)) >= 0) return BigDecimal.valueOf(7.0);
        else if(grade.compareTo(BigDecimal.valueOf(75)) >= 0) return BigDecimal.valueOf(6.0);
        else if(grade.compareTo(BigDecimal.valueOf(70)) >= 0) return BigDecimal.valueOf(5.0);
        else if(grade.compareTo(BigDecimal.valueOf(65)) >= 0) return BigDecimal.valueOf(4.0);
        else if(grade.compareTo(BigDecimal.valueOf(60)) >= 0) return BigDecimal.valueOf(3.0);
        else if(grade.compareTo(BigDecimal.valueOf(55)) >= 0) return BigDecimal.valueOf(2.0);
        else if(grade.compareTo(BigDecimal.valueOf(50)) >= 0) return BigDecimal.valueOf(1.0);
        return BigDecimal.ZERO;
    }

    @Override
    public String convertGradeToClassification(BigDecimal grade) {
        if(grade == null) throw new IllegalArgumentException("Grade cannot be null");
        else if(grade.compareTo(BigDecimal.valueOf(90)) >= 0) return "A+";
        else if(grade.compareTo(BigDecimal.valueOf(85)) >= 0) return "A";
        else if(grade.compareTo(BigDecimal.valueOf(80)) >= 0) return "A-";
        else if(grade.compareTo(BigDecimal.valueOf(75)) >= 0) return "B+";
        else if(grade.compareTo(BigDecimal.valueOf(70)) >= 0) return "B";
        else if(grade.compareTo(BigDecimal.valueOf(65)) >= 0) return "B-";
        else if(grade.compareTo(BigDecimal.valueOf(60)) >= 0) return "C+";
        else if(grade.compareTo(BigDecimal.valueOf(55)) >= 0) return "C";
        else if(grade.compareTo(BigDecimal.valueOf(50)) >= 0) return "C-";
        else if(grade.compareTo(BigDecimal.valueOf(40)) >= 0) return "D";
        return "F";
    }
}
