package com.gradient.gradetracker.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Grade> grades;

    public Course() {}

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters & setters...
}
