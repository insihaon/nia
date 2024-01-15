import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import createPersistedState from 'vuex-persistedstate'
import { encryptState, decryptState } from '@/utils/crypto'

Vue.use(Vuex)

const modulesFiles = require.context('./modules', true, /\.js$/)

// 폴더 modules 하위 js 파일의 파일명 만 추출함
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules[moduleName] = value.default
  return modules
}, {})

const paths = [] // Object.keys(modules)
const SALT_KEY = '__DATAHUB__'
const persistedState = createPersistedState({
  key: 'state', // 저장할 상태의 키
  // storage: window.localStorage, // 원하는 저장소를 선택합니다 (로컬 스토리지, 세션 스토리지 등)
  paths,
  storage: {
    getItem: (key) => decryptState(localStorage.getItem(key), SALT_KEY),
    setItem: (key, state) => localStorage.setItem(key, encryptState(state, SALT_KEY)),
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
