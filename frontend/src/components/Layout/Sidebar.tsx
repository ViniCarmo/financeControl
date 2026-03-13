import { NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const navLinkClass =
  "flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium hover:bg-slate-800 transition-colors";

export function Sidebar() {
  const { user, logout } = useAuth();

  return (
    <aside className="bg-slate-900 border-r border-slate-800 w-64 min-h-screen flex flex-col">
      <div className="px-4 py-4 border-b border-slate-800">
        <h1 className="text-lg font-semibold text-emerald-400">Finance Dashboard</h1>
        {user && <p className="text-xs text-slate-400 mt-1">Welcome, {user.username}</p>}
      </div>
      <nav className="flex-1 px-2 py-4 space-y-1">
        <NavLink
          to="/"
          end
          className={({ isActive }) =>
            `${navLinkClass} ${isActive ? "bg-slate-800 text-emerald-400" : "text-slate-200"}`
          }
        >
          Dashboard
        </NavLink>
        <NavLink
          to="/transactions"
          className={({ isActive }) =>
            `${navLinkClass} ${isActive ? "bg-slate-800 text-emerald-400" : "text-slate-200"}`
          }
        >
          Transactions
        </NavLink>
        <NavLink
          to="/profile"
          className={({ isActive }) =>
            `${navLinkClass} ${isActive ? "bg-slate-800 text-emerald-400" : "text-slate-200"}`
          }
        >
          Profile
        </NavLink>
      </nav>
      <div className="px-4 py-4 border-t border-slate-800">
        <button
          type="button"
          onClick={logout}
          className="w-full text-sm font-medium text-slate-200 bg-slate-800 hover:bg-slate-700 rounded-md px-3 py-2 transition-colors"
        >
          Logout
        </button>
      </div>
    </aside>
  );
}

