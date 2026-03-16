import { useEffect, useMemo, useState } from "react";
import { api } from "../api/client";
import type { Transaction } from "../types";
import { Card } from "../components/ui";
import { useAuth } from "../context/AuthContext";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from "recharts";

export function DashboardPage() {
  const { token } = useAuth();
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [recentTransactions, setRecentTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const currentMonthPrefix = useMemo(() => {
    const now = new Date();
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, "0");
    return `${yyyy}-${mm}`;
  }, []);

  const currentMonthTransactions = useMemo(
    () => transactions.filter((t) => t.date.startsWith(currentMonthPrefix)),
    [transactions, currentMonthPrefix]
  );

  const totals = useMemo(() => {
    let totalIncome = 0;
    let totalExpense = 0;
    for (const tx of currentMonthTransactions) {
      if (tx.type === "INCOME") totalIncome += tx.value;
      else totalExpense += tx.value;
    }
    return {
      totalIncome,
      totalExpense,
      balance: totalIncome - totalExpense
    };
  }, [currentMonthTransactions]);

  const chartData = useMemo(
    () => [
      {
        name: "Current month",
        income: totals.totalIncome,
        expense: totals.totalExpense
      }
    ],
    [totals.totalIncome, totals.totalExpense]
  );

  const loadData = async () => {
    if (!token) {
      setTransactions([]);
      setRecentTransactions([]);
      setLoading(false);
      setError(null);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const [txResForTotals, txResRecent] = await Promise.all([
        api.get("/transactions", { params: { page: 0, size: 100 } }),
        api.get("/transactions", { params: { page: 0, size: 10 } })
      ]);

      const txContentTotals =
        "content" in txResForTotals.data ? txResForTotals.data.content : txResForTotals.data;
      setTransactions(txContentTotals);

      const txContentRecent =
        "content" in txResRecent.data ? txResRecent.data.content : txResRecent.data;
      setRecentTransactions(txContentRecent);
    } catch (err: unknown) {
      console.error(err);
      setError("Could not load dashboard data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, [token]);

  if (loading) {
    return <p className="text-sm text-slate-300">Loading dashboard...</p>;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-100">Dashboard</h2>
          <p className="text-xs text-slate-400 mt-1">
            Overview of your income and expenses.
          </p>
        </div>
      </div>

      {error && <p className="text-xs text-expense">{error}</p>}
      {!token && (
        <div className="rounded-xl border border-slate-800 bg-slate-900/40 px-4 py-3">
          <p className="text-xs text-slate-300">
            You are browsing in read-only mode. Sign in to see your dashboard totals and recent activity.
          </p>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card
          title="Total income (current month)"
          value={
            token
              ? totals.totalIncome.toLocaleString("en-US", { style: "currency", currency: "USD" })
              : "-"
          }
          accent="income"
        />
        <Card
          title="Total expenses (current month)"
          value={
            token
              ? totals.totalExpense.toLocaleString("en-US", { style: "currency", currency: "USD" })
              : "-"
          }
          accent="expense"
        />
        <Card
          title="Balance (current month)"
          value={
            token ? totals.balance.toLocaleString("en-US", { style: "currency", currency: "USD" }) : "-"
          }
          accent={
            !token ? "neutral" : totals.balance >= 0 ? "income" : "expense"
          }
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4">
          <div className="flex items-center justify-between mb-3">
            <h3 className="text-sm font-semibold text-slate-100">Income vs expenses</h3>
          </div>
          {!token ? (
            <p className="text-xs text-slate-400">Sign in to see the chart.</p>
          ) : (
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1f2937" />
                  <XAxis dataKey="name" stroke="#9ca3af" />
                  <YAxis stroke="#9ca3af" />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "#020617",
                      border: "1px solid #1f2937",
                      borderRadius: "0.5rem"
                    }}
                  />
                  <Legend />
                  <Bar dataKey="income" fill="#16a34a" name="Income" />
                  <Bar dataKey="expense" fill="#dc2626" name="Expense" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          )}
        </div>

        <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4">
          <div className="flex items-center justify-between mb-3">
            <h3 className="text-sm font-semibold text-slate-100">Recent transactions</h3>
          </div>
          {recentTransactions.length === 0 ? (
            <p className="text-xs text-slate-400">No recent transactions.</p>
          ) : (
            <ul className="divide-y divide-slate-800">
              {recentTransactions.map((tx) => (
                <li key={tx.id} className="py-2 flex items-center justify-between gap-3">
                  <div>
                    <p className="text-xs font-medium text-slate-100">{tx.description}</p>
                    <p className="text-[11px] text-slate-400">
                      {new Date(tx.date).toLocaleDateString()}
                    </p>
                  </div>
                  <p
                    className={`text-sm font-semibold ${
                      tx.type === "INCOME" ? "text-income" : "text-expense"
                    }`}
                  >
                    {tx.type === "EXPENSE" ? "-" : "+"}
                    {tx.value.toLocaleString("en-US", {
                      style: "currency",
                      currency: "USD"
                    })}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

