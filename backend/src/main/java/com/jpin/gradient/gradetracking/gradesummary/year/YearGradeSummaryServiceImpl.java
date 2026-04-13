package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.year.dto.YearGradeSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class YearGradeSummaryServiceImpl implements YearGradeSummaryService{
    @Override
    public YearGradeSummaryResponse getAverageGradeForYear(Long yearId) {
        return null;
    }
}
