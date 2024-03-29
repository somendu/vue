module.exports = {
  outputDir: '../Lineage-Upload/src/main/resources/static',
  transpileDependencies: ['vue-octicon'],
  configureWebpack: {
    devtool: 'source-map'
  },
  devServer: {
    port: 8090,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
};
