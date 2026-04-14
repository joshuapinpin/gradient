package com.jpin.gradient.core.course;

import com.jpin.gradient.core.course.dto.CourseCreateRequest;
import com.jpin.gradient.core.course.dto.CourseResponse;
import com.jpin.gradient.core.course.dto.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
	CourseResponse createCourse(CourseCreateRequest request);
	CourseResponse getCourseById(Long id);
	List<CourseResponse> getCourses();
	List<CourseResponse> getCoursesByTermId(Long termId);
	CourseResponse updateCourse(Long id, CourseUpdateRequest request);
	void deleteCourse(Long id);
	void removeAssessmentFromCourse(Long courseId, Long assessmentId);
}
