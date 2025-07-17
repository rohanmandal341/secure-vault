import { useState } from "react";
import api from "../utils/api";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  const handleRegister = async () => {
    try {
      const res = await api.post("/auth/register", form);
      setMsg("✅ Registration successful. Please verify your email.");
      setForm({ name: "", email: "", password: "" });
    } catch (err) {
      setMsg("❌ Registration failed. Try again.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f0c29] via-[#302b63] to-[#24243e] flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-md bg-white/10 backdrop-blur-xl p-8 rounded-xl shadow-2xl border border-white/10 text-white">
        <h2 className="text-3xl font-bold mb-6 text-center">📝 Register Account</h2>

        {msg && (
          <div className={`text-sm mb-4 text-center font-medium ${
            msg.startsWith("✅") ? "text-green-400" : "text-red-400"
          }`}>
            {msg}
          </div>
        )}

        <input
          type="text"
          placeholder="Name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          className="w-full px-4 py-2 mb-4 bg-white/5 text-white border border-white/20 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
        />

        <input
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          className="w-full px-4 py-2 mb-4 bg-white/5 text-white border border-white/20 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
        />

        <input
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          className="w-full px-4 py-2 mb-6 bg-white/5 text-white border border-white/20 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
        />

        <button
          onClick={handleRegister}
          className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 rounded-md transition"
        >
          Register
        </button>

        <p className="mt-4 text-center text-white/80 text-sm">
          Already have an account?{" "}
          <span
            onClick={() => nav("/login")}
            className="text-purple-300 hover:underline cursor-pointer"
          >
            Login here
          </span>
        </p>
      </div>
    </div>
  );
}
