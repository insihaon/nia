import AppMain from '@/layout/components/ipms/AppMain'
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
        path: 'ipBlockManagement',
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
        path: 'ipunAllocatedStatus',
        component: () => import('@/views-ipms/menus/ipAssignMng/ipunAllocatedStatus'),
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
        component: () => import('@/views-ipms/menus/ipInfoMng/ipInfoList'),
        name: 'IpInfoList',
        meta: { title: 'IP 정보조회', affix: false }
      },
      {
        path: 'traceRoute',
        component: () => import('@/views-ipms/menus/ipInfoMng/traceRoute'),
        name: 'TraceRoute',
        meta: { title: 'Trace Route', affix: false }
      },
      // {
      //   path: 'ipInfoMultiList',
      //   component: () => import('@/views-ipms/menus/ipInfoMng/ipInfoMultiList'),
      //   name: 'IpInfoMultiList',
      //   meta: { title: 'IP 정보조회(멀티)', affix: false }
      // },
      {
        path: 'ipHistMng',
        component: () => import('@/views-ipms/menus/ipInfoMng/ipHistManagement'),
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
        component: () => import('@/views-ipms/menus/ipStatMng/ipAddressRoutingStat'),
        name: 'IpAddressRoutingStat',
        meta: { title: 'IP주소 라우팅 비교/점검 통계 현황', affix: false }
      },
      {
        path: 'ipStatByOrgService',
        component: () => import('@/views-ipms/menus/ipStatMng/ipStatByOrgService'),
        name: 'IpStatByOrgService',
        meta: { title: 'IP 조직서비스별 통계 현황', affix: false }
      },
      {
        path: 'ipStatByService',
        component: () => import('@/views-ipms/menus/ipStatMng/ipStatByService'),
        name: 'IpStatByService',
        meta: { title: 'IP 서비스별 통계 현황', affix: false }
      },
      {
        path: 'ipStatByBlockSize',
        component: () => import('@/views-ipms/menus/ipStatMng/ipStatByBlockSize'),
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
    redirect: '/dbMng/ipAdressRoutingCompare',
    meta: {
      title: 'DB관리'
    },
    children: [
      {
        path: 'ipAdressRoutingCompare',
        component: () => import('@/views-ipms/menus/dbMng/ipAdressRoutingCompare'),
        name: 'IpAdressRoutingCompare',
        meta: { title: 'IP주소 라우팅 비교/점검', affix: false }
      },
      {
        path: 'operInfoLinkMng',
        component: () => import('@/views-ipms/menus/dbMng/operInfoLinkManagement'),
        name: 'OperInfoLinkManagement',
        meta: { title: '운용정보관리(링크)', affix: false }
      },
      {
        path: 'operInfoFacilityMng',
        component: () => import('@/views-ipms/menus/dbMng/operInfoFacilityManagement'),
        name: 'OperInfoFacilityManagement',
        meta: { title: '운용정보관리(시설)', affix: false }
      },
      {
        path: 'uploadMng',
        component: () => import('@/views-ipms/menus/dbMng/uploadManagement'),
        name: 'UploadManagement',
        meta: { title: '업로드 관리', affix: false }
      },
      {
        path: 'nonKtIpStatus',
        component: () => import('@/views-ipms/menus/dbMng/nonKtIpStatus'),
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
        component: () => import('@/views-ipms/menus/board/ipAdressNodeApplyTransfer'),
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
        path: 'ipResourceAssignmentApply',
        component: () => import('@/views-ipms/menus/board/ipResourceAssignmentApply'),
        name: 'IpResourceAssignmentApply',
        meta: { title: 'IP자원 배정 신청', affix: false }
      },
      {
        path: 'privateAssignmentrReturn',
        component: () => import('@/views-ipms/menus/board/privateAssignmentrReturn'),
        name: 'PrivateAssignmentrReturn',
        meta: { title: '사설AS 할당/반납 신청', affix: false }
      },
      {
        path: 'whoisInfoChange',
        component: () => import('@/views-ipms/menus/board/whoisInfoChange'),
        name: 'WhoisInfoChange',
        meta: { title: 'Whois정보 변경 신청', affix: false }
      },
      {
        path: 'issueDataRequest',
        component: () => import('@/views-ipms/menus/board/issueDataRequest'),
        name: 'IssueDataRequest',
        meta: { title: '개발/오류수정/자료 요청', affix: false }
      },
      {
        path: 'authApply',
        component: () => import('@/views-ipms/menus/board/authApply'),
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
    redirect: '/operInfoMng/organizationalMng/organizationalStandardsManagement',
    meta: {
      title: '운용정보관리'
    },
    children: [
      {
        path: '/organizationalMng',
        component: AppMain,
        meta: { title: '조직관리', affix: false },
        children: [
          {
            path: 'organizationalStandardsManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/organizationalMng/organizationalStandardsManagement'),
            name: 'OrganizationalStandardsManagement',
            meta: { title: '조직기준 관리', affix: false }
          },
          {
            path: 'rankCodeManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/organizationalMng/rankCodeManagement'),
            name: 'RankCodeManagement',
            meta: { title: '계위코드 관리', affix: false }
          },
          {
            path: 'orgRankInfoManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/organizationalMng/orgRankInfoManagement'),
            name: 'OrgRankInfoManagement',
            meta: { title: '조직계위 정보관리', affix: false }
          }
        ]
      },
      // {
      //   path: '/operInfoMng/productMng',
      //   component: AppMain,
      //   meta: { title: '상품관리', affix: false },
      //   children: [
      //     {
      //       path: 'serviceNetworkManagement',
      //       component: () => import('@/views-ipms/menus/operInfoMng/productMng/serviceNetworkManagement'),
      //       name: 'ServiceNetworkManagement',
      //       meta: { title: '서비스망 관리', affix: false }
      //     },
      //     {
      //       path: 'serviceCodeManagement',
      //       component: () => import('@/views-ipms/menus/operInfoMng/productMng/serviceCodeManagement'),
      //       name: 'ServiceCodeManagement',
      //       meta: { title: '서비스 코드 관리', affix: false }
      //     },
      //     {
      //       path: 'productManagement',
      //       component: () => import('@/views-ipms/menus/operInfoMng/productMng/productManagement'),
      //       name: 'ProductManagement',
      //       meta: { title: '상품관리', affix: false }
      //     }
      //   ]
      // },
      {
        path: '/operInfoMng/userMng',
        component: AppMain,
        meta: { title: '사용자 관리', affix: false },
        children: [
          {
            path: 'userInfoManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/userMng/userInfoManagement'),
            name: 'UserInfoManagement',
            meta: { title: '사용자 정보 관리', affix: false }
          },
          {
            path: 'userConnectStatus',
            component: () => import('@/views-ipms/menus/operInfoMng/userMng/userConnectStatus'),
            name: 'UserConnectStatus',
            meta: { title: '사용자 접속 현황', affix: false }
          },
          {
            path: 'userAuthManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/userMng/userAuthManagement'),
            name: 'UserAuthManagement',
            meta: { title: '사용자 권한 관리', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/menuMng',
        component: AppMain,
        meta: { title: '메뉴 관리', affix: false },
        children: [
          {
            path: 'screenManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/menuMng/screenManagement'),
            name: 'ScreenManagement',
            meta: { title: '화면 관리', affix: false }
          },
          {
            path: 'menuManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/menuMng/menuManagement'),
            name: 'MenuManagement',
            meta: { title: '메뉴 관리', affix: false }
          },
          {
            path: 'menuAuthManagement',
            component: () => import('@/views-ipms/menus/operInfoMng/menuMng/menuAuthManagement'),
            name: 'MenuAuthManagement',
            meta: { title: '메뉴권한관리', affix: false }
          }
        ]
      },
      // {
      //   path: '/operInfoMng/thresholdManagement',
      //   component: AppMain,
      //   meta: { title: '임계치관리', affix: false },
      //   children: [
      //     {
      //       path: 'thresholdStandardManagement',
      //       component: () => import('@/views-ipms/menus/operInfoMng/thresholdManagement/thresholdStandardManagement'),
      //       name: 'ThresholdStandardManagement',
      //       meta: { title: '임계치 기준관리', affix: false }
      //     },
      //   ]
      // },
      // {
      //   path: '/operInfoMng/privateAsMng',
      //   component: AppMain,
      //   meta: { title: '사설 AS관리', affix: false },
      //   children: [
      //     {
      //       path: 'privateAsUseStatus',
      //       component: () => import('@/views-ipms/menus/operInfoMng/privateAsMng/privateAsUseStatus'),
      //       name: 'PrivateAsUseStatus',
      //       meta: { title: '사설 AS 사용현황', affix: false }
      //     },
      //   ]
      // },
      // {
      //   path: '/operInfoMng/postAuditInfoMng',
      //   component: AppMain,
      //   meta: { title: '사후감사정보관리', affix: false },
      //   children: [
      //     {
      //       path: 'iFomsConfigInfoMng',
      //       component: () => import('@/views-ipms/menus/operInfoMng/postAuditInfoMng/iFomsConfigInfoMng'),
      //       name: 'IFomsConfigInfoMng',
      //       meta: { title: 'i-FOMS 시설 config 구성 정보관리', affix: false }
      //     },
      //   ]
      // },
      {
        path: '/operInfoMng/whoIs',
        component: AppMain,
        meta: { title: 'WHOIS 정보공개 관리', affix: false },
        children: [
          {
            path: 'whoisInfoOpenMng',
            component: () => import('@/views-ipms/menus/operInfoMng/whoIs/whoisInfoOpenMng'),
            name: 'WhoisInfoOpenManagement',
            meta: { title: 'WHOIS 정보공개 관리', affix: false }
          },
        ]
      },
      {
        path: '/operInfoMng/linkMng',
        component: AppMain,
        meta: { title: '연동관리', affix: false },
        children: [
          {
            path: 'batchLinkInfo',
            component: () => import('@/views-ipms/menus/operInfoMng/linkMng/batchLinkInfo'),
            name: 'BatchLinkInformation',
            meta: { title: '배치 연동 정보', affix: false }
          },
          {
            path: 'zipCodeLinkMng',
            component: () => import('@/views-ipms/menus/operInfoMng/linkMng/zipCodeLinkMng'),
            name: 'ZipCodeLinkManagement',
            meta: { title: '우편번호 연동관리', affix: false }
          },
          {
            path: 'batchLinkHistStatus',
            component: () => import('@/views-ipms/menus/operInfoMng/linkMng/batchLinkHistStatus'),
            name: 'BatchLinkHistStatus',
            meta: { title: '배치 연동 이력현황', affix: false }
          },
          {
            path: 'neossLinkErrorStatus',
            component: () => import('@/views-ipms/menus/operInfoMng/linkMng/neossLinkErrorStatus'),
            name: 'NeossLinkErrorStatus',
            meta: { title: 'NEOSS 연동 오류 현황', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/tacsMng',
        component: AppMain,
        meta: { title: 'TACS관리', affix: false },
        children: [
          {
            path: 'tacsLinkInfoMng',
            component: () => import('@/views-ipms/menus/operInfoMng/tacsMng/tacsLinkInfoMng'),
            name: 'TacsLinkInfoManagement',
            meta: { title: 'TACS 연동 정보관리', affix: false }
          },
          {
            path: 'equipInfoMngByOrg',
            component: () => import('@/views-ipms/menus/operInfoMng/tacsMng/tacsEquipInfoMngByOrg'),
            name: 'TacsEquipInfoManagementByOrg',
            meta: { title: '조직별 장비 정보관리', affix: false }
          },
          {
            path: 'commandInfoManagementByEuip',
            component: () => import('@/views-ipms/menus/operInfoMng/tacsMng/tacsCommandInfoManagementByEuip'),
            name: 'TacsCommandInfoManagementByEquip',
            meta: { title: '장비별 명령어 정보관리', affix: false }
          },
          {
            path: 'tacsLinkHistStatus',
            component: () => import('@/views-ipms/menus/operInfoMng/tacsMng/tacsLinkHistStatus'),
            name: 'TacsLinkHistStatus',
            meta: { title: 'TACS 연동 이력현황', affix: false }
          }
        ]
      },
      {
        path: '/operInfoMng/ipAdressRoutingCompare',
        component: AppMain,
        meta: { title: 'IP주소 라우팅 비교/점검', affix: false },
        children: [
          {
            path: 'equipInfoMngByOrg',
            component: () => import('@/views-ipms/menus/operInfoMng/ipAdressRoutingCompare/equipInfoMngByOrg'),
            name: 'EquipInfoManagementByOrg',
            meta: { title: '조직별 장비 정보관리', subTitle: '(IP주소 라우팅 비교/점검)', affix: false }
          },
          {
            path: 'commandInfoManagementByEuip',
            component: () => import('@/views-ipms/menus/operInfoMng/ipAdressRoutingCompare/commandInfoManagementByEuip'),
            name: 'CommandInfoManagementByEquip',
            meta: { title: '장비별 명령어 정보관리', subTitle: '(IP주소 라우팅 비교/점검)', affix: false }
          },
          {
            path: 'wirelessIpPriorInfoMng',
            component: () => import('@/views-ipms/menus/operInfoMng/ipAdressRoutingCompare/wirelessIpPriorInfoMng'),
            name: 'WirelessIpPriorInfoManagement',
            meta: { title: '무선IP 사전 정보관리', affix: false }
          },
          {
            path: 'routingLinkHistStatus',
            component: () => import('@/views-ipms/menus/operInfoMng/ipAdressRoutingCompare/routingLinkHistStatus'),
            name: 'RoutingLinkHistStatus',
            meta: { title: '라우팅 연동 이력현황', affix: false }
          },
          {
            path: 'wiredIpPriorInfoMng',
            component: () => import('@/views-ipms/menus/operInfoMng/ipAdressRoutingCompare/wiredIpPriorInfoMng'),
            name: 'WiredIpPriorInfoManagement',
            meta: { title: '유선IP 사전 정보관리', affix: false }
          },
        ]
      },

    ]
  }
].filter(v => v.disable !== true))
