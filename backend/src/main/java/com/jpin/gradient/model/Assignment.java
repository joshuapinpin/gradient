package com.jpin.gradient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    /** TO:DO - consider using an enum for assignment types (e.g., Homework, Quiz, Exam) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private String assignmentType;

    // doesn't have to null as it may be empty at first
    private LocalDateTime dueDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Grade must be a non-negative number")
    @DecimalMax(value = "100.0", inclusive = true, message = "Grade must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Grade can have at most 3 digits and 2 decimal places")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    @Column(precision = 5, scale = 2)
    private BigDecimal score;
}
