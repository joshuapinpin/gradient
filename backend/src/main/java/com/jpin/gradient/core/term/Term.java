package com.jpin.gradient.core.term;

import com.jpin.gradient.core.course.Course;
import com.jpin.gradient.core.year.Year;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // only include id in equals and hashcode
public class Term {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    @ToString.Exclude
    private Year year;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // to avoid circular reference
    private List<Course> courses = new ArrayList<>();

    /**
     * Adds a course to this term and sets the term on the course.
     */
    public void addCourse(Course course) {
        courses.add(course);
        course.setTerm(this);
    }

    /**
     * Removes a course from this term and unsets the term on the course.
     */
    public void removeCourse(Course course) {
        courses.remove(course);
        course.setTerm(null);
    }
}
