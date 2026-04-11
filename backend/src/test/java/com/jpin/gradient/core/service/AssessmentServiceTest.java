package com.jpin.gradient.core.service;

import com.jpin.gradient.core.assessment.AssessmentCreateRequest;
import com.jpin.gradient.core.assessment.AssessmentResponse;
import com.jpin.gradient.core.assessment.AssessmentUpdateRequest;
import com.jpin.gradient.core.shared.exception.ResourceNotFoundException;
import com.jpin.gradient.core.assessment.Assessment;
import com.jpin.gradient.core.assessment.AssessmentType;
import com.jpin.gradient.core.course.Course;
import com.jpin.gradient.core.term.Term;
import com.jpin.gradient.core.assessment.AssessmentRepository;
import com.jpin.gradient.core.course.CourseRepository;
import com.jpin.gradient.core.assessment.AssessmentServiceImpl;
import org.assertj.core.api.Assertions;
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
public class AssessmentServiceTest {
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    // -- Test Fixture Helpers --
    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Software Development");

        Term term = new Term();
        term.setId(1L);
        term.setName("Spring 2026");

        course.setTerm(term);
        return course;
    }

    /** ========== CREATE COURSE TESTS ========== */

    @Test
    void createAssessment_shouldReturnResponse() {
        AssessmentCreateRequest req = new AssessmentCreateRequest();
        req.setName("Midterm Exam");
        req.setWeight(new BigDecimal("0.3"));
        req.setAssessmentType(AssessmentType.EXAM);
        req.setCourseId(1L);

        Assessment assessment = new Assessment();
        assessment.setId(1L);
        assessment.setName(req.getName());
        assessment.setWeight(req.getWeight());
        assessment.setAssessmentType(req.getAssessmentType());
        assessment.setCourse(createCourse());

        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(createCourse()));

        AssessmentResponse resp = assessmentService.createAssessment(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getName()).isEqualTo("Midterm Exam");
        assertThat(resp.getWeight()).isEqualByComparingTo(new BigDecimal("0.3"));
        assertThat(resp.getAssessmentType()).isEqualTo(AssessmentType.EXAM);
        assertThat(resp.getCourseId()).isEqualTo(1L);
    }

    @Test
    void createAssessment_noCourseId() throws Exception{
        AssessmentCreateRequest req = new AssessmentCreateRequest();
        req.setName("Midterm Exam");
        req.setWeight(new BigDecimal("0.3"));
        req.setAssessmentType(AssessmentType.EXAM);
        // courseId is not set

        Assertions.assertThatThrownBy(() -> assessmentService.createAssessment(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found with id: null");
    }

    @Test
    void createAssessment_courseNotFound() throws Exception {
        AssessmentCreateRequest req = new AssessmentCreateRequest();
        req.setName("Midterm Exam");
        req.setWeight(new BigDecimal("0.3"));
        req.setAssessmentType(AssessmentType.EXAM);
        req.setCourseId(999L); // non-existent course ID

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> assessmentService.createAssessment(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found with id: 999");
    }


    /** ========== READ COURSE TESTS ========== */

    @Test
    void getAssessmentById_shouldReturnResponse() {
        Assessment assessment = new Assessment();
        assessment.setId(1L);
        assessment.setName("Midterm Exam");
        assessment.setWeight(new BigDecimal("0.3"));
        assessment.setAssessmentType(AssessmentType.EXAM);
        assessment.setCourse(createCourse());

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        AssessmentResponse resp = assessmentService.getAssessmentById(1L);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getName()).isEqualTo("Midterm Exam");
        assertThat(resp.getWeight()).isEqualByComparingTo(new BigDecimal("0.3"));
        assertThat(resp.getAssessmentType()).isEqualTo(AssessmentType.EXAM);
        assertThat(resp.getCourseId()).isEqualTo(1L);
    }

    @Test
    void getAssessmentById_notFound() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> assessmentService.getAssessmentById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Assessment not found with id: 1");
    }

    @Test
    void getAssessments_shouldReturnList() {
        Assessment assessment1 = new Assessment();
        assessment1.setId(1L);
        assessment1.setName("Midterm Exam");
        assessment1.setWeight(new BigDecimal("0.3"));
        assessment1.setAssessmentType(AssessmentType.EXAM);
        assessment1.setCourse(createCourse());

        Assessment assessment2 = new Assessment();
        assessment2.setId(2L);
        assessment2.setName("Final Exam");
        assessment2.setWeight(new BigDecimal("0.5"));
        assessment2.setAssessmentType(AssessmentType.EXAM);
        assessment2.setCourse(createCourse());

        when(assessmentRepository.findAll()).thenReturn(java.util.List.of(assessment1, assessment2));

        List<AssessmentResponse> responses = assessmentService.getAssessments();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getName()).isEqualTo("Midterm Exam");
        assertThat(responses.get(0).getWeight()).isEqualByComparingTo(new BigDecimal("0.3"));
        assertThat(responses.get(0).getAssessmentType()).isEqualTo(AssessmentType.EXAM);
        assertThat(responses.get(0).getCourseId()).isEqualTo(1L);

        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getName()).isEqualTo("Final Exam");
        assertThat(responses.get(1).getWeight()).isEqualByComparingTo(new BigDecimal("0.5"));
        assertThat(responses.get(1).getAssessmentType()).isEqualTo(AssessmentType.EXAM);
        assertThat(responses.get(1).getCourseId()).isEqualTo(1L);
    }

    /** ========== UPDATE COURSE TESTS ========== */

    @Test
    void updateAssessment_shouldReturnUpdatedResponse() {
        Assessment assessment = new Assessment();
        assessment.setId(1L);
        assessment.setName("Midterm Exam");
        assessment.setWeight(new BigDecimal("0.3"));
        assessment.setAssessmentType(AssessmentType.EXAM);
        assessment.setCourse(createCourse());

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        var updateReq = new AssessmentUpdateRequest();
        updateReq.setName("Updated Midterm Exam");
        updateReq.setWeight(new BigDecimal("0.35"));
        updateReq.setAssessmentType(AssessmentType.QUIZ);

        AssessmentResponse resp = assessmentService.updateAssessment(1L, updateReq);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getName()).isEqualTo("Updated Midterm Exam");
        assertThat(resp.getWeight()).isEqualByComparingTo(new BigDecimal("0.35"));
        assertThat(resp.getAssessmentType()).isEqualTo(AssessmentType.QUIZ);
        assertThat(resp.getCourseId()).isEqualTo(1L);
    }

    @Test
    void updateAssessment_notFound() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());

        var updateReq = new AssessmentUpdateRequest();
        updateReq.setName("Updated Midterm Exam");
        updateReq.setWeight(new BigDecimal("0.35"));
        updateReq.setAssessmentType(AssessmentType.QUIZ);

        Assertions.assertThatThrownBy(() -> assessmentService.updateAssessment(1L, updateReq))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Assessment not found with id: 1");
    }

    /** ========== DELETE  COURSE TESTS ========== */

    @Test
    void deleteAssessment_shouldDelete() {
        Assessment assessment = new Assessment();
        assessment.setId(1L);
        assessment.setName("Midterm Exam");
        assessment.setWeight(new BigDecimal("0.3"));
        assessment.setAssessmentType(AssessmentType.EXAM);
        assessment.setCourse(createCourse());

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        // No exception should be thrown
        assessmentService.deleteAssessment(1L);
    }

    @Test
    void deleteAssessment_notFound() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> assessmentService.deleteAssessment(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Assessment not found with id: 1");
    }


}
