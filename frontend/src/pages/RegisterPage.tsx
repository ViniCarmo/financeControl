import { FormEvent, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Button, Input } from "../components/ui";

export function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await register({ username, email, password });
      const from = (location.state as { from?: string } | null)?.from;
      navigate(from || "/");
    } catch (err: unknown) {
      setError("Could not create account. Check data or try again later.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-950 px-4 py-6 sm:py-10">
      <div className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/80 px-5 py-6 sm:px-6 sm:py-8 shadow-xl">
        <h1 className="text-2xl font-semibold text-emerald-400 text-center mb-2">Finance Dashboard</h1>
        <p className="text-sm text-slate-400 text-center mb-6">Create your account</p>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Username</label>
            <Input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
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
              autoComplete="new-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="text-xs text-expense">{error}</p>}
          <Button type="submit" className="w-full mt-2" disabled={loading}>
            {loading ? "Creating account..." : "Sign up"}
          </Button>
        </form>
        <p className="mt-4 text-xs text-slate-400 text-center">
          Already have an account?{" "}
          <Link to="/login" className="text-emerald-400 hover:text-emerald-300 font-medium">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}

