import { createContext, useContext, useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";

const AuthContext = createContext(null);

function decodeJwt(token) {
  try {
    const payload = token.split(".")[1];
    const json = atob(payload.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(json);
  } catch {
    return null;
  }
}

function extractUser(jwt) {
  const p = decodeJwt(jwt);
  if (!p) return null;
  return {
    email: p.sub,
    roles: p.roles || [],
    userId: p.userId ?? null,
  };
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const stored = localStorage.getItem("token");
    if (!stored) return;
    const payload = decodeJwt(stored);
    if (payload && payload.exp * 1000 > Date.now()) {
      setToken(stored);
      setUser(extractUser(stored));
    } else {
      localStorage.removeItem("token");
    }
  }, []);

  const login = async (email, password) => {
    const res = await axiosClient.post("/login", { email, password });
    const jwt = res.data.token;
    localStorage.setItem("token", jwt);
    setToken(jwt);
    setUser(extractUser(jwt));
    return extractUser(jwt);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
  };

  const isAdmin = () => (user?.roles || []).includes("ROLE_ADMIN");

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
  return ctx;
}