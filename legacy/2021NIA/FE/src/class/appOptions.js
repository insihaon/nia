import { Storage } from '@/assets/libs/Storage'
import store from '@/store'
import { Device } from './device'
const { param2Obj, reload } = require('@/utils')

const APP_PROJECT = process.env.VUE_APP_PROJECT
const NODE_ENV_DEV = process.env.NODE_ENV === 'development'

let instance = null

export class AppOptions extends Storage {
  static get instance() {
    return new AppOptions()
  }

  constructor() {
    if (instance) {
      return instance
    }

    super()

    instance = this
    this.self = this
    this._data = Object.assign(this._data, {
      baseURLs: this.readEnv(process.env.VUE_APP_BASE_API, ['//localhost:8070', '/mock']),
      baseURLIndex: this.readEnv(process.env.VUE_APP_BASE_API_INDEX, 0),
      isOnlyFront: this.readEnv(process.env.VUE_APP_ONLY_FE, false),
      mobile: Device.instance.mobile ?? false,
      mock: process.env.VUE_APP_MOCK || '',
      projectList: ['datahub', 'nia', 'ipms'],
      project: APP_PROJECT?.toLowerCase(),
      baseURL: null,
      useWebsocket: this.readEnv(process.env.VUE_APP_USE_WEBSOCKET, true),
      env: process.env,
      lastUser: null,
      useWsLog: false,
      wsTicket: true,
      language: ['ko', 'en'].at(0),
      urlParam: {},
    })
    this._load()
    this._defineProp()
    // _load 된 데이터를 encrypt / decrypt 로 적용하기 위해 저장
    this._save()

    this._data.baseURL = this._getDefaultBaseUrl()
    // this._data.mock = this._data.mock || this.readEnv(process.env.VUE_APP_MOCK, '')

    const href = location.href
    this._data.urlParam = param2Obj(href)
    this._data.debugEncrypt = !this.debug
    this.updateUseWebSocket()

    if (this.debug) {
      setTimeout(() => {
        console.log(`useWebsocket=${this.useWebsocket}`)
        console.log(`baseURL=${this.baseURL}`)
        console.log(this._data)
      }, 1000)
    }
  }

  // get mock() {
  //   /* request 요청시 json file을 가져온다 */
  //   return this.baseURL === '/mock'
  // }
  get isGod() {
    return this._data.debug
  }

  get dark() {
    return ['ipms'].includes(this._data.project)
  }

  get encrypt() {
    return this._data.mock !== 'FE' && this._data.debugEncrypt
  }

  setFrontMock() {
    this.setMock('FE')
  }

  setBackendMock() {
    this.setMock('BE')
  }

  setMock(mock = null, project = null) {
    const newOptions = {}

    if (project) newOptions.project = project

    switch (mock) {
      case 'FE':
        newOptions.debug = true
        console.error(`\\frontend\\mock\\json\\${project} 에 json 파일이 있는지 확인하세요`)
        break
      case 'BE':
      // eslint-disable-next-line no-fallthrough
        console.error(`\\msa\\json\\mock 에 json 파일이 있는지 확인하세요`)
        break
      default:
        mock = null
        this.reset()
    }
    newOptions.mock = mock
    newOptions.useWebsocket = false
    this.update(newOptions, false)
    setTimeout(() => {
      reload(true)
    }, 1000)
  }

  _getDefaultBaseUrl = function () {
    const { baseURL, baseURLs, baseURLIndex, project } = this

    if (!baseURLs.includes('/mock')) {
      baseURLs.push('/mock')
    }

    const { host } = document.location

    let url = null
    if (baseURL) {
      return baseURL
    } else if (!this.debugOrDev) {
      url = `//${host}`
    } else if (baseURLs[baseURLIndex]) {
      url = baseURLs[baseURLIndex]
    } else {
      url = `//${host}`
    }

    const baseUrl = process.env.BASE_URL || ''
    const urlPrefix = process.env.VUE_APP_URL_PREFIX || ''

    return `${url.replace(/\/+$/, '')}${baseUrl}${urlPrefix}`.replaceAll(/(?<!^)\/{2,}/g, '/')
  }

  get blackDtlList() {
    return store?.getters?.blackDtlList
  }

  get roles() {
    return store?.getters?.roles
  }

  get routes() {
    return store?.getters?.permission_routes
  }

  get wsUrl() {
    let url = store.getters?.server?.wsUrl || '/ws-stomp'
    const server = this._getDefaultBaseUrl()
    url = url.startsWith('//') ? url : `${server}${url}`
    url = url.replaceAll(/(\/\/)+/g, '/').replaceAll(/^\//g, '//')
    // console.log(`wsUrl=${url}`)
    return url
  }

  updateUseWebSocket() {
    const isMock = this._data.baseURL.includes('/mock')
    this._data.useWebsocket = !isMock
  }

  setBaseURLIndex(index) {
    const { baseURLs } = this
    const baseURL = baseURLs[index]
    if (baseURL !== undefined) {
      this.update({ baseURLIndex: index, baseURL })
    }
    return baseURLs[index]
  }
}

