package com.jpin.gradient.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TermUpdateRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
}
