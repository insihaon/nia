import { AppOptions } from '@/class/appOptions'
import http from '@/min/http'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

export function apiIpAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_IP_ALARM_LIST',
    data: params
  })
}
export function apiTransmissionAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TRANSMISSION_ALARM_LIST',
    data: params
  })
}
export function apiDashboardStatistics(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DASHBOARD_STATISTICS',
    data: params
  })
}
export function apiSelfProcessStatistics(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SELF_PROCESS_STATISTICS',
    data: params
  })
}
export function apiSopList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SOP_LIST',
    data: params
  })
}
export function apiSopCode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SOP_CODE_LIST',
    data: params
  })
}

export function apiTrafficAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TRAFFIC_AGENCY_LIST',
    data: params
  })
}

export function apiSelectAgencyCodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_CODE_LIST',
    data: params
  })
}

export function apiSelectAppTrafficList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_APP_TRAFFIC_TOP_LIST',
    data: params
  })
}

export function apiApplicationCodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_APPLICATION_CODE_LIST',
    data: params
  })
}

export function apiSelectInOutTrafficList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_INOUT_TRAFFIC_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedNationList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_NATION_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_AGENCY_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedAppList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_APP_LIST',
    data: params
  })
}

export function apiSelectEquipAmountUsedList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_EQUIP_AMOUNT_USED_LIST',
    data: params
  })
}
