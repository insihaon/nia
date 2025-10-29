import store from '@/store'

const rt_alarm1 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144093', 'alarmno': 'I25454', 'if_num': null, 'fault_time': null, 'node_nm': 'daejeon-n9k', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'daejeon-n9k', 'ticket_id': '1671777', 'ai_accuracy': null, 'port': '1720408774278', 'alarmtime': '2025-10-28 12:02:46', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - (436221440)Ethernet1/28#436221440', 'node_num': '1623828015475', 'ip_addr': '116.89.161.24', 'alarmloc': 'Ethernet1/28', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const rt_alarm2 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144084', 'alarmno': 'I25453', 'if_num': null, 'fault_time': null, 'node_nm': 'pangyo-5812', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'pangyo-5812', 'ticket_id': '1671775', 'ai_accuracy': null, 'port': '1720139332303', 'alarmtime': '2025-10-24 22:37:54', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - (10003)xe3#10003', 'node_num': '1623913556405', 'ip_addr': '116.89.169.33', 'alarmloc': 'xe3', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const rt_alarm3 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144082', 'alarmno': 'I25452', 'if_num': null, 'fault_time': null, 'node_nm': 'pangyo-n9k', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'pangyo-n9k', 'ticket_id': '1671773', 'ai_accuracy': null, 'port': '1720414747478', 'alarmtime': '2025-10-24 21:16:24', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - MGT_Eth1/42_Opensysnet판교수집서버_1G#436228608', 'node_num': '1623907683109', 'ip_addr': '116.89.161.84', 'alarmloc': 'Ethernet1/42', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const rt_alarm4 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144080', 'alarmno': 'I25451', 'if_num': null, 'fault_time': null, 'node_nm': 'daejeon-n9k', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'daejeon-n9k', 'ticket_id': '1671771', 'ai_accuracy': null, 'port': '1720408774278', 'alarmtime': '2025-10-24 06:58:05', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - (436221440)Ethernet1/28#436221440', 'node_num': '1623828015475', 'ip_addr': '116.89.161.24', 'alarmloc': 'Ethernet1/28', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const rt_alarm5 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144078', 'alarmno': 'I25450', 'if_num': null, 'fault_time': null, 'node_nm': 'jeju-n9k', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'jeju-n9k', 'ticket_id': '1671769', 'ai_accuracy': null, 'port': '1720414854582', 'alarmtime': '2025-10-22 17:38:43', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - INTER_Eth1/2_제주JejuSAN_10G#436208128', 'node_num': '1720142160035', 'ip_addr': '116.89.161.74', 'alarmloc': 'Ethernet1/2', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const rt_alarm6 = { 'ticket_type': 'RT', 'root_cause_sysnamez': null, 'clusterno': '144076', 'alarmno': 'I25449', 'if_num': null, 'fault_time': null, 'node_nm': 'jeju-n9k', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': 'PORT 다운', 'alarmmsg': 'PORT_DOWN', 'ticket_rca_result_code': 'SWITCH_PORT_DOWN', 'fault_type': 'SwitchFail', 'root_cause_sysnamea': 'jeju-n9k', 'ticket_id': '1671767', 'ai_accuracy': null, 'port': '1720414854582', 'alarmtime': '2025-10-22 11:43:58', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'port down - INTER_Eth1/2_제주JejuSAN_10G#436208128', 'node_num': '1720142160035', 'ip_addr': '116.89.161.74', 'alarmloc': 'Ethernet1/2', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const syslog_alarm1 = { 'ticket_type': 'SYSLOG', 'root_cause_sysnamez': null, 'clusterno': null, 'alarmno': '397753162', 'if_num': null, 'fault_time': null, 'node_nm': 'seoul-s9510-1', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': null, 'alarmmsg': 'IFMGR_IF_UP_4', 'ticket_rca_result_code': null, 'fault_type': null, 'root_cause_sysnamea': null, 'ticket_id': null, 'ai_accuracy': null, 'port': null, 'alarmtime': '2025-10-28 13:51:20', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'Interface xe23 changed state to up', 'node_num': '1737605079007', 'ip_addr': '116.89.169.17', 'alarmloc': 'xe23', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const syslog_alarm2 = { 'ticket_type': 'SYSLOG', 'root_cause_sysnamez': null, 'clusterno': null, 'alarmno': '397725868', 'if_num': null, 'fault_time': null, 'node_nm': 'seoul-s9510-1', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': null, 'alarmmsg': 'IFMGR_IF_UP_4', 'ticket_rca_result_code': null, 'fault_type': null, 'root_cause_sysnamea': null, 'ticket_id': null, 'ai_accuracy': null, 'port': null, 'alarmtime': '2025-10-28 13:49:28', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'Interface xe25 changed state to up', 'node_num': '1737605079007', 'ip_addr': '116.89.169.17', 'alarmloc': 'xe25', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const syslog_alarm3 = { 'ticket_type': 'SYSLOG', 'root_cause_sysnamez': null, 'clusterno': null, 'alarmno': '397705927', 'if_num': null, 'fault_time': null, 'node_nm': 'seoul-s9510-1', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': null, 'alarmmsg': 'IFMGR_IF_DOWN_2', 'ticket_rca_result_code': null, 'fault_type': null, 'root_cause_sysnamea': null, 'ticket_id': null, 'ai_accuracy': null, 'port': null, 'alarmtime': '2025-10-28 13:48:04', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'Interface xe23 changed state to down', 'node_num': '1737605079007', 'ip_addr': '116.89.169.17', 'alarmloc': 'xe23', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const syslog_alarm4 = { 'ticket_type': 'SYSLOG', 'root_cause_sysnamez': null, 'clusterno': null, 'alarmno': '397705392', 'if_num': null, 'fault_time': null, 'node_nm': 'seoul-s9510-1', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': null, 'alarmmsg': 'IFMGR_IF_DOWN_2', 'ticket_rca_result_code': null, 'fault_type': null, 'root_cause_sysnamea': null, 'ticket_id': null, 'ai_accuracy': null, 'port': null, 'alarmtime': '2025-10-28 13:48:02', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'Interface xe25 changed state to down', 'node_num': '1737605079007', 'ip_addr': '116.89.169.17', 'alarmloc': 'xe25', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const syslog_alarm5 = { 'ticket_type': 'SYSLOG', 'root_cause_sysnamez': null, 'clusterno': null, 'alarmno': '397445340', 'if_num': null, 'fault_time': null, 'node_nm': 'seoul-s9510-1', 'root_cause_porta': null, 'ticket_rca_result_dtl_code': null, 'alarmmsg': 'IFMGR_IF_UP_4', 'ticket_rca_result_code': null, 'fault_type': null, 'root_cause_sysnamea': null, 'ticket_id': null, 'ai_accuracy': null, 'port': null, 'alarmtime': '2025-10-28 13:30:11', 'root_cause_portz': null, 'zero1_entropy': null, 'alarmmsg_original': 'Interface xe23 changed state to up', 'node_num': '1737605079007', 'ip_addr': '116.89.169.17', 'alarmloc': 'xe23', 'total_related_alarm_cnt': null, 'status': 'AUTO_FIN' }
const att_alarm1 = { 'ticket_type': 'ATT2', 'root_cause_sysnamez': '대전-광주', 'clusterno': null, 'alarmno': null, 'if_num': '1720413434118', 'fault_time': '2025-09-29 17:35:00', 'node_nm': 'daejeon-7712', 'root_cause_porta': 'ce4/1', 'ticket_rca_result_dtl_code': '이상 트래픽(이용기관)', 'alarmmsg': 'TrafficFail', 'ticket_rca_result_code': null, 'fault_type': 'TrafficFail', 'root_cause_sysnamea': 'daejeon-7712', 'ticket_id': '1671658', 'ai_accuracy': '0', 'port': null, 'alarmtime': '2025-09-29 18:25:22', 'root_cause_portz': 'ce4/1', 'zero1_entropy': 0.0008064021, 'alarmmsg_original': null, 'node_num': '1623913471006', 'ip_addr': '116.89.169.21', 'alarmloc': 'ce4/1', 'total_related_alarm_cnt': 1, 'status': 'AUTO_FIN' }
const att_alarm2 = { 'ticket_type': 'ATT2', 'root_cause_sysnamez': '광주-대전', 'clusterno': null, 'alarmno': null, 'if_num': '1720141987296', 'fault_time': '2025-09-30 09:34:00', 'node_nm': 'gwangju-7712', 'root_cause_porta': 'ce2/1', 'ticket_rca_result_dtl_code': '이상 트래픽(이용기관)', 'alarmmsg': 'TrafficFail', 'ticket_rca_result_code': null, 'fault_type': 'TrafficFail', 'root_cause_sysnamea': 'gwangju-7712', 'ticket_id': '1671659', 'ai_accuracy': '0', 'port': null, 'alarmtime': '2025-09-30 09:40:43', 'root_cause_portz': 'ce2/1', 'zero1_entropy': 0.2324228527, 'alarmmsg_original': null, 'node_num': '1720140269083', 'ip_addr': '116.89.169.41', 'alarmloc': 'ce2/1', 'total_related_alarm_cnt': 1, 'status': 'AUTO_FIN' }
const att_alarm3 = { 'ticket_type': 'ATT2', 'root_cause_sysnamez': '대전-광주', 'clusterno': null, 'alarmno': null, 'if_num': '1720413434118', 'fault_time': '2025-09-30 09:34:00', 'node_nm': 'daejeon-7712', 'root_cause_porta': 'ce4/1', 'ticket_rca_result_dtl_code': '이상 트래픽(이용기관)', 'alarmmsg': 'TrafficFail', 'ticket_rca_result_code': null, 'fault_type': 'TrafficFail', 'root_cause_sysnamea': 'daejeon-7712', 'ticket_id': '1671660', 'ai_accuracy': '0', 'port': null, 'alarmtime': '2025-09-30 09:40:46', 'root_cause_portz': 'ce4/1', 'zero1_entropy': 0.2750223166, 'alarmmsg_original': null, 'node_num': '1623913471006', 'ip_addr': '116.89.169.21', 'alarmloc': 'ce4/1', 'total_related_alarm_cnt': 1, 'status': 'AUTO_FIN' }

function nowDateTime() {
    const date = new Date()

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0') // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms || 5000))

async function unshiftFunction(alarm, param) {
    const simulationStatus = store.state.chatbot.simulationStatus
    if (simulationStatus === 'ON') {
        this.ipNetworkList.unshift(Object.assign(alarm, { status: 'INIT', alarmtime: nowDateTime(), ticket_id: alarm.ticket_id ? param.maxTicketId++ : '' }))
        await delay(param?.injectionDelay)
    } else {
        return
    }
}

// --------------------------------

export async function niaSimulationStart(param) {
    // [고성호] 시연 테스트용

    const simulationStatus = store.state.chatbot.simulationStatus

    if (simulationStatus === 'ON') {
        store.commit('chatbot/SWITCH_SIMULATION_STATUS')
    } else {
        store.commit('chatbot/SWITCH_SIMULATION_STATUS')
        // prettier-ignore
        await (injectionTableData.bind(this))(param)
    }
}

async function injectionTableData(param = {}) {
    const maxTicketObject = this.ipNetworkList.reduce((max, current) => {
        if (!max || current.ticket_id > max.ticket_id) { return current }
        return max
    }, undefined)
    param.maxTicketId = maxTicketObject.ticket_id

    await (unshiftFunction.bind(this))(syslog_alarm1, param)
    await (unshiftFunction.bind(this))(rt_alarm1, param)
    await (unshiftFunction.bind(this))(att_alarm1, param)

    await (unshiftFunction.bind(this))(syslog_alarm2, param)
    await (unshiftFunction.bind(this))(rt_alarm2, param)
    await (unshiftFunction.bind(this))(att_alarm2, param)

    await (unshiftFunction.bind(this))(syslog_alarm3, param)
    await (unshiftFunction.bind(this))(rt_alarm3, param)
    await (unshiftFunction.bind(this))(att_alarm3, param)

    await (unshiftFunction.bind(this))(rt_alarm4, param)
    await (unshiftFunction.bind(this))(rt_alarm5, param)
    await (unshiftFunction.bind(this))(rt_alarm6, param)

    await (unshiftFunction.bind(this))(syslog_alarm4, param)
    await (unshiftFunction.bind(this))(syslog_alarm5, param)
}

