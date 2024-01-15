// import { getApiData } from '@/utils'
import http from '@/min/http'
import { AppOptions } from '@/class/appOptions'
// import { apiGetObjectFromJsonFile2 } from '@/api/json-file'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

export function apiKong(uri, params = {}) {
  return http({
    url: `/kong/${uri}`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}
window.kong = async function (uri, params) {
  const res = await apiKong(uri, params)
  console.log({ ...res })
}
export function apiEcho(uri, params = {}) {
  return http({
    url: `/dh/api-test${uri}`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}

export function apiReprocessingAPIData(params = {}) {
  return http({
    url: `/dh/REQ_RESEND`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}

export function apiSelectBlackAuthorityByMenu(params = {}) {
  return http({
    url: '/dh/SELECT_BLACK_AUTHORITY_BY_MENU',
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}

export function apiDataListTest(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_LIST',
    data: params
  })
}
export function apiTableListTest(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TABLE_LIST',
    data: params
  })
}

export function apiSelectInfoList(params = {}) { // API 정보 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_INFO_LIST',
    data: params
  })
}

export function apiSelectHistList(params = {}) { // API 연동/오류 이력 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_HIST_LIST',
    data: params
  })
}

export function apiUpdateApiRetryStatus(params = {}) { // API 연동 오류 재처리
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_API_PROC_RETRY_STATUS',
    data: params
  })
}

export function apiUpdateApiRetryProc(params = {}) { // API 연동 오류 재처리 서비스
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_API_RETRY_PROC',
    data: params
  })
}

export function apiSelectSuccessCountList(params = {}) { // API 연동 통계 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_SUCCESS_COUNT_LIST',
    data: params
  })
}

export function apiSelectFailCountList(params = {}) { // API 연동 통계 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_FAIL_COUNT_LIST',
    data: params
  })
}

export function apiSelectAuthHistList(params = {}) { // API 서비스 사용 권한 신청 이력
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_HIST_AUTH_LIST',
    data: params
  })
}

// export function apiInsertApiAuth(params = {}) { // API 서비스 사용 권한 신청
//   return http({
//     url: '/selectOne',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'INSERT_API_AUTH_HIST',
//     data: params
//   })
// }
export function apiInsertApiAuthProc(params = {}) { // API 서비스 사용 권한 신청
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_API_AUTH_HIST_PROC',
    data: params
  })
}

export function apiUpdateApiAuth(params = {}) { // API 서비스 권한 수정(관리자)
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_API_AUTH_HIST',
    data: params
  })
}

export function apiUpdateApiAuthProc(params = {}) { // API 서비스 권한 수정(관리자)
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_API_AUTH_HIST_PROC',
    data: params
  })
}

export function apiDeleteAuthHistList(params = {}) { // API 서비스 사용 권한 취소(사용자)
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_API_AUTH_HIST',
    data: params
  })
}

export function apiSelectDataCatalogList(params = {}) { // 데이터셋 카탈로그 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_LIST',
    data: params
  })
}
export function apiSelectDataSetHistList(params = {}) { // 데이터셋 요청 이력 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_HIST_LIST',
    data: params
  })
}

export function apiSelectDataSetApiList(params = {}) { // 데이터셋 요청 이력 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_DATA_SET_LIST',
    data: params
  })
}

// export function apiUpdateDataSetHistList(params = {}) { // 데이터셋 요청 이력 상태 수정(관리자 권한 부여)
//   return http({
//     url: '/modify',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'UPDATE_DATA_SET_HIST_LIST',
//     data: params
//   })
// }
export function apiUpdateDataSetHistListProc(params = {}) { // 데이터셋 요청 이력 상태 수정(관리자 권한 부여)
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_DATA_SET_HIST_LIST_PROC',
    data: params
  })
}

export function apiDeleteDataSetHistList(params = {}) { // 데이터셋 요청 이력 삭제(사용자 기능)
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_DATA_SET_HIST_LIST',
    data: params
  })
}

export function apiSelectDataSetColumnList(params = {}) { // 데이터셋 요청 테이블, 컬럼 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_COL_LIST',
    data: params
  })
}
export function apiSelectApiTableList(params = {}) { // api 테이블 정보 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_TABLE_LIST',
    data: params
  })
}

export function apiSelectApiList(params = {}) { // api 정보 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_LIST',
    data: params
  })
}

export function apiDeleteApiColOne(params = {}) { // 데이터셋 요청 테이블 정보 삭제
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_API_TABLE_ONE',
    data: params
  })
}

export function apiSelectDataSetPopOver(params = {}) { // 데이터셋 요청 정보 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_POP_OVER_DATA',
    data: params
  })
}

export function apiInsertDataCatalogList(params = {}) { // 데이터셋 카탈로그 테이블 등록
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_DATA_SET_LIST',
    data: params
  })
}

export function apiUpdateDataCatalogList(params = {}) { // 데이터셋 카탈로그 테이블 수정
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_DATA_SET_LIST',
    data: params
  })
}

export function apiDeleteDataCatalogList(params = {}) { // 데이터셋 카탈로그 테이블 삭제
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_DATA_SET_LIST',
    data: params
  })
}

export function apiSelectDataTableList(params = {}) { // 데이터셋 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_TABLE_LIST',
    data: params
  })
}

export function apiSelectLinkSystemList(params = {}) { // 연동 시스템 관리 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_SYSTEM_LIST',
    data: params
  })
}

// export function apiInsertLinkSystemList(params = {}) { // 연동 시스템 관리 테이블 등록
//   return http({
//     url: '/selectOne',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'INSERT_LINK_SYSTEM_LIST',
//     data: params
//   })
// }

export function apiSelectLinkSystemTableList(params = {}) { // 연동시스템 테이블 조회(selectBox)
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_SYSTEM_TABLE_LIST',
    data: params
  })
}

export function apiInsertLinkSystemListProc(params = {}) { // 연동 시스템 관리 테이블 등록
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_LINK_SYSTEM_LIST_PROC',
    data: params
  })
}

export function apiUpdateLinkSystem(params = {}) { // 연동 시스템 관리 테이블 수정
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_LINK_SYSTEM',
    data: params
  })
}

// export function apiDeleteLinkSystemList(params = {}) { // 연동 시스템 관리 테이블 삭제
//   return http({
//     url: '/modify',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'DELETE_LINK_SYSTEM_LIST',
//     data: params
//   })
// }
export function apiDeleteLinkSystemListProc(params = {}) { // 연동 시스템 관리 테이블 삭제
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_LINK_SYSTEM_LIST_PROC',
    data: params
  })
}

export function apiSelectApicodeList(params = {}) { // API 코드 리스트 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_API_CODE_LIST',
    data: params
  })
}

export function apiSelectDataSetReqProc(params = {}) { // 데이터셋 > 조회 > 요청
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'REQUEST_API_DATA_SET_PROC',
    data: params
  })
}

export function apiInsertDataSetReqProc(params = {}) { // 데이터셋 > 생성 요청
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_DATA_REQ_PROC',
    data: params
  })
}

export function apiSelectTbMetaDataSetMst(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TB_METADATA_MST',
    data: params
  })
}

export function apiORG_HEAD_ORGS_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'ORG_HEAD_ORGS_LIST',
    data: params
  })
}

export function apiORG_CENTER_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'ORG_CENTER_LIST',
    data: params
  })
}

export function apiORG_TEAM_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'ORG_TEAM_LIST',
    data: params
  })
}

export function apiORG_OFFICE_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'ORG_OFFICE_LIST',
    data: params
  })
}

// -- 시작
export function apiSELECT_VW_ORG_MST_HQ(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_VW_ORG_MST_HQ',
    data: params
  })
}
export function apiGET_ORG_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'GET_ORG_LIST',
    data: params
  })
}
export function apiGET_OFFICE_LIST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'GET_OFFICE_LIST',
    data: params
  })
}
// -- 끝

export function apiSELECT_NEA_LIST(params = {}) {
  if (!isProd && debug) {
    // return apiGetObjectFromJsonFile2('SELECT_NEA_LIST.json')
  } else {
    return http({
      url: '/selectList',
      method: 'post',
      filePath: filePath,
      sqlId: 'SELECT_NEA_LIST',
      data: params,
    })
  }
}

export function apiSELECT_NEA_UPPER_LIST(params = {}) {
  if (!isProd && debug) {
    // return apiGetObjectFromJsonFile2('SELECT_NEA_UPPER_LIST.json')
  } else {
    return http({
      url: '/selectList',
      method: 'post',
      filePath: filePath,
      sqlId: 'SELECT_NEA_UPPER_LIST',
      data: params,
    })
  }
}
export function apiSELECT_NEA_DOWN_LIST(params = {}) {
  if (!isProd && debug) {
    // return apiGetObjectFromJsonFile2('SELECT_NEA_DOWN_LIST.json')
  } else {
    return http({
      url: '/selectList',
      method: 'post',
      filePath: filePath,
      sqlId: 'SELECT_NEA_DOWN_LIST',
      data: params
    })
  }
}
export function apiSELECT_TB_INTRL_SYSTEM_MST(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TB_INTRL_SYSTEM_MST',
    data: params
  })
}

// export function apiInsertDataSetAuthList(params = {}) { // 데이터셋 > 생성 요청
//   return http({
//     url: '/selectOne',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'INSERT_DATA_SET_AUTH_LIST',
//     data: params
//   })
// }

// export function apiInsertDataSetValueList(params = {}) { // 데이터셋 > 생성 요청
//   return http({
//     url: '/selectOne',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'INSERT_DATA_SET_VALUE_LIST',
//     data: params
//   })
// }

