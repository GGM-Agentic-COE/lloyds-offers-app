import axios from 'axios';
import { useAuthStore } from '../stores/authStore';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api/v1',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const { accessToken } = useAuthStore.getState();
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
  config.headers['X-Correlation-Id'] = crypto.randomUUID();
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;
      const { refreshToken, setTokens, clearAuth } = useAuthStore.getState();
      if (!refreshToken) { clearAuth(); return Promise.reject(error); }
      try {
        const res = await axios.post(`${api.defaults.baseURL}/auth/refresh`, { refreshToken });
        setTokens(res.data.data.accessToken, res.data.data.refreshToken, useAuthStore.getState().userId!);
        original.headers.Authorization = `Bearer ${res.data.data.accessToken}`;
        return api(original);
      } catch { clearAuth(); return Promise.reject(error); }
    }
    return Promise.reject(error);
  }
);

export { api };
