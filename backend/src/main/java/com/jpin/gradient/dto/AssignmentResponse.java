package com.jpin.gradient.dto;

import com.jpin.gradient.model.AssignmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
