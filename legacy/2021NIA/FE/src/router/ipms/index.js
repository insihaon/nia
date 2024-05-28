import Layout from '@/views-ipms/layout'

export const ipmsLogin = import('@/views-ipms/login/index')
export const ipmsHome = '/ipms/index'

export const ipmsRoute = Object.freeze([
  {
    path: '/ipms',
    component: Layout,
    hidden: true,
    disable: false,
    redirect: '/ipms/index',
    meta: {
      title: 'IPMS'
    },
    children: [
      {
        path: 'index',
        component: () => import('@/views-ipms/index'),
        name: 'IpmsMain',
        meta: { title: '', affix: false }
      },
    ]
  }
].filter(v => v.disable !== true))
