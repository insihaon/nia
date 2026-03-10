import Layout from '@/views-nia/layout'
const isMobile = window.deviceInfo.mobile

export const niaLogin = isMobile ? import('@/views-nia/login/m_index') : import('@/views-nia/login/index')
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
        component: isMobile ? () => import('@/views-nia/dashBoard/m_index') : () => import('@/views-nia/dashBoard/index'),
        name: 'NiaMain',
        meta: { title: 'DashBoard', affix: false }
      }
    ]
  },
  {
    path: '/performanceMonitoring',
    component: Layout,
    redirect: '/performanceMonitoring/trafficAnalysisInstitution',
    meta: { title: '자원 · 성능 감시' },
    children: [
      {
        path: 'trafficAnalysisInstitution',
        component: () => import('@/views-nia/pages/performanceMonitoring/trafficAnalysisInstitution.vue'),
        name: 'TrafficAnalysisInstitution',
        meta: { title: '이용기관별 트래픽 분석 Top(N)' }
      },
      {
        path: 'trafficAnalysisApp',
        component: () => import('@/views-nia/pages/performanceMonitoring/trafficAnalysisApp.vue'),
        name: 'TrafficAnalysisApp',
        meta: { title: '어플리케이션별 트래픽분석 Top(N)' }
      },
      {
        path: 'trafficAnalysisPort',
        component: () => import('@/views-nia/pages/performanceMonitoring/trafficAnalysisPort.vue'),
        name: 'TrafficAnalysisPort',
        meta: { title: '장비 포트별 in/out 트래픽' }
      },
      {
        path: 'equipmentUsage',
        component: () => import('@/views-nia/pages/performanceMonitoring/equipmentUsage.vue'),
        name: 'EquipmentUsage',
        meta: { title: '장비 사용량' },
      },
      {
        path: 'undefindedTraffic',
        component: () => import('@/views-nia/pages/performanceMonitoring/undefindedTraffic.vue'),
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
      title: '이력관리',
    },
    children: [
      {
        path: 'sopHistory',
        component: () => import('@/views-nia/pages/alarmMonitoring/sopHistory'),
        name: 'SopHistory',
        meta: { title: 'SOP 이력 조회' },
      },
      {
        path: 'disabilityStatusHistoryManagement',
        component: () => import('@/views-nia/pages/alarmMonitoring/disabilityStatusHistoryManagement'),
        name: 'DisabilityStatusHistoryManagement',
        meta: { title: '장애이력' },
      },
      {
        path: 'syslogHistoryInquiry',
        component: () => import('@/views-nia/pages/alarmMonitoring/syslogHistoryInquiry'),
        name: 'SyslogHistoryInquiry',
        meta: { title: 'Syslog 이력조회' },
      },
      {
        path: 'syslogRuleHistoryInquiry',
        component: () => import('@/views-nia/pages/alarmMonitoring/syslogRuleHistoryInquiry'),
        name: 'SyslogRuleHistoryInquiry',
        meta: { title: 'Syslog rule 이력조회' },
        modalMode: true
      },
      {
        path: 'snapshotHistory',
        component: () => import('@/views-nia/pages/dataSnapshot/snapshotHistory'),
        name: 'SnapshotHistory',
        meta: { title: '스냅샷 이력조회' },
      },
    ]
  },
  {
    path: '/operationStatusScreen',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/operationStatusScreen/operationStatusStatistics',
    meta: {
      title: '운용현황 화면',
    },
    children: [
      {
        path: 'controlScreen',
        component: isMobile ? () => import('@/views-nia/dashBoard/m_index') : () => import('@/views-nia/dashBoard/index'),
        name: 'NiaMain',
        meta: { title: '관제화면' },
      },
      {
        path: 'aiMonitoring',
        component: () => import('@/views-nia/pages/operationStatusScreen/aiMonitoring'),
        name: 'AiMonitoring',
        meta: { title: 'AI 모니터링' },
      },
      {
        path: 'serverMonitoring',
        component: () => import('@/views-nia/pages/operationStatusScreen/serverMonitoring'),
        name: 'ServerMonitoring',
        meta: { title: '서버모니터링' },
      },
      {
        path: 'operationStatusStatistics',
        component: () => import('@/views-nia/pages/operationStatusScreen/operationStatusStatistics'),
        name: 'OperationStatusStatistics',
        meta: { title: '운용 현황 통계' },
      },
      {
        path: 'selfProcessingDashboard',
        component: () => import('@/views-nia/pages/operationStatusScreen/selfProcessingDashboard'),
        name: 'SelfProcessingDashboard',
        meta: { title: '자가처리 현황 대시보드' },
      }

    ]
  },
  {
    path: '/transientOutage',
    component: Layout,
    hidden: false,
    disable: false,
    redirect: '/transientOutage/transientOutageList',
    meta: {
      title: '장애감시',
    },
    children: [
      {
        path: 'transientOutageList',
        component: () => import('@/views-nia/pages/TransientOutage/TransientOutageList'),
        name: 'TransientOutageList',
        meta: { title: '순단장애' },
      },
      {
        path: 'predictiveMaintenance',
        component: () => import('@/views-nia/pages/PredictiveMaintenance/PredictiveMaintenanceTicket'),
        name: 'PredictiveMaintenance',
        meta: { title: '예지보전' },
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
      title: '자원 · 구성 관리',
    },
    children: [
      {
        path: 'nodeInquiryList',
        component: () => import('@/views-nia/pages/nodeManagement/nodeInquiryList'),
        name: 'NodeInquiryList',
        meta: { title: '노드 리스트 조회' },
      },
      {
        path: 'linkInquiryInfoList',
        component: () => import('@/views-nia/pages/linkManagement/linkInquiryInfoList'),
        name: 'LinkInquiryInfoList',
        meta: { title: '링크 정보 조회' },
      },
      {
        path: 'agencyInquiryList',
        component: () => import('@/views-nia/pages/agency/agencyInquiryList'),
        name: 'AgencyInquiryList',
        meta: { title: '이용기관 조회' },
      },
    ]
  },
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
        component: () => import('@/views-nia/pages/manager/authSettings'),
        name: 'AuthSettings',
        meta: { title: '관리자 권한 설정' },
      },
      {
        path: 'profileInquiry',
        component: () => import('@/views-nia/pages/profile/profileInquiry'),
        name: 'ProfileInquiry',
        meta: { title: '자동조치 프로파일 관리' },
      },
    ]
  },
].filter(v => v.disable !== true))
