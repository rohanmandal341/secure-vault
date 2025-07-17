import { useEffect, useState } from "react";
import api from "../utils/api";

export default function AdminAudit() {
  const [logs, setLogs] = useState([]);

  const fetch = async () => {
    try {
      const res = await api.get("/admin/audit");
      setLogs(res.data);
    } catch (error) {
      console.error("Failed to fetch audit logs", error);
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f0c29] via-[#302b63] to-[#24243e] flex items-center justify-center px-4">
      <div className="w-full max-w-6xl bg-white/5 backdrop-blur-lg shadow-2xl rounded-2xl p-8 border border-white/10">
        <h2 className="text-4xl font-bold text-white mb-8 flex items-center gap-3">
      
          Admin Audit Logs
        </h2>

        {logs.length === 0 ? (
          <p className="text-gray-300 text-center py-6">No logs available.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm table-auto rounded-md overflow-hidden">
              <thead className="bg-white/10 text-white/90">
                <tr>
                  <th className="py-3 px-4 text-left">👤 User</th>
                  <th className="py-3 px-4 text-left">📝 Action</th>
                  <th className="py-3 px-4 text-left">🌐 IP</th>
                  <th className="py-3 px-4 text-left">🕒 Timestamp</th>
                </tr>
              </thead>
              <tbody>
                {logs.map((log) => (
                  <tr key={log.id} className="border-t border-white/10 hover:bg-white/5 text-white/80">
                    <td className="py-3 px-4">{log.username}</td>
                    <td className="py-3 px-4">{log.action}</td>
                    <td className="py-3 px-4">{log.ipAddress}</td>
                    <td className="py-3 px-4 text-sm text-gray-300">
                      {new Date(log.timestamp).toLocaleString()}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
