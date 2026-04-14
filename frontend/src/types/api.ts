export interface YearResponse {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
}

export interface TermResponse {
  id: number;
  name: string;
  startDate: string | null;
  endDate: string | null;
  yearId: number;
}

export interface CourseResponse {
  id: number;
  name: string;
  assessmentCount: number;
  termId: number;
}

export type AssessmentType = 'HOMEWORK' | 'QUIZ' | 'EXAM' | 'PROJECT' | 'LAB' | 'TUTORIAL' | 'OTHER';

export interface AssessmentResponse {
  id: number;
  name: string;
  weight: number;
  grade: number | null;
  dueDate: string | null;
  assessmentType: AssessmentType;
  courseId: number;
  graded: boolean;
}

export interface GradeSimpleSummary {
  averageGrade: number;
  averageGpa: number;
  classification: string;
}

export interface CourseGradeSimpleSummary extends GradeSimpleSummary {
  courseId: number;
}

export interface CourseGradeTarget {
  classification: string;
  requiredAverage: number;
}

export interface CourseGradeFullSummary extends CourseGradeSimpleSummary {
  percentageGraded: number;
  minPossibleGrade: number;
  maxPossibleGrade: number;
  gradeTargets: CourseGradeTarget[];
}

export interface TermGradeSimpleSummary extends GradeSimpleSummary {
  termId: number;
}

export interface YearGradeSimpleSummary extends GradeSimpleSummary {
  yearId: number;
}

export interface ApiError {
  error: string;
  message: string;
  fieldErrors?: Array<{ field: string; message: string }>;
}
