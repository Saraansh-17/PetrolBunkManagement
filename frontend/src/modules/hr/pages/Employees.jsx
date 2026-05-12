import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { UserPlus, Search, Trash2, Edit2 } from 'lucide-react';

export default function EmployeesPage() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form, setForm] = useState({ name: '', designation: '', department: 'Operations' });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => { fetchEmployees(); }, []);

  const fetchEmployees = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/v1/employees');
      setEmployees(res.data);
    } catch (err) {
      setEmployees([
        { employeeId: 'EMP201', name: 'Pallavi Sharma', designation: 'Team Lead', department: 'HR' },
        { employeeId: 'EMP202', name: 'Deepak Kumar', designation: 'Senior Operator', department: 'Operations' },
      ]);
    }
    setLoading(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await axios.put(`http://localhost:8080/api/v1/employees/${editingId}`, form);
      } else {
        await axios.post('http://localhost:8080/api/v1/employees', form);
      }
      fetchEmployees();
    } catch (err) {
      if (editingId) {
        setEmployees(employees.map(emp => emp.employeeId === editingId ? { ...emp, ...form } : emp));
      } else {
        setEmployees([...employees, { employeeId: `EMP${Date.now()}`, ...form }]);
      }
    }
    closeModal();
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/v1/employees/${id}`);
      fetchEmployees();
    } catch (err) {
      setEmployees(employees.filter(emp => emp.employeeId !== id));
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingId(null);
    setForm({ name: '', designation: '', department: 'Operations' });
  };

  const filteredEmployees = employees.filter(emp => 
    emp.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    emp.designation.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6 animate-in fade-in duration-700">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-4xl font-black tracking-tighter text-white">Staff <span className="text-emerald-500">Management</span></h1>
          <p className="text-neutral-500 text-sm">Manage bunk employees and roles</p>
        </div>
        <button onClick={() => setIsModalOpen(true)} className="px-6 py-3 premium-gradient text-black rounded-xl font-bold flex items-center gap-2 text-sm shadow-lg">
          <UserPlus size={18} /> Add New Staff
        </button>
      </div>

      <div className="glass-card overflow-hidden">
        <div className="p-4 border-b border-white/5 bg-white/5 flex items-center gap-4">
          <Search size={18} className="text-neutral-500 ml-2" />
          <input 
            type="text" placeholder="Search employees..." value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="flex-1 bg-transparent border-none outline-none text-white text-sm"
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="text-neutral-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                <th className="px-6 py-4">Employee</th>
                <th className="px-6 py-4">Role</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {filteredEmployees.map(emp => (
                <tr key={emp.employeeId} className="group hover:bg-white/[0.02]">
                  <td className="px-6 py-5">
                    <div className="text-white font-bold">{emp.name}</div>
                    <div className="text-[10px] font-mono text-neutral-600 uppercase">Staff ID: {emp.employeeId}</div>
                  </td>
                  <td className="px-6 py-5">
                    <div className="text-neutral-300 text-sm">{emp.designation}</div>
                    <div className="text-[10px] text-neutral-600 uppercase">{emp.department}</div>
                  </td>
                  <td className="px-6 py-5 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button onClick={() => { setForm(emp); setEditingId(emp.employeeId); setIsModalOpen(true); }} className="p-2 text-neutral-500 hover:text-white transition-colors"><Edit2 size={16} /></button>
                      <button onClick={() => handleDelete(emp.employeeId)} className="p-2 text-neutral-500 hover:text-red-400 transition-colors"><Trash2 size={16} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-black/80 backdrop-blur-sm" onClick={closeModal}>
          <div className="relative w-full max-w-md glass-card p-8 border-emerald-500/20" onClick={e => e.stopPropagation()}>
            <h2 className="text-2xl font-black text-white mb-6">{editingId ? 'Edit' : 'New'} Staff Member</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-1">
                <label className="text-[10px] font-black uppercase text-neutral-500 ml-1">Full Name</label>
                <input required type="text" value={form.name} onChange={e => setForm({...form, name: e.target.value})} className="w-full px-4 py-3 bg-neutral-900 border border-white/10 rounded-xl text-white outline-none focus:ring-1 focus:ring-emerald-500 transition-all" />
              </div>
              <div className="space-y-1">
                <label className="text-[10px] font-black uppercase text-neutral-500 ml-1">Designation</label>
                <input required type="text" value={form.designation} onChange={e => setForm({...form, designation: e.target.value})} className="w-full px-4 py-3 bg-neutral-900 border border-white/10 rounded-xl text-white outline-none focus:ring-1 focus:ring-emerald-500 transition-all" />
              </div>
              <div className="space-y-1">
                <label className="text-[10px] font-black uppercase text-neutral-500 ml-1">Department</label>
                <select value={form.department} onChange={e => setForm({...form, department: e.target.value})} className="w-full px-4 py-3 bg-neutral-900 border border-white/10 rounded-xl text-white outline-none focus:ring-1 focus:ring-emerald-500 transition-all appearance-none">
                  <option value="Operations">Operations</option>
                  <option value="HR">HR</option>
                  <option value="Inventory">Inventory</option>
                </select>
              </div>
              <div className="flex gap-3 pt-4">
                <button type="button" onClick={closeModal} className="flex-1 py-3 rounded-xl border border-white/10 text-white font-bold hover:bg-white/5">Cancel</button>
                <button type="submit" className="flex-1 py-3 premium-gradient text-black rounded-xl font-bold">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
