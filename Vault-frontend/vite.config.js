import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite' // ✅ if you're insisting on using this

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],server: {
    port: 5173, // ✅ change this from 5175 (or whatever it was)
  },
})
