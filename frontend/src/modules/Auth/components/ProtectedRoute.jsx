import { Navigate } from "react-router-dom";

import Loader from "../../../shared/Loader";

import { useAuth } from "../context/useAuth";


const ProtectedRoute = ({ children }) => {

  const {
    user,
    loading,
    isAuthenticated,
  } = useAuth();


  // While checking auth session
  if (loading) {

    return <Loader />;
  }


  // User not logged in
  if (!isAuthenticated || !user) {

    return <Navigate to="/login" replace />;
  }


  // User authenticated
  return children;
};

export default ProtectedRoute;