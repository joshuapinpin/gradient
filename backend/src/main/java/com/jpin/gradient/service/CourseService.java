package com.jpin.gradient.service;

import com.jpin.gradient.dto.course.CourseCreateRequest;
import com.jpin.gradient.dto.course.CourseResponse;
import com.jpin.gradient.dto.course.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
	CourseResponse createCourse(CourseCreateRequest request);
	CourseResponse getCourseById(Long id);
	List<CourseResponse> getCourses();
	CourseResponse updateCourse(Long id, CourseUpdateRequest request);
	void deleteCourse(Long id);
	void removeAssessmentFromCourse(Long courseId, Long assessmentId);
}
