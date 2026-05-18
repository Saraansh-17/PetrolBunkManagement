import { Navigate } from "react-router-dom";

import Loader from "../../../shared/Loader";

import { useAuth } from "../context/useAuth";


const AdminRoute = ({ children }) => {

  const {
    user,
    loading,
    isAuthenticated,
  } = useAuth();


  // Session Loading
  if (loading) {

    return <Loader />;
  }


  // Not Logged In
  if (!isAuthenticated || !user) {

    return <Navigate to="/login" replace />;
  }


  // Not Admin
  if (user.role !== "ADMIN") {

    return <Navigate to="/" replace />;
  }


  // Admin Access
  return children;
};

export default AdminRoute;