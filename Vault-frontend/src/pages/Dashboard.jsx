import { useEffect, useState } from "react";
import api from "../utils/api";
import logo from "../assets/image.png";

export default function Dashboard() {
  const [files, setFiles] = useState([]);
  const [file, setFile] = useState();
  const [search, setSearch] = useState("");
  const [link, setLink] = useState("");

  const fetchFiles = async () => {
    try {
      const res = await api.get("/files");
      setFiles(res.data);
    } catch (err) {
      console.error("Error fetching files", err);
    }
  };

  const handleUpload = async () => {
    if (!file) return alert("Select a file to upload");
    const form = new FormData();
    form.append("file", file);
    await api.post("/files", form);
    setFile(null);
    fetchFiles();
  };

  const handleDelete = async (id) => {
    await api.delete(`/files/${id}`);
    fetchFiles();
  };

  const handleDownload = (id) =>
    window.open(`http://localhost:1010/api/files/download/${id}`, "_blank");

  const handleShare = async (id) => {
    const res = await api.post(`/files/share/${id}`);
    setLink(res.data);
  };

  const handleSearch = async () => {
    const res = await api.get(`/files/search?keyword=${search}`);
    setFiles(res.data);
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  return (
    <div className="min-h-screen flex bg-gradient-to-br from-[#0f0c29] via-[#302b63] to-[#24243e] text-white">
      {/* Sidebar */}
      <aside className="w-64 bg-black/30 p-6 border-r border-white/10 hidden md:flex flex-col gap-4">
        <div className="flex items-center gap-3 mb-6">
          <img src={logo} alt="Vault Logo" className="w-10 h-10 rounded" />
          <h1 className="text-2xl font-bold">Secure Vault</h1>
        </div>
        <nav className="flex flex-col gap-4 text-sm">
          <span className="hover:text-purple-400 cursor-pointer">📁 My Files</span>
          <span className="hover:text-purple-400 cursor-pointer">📤 Shared</span>
          <span className="hover:text-purple-400 cursor-pointer">🕑 Recent</span>
          <span className="hover:text-purple-400 cursor-pointer">🗑️ Trash</span>
        </nav>
        <div className="mt-auto text-xs text-white/60">
          <p>Used: 0 of 1 GB</p>
          <button className="text-purple-400 hover:underline mt-1">Upgrade</button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 px-6 py-10">
        {/* Header Controls */}
        <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-6">
          <div className="flex-1 flex gap-3">
            <input
              type="file"
              className="bg-white/5 border border-white/20 text-sm rounded p-2 w-full md:w-auto"
              onChange={(e) => setFile(e.target.files[0])}
            />
            <button
              onClick={handleUpload}
              className="bg-blue-600 hover:bg-blue-700 px-4 py-2 text-sm font-medium rounded"
            >
              Upload
            </button>
          </div>
          <div className="flex gap-3 w-full md:w-auto">
            <input
              type="text"
              placeholder="🔍 Search files..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="bg-white/5 border border-white/20 text-sm rounded p-2 w-full"
            />
            <button
              onClick={handleSearch}
              className="bg-purple-600 hover:bg-purple-700 px-4 py-2 text-sm font-medium rounded"
            >
              Search
            </button>
          </div>
        </div>

        {/* File Table */}
        <div className="bg-white/5 p-6 rounded-xl shadow-lg">
          {files.length === 0 ? (
            <p className="text-white/80 text-center py-6">No files uploaded yet.</p>
          ) : (
            <table className="w-full text-sm table-auto">
              <thead className="text-white/90 bg-white/10 rounded">
                <tr>
                  <th className="text-left p-3">📄 Filename</th>
                  <th className="text-left p-3">⚙️ Actions</th>
                </tr>
              </thead>
              <tbody>
                {files.map((f) => (
                  <tr
                    key={f.id}
                    className="border-t border-white/10 hover:bg-white/10 transition"
                  >
                    <td className="p-3">{f.fileName}</td>
                    <td className="p-3 flex flex-wrap gap-2">
                      <button
                        onClick={() => handleDownload(f.id)}
                        className="bg-green-600 hover:bg-green-700 px-3 py-1 rounded"
                      >
                        Download
                      </button>
                      <button
                        onClick={() => handleDelete(f.id)}
                        className="bg-red-600 hover:bg-red-700 px-3 py-1 rounded"
                      >
                        Delete
                      </button>
                      <button
                        onClick={() => handleShare(f.id)}
                        className="bg-indigo-600 hover:bg-indigo-700 px-3 py-1 rounded"
                      >
                        Share
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Shareable Link */}
        {link && (
          <div className="mt-6 p-4 bg-black/20 border border-indigo-500 rounded-md">
            <p className="mb-2 font-medium text-indigo-300">🔗 Shareable Link:</p>
            <a
              href={link}
              target="_blank"
              rel="noreferrer"
              className="text-blue-400 underline break-all"
            >
              {link}
            </a>
          </div>
        )}
      </main>
    </div>
  );
}
