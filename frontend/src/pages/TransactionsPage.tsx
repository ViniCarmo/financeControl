import { FormEvent, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { api } from "../api/client";
import type { PaginatedResponse, Transaction, TransactionType } from "../types";
import { Button, Input, Modal, Select } from "../components/ui";
import { useAuth } from "../context/AuthContext";

interface TransactionFormState {
  value: string;
  type: TransactionType;
  date: string;
  description: string;
}

const emptyForm: TransactionFormState = {
  value: "",
  type: "INCOME",
  date: "",
  description: ""
};

export function TransactionsPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [filterType, setFilterType] = useState<"ALL" | TransactionType>("ALL");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editing, setEditing] = useState<Transaction | null>(null);
  const [form, setForm] = useState<TransactionFormState>(emptyForm);
  const [saving, setSaving] = useState(false);

  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [deleting, setDeleting] = useState(false);

  const loadTransactions = async (pageToLoad = page) => {
    if (!token) {
      setTransactions([]);
      setTotalPages(0);
      setLoading(false);
      setError(null);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<PaginatedResponse<Transaction>>("/transactions", {
        params: { page: pageToLoad, size }
      });
      const data = res.data;
      setTransactions(data.content);
      setTotalPages(data.totalPages ?? 1);
    } catch (err: unknown) {
      console.error(err);
      setError("Could not load transactions.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadTransactions(0);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filterType, token]);

  useEffect(() => {
    void loadTransactions(page);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, token]);

  const openCreateModal = () => {
    if (!token) {
      navigate("/login", { state: { from: `${location.pathname}${location.search}` } });
      return;
    }
    setEditing(null);
    setForm({
      ...emptyForm,
      date: new Date().toISOString().slice(0, 10)
    });
    setIsModalOpen(true);
  };

  const openEditModal = (tx: Transaction) => {
    if (!token) {
      navigate("/login", { state: { from: `${location.pathname}${location.search}` } });
      return;
    }
    setEditing(tx);
    setForm({
      value: tx.value.toString(),
      type: tx.type,
      date: tx.date.slice(0, 10),
      description: tx.description
    });
    setIsModalOpen(true);
  };

  const handleSave = async (e: FormEvent) => {
    e.preventDefault();
    if (!token) {
      navigate("/login", { state: { from: `${location.pathname}${location.search}` } });
      return;
    }
    setSaving(true);
    setError(null);
    try {
      const payload = {
        value: parseFloat(form.value),
        type: form.type,
        date: form.date,
        description: form.description
      };
      if (editing) {
        await api.put(`/transactions/${editing.id}`, payload);
      } else {
        await api.post("/transactions", payload);
      }
      setIsModalOpen(false);
      setEditing(null);
      setForm(emptyForm);
      await loadTransactions();
    } catch (err: unknown) {
      console.error(err);
      setError("Could not save transaction.");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = (id: number) => {
    if (!token) {
      navigate("/login", { state: { from: `${location.pathname}${location.search}` } });
      return;
    }
    setDeleteId(id);
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    if (!token) {
      navigate("/login", { state: { from: `${location.pathname}${location.search}` } });
      return;
    }
    setDeleting(true);
    setError(null);
    try {
      await api.delete(`/transactions/${deleteId}`);
      setDeleteId(null);
      await loadTransactions();
    } catch (err: unknown) {
      console.error(err);
      setError("Could not delete transaction.");
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-100">Transactions</h2>
          <p className="text-xs text-slate-400 mt-1">Manage all your income and expenses.</p>
        </div>
        <Button onClick={openCreateModal}>Add transaction</Button>
      </div>

      {error && <p className="text-xs text-expense">{error}</p>}
      {!token && (
        <div className="rounded-xl border border-slate-800 bg-slate-900/40 px-4 py-3">
          <p className="text-xs text-slate-300">
            You are browsing in read-only mode. Sign in to add, edit, or delete transactions.
          </p>
        </div>
      )}
      {token && filterType !== "ALL" && (
        <div className="rounded-xl border border-slate-800 bg-slate-900/40 px-4 py-3">
          <p className="text-xs text-slate-300">
            Filter is a visual indicator only right now. The backend does not support type filtering yet, so results are not filtered server-side.
          </p>
        </div>
      )}

      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-2 text-xs text-slate-300">
          <span>Filter by type:</span>
          <Select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value as "ALL" | TransactionType)}
            className="w-40"
          >
            <option value="ALL">All</option>
            <option value="INCOME">Income</option>
            <option value="EXPENSE">Expense</option>
          </Select>
        </div>

        <div className="flex items-center gap-2 text-xs text-slate-300">
          <Button
            variant="secondary"
            disabled={page === 0 || loading || !token}
            onClick={() => setPage((p) => Math.max(0, p - 1))}
          >
            Previous
          </Button>
          <span>
            Page {page + 1} of {totalPages || 1}
          </span>
          <Button
            variant="secondary"
            disabled={page + 1 >= totalPages || loading || !token}
            onClick={() => setPage((p) => p + 1)}
          >
            Next
          </Button>
        </div>
      </div>

      <div className="overflow-x-auto rounded-xl border border-slate-800 bg-slate-900/60">
        <table className="min-w-full text-left text-xs">
          <thead className="border-b border-slate-800 bg-slate-900/80">
            <tr>
              <th className="px-4 py-2 font-medium text-slate-400">Date</th>
              <th className="px-4 py-2 font-medium text-slate-400">Description</th>
              <th className="px-4 py-2 font-medium text-slate-400">Type</th>
              <th className="px-4 py-2 font-medium text-slate-400">Value</th>
              <th className="px-4 py-2 font-medium text-slate-400 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={5} className="px-4 py-6 text-center text-slate-400">
                  Loading transactions...
                </td>
              </tr>
            ) : !token ? (
              <tr>
                <td colSpan={5} className="px-4 py-6 text-center text-slate-400">
                  Sign in to view your transactions.
                </td>
              </tr>
            ) : transactions.length === 0 ? (
              <tr>
                <td colSpan={5} className="px-4 py-6 text-center text-slate-400">
                  No transactions yet. Start by adding one.
                </td>
              </tr>
            ) : (
              transactions.map((tx) => (
                <tr key={tx.id} className="border-t border-slate-800">
                  <td className="px-4 py-2 text-slate-200">
                    {new Date(tx.date).toLocaleDateString()}
                  </td>
                  <td className="px-4 py-2 text-slate-200">{tx.description}</td>
                  <td
                    className={`px-4 py-2 font-medium ${
                      tx.type === "INCOME" ? "text-income" : "text-expense"
                    }`}
                  >
                    {tx.type === "INCOME" ? "Income" : "Expense"}
                  </td>
                  <td className="px-4 py-2 text-slate-200">
                    {tx.type === "EXPENSE" ? "-" : "+"}
                    {tx.value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                  </td>
                  <td className="px-4 py-2 text-right space-x-2">
                    <Button
                      variant="secondary"
                      className="text-xs px-2 py-1"
                      onClick={() => openEditModal(tx)}
                    >
                      Edit
                    </Button>
                    <Button
                      variant="ghost"
                      className="text-xs px-2 py-1 text-expense hover:bg-slate-800"
                      onClick={() => confirmDelete(tx.id)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        title={editing ? "Edit transaction" : "New transaction"}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      >
        <form onSubmit={handleSave} className="space-y-3">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Value</label>
              <Input
                type="number"
                step="0.01"
                value={form.value}
                onChange={(e) => setForm((f) => ({ ...f, value: e.target.value }))}
                required
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Type</label>
              <Select
                value={form.type}
                onChange={(e) =>
                  setForm((f) => ({ ...f, type: e.target.value as TransactionType }))
                }
              >
                <option value="INCOME">Income</option>
                <option value="EXPENSE">Expense</option>
              </Select>
            </div>
          </div>
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Date</label>
            <Input
              type="date"
              value={form.date}
              onChange={(e) => setForm((f) => ({ ...f, date: e.target.value }))}
              required
            />
          </div>
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Description</label>
            <Input
              value={form.description}
              onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
              required
            />
          </div>
          <div className="flex justify-end gap-2 pt-2">
            <Button
              type="button"
              variant="ghost"
              onClick={() => {
                setIsModalOpen(false);
                setEditing(null);
              }}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={saving}>
              {saving ? "Saving..." : "Save"}
            </Button>
          </div>
        </form>
      </Modal>

      <Modal
        title="Delete transaction"
        isOpen={deleteId !== null}
        onClose={() => setDeleteId(null)}
      >
        <p className="text-xs text-slate-200 mb-4">
          Are you sure you want to delete this transaction? This action cannot be undone.
        </p>
        <div className="flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={() => setDeleteId(null)}>
            Cancel
          </Button>
          <Button type="button" variant="primary" className="bg-expense hover:bg-red-700" onClick={handleDelete} disabled={deleting}>
            {deleting ? "Deleting..." : "Delete"}
          </Button>
        </div>
      </Modal>
    </div>
  );
}

