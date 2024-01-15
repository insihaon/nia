import { AppOptions } from '@/class/appOptions'
import { LocationHistory } from '@/class/locationHistory'
import Layout from '@/layout'
import Vue from 'vue'
import vueDebounce from 'vue-debounce'
import Router from 'vue-router'

Vue.use(Router)
Vue.use(vueDebounce, {
  listenTo: ['input', 'keyup'] // 요소에 첨부된 사용자 지정 이벤트 설정
})

export const _var = { Layout }
const isDebug = (AppOptions.instance.debug === true)
const { project } = AppOptions.instance

const { dataHubHome, dataHubLogin, dataHubRoute } = require('./dataHub/index')
const loginView = dataHubLogin
const projectRoute = dataHubRoute
const projectHome = dataHubHome

export const constantRoutes = [
  {
    name: 'ROOT',
    path: '/',
    component: Layout,
    redirect: '/home'
  },
  {
    name: 'Home',
    path: '/home',
    component: Layout,
    redirect: projectHome
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        name: 'Redirect',
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    name: 'Login',
    path: '/login',
    component: () => loginView,
    hidden: true
  },
  {
    name: 'AuthRedirect',
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  }
].filter(v => v.disable !== true)

export const asyncRoutes = [
  ...projectRoute,
].filter(v => v.disable !== true)

const createRouter = () => {
  var router = new Router({
    // mode: 'history', // history 모드를 사용하면 Responsive 을 이용한 반응형 웹 테스트 화면이 404에러로 뜬다
    scrollBehavior(to, from, savedPosition) {
      const fromHistory = Boolean(savedPosition)
      LocationHistory.instance.update({
        fromHistory, to, from
      })
      return savedPosition || { x: 0, y: 0 }
    },
    routes: [...constantRoutes],
    routes2: [...asyncRoutes]
  })

  const anonymousMode = JSON.parse(process.env.VUE_APP_ANONYMOUS_MODE || 'true')
  router.afterEach((to, from) => {
    window.$route = to
  })

  return router
}

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

