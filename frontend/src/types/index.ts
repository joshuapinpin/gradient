export type Page = 'dashboard' | 'grades';
export type Priority = 'High' | 'Medium' | 'Low';
export type AssessmentStatus = 'Not Started' | 'In Progress' | 'Completed';

export interface Assessment {
  id: string;
  name: string;
  courseCode: string;
  courseFullName: string;
  type: string;
  priority: Priority;
  status: AssessmentStatus;
  progress: number;
  dueDate: string;
  daysLeft: number;
  weight: number;
  grade?: number;
}

export interface AssessmentWeight {
  name: string;
  grade: number;
  weight: number;
}

export interface Course {
  id: string;
  code: string;
  name: string;
  currentGrade: number;
  letterGrade: string;
  targetGrade: string;
  requiredAvg: number;
  assignmentWeightsTotal: number;
  assessments: AssessmentWeight[];
}

export interface AttendanceRecord {
  courseCode: string;
  courseName: string;
  attended: number;
  total: number;
  percentage: number;
  missedNotes?: string;
  reviewLink?: string;
  perfect?: boolean;
}

export interface ScheduleEntry {
  id: string;
  courseCode: string;
  courseName: string;
  type: 'Lecture' | 'Tutorial' | 'Lab';
  day: string;
  startTime: string;
  endTime: string;
  location: string;
  lecturer?: string;
}

export interface Term {
  id: string;
  label: string;
}

export interface UserProfile {
  name: string;
  program: string;
  initials: string;
}
