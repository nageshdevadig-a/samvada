import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  build: {
    minify: 'terser', // Standard industry minifier
    terserOptions: {
      compress: {
        // This removes all console.logs but keeps console.error/warn
        drop_console: true,
        drop_debugger: true,
      },
    },
  },
})
