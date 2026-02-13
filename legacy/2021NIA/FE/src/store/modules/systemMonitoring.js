// import moment from 'moment'
import moment from 'moment'
import _ from 'lodash'
import { AppOptions } from '@/class/appOptions'
import Encrypt from '@/assets/libs/Encrypt.min'

const systemMonitoringMapDefault = {
    'LinkTraffic': { key: 'ipSdnTrafficeKey', show: true, group: '수집/전송' },
    'Sflow': { key: 'ipSdnSflowKey', show: true, group: '수집/전송' },
    'Syslog': { key: 'ipSdnSyslogKey', show: true, group: '수집/전송' },
    'NodeFactor': { key: 'ipSdnNodeFactorKey', show: true, group: '수집/전송' },
    'IPSDN_Perf': { key: 'ipPerfKey', show: true, group: '수집/전송' },
    'IPSDN_ResourceIf': { key: 'ipResourceIfKey', show: true, group: '수집/전송' },
    'IPSDN_Resource': { key: 'ipResourceKey', show: true, group: '수집/전송' },
    'EMS_SIPC': { key: 'emsSipcKey', show: false, hide: true, group: '수집/전송' },
    'EMS_PM': { key: 'emsPmKey', show: false, hide: true, group: '수집/전송' },
    '유해 Taffic AI - A': { key: 'aiTrafficNoxKey', show: true, group: 'AI연동' },
    '유해 Taffic AI - C': { key: 'aiTrafficNoxKey2', show: true, group: 'AI연동' },
    '이상 Taffic AI - A': { key: 'aiTrafficAnoKey', show: true, group: 'AI연동' },
    '이상 Taffic AI - C': { key: 'aiTrafficAnoKey2', show: true, group: 'AI연동' },
}

const state = {
    systemMonitoringMap: _.cloneDeep(systemMonitoringMapDefault),

    niaProcess: {
        ipSdnTrafficeKey: {
            name: 'LinkTraffic', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipSdnTrafficeKey', time: null, cycle: null },
                { key: 'aiIpSdnTrafficeKey', time: null, cycle: null },
            ],
        },
        ipSdnSflowKey: {
            name: 'Sflow', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipSdnSflowKey', time: null, cycle: null },
                { key: 'aiIpSdnSflowKey', time: null, cycle: null },
            ],
        },
        ipSdnSyslogKey: {
            name: 'Syslog', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipSdnSyslogKey', time: null, cycle: null },
                { key: 'aiIpSdnSyslogKey', time: null, cycle: null },
            ],
        },
        ipSdnNodeFactorKey: {
            name: 'NodeFactor', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipSdnNodeFactorKey', time: null, cycle: null },
                { key: 'aiIpSdnNodeFactorKey', time: null, cycle: null },
            ],
        },
        aiTrafficNoxKey: {
            name: '유해 Taffic AI - A', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span>AIF 유해트래픽 AI모델 연동 상태</span><br><br>
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            steps: [
                { key: 'aiTrafficNoxKey', time: null, cycle: null },
            ],
        },
        aiTrafficAnoKey: {
            name: '이상 Taffic AI - A', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span>AIF 이상트래픽 AI모델 연동 상태</span><br><br>
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            steps: [
                { key: 'aiTrafficAnoKey', time: null, cycle: null },
            ],
        },
        aiTrafficNoxKey2: {
            name: '유해 Taffic AI - C', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span>Codej 유해트래픽 AI모델 연동 상태</span><br><br>
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            steps: [
                { key: 'aiTrafficNoxKey2', time: null, cycle: null },
            ],
        },
        aiTrafficAnoKey2: {
            name: '이상 Taffic AI - C', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span>Codej 유해트래픽 AI모델 연동 상태</span><br><br>
                <span style="color:red">빨강</span> : AI 연동 실패<br>
                <span style="color:lime">초록</span> : AI 연동 성공
            </div>
            `,
            steps: [
                { key: 'aiTrafficAnoKey2', time: null, cycle: null },
            ],
        },
        ipPerfKey: {
            name: 'IPSDN_Perf', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipPerfKey', time: null, cycle: null },
                { key: 'aiIpPerfKey', time: null, cycle: null },
            ],
        },
        ipResourceIfKey: {
            name: 'IPSDN_ResourceIf', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패 <br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipResourceIfKey', time: null, cycle: null },
                { key: 'aiIpResourceIfKey', time: null, cycle: null },
            ],
        },
        ipResourceKey: {
            name: 'IPSDN_Resource', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : 수집 실패<br>
                <span style="color:orange">주황</span> : SFTP 전송 실패<br>
                <span style="color:lime">초록</span> : 수집/전송 성공
            </div>
            `,
            steps: [
                { key: 'ipResourceKey', time: null, cycle: null },
                { key: 'aiIpResourceKey', time: null, cycle: null },
            ],
        },
        emsSipcKey: {
            name: 'EMS_SIPC', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : EMS SIPC 수집 실패<br>
                <span style="color:lime">초록</span> : EMS SIPC 수집 성공
            </div>
            `,
            steps: [
                { key: 'emsSipcKey', time: null, cycle: null },
            ],
        },
        emsPmKey: {
            name: 'EMS_PM', status: 'red',
            tooltip: `
            <div style="font-size: 13px; text-align: left">
                <span style="color:red">빨강</span> : EMS PM 수집 실패<br>
                <span style="color:lime">초록</span> : EMS PM 수집 성공
            </div>
            `,
            steps: [
                { key: 'emsPmKey', time: null, cycle: null },
            ],
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
    SET_NIA_PROCESS_STEP(state, { processKey, stepIndex, stepData }) {
        if (state.niaProcess[processKey] && state.niaProcess[processKey].steps) {
            state.niaProcess[processKey].steps[stepIndex] = { ...state.niaProcess[processKey].steps[stepIndex], ...stepData }
        }
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

    setNiaProcessStep({ commit }, { processKey, stepIndex, stepData }) {
        commit('SET_NIA_PROCESS_STEP', { processKey, stepIndex, stepData })
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
