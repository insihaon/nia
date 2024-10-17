/* eslint-disable */
import Vue from 'vue'
import App from './App'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


import './assets/css/fonts/xeicon.min.css';
import LocaleKO from 'element-ui/lib/locale/lang/ko';
Vue.use(ElementUI, { locale: LocaleKO });

import 'element-theme-dark';

import axios from 'axios'
Vue.prototype.$http = axios

Vue.config.productionTip = true

new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
