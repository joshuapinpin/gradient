package com.jpin.gradient.service;

import com.jpin.gradient.dto.create.YearCreateRequest;
import com.jpin.gradient.dto.response.YearResponse;
import com.jpin.gradient.dto.update.YearUpdateRequest;

import java.util.List;

public interface YearService {
    YearResponse createYear(YearCreateRequest request);
    YearResponse getYearById(Long id);
    List<YearResponse> getYears();
    YearResponse updateYear(Long id, YearUpdateRequest request);
    void deleteYear(Long id);
    void removeTermFromYear(Long yearId, Long termId);
}
