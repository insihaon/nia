const state = {
    EventType: {
        changeLvl: 'changeLvl',
    },
    toParams: null,
    adminYn: 'Y',
    ownerYn: 'Y',
    suserGradeCd: 'UR0001'
}

const mutations = {
    SET_TO_PARAM(state, params) {
        state.toParams = params
    },

    SET_ADMIN_YN(state, value) {
        state.adminYn = value
    },

    SET_OWNER_YN(state, value) {
        state.ownerYn = value
    },

    SET_SUSER_GRADE_CD(state, value) {
        state.suserGradeCd = value
    }

}

const actions = {
    setToParam({ commit }, params) {
        commit('SET_TO_PARAM', params)
    },

    setAdminYn({ commit }, value) {
        commit('SET_ADMIN_YN', value)
    },

    setOwnerYn({ commit }, value) {
        commit('SET_OWNER_YN', value)
    }

}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
