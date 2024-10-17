/* eslint-disable */
import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/',
    component: ()=>import('@/pages/Login'),
    name: 'Login',
    hidden: true
  },
  {
    path: '/Home',
    component: Layout,
    redirect: '/Main',
    children: [{
      path: '/Main',
      name: 'Main',
      component: () => import('@/pages/Main')
    }]
  },
  {
    path: '/page_1',
    component: Layout,
    redirect: '/Page_1',
    children: [{
      path: '/Page_1',
      name: 'Page_1',
      component: () => import('@/pages/Page_1')
    }]
  },
  {
    path: '/page_popup',
    component: Layout,
    redirect: '/Page_popup',
    children: [{
      path: '/Page_popup',
      name: 'Page_popup',
      component: () => import('@/pages/Page_popup')
    }]
  },
  
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  routes: constantRoutes
})

const router = createRouter()
export default router
