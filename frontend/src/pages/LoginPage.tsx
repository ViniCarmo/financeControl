import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Button, Input } from "../components/ui";

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login({ email, password });
      navigate("/");
    } catch (err: unknown) {
      setError("Invalid credentials or server error.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-950 px-4">
      <div className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/80 px-6 py-8 shadow-xl">
        <h1 className="text-2xl font-semibold text-emerald-400 text-center mb-2">Finance Dashboard</h1>
        <p className="text-sm text-slate-400 text-center mb-6">Sign in to manage your finances</p>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Email</label>
            <Input
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Password</label>
            <Input
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="text-xs text-expense">{error}</p>}
          <Button type="submit" className="w-full mt-2" disabled={loading}>
            {loading ? "Signing in..." : "Sign in"}
          </Button>
        </form>
        <p className="mt-4 text-xs text-slate-400 text-center">
          Do not have an account?{" "}
          <Link to="/register" className="text-emerald-400 hover:text-emerald-300 font-medium">
            Sign up
          </Link>
        </p>
      </div>
    </div>
  );
}

