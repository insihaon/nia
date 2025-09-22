// store.js
import { niaRoute } from '@/router/nia/index'
import _ from 'lodash'
import constants from '@/min/constants'
import { getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'

// 라우터 이름 기반으로 키 자동 생성
const routerParameter = {}
function setRouteName(routes) {
    routes.forEach((route) => {
        if (route.name) {
            routerParameter[route.name] = ''
        }

        if (route.children) {
            setRouteName(route.children)
        }
    })
}
setRouteName(niaRoute)

function setChatbotKey() {
    Object.keys(constants.nia.chatbotKeyMap).forEach((key) => {
        routerParameter[constants.nia.chatbotKeyMap[key].parameterKey] = ''
    })
}
setChatbotKey()

function getCurrentTime() {
    const now = new Date()
    return now.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
    })
}

const chatbotCommand = constants.nia.chatbotCommand

const defaultAlarmFocusModeFirstChatMessages = {
    type: 'bot-answer',
    content: `<b>집중경보 모드가 실행되었습니다.</b><br>
    ` +
        showNumberText(1, `${chatbotCommand.focusModeCheckAlarm.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', chatbotCommand.focusModeCheckAlarm.action)}<br>`) +
        showNumberText(2, `${chatbotCommand.failover.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', chatbotCommand.failover.action)}`),
    time: getCurrentTime(),
    ticketData: {}
}

const defaultQuestionModeChatMessages = {
    type: 'bot-answer',
    content: '안녕하세요! 무엇을 도와드릴까요?',
    time: getCurrentTime(),
}

const state = {
    routerParameter,
    lastFocusModule: { name: '', type: '' },
    currentMode: 'questionMode',
    modes: ['questionMode', 'alarmFocusMode'],
    questionMode_chatMessages: [_.cloneDeep(defaultQuestionModeChatMessages)],
    alarmFocusMode_chatMessages: [_.cloneDeep(defaultAlarmFocusModeFirstChatMessages)],
}

const mutations = {
    SET_LAST_FOCUS_MODULE(state, { name, type }) {
        if (name === 'chatbot') return
        if (state.lastFocusModule.name === name && state.lastFocusModule.type === type) return

        state.lastFocusModule.name = name
        state.lastFocusModule.type = type
    },

    SWITCH_ROUTER_PARAMETER(state, { name, parameter }) {
        if (parameter && parameter.length > 0) {
            if (!Object.prototype.hasOwnProperty.call(state.routerParameter, name)) {
                throw new Error('parameter가 존재함에도 state가 정의되지 않았습니다.')
            }
            state.routerParameter[name] = parameter
        }
    },

    CLEAR_ROUTER_PARAMETER(state, { name }) {
        if (!Object.prototype.hasOwnProperty.call(state.routerParameter, name)) {
            throw new Error('state가 정의되지 않았습니다.')
        }
        state.routerParameter[name] = ''
    },

    PUSH_CHAT_MESSAGE(state, { content, type, callBack }) {
        if (type === 'bot-alert') {
            state.questionMode_chatMessages.push({
                type: type,
                content: content,
                time: getCurrentTime(),
            })
            return
        }

        switch (state.currentMode) {
            case 'questionMode':
                state.questionMode_chatMessages.push({
                    type: type,
                    content: content,
                    time: getCurrentTime(),
                })
                break
            case 'alarmFocusMode':
                state.alarmFocusMode_chatMessages.push({
                    type: type,
                    content: content,
                    time: getCurrentTime(),
                })
                break
        }

        if (callBack) callBack()
    },

    POP_CHAT_MESSAGE(state) {
        switch (state.currentMode) {
            case 'questionMode':
                state.questionMode_chatMessages.pop()
                break
            case 'alarmFocusMode':
                state.alarmFocusMode_chatMessages.pop()
                break
        }
    },

    MODE_CHANGE(state, { newMode }) {
        state.currentMode = newMode
    },

    SET_ALARM_FUCUS_CHAT_TICKET_DATA(state, { ticketData }) {
        // 초기화
        state.alarmFocusMode_chatMessages.length = 0
        state.alarmFocusMode_chatMessages.push(defaultAlarmFocusModeFirstChatMessages)

        state.alarmFocusMode_chatMessages[0].ticketData = ticketData
    },

    RESET_CHAT(state) {
        switch (state.currentMode) {
            case 'questionMode':
                state.questionMode_chatMessages.length = 0
                state.questionMode_chatMessages.push(_.cloneDeep(defaultQuestionModeChatMessages))
                break
            case 'alarmFocusMode':
                state.alarmFocusMode_chatMessages.length = 0
                state.alarmFocusMode_chatMessages.push(_.cloneDeep(defaultAlarmFocusModeFirstChatMessages))
                break
        }
    }

}

export const searchMessaging = '검색 중입니다...'
export const errorMessaging1 = '죄송합니다. 검색 중 오류가 발생했습니다.'
export const errorMessaging2 = '죄송합니다. 검색 결과를 찾을 수 없습니다. 다른 키워드로 검색해보세요.'
export const errorMessaging3 = '찾으시는 번호는 없습니다. :'

export const endMessage = 'ESC. 모든 창닫고, 집중경보해제'
export const nextMessage = `
어떤 작업을 실행할까요?`

const actions = {
    pushLodingMessage({ commit }) {
        commit('PUSH_CHAT_MESSAGE', {
            type: 'bot-answer',
            content: searchMessaging,
            time: getCurrentTime(),
        })
    },

    userPushQuestionMessage({ dispatch, commit }, { content }) {
        commit('PUSH_CHAT_MESSAGE', { content, type: 'user' })
        dispatch('pushLodingMessage')
    },

    botPushAnswerMessage({ commit }, { content, addContent, isAlert, callBack }) {
        switch (state.currentMode) {
            case 'questionMode':
                if (state.questionMode_chatMessages.at(-1).content === searchMessaging) {
                    commit('POP_CHAT_MESSAGE')
                }
                break
            case 'alarmFocusMode':
                if (state.alarmFocusMode_chatMessages.at(-1).content === searchMessaging) {
                    commit('POP_CHAT_MESSAGE')
                }
                break
        }

        if (addContent) { content += addContent }
        commit('PUSH_CHAT_MESSAGE', { content, type: isAlert ? 'bot-alert' : 'bot-answer', callBack: callBack })
    },

    newAlarmFocusChat({ commit }, { ticketData }) {
        commit('MODE_CHANGE', { newMode: 'alarmFocusMode' })
        commit('SET_ALARM_FUCUS_CHAT_TICKET_DATA', { ticketData })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

