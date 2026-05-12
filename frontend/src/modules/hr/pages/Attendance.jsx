import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Calendar, Activity, UserCheck, UserX } from 'lucide-react';

export default function AttendancePage() {
  const [records, setRecords] = useState([]);
  const [form, setForm] = useState({ employeeId: '', employeeName: '', status: 'PRESENT' });
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState({ present: 0, absent: 0 });

  useEffect(() => { fetchRecords(); }, []);

  const fetchRecords = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/v1/attendance');
      setRecords(res.data);
      calculateStats(res.data);
    } catch (err) {
      const mockData = [
        { attendanceId: 5001, employeeId: 'EMP201', employeeName: 'Pallavi Sharma', status: 'PRESENT', checkInTime: new Date().toISOString() },
        { attendanceId: 5002, employeeId: 'EMP202', employeeName: 'Deepak Kumar', status: 'ABSENT', checkInTime: new Date().toISOString() },
      ];
      setRecords(mockData);
      calculateStats(mockData);
    }
  };

  const calculateStats = (data) => {
    const s = { present: 0, absent: 0 };
    data.forEach(r => {
      if (r.status === 'PRESENT') s.present++;
      else if (r.status === 'ABSENT') s.absent++;
    });
    setStats(s);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    const payload = {
      employeeId: form.employeeId,
      status: form.status,
      checkInTime: new Date().toISOString()
    };
    try {
      await axios.post('http://localhost:8080/api/v1/attendance', payload);
      fetchRecords(); 
    } catch (err) {
      const newRecord = { attendanceId: Date.now(), ...form, checkInTime: payload.checkInTime };
      const updated = [newRecord, ...records];
      setRecords(updated);
      calculateStats(updated);
    }
    setForm({ employeeId: '', employeeName: '', status: 'PRESENT' });
    setLoading(false);
  };

  return (
    <div className="space-y-8 animate-in fade-in duration-700">
      <header className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <h1 className="text-5xl font-black tracking-tighter text-white">Daily <span className="text-emerald-500">Attendance</span></h1>
          <p className="text-neutral-500 text-sm mt-1">Live attendance tracking system</p>
        </div>
        <div className="flex gap-4">
          <StatMini label="Present" value={stats.present} color="text-emerald-400" />
          <StatMini label="Absent" value={stats.absent} color="text-red-400" />
        </div>
      </header>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        <div className="lg:col-span-4">
          <div className="glass-card p-8 border-emerald-500/10">
            <h2 className="text-xl font-black text-white mb-6 flex items-center gap-2">
              <div className="w-1.5 h-5 bg-emerald-500 rounded-full" /> Staff Check-in
            </h2>
            <form onSubmit={handleSubmit} className="space-y-5">
              <div className="space-y-1">
                <label className="text-[10px] font-black uppercase text-neutral-500 ml-1">Staff ID</label>
                <input required type="text" value={form.employeeId} onChange={e => setForm({...form, employeeId: e.target.value})} className="w-full px-4 py-3 bg-neutral-900 border border-white/10 rounded-xl text-white outline-none focus:ring-1 focus:ring-emerald-500" placeholder="e.g. EMP201" />
              </div>
              <div className="space-y-1">
                <label className="text-[10px] font-black uppercase text-neutral-500 ml-1">Full Name</label>
                <input required type="text" value={form.employeeName} onChange={e => setForm({...form, employeeName: e.target.value})} className="w-full px-4 py-3 bg-neutral-900 border border-white/10 rounded-xl text-white outline-none focus:ring-1 focus:ring-emerald-500" placeholder="e.g. Pallavi Sharma" />
              </div>
              <div className="flex gap-2">
                {['PRESENT', 'ABSENT'].map(s => (
                  <button key={s} type="button" onClick={() => setForm({...form, status: s})} className={`flex-1 py-3 rounded-xl text-[10px] font-black tracking-widest transition-all ${form.status === s ? 'bg-emerald-500 text-black shadow-lg' : 'bg-white/5 text-neutral-500'}`}>{s}</button>
                ))}
              </div>
              <button type="submit" disabled={loading} className="w-full premium-gradient text-black font-black py-4 rounded-xl mt-4 flex items-center justify-center gap-2">
                {loading ? <Activity className="animate-spin" size={18} /> : <><Calendar size={18} /> Record</>}
              </button>
            </form>
          </div>
        </div>

        <div className="lg:col-span-8">
          <div className="glass-card h-full flex flex-col overflow-hidden">
            <div className="p-6 border-b border-white/5 bg-white/5 flex items-center justify-between">
              <h3 className="font-black text-white">Daily Ledger</h3>
              <div className="text-[10px] font-mono text-neutral-500 uppercase">Live Telemetry Active</div>
            </div>
            <div className="flex-1 overflow-x-auto">
              <table className="w-full text-left">
                <thead>
                  <tr className="text-neutral-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5 bg-neutral-950/30">
                    <th className="px-6 py-4">Associate</th>
                    <th className="px-6 py-4">Status</th>
                    <th className="px-6 py-4 text-right">Time</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-white/5">
                  {records.map(record => (
                    <tr key={record.attendanceId || record.id} className="hover:bg-white/[0.02] group">
                      <td className="px-6 py-4 text-white font-bold">{record.employeeName}</td>
                      <td className="px-6 py-4">
                        <span className={`text-[10px] font-black uppercase tracking-widest ${record.status === 'PRESENT' ? 'text-emerald-500' : 'text-red-500'}`}>{record.status}</span>
                      </td>
                      <td className="px-6 py-4 text-right text-xs font-mono text-neutral-500 group-hover:text-emerald-500 transition-colors">
                        {new Date(record.checkInTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function StatMini({ label, value, color }) {
  return (
    <div className="glass-card px-6 py-3 flex flex-col items-center min-w-[80px]">
      <span className={`text-xl font-black ${color}`}>{value}</span>
      <span className="text-[10px] font-black uppercase text-neutral-600">{label}</span>
    </div>
  );
}
