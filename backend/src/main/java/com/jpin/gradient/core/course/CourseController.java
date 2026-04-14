package com.jpin.gradient.core.course;

import java.util.List;

import com.jpin.gradient.core.course.dto.CourseCreateRequest;
import com.jpin.gradient.core.course.dto.CourseResponse;
import com.jpin.gradient.core.course.dto.CourseUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(@Valid @RequestBody CourseCreateRequest request) {
        return courseService.createCourse(request);
    }

    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping
    public List<CourseResponse> list() {
        return courseService.getCourses();
    }

    @GetMapping("/term/{termId}")
    public List<CourseResponse> getByTermId(@PathVariable Long termId) {
        return courseService.getCoursesByTermId(termId);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @DeleteMapping("/{courseId}/assessments/{assessmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAssessmentFromCourse(@PathVariable Long courseId, @PathVariable Long assessmentId) {
        courseService.removeAssessmentFromCourse(courseId, assessmentId);
    }
}
