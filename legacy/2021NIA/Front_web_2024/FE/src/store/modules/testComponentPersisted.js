const state = {
    savedComponentConfigMap: {},
}

const mutations = {
    SAVE_COMPONENT_DATA: (state, map) => {
        state.savedComponentConfigMap[map.path] = map.data
    },

    DEL_SAVED_COMPONENT_DATA: (state, path) => {
        delete state.savedComponentConfigMap[path]
    },
}

const actions = {
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

