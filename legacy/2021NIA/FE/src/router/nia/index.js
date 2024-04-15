import Layout from '@/views-nia/layout'

export const niaLogin = import('@/views-nia/login/index')
export const niaHome = '/dashBoard/index'
export const niaRoute = Object.freeze([
  {
    path: '/dashBoard',
    component: Layout,
    hidden: true,
    disable: false,
    redirect: '/dashBoard/index',
    meta: {
      title: '대시보드',
    },
    children: [
      {
        path: 'index',
        component: () => import('@/views-nia/dashBoard/index'),
        name: 'NiaMain',
        meta: { title: 'DashBoard', affix: false }
      }
    ]
  },
  {
    path: '/performanceMonitoring',
    component: Layout,
    redirect: '/performanceMonitoring/trafficAnalysisInstitution',
    meta: { title: '성능감시' },
    children: [
      {
        path: 'trafficAnalysisInstitution',
        component: () => import('@/views-nia/performanceMonitoring/trafficAnalysisInstitution'),
        name: 'TrafficAnalysisInstitution',
        meta: { title: '이용기관별 트래픽 분석' }
      },
      {
        path: 'trafficAnalysisApp',
        component: () => import('@/views-nia/performanceMonitoring/trafficAnalysisApp'),
        name: 'TrafficAnalysisApp',
        meta: { title: '어플리케이션별 트래픽분석' }
      },
      {
        path: 'trafficAnalysisPort',
        component: () => import('@/views-nia/performanceMonitoring/trafficAnalysisPort'),
        name: 'TrafficAnalysisPort',
        meta: { title: '장비 포트별 in/out 트래픽' }
      },
      {
        path: 'equipmentUsage',
        component: () => import('@/views-nia/performanceMonitoring/equipmentUsage'),
        name: 'EquipmentUsage',
        meta: { title: '장비 사용량' },
      },
      {
        path: 'undefindedTraffic',
        component: () => import('@/views-nia/performanceMonitoring/undefindedTraffic'),
        name: 'UndefindedTraffic',
        meta: { title: '미확인 트래픽 정의' }
      },
    ]
  },
  {
    path: '/alarmMonitoring',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/alarmMonitoring/sopHistory',
    meta: {
      title: '장애감시',
    },
    children: [
      {
        path: 'sopHistory',
        component: () => import('@/views-nia/alarmMonitoring/sopHistory'),
        name: 'SopHistory',
        meta: { title: 'SOP 이력 조회' },
      },
      {
        path: 'disabilityStatusHistoryManagement',
        component: () => import('@/views-nia/alarmMonitoring/disabilityStatusHistoryManagement'),
        name: 'DisabilityStatusHistoryManagement',
        meta: { title: '장애현황 및 이력관리' },
      },
      {
        path: 'syslogHistoryInquiry',
        component: () => import('@/views-nia/alarmMonitoring/syslogHistoryInquiry'),
        name: 'SyslogHistoryInquiry',
        meta: { title: 'Syslog 이력조회' },
      },
      {
        path: 'syslogRuleHistoryInquiry',
        component: () => import('@/views-nia/alarmMonitoring/syslogRuleHistoryInquiry'),
        name: 'SyslogRuleHistoryInquiry',
        meta: { title: 'Syslog rule 이력조회' },
        modalMode: true
      },
    ]
  },
  {
    path: '/operationStatusScreen',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/operationStatusScreen/controlScreen',
    meta: {
      title: '운용현황 화면',
    },
    children: [
      {
        path: 'controlScreen',
        component: () => import('@/views-nia/dashBoard/index'),
        name: 'NiaMain',
        meta: { title: '관제화면' },
      },
      {
        path: 'aiMonitoring',
        component: () => import('@/views-nia/operationStatusScreen/aiMonitoring'),
        name: 'AiMonitoring',
        meta: { title: 'AI 모니터링' },
      },
      {
        path: 'serverMonitoring',
        component: () => import('@/views-nia/operationStatusScreen/serverMonitoring'),
        name: 'ServerMonitoring',
        meta: { title: '서버모니터링' },
      },
      {
        path: 'operationStatusStatistics',
        component: () => import('@/views-nia/operationStatusScreen/operationStatusStatistics'),
        name: 'OperationStatusStatistics',
        meta: { title: '운용 현황 통계' },
      },
      {
        path: 'selfProcessingDashboard',
        component: () => import('@/views-nia/operationStatusScreen/selfProcessingDashboard'),
        name: 'SelfProcessingDashboard',
        meta: { title: '자가처리 현황 대시보드' },
      }

    ]
  },
  {
    path: '/profile',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/profile/profileInquiry',
    meta: {
      title: '프로파일',
    },
    children: [
      {
        path: 'profileInquiry',
        component: () => import('@/views-nia/profile/profileInquiry'),
        name: 'ProfileInquiry',
        meta: { title: '조치 프로파일 조회' },
      },
    ]
  },
  {
    path: '/dataSnapshot',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/dataSnapshot/snapshotHistory',
    meta: {
      title: '데이터스냅샷',
    },
    children: [
      {
        path: 'snapshotHistory',
        component: () => import('@/views-nia/dataSnapshot/snapshotHistory'),
        name: 'SnapshotHistory',
        meta: { title: '스냅샷 이력조회' },
      },
    ]
  },
  // {
  //   path: '/userManagement',
  //   component: Layout,
  //   hidden: false,
  //   disable: false,
  //   redirect: '/userManagement/ModaluserSettings',
  //   meta: {
  //     title: '사용자 관리',
  //   },
  //   children: [
  //     {
  //       path: 'userSettings',
  //       component: () => import('@/views-nia/userManagement/ModaluserSettings'),
  //       name: 'UserSettings',
  //       meta: { title: '사용자 설정' },
  //       modalMode: true
  //     },
  //   ]
  // },
  {
    path: '/manager',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/manager/authSettings',
    meta: {
      title: '관리자',
    },
    children: [
      {
        path: 'authSettings',
        component: () => import('@/views-nia/manager/authSettings'),
        name: 'AuthSettings',
        meta: { title: '관리자 권한 설정' },
      },
    ]
  },
  {
    path: '/nodeManagement',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/nodeManagement/nodeInquiryList',
    meta: {
      title: '노드관리',
    },
    children: [
      {
        path: 'nodeInquiryList',
        component: () => import('@/views-nia/nodeManagement/nodeInquiryList'),
        name: 'NodeInquiryList',
        meta: { title: '노드 리스트 조회' },
      },
    ]
  },
  {
    path: '/linkManagement',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/linkManagement/linkInquiryInfoList',
    meta: {
      title: '링크관리',
    },
    children: [
      {
        path: 'linkInquiryInfoList',
        component: () => import('@/views-nia/linkManagement/linkInquiryInfoList'),
        name: 'LinkInquiryInfoList',
        meta: { title: '링크 정보 조회' },
      },
    ]
  },
  {
    path: '/agency',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/agency/agencyInquiryList',
    meta: {
      title: '이용기관',
    },
    children: [
      {
        path: 'agencyInquiryList',
        component: () => import('@/views-nia/agency/agencyInquiryList'),
        name: 'AgencyInquiryList',
        meta: { title: '이용기관 조회' },
      },
    ]
  },
].filter(v => v.disable !== true))
