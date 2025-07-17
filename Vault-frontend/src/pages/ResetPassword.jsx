import { useState } from "react";
import api from "../utils/api";
import { useNavigate } from "react-router-dom";

export default function ResetPassword() {
  const [form, setForm] = useState({ email: "", otp: "", newPassword: "" });
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  const handleReset = async () => {
    try {
      await api.post("/auth/reset", form);
      setMsg("✅ Password reset successfully. Login now!");
      setTimeout(() => nav("/login"), 2000);
    } catch (err) {
      setMsg("❌ Invalid OTP or email.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="bg-white/10 backdrop-blur-md p-8 rounded-xl shadow-lg w-full max-w-md border border-white/10">
        <h2 className="text-2xl font-bold mb-6 text-center">🔒 Reset Password</h2>

        {msg && <p className="mb-4 text-sm text-yellow-400">{msg}</p>}

        <input
          type="email"
          placeholder="Your Email"
          className="w-full px-4 py-2 mb-3 bg-white/5 text-white border border-white/20 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          onChange={(e) => setForm({ ...form, email: e.target.value })}
        />

        <input
          type="text"
          placeholder="Enter OTP"
          className="w-full px-4 py-2 mb-3 bg-white/5 text-white border border-white/20 rounded"
          onChange={(e) => setForm({ ...form, otp: e.target.value })}
        />

        <input
          type="password"
          placeholder="New Password"
          className="w-full px-4 py-2 mb-4 bg-white/5 text-white border border-white/20 rounded"
          onChange={(e) => setForm({ ...form, newPassword: e.target.value })}
        />

        <button
          onClick={handleReset}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded"
        >
          Reset Password
        </button>
      </div>
    </div>
  );
}
