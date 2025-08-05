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
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Grade() {}

    public Grade(double score, User user, Course course) {
        this.score = score;
        this.user = user;
        this.course = course;
    }

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    // Getters & setters...
}
