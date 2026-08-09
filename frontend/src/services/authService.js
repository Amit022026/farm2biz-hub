import api from './api';

export const authService = {
  login: (credentials) => api.post('/users/login', credentials).then((r) => r.data),
  register: (payload) => api.post('/users/register', payload).then((r) => r.data),
};
