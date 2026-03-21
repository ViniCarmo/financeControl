import React from "react";

export const Card: React.FC<{ title: string; value: string; accent?: "income" | "expense" | "neutral" }> = ({
  title,
  value,
  accent = "neutral"
}) => {
  const accentColor =
    accent === "income" ? "text-income" : accent === "expense" ? "text-expense" : "text-slate-50";

  return (
    <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4 shadow-sm">
      <p className="text-xs font-medium text-slate-400 uppercase tracking-wide">{title}</p>
      <p className={`mt-2 text-2xl font-semibold ${accentColor}`}>{value}</p>
    </div>
  );
};

export const Button: React.FC<
  React.ButtonHTMLAttributes<HTMLButtonElement> & { variant?: "primary" | "secondary" | "ghost" }
> = ({ variant = "primary", className = "", ...props }) => {
  const base =
    "inline-flex min-h-[44px] items-center justify-center rounded-md px-3 py-2.5 text-sm font-medium transition-colors";
  const variants: Record<string, string> = {
    primary: "bg-emerald-500 hover:bg-emerald-600 text-white disabled:opacity-60 disabled:cursor-not-allowed",
    secondary:
      "bg-slate-800 hover:bg-slate-700 text-slate-50 border border-slate-700 disabled:opacity-60 disabled:cursor-not-allowed",
    ghost: "bg-transparent hover:bg-slate-800 text-slate-200"
  };
  return <button className={`${base} ${variants[variant]} ${className}`} {...props} />;
};

export const Input: React.FC<React.InputHTMLAttributes<HTMLInputElement>> = ({
  className = "",
  ...props
}) => (
  <input
    className={`w-full min-h-[44px] rounded-md border border-slate-700 bg-slate-900 px-3 py-2.5 text-sm text-slate-50 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent ${className}`}
    {...props}
  />
);

export const Select: React.FC<React.SelectHTMLAttributes<HTMLSelectElement>> = ({
  className = "",
  children,
  ...props
}) => (
  <select
    className={`w-full min-h-[44px] rounded-md border border-slate-700 bg-slate-900 px-3 py-2.5 text-sm text-slate-50 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent ${className}`}
    {...props}
  >
    {children}
  </select>
);

export const Textarea: React.FC<React.TextareaHTMLAttributes<HTMLTextAreaElement>> = ({
  className = "",
  ...props
}) => (
  <textarea
    className={`w-full min-h-[44px] rounded-md border border-slate-700 bg-slate-900 px-3 py-2.5 text-sm text-slate-50 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent ${className}`}
    {...props}
  />
);

export const Modal: React.FC<{
  title: string;
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
}> = ({ title, isOpen, onClose, children }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-0 sm:p-4">
      <div className="h-full w-full rounded-none border-0 bg-slate-900 shadow-xl sm:h-auto sm:max-w-lg sm:rounded-xl sm:border sm:border-slate-800">
        <div className="flex items-center justify-between border-b border-slate-800 px-4 py-3">
          <h2 className="text-sm font-semibold text-slate-100">{title}</h2>
          <button
            type="button"
            onClick={onClose}
            className="min-h-[44px] min-w-[44px] text-slate-400 hover:text-slate-200 text-sm px-1"
          >
            ✕
          </button>
        </div>
        <div className="max-h-[calc(100vh-64px)] overflow-y-auto px-4 py-4">{children}</div>
      </div>
    </div>
  );
};

