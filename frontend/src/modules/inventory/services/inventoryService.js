import apiClient from "../../../services/apiClient";

import { ENDPOINTS } from "../../../services/endpoints";


// ==============================
// Get Inventory By Fuel Type
// ==============================

export const getInventoryByFuelType = async (fuelType) => {

  const response = await apiClient.get(
    `${ENDPOINTS.INVENTORY.GET_STOCK}?fuelType=${fuelType}`
  );

  return response.data;
};