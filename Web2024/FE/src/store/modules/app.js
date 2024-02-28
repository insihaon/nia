import { apiSetting } from '@/api/server'
import { AppOptions } from '@/class/appOptions'
import Vue from 'vue'

const storage = window.localStorage || window.sessionStorage

const state = {
  sidebar: {
    opened: storage.getItem('sidebarStatus') ? !!+storage.getItem('sidebarStatus') : true,
    withoutAnimation: false
  },
  historybar: {
    opened: storage.getItem('historybarStatus') ? !!+storage.getItem('historybarStatus') : true,
    withoutAnimation: false
  },
  screenDevice: 'desktop',
  viewport: 'xl',
  size: storage.getItem('size') ?? ('mini' || 'small' || 'medium'),
  outline: storage.getItem('showOutline') === '1',
  windowSize: { width: 0, height: 0 },
  showWindowSize: storage.getItem('showWindowSize') === '1',
  openedViewData: JSON.parse(storage.getItem('openedViewData') || '{}'),
  expire: Number(storage.getItem('expire')),
  server: null,
  showHistory: false,
  loadTime: new Date()
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    if (state.sidebar.opened) {
      storage.setItem('sidebarStatus', 1)
    } else {
      storage.setItem('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    storage.setItem('sidebarStatus', 0)
    state.sidebar.opened = false
  },
  TOGGLE_HISTORYBAR: state => {
    state.historybar.opened = !state.historybar.opened
    state.historybar.withoutAnimation = false
    if (state.historybar.opened) {
      storage.setItem('historybarStatus', 1)
    } else {
      storage.setItem('historybarStatus', 0)
    }
  },
  CLOSE_HISTORYBAR: (state, withoutAnimation) => {
    storage.setItem('historybarStatus', 0)
    state.historybar.opened = false
    state.historybar.withoutAnimation = withoutAnimation
  },
  SET_SCREEN_DEVICE: (state, screenDevice) => {
    state.screenDevice = screenDevice
  },
  SET_VIEWPORT: (state, viewport) => {
    state.viewport = viewport
  },
  SET_SIZE: (state, size) => {
    state.size = size
    storage.setItem('size', size)
  },
  SET_WINDOW_SIZE: (state, size) => {
    state.windowSize = size
  },
  TOGGLE_OUTLINE: state => {
    state.outline = !state.outline
    if (state.outline) {
      storage.setItem('showOutline', 1)
    } else {
      storage.setItem('showOutline', 0)
    }
  },
  TOGGLE_SHOW_WINDOW_SIZE: state => {
    state.showWindowSize = !state.showWindowSize
    if (state.showWindowSize) {
      storage.setItem('showWindowSize', 1)
    } else {
      storage.setItem('showWindowSize', 0)
    }
  },
  SAVE_VIEWDATA_IN_STORAGE: (state, expire = 0) => {
    storage.setItem('expire', expire)
    storage.setItem('openedViewData', JSON.stringify(state.openedViewData))
  },
  DELETE_VIEWDATA_IN_STORAGE: (state) => {
    storage.removeItem('openedViewData')
  },
  SET_VIEWDATA: (state, viewData) => {
    Object.assign(state.openedViewData, viewData)
  },
  REMOVE_VIEWDATA: (state, viewName) => {
    Vue.delete(state.openedViewData, viewName)
  },
  REMOVEALL_VIEWDATA: (state, viewName) => {
    state.openedViewData = {}
  },
  SET_SERVER: (state, server) => {
    state.server = server

    const useWebsocket = JSON.parse(server?.wsEnabled || 'false')
    if (AppOptions.instance.useWebsocket !== useWebsocket) {
      AppOptions.instance.useWebsocket = useWebsocket
    }
  },
  SET_HEARTBEATSFAIL: (state, log) => {
    state.heartbeatsFail = { ...log }
  },
  TOGGLE_SHOW_HISTORY: (state, v) => {
    state.showHistory = !state.showHistory
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleHistoryBar({ commit }) {
    commit('TOGGLE_HISTORYBAR')
  },
  closeHistoryBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_HISTORYBAR', withoutAnimation)
  },
  setScreenDevice({ commit }, screenDevice) {
    commit('SET_SCREEN_DEVICE', screenDevice)
  },
  setViewport({ commit }, viewport) {
    commit('SET_VIEWPORT', viewport)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  },
  setWindowSize({ commit }, size) {
    commit('SET_WINDOW_SIZE', size)
  },
  toggleOutline({ commit }) {
    commit('TOGGLE_OUTLINE')
  },
  toggleShowWindowSize({ commit }) {
    commit('TOGGLE_SHOW_WINDOW_SIZE')
  },
  saveViewDataInStorage({ commit }, expire) {
    commit('SAVE_VIEWDATA_IN_STORAGE', expire)
  },
  removeViewDataInStorage({ commit }) {
    commit('DELETE_VIEWDATA_IN_STORAGE')
  },
  setViewData({ commit }, viewData) {
    commit('REMOVEALL_VIEWDATA')
    commit('SET_VIEWDATA', viewData)
  },
  putViewData({ commit }, viewData) {
    commit('SET_VIEWDATA', viewData)
  },
  removeViewData({ commit }, viewName) {
    commit('REMOVE_VIEWDATA', viewName)
  },
  async getServer({ commit, state }, force = false) {
    try {
      if (force === false && state.server) return state.server
      const { result } = await apiSetting()
      result.timeDiff = new Date().getTime() - result.time
      commit('SET_SERVER', result)
      return result
    } catch (e) {
      console.error('setting.json및 Mock모드 여부를 확인하세요', e)
    }
  },
  toggleShowHistory({ commit }) {
    commit('TOGGLE_SHOW_HISTORY')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
