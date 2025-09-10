import { AppOptions } from '@/class/appOptions'
import { clone, wait } from '@/utils'
import { getJsonfileName2 } from '@/utils/jsonfile'
import store from '@/store'
import { getToken, getIpsdnToken } from '@/utils/auth'
import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import moment from 'moment'
import Encrypt from '@/assets/libs/Encrypt'

const { debug, mock, baseURL, project, encrypt } = AppOptions.instance
const debugLog = debug ? console.log : () => { /* 빈 블록 사용 금지 */ }
const clearLog = (debug) ? () => { } : () => { wait(500).then(console.clear) }
export const __ = { store, AppOptions }

if (debug) {
  Object.assign(console, console._)
}

// axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'
// axios.defaults.headers.common['Access-Control-Expose-Headers'] = 'Client-Addr'
axios.defaults.withCredentials = true // true 로 설정하면 서버에 설정이 없으면 호출이 안 될 수 있다.  "allowCredential" 로 검색 해서 서버설정도 변경 필요
const service = axios.create({
  baseURL: mock === 'FE' ? '/mock' : baseURL, // url = base url + request url
  timeout: (process.env.VUE_APP_HTTP_TIMEOUT || 10) * 1000 // request timeout
})

service.interceptors.request.use(
  config => {
    config.data = config.data || {}
    const sqlId = config.sqlId || ''
    let url = config.url
    if (/\/(selectList|selectListPaging12|selectOne|modify)$/.test(url) && sqlId) {
      url = config.url += `/${sqlId}`
    }

    const { data, testData } = config
    config.data = Encrypt.encryptHttp(data, !config.encrypt && encrypt)

    const command = config.command = url.slice(1).replace(/[^a-zA-Z0-9]+/, '_')

    if (store.getters.token) {
      config.headers['X-AUTH-TOKEN'] = getToken()
    }
    if (project === 'nia' && store.getters.ipsdnToken) {
      config.headers['X-AUTH-IP-TOKEN'] = getIpsdnToken()
    }

    if (command) {
      const params = config.data || testData || {}
      config.requestTime = Date.now()
      config.headers['fileIndex'] = config.fileIndex
      config.headers['project'] = project
      config.headers['urlOrigin'] = config.urlOrigin = url
      config.headers['jsonfilename'] = encodeURIComponent(getJsonfileName2(url, config))
      if (store.getters.server) {
        config.headers['verifier'] = Encrypt.toEncrypt(String(Date.now() - (store.getters.server.timeDiff || 0)))
      }

      if (mock === 'BE') {
        config.url = '/mock'
      }

      debugLog({ url, command, sqlId }, params && clone(params))
    }
    clearLog()
    return config
  },
  error => {
    debugLog(error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    debugLog(`response=`, { ...response })
    const res = response.data instanceof ArrayBuffer ? response : response.data

    Encrypt.decryptHttp(res)

    store.dispatch('serviceLog/addServiceLog', response)
    if (res.sql) debugLog(res.sql)

    if (response.result === 'kickout') {
      store.dispatch('user/logout')
    }

    clearLog()

    switch (response.status) {
      case 200:
        debugLog(`response.status=${response.status}`)
        return res
      case 401:
      case 403:
        debugLog(`response.status=${response.status}`)
        logout()
      // eslint-disable-next-line no-fallthrough
      default:
        debugLog(`response.status=${response.status}`)
        Message({
          message: res.message || 'Error',
          type: 'error',
          duration: 5 * 1000
        })

        return Promise.reject(new Error(res.message || 'Error'))
    }
  },
  error => {
    debugLog(`response error=`, { ...error })
    const useServiceLog = true
    const urlOrigin = error.config?.urlOrigin
    const requestTime = error.config?.requestTime

    if (useServiceLog) {
      store.dispatch('serviceLog/addServiceErrorLog', error)

      if (error && error.response) {
        switch (error.response.status) {
          case 401:
          case 403:
          case 405: // IPMS 에서 권한 없음 반환 시 로그오프하도록 처리. 공통코드에 위배된다면 코드 고려
            debugLog(`error.response.status=`, error.response.status)
            logout()
            break
        }
      }

      wait(500).then(() => {
        const { response } = error
        const result = response ? response.result : null
        if (result) {
          if (result.sql) debugLog(result.sql)
          if (result.success === false) {
            console.error(result.detailMessage)
            console.error(result.message)
            if (debug) {
              Message({
                message: result.message || result.detailMessage,
                type: 'error',
                duration: 5 * 1000
              })
            }
          }
        } else {
          console.error('response data is null, error=', error)
        }
      })
    } else {
      debugLog('err' + error)
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
      })
    }

    store.dispatch('errorLog/addErrorLog', {
      err: error,
      vm: '',
      info: `requestTime:${moment(new Date(requestTime)).format('YYYY-MM-DD HH:mm:ss.SSS')}, client-addr:${error.response.headers['client-addr']}`,
      url: urlOrigin
    })

    clearLog()
    return Promise.reject(error)
  }
)

function logout() {
  setTimeout(() => {
    store.dispatch('user/logout')
  }, 5000)
  MessageBox.confirm(
    '"로그인 세션 만료" 정책에 따라 로그아웃 됩니다. ',
    '로그아웃',
    {
      confirmButtonText: '재인증',
      cancelButtonText: '취소',
      type: 'warning'
    }
  ).then(() => {
    store.dispatch('user/logout')
  })
}

export default service
