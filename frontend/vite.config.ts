import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "./build/dist",
  },
  server: {
    proxy: {
      "/api": "http://localhost:8080",
    },
  },
  define: {
    APP_VERSION: JSON.stringify(process.env["FLY_ALLOC_ID"] || "local"),
  },
});
