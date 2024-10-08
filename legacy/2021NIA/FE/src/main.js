import Vue from 'vue'
import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import 'vue2-animate/dist/vue2-animate.min.css'

import Element from 'element-ui'
import './styles/element-variables.scss'
import 'animate.css/animate.min.css'

import defaultLang from 'element-ui/lib/locale/lang/ko'

import App from './App'
import store from './store'
import router from './router'
import * as filters from '@/filters'

import '@/permission'
import '@/utils/extend'
import '@/utils/error-log'

import '@/quasar'

import '@/styles/index.scss'
import 'ag-grid-community/dist/styles/ag-grid.scss'
import 'ag-grid-community/dist/styles/ag-theme-material.css'

import './assets/css/tailwindcss.css'

import LoadScript from 'vue-plugin-load-script'

import VModal from 'vue-js-modal'
import { i18n } from './i18n'

import DomHints from 'vue-dom-hints'

import splitPane from 'vue-splitpane'
Vue.component('SplitPane', splitPane)

import LottieVuePlayer from '@lottiefiles/vue-lottie-player'
Vue.use(LottieVuePlayer)

import * as echarts from 'echarts'
import { plugin } from 'echarts-for-vue'
Vue.use(plugin, { echarts })

import SvgIcon from '@jamescoyle/vue-icon'
Vue.component('SvgIcon', SvgIcon)

import ElTableDraggable from 'el-table-draggable'
Vue.component('ElTableDraggable', ElTableDraggable)

process.env.NODE_ENV === 'development' ? require('@/min/global') : require('@/min/global.min')

Vue.use(LoadScript)
Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  locale: defaultLang,
})
Vue.use(VModal, { dynamic: true })
if (process.env.NODE_ENV !== 'production') {
  Vue.use(DomHints)
}

Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key])
})

Vue.config.devtools = process.env.NODE_ENV !== 'production'
Vue.config.performance = false
Vue.config.productionTip = false

Vue.prototype.setDefaultPagination = () => {
  var defaultOpt = {
    total: 0,
    currentPage: 1,
    pageSize: 10,
    data: []
  }
  return defaultOpt
}

if (process.env.NODE_ENV !== 'development') {
  console._ = {
    info: console.info,
    warn: console.warn,
    log: console.log,
    error: console.error
  }
  console.info = () => { }
  console.warn = () => { }
  console.log = () => { }
  console.error = () => { }
}

const vue = new Vue({
  el: '#app',
  i18n,
  router,
  store,
  render: (h) => h(App),
})
