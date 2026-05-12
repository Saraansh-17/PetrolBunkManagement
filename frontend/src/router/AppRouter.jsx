
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { HRDashboard, HREmployees, HRAttendance } from '../modules/hr'; // Aapka module import ho raha hai

const AppRouter = () => {
  return (
    <Routes>
    
      <Route path="/hr-dashboard" element={<HRDashboard />} />
      
    
      <Route path="/hr-employees" element={<HREmployees />} />
      

      <Route path="/hr-attendance" element={<HRAttendance />} />
    </Routes>
  );
};

export default AppRouter;
