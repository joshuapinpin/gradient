package com.jpin.gradient.gradetracking.gradesummary.year;

import com.jpin.gradient.gradetracking.gradesummary.course.CourseGradeSummaryService;
import com.jpin.gradient.gradetracking.gradesummary.summary.CourseGradeSimpleSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class YearGradeSummaryServiceImpl implements YearGradeSummaryService {

    private final CourseGradeSummaryService courseGradeSummaryService;

    public YearGradeSummaryServiceImpl(CourseGradeSummaryService courseGradeSummaryService) {
        this.courseGradeSummaryService = courseGradeSummaryService;
    }


    /**
     * @param yearId
     * @return
     */
    @Override
    public CourseGradeSimpleSummary getSimpleSummary(Long yearId) {
        return null;
    }
}
