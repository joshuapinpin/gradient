import { api } from './client';
import type { TermResponse } from '../types/api';

export const termsApi = {
  list: () => api.get<TermResponse[]>('/api/terms'),
  listByYear: (yearId: number) => api.get<TermResponse[]>(`/api/terms/year/${yearId}`),
  getById: (id: number) => api.get<TermResponse>(`/api/terms/${id}`),
  create: (body: { name: string; yearId: number; startDate?: string; endDate?: string }) =>
    api.post<TermResponse>('/api/terms', body),
  update: (id: number, body: { name: string; startDate?: string; endDate?: string }) =>
    api.put<TermResponse>(`/api/terms/${id}`, body),
  delete: (id: number) => api.delete(`/api/terms/${id}`),
};
