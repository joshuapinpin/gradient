package com.jpin.gradient.service;

import com.jpin.gradient.dto.create.CourseCreateRequest;
import com.jpin.gradient.dto.response.CourseResponse;
import com.jpin.gradient.dto.update.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
	CourseResponse createCourse(CourseCreateRequest request);
	CourseResponse getCourseById(Long id);
	List<CourseResponse> getCourses();
	CourseResponse updateCourse(Long id, CourseUpdateRequest request);
	void deleteCourse(Long id);
	void removeAssessmentFromCourse(Long courseId, Long assessmentId);
}
