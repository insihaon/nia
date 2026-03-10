const state = {
    datahubMenuId: 'O0000183',
    authRejectReason: '',
    dashboardCollectTimeHistory: [],
    timestamp: null,

    grafanaSetting: {
        theme: 'light'
    }
}

const mutations = {
    SET_STATE(state, { key, value }) {
        state[key] = value
    }
}

const actions = {
    SET_STATE({ commit }, { key, value }) {
        try {
            commit('SET_STATE', { key, value })
        } catch (e) {
            debugger
        }
    }

}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
