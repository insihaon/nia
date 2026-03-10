import { getApiData } from '@/utils'
import http from '@/min/http'

export const { filePath, group } = getApiData(__filename, [apiSetting])

export function apiSetting(params = {}) {
  return http({
    url: '/setting',
    method: 'post',
    filePath: filePath,
    sqlId: 'apiSetting',
    data: params
  })
}

