import { apiIpAlarmList, apiTransmissionAlarmList } from '@/api/nia'

const POLL_INTERVAL = 60 * 1000 // 60초

const state = {
  ipNetworkList: [],
  transmissionNetworkList: [],
  pollTimer: null,
  initialized: false
}

const mutations = {
  SET_IP_ALARM(state, list) {
    state.ipNetworkList = list || []
  },
  SET_TRANS_TT(state, list) {
    state.transmissionNetworkList = list || []
  },
  SET_POLL_TIMER(state, timer) {
    state.pollTimer = timer
  },
  SET_INITIALIZED(state, val) {
    state.initialized = val
  }
}

const actions = {
  async fetchIpAlarmList({ commit }, param) {
    try {
      const res = await apiIpAlarmList(param)
      commit('SET_IP_ALARM', res?.result)
    } catch (error) {
      console.error('[nia] fetchIpAlarmList error:', error)
    }
  },
  async fetchTransAlarmList({ commit }) {
    try {
      const res = await apiTransmissionAlarmList()
      commit('SET_TRANS_TT', res?.result)
    } catch (error) {
      console.error('[nia] fetchTransAlarmList error:', error)
    }
  },
  async fetchAll({ dispatch }) {
    await Promise.all([
      dispatch('fetchIpAlarmList'),
      dispatch('fetchTransAlarmList')
    ])
  },
  startPolling({ state, dispatch, commit }) {
    if (state.pollTimer) return
    dispatch('fetchAll')
    const timer = setInterval(() => dispatch('fetchAll'), POLL_INTERVAL)
    commit('SET_POLL_TIMER', timer)
    commit('SET_INITIALIZED', true)
  },
  stopPolling({ state, commit }) {
    if (state.pollTimer) {
      clearInterval(state.pollTimer)
      commit('SET_POLL_TIMER', null)
    }
  },
  // 하위 호환: dashboard에서 직접 데이터를 넣을 때 (WebSocket 이벤트)
  insertIpNetworkList({ commit }, value) {
    commit('SET_IP_ALARM', value)
  },
  insertTransmissionNetworkList({ commit }, value) {
    commit('SET_TRANS_TT', value)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
