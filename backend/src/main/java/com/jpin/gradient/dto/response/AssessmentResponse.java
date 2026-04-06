package com.jpin.gradient.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jpin.gradient.model.AssessmentType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssessmentResponse {
    private Long id;
    private String name;
    private BigDecimal weight;
    private BigDecimal grade; // nullable
    private LocalDateTime dueDate;
    private AssessmentType assessmentType;
    private Long courseId;
    private boolean graded;

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
        this.graded = (grade != null);
    }
}
