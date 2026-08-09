import { createContext, useEffect, useState, useCallback } from 'react';
import { authService } from '../services/authService';
import { setToken, clearToken, setUnauthorizedHandler } from '../services/api';

export const AuthContext = createContext(null);
const STORAGE_KEY = 'farm2biz_user';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const logout = useCallback(() => {
    clearToken();
    setUser(null);
    sessionStorage.removeItem(STORAGE_KEY);
  }, []);

  useEffect(() => {
    setUnauthorizedHandler(() => logout());
  }, [logout]);

  useEffect(() => {
    const saved = sessionStorage.getItem(STORAGE_KEY);
    if (saved) {
      const parsed = JSON.parse(saved);
      setToken(parsed.token);
      setUser(parsed.user);
    }
    setLoading(false);
  }, []);

  const persist = (data) => {
    setToken(data.token);
    const userObj = { userId: data.userId, name: data.name, email: data.email, role: data.role };
    setUser(userObj);
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify({ token: data.token, user: userObj }));
    return userObj;
  };

  const login = async (credentials) => persist(await authService.login(credentials));

  const register = async (payload) => {
    await authService.register(payload);
    return login({ email: payload.email, password: payload.password });
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
