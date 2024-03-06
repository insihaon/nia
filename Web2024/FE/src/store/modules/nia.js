const state = {
    ipNetworkList: [],
    transmissionNetworkList: []
}

const mutations = {
  SET_IP_ALARM(state, { key, value }) {
    state.ipNetworkList = value
  },
  SET_TRANS_TT(state, { key, value }) {
    state.transmissionNetworkList = value
  }
}

const actions = {
  insertIpNetworkList({ commit }, value) {
    commit('SET_IP_ALARM', { value })
  },
  insertTransmissionNetworkList({ commit }, value) {
    commit('SET_TRANS_TT', { value })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
