import _ from 'lodash'
const state = {
    curProfileByVue: {/* [viewName]: curProfileName ... */ },
    toParams: null,
    adminYn: 'Y',
    ownerYn: 'Y',
    suserGradeCd: 'UR0001',
    currentCondition: {},
    isDropdownOpen: false,
    isFacilites: false, // 시설용 화면(시설용IP배정, 시설용IP할당, 시설용IPv4 자동할당) 활성상태
}

const mutations = {
    SET_CURRENT_PROFILE_BY_VUE(state, param) {
        state.curProfileByVue = Object.assign({}, state.curProfileByVue, { [param.key]: param.profileName })
    },
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
    },
    RESET_CURRENT_CONDITION(state) {
        state.currentCondition = {}
    },
    MERGE_CURRENT_CONDITION(state, value) {
        /* value = requestParameter */
        _.merge(state.currentCondition, value)
    },
    SET_DROPDOWN_VISIBILITY(state, isVisible) {
        state.isDropdownOpen = isVisible
    },
    SET_IS_FACILITES_OPTION(state, isVisible) {
        state.isFacilites = isVisible
    },
}
const actions = {
    setCurProfileByVue({ commit }, params) {
        commit('SET_CURRENT_PROFILE_BY_VUE', params)
    },
    setToParam({ commit }, params) {
        commit('SET_TO_PARAM', params)
    },
    setAdminYn({ commit }, value) {
        commit('SET_ADMIN_YN', value)
    },
    setOwnerYn({ commit }, value) {
        commit('SET_OWNER_YN', value)
    },
    resetCurrentCondition({ commit }, value) {
        commit('RESET_CURRENT_CONDITION')
    },
    mergeCurrentCondition({ commit }, value) {
        commit('MERGE_CURRENT_CONDITION', value)
    },
    setDropdownVisibility({ commit }, value) {
        commit('SET_DROPDOWN_VISIBILITY', value)
    },
    setIsFacilitesOption({ commit }, value) {
        commit('SET_IS_FACILITES_OPTION', value)
    },
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
