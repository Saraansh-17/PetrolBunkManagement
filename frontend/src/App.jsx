import { BrowserRouter as Router, Link, useLocation } from 'react-router-dom';
import AppRouter from './router/AppRouter';
import { LayoutDashboard, Users, UserRoundCog } from 'lucide-react';
import './App.css';

const Sidebar = () => {
  return (
    <div className="w-64 min-h-screen bg-[#0a0a0a] border-r border-white/5 p-6 fixed left-0 top-0">
      <div className="text-emerald-500 font-black text-2xl mb-10 px-2 tracking-tighter">PETROL BUNK</div>
      <nav className="space-y-2">
        <Link to="/hr-dashboard" className="flex items-center gap-3 text-neutral-400 hover:text-white p-3 rounded-xl hover:bg-white/5 transition-all">
          <LayoutDashboard size={20} /> <span className="font-bold text-sm">Dashboard</span>
        </Link>
        <Link to="/hr-employees" className="flex items-center gap-3 text-neutral-400 hover:text-white p-3 rounded-xl hover:bg-white/5 transition-all">
          <UserRoundCog size={20} /> <span className="font-bold text-sm">Staff Management</span>
        </Link>
        <Link to="/hr-attendance" className="flex items-center gap-3 text-neutral-400 hover:text-white p-3 rounded-xl hover:bg-white/5 transition-all">
          <Users size={20} /> <span className="font-bold text-sm">Daily Attendance</span>
        </Link>
      </nav>
    </div>
  );
};

function App() {
  return (
    <Router>
      <div className="flex bg-[#050505] min-h-screen text-white">
        
        <Sidebar />

        
        <main className="flex-1 ml-64 p-10">
          <div className="max-w-7xl mx-auto">
            <AppRouter />
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;
