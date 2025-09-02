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

function getCurrentTime() {
    const now = new Date()
    return now.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
    })
}

const state = {
    eventParameter,
    chatMessages: [{
        type: 'bot',
        content: '안녕하세요! 무엇을 도와드릴까요?',
        time: getCurrentTime(),
    }]
}

const mutations = {
    SWITCH_EVENT_PARAMETER(state, { name, parameter }) {
        if (parameter && parameter.length > 0) {
            if (!Object.prototype.hasOwnProperty.call(state.eventParameter, name)) {
                throw new Error('parameter가 존재함에도 state가 정의되지 않았습니다.')
            }
            state.eventParameter[name] = parameter
        }
    },

    CLEAR_EVENT_PARAMETER(state, { name }) {
        if (!Object.prototype.hasOwnProperty.call(state.eventParameter, name)) {
            throw new Error('state가 정의되지 않았습니다.')
        }
        state.eventParameter[name] = ''
    },

    PUSH_CHAT_MESSAGE(state, { content, type }) {
        state.chatMessages.push({
            type: type,
            content: content,
            time: getCurrentTime(),
        })
    },

    POP_CHAT_MESSAGE(state) {
        var x = state.chatMessages.pop()
        // x는 질문인지 확인해서 아니면 error 표시
    }
}

const actions = {
    pushLodingMessage({ commit }) {
        commit('PUSH_CHAT_MESSAGE', {
            type: 'bot',
            content: '검색 중입니다...',
            time: getCurrentTime(),
        })
    },

    userPushQuestionMessage({ dispatch, commit }, { content }) {
        commit('PUSH_CHAT_MESSAGE', { content, type: 'user' })
        dispatch('pushLodingMessage')
    },

    botPushAnsewerMessage({ commit }, { content, isAnswer }) {
        if (isAnswer) {
            commit('POP_CHAT_MESSAGE')
        } else {
            content += ' <span class="move-text">[진행]</span>'
        }
        commit('PUSH_CHAT_MESSAGE', { content, type: 'bot' })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

