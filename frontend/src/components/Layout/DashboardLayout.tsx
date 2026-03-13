import React from "react";
import { Sidebar } from "./Sidebar";

export const DashboardLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-50 flex">
      <Sidebar />
      <main className="flex-1 flex flex-col">
        <div className="flex-1 px-6 py-6">{children}</div>
      </main>
    </div>
  );
};

