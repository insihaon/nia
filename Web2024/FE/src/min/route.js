import store from '@/store'
import router from '@/router'
import { exportToFile } from '@/utils'
import _ from 'lodash'
import clip from '@/utils/clipboard'

export function getServicelist(filter) {
  const logs = store.getters.serviceLogs
  let i = 0
  const arr = []

  if (filter) {
    console.info(`filter keyword : ${filter}`)
  }

  for (let index = logs.length - 1; index >= 0; --index, i++) {
    var log = logs[index]
    if (filter) {
      if (Number.isInteger(filter)) {
        if (filter < i + 1) {
          continue
        }
      } else if (
        new RegExp(filter, 'gi').test(JSON.stringify(log)) === false
      ) {
        continue
      }
    }
    // if (log.data.sql === null) continue
    arr.unshift(_.cloneDeep(logs[index]))
  }

  return arr
}

export function getApilist(filter) {
  const serviceList = getServicelist(filter)
  const array = []
  for (let index = 0; index < serviceList.length; index++) {
    const { config } = serviceList[index]
    array.push(
      JSON.stringify({
        url: config.urlOrigin,
        method: config.method,
        filePath: config.filePath,
        data: JSON.parse(config.data)
      })
    )
  }
  console.log('클립보드에 API가 복사 되었습니다.')
  clip(array.join(',\n'))
}

export function resendService(filter) {
  try {
    const serviceList = getServicelist(filter)
    const service = serviceList[0]
    const param = {
      url: service.config.urlOrigin,
      method: service.config.method,
      command: service.config.command,
      sqlId: service.config.sqlId,
      data: service.config.data
    }

    if (service) {
      console.log(`resendService ${JSON.stringify(param)}`)
      service(param)
    }
  } catch (error) {
    console.error(error)
  }
}

export async function exportServicelist(filter, maxByte) {
  const array = getServicelist(filter)
  for (let index = 0; index < array.length; index++) {
    const log = array[index]
    const { jsonFileName } = log
    await exportToFile(log, jsonFileName, maxByte)
  }
  return array
}

export function getLastSql(filter) {
  const last3 = getServicelist(filter).filter(r => r && r.data && r.data.sql).slice(0, 3)

  last3.forEach(log => {
    const sql = log && log.data && log.data.sql
    if (sql) {
      console.debug(`\n\n-- ${log.responseTime}`)
      console.log(sql)
    }
  })
}

export function getViewName() {
  return (router.history.current.path || '').substring(1).replace(/\/index$/, '').replace(/\//gi, '/')
}
