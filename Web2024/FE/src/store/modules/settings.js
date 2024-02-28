import variables from '@/styles/element-variables.scss'
import defaultSettings from '@/settings'

export const _var = { variables, defaultSettings }

const storage = window.localStorage || window.sessionStorage
const { showSettings, tagsView, fixedHeader, navbarOnly, sidebarLogo, bottombar, popupLayout, menuType, layoutType, fixSide, topPaneSize, sidePaneSize } = Object.assign(defaultSettings, JSON.parse(storage.getItem('settings') || '{}'))

const state = {
  theme: variables.theme,
  showSettings: showSettings,
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  fixSide: fixSide,
  navbarOnly: navbarOnly,
  sidebarLogo: sidebarLogo,
  bottombar: bottombar,
  menuType: menuType,
  layoutType: layoutType,
  topPaneSize: topPaneSize,
  sidePaneSize: sidePaneSize,
  popupLayout: popupLayout
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value

      storage.setItem('settings', JSON.stringify(Object.assign({ ...state }, { [key]: value })))
    }
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

