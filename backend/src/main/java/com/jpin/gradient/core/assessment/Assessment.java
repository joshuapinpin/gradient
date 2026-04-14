package com.jpin.gradient.core.assessment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jpin.gradient.core.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // only include id in equals and hashcode
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AssessmentType assessmentType;

    // can be a null as it may be empty at first
    private LocalDateTime dueDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Weight must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Weight can have at most 3 digits and 2 decimal places")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    @Column(precision = 5, scale = 2)
    private BigDecimal grade;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    private Course course;
}
