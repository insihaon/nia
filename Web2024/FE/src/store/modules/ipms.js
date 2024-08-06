const state = {
    EventType: {
        changeLvl: 'changeLvl',
    },
    toParams: null
}

const mutations = {
    SET_TO_PARAM(state, params) {
        state.toParams = params
    }
}

const actions = {
    setToParam({ commit }, params) {
        commit('SET_TO_PARAM', params)
    },
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
