import apiClient from "../../../services/apiClient";
import { ENDPOINTS } from "../../../services/endpoints";


// ==============================
// Login User
// ==============================

export const loginUser = async (payload) => {

  const response = await apiClient.post(
    ENDPOINTS.AUTH.LOGIN,
    payload
  );

  return response.data;
};


// ==============================
// Logout User
// ==============================

export const logoutUser = async () => {

  const response = await apiClient.post(
    ENDPOINTS.AUTH.LOGOUT
  );

  return response.data;
};


// ==============================
// Get Current Logged In User
// ==============================

export const getCurrentUser = async () => {

  const response = await apiClient.get(
    ENDPOINTS.AUTH.GET_ME
  );

  return response.data;
};