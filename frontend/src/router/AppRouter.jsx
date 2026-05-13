import { Routes, Route } from "react-router-dom";

import Dashboard from "../modules/dashboard/pages/Dashboard";
import PurchaseFuel from "../modules/fuel/pages/PurchaseFuel";
import ConsumeFuel from "../modules/fuel/pages/ConsumeFuel";
import Inventory from "../modules/inventory/pages/Inventory";
import Employees from "../modules/hr/pages/Employees";
import Attendance from "../modules/hr/pages/Attendance";

const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />

      <Route path="/purchase-fuel" element={<PurchaseFuel />} />

      <Route path="/consume-fuel" element={<ConsumeFuel />} />

      <Route path="/inventory" element={<Inventory />} />

      <Route path="/employees" element={<Employees />} />

      <Route path="/attendance" element={<Attendance />} />
    </Routes>
  );
};

export default AppRouter;
