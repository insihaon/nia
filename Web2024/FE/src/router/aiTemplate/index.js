import Layout from '@/views-aiTemplate/layout'
import AppMain from '@/layout/components/aiTemplate/AppMain'
export const aiTemplateLogin = import('@/views-aiTemplate/login/index')
export const aiTemplateHome = '/at/index'

// routing example
export const aiTemplateRoute = Object.freeze([
  {
    path: '/at',
    component: Layout,
    hidden: true,
    disable: false,
    redirect: '/at/index',
    meta: { title: 'Main' },
    children: [
      {
        path: 'index',
        component: () => import('@/views-aiTemplate/index'),
        name: 'aiTemplate',
        meta: { title: 'MainPage', affix: false }
      },
    ]
  },
  {
    path: '/menu1',
    redirect: '/menu1/onedeps/one',
    component: Layout,
    meta: { title: 'MENU 1', icon: 'el-icon-menu' },
    children: [
      {
        path: '/menu1/onedeps',
        redirect: '/menu1/onedeps/one',
        component: AppMain,
        meta: { title: 'MENU 1-1' },
        children: [
          {
            path: 'one',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiUseAuthApply',
            meta: { title: 'MENU 1-1-1', affix: true }
          },
          {
            path: 'two',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiInfoHistManagement',
            meta: { title: 'MENU 1-1-2' }
          },
        ]
      },
      {
        path: '/menu1/twodeps',
        redirect: '/menu1/twodeps/one',
        name: 'ApiInfoHistManagement',
        component: AppMain,
        meta: { title: 'MENU 1-2' },
        children: [
          {
            path: 'one',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiUseAuthApply',
            meta: { title: 'MENU 1-2-1' }
          },
          {
            path: 'two',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiInfoHistManagement',
            meta: { title: 'MENU 1-2-2' }
          },
        ]
      },
    ]
  },
  {
    path: '/menu2',
    redirect: '/menu2/onedeps/one',
    component: Layout,
    meta: { title: 'MENU 2', icon: 'el-icon-s-comment' },
    children: [
      {
        path: '/menu2/onedeps',
        redirect: '/menu2/onedeps/one',
        component: AppMain,
        meta: { title: 'MENU 2-1' },
        children: [
          {
            path: 'one',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiUseAuthApply',
            meta: { title: 'MENU 2-1-1' }
          },
          {
            path: 'two',
            component: () => import('@/views-aiTemplate/login/index'),
            name: 'ApiInfoHistManagement',
            meta: { title: 'MENU 2-1-2' }
          },
        ]
      },
      {
        path: '/menu2/twodeps',
        component: () => import('@/views-aiTemplate/login/index'),
        name: 'MetaDataManagement',
        meta: { title: 'MENU 2-1' },
      },
    ]
  },
  {
    path: '/menu3',
    component: Layout,
    hidden: true,
    disable: false,
    redirect: '/menu3/index',
    meta: { title: 'MENU 3' },
    children: [
      {
        path: 'index',
        component: () => import('@/views-aiTemplate/index'),
        name: 'aiTemplate',
        meta: { title: 'MainPage', affix: false }
      },
    ]
  },
].filter(v => v.disable !== true))
