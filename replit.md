# Gradient - Grade Tracker

## Overview
A full-stack academic grade tracker. Students create academic years → terms → courses, add assessments with weights, enter grades, and the app shows GPA and grade averages via the Spring Boot backend (NZ grading scheme: A+ = 90–100, A = 85–89, etc.).

## Architecture
- **Frontend**: React 19 + TypeScript, Vite, port 5000
- **Backend**: Spring Boot 3.5.11 (Java 17), REST API, port 8080
- **Database**: H2 in-memory (development)
- **Proxy**: Vite forwards `/api/*` → `http://localhost:8080`

## Frontend Structure
```
frontend/src/
  api/
    client.ts             # Base fetch wrapper with error handling
    years.ts              # Year CRUD
    terms.ts              # Term CRUD
    courses.ts            # Course CRUD
    assessments.ts        # Assessment CRUD + grading
    grades.ts             # Grade summary endpoints
  types/
    api.ts                # Backend response types
    index.ts              # Page type and legacy badge types
  components/
    layout/
      Sidebar.tsx          # Nav: Dashboard + Grade Tracker only
      Layout.tsx
    ui/
      StatCard.tsx         # Dashboard stat cards
      ProgressBar.tsx      # Color-coded progress bar
      Badge.tsx            # Priority/status badges (legacy)
    modals/
      Modal.tsx            # Base modal with ESC/click-outside close
      Modal.module.css
      formStyles.module.css
      AddYearModal.tsx     # Create academic year (name, dates)
      AddTermModal.tsx     # Create term (name, yearId, optional dates)
      AddCourseModal.tsx   # Create course (name, termId)
      AddAssessmentModal.tsx # Create assessment (name, type, weight, dueDate)
      GradeModal.tsx       # Enter/edit grade for an assessment
  pages/
    Dashboard/             # Year GPA, term tabs, course grade cards, quick stats
    GradeTracker/          # Full CRUD: years/terms selector, course cards with
                           # expandable assessments, grade entry, grade targets
  App.tsx                  # Routes: dashboard | grades
```

## Backend API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/api/years` | List all / create year |
| GET | `/api/terms/year/{yearId}` | Terms for a year |
| POST | `/api/terms` | Create term |
| GET | `/api/courses/term/{termId}` | Courses for a term |
| POST | `/api/courses` | Create course |
| GET | `/api/assessments/course/{courseId}` | Assessments for a course |
| POST | `/api/assessments` | Create assessment |
| POST | `/api/assessments/{id}/grade` | Set/update grade |
| DELETE | `/api/assessments/{id}` | Delete assessment |
| DELETE | `/api/courses/{id}` | Delete course |
| GET | `/api/grade-summaries/course/{id}/full-summary` | Full summary (avg, GPA, classification, targets) |
| GET | `/api/grade-summaries/term/{id}/average` | Term average |
| GET | `/api/grade-summary/year/{id}/average` | Year average |

## NZ Grading Scheme
A+ ≥ 90 (GPA 9), A ≥ 85 (8), A- ≥ 80 (7), B+ ≥ 75 (6), B ≥ 70 (5), B- ≥ 65 (4), C+ ≥ 60 (3), C ≥ 55 (2), C- ≥ 50 (1), D ≥ 40 (0), F < 40 (0)

## Key Configuration
- `spring.jpa.properties.hibernate.globally_quoted_identifiers=true` — fixes `year` reserved word in H2
- Java version targeting: 17

## Workflows
- **Start application**: `cd frontend && npm run dev` (port 5000, webview)
- **Start Backend**: `cd backend && ./mvnw spring-boot:run` (port 8080)
