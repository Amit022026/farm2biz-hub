import api from './api';

export const orderService = {
  place: (payload) => api.post('/orders', payload).then((r) => r.data),
  myOrders: () => api.get('/orders/my-orders').then((r) => r.data),
  farmerOrders: () => api.get('/orders/farmer-orders').then((r) => r.data),
  allOrders: () => api.get('/orders').then((r) => r.data),
  accept: (id) => api.patch(`/orders/${id}/accept`).then((r) => r.data),
  reject: (id) => api.patch(`/orders/${id}/reject`).then((r) => r.data),
  cancel: (id) => api.patch(`/orders/${id}/cancel`).then((r) => r.data),
};
