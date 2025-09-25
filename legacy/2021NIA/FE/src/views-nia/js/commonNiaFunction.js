import { niaRoute } from '@/router/nia/index'
import router from '@/router'
import { apiIpAlarmList } from '@/api/nia'
import store from '@/store'
import axios from 'axios'
import { searchMessaging, errorMessaging1, errorMessaging2, errorMessaging3 } from '@/store/modules/chatbot.js'
import constants from '@/min/constants'

export function getInvisibleSpanParameter(routerPath, popupDialogName, action) {
    return `<span style="display:none">[path]:${routerPath}, [popup]:${popupDialogName}, [action]:${action}</span>`
}

export function getNiaRouterPathByName(routerName, path = '', routes = niaRoute) {
    for (const route of routes) {
        const slashRoutePath = route.path.startsWith('/') ? route.path : '/' + route.path

        if (route.name === routerName) {
            return path + slashRoutePath
        } else {
            if (route.children) {
                const routerPath = getNiaRouterPathByName(routerName, path + slashRoutePath, route.children)
                if (routerPath) {
                    return routerPath
                }
            }
        }
    }

    return null
}

export function getNiaRouteNameByPath(path, routes = niaRoute, prefix = '') {
    for (const route of routes) {
        // 1. 현재 라우트의 path와 일치하는지 확인
        if (prefix + route.path === path) {
            return route.name
        }

        // 2. children이 있는지 확인하고 재귀적으로 탐색
        if (route.children) {
            const foundName = getNiaRouteNameByPath(path, route.children, route.path + '/')
            // 자식 라우트에서 이름이 발견되면 즉시 반환
            if (foundName) {
                return foundName
            }
        }
    }

    // 모든 라우트를 탐색했지만 일치하는 것을 찾지 못한 경우
    return null
}

export function getNiaRouteTitleByPath(path, routes = niaRoute, prefix = '') {
    for (const route of routes) {
        // 1. 현재 라우트의 path와 일치하는지 확인
        if (prefix + route.path === path) {
            return route.meta.title
        }

        // 2. children이 있는지 확인하고 재귀적으로 탐색
        if (route.children) {
            const foundTitle = getNiaRouteTitleByPath(path, route.children, route.path + '/')
            // 자식 라우트에서 이름이 발견되면 즉시 반환
            if (foundTitle) {
                return foundTitle
            }
        }
    }

    // 모든 라우트를 탐색했지만 일치하는 것을 찾지 못한 경우
    return null
}

function isModal(wdata) {
    return !!wdata.params
}

function isChatbotGenerated(wdata) {
    return wdata.params && wdata.params.isChatbotGenerated
}

export async function getAlarmFocusTicketData(wdata) {
    if (!isModal(wdata) || !isChatbotGenerated(wdata)) {
        return
    }

    const currentMode = store.state.chatbot.currentMode
    if (currentMode === 'alarmFocusMode') {
        const ticket_id = store.state.chatbot.alarmFocusTicketData.ticket_id
        const res = await apiIpAlarmList({ TICKET_ID: ticket_id })
        if (res) {
            const ticketData = res.result[0]
            ticketData.ticket_id = ticket_id
            return ticketData
        }
    } else {
        return
    }
}

function isCurrentRouterDashboard() {
    return router.history.current.name === 'NiaMain'
}

function getFilter() {
    console.log('filterRouter : ', router)
    if (isCurrentRouterDashboard()) {
        return {
            bool: { must_not: [{ term: { 'popup.keyword': '' } }] }
        }
    } else {
        return {
            bool: { must: [{ term: { 'popup.keyword': '' } }] }
        }
    }
}

export async function getSpanFormatMessageForDB(userQuestion) {
    try {
        const esClient = axios.create({
            baseURL: 'http://116.89.191.47:8001/es',
            timeout: 10000,
            headers: { 'Content-Type': 'application/json' },
        })

        const response = await esClient.post('/chatbot_index/_search', {
            query: {
                function_score: {
                    query: {
                        bool: {
                            must: [
                                { match: { keyword: userQuestion } },
                                { term: { 'action.keyword': '' } },
                            ],
                        }
                    },
                    functions: [
                        {
                            filter: getFilter(),
                            weight: 3
                        }
                    ],
                    score_mode: 'sum'
                }
            }
        })

        console.log('score Log : ', response.data)
        return getSpanFormatMessage(response, '검색 결과를 찾았습니다\n\n', { showScore: true })
    } catch (error) {
        console.error('ElasticSearch 검색 오류:', error)
        throw error
    }
}

export async function getWindowActionList(dialogNm, popupName) {
    try {
        const esClient = axios.create({
            baseURL: 'http://116.89.191.47:8001/es',
            timeout: 10000,
            headers: { 'Content-Type': 'application/json' },
        })

        const response = await esClient.post('/chatbot_index/_search', {
            query: {
                bool: {
                    must: [
                        { match: { popup: dialogNm } },
                        { exists: { field: 'action' } }
                    ],
                    must_not: [
                        { term: { 'action.keyword': '' } }
                    ]
                }
            }
        })

        return getSpanFormatMessage(response, `<b>${popupName} 화면에서 활용가능한 명령어입니다.</b>\n\n`, { showScore: false })
    } catch (error) {
        console.error('ElasticSearch 검색 오류:', error)
        throw error
    }
}

export function showNumberText(number, text) {
    return `${number}. ${text}`
}

function getSpanFormatMessage(response, messagePrefix, customObj = {}) {
    const data = response.data
    if (data.hits && data.hits.hits && data.hits.hits.length > 0) {
        const hits = data.hits.hits
        let resultMessage = messagePrefix

        const currentMode = store.state.chatbot.currentMode
        hits.forEach((hit, index) => {
            const source = hit._source
            const hiddenParameter = getInvisibleSpanParameter(source.path, source.popup, source.action)

            resultMessage += `${index + 1}. ${source.name}`
            if (customObj.showScore) {
                resultMessage += ` <b>(${Number(hit._score).toFixed(1)}점)</b>`
            }

            if (currentMode === 'questionMode' && source.popup.length > 0 && source.action.length === 0) {
                resultMessage += `${constants.nia.chatbotIcon.popupWarning}`
            }
            resultMessage += hiddenParameter
            resultMessage += '\n'
        })

        return resultMessage
    } else {
        return errorMessaging2
    }
}

export function getMatchMapOfspanFormatMessage(userQuestion, spanFormatMessage) {
    const matchMap = {
        matchContext: '',
        path: '',
        popup: '',
        action: '',
    }

    if (/^\d$/.test(userQuestion)) {
        const pattern = new RegExp(`${userQuestion}\\. (.*?)<span.*?\\[path\\]:(.*?)\\, \\[popup\\]:(.*?)\\, \\[action\\]:(.*?)\\<\\/span\\>`)
        const match = spanFormatMessage.match(pattern)

        if (match) {
            matchMap.matchContext = match[1].trim()
            matchMap.path = match[2].trim()
            matchMap.popup = match[3].trim()
            matchMap.action = match[4].trim()
            return matchMap
        }
    } else if (userQuestion.length > 0) {
        const pattern = new RegExp(`\\d\\. .*?<span.*?\\[path\\]:.*?\\, \\[popup\\]:.*?\\, \\[action\\]:.*?\\<\\/span\\>`, 'g')
        const matchs = spanFormatMessage.match(pattern)

        if (matchs) {
            const fullString = matchs.find((matchText) => {
                const pattern = new RegExp(`\\d\\. (.*?)<span.*?\\[path\\]:(.*?)\\, \\[popup\\]:(.*?)\\, \\[action\\]:(.*?)\\<\\/span\\>`)
                const textMatch = matchText.match(pattern)
                return textMatch && textMatch[1].replace(/\s/g, '').toLowerCase() === userQuestion.replace(/\s/g, '').toLowerCase()
            })

            if (fullString) {
                const pattern = new RegExp(`\\d\\. (.*?)<span.*?\\[path\\]:(.*?)\\, \\[popup\\]:(.*?)\\, \\[action\\]:(.*?)\\<\\/span\\>`)
                const lastMatch = fullString.match(pattern)

                matchMap.matchContext = lastMatch[1].trim()
                matchMap.path = lastMatch[2].trim()
                matchMap.popup = lastMatch[3].trim()
                matchMap.action = lastMatch[4].trim()
                return matchMap
            }
        }
    }

    return matchMap
}

