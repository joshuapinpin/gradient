package com.jpin.gradient.assignment.dto;

import java.math.BigDecimal;

import com.jpin.gradient.assignment.AssignmentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AssignmentCreateRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private AssignmentType assignmentType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal weight;

}
