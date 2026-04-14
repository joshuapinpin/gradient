import { api } from './client';
import type {
  CourseGradeFullSummary,
  CourseGradeSimpleSummary,
  TermGradeSimpleSummary,
  YearGradeSimpleSummary,
} from '../types/api';

export const gradesApi = {
  courseAverage: (courseId: number) =>
    api.get<CourseGradeSimpleSummary>(`/api/grade-summaries/course/${courseId}/average`),
  courseFullSummary: (courseId: number) =>
    api.get<CourseGradeFullSummary>(`/api/grade-summaries/course/${courseId}/full-summary`),
  termAverage: (termId: number) =>
    api.get<TermGradeSimpleSummary>(`/api/grade-summaries/term/${termId}/average`),
  yearAverage: (yearId: number) =>
    api.get<YearGradeSimpleSummary>(`/api/grade-summary/year/${yearId}/average`),
};
