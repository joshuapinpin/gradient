import { api } from './client';
import type { YearResponse } from '../types/api';

export const yearsApi = {
  list: () => api.get<YearResponse[]>('/api/years'),
  getById: (id: number) => api.get<YearResponse>(`/api/years/${id}`),
  create: (body: { name: string; startDate: string; endDate: string }) =>
    api.post<YearResponse>('/api/years', body),
  update: (id: number, body: { name: string; startDate: string; endDate: string }) =>
    api.put<YearResponse>(`/api/years/${id}`, body),
  delete: (id: number) => api.delete(`/api/years/${id}`),
};
