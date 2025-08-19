import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import createPersistedState from 'vuex-persistedstate'
import Encrypt from '@/assets/libs/Encrypt.min'
import { AppOptions } from '@/class/appOptions'

Vue.use(Vuex)

const modulesFiles = require.context('./modules', true, /\.js$/)

// 폴더 modules 하위 js 파일의 파일명 만 추출함
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules[moduleName] = value.default
  return modules
}, {})

const KEY = 'state'
const paths = ['testComponentPersisted', 'systemMonitoring.systemMonitoringMap'] // Object.keys(modules)
const debug = AppOptions.instance.debug

const getItem = (decrypt) => {
  return (key) => decrypt ? localStorage.getItem(key) : Encrypt.toDecrypt(localStorage.getItem(key))
}

const setItem = (decrypt) => {
  return (key, state) => decrypt ? localStorage.setItem(key, state) : localStorage.setItem(key, Encrypt.toEncrypt(JSON.stringify(state)))
}

  /**
   * 데이터 초기화
   */
  ; (function () {
    let decrypt = false
    try {
      JSON.parse(localStorage.getItem(KEY))
      decrypt = true
    } catch (error) {
      decrypt = false
    }

    if (decrypt !== debug) {
      // eslint-disable-next-line no-useless-call
      const data = getItem(decrypt).call(null, KEY)
      // eslint-disable-next-line no-useless-call
      setItem(debug).call(null, KEY, data)
    }
  })()

const persistedState = createPersistedState({
  key: KEY, // 저장할 상태의 키
  // storage: window.localStorage, // 원하는 저장소를 선택합니다 (로컬 스토리지, 세션 스토리지 등)
  paths,
  storage: {
    getItem: getItem(debug),
    setItem: setItem(debug),
    removeItem: key => { localStorage.removeItem(key) },
  },
})

const store = new Vuex.Store({
  modules,
  getters,
  plugins: [
    persistedState
  ],
})
export default store
