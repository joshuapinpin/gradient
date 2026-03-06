package com.jpin.gradient.assignment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AssignmentResponse {
    private Long id;
    private String name;
    private BigDecimal weight;
    private BigDecimal score; // nullable
    private LocalDateTime dueDate;
    private AssignmentType assignmentType;
    private boolean graded;

    public void setScore(BigDecimal score) {
        this.score = score;
        this.graded = (score != null);
    }
}
