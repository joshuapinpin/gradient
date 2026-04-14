import { api } from './client';
import type { CourseResponse } from '../types/api';

export const coursesApi = {
  list: () => api.get<CourseResponse[]>('/api/courses'),
  listByTerm: (termId: number) => api.get<CourseResponse[]>(`/api/courses/term/${termId}`),
  getById: (id: number) => api.get<CourseResponse>(`/api/courses/${id}`),
  create: (body: { name: string; termId: number }) =>
    api.post<CourseResponse>('/api/courses', body),
  update: (id: number, body: { name: string }) =>
    api.put<CourseResponse>(`/api/courses/${id}`, body),
  delete: (id: number) => api.delete(`/api/courses/${id}`),
};
