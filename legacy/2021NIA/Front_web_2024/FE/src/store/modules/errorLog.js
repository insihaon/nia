const storage = window.localStorage || window.sessionStorage

const state = {
  logs: JSON.parse(storage.getItem('errorLogs')) || []
}

const mutations = {
  LOAD_ERROR_LOG: (state) => {
    state.logs = JSON.parse(storage.getItem('errorLogs'))
  },
  SAVE_ERROR_LOG: (state) => {
    storage.setItem('errorLogs', JSON.stringify(state.logs))
  },
  DELETE_ERROR_LOG_IN_STORAGE: (state) => {
    storage.removeItem('errorLogs')
  },
  ADD_ERROR_LOG: (state, log) => {
    state.logs.push(log)
  },
  SET_ERROR_LOG: (state, arrLog) => {
    state.logs = [].concat(...arrLog)
  },
  CLEAR_ERROR_LOG: (state) => {
    state.logs.splice(0)
  }
}

const actions = {
  addErrorLog({ commit }, log) {
    commit('ADD_ERROR_LOG', log)
  },
  setErrorLog({ commit }, arrLog) {
    commit('SET_ERROR_LOG', arrLog)
    commit('SAVE_ERROR_LOG')
  },
  clearErrorLog({ commit }) {
    commit('CLEAR_ERROR_LOG')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
