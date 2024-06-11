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
  },
  {
    path: '/ipAssignMng',
    component: Layout,
    menuIdx: 0,
    disable: false,
    redirect: '/ipAssignMng/ipBlockManagement',
    meta: {
      title: 'IP 배정관리'
    },
    children: [
      {
        path: 'ipBlockMnagement',
        component: () => import('@/views-ipms/menus/ipAssignMng/ipBlockManagement'),
        name: 'IpBlockManagement',
        meta: { title: 'IP 블록관리', affix: false }
      },
      /* 삭제
      {
        path: 'ipBlockMngPrivate',
        component: () => null,
        name: 'IpBlockMnagementPrivate',
        meta: { title: 'IP 블록관리(사설)', affix: false }
      }, */
      {
        path: 'ipAssign',
        component: () => import('@/views-ipms/menus/ipAssignMng/ipAssign'),
        name: 'IpAssign',
        meta: { title: 'IP 배정', affix: false }
      },
      {
        path: 'ipunAllocatedStatuRs',
        component: () => import('@/views-ipms/menus/ipAssignMng/ipunAllocatedStatuRs'),
        name: 'IpunAllocatedStatus',
        meta: { title: 'IP 미배정 현황', affix: false }
      },
    ]
  },
  {
    path: '/ipAllocationMng',
    component: Layout,
    disable: false,
    menuIdx: 1,
    redirect: '/ipAllocationMng/ipAllocation',
    meta: {
      title: 'IP 할당관리'
    },
    children: [
      {
        path: 'ipAllocation',
        component: () => import('@/views-ipms/menus/ipAllocationMng/ipAllocation'),
        name: 'IpAllocation',
        meta: { title: 'IP 할당', affix: false }
      },
      {
        path: 'neOssOrder',
        component: () => import('@/views-ipms/menus/ipAllocationMng/neOssOrder'),
        name: 'NeOssOrder',
        meta: { title: 'NeOSS오더', affix: false }
      },
      {
        path: 'ipSeonbeonjang',
        component: () => import('@/views-ipms/menus/ipAllocationMng/ipSeonbeonjang'),
        name: 'IpSeonbeonjang',
        meta: { title: 'IP 선번장', affix: false }
      },
      {
        path: 'vpnIpStatue',
        component: () => import('@/views-ipms/menus/ipAllocationMng/vpnIpStatue'),
        name: 'VpnIpStatue',
        meta: { title: 'VPN IP현황', affix: false }
      },
    ]
  },
  {
    path: '/ipInfoMng',
    component: Layout,
    disable: false,
    menuIdx: 2,
    redirect: '/ipInfoMng/ipInfoList',
    meta: {
      title: 'IP 정보관리'
    },
    children: [
      {
        path: 'ipInfoList',
        component: () => null,
        name: 'IpInfoList',
        meta: { title: 'IP 정보조회', affix: false }
      },
      {
        path: 'traceRoute',
        component: () => null,
        name: 'TraceRoute',
        meta: { title: 'Trace Route', affix: false }
      },
      {
        path: 'ipInfoMultiList',
        component: () => null,
        name: 'IpInfoMultiList',
        meta: { title: 'IP 정보조회(multi)', affix: false }
      },
      {
        path: 'ipHistMng',
        component: () => null,
        name: 'IpHistManagement',
        meta: { title: 'IP 이력관리', affix: false }
      },
    ]
  },
  {
    path: '/ipStatMng',
    component: Layout,
    disable: false,
    menuIdx: 3,
    redirect: '/ipStatMng/ipAddressRoutingStat',
    meta: {
      title: 'IP 통계관리'
    },
    children: [
      {
        path: 'ipAddressRoutingStat',
        component: () => null,
        name: 'IpAddressRoutingStat',
        meta: { title: 'IP주소 라우팅 비교/점검 통계 현황', affix: false }
      },
      {
        path: 'ipStatByOrgService',
        component: () => null,
        name: 'IpStatByOrgService',
        meta: { title: 'IP 조직서비스별 통계 현황', affix: false }
      },
      {
        path: 'ipStatByService',
        component: () => null,
        name: 'IpStatByService',
        meta: { title: 'IP 서비스별 통계 현황', affix: false }
      },
      {
        path: 'ipStatByBlockSize',
        component: () => null,
        name: 'IpStatByBlockSize',
        meta: { title: 'IP 블록크기별 통계 현황', affix: false }
      },
    ]
  },
  {
    path: '/dbMng',
    component: Layout,
    disable: false,
    menuIdx: 4,
    // redirect: '/ipAllocationMng/ipAllocation',
    meta: {
      title: 'DB관리'
    },
    children: [
      {
        path: 'ipAdressRoutingCompare',
        component: () => null,
        name: 'IpAdressRoutingCompare',
        meta: { title: 'IP주소 라우팅 비교/점검', affix: false }
      },
      {
        path: 'operInfoLinkMng',
        component: () => null,
        name: 'OperInfoFacilityLinkManagement',
        meta: { title: '운용정보관리(링크)', affix: false }
      },
      {
        path: 'operInfoFacilityMng',
        component: () => null,
        name: 'OperInfoFacilityManagement',
        meta: { title: '운용정보관리(시설)', affix: false }
      },
      {
        path: 'uploadMng',
        component: () => null,
        name: 'UploadManagement',
        meta: { title: '업로드 관리', affix: false }
      },
      {
        path: 'nonKtIpStatus',
        component: () => null,
        name: 'NonKtIpStatus',
        meta: { title: 'Non-KT IP 현황', affix: false }
      },
    ]
  },
  {
    path: '/board',
    component: Layout,
    disable: false,
    menuIdx: 5,
    redirect: '/board/notice',
    meta: {
      title: '게시판'
    },
    children: [
      {
        path: 'notice',
        component: () => import('@/views-ipms/menus/board/notice'),
        name: 'Notice',
        meta: { title: '공지사항', affix: false }
      },
      {
        path: 'ipAdressNodeApplyTransfer',
        component: () => null,
        name: 'IpAdressNodeApplyTransfer',
        meta: { title: 'IP주소 노드 이전 신청', affix: false }
      },
      /* 삭제
      {
        path: 'privateIpApply',
        component: () => null,
        name: 'PrivateIpApply',
        meta: { title: '사설 IP 신청', affix: false }
      }, */
      {
        path: 'ipResourceAssignReturn',
        component: () => null,
        name: 'IpResourceAssignmentApply',
        meta: { title: 'IP자원 배정 신청', affix: false }
      },
      {
        path: 'privateAssignmentrReturn',
        component: () => null,
        name: 'PrivateAssignmentrReturn',
        meta: { title: '사설AS 할당/반납 신청', affix: false }
      },
      {
        path: 'whoisInfoChange',
        component: () => null,
        name: 'WhoisInfoChange',
        meta: { title: 'Whois정보 변경 신청', affix: false }
      },
      {
        path: 'issueDataRequest',
        component: () => null,
        name: 'IssueDataRequest',
        meta: { title: '개발/오류수정/자료 요청', affix: false }
      },
      {
        path: 'authApply',
        component: () => null,
        name: 'AuthApply',
        meta: { title: '권한 신청 게시판', affix: false }
      },
    ]
  },
  {
    path: '/operInfoMng',
    component: Layout,
    disable: false,
    menuIdx: 6,
    redirect: '/operInfoMng/organizationalMng/organizationalStandardsMng',
    meta: {
      title: '운용정보관리'
    },
    children: [
      {
        path: '/operInfoMng/organizationalMng',
        component: Layout,
        meta: { title: '조직관리', affix: false },
        children: [
          {
            path: 'organizationalStandardsMng',
            component: () => import('@/views-ipms/menus/operInfoMng/OrganizationalStandardsManagement'),
            name: 'OrganizationalStandardsManagement',
            meta: { title: '조직기준 관리', affix: false }
          },
          {
            path: 'rankCodeMng',
            component: () => null,
            name: 'RankCodeManagement',
            meta: { title: '계위코드 관리', affix: false }
          },
          {
            path: 'OrgRankInfoMng',
            component: () => null,
            name: 'OrgRankInfoManagement',
            meta: { title: '조직계위 정보관리', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/productMng',
        component: Layout,
        meta: { title: '상품관리', affix: false },
        children: [
          {
            path: 'serviceNetworkMng',
            component: null,
            name: 'ServiceNetworkMnagement',
            meta: { title: '서비스망 관리', affix: false }
          },
          {
            path: 'serviceCodeMng',
            component: () => null,
            name: 'ServiceCodeManagement',
            meta: { title: '서비스 코드 관리', affix: false }
          },
          {
            path: 'productMng',
            component: () => null,
            name: 'productManagement',
            meta: { title: '상품관리', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/userMng',
        component: Layout,
        meta: { title: '사용자 관리', affix: false },
        children: [
          {
            path: 'userInfoMng',
            component: () => null,
            name: 'UserInfoManagement',
            meta: { title: '사용자 정보 관리', affix: false }
          },
          {
            path: 'userConnectStatus',
            component: () => null,
            name: 'UserConnectStatus',
            meta: { title: '사용자 접속 현황', affix: false }
          },
          {
            path: 'userAuthMng',
            component: () => null,
            name: 'UserAuthManagement',
            meta: { title: '사용자 권한 관리', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/menuMng',
        component: Layout,
        meta: { title: '메뉴 관리', affix: false },
        children: [
          {
            path: 'screenMng',
            component: () => null,
            name: 'screenManagement',
            meta: { title: '화면 관리', affix: false }
          },
          {
            path: 'menuMng',
            component: () => null,
            name: 'MenuManagement',
            meta: { title: '메뉴 관리', affix: false }
          },
          {
            path: 'menuAuthMng',
            component: () => null,
            name: 'MenuAuthManagement',
            meta: { title: '메뉴권한관리', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/thresholdManagement',
        component: Layout,
        meta: { title: '임계치관리', affix: false },
        children: [
          {
            path: 'thresholdStandardManagement',
            component: () => null,
            name: 'ThresholdStandardManagement',
            meta: { title: '임계치 기준관리', affix: false }
          },
        ]
      },
      {
        path: '/operInfoMng/privateAsMng',
        component: Layout,
        meta: { title: '사설 AS관리', affix: false },
        children: [
          {
            path: 'privateAsUseStatus',
            component: () => null,
            name: 'PrivateAsUseStatus',
            meta: { title: '사설 AS 사용현황', affix: false }
          },
        ]
      },
      {
        path: '/operInfoMng/postAuditInfoMng',
        component: Layout,
        meta: { title: '사후감사정보관리', affix: false },
        children: [
          {
            path: 'iFomsConfigInfoMng',
            component: () => null,
            name: 'IFomsConfigInfoMng',
            meta: { title: 'i-FOMS 시설 config 구성 정보관리', affix: false }
          },
        ]
      },
      {
        path: '/operInfoMng/whoIs',
        component: Layout,
        meta: { title: 'WHOIS 정보공개 관리', affix: false },
        children: [
          {
            path: 'whoisInfoOpenMng',
            component: () => null,
            name: 'WhoisInfoOpenManagement',
            meta: { title: 'WHOIS 정보공개 관리', affix: false }
          },
        ]
      },
      {
        path: '/operInfoMng/linkMng',
        component: Layout,
        meta: { title: '연동관리', affix: false },
        children: [
          {
            path: 'batchLinkInfo',
            component: () => null,
            name: 'BatchLinkInformation',
            meta: { title: '배치 연동 정보', affix: false }
          },
          {
            path: 'ZipCodeLinkMng',
            component: () => null,
            name: 'ZipCodeLinkManagement',
            meta: { title: '우편번호 연동관리', affix: false }
          },
          {
            path: 'batchLinkHistStatus',
            component: () => null,
            name: 'BatchLinkHistStatus',
            meta: { title: '배치 연동 이력현황', affix: false }
          },
          {
            path: 'neossLinkErrorStatus',
            component: () => null,
            name: 'NeossLinkErrorStatus',
            meta: { title: 'NEOSS 연동 오류 현황', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/tacsMng',
        component: Layout,
        meta: { title: 'TACS관리', affix: false },
        children: [
          {
            path: 'tacsLinkInfoMng',
            component: () => null,
            name: 'TacsLinkInfoManagement',
            meta: { title: 'TACS 연동 정보관리', affix: false }
          },
          {
            path: 'equipInfoMngByOrg',
            component: () => null,
            name: 'TacsEquipInfoManagementByOrg',
            meta: { title: '조직별 장비 정보관리', affix: false }
          },
          {
            path: 'commandInfoManagementByEuip',
            component: () => null,
            name: 'TacsCommandInfoManagementByEquip',
            meta: { title: '장비별 명령어 정보관리', affix: false }
          },
          {
            path: 'tacsLinkHistStatus',
            component: () => null,
            name: 'TacsLinkHistStatus',
            meta: { title: 'TACS 연동 이력현황', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/ipAdressRoutingCompare',
        component: Layout,
        meta: { title: 'IP주소 라우팅 비교/점검', affix: false },
        children: [
          {
            path: 'equipInfoMngByOrg',
            component: () => null,
            name: 'EquipInfoManagementByOrg',
            meta: { title: '조직별 장비 정보 관리', affix: false }
          },
          {
            path: 'commandInfoManagementByEuip',
            component: () => null,
            name: 'CommandInfoManagementByEquip',
            meta: { title: '장비별 명령어 정보관리', affix: false }
          },
          {
            path: 'wirelessIpPriorInfoMng',
            component: () => null,
            name: 'WirelessIpPriorInfoManagement',
            meta: { title: '무선IP 사전 정보관리', affix: false }
          },
          {
            path: 'routingLinkHistStatus',
            component: () => null,
            name: 'RoutingLinkHistStatus',
            meta: { title: '라우팅 연동 이력현황', affix: false }
          },
          {
            path: 'wiredIpPriorInfoMng',
            component: () => null,
            name: 'WiredIpPriorInfoManagement',
            meta: { title: '유선IP 사전 정보관리', affix: false }
          },
        ]
      },

    ]
  }
].filter(v => v.disable !== true))
