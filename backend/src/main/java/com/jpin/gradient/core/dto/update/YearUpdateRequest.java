package com.jpin.gradient.core.dto.update;

import com.jpin.gradient.core.validation.ValidDateRange;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ValidDateRange
public class YearUpdateRequest {

    @Size(max = 50)
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
