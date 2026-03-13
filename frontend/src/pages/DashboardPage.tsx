import { useEffect, useMemo, useState } from "react";
import { api } from "../api/client";
import type { Summary, Transaction } from "../types";
import { Button, Card } from "../components/ui";
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

interface SummaryApiResponse extends Summary {
  id?: number;
}

export function DashboardPage() {
  const [summaries, setSummaries] = useState<SummaryApiResponse[]>([]);
  const [recentTransactions, setRecentTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [generating, setGenerating] = useState(false);

  const currentMonthSummary = useMemo(() => {
    if (summaries.length === 0) return null;
    return summaries[summaries.length - 1];
  }, [summaries]);

  const chartData = useMemo(
    () =>
      summaries.map((s) => ({
        month: s.month,
        income: s.totalIncome,
        expense: s.totalExpense
      })),
    [summaries]
  );

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [summaryRes, txRes] = await Promise.all([
        api.get<SummaryApiResponse[]>("/summary"),
        api.get("/transactions", { params: { page: 0, size: 10 } })
      ]);
      setSummaries(summaryRes.data);
      const txContent = "content" in txRes.data ? txRes.data.content : txRes.data;
      setRecentTransactions(txContent);
    } catch (err: unknown) {
      console.error(err);
      setError("Could not load dashboard data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, []);

  const handleGenerateSummary = async () => {
    setGenerating(true);
    try {
      await api.post("/summary/generate");
      await loadData();
    } catch (err: unknown) {
      console.error(err);
      setError("Could not generate summary for current month.");
    } finally {
      setGenerating(false);
    }
  };

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
        <Button onClick={handleGenerateSummary} disabled={generating}>
          {generating ? "Generating..." : "Generate current month summary"}
        </Button>
      </div>

      {error && <p className="text-xs text-expense">{error}</p>}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card
          title="Total income (current month)"
          value={
            currentMonthSummary
              ? currentMonthSummary.totalIncome.toLocaleString("en-US", {
                  style: "currency",
                  currency: "USD"
                })
              : "-"
          }
          accent="income"
        />
        <Card
          title="Total expenses (current month)"
          value={
            currentMonthSummary
              ? currentMonthSummary.totalExpense.toLocaleString("en-US", {
                  style: "currency",
                  currency: "USD"
                })
              : "-"
          }
          accent="expense"
        />
        <Card
          title="Balance (current month)"
          value={
            currentMonthSummary
              ? currentMonthSummary.balance.toLocaleString("en-US", {
                  style: "currency",
                  currency: "USD"
                })
              : "-"
          }
          accent={
            currentMonthSummary
              ? currentMonthSummary.balance >= 0
                ? "income"
                : "expense"
              : "neutral"
          }
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4">
          <div className="flex items-center justify-between mb-3">
            <h3 className="text-sm font-semibold text-slate-100">Income vs expenses</h3>
          </div>
          {chartData.length === 0 ? (
            <p className="text-xs text-slate-400">No summary data yet. Generate one to see the chart.</p>
          ) : (
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1f2937" />
                  <XAxis dataKey="month" stroke="#9ca3af" />
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

