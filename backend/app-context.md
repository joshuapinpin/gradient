# Grade & Assignment Tracker

## Project Overview

Full-stack web application for tracking academic grades, calculating required scores for target grades, and managing assignments with deadlines, status, and progress tracking.

## Core Features

The application consists of two interconnected tracking systems that share common data objects:

### 1. Grade Tracking System

Track and analyze academic performance across courses and terms.

**Key Components:**

- **Term/Period Management**
  - Support for various academic periods (trimesters, terms, full year)
  - Examples: "Trimester 1", "Term 2", "2024 Academic Year"

- **Course Organization**
  - Track multiple courses per term
  - Example: SWEN221, COMP102, etc.

- **Assignment Grade Tracking**
  - Individual assignments with grades and weights
  - Example: Test 1, Project 2, Final Exam
  - Weight validation: All assignment weights must sum to 100% per course

- **Performance Analytics**
  - Calculate current course averages
  - **Target Grade Calculator**: Determine required average score on remaining assignments to achieve a desired final grade
  - Example: "To achieve A+ (90%), you need an average of 85% on remaining assignments"

### 2. Assignment/Deadline Tracking System

Manage assignment lifecycle from creation to completion.

**Tracked Attributes:**

- **Basic Information**
  - Assignment name
  - Associated course
  - Assessment type (test, project, homework, etc.)

- **Management Fields** (all user-editable)
  - Priority level
  - Status (not started, in progress, completed, etc.)
  - Progress percentage

- **Time Tracking**
  - Due date
  - Days remaining until due date
  - Countdown display

## Data Architecture

### Interconnected Design

Both tracking systems utilize shared data objects to maintain consistency:

- A single assignment object serves both the grade tracker and deadline tracker
- Changes in one system reflect in the other
- Unified course and term structure across features

**Example Flow:**
```
Assignment Object
├── Grade Tracking Data
│   ├── Grade received
│   └── Weight percentage
└── Deadline Tracking Data
    ├── Due date
    ├── Priority level
    ├── Status
    └── Progress
```

## Technical Requirements

- Full-stack implementation
- Data persistence for terms, courses, and assignments
- Real-time calculations for grade requirements
- Dynamic deadline countdown
- User interface for managing all tracking features