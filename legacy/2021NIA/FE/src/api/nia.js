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
