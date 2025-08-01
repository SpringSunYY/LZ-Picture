import { fileURLToPath, URL } from 'node:url'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import autoprefixer from 'autoprefixer'
import tailwindcss from 'tailwindcss'
// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    createSvgIconsPlugin({
      iconDirs: [path.resolve(__dirname, 'src/assets/icons/svg')],
      symbolId: 'icon-[name]',
    }),
  ],
  css: {
    postcss: {
      plugins: [tailwindcss, autoprefixer],
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
        target: 'http://localhost:8080/user',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/dev-api/, ''),
      },
      '/dev-api/profile': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/dev-api/, ''),
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
    rollupOptions: {
      output: {
        manualChunks: {
          // 将第三方库单独打包
          'vue-vendor': ['vue', 'vue-router', 'pinia', 'pinia-plugin-persistedstate'],
          'ui-vendor': ['ant-design-vue', 'v-viewer', 'viewerjs', 'vue-cropperjs'],
          'utils-vendor': ['axios', 'lodash-es', 'jsencrypt', 'libphonenumber-js'],
          'uppy-vendor': [
            '@uppy/core',
            '@uppy/dashboard',
            '@uppy/image-editor',
            '@uppy/locales/lib/zh_CN'
          ],
          'filepond-vendor': [
            'filepond-plugin-file-encode',
            'filepond-plugin-file-validate-size',
            'filepond-plugin-file-validate-type',
            'filepond-plugin-image-exif-orientation',
            'filepond-plugin-image-preview',
            'filepond-plugin-image-transform',
            'vue-filepond',
          ],
          'editor-vendor': ['md-editor-v3'],
          'vueuse-vendor': ['@vueuse/core'],
          // 将较大的组件单独打包
          'about-components': [
            '@/components/about/AboutLayout.vue',
            '@/components/about/ContactSection.vue',
            '@/components/about/ContactSponsorsSection.vue',
            '@/components/about/CopyrightSection.vue',
            '@/components/about/HelpCenterSection.vue',
            '@/components/about/JoinUsSelection.vue',
            '@/components/about/StorySection.vue',
            '@/components/about/TeamSection.vue',
            '@/components/about/UserAgreementSection.vue',
            '@/components/about/VisionSection.vue',
          ],
        },
      },
    },
    chunkSizeWarningLimit: 1000, // 将警告阈值提高到 1000 kB
  },
})
