import moment from 'moment'
import { AppOptions } from '@/class/appOptions'
import { getJsonfileName2 } from '@/utils/jsonfile'
import store from '@/store'

export const _var = { moment }
const storage = window.localStorage || window.sessionStorage
const MAX = AppOptions.instance.debug === true ? 10000 : 100
const { project } = AppOptions.instance

const state = {
  logs: JSON.parse(storage.getItem('serviceLogs') || '[]')
}

const mutations = {
  LOAD_SERVICE_LOG: (state) => {
    state.logs = JSON.parse(storage.getItem('serviceLogs'))
  },
  SAVE_SERVICE_LOG: (state) => {
    storage.setItem('serviceLogs', JSON.stringify(state.logs))
  },
  DELETE_SERVICE_LOG_IN_STORAGE: (state) => {
    storage.removeItem('serviceLogs')
  },
  ADD_SERVICE_LOG: (state, log) => {
    state.logs.push(log)
    if (state.logs.length > MAX) {
      state.logs.splice(0, state.logs.length - MAX)
    }
  },
  SET_SERVICE_LOG: (state, arrLog) => {
    state.logs = [].concat(...arrLog)
  },
  CLEAR_SERVICE_LOG: (state) => {
    state.logs.splice(0)
  }
}

const actions = {
  addServiceLog({ commit }, response) {
    const bcnApAddr = store.getters?.server?.bcnApAddr
    const { data, status, config } = response
    const { method, baseURL, url, params, requestTime, urlOrigin, filePath, fileIndex } = config
    const jsonFileName = config.headers.jsonFileName || getJsonfileName2(urlOrigin, config, project)

    const paramString = (typeof params === 'string') ? params : `'${JSON.stringify(params)}'`
    const fetch = `fetch('${url}', { 'headers': { 'content-type': 'application/json; charset=UTF-8' }, 'body': ${paramString}, 'method': '${method}' })`

    if (!jsonFileName) return

    const log = {
      bcnApAddr: bcnApAddr,
      fileIndex,
      jsonFileName,
      data,
      status,
      config: {
        filePath,
        method,
        baseURL,
        url,
        urlOrigin,
        params: params || null,
        data: config.data || null,
        fetch
      },
      requestTime: moment(requestTime).format('YYYY-MM-DD HH:mm:ss.SSS'),
      responseTime: moment(Date.now()).format('YYYY-MM-DD HH:mm:ss.SSS'),
      duration: Date.now() - requestTime
    }
    commit('ADD_SERVICE_LOG', log)
  },
  addServiceErrorLog({ commit }, error) {
    if (!error.response) {
      // error.config 로 접근해야 한다.
    }
    const bcnApAddr = store.getters?.server?.bcnApAddr
    const { data, status, config, message, code } = error?.response || error
    const { method, baseURL, url, params, requestTime, urlOrigin, filePath, fileIndex } = config
    const jsonFileName = config.headers.jsonFileName || getJsonfileName2(urlOrigin, config)

    const paramString = (typeof params === 'string') ? params : `'${JSON.stringify(params)}'`
    const fetch = `fetch('${url}', { 'headers': { 'content-type': 'application/json; charset=UTF-8' }, 'body': ${paramString}, 'method': '${method}' })`

    if (!jsonFileName) return

    const log = {
      bcnApAddr: bcnApAddr,
      fileIndex,
      jsonFileName,
      data: data ?? { 'result': false, message },
      status: status ?? code,
      config: {
        filePath,
        method,
        baseURL,
        url,
        urlOrigin,
        params: params || null,
        data: config.data || null,
        fetch
      },
      requestTime: moment(requestTime).format('YYYY-MM-DD HH:mm:ss.SSS'),
      responseTime: moment(Date.now()).format('YYYY-MM-DD HH:mm:ss.SSS'),
      duration: Date.now() - requestTime
    }
    commit('ADD_SERVICE_LOG', log)
  },
  setServiceLog({ commit }, arrLog) {
    commit('SET_SERVICE_LOG', arrLog)
    commit('SAVE_SERVICE_LOG')
  },
  clearServiceLog({ commit }) {
    commit('CLEAR_SERVICE_LOG')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
