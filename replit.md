# Gradient - Grade & Assignment Tracker

## Overview
A full-stack web application for tracking academic grades and assignments. Students can manage courses, track assignment status, and calculate required scores to achieve target grades.

## Architecture
- **Frontend**: React 19 + TypeScript, built with Vite, runs on port 5000
- **Backend**: Spring Boot 3.5.11 (Java 17), REST API, runs on port 8080
- **Database**: H2 in-memory database (development)

## Project Structure
```
/
├── frontend/          # React + Vite app (port 5000)
│   ├── src/
│   │   ├── App.tsx
│   │   └── main.tsx
│   └── vite.config.ts
├── backend/           # Spring Boot app (port 8080)
│   ├── src/main/java/com/jpin/gradient/
│   │   ├── core/          # Year, Term, Course, Assessment entities
│   │   ├── assessmenttracking/
│   │   ├── gradetracking/
│   │   └── scheduletracking/
│   └── src/main/resources/application.properties
└── replit.md
```

## Workflows
- **Start application**: `cd frontend && npm run dev` (port 5000, webview)
- **Start Backend**: `cd backend && ./mvnw spring-boot:run` (port 8080, console)

## Key Configuration
- Vite proxy: `/api` requests forwarded to `http://localhost:8080`
- Java version: 17 (GraalVM 22.3 provides Java 19, targeting 17)
- H2 globally quoted identifiers enabled (avoids reserved word conflicts)
- `spring.jpa.properties.hibernate.globally_quoted_identifiers=true` fixes `year` reserved word

## Features
- Grade tracking with course averages
- Assignment management (status, priority, due dates)
- Calculate required scores for target grades
- Attendance tracking
