# Gradient - Grade & Assignment Tracker

## Overview
A full-stack web application for tracking academic grades and assignments. Students can manage courses, track assignment status, and calculate required scores to achieve target grades.

## Architecture
- **Frontend**: React 19 + TypeScript, built with Vite, runs on port 5000
- **Backend**: Spring Boot 3.5.11 (Java 17), REST API, runs on port 8080
- **Database**: H2 in-memory database (development)

## Frontend Structure
```
frontend/src/
  types/
    index.ts              # Shared TypeScript interfaces
  data/
    mockData.ts           # Mock data for development
  components/
    layout/
      Sidebar.tsx/.module.css   # Navigation sidebar
      Layout.tsx/.module.css    # Main app shell
    ui/
      StatCard.tsx/.module.css  # Dashboard stat cards
      ProgressBar.tsx/.module.css
      Badge.tsx/.module.css     # Priority & status badges
  pages/
    Dashboard/            # Main overview with GPA, assignments, attendance
    GradeTracker/         # Expandable course grade cards
    AssessmentTracker/    # Filterable assignment table
    ScheduleTracker/      # Weekly timetable + attendance
  App.tsx                 # Page routing (state-based)
  index.css               # Global reset & typography
```

## Backend Structure
```
backend/src/main/java/com/jpin/gradient/
  core/                   # Year, Term, Course, Assessment JPA entities
  assessmenttracking/     # Assignment CRUD controllers/services
  gradetracking/          # Grade calculation services
  scheduletracking/       # Attendance tracking
```

## Workflows
- **Start application**: `cd frontend && npm run dev` (port 5000, webview)
- **Start Backend**: `cd backend && ./mvnw spring-boot:run` (port 8080, console)

## Key Configuration
- Vite proxy: `/api` requests forwarded to `http://localhost:8080`
- Java version: 17 (GraalVM 22.3 provides Java 19, targeting 17)
- `spring.jpa.properties.hibernate.globally_quoted_identifiers=true` fixes `year` reserved word in H2

## Design
- Inspired by academic tracker dashboard reference image
- Blue gradient sidebar with white navigation icons
- Stat cards with colored icons on pastel backgrounds
- Progress bars color-coded by grade/attendance level
- Priority badges (High/Medium/Low), Status badges (Not Started/In Progress/Completed)
- CSS Modules for scoped styling on all components/pages
