package com.jpin.gradient.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // only include id in equals and hashcode
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // to avoid circular reference
    private Set<Assessment> assessments;

    /**
     * Adds an assessment to this course and sets the course on the assessment.
     */
    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
        assessment.setCourse(this);
    }

    /**
     * Removes an assessment from this course and unsets the course on the assessment.
     */
    public void removeAssessment(Assessment assessment) {
        assessments.remove(assessment);
        assessment.setCourse(null);
    }
}
