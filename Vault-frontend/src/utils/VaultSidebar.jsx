import { NavLink, Outlet, useNavigate } from "react-router-dom";
import logo from "../assets/image.png";

export default function VaultSidebar() {
  const nav = useNavigate();

  return (
    <div className="flex min-h-screen bg-gradient-to-br from-[#0f0c29] via-[#302b63] to-[#24243e] text-white">
      {/* Sidebar */}
      <aside className="w-64 p-6 bg-white/5 backdrop-blur-md border-r border-white/10">
        <div className="flex items-center gap-3 mb-10">
          <img src={logo} alt="Vault Logo" className="w-10 h-10" />
          <h1 className="text-xl font-semibold">Secure Vault</h1>
        </div>

        {/* Sidebar Links */}
        <nav className="space-y-3 text-sm font-medium">
          <NavLink to="/dashboard" className="block px-3 py-2 rounded hover:bg-white/10">
            📁 My Files
          </NavLink>
          <NavLink to="/shared" className="block px-3 py-2 rounded hover:bg-white/10">
            📤 Shared
          </NavLink>
          <NavLink to="/admin" className="block px-3 py-2 rounded hover:bg-white/10">
            🕵️ Admin Logs
          </NavLink>
          <NavLink to="/trash" className="block px-3 py-2 rounded hover:bg-white/10">
            🗑️ Trash
          </NavLink>
        </nav>

        <div className="mt-10 text-xs">
          <p className="text-white/70 mb-1">📊 Storage used: 0 of 1 GB</p>
          <button className="w-full bg-blue-600 hover:bg-blue-700 py-2 rounded-md">
            Upgrade
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6 overflow-y-auto">
        <Outlet /> {/* This will render the matching route’s content */}
      </main>
    </div>
  );
}
 