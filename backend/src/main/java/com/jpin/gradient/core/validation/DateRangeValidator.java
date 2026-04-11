package com.jpin.gradient.core.validation;

import com.jpin.gradient.core.dto.create.TermCreateRequest;
import com.jpin.gradient.core.dto.create.YearCreateRequest;
import com.jpin.gradient.core.dto.update.TermUpdateRequest;
import com.jpin.gradient.core.dto.update.YearUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (value instanceof TermCreateRequest req) {
            startDate = req.getStartDate();
            endDate = req.getEndDate();
        }
        else if (value instanceof TermUpdateRequest req) {
            startDate = req.getStartDate();
            endDate = req.getEndDate();
        }
        else if(value instanceof YearCreateRequest req) {
            startDate = req.getStartDate();
            endDate = req.getEndDate();
        }
        else if(value instanceof YearUpdateRequest req) {
            startDate = req.getStartDate();
            endDate = req.getEndDate();
        }
        else return true; // Not applicable

        // If either date is null, we consider it valid (optional fields); let @NotNull handle if needed
        if (startDate == null || endDate == null) return true;
        return startDate.isBefore(endDate);
    }
}
