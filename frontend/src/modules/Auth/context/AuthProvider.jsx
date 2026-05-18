import { useEffect, useState } from "react";

import { AuthContext } from "./AuthContext";

import {
  getCurrentUser,
  logoutUser,
} from "../services/authService";


export const AuthProvider = ({ children }) => {

  const [user, setUser] = useState(null);

  const [loading, setLoading] = useState(true);


  // Fetch Logged User
  const fetchCurrentUser = async () => {

    try {

      const data = await getCurrentUser();

      setUser(data);

    } catch (err) {

      console.log(err, "No Active Session");

      setUser(null);

    } finally {

      setLoading(false);
    }
  };


  // Logout
  const logout = async () => {

    try {

      await logoutUser();

      setUser(null);

    } catch (error) {

      console.log(error);
    }
  };


  // App Load
  useEffect(() => {

    const loadUser = async () => {
      await fetchCurrentUser();
    };

    loadUser();

  }, []);


  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        loading,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};