import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

let accessToken = null;
let onUnauthorized = () => {};

export function setToken(token) { accessToken = token; }
export function getToken() { return accessToken; }
export function clearToken() { accessToken = null; }
export function setUnauthorizedHandler(fn) { onUnauthorized = fn; }

const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      clearToken();
      onUnauthorized();
    }
    return Promise.reject(error);
  }
);

export default api;
