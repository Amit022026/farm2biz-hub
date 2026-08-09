import api from './api';

export const userService = {
  listAll: () => api.get('/users').then((r) => r.data),
  remove: (id) => api.delete(`/users/${id}`).then((r) => r.data),
};
