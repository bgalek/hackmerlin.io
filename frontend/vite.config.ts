import { defineConfig } from "vite";
import react from "@vitejs/plugin-react"; // https://vitejs.dev/config/

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "build/dist/static",
  },
  server: {
    proxy: {
      "/api": "http://localhost:8080",
    },
  },
});
