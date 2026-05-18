import { useNavigate } from "react-router-dom";
import { useAuth } from "../modules/Auth/context/useAuth";
import { LogOut } from "lucide-react";

const LogoutButton = () => {
  const navigate = useNavigate();
  const { logout } = useAuth();

  // ==============================
  // Handle Logout
  // ==============================

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  return (
    <button
      onClick={handleLogout}
      className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-red-600 bg-red-50 border border-red-100 rounded-lg hover:bg-red-100 active:bg-red-200 transition-all dark:text-red-400 dark:bg-red-500/10 dark:border-red-500/20 dark:hover:bg-red-500/20"
      title="Logout"
    >
      <LogOut className="h-4 w-4" />
      <span>Logout</span>
    </button>
  );
};

export default LogoutButton;