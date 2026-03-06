package com.jpin.gradient.dto.assessment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jpin.gradient.model.AssignmentType;
import lombok.Data;

@Data
public class AssignmentResponse {
    private Long id;
    private String name;
    private BigDecimal weight;
    private BigDecimal grade; // nullable
    private LocalDateTime dueDate;
    private AssignmentType assignmentType;
    private boolean graded;

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
        this.graded = (grade != null);
    }
}
