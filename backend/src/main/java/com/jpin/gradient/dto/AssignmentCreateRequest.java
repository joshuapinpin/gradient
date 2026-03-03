package com.jpin.gradient.dto;

import com.jpin.gradient.model.AssignmentType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
