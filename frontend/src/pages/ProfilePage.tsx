import { FormEvent, useState } from "react";
import { api } from "../api/client";
import { useAuth } from "../context/AuthContext";
import { Button, Input } from "../components/ui";

export function ProfilePage() {
  const { user, logout } = useAuth();
  const [username, setUsername] = useState(user?.username ?? "");
  const [email, setEmail] = useState(user?.email ?? "");
  const [password, setPassword] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [deleting, setDeleting] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [passwordValidation, setPasswordValidation] = useState<string | null>(null);

  const handleSave = async (e: FormEvent) => {
    e.preventDefault();
    setPasswordValidation(null);
    if (!password.trim()) {
      setPasswordValidation("Password is required to save changes.");
      return;
    }
    setSaving(true);
    setError(null);
    setSuccess(null);
    try {
      await api.put("/users/me", {
        username,
        email,
        password
      });
      setSuccess("Profile updated successfully.");
      setPassword("");
    } catch (err: unknown) {
      console.error(err);
      setError("Could not update profile.");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    setDeleting(true);
    setError(null);
    try {
      await api.delete("/users/me");
      logout();
    } catch (err: unknown) {
      console.error(err);
      setError("Could not delete account.");
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className="space-y-6 max-w-xl mx-auto">
      <div>
        <h2 className="text-xl font-semibold text-slate-100">Profile</h2>
        <p className="text-xs text-slate-400 mt-1">Manage your personal information.</p>
      </div>

      {error && <p className="text-xs text-expense">{error}</p>}
      {success && <p className="text-xs text-income">{success}</p>}
      {passwordValidation && <p className="text-xs text-expense">{passwordValidation}</p>}

      <form
        onSubmit={handleSave}
        className="space-y-4 rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4 sm:px-5 sm:py-5"
      >
        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">Username</label>
          <Input value={username} onChange={(e) => setUsername(e.target.value)} required />
        </div>
        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">Email</label>
          <Input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">Password</label>
          <Input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Required to save changes"
          />
          <p className="mt-1 text-[11px] text-slate-400">
            For now, the backend requires your password to update profile details.
          </p>
        </div>
        <div className="flex justify-end">
          <Button type="submit" disabled={saving}>
            {saving ? "Saving..." : "Save changes"}
          </Button>
        </div>
      </form>

      <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-4 py-4 sm:px-5 sm:py-5">
        <h3 className="text-sm font-semibold text-slate-100 mb-2">Danger zone</h3>
        <p className="text-xs text-slate-400 mb-3">
          Deleting your account will remove all your data. This action cannot be undone.
        </p>
        {!confirmDelete ? (
          <Button
            type="button"
            className="bg-expense hover:bg-red-700"
            onClick={() => setConfirmDelete(true)}
          >
            Delete account
          </Button>
        ) : (
          <div className="flex flex-col items-start gap-2 sm:flex-row sm:items-center sm:gap-3">
            <p className="text-xs text-slate-200">Are you sure?</p>
            <Button
              type="button"
              variant="ghost"
              onClick={() => setConfirmDelete(false)}
            >
              Cancel
            </Button>
            <Button
              type="button"
              className="bg-expense hover:bg-red-700"
              onClick={handleDelete}
              disabled={deleting}
            >
              {deleting ? "Deleting..." : "Yes, delete my account"}
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}

