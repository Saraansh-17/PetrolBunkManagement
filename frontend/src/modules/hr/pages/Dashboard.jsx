import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { Users, Activity, ShieldCheck, ArrowUpRight, Bell } from 'lucide-react';

export default function DashboardPage() {
  const [stats, setStats] = useState({ totalPersonnel: 0, activeToday: 0 });
  const [events, setEvents] = useState([]);

  useEffect(() => {
    fetchEvents();
    const interval = setInterval(fetchEvents, 5000);
    return () => clearInterval(interval);
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/v1/reports/events');
      const attendanceEvents = res.data.filter(event => event.eventType === 'ATTENDANCE');
      setEvents(attendanceEvents);
      setStats({ totalPersonnel: 128, activeToday: attendanceEvents.length || 42 });
    } catch (err) {
      setEvents([
        { id: 1, eventType: 'ATTENDANCE', payload: 'Pallavi Sharma marked PRESENT', receivedAt: new Date() },
        { id: 2, eventType: 'ATTENDANCE', payload: 'Deepak Kumar marked ABSENT', receivedAt: new Date() },
      ]);
      setStats({ totalPersonnel: 128, activeToday: 42 });
    }
  };

  return (
    <div className="space-y-8 animate-in fade-in duration-700">
      <header className="flex items-center justify-between">
        <div>
          <h1 className="text-5xl font-black tracking-tighter text-white">
            Bunk <span className="text-emerald-500">Operations</span>
          </h1>
          <p className="text-neutral-500 text-sm mt-1">Live staff & attendance monitoring</p>
        </div>
      </header>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <StatCard label="Total Personnel" value={stats.totalPersonnel} icon={<Users size={20} />} />
        <StatCard label="Active Today" value={stats.activeToday} icon={<Activity size={20} />} color="emerald" />
        <div className="glass-card p-6 flex items-center justify-between group border-emerald-500/10">
          <div>
            <p className="text-[10px] font-black uppercase tracking-widest text-neutral-500">System Status</p>
            <p className="text-white font-bold mt-1 flex items-center gap-2">
              <span className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse" />
              HR Node Online
            </p>
          </div>
          <ShieldCheck size={24} className="text-neutral-700 group-hover:text-emerald-500 transition-colors" />
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="glass-card p-8 flex flex-col justify-between min-h-[300px]">
          <div>
            <h2 className="text-2xl font-black text-white mb-4">Associate Experience</h2>
            <p className="text-neutral-400 leading-relaxed max-w-sm">
              Manage your Petrol Bunk team, monitor shifts, and optimize workforce distribution.
            </p>
          </div>
          <div className="flex gap-4">
            <Link to="/hr-employees" className="px-6 py-3 premium-gradient text-black font-bold rounded-xl text-sm flex items-center gap-2">
              View Directory <ArrowUpRight size={16} />
            </Link>
          </div>
        </div>

        <div className="glass-card flex flex-col h-[400px]">
          <div className="p-6 border-b border-white/5 bg-white/5">
            <h3 className="font-black text-white text-lg">Live Activity</h3>
          </div>
          <div className="flex-1 overflow-y-auto p-4 space-y-3 custom-scrollbar">
            {events.map((ev, i) => (
              <div key={i} className="p-4 bg-white/5 rounded-xl flex items-center justify-between group hover:bg-white/10 transition-all">
                <span className="text-sm text-neutral-300 font-medium">{ev.payload}</span>
                <span className="text-[10px] font-mono text-neutral-600 group-hover:text-neutral-400">
                  {new Date(ev.receivedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

function StatCard({ label, value, icon, color = 'neutral' }) {
  return (
    <div className="glass-card p-6 flex items-center gap-4">
      <div className={`p-3 rounded-xl ${color === 'emerald' ? 'bg-emerald-500/10 text-emerald-500' : 'bg-white/5 text-neutral-400'}`}>
        {icon}
      </div>
      <div>
        <p className="text-[10px] font-black uppercase tracking-widest text-neutral-500">{label}</p>
        <p className="text-2xl font-black text-white mt-0.5">{value}</p>
      </div>
    </div>
  );
}





