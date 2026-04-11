
package com.jpin.gradient.core.term;

import com.jpin.gradient.core.shared.validation.ValidDateRange;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ValidDateRange
public class TermUpdateRequest {

    @Size(max = 50)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
}
