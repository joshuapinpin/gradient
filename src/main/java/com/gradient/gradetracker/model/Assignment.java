// 1. Assignment Entity (entity/Assignment.java)
package com.gradient.gradetracker.model;

import java.time.LocalDate;

import com.gradient.gradetracker.model.enums.AssignmentType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Assignment name is required")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Assignment type is required")
    @Enumerated(EnumType.STRING)
    private AssignmentType type;

    @NotNull(message = "Due date is required")
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "weight_percentage")
    private Double weightPercentage; // For weighted grading

    // Many assignments belong to one course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToOne(mappedBy = "assignment", cascade = CascadeType.ALL)
    private Grade grade;

    // Constructors
    public Assignment() {}

    public Assignment(String name, String description, AssignmentType type,
                      LocalDate dueDate, Course course) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.dueDate = dueDate;
        this.course = course;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AssignmentType getType() { return type; }
    public void setType(AssignmentType type) { this.type = type; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Double getWeightPercentage() { return weightPercentage; }
    public void setWeightPercentage(Double weightPercentage) { this.weightPercentage = weightPercentage; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }
}