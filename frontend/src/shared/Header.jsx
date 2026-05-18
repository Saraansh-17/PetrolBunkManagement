import { NavLink, Link, useLocation } from 'react-router-dom';
import { Fuel, LayoutDashboard, Droplet, Package, Users, Bell, UserCircle, ChevronDown } from 'lucide-react';

import { useAuth } from '../modules/Auth/context/useAuth';
import LogoutButton from './Logout';

const Header = () => {
  const location = useLocation();
  const { user, isAuthenticated } = useAuth();

  const navItems = [
    { name: 'Dashboard', path: '/', icon: LayoutDashboard },
    { 
      name: 'Fuel Ops', 
      path: '#', 
      icon: Droplet,
      subItems: [
        { name: 'Consume Fuel', path: '/consume-fuel' },
        { name: 'Purchase Fuel', path: '/purchase-fuel' }
      ]
    },
    { name: 'Inventory', path: '/inventory', icon: Package },
    { 
      name: 'Staff (HR)', 
      path: '#', 
      icon: Users,
      subItems: [
        { name: 'Attendance', path: '/attendance' },
        { name: 'Employees', path: '/employees' }
      ]
    },
  ];

  if (location.pathname === '/login') {
    return (
      <header className="sticky top-0 z-50 w-full border-b border-gray-200/80 bg-white/80 backdrop-blur-xl transition-all dark:border-gray-800/80 dark:bg-gray-950/80 shadow-sm">
        <div className="mx-auto flex h-16 max-w-7xl items-center px-4 sm:px-6 lg:px-8">
          <Link to="/" className="group flex items-center gap-2">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 text-white shadow-lg shadow-blue-500/30 transition-transform group-hover:scale-105">
              <Fuel className="h-5 w-5" />
            </div>
            <span className="bg-gradient-to-r from-gray-900 to-gray-600 bg-clip-text text-xl font-extrabold text-transparent dark:from-white dark:to-gray-300 tracking-tight">
              PetrolBunk
            </span>
          </Link>
        </div>
      </header>
    );
  }

  const filteredNavItems = navItems.filter((item) => {
    if (item.name === 'Staff (HR)' && user?.role !== 'ADMIN') {
      return false;
    }
    return true;
  });

  return (
    <header className="sticky top-0 z-50 w-full border-b border-gray-200/80 bg-white/80 backdrop-blur-xl transition-all dark:border-gray-800/80 dark:bg-gray-950/80 shadow-sm">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
        {/* Logo Section */}
        <div className="flex items-center gap-8">
          <Link to="/" className="group flex items-center gap-2">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 text-white shadow-lg shadow-blue-500/30 transition-transform group-hover:scale-105">
              <Fuel className="h-5 w-5" />
            </div>
            <span className="bg-gradient-to-r from-gray-900 to-gray-600 bg-clip-text text-xl font-extrabold text-transparent dark:from-white dark:to-gray-300 tracking-tight">
              PetrolBunk
            </span>
          </Link>

          {/* Navigation */}
          <nav className="flex items-center space-x-1">
            {filteredNavItems.map((item) => {
              const Icon = item.icon;
              if (item.subItems) {
                return (
                  <div key={item.name} className="relative group">
                    <NavLink
                      to={item.path}
                      className={({ isActive }) =>
                        `flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium transition-all ${
                          isActive
                            ? 'bg-blue-50 text-blue-700 dark:bg-blue-500/10 dark:text-blue-400'
                            : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900 dark:text-gray-300 dark:hover:bg-gray-800 dark:hover:text-white'
                        }`
                      }
                    >
                      <Icon className="h-4 w-4 opacity-70 group-hover:opacity-100 transition-opacity" />
                      {item.name}
                      <ChevronDown className="h-3 w-3 opacity-50 group-hover:opacity-100 transition-transform group-hover:rotate-180 ml-0.5" />
                    </NavLink>
                    
                    {/* Dropdown Menu */}
                    <div className="absolute left-0 top-full pt-2 hidden group-hover:block w-48 z-50">
                      <div className="rounded-xl border border-gray-200/80 bg-white/90 backdrop-blur-xl p-2 shadow-xl dark:border-gray-800/80 dark:bg-gray-950/90 shadow-blue-500/5">
                        {item.subItems.map((subItem) => (
                          <NavLink
                            key={subItem.name}
                            to={subItem.path}
                            className={({ isActive }) =>
                              `block rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                                isActive
                                  ? 'bg-blue-50 text-blue-700 dark:bg-blue-500/10 dark:text-blue-400'
                                  : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white'
                              }`
                            }
                          >
                            {subItem.name}
                          </NavLink>
                        ))}
                      </div>
                    </div>
                  </div>
                );
              }

              return (
                <NavLink
                  key={item.name}
                  to={item.path}
                  className={({ isActive }) =>
                    `group flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium transition-all ${
                      isActive
                        ? 'bg-blue-50 text-blue-700 dark:bg-blue-500/10 dark:text-blue-400'
                        : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900 dark:text-gray-300 dark:hover:bg-gray-800 dark:hover:text-white'
                    }`
                  }
                >
                  <Icon className="h-4 w-4 opacity-70 group-hover:opacity-100 transition-opacity" />
                  {item.name}
                </NavLink>
              );
            })}
          </nav>
        </div>

        {/* Right Section */}
        <div className="flex items-center gap-4">
          
          {isAuthenticated && <LogoutButton />}

          <button className="relative flex h-10 w-10 items-center justify-center rounded-full text-gray-500 transition-colors hover:bg-gray-100 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white">
            <Bell className="h-5 w-5" />
            <span className="absolute top-2.5 right-2.5 flex h-2 w-2">
              <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-red-400 opacity-75"></span>
              <span className="relative inline-flex h-2 w-2 rounded-full bg-red-500"></span>
            </span>
          </button>

          <div className="flex items-center gap-3 pl-4 border-l border-gray-200 dark:border-gray-800">
            <div className="flex flex-col items-end">
              <span className="text-sm font-semibold text-gray-900 dark:text-white leading-none capitalize">{user?.username || 'User'}</span>
              <span className="text-xs text-gray-500 dark:text-gray-400 mt-1 capitalize">{user?.role?.toLowerCase() || 'Employee'}</span>
            </div>
            <button className="flex h-9 w-9 items-center justify-center rounded-full bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 hover:ring-2 ring-blue-500/50 transition-all">
              <UserCircle className="h-6 w-6 text-gray-600 dark:text-gray-300" />
            </button>
          </div>

        </div>
      </div>
    </header>
  );
};

export default Header;
