package com.jpin.gradient.service;

import com.jpin.gradient.dto.create.CourseCreateRequest;
import com.jpin.gradient.dto.response.CourseResponse;
import com.jpin.gradient.model.Course;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.repository.AssessmentRepository;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @InjectMocks
    private CourseServiceImpl courseService;

    // --- Test fixture helpers ---
    private Term createTerm() {
        Term term = new Term();
        term.setId(1L);
        term.setName("Spring 2026");
        return term;
    }

    @Test
    void createCourse_shouldReturnCourseResponse() {
        CourseCreateRequest request = new CourseCreateRequest();
        request.setName("Test Course");
        request.setTermId(1L);

        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);

        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(assessmentRepository.countByCourseId(10L)).thenReturn(0L);

        CourseResponse response = courseService.createCourse(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Test Course");
        assertThat(response.getAssessmentCount()).isEqualTo(0);
    }
}
