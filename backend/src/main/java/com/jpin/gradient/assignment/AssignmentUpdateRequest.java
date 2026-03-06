package com.jpin.gradient.assignment;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AssignmentUpdateRequest {

    @Size(max = 200)
    private String name;

    // optional update; if provided must still be (0, 100]
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal weight;

    private LocalDateTime dueDate;

    private AssignmentType assignmentType;

}
