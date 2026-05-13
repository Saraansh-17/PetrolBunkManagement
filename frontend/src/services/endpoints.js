
export const ENDPOINTS = {
  FUEL: {
    // Fuel Consume
    RECORD_SALE: "/fuel/sale",
    GET_ALL_SALES: "/fuel/sales",
    GET_SALE_BY_ID: (id) => `/fuel/sales/${id}`,

    // Fuel Purchases
    RECORD_PURCHASE: "/fuel/purchase",
    GET_ALL_PURCHASES: "/fuel/purchases",
    GET_PURCHASE_BY_ID: (id) => `/fuel/purchases/${id}`,
  },

  INVENTORY: {
    GET_STOCK: "/inventory/stock",
    RESERVE_STOCK: "/inventory/reserve",
  },

  EMPLOYEES: {
    GET_ALL_EMPLOYEES: "/employees",
    GET_EMPLOYEE_BY_ID: (id) => `/employees/${id}`,
    CREATE_EMPLOYEE: "/employees",
  },

  ATTENDANCE: {
    MARK_ATTENDANCE: "/attendance",
    GET_ALL_ATTENDANCE: "/attendance",
    GET_ATTENDANCE_BY_ID: (id) => `/attendance/${id}`,
  },
};