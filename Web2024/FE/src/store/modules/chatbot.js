// store.js
import { niaRoute } from '@/router/nia/index'
import _ from 'lodash'
import constants from '@/min/constants'
import { getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'
import { apiIpAlarmList, apiSelectSopHistList, apiSopSyslogHistList, apiInsertChatbotHistory } from '@/api/nia'
import store from '@/store'
import moment from 'moment'

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
        content: `<b>아래 메뉴를 통해 원하시는 업무를 선택하실 수 있습니다</b>
    ` +
            showNumberText(1, `${chatbotCommand.focusModeCheckAlarm.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', chatbotCommand.focusModeCheckAlarm.action)}<br>`) +
            showNumberText(2, `${chatbotKeyMap.processFin.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.processFin.dialogNm, '')}<br>`) +
            showNumberText(3, `${chatbotKeyMap.configTest.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.configTest.dialogNm, '')}<br>`) +
            showNumberText(4, `${chatbotKeyMap.requestForAction.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), chatbotKeyMap.requestForAction.dialogNm, '')}<br>`) +
            `<br>${constants.nia.chatbotComment.lastComment}`,
        time: getCurrentTime(),
    }
}

const defaultQuestionModeChatMessages = {
    type: constants.nia.chatType.botAnswer,
    content: `안녕하세요, 장애대응 어시스턴트입니다.<br> 현재 저는 <span style='font-weight:1000; color:red'>휴면 상태</span>로 설정되어 있습니다.<br> 실시간 응대가 필요하실 경우, 발행된 티켓 옆의 <span class="chatbotIcon"><i class="el-icon-chat-dot-square"></i></span> 아이콘을 눌러 저를 깨워주시면 즉시 응대를 시작하겠습니다.`,
    time: getCurrentTime(),
}

const state = {
    routerParameter,
    lastFocusPopup: { name: '', type: '' },
    currentMode: constants.nia.chatbotMode.questionMode,
    questionMode_chatMessages: [_.cloneDeep(defaultQuestionModeChatMessages)],
    alarmFocusMode_chatMessages: [getDefaultAlarmFocusModeFirstChatMessages()],
    alarmFocusTicketData: {}, // 현재 선택된 경보의 ticket 정보
    alarmFocusSopDataList: [],
    actionType: constants.nia.chatbotActiontype.assist
}

async function loadTicketData(focusTicketData) {
    return focusTicketData

    // if (focusTicketData.ticket_type === 'SYSLOG') {
    //     return await apiIpAlarmList({ ALARMNO: focusTicketData.alarmno })
    // } else {
    //     return await apiIpAlarmList({ TICKET_ID: focusTicketData.ticket_id })
    // }
}

const mutations = {
    SET_LAST_FOCUS_MODULE(state, { name, type }) {
        if (name === 'chatbot') return
        if (state.lastFocusPopup.name === name && state.lastFocusPopup.type === type) return

        state.lastFocusPopup.name = name
        state.lastFocusPopup.type = type
    },

    SWTICH_ACTION(state) {
        switch (state.actionType) {
            case constants.nia.chatbotActiontype.expert:
                state.actionType = constants.nia.chatbotActiontype.assist
                break
            case constants.nia.chatbotActiontype.assist:
                state.actionType = constants.nia.chatbotActiontype.expert
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

                if (content !== searchMessaging) {
                    apiInsertChatbotHistory({
                        user_id: store.state.user.info.uid,
                        reg_date: moment().toISOString(),
                        chat_content: content,
                        chat_type: type,
                        chatbot_action_type: state.actionType
                    })
                }
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

    async SET_ALARM_FOCUS_CHAT_TICKET_DATA(state, { ticketData }) {
        state.alarmFocusTicketData = await loadTicketData(ticketData)

        let res
        if (state.alarmFocusTicketData.ticket_type === 'SYSLOG') {
            res = await apiSopSyslogHistList({ NODE_NM: ticketData.node_nm })
        } else {
            res = await apiSelectSopHistList({ NODE_NM: ticketData.node_nm })
        }

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
        if (state.currentMode === constants.nia.chatbotMode.questionMode) {
            // 현재 질문 모드는 입력되는 것을 막아놓음
            return
        }
        commit('PUSH_CHAT_MESSAGE', {
            content, type: isAlert ? constants.nia.chatType.botAlert : constants.nia.chatType.botAnswer,
            callBack: callBack
        })
    },

    newAlarmFocusChat({ commit }, { ticketData }) {
        commit('MODE_CHANGE', { newMode: 'alarmFocusMode' })
        commit('RESET_CHAT')
        commit('SET_ALARM_FOCUS_CHAT_TICKET_DATA', { ticketData })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

