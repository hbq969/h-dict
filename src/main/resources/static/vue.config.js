const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: 'hbq969-dict',
  publicPath: './',
  devServer: {
    proxy: {
      '/dev': {
        target: 'http://localhost:32100/cscenter-workbench',
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/dev': '' },
      }
    },
  }
})
