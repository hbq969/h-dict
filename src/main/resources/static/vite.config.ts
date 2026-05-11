// vite.config.ts 配置参考 https://cn.vitejs.dev/config/

import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import autoprefixer from 'autoprefixer'
// 预加载插件
import Components from 'unplugin-vue-components/vite';
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers';

// https://vite.dev/config/
export default defineConfig({
  base: './',
  plugins: [
    // 预加载组件
    Components({
      resolvers: [ElementPlusResolver()]
    }),
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
    postcss: {
      plugins: [autoprefixer()] // 自动加浏览器前缀
    }
  },
  // 预构建依赖
  optimizeDeps: {
    include: ['lodash-es', 'element-plus/es/']
  },
  build: {
    outDir: 'hbq969-dict',
    emptyOutDir: true,
    // 根据项目实际需求调整（单位：kb）
    chunkSizeWarningLimit: 1024,
    rollupOptions: {
      onwarn(warning, warn) {
        // 忽略 @vueuse/core 中的 PURE 注释位置警告
        if (warning.code === 'THIS_IS_UNDEFINED') { warn(warning); return; }
        if (warning.message?.includes('PURE')) { return; }
        warn(warning)
      },
      output: {
        // 统一字体文件名格式
        assetFileNames: 'assets/[name]-[hash][extname]',
        // 分模块打包，减少单个文件的体积
        manualChunks: {
          // 核心框架合并
          libs: ['vue', 'vue-router', 'pinia'],
          element: ['element-plus'],
          // 工具库合并
          utils: ['lodash-es', 'axios', 'echarts']
        }
      }
    }
  },
  server: {
    // 设置开发服务器端口
    port: 3000,
    // 启动时自动打开浏览器
    open: true,
    proxy: {
      '/dev': {
        // 代理目标地址
        target: 'http://localhost:30120',
        // 是否改变源地址
        changeOrigin: true,
        // 如果是https接口，需要配置这个参数
        secure: false,
        // 重写路径
        rewrite: (path) => path.replace(/^\/dev/, '')
      }
    }
  }
})
