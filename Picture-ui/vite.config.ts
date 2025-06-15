import { fileURLToPath, URL } from 'node:url'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import  autoprefixer from 'autoprefixer'
import tailwindcss from 'tailwindcss'
import Components from 'unplugin-vue-components/vite'
import { UndrawUiResolver } from 'undraw-ui/es/resolvers'
// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    createSvgIconsPlugin({
      iconDirs: [path.resolve(__dirname, 'src/assets/icons/svg')],
      symbolId: 'icon-[name]',
    }),
    Components({
      resolvers: [UndrawUiResolver]
    }),
  ],
  css: {
    postcss: {
      plugins: [
        tailwindcss,
        autoprefixer,
      ],
    },
    preprocessorOptions: {
      scss: {
        additionalData: `@use "sass:color";`,
      },
    },
  },
  server: {
    port: 80,
    host: true,
    open: true,
    proxy: {
      '/dev-api': {
        // target: 'http://8.138.211.218:8080/user',
        target: 'http://localhost:8080/user',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/dev-api/, ''),
      },
      '/profile': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/dev-api/, '')
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@/views': path.resolve(__dirname, './src/views'),
    },
  },
  define: {
    __VUE_PROD_DEVTOOLS__: false,
  },
  build: {
    sourcemap: false, // 禁用源映射
  },
})
