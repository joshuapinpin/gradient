package com.gradient.gradetracker.model;

import jakarta.persistence.*;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double score;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Grade() {}

    public Grade(double score, Student student, Course course) {
        this.score = score;
        this.student = student;
        this.course = course;
    }

    // Getters & setters...
}
