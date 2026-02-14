import axios from 'axios';

const baseUrl = import.meta.env.VITE_BASE_URL || 'http://localhost:8080';

const axiosInstance = axios.create({
  baseURL: baseUrl,
  withCredentials: true, // Important for sessions/cookies
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if it exists
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
export { baseUrl };