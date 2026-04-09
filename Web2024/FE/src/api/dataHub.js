// NIA에서 참조하는 DataHub API 함수만 유지
// 재결합 시 원본 dataHub.js로 교체할 것 (03_재결합_merge_가이드.md 참고)
import http from '@/min/http'

export const filePath = __filename.replace(/\\/g, '/')

export function apiSelectDataSetHistList(params = {}) { // 데이터셋 요청 이력 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_HIST_LIST',
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

export function apiSelectDataSetPopOver(params = {}) { // 데이터셋 요청 정보 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_POP_OVER_DATA',
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

export function apiSelectDataCatalogList(params = {}) { // 데이터셋 카탈로그 테이블 조회
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SET_LIST',
    data: params
  })
}
