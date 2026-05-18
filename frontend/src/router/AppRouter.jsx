import { Routes, Route } from "react-router-dom";

import Dashboard from "../modules/dashboard/pages/Dashboard";

import PurchaseFuel from "../modules/fuel/pages/PurchaseFuel";
import ConsumeFuel from "../modules/fuel/pages/ConsumeFuel";

import Inventory from "../modules/inventory/pages/Inventory";

import Employees from "../modules/hr/pages/Employees";
import Attendance from "../modules/hr/pages/Attendance";

import Login from "../modules/Auth/pages/Login.jsx";

import ProtectedRoute from "../modules/Auth/components/ProtectedRoute.jsx";

import AdminRoute from "../modules/Auth/components/AdminRoute.jsx";


const AppRouter = () => {

  return (

    <Routes>

      {/* Public Route */}
      <Route
        path="/login"
        element={<Login />}
      />



      {/* Employee + Admin Routes */}

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />



      <Route
        path="/purchase-fuel"
        element={
          <ProtectedRoute>
            <PurchaseFuel />
          </ProtectedRoute>
        }
      />



      <Route
        path="/consume-fuel"
        element={
          <ProtectedRoute>
            <ConsumeFuel />
          </ProtectedRoute>
        }
      />



      {/* Admin Only Routes */}

      <Route
        path="/inventory"
        element={
          <AdminRoute>
            <Inventory />
          </AdminRoute>
        }
      />



      <Route
        path="/employees"
        element={
          <AdminRoute>
            <Employees />
          </AdminRoute>
        }
      />



      <Route
        path="/attendance"
        element={
          <AdminRoute>
            <Attendance />
          </AdminRoute>
        }
      />

    </Routes>
  );
};

export default AppRouter;