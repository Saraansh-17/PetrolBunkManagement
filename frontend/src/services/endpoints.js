
export const ENDPOINTS = {
  AUTH: {
    LOGIN: "/auth/login",
    LOGOUT: "/auth/logout",
    GET_ME: "/auth/me",
  },
  FUEL: {
    // Fuel Consume
    RECORD_SALE: "/api/v1/fuel/sale",
    GET_ALL_SALES: "/api/v1/fuel/sales",
    GET_SALE_BY_ID: (id) => `api/v1/fuel/sales/${id}`,

    // Fuel Purchases
    RECORD_PURCHASE: "api/v1/fuel/purchase/create",
    GET_ALL_PURCHASES: "api/v1/fuel/purchase/purchases",
    GET_PURCHASE_BY_ID: (id) => `api/v1/fuel/purchases/${id}`,
  },

  INVENTORY: {
    GET_STOCK: "api/v1/inventory/stock",
    RESERVE_STOCK: "api/v1/inventory/reserve",
  },

  EMPLOYEES: {
    GET_ALL_EMPLOYEES: "api/v1/employees",
    GET_EMPLOYEE_BY_ID: (id) => `api/v1/employees/${id}`,
    CREATE_EMPLOYEE: "api/v1/employees",
  },

  ATTENDANCE: {
    MARK_ATTENDANCE: "/attendance",
    GET_ALL_ATTENDANCE: "/attendance",
    GET_ATTENDANCE_BY_ID: (id) => `/attendance/${id}`,
  },
};