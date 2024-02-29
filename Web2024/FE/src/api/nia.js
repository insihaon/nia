import http from '@/min/http'
import { AppOptions } from '@/class/appOptions'
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
