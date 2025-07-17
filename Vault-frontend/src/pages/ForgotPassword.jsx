import { useState } from "react";
import api from "../utils/api";
import { useNavigate } from "react-router-dom";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  const handleSendOtp = async () => {
    try {
      await api.post("/auth/forgot", { email });
      setMsg("OTP sent to your email. Proceed to reset.");
    } catch (err) {
      setMsg("Email not found or error sending OTP.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="bg-white/10 backdrop-blur-md p-8 rounded-xl shadow-lg w-full max-w-md border border-white/10">
        <h2 className="text-2xl font-bold mb-6 text-center">🔁 Forgot Password</h2>

        {msg && <p className="mb-4 text-sm text-yellow-400">{msg}</p>}

        <input
          type="email"
          placeholder="Enter your registered email"
          className="w-full px-4 py-2 mb-4 bg-white/5 text-white border border-white/20 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          onChange={(e) => setEmail(e.target.value)}
        />

        <button
          onClick={handleSendOtp}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded"
        >
          Send OTP
        </button>

        <p className="mt-4 text-sm text-center text-gray-400">
          Got the OTP?{" "}
          <span
            onClick={() => nav("/reset-password")}
            className="text-blue-400 hover:underline cursor-pointer"
          >
            Reset Here
          </span>
        </p>
      </div>
    </div>
  );
}
