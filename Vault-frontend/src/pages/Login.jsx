import { useState } from "react";
import api from "../utils/api";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await api.post("/auth/login", form);
      const { token } = res.data;
      localStorage.setItem("token", token);
      localStorage.setItem("role", "USER");
      nav("/");
    } catch (err) {
      setMsg("Invalid email or password");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f0c29] via-[#302b63] to-[#24243e] flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-md bg-white/10 backdrop-blur-xl p-8 rounded-xl shadow-2xl border border-white/10 text-white">
        <h2 className="text-3xl font-bold mb-6 text-center">🔐 Secure Vault Login</h2>

        {msg && (
          <div className="text-red-400 text-sm mb-4 text-center font-medium">
            {msg}
          </div>
        )}

        <input
          type="email"
          placeholder="Email"
          className="w-full px-4 py-2 mb-4 bg-white/5 text-white border border-white/20 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
          onChange={(e) => setForm({ ...form, email: e.target.value })}
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full px-4 py-2 mb-2 bg-white/5 text-white border border-white/20 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
          onChange={(e) => setForm({ ...form, password: e.target.value })}
        />

        <p
          className="text-right text-sm text-purple-300 hover:underline cursor-pointer mb-4"
          onClick={() => nav("/forgot-password")}
        >
          Forgot Password?
        </p>

        <button
          onClick={handleLogin}
          className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 rounded-md transition"
        >
          Login
        </button>

        <p className="mt-4 text-center text-white/80 text-sm">
          Don’t have an account?{" "}
          <span
            onClick={() => nav("/register")}
            className="text-purple-300 hover:underline cursor-pointer"
          >
            Register here
          </span>
        </p>
      </div>
    </div>
  );
}
