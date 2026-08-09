import api from './api';

export const paymentService = {
  pay: (payload) => api.post('/payments', payload).then((r) => r.data),
  getForOrder: (orderId) => api.get(`/payments/order/${orderId}`).then((r) => r.data),
};
