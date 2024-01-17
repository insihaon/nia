import Layout from '@/views-dataHub/layout'

export const dataHubLogin = import('@/views-dataHub/login/index')
export const dataHubHome = '/dh/index'

export const dataHubRoute = Object.freeze([

  {
    path: '/dh',
    component: Layout,
    hidden: true,
    disable: false,
    redirect: '/dh/index',
    meta: {
      title: '메인'
    },
    children: [
      {
        path: 'index',
        component: () => import('@/views-dataHub/index'),
        name: 'datahubMain',
        meta: { title: '', affix: false }
      },
    ]
  },
  {
    path: '/apiManagement',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/apiManagement',
    meta: {
      title: 'API정보',
    },
    children: [
      {
        path: 'apiUseAuthApply',
        component: () => import('@/views-dataHub/apiManagement/apiUseAuthApply'),
        name: 'ApiUseAuthApply',
        meta: { title: 'API 사용 신청' }
      },
      {
        path: 'apiInfoHist',
        component: () => import('@/views-dataHub/apiManagement/apiInfoHistManagement'),
        name: 'ApiInfoHistManagement',
        meta: { title: 'API 성공이력' }
      },
      {
        path: 'apiAlaramHist',
        component: () => import('@/views-dataHub/apiManagement/apiAlaramHistManagement'),
        name: 'ApiAlaramHistManagement',
        meta: { title: 'API 오류이력' }
      },
      {
        path: 'apiUsageStatus',
        component: () => import('@/views-dataHub/administrator/apiManagement/apiUsageStatus'),
        name: 'ApiUsageStatus',
        meta: { title: 'API 신청내역', grant: ['ROLE_ADMIN'] },
      },
      {
        path: 'apiStatistics',
        component: () => import('@/views-dataHub/apiManagement/apiStatistics'),
        name: 'ApiStatistics',
        meta: { title: 'API 통계' }
      },
      {
        path: 'authManagement',
        component: () => import('@/views-dataHub/apiManagement/authManagement'),
        name: 'AuthManagement',
        meta: { title: 'API 권한 관리', grant: ['ROLE_ADMIN'] },
      },
      {
        path: 'linkSystemManagement',
        component: () => import('@/views-dataHub/administrator/linkSystemManagement'),
        name: 'LinkSystemManagement',
        meta: { title: '연동 시스템 관리', grant: ['ROLE_ADMIN'] },
      },
    ]
  },
  {
    path: '/metaData',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/metaData',
    meta: {
      title: '카탈로그',
    },
    children: [
      {
        path: 'metaData',
        component: () => import('@/views-dataHub/metaData/metaData'),
        name: 'MetaData',
        meta: { title: '메타데이터 조회' },
      },
      {
        path: 'metaDataManagement',
        component: () => import('@/views-dataHub/metaData/metaDataManagement'),
        name: 'MetaDataManagement',
        meta: { title: '메타데이터 관리', grant: ['ROLE_ADMIN'] },
      },
      {
        path: 'selectDataSet',
        component: () => import('@/views-dataHub/metaData/selectDataSet'),
        name: 'SelectDataSet',
        meta: { title: 'API 데이터 조회' }
      },
      {
        path: 'createDataSet',
        component: () => import('@/views-dataHub/metaData/createDataSet'),
        name: 'CreateDataSet',
        meta: { title: 'API 생성요청' }
      },
      {
        path: 'dataSetManagement',
        component: () => import('@/views-dataHub/metaData/dataSetManagement'),
        name: 'DataSetManagement',
        meta: { title: 'API 생성요청 관리', grant: ['ROLE_ADMIN'] },

      },
    ]
  },
  {
    path: '/dashBoard',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/dashBoard',
    meta: {
      title: '운용현황',
    },
    children: [
      {
        path: 'apiProvidingStatus',
        component: () => import('@/views-dataHub/dashboard/integratedOperationStatus/apiProvidingStatus'),
        name: 'ApiProvidingStatus',
        meta: { title: 'API 사용 현황' },
      },
      {
        path: 'collectionAnalysis',
        component: () => import('@/views-dataHub/dashboard/integratedOperationStatus/collectionAnalysis'),
        name: 'CollectionAnalysis',
        meta: { title: '데이터 수집 현황' },
      },
      {
        path: 'nodeInfoManagement',
        component: () => import('@/views-dataHub/administrator/apiManagement/nodeInfoManagement'),
        name: 'NodeInfoManagement',
        meta: { title: '서버 노드 현황' }
      },
      {
        path: 'openSourceDashboard',
        component: () => import('@/views-dataHub/dashboard/integratedOperationStatus/openSourceDashboard'),
        name: 'LoadingAnalysis',
        meta: { title: '어플리케이션 현황' },
      },
      // 어플리케이션 현황
    ]
  },
  {
    path: '/manager',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/manager',
    meta: {
      title: '영향회선',
    },
    children: [
      {
        path: 'influenceCircit',
        component: () => import('@/views-dataHub/dashboard/influenceCircit/influenceCircit'),
        name: 'influenceCircit',
        meta: { title: '시설분석' },
      }
    ]
  }
].filter(v => v.disable !== true))
