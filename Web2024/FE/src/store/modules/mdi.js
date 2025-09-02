const state = {
  windows: [],
  window_param: {
    name: '',
    id: '',
    tyupe: 'default',
    windowState: 'normal',
    showTitle: true,
    x: 30,
    y: 100,
    width: 1000,
    height: 600,
    backgroundColor: '#FFFFFF',
    resizeble: true,
    scale: 'responsive',
    minWidth: 800,
    minHeight: 5000,
    maxWidth: 1800,
    maxHeight: 900,
    zindex: 100,
    closeConfirm: false,
    screenShot: null,
    params: {},
    callback: null
  }
}

const mutations = {
  BRING_TO_FRONT_WINDOW(state, id) {
    var i = 0
    var wCount = state.windows.length
    for (i = 0; i < wCount; i++) {
      if (id === state.windows[i].id) {
        state.windows[i].zindex = 1000
      } else {
        if (state.windows[i].zindex > 0) {
          state.windows[i].zindex--
        }
      }
    }
  },
  REMOVE_WINDOW(state, id) {
    var i = 0
    var wCount = state.windows.length
    for (i = 0; i < wCount; i++) {
      if (id === state.windows[i].id) {
        state.windows.splice(i, 1)
        break
      }
    }
    if (state.windows.length > 0) {
      state.windows[state.windows.length - 1].zindex = 1000
    }
  },
  ADD_WINDOW(state, value) {
    value.zindex = 1000
    var i = 0
    var wCount = state.windows.length
    for (i = 0; i < wCount; i++) {
      state.windows[i].zindex--
    }
    state.windows.push(value)
  },
  SET_WINDOW_OPTIONS(state, { id, options }) {
    var window = state.windows.find(window => window.id === id)
    if (window) {
      Object.assign(window, options)
    }
  }
}

const actions = {
  addWindow({ commit }, data) {
    commit('ADD_WINDOW', data)
  },
  removeWindow({ commit }, id) {
    commit('REMOVE_WINDOW', id)
  },
  bringToFrontWindow({ commit }, id) {
    commit('BRING_TO_FRONT_WINDOW', id)
  },
  setWindowOptions({ commit }, { id, options }) {
    commit('SET_WINDOW_OPTIONS', { id, options })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
