import React, { createContext, useContext, useEffect, useState } from "react";
import { api, LoginRequest, LoginResponse, RegisterRequest } from "../api/client";
import type { User } from "../types";

interface AuthContextValue {
  user: User | null;
  token: string | null;
  loading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchMe = async () => {
      if (!token) {
        setLoading(false);
        return;
      }
      try {
        const response = await api.get<User>("/users/me");
        setUser(response.data);
      } catch {
        localStorage.removeItem("token");
        setToken(null);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };
    fetchMe();
  }, [token]);

  const login = async (credentials: LoginRequest) => {
    const response = await api.post<LoginResponse>("/auth", credentials);
    const newToken = response.data.token;
    localStorage.setItem("token", newToken);
    setToken(newToken);
    const meResponse = await api.get<User>("/users/me");
    setUser(meResponse.data);
  };

  const register = async (data: RegisterRequest) => {
    await api.post("/users", data);
    await login({ email: data.email, password: data.password });
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextValue => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
};

