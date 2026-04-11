package com.jpin.gradient.core.course;

import java.util.List;

public interface CourseService {
	CourseResponse createCourse(CourseCreateRequest request);
	CourseResponse getCourseById(Long id);
	List<CourseResponse> getCourses();
	CourseResponse updateCourse(Long id, CourseUpdateRequest request);
	void deleteCourse(Long id);
	void removeAssessmentFromCourse(Long courseId, Long assessmentId);
}
