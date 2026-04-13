package com.jpin.gradient.gradetracking.gradeconversion.service;

import com.jpin.gradient.gradetracking.gradeconversion.model.GradeConversion;
import com.jpin.gradient.gradetracking.gradeconversion.model.GradeType;

import java.math.BigDecimal;
import java.util.List;

public interface GradeSchemeStrategy {
    GradeConversion convertGrade(BigDecimal grade);
    List<GradeType> getGradeTypes();
}
