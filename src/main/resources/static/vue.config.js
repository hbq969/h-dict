const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: 'ui-dict',
  publicPath: './',
  devServer: {
    proxy: {
      '/dev': {
        target: 'http://localhost:30150',
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/dev': '' },
      },
    },
  }
})
