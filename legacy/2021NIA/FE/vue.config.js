'use strict'
const path = require('path')
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')
const WebpackShellPlugin = require('webpack-shell-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const liveReload = true

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following method:
// port = 4000 npm run dev OR npm run dev --port = 4000
const port = process.env.port || process.env.npm_config_port || 4000 // dev port
console.log(`${getTitleName()} server started: port=${port}, env=${process.env.NODE_ENV}, project=${process.env.VUE_APP_PROJECT}`)

function getTitleName() {
  return process.env.VUE_APP_TITLE
}

function getBaseUrl() {
  return process.env.BASE_URL ?? '/'
}

function isProduction() {
  // const env = ['development', 'staging', 'production']
  return ['staging', 'production'].includes(process.env.NODE_ENV)
}

function getPlugIns() {
  if (!isProduction()) return []
  var plugins = [
    new (require('webpack').IgnorePlugin)({
      resourceRegExp: /\/views-demo.+/
    }),
    new UglifyJsPlugin({
      uglifyOptions: {
        minimize: true,
        compress: true,
        warnings: false,
        mangle: true
      }
    })
  ]

  console.log('=================================================')
  console.log(new Date().toString())
  var commands = []
  var dir = getOutputDir()

  commands = [...commands, ...[
    // [for window]
    `echo BUILD-TIME : %date% %time% > ${dir}/version.txt`,
    `echo BUILD-USER : %username% >> ${dir}/version.txt`,
    `echo BUILD-HOST : %userdomain% >> ${dir}/version.txt`,
    `echo publicPath : ${getBaseUrl()} >> ${dir}/version.txt`,
    `type ${dir}/version.txt`
    // [for linux]
    // 'date > ' + dir + '/version.txt',
    // 'whoami >> ' + dir + '/version.txt',
    // 'cat ' + dir + '/version.txt',
  ]]

  plugins.push(
    new WebpackShellPlugin({
      onBuildExit: commands
    })
  )

  return plugins
}

function getOutputDir() {
  let dir = 'dist'
  switch (process.env.VUE_APP_PROJECT) {
    case 'datahub':
      dir = '../BE/app-dataHub/src/main/resources/static'
      break
  }
  console.log(`OutputDir=${dir}`)
  return dir
}

module.exports = {
  publicPath: getBaseUrl(),
  // publicIgnore: [/extlib\/*\/*((?!min).).js$/],

  outputDir: getOutputDir(),
  assetsDir: 'assets',
  lintOnSave: !isProduction(),
  productionSourceMap: true,

  devServer: {
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept',
      'Access-Control-Expose-Headers': 'Client-Addr'
    },
    host: '0.0.0.0',
    port: port,
    open: false,
    overlay: {
      // overlay: true, // 여기서 overlay: false는 오류를 더이상 화면에 표시하지 않겠다는 말임.
      warnings: false,
      errors: true
    },
    // proxy: {
    //   '/ws-stomp': {
    //     target: 'http://127.0.0.1:8070',
    //     ws: true,
    //     changeOrigin: true
    //   }
    // },
    // before: require('./mock/mock-server.js'), // nodejs 서버 실행
    // #region no live-reload start
    hot: liveReload,
    inline: liveReload,
    liveReload: liveReload
    // #endregion no live-reload start
  },
  configureWebpack: {
    devtool: 'source-map',
    name: getTitleName(),
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },
    context: __dirname,
    node: {
      __filename: true
    },
    plugins: getPlugIns()
  },

  chainWebpack(config) {
    // config.module
    //   .rule('js')
    //   .test(/\.jsx?$/)
    //   .exclude
    //   .add(/extlib\/*\/*((?!min).)*.js/) // Regexp: ignore anything inside of path/to/folder that has .ignore in the file extension
    //   .end()

    // 파일복사 예외 파일 설정(배포시 제외 파일/폴더)
    config.plugin('copy').tap(([options]) => {
      // options[0].ignore.push('extlib/core')
      return [options]
    })

    // it can improve the speed of the first screen, it is recommended to turn on preload
    config.plugin('preload').tap(() => [
      {
        rel: 'preload',
        // to ignore runtime.js
        fileBlacklist: [/\.map$/, /hot-update\.js$/, /runtime\..*\.js$/],
        include: 'initial'
      }
    ])

    config.plugins.delete('prefetch')

    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config
      .when(isProduction(),
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
  },

  pluginOptions: {
    quasar: {
      importStrategy: 'kebab',
      rtlSupport: false
    }
  },

  transpileDependencies: [
    'quasar'
  ]
}
