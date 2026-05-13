import apiClient from "../../../services/apiClient";
import { ENDPOINTS } from "../../../services/endpoints";


// ==============================
// Record Fuel Sale
// ==============================

export const recordFuelSale = async (payload) => {
const response = await apiClient.post(
  ENDPOINTS.FUEL.RECORD_SALE,
  payload
);

  return response.data;
};


// ==============================
// Get All Fuel Sales
// ==============================

export const getAllFuelSales = async () => {
const response = await apiClient.get(
  ENDPOINTS.FUEL.GET_ALL_SALES
);

  return response.data;
};


// ==============================
// Get Fuel Sale By ID
// ==============================

export const getFuelSaleById = async (id) => {
  const response = await apiClient.get(
    ENDPOINTS.FUEL.GET_SALE_BY_ID(id)
  );

  return response.data;
};


// ==============================
// Record Fuel Purchase
// ==============================

export const recordFuelPurchase = async (payload) => {
  const response = await apiClient.post(
    ENDPOINTS.FUEL.RECORD_PURCHASE,
    payload
  );

  return response.data;
};


// ==============================
// Get All Purchases
// ==============================

export const getAllPurchases = async () => {
  const response = await apiClient.get(
    ENDPOINTS.FUEL.GET_ALL_PURCHASES
  );

  return response.data;
};

// ==============================
// Get Purchase By ID
// ==============================

export const getPurchaseById = async (id) => {
  const response = await apiClient.get(
    ENDPOINTS.FUEL.GET_PURCHASE_BY_ID(id)
  );

  return response.data;
};