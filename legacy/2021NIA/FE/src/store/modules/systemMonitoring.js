// import moment from 'moment'
import moment from 'moment'
import _ from 'lodash'
import { AppOptions } from '@/class/appOptions'
import Encrypt from '@/assets/libs/Encrypt.min'

const systemMonitoringMapDefault = {
    'LinkTraffic': { key: 'ipSdnTrafficeKey', show: true },
    'Sflow': { key: 'ipSdnSflowKey', show: true },
    'Syslog': { key: 'ipSdnSyslogKey', show: true },
    'NodeFactor': { key: 'ipSdnNodeFactorKey', show: true },
    'AI_Traffic_유해': { key: 'aiTrafficNoxKey', show: true },
    'AI_Traffic_이상': { key: 'aiTrafficAnoKey', show: true },
    'IPSDN_Perf': { key: 'ipPerfKey', show: true },
    'IPSDN_ResourceIf': { key: 'ipResourceIfKey', show: true },
    'IPSDN_Resource': { key: 'ipResourceKey', show: true },
    'EMS_SIPC': { key: 'emsSipcKey', show: false, hide: true },
    'EMS_PM': { key: 'emsPmKey', show: false, hide: true },
}

const state = {
    systemMonitoringMap: _.cloneDeep(systemMonitoringMapDefault),

    niaProcess: {
        ipSdnTrafficeKey: {
            name: 'LinkTraffic', status: 'red', cycle: 5 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpSdnTrafficeKey',
                cycle: 5 * 60 * 1000,
            },
        },
        ipSdnSflowKey: {
            name: 'Sflow', status: 'red', cycle: 10 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpSdnSflowKey',
                cycle: 10 * 60 * 1000,
            },
        },
        ipSdnSyslogKey: {
            name: 'Syslog', status: 'red', cycle: 10 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpSdnSyslogKey',
                cycle: 10 * 60 * 1000,
            },
        },
        ipSdnNodeFactorKey: {
            name: 'NodeFactor', status: 'red', cycle: 10 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpSdnNodeFactorKey',
                cycle: 10 * 60 * 1000,
            },
        },
        aiTrafficNoxKey: {
            name: 'AI_Traffic_유해', status: 'red', cycle: 5 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
        },
        aiTrafficAnoKey: {
            name: 'AI_Traffic_이상', status: 'red', cycle: 5 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
        },
        ipPerfKey: {
            name: 'IPSDN_Perf', status: 'red', cycle: 10 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpPerfKey',
                cycle: 10 * 60 * 1000,
            },
        },
        ipResourceIfKey: {
            name: 'IPSDN_ResourceIf', status: 'red', cycle: moment().set({ hour: 0, minute: 15, second: 0, millisecond: 0 }),
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패 <br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpResourceIfKey',
                cycle: moment().set({ hour: 2, minute: 40, second: 0, millisecond: 0 })
            },
        },
        ipResourceKey: {
            name: 'IPSDN_Resource', status: 'red', cycle: moment().set({ hour: 0, minute: 5, second: 0, millisecond: 0 }),
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            secondStep: {
                key: 'aiIpResourceKey',
                cycle: moment().set({ hour: 2, minute: 30, second: 0, millisecond: 0 })
            },
        },
        emsSipcKey: {
            name: 'EMS_SIPC', status: 'red', cycle: 18 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : EMS SIPC 수집 실패<br>
                <span style="color:lime">초록</span> : EMS SIPC 수집 성공
            </div>
            `,
        },
        emsPmKey: {
            name: 'EMS_PM', status: 'red', cycle: 18 * 60 * 1000,
            firstTime: '', secondTime: '', secondSycle: '',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : EMS PM 수집 실패<br>
                <span style="color:lime">초록</span> : EMS PM 수집 성공
            </div>
            `,
        },
    },
}

const mutations = {
    SWITCH_SHOW_STATE(state, systemMonitoringKey) {
        const originVal = state.systemMonitoringMap[systemMonitoringKey].show
        state.systemMonitoringMap[systemMonitoringKey].show = !originVal

        const lastUserInfo = Encrypt.toDecrypt(AppOptions.instance.lastUser)
        if (lastUserInfo) {
            localStorage.setItem('systemMonitoring_systemMonitoringMap' + '_' + lastUserInfo.id, JSON.stringify(state.systemMonitoringMap))
        }
    },
    SET_NIA_PROCESS(state, { processKey, mapKey, value }) {
        state.niaProcess[processKey][mapKey] = value
    },
    RESET_SYSTEM_MONITORING_MAP(state) {
        state.systemMonitoringMap = _.cloneDeep(systemMonitoringMapDefault)
    },
    RECOVERY_SYSTEM_MONITORING_MAP(state, user_systemMonitoringMap) {
        state.systemMonitoringMap = user_systemMonitoringMap
    }
}

const actions = {
    switchShowState({ commit }, systemMonitoringKey) {
        commit('SWITCH_SHOW_STATE', systemMonitoringKey)
    },

    setNiaProcess({ commit }, { processKey, mapKey, value }) {
        commit('SET_NIA_PROCESS', { processKey, mapKey, value })
    },

    checkSaveState({ commit }) {
        const lastUserInfo = Encrypt.toDecrypt(AppOptions.instance.lastUser)
        if (lastUserInfo) {
            const user_systemMonitoringMap = JSON.parse(localStorage.getItem('systemMonitoring_systemMonitoringMap' + '_' + lastUserInfo.id))
            if (user_systemMonitoringMap) {
                commit('RECOVERY_SYSTEM_MONITORING_MAP', user_systemMonitoringMap)
            } else {
                commit('RESET_SYSTEM_MONITORING_MAP')
            }
        } else {
            console.error('lastUserInfo가 없음')
        }
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
