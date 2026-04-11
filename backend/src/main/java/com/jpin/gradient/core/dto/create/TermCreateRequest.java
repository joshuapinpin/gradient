package com.jpin.gradient.core.dto.create;

import com.jpin.gradient.core.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@ValidDateRange
public class TermCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    // Optional start and end dates
    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private Long yearId;
}
