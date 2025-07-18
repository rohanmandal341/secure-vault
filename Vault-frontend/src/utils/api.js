import axios from "axios";
const api = axios.create({ baseURL: "http://localhost:1010/api" });

// Interceptor: Attach JWT token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;
