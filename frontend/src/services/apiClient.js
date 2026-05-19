const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {}),
    },
    ...options,
  });

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({}));
    const message = errorBody.message ?? response.statusText;
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }
  return response.json();
}

export const api = {
  gatewayHealth: () => request("/gateway/health"),

  login: (email, password) =>
    request("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    }),

  logout: () => request("/auth/logout", { method: "POST" }),

  me: () => request("/auth/me"),

  refresh: () => request("/auth/refresh", { method: "POST" }),

  registerAuthEmployee: (payload) =>
    request("/auth/employees/register", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  listAuthEmployees: () => request("/auth/employees"),

  listHrEmployees: () => request("/hr/employees"),

  createHrEmployee: (payload) =>
    request("/hr/employees", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  fuelHealth: () => request("/fuel/health"),
  fuelNozzles: () => request("/fuel/nozzles"),
  fuelSales: () => request("/fuel/sales"),

  inventoryHealth: () => request("/inventory/health"),
  inventoryItems: () => request("/inventory/items"),
};

export default api;
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:3001/api/v1",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true
});

export default apiClient;
