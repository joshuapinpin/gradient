package com.jpin.gradient.core.year;

import java.util.List;

public interface YearService {
    YearResponse createYear(YearCreateRequest request);
    YearResponse getYearById(Long id);
    List<YearResponse> getYears();
    YearResponse updateYear(Long id, YearUpdateRequest request);
    void deleteYear(Long id);
    void removeTermFromYear(Long yearId, Long termId);
}
