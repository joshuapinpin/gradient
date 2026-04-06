package com.jpin.gradient.validation;

import com.jpin.gradient.dto.create.TermCreateRequest;
import jakarta.validation.ConstraintValidator;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, TermCreateRequest> {
    @Override
    public boolean isValid(TermCreateRequest request, jakarta.validation.ConstraintValidatorContext context) {
        // If either date is null, we consider it valid (optional fields); let @NotNull handle if needed
        if (request.getStartDate() == null || request.getEndDate() == null) return true;
        return request.getStartDate().isBefore(request.getEndDate());
    }
}
