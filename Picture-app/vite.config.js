import { defineConfig } from 'vite'
import uniPlugin from '@dcloudio/vite-plugin-uni'
import { fileURLToPath, URL } from 'node:url'
import { uniPrettyUrl } from '@hughcube/uni-pretty-url/vite'
import UniPages from '@uni-helper/vite-plugin-uni-pages'
// uview-plus 3.x 官方 Root 注入插件
// 编译期在 App.up.vue 的 <UpRootView/> 内注入 <up-toast ref="upGlobalToastRef"/>
// 与 <up-notify ref="upGlobalNotifyRef"/>,并自动调用 setRootToastRef / setRootNotifyRef
// 必须注册在 uni() 之前
// 见 https://uview-plus.jiangruyi.com/guide/root.html
import UniUpRoot from 'uview-plus/libs/root/index.js'

const uni = uniPlugin.default || uniPlugin

export default defineConfig({
    plugins: [
        UniPages({
            exclude: ['**/components/**/**.*'],
            dts: 'src/types/uni-pages.d.ts',
        }),
        // 必须在 uni() 前面：去掉 H5 地址栏的 /pages/ 前缀
        // 业务跳转照旧写 /pages/xxx,地址栏自动显示 /xxx
        uniPrettyUrl(),
        // uview-plus Root 注入(必须在 uni() 之前)
        UniUpRoot({
            rootFileName: 'App.up',
            autoCreateRootFile: true,
        }),
        uni(),
    ],
    // H5 端开发服务器代理：避免浏览器 CORS 预检(OPTIONS)问题
    // 配置后，前端请求以 /api 开头会被 Vite 转发到 target 后端，
    // 浏览器看到的是同源请求，根本不会触发 OPTIONS 预检。
    // 生产环境如需同样效果，请用 Nginx 反向代理。
    server: {
        host: '0.0.0.0',
        port: 9000,
        proxy: {
            // 例如：前端 uni.request({ url: '/api/login' })
            // 会被 Vite 转发到 http://localhost:8080/user/login
            '/api': {
                target: 'http://localhost:8080/user',
                changeOrigin: true,
                // 如果你不想在 url 里保留 /api 前缀就解开下面这行
                rewrite: (path) => path.replace(/^\/api/, ''),
            },
        },
    },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url)),
        },
    },
    css: {
        preprocessorOptions: {
            scss: {
                // silenceDeprecations: 关掉 sass 1.80+ 的 deprecation 警告
                // - 'import': @import 在 dart-sass 3.0 会移除
                // - 'global-builtin': 全局 unquote()/lighten() 等函数在 3.0 会移除
                // - 'color-functions': rgba($color, 0.08) 这类老函数被 color.adjust 替代
                // - 'legacy-js-api': 旧 sass.render() 同步 API
                silenceDeprecations: ['legacy-js-api', 'color-functions', 'import', 'global-builtin'],
            },
        },
    },
})
