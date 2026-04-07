package com.jpin.gradient.service;

import com.jpin.gradient.dto.create.CourseCreateRequest;
import com.jpin.gradient.dto.response.CourseResponse;
import com.jpin.gradient.dto.update.CourseUpdateRequest;
import com.jpin.gradient.exception.ResourceNotFoundException;
import com.jpin.gradient.model.Assessment;
import com.jpin.gradient.model.AssessmentType;
import com.jpin.gradient.model.Course;
import com.jpin.gradient.model.Term;
import com.jpin.gradient.repository.AssessmentRepository;
import com.jpin.gradient.repository.CourseRepository;
import com.jpin.gradient.repository.TermRepository;
import com.jpin.gradient.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private TermRepository termRepository;
    @InjectMocks
    private CourseServiceImpl courseService;

    // --- Test fixture helpers ---
    private Term createTerm() {
        Term term = new Term();
        term.setId(1L);
        term.setName("Spring 2026");
        return term;
    }


    private Assessment createAssessment() {
        Assessment assessment = new Assessment();
        assessment.setId(5L);
        assessment.setName("Midterm Exam");
        assessment.setAssessmentType(AssessmentType.EXAM);
        assessment.setWeight(BigDecimal.valueOf(0.3));

        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(createTerm());

        assessment.setCourse(course);
        return assessment;
    }

    /** ========== CREATE COURSE TESTS ========== **/

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
        when(termRepository.findById(1L)).thenReturn(Optional.of(term));

        CourseResponse response = courseService.createCourse(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Test Course");
        assertThat(response.getAssessmentCount()).isEqualTo(0);
    }

    @Test
    void createCourse_invalidNoTermId(){
        CourseCreateRequest request = new CourseCreateRequest();
        request.setName("Test Course");
        // No term ID set

        try {
            courseService.createCourse(request);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ResourceNotFoundException.class)
                         .hasMessageContaining("Term not found with id: null");
        }
    }

    /** ========== READ COURSE TESTS ========== **/

    @Test
    void getCourseById_shouldReturnCourseResponse() {
        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(assessmentRepository.countByCourseId(10L)).thenReturn(0L);

        CourseResponse response = courseService.getCourseById(10L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Test Course");
        assertThat(response.getAssessmentCount()).isEqualTo(0);
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            courseService.getCourseById(999L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ResourceNotFoundException.class)
                         .hasMessageContaining("Course not found with id: 999");
        }
    }

    @Test
    void getCourses_shouldReturnListOfCourseResponses() {
        Term term = createTerm();
        Course course1 = new Course();
        course1.setId(10L);
        course1.setName("Course 1");
        course1.setTerm(term);

        Course course2 = new Course();
        course2.setId(20L);
        course2.setName("Course 2");
        course2.setTerm(term);

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
        when(assessmentRepository.countByCourseId(10L)).thenReturn(0L);
        when(assessmentRepository.countByCourseId(20L)).thenReturn(0L);

        List<CourseResponse> responses = courseService.getCourses();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(10L);
        assertThat(responses.get(0).getName()).isEqualTo("Course 1");
        assertThat(responses.get(0).getAssessmentCount()).isEqualTo(0);
        assertThat(responses.get(1).getId()).isEqualTo(20L);
        assertThat(responses.get(1).getName()).isEqualTo("Course 2");
        assertThat(responses.get(1).getAssessmentCount()).isEqualTo(0);
    }

    /** ========== UPDATE COURSE TESTS ========== **/

    @Test
    void updateCourse_shouldReturnUpdatedCourseResponse() {
        CourseUpdateRequest request = new CourseUpdateRequest();
        request.setName("Updated Course");

        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);

        Course updatedCourse = new Course();
        updatedCourse.setId(10L);
        updatedCourse.setName("Updated Course");
        updatedCourse.setTerm(term);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(assessmentRepository.countByCourseId(10L)).thenReturn(0L);

        CourseResponse response = courseService.updateCourse(10L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Updated Course");
        assertThat(response.getAssessmentCount()).isEqualTo(0);
    }

    @Test
    void updateCourse_notFound() {
        CourseUpdateRequest request = new CourseUpdateRequest();
        request.setName("Updated Course");

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            courseService.updateCourse(999L, request);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ResourceNotFoundException.class)
                         .hasMessageContaining("Course not found with id: 999");
        }
    }

    @Test
    void updateCourse_changeName(){
        CourseUpdateRequest request = new CourseUpdateRequest();
        request.setName("New Name");

        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Old Name");
        course.setTerm(term);

        Course updatedCourse = new Course();
        updatedCourse.setId(10L);
        updatedCourse.setName("New Name");
        updatedCourse.setTerm(term);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(assessmentRepository.countByCourseId(10L)).thenReturn(0L);

        CourseResponse response = courseService.updateCourse(10L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("New Name");
        assertThat(response.getAssessmentCount()).isEqualTo(0);
    }

    /** ========== DELETE COURSE TESTS ========== **/

    @Test
    void deleteCourse_shouldDeleteCourse() {
        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        // No exception means success
        courseService.deleteCourse(10L);
    }

    @Test
    void deleteCourse_notFound(){
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            courseService.deleteCourse(999L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ResourceNotFoundException.class)
                         .hasMessageContaining("Course not found with id: 999");
        }
    }

    @Test
    void removeAssessmentFromCourse_shouldRemoveAssessment() {
        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);
        course.addAssessment(createAssessment());

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        // No exception means success
        courseService.removeAssessmentFromCourse(10L, 5L);
    }

    @Test
    void removeAssessmentFromCourse_assessmentNotFound() {
        Term term = createTerm();
        Course course = new Course();
        course.setId(10L);
        course.setName("Test Course");
        course.setTerm(term);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        try {
            courseService.removeAssessmentFromCourse(10L, 999L);
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("Assessment not found in course with id: 999");
        }
    }
}
