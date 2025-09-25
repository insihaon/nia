// store.js
import { niaRoute } from '@/router/nia/index'
import _ from 'lodash'
import constants from '@/min/constants'
import { getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'
import { apiSelectSopHistList } from '@/api/nia'

const chatbotCommand = constants.nia.chatbotCommand
const chatbotKeyMap = constants.nia.chatbotKeyMap

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
    Object.keys(chatbotKeyMap).forEach((key) => {
        routerParameter[chatbotKeyMap[key].parameterKey] = ''
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

function getDefaultAlarmFocusModeFirstChatMessages() {
    return {
        type: constants.nia.chatType.botAnswer,
        content: `<b>아래 메뉴를 통해 원하시는 업무를 선택하실 수 있습니다</b><br>
    ` +
            showNumberText(1, `${chatbotCommand.focusModeCheckAlarm.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', chatbotCommand.focusModeCheckAlarm.action)}<br>`) +
            showNumberText(2, `${chatbotKeyMap.processFin.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.processFin.dialogNm, '')}<br>`) +
            showNumberText(3, `${chatbotKeyMap.configTest.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.configTest.dialogNm, '')}<br>`) +
            showNumberText(4, `${chatbotKeyMap.requestForAction.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.requestForAction.dialogNm, '')}<br><br>`) +
            `${constants.nia.chatbotComment.lastComment}`,
        time: getCurrentTime(),
    }
}

const defaultQuestionModeChatMessages = {
    type: constants.nia.chatType.botAnswer,
    content: `안녕하세요 장애대응 챗봇입니다<br><span style='color: red'>현재 챗봇 비활성화 상태입니다</span><br>발행된 티켓의 <span class="chatbotIcon">${constants.nia.chatbotIcon.assistantIcon}</span>을 클릭하면 챗봇을 활성화할 수 있습니다`,
    time: getCurrentTime(),
}

const state = {
    routerParameter,
    lastFocusModule: { name: '', type: '' },
    currentMode: constants.nia.chatbotMode.questionMode,
    questionMode_chatMessages: [_.cloneDeep(defaultQuestionModeChatMessages)],
    alarmFocusMode_chatMessages: [getDefaultAlarmFocusModeFirstChatMessages()],
    alarmFocusTicketData: {},
    alarmFocusSopDataList: [],
    actionType: constants.nia.chatbotActiontype.interactive
}

const mutations = {
    SET_LAST_FOCUS_MODULE(state, { name, type }) {
        if (name === 'chatbot') return
        if (state.lastFocusModule.name === name && state.lastFocusModule.type === type) return

        state.lastFocusModule.name = name
        state.lastFocusModule.type = type
    },

    SWTICH_ACTION(state) {
        switch (state.actionType) {
            case constants.nia.chatbotActiontype.interactive:
                state.actionType = constants.nia.chatbotActiontype.prompted
                break
            case constants.nia.chatbotActiontype.prompted:
                state.actionType = constants.nia.chatbotActiontype.interactive
                break
        }
    },

    SWITCH_ROUTER_PARAMETER(state, { name, parameter }) {
        if (parameter && (parameter.length > 0 || Object.keys(parameter).length > 0)) {
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
        if (type === constants.nia.chatType.botAlert) {
            state.alarmFocusMode_chatMessages.push({
                type: type,
                content: content,
                time: getCurrentTime(),
            })
            return
        }

        switch (state.currentMode) {
            case constants.nia.chatbotMode.questionMode:
                state.questionMode_chatMessages.push({
                    type: type,
                    content: content,
                    time: getCurrentTime(),
                })
                break
            case constants.nia.chatbotMode.alarmFocusMode:
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
            case constants.nia.chatbotMode.questionMode:
                state.questionMode_chatMessages.pop()
                break
            case constants.nia.chatbotMode.alarmFocusMode:
                state.alarmFocusMode_chatMessages.pop()
                break
        }
    },

    MODE_CHANGE(state, { newMode }) {
        state.currentMode = newMode
    },

    async SET_ALARM_FUCUS_CHAT_TICKET_DATA(state, { ticketData }) {
        state.alarmFocusTicketData = ticketData

        const res = await apiSelectSopHistList({ NODE_NM: ticketData.node_nm })
        const sopDataList = res.result
        state.alarmFocusSopDataList.length = 0
        state.alarmFocusSopDataList.push(...sopDataList)
    },

    RESET_CHAT(state) {
        switch (state.currentMode) {
            case constants.nia.chatbotMode.questionMode:
                state.questionMode_chatMessages.length = 1
                state.questionMode_chatMessages.time = getCurrentTime()
                break
            case constants.nia.chatbotMode.alarmFocusMode:
                {
                    const tempMessageArray = _.cloneDeep(state.alarmFocusMode_chatMessages)
                    state.alarmFocusMode_chatMessages.length = 1
                    state.alarmFocusMode_chatMessages.time = getCurrentTime()

                    const filterArray = tempMessageArray.filter((m) => { return m.type === constants.nia.chatType.botAlert })
                    state.alarmFocusMode_chatMessages.push(...filterArray)
                }
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
            type: constants.nia.chatType.botAnswer,
            content: searchMessaging,
            time: getCurrentTime(),
        })
    },

    userPushQuestionMessage({ dispatch, commit }, { content }) {
        commit('PUSH_CHAT_MESSAGE', { content, type: constants.nia.chatType.user })
        dispatch('pushLodingMessage')
    },

    botPushAnswerMessage({ commit }, { content, addContent, isAlert, callBack }) {
        switch (state.currentMode) {
            case constants.nia.chatbotMode.questionMode:
                if (state.questionMode_chatMessages.at(-1).content === searchMessaging) {
                    commit('POP_CHAT_MESSAGE')
                }
                break
            case constants.nia.chatbotMode.alarmFocusMode:
                if (state.alarmFocusMode_chatMessages.at(-1).content === searchMessaging) {
                    commit('POP_CHAT_MESSAGE')
                }
                break
        }

        if (addContent) { content += addContent }
        commit('PUSH_CHAT_MESSAGE', { content, type: isAlert ? constants.nia.chatType.botAlert : constants.nia.chatType.botAnswer, callBack: callBack })
    },

    newAlarmFocusChat({ commit }, { ticketData }) {
        commit('MODE_CHANGE', { newMode: 'alarmFocusMode' })
        commit('RESET_CHAT')
        commit('SET_ALARM_FUCUS_CHAT_TICKET_DATA', { ticketData })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

