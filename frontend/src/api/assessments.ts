import { api } from './client';
import type { AssessmentResponse, AssessmentType } from '../types/api';

export const assessmentsApi = {
  listByCourse: (courseId: number) =>
    api.get<AssessmentResponse[]>(`/api/assessments/course/${courseId}`),
  create: (body: {
    name: string;
    assessmentType: AssessmentType;
    weight: number;
    courseId: number;
    dueDate?: string;
  }) => api.post<AssessmentResponse>('/api/assessments', body),
  update: (id: number, body: {
    name?: string;
    weight?: number;
    dueDate?: string;
    assessmentType?: AssessmentType;
  }) => api.put<AssessmentResponse>(`/api/assessments/${id}`, body),
  grade: (id: number, grade: number | null) =>
    api.post<AssessmentResponse>(`/api/assessments/${id}/grade`, { grade }),
  delete: (id: number) => api.delete(`/api/assessments/${id}`),
};
