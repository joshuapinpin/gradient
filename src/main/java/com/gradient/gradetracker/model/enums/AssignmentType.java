package com.gradient.gradetracker.model.enums;

public enum AssignmentType {
    HOMEWORK("Homework"),
    QUIZ("Quiz"),
    TEST("Test"),
    PROJECT("Project"),
    LAB("Lab");

    private final String typeName;

    AssignmentType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

}
