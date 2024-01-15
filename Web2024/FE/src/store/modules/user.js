import { apiGetInfo, apiLogin, apiLogout, apiSignUp } from '@/api/auth'
import { AppOptions } from '@/class/appOptions'
import Encrypt from '@/assets/libs/Encrypt.min'
import router, { resetRouter } from '@/router'
import { getInfo, setInfo, removeInfo, getToken, removeToken, setToken } from '@/utils/auth'

export const _var = { Encrypt, apiLogin, apiLogout, apiSignUp, apiGetInfo, getToken, setToken, removeToken, router, resetRouter,
  getInfo, setInfo, removeInfo
}

const state = {
  token: getToken(),
  useOtp: '',
  uid: '',
  name: '',
  avatar: '',
  roles: [],
  userCount: 0,
  sessionCount: 0,
  info: getInfo(),
  nmsUid: '' /* gwt의 nms에서는 LOGIN_ID와 USER_ID가 다르므로, USER_ID는 DB에서 따로 가져온다. */,
  nmsLoginId: '',
  blackDtlList: ['btnAdd', 'btnTemplate', 'btnManualAdd', 'btnReceiver', 'btnExceptAdd', 'btnAnalysis', 'btnDelete', 'btnManualDelete'],
}

const mutations = {
  SET_INFO: (state, info) => {
    state.info = info
    setInfo(info)
  },
  SET_TOKEN: (state, token) => {
    state.token = token
    setToken(token)
  },
  SET_USE_OTP: (state, useOtp) => {
    state.useOtp = useOtp
  },
  SET_UID: (state, uid) => {
    state.uid = uid
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_BLACK_DTL_LIST: (state, list) => {
    state.blackDtlList = list
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_CONNECT_COUNT: (state, { userCount, sessionCount }) => {
    state.userCount = userCount
    state.sessionCount = sessionCount
  },
  SET_NMS_LOGINID: (state, loginId) => {
    state.nmsLoginId = loginId
  },
  SET_NMS_UID: (state, uid) => {
    state.nmsUid = uid
  }
}

const actions = {
  setConnectCount({ commit }, value) {
    commit('SET_CONNECT_COUNT', value)
  },

  async login({ commit, dispatch, state, rootState }, userInfo = {}) {
    const { server } = rootState.app
    let { username, password, ctrlKey } = userInfo
    const { debug, urlParam, project, isProd } = AppOptions.instance
    const { LOGIN_ID, MENU_ID, USER_ID, USER_NAME } = urlParam
    const NODE_ENV_DEV = Boolean(process.env.NODE_ENV === 'development' ?? 'false')

    return new Promise((resolve, reject) => {
      if (!(debug || NODE_ENV_DEV) && (project === 'datahub' && ['O1000179', 'O1000184', 'O0000183'].includes(MENU_ID) === false)) {
        console.error('권한 요청 실패 : 부적합한 MENU_ID')
        resolve(false)
        return
      }

      if (server?.authLogin === false) {
        username = LOGIN_ID ?? username ?? 'anonymous'
        password = password ?? 'anonymous'
        ctrlKey = false
      }

      return apiLogin({ username: username, password: password, ctrlKey, MENU_ID, USER_ID }).then(response => {
        const { result } = response
        if (project === 'datahub') {
          if (isProd) {
            // eslint-disable-next-line eqeqeq
            if (result.Reserved0 != LOGIN_ID || result.Reserved1 != USER_ID || result.Reserved2 != MENU_ID || result.Name != USER_NAME) {
              resolve(false)
              return
            }
          }
        }

        commit('SET_INFO', result)
        commit('SET_TOKEN', result.accessToken)
        commit('SET_USE_OTP', result.otpShow)
        commit('SET_ROLES', result.RolesList ?? [])
        commit('SET_BLACK_DTL_LIST', result.BlackDtlList ?? [])
        AppOptions.instance.lastUser = Encrypt.toEncrypt({ id: username, pw: (ctrlKey || debug) ? password : null })
        resolve(true)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  async getInfo({ commit, state, rootState }, anonymousMode) {
    const { id, roles, N_NAME, user_name, Name } = state.info
    commit('SET_UID', id)
    commit('SET_ROLES', roles)
    commit('SET_NAME', N_NAME || user_name || Name)

    if (!roles || roles.length <= 0) {
      console.error('getInfo: roles must be a non-null array!')
    }

    try {
      // const ws = WebSocketManager.instance
      // ws.connect()
    } catch (error) {
      // 예외 처리
    }

    return state.info
  },

  logout({ commit, state, dispatch }) {
    apiLogout(state.token)

    commit('SET_TOKEN', '')
    commit('SET_INFO', '')
    commit('SET_ROLES', [])
    removeToken()
    removeInfo()
    resetRouter()

    // WebSocketManager.instance.dispose()
    dispatch('tagsView/delAllViews', null, { root: true })
    window.location.reload()
  },

  async signup({ commit, dispatch, state }, joinInfo) {
    if (state.pass) {
      return new Promise((resolve, reject) => {
        commit('SET_INFO', {
          id: 'testuser', roles: ['ROLE_ANNOYMOUS'], N_NAME: 'testuser'
        })
        commit('SET_TOKEN', 'accessToken')
        commit('SET_USE_OTP', false)
        setToken('accessToken')
        resolve()
      })
    }
    const { login_id, password, user_name, user_id, tel_num, email_addr, ctrlKey } = joinInfo
    const { debug } = AppOptions.instance
    return new Promise((resolve, reject) => {
      apiSignUp({ login_id, password, user_name: user_name.trim(), user_id, tel_num, email_addr }).then(response => {
        const { result } = response
        commit('SET_INFO', result)
        commit('SET_TOKEN', result.accessToken)
        commit('SET_USE_OTP', result.otpShow)
        AppOptions.instance.lastUser = Encrypt.toEncrypt({ id: login_id, pw: (ctrlKey || debug) ? password : null })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  resetInfo({ commit }) {
    return new Promise(resolve => {
      commit('SET_INFO', '')
      commit('SET_ROLES', [])
      removeInfo()
      resolve()
    })
  },

  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    // commit('SET_INFO', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    router.addRoutes(accessRoutes)

    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
