package com.jpin.gradient.dto.update;

import com.jpin.gradient.model.AssessmentType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class AssessmentUpdateRequest {

    @Size(max = 50)
    @NotBlank
    private String name;

    // optional update; if provided must still be (0, 100]
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal weight;

    private LocalDateTime dueDate;

    private AssessmentType assessmentType;

    private Long courseId;

}
