package com.jpin.gradient.dto.create;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jpin.gradient.model.AssessmentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssessmentCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private AssessmentType assessmentType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal weight;

    @NotNull
    private Long courseId;

    private LocalDateTime dueDate;

}
