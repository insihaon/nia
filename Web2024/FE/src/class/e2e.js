import moment from 'moment'

import { AppOptions } from '@/class/appOptions'
import http from '@/min/http'
import { exportServicelist, getServicelist, clearServicelist } from '@/min/route'
import $store from '@/store'
import { apis as niaApis } from '@/api/e2e/nia'
import { apis as ipmsApis } from '@/api/e2e/ipms'
import { apis as testApis } from '@/api/e2e/test'

export const filePath = __filename.replace(/\\/g, '/')

export const _var = { moment }
let instance = null

export const apis_exam = [
  { url: '/selectList', sqlId: 'SELECT_API_LIST', param: { }, comment: '' }
]

function toString(data) {
  if (!data) return undefined
  return typeof data === 'string' ? data : `${JSON.stringify(data)}`
}

function toObejct(data = {}) {
  return (typeof data === 'object') ? data : JSON.parse(data)
}

export async function requestFactory(url, sqlId = '', param = {}, fileIndex = 0) {
  // AppOptions.instance.debug && console.log(JSON.stringify({ url, sqlId, param }))
  return http({
    url,
    method: 'post',
    data: toObejct(param),
    filePath: filePath,
    sqlId,
    fileIndex
  })
}

export class E2e {
  static get instance() {
    return new E2e()
  }

  constructor() {
    if (instance) {
      return instance
    }
    instance = window.e2e = this
    this.self = this
    this.maxMB = 20 /* 최대 json 파일 크기 20MB 설정 */
    this.fileIndex = false /* true: 파일명에 index를 붙인다. */
    this.fails = []
    this.retry = (process.env.NODE_ENV !== 'development')
  }

  async request(apis, start, end, download) {
    // for (let index = start; index < end; index++) {
    //   console.log(`index=${index}, start=${start}, end=${end}`)
    // }

    start = start ?? 0
    end = end ?? apis.length

    return new Promise(resolve => {
      const arr = []
      for (let index = start; index < end; index++) {
        const api = apis[index]
        const obj = (typeof api === 'string') ? JSON.parse(api) : api
        const { url, sqlId, param, data } = obj
        arr.push(requestFactory(url, sqlId, (param ?? data), obj.fileIndex ?? index))
      }

      Promise.allSettled(arr)
        .then(async result => {
          this.fails = result.reduce((acc, cur, idx) => {
            const success = (cur.status === 'fulfilled' && cur?.value?.result !== false)
            if (!success) acc.push(Object.assign({}, apis[idx], { fileIndex: idx }))
            return acc
          }, [])
          const f = this.fails.length
          const s = end - start - f
          // console.log(`E2E TEST - Result(${start}~${end}): success=${s}, fail=${f}`)

          if (download) {
            await exportServicelist(null, this.maxMB * 1024 * 1024)
            $store.dispatch('serviceLog/clearServiceLog')
          }
          resolve({ f, s })
        })
    })
  }

  async getData(apis = apis_exam, start = 0, end = 1100, buffer = 100, download = false) {
    clearServicelist()
    this.fails.splice(0)

    window.fileIndex = this.fileIndex

    console.log('E2E TEST - START')
    end = Math.min(end, apis.length)
    let fail = 0
    let success = 0
    for (let index = start; index < end; index += buffer) {
      const { f, s } = await this.request(apis, index, Math.min(end, index + buffer), download)
      // console.log(`start=${index}, end=${Math.min(end, index + buffer)}`)
      fail += f
      success += s
    }

    // retry ...
    if (this.retry) {
      window.fileIndex = true
      await this.request(this.fails)
      window.fileIndex = this.fileIndex
    }

    getServicelist()

    console.log(`E2E TEST - FINISH: success=${success}, fail=${fail}`)
  }

  async nia(download = false, start = 0, end = 1100, buffer = 100,) {
    this.getData(niaApis, start, end, buffer, download)
  }

  async ipms(download = false, start = 0, end = 1100, buffer = 100,) {
    this.getData(ipmsApis, start, end, buffer, download)
  }

  async test(download = false, start = 0, end = 1100, buffer = 100,) {
    this.getData(testApis, start, end, buffer, download)
  }

  get(apis) {
    if (Array.isArray(apis) === false) {
      apis = [apis]
    }

    this.request(apis, 0, 1).then(result => {
      console.log(JSON.parse(JSON.stringify(result[0]?.value?.result)))
    })
  }
}

E2e.instance
