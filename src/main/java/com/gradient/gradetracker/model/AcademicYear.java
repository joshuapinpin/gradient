package com.gradient.gradetracker.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String startDate;
    private String endDate;

    // Many academic years belong to one user
    private User user;

    // One academic year can have multiple terms
    private List<Course> courses;

    public AcademicYear() {}

    public AcademicYear(String name, String startDate, String endDate, User user) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    // Getters and Setters
}
