import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  base: "/",
  plugins: [react()],
  preview: {
    port: 8000,
    strictPort: true,
  },
  server: {
    port: 8000,
    strictPort: true,
    host: true,
    origin: "http://localhost:8000",
    proxy: {
      '/api': {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
})
