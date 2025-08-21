// store.js
import { niaRoute } from '@/router/nia/index'

// 라우터 이름 기반으로 eventParameter 키 자동 생성
const eventParameter = {}
function setRouteName(routes) {
    routes.forEach((route) => {
        if (route.name) {
            eventParameter[route.name] = ''
        }

        if (route.children) {
            setRouteName(route.children)
        }
    })
}
setRouteName(niaRoute)

const state = {
    eventParameter
}

const mutations = {
    SWITCH_STATE(state, { name, parameter }) {
        if (parameter && parameter.length > 0) {
            if (!Object.prototype.hasOwnProperty.call(state.eventParameter, name)) {
                throw new Error('parameter가 존재함에도 state가 정의되지 않았습니다.')
            }
            state.eventParameter[name] = parameter
        }
    },

    CLEAR_STATE(state, { name }) {
        if (!Object.prototype.hasOwnProperty.call(state.eventParameter, name)) {
            throw new Error('state가 정의되지 않았습니다.')
        }
        state.eventParameter[name] = ''
    }

}

const actions = {

}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

