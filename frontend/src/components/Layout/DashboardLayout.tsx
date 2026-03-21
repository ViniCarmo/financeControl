import React, { useState } from "react";
import { Sidebar } from "./Sidebar";

export const DashboardLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [mobileSidebarOpen, setMobileSidebarOpen] = useState(false);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-50 flex">
      <Sidebar className="hidden md:flex" />

      <div
        className={`fixed inset-0 z-50 md:hidden ${mobileSidebarOpen ? "block" : "hidden"}`}
        aria-hidden={!mobileSidebarOpen}
      >
        <div
          className="absolute inset-0 bg-black/60"
          onClick={() => setMobileSidebarOpen(false)}
        />
        <Sidebar className="relative z-10 w-72" onNavigate={() => setMobileSidebarOpen(false)} />
      </div>

      <main className="flex-1 flex flex-col">
        <div className="md:hidden flex items-center justify-between border-b border-slate-800 bg-slate-900/80 px-4 py-3">
          <button
            type="button"
            onClick={() => setMobileSidebarOpen(true)}
            className="min-h-[44px] min-w-[44px] rounded-md border border-slate-700 bg-slate-800 px-3 text-slate-100"
            aria-label="Open menu"
          >
            ☰
          </button>
          <p className="text-sm font-semibold text-emerald-400">Finance Dashboard</p>
          <div className="w-11" />
        </div>
        <div className="flex-1 px-4 py-4 md:px-6 md:py-6">{children}</div>
      </main>
    </div>
  );
};

