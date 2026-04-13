package com.jpin.gradient.core.year;

import com.jpin.gradient.core.year.dto.YearCreateRequest;
import com.jpin.gradient.core.year.dto.YearResponse;
import com.jpin.gradient.core.year.dto.YearUpdateRequest;

import java.util.List;

public interface YearService {
    YearResponse createYear(YearCreateRequest request);
    YearResponse getYearById(Long id);
    List<YearResponse> getYears();
    YearResponse updateYear(Long id, YearUpdateRequest request);
    void deleteYear(Long id);
    void removeTermFromYear(Long yearId, Long termId);
}
