package com.jpin.gradient.core.service;

import com.jpin.gradient.core.dto.create.YearCreateRequest;
import com.jpin.gradient.core.dto.response.YearResponse;
import com.jpin.gradient.core.dto.update.YearUpdateRequest;

import java.util.List;

public interface YearService {
    YearResponse createYear(YearCreateRequest request);
    YearResponse getYearById(Long id);
    List<YearResponse> getYears();
    YearResponse updateYear(Long id, YearUpdateRequest request);
    void deleteYear(Long id);
    void removeTermFromYear(Long yearId, Long termId);
}
