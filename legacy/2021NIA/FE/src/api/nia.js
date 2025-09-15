import { AppOptions } from '@/class/appOptions'
import http from '@/min/http'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

export function apiSystemMonitoring(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSTEM_MONITORING_CURRENT',
    data: params
  })
}
export function apiSendMQ(serviceName, params = {}) {
  return http({
    url: `/nia/send/${serviceName}`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}
export function apiRemote(remoteType, params = {}) {
  return http({
    url: `/nia/remote/${remoteType}`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}
export function apiIpsdnRequest(params = {}) {
  return http({
    url: `/nia/ipsdn`,
    method: 'post',
    filePath: filePath,
    sqlId: '',
    data: params
  })
}
export function apiIpAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_IP_ALARM_LIST',
    data: params
  })
}
export function apiTransmissionAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TRANSMISSION_ALARM_LIST',
    data: params
  })
}
export function apiTransmissionTicketDetail(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TRANSMISSION_TICKET_DETAIL',
    data: params
  })
}
export function apiDashboardStatistics(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DASHBOARD_STATISTICS',
    data: params
  })
}
export function apiSelfProcessStatistics(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SELF_PROCESS_STATISTICS',
    data: params
  })
}
export function apiSelfProcessList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SELF_PROCESS_LIST',
    data: params
  })
}
export function apiSyslogInterfaceList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSLOG_INTERFACE_LIST',
    data: params
  })
}
export function apiSelfProcessTrafficInfo(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SELF_PROCESS_TRAFFIC_INFO',
    data: params
  })
}
export function apiSelfProcessSyslogInfo(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SELF_PROCESS_SYSLOG_INFO',
    data: params
  })
}
export function apiATTTrafficChart(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_ATT2_CHART',
    data: params
  })
}
export function apiNTTTrafficChart(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NTT_CHART',
    data: params
  })
}
export function apiSelectAiDetectionInfo(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AI_DETECTION_INFO',
    data: params
  })
}
export function apiSopSyslogHistList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SOP_SYSLOG_HIST_LIST',
    data: params
  })
}
/*  */
export function apiUpdateSyslogHist(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_SYSLOG_HIST',
    data: params
  })
}
export function apiDeleteSyslogHist(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_SYSLOG_HIST',
    data: params
  })
}
/*  */
export function apiSelectSopHistList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SOP_HIST_LIST',
    data: params
  })
}
export function apiUpdateSopHist(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_SOP_HIST',
    data: params
  })
}
export function apiDeleteSopHist(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_SOP_HIST',
    data: params
  })
}
export function apiAlarmCurAndHistList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_ALARM_CURRENT_HISTORY_LIST',
    data: params
  })
}

export function apiSyslogHistList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSLOG_HIST_LIST',
    data: params
  })
}

export function apiSyslogEquipmentList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSLOG_EQUIPMENT_LIST',
    data: params
  })
}

export function apiSelectPortList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PORT_LIST',
    data: params
  })
}

export function apiSyslogRuleList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSLOG_RULE_LIST',
    data: params
  })
}

export function apiUpdateSyslogRule(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_NIA_SYSLOG_RULE',
    data: params
  })
}

export function apiDeleteSyslogRule(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_NIA_SYSLOG_RULE',
    data: params
  })
}

export function apiInsertSyslogRule(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_NIA_SYSLOG_RULE',
    data: params
  })
}

export function apiUpdateNodePosition(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_NODE_POSITION',
    data: params
  })
}

export function apiSelectCheckRuleName(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_CHECK_RULE_NAME',
    data: params
  })
}

export function apiSelectSopCode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SOP_CODE_LIST',
    data: params
  })
}

export function apiInsertSop(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_SOP_CODE',
    data: params
  })
}
export function apiUpdateSop(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_SOP_CODE',
    data: params
  })
}
export function apiDeleteSop(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_SOP_CODE',
    data: params
  })
}

export function apiTrafficAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TRAFFIC_AGENCY_LIST',
    data: params
  })
}

export function apiSelectAgencyCodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_CODE_LIST',
    data: params
  })
}

export function apiSelectAppTrafficList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_APP_TRAFFIC_TOP_LIST',
    data: params
  })
}

export function apiApplicationCodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_APPLICATION_CODE_LIST',
    data: params
  })
}

export function apiSelectInOutTrafficList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_INOUT_TRAFFIC_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedNationList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_NATION_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_AGENCY_LIST',
    data: params
  })
}

export function apiSelectUnidentifiedAppList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_UNIDENTIFIED_APP_LIST',
    data: params
  })
}

export function apiUpdateAppIpList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_APP_IP',
    data: params
  })
}

export function apiInsertAppIp(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_APP_IP',
    data: params
  })
}

export function apiDeleteAppIpList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_APP_IP',
    data: params
  })
}

export function apiSelectEquipAmountUsedList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_EQUIP_AMOUNT_USED_LIST',
    data: params
  })
}

export function apiSelectProfileList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PROFILE_LIST',
    data: params
  })
}

export function apiInsertProfileListProc(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'insertProfileList',
    data: params
  })
}

export function apiUpdateProfileList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_PROFILE_LIST',
    data: params
  })
}

export function apiDeleteProfileListProc(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'deleteProfileList',
    data: params
  })
}

export function apiTicketTypeCode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PROFILE_TICKET_TYPE_LIST',
    data: params
  })
}

export function apiAlarmTypeCode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PROFILE_ALARM_TYPE_LIST',
    data: params
  })
}

export function apiProfileNodeCode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PROFILE_NODE_LIST',
    data: params
  })
}

export function apiProfileRecoveryList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_PROFILE_RECOVERY_LIST',
    data: params
  })
}

export function apiInsertProfileRecovery(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_PROFILE_NODE_NAME_LIST',
    data: params
  })
}

export function apiDeleteProfileRecovery(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_PROFILE_NODE_NAME_LIST',
    data: params
  })
}

export function apiSelectNodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NODE_LIST',
    data: params
  })
}

export function apiUpdatePortList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_PORT_LIST',
    data: params
  })
}

export function apiSelectLinkList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_LIST',
    data: params
  })
}

export function apiDeleteLinkList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_LINK_LIST',
    data: params
  })
}
export function apiSelectlinkStartNode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_START_NODE_LIST',
    data: params
  })
}

export function apiSelectlinkEndNode(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_END_NODE_LIST',
    data: params
  })
}

export function apiSelectlinkIfList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_LINK_IF_LIST',
    data: params
  })
}

export function apiInsertLinkList(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_LINK_LIST',
    data: params
  })
}

export function apiUpdateLinkList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_LINK_LIST',
    data: params
  })
}

export function apiSelectAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_LIST',
    data: params
  })
}

export function apiSelectAgencyNodeIdList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_NODE_ID_LIST',
    data: params
  })
}

export function apiSelectAgencyIfIdList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_IF_ID_LIST',
    data: params
  })
}

export function apiSelectAgencyIpList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AGENCY_IP_LIST',
    data: params
  })
}

export function apiInsertAgencyIpList(params = {}) {
  return http({
    url: '/selectOne',
    method: 'post',
    filePath: filePath,
    sqlId: 'INSERT_AGENCY_IP_LIST',
    data: params
  })
}

export function apiUpdateAgencyIpList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_AGENCY_IP_LIST',
    data: params
  })
}

export function apiDeleteAgencyIpList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_AGENCY_IP_LIST',
    data: params
  })
}

export function apiUpdateAgencyDetailList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_AGENCY_DETAIL_LIST',
    data: params
  })
}

export function apiSelectUserList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_USER_LIST',
    data: params
  })
}

export function apiUpdateUserGrantList(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'UPDATE_USER_GRANT_LIST',
    data: params
  })
}

export function apiSelectSnapshotList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_DATA_SNAPSHOT_LIST',
    data: params
  })
}
export function apiDeleteSnapshot(params = {}) {
  return http({
    url: '/modify',
    method: 'post',
    filePath: filePath,
    sqlId: 'DELETE_DATA_SNAPSHOT',
    data: params
  })
}
export function apiSelectNiaAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_ALARM_LIST',
    data: params
  })
}
export function apiSelectAiChartData(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_AI_CHART_DATA',
    data: params
  })
}

export function apiSelectNiaCableAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_CABLE_ALARM_LIST',
    data: params
  })
}

export function apiSelectNiaAbnormalTraffic2List(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_ABNORMAL_TRAFFIC2_LIST',
    data: params
  })
}

export function apiSelectNiaBadTraffic2List(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_BAD_TRAFFIC2_LIST',
    data: params
  })
}

export function apiSelectNiaAbnormalTrafficList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_ABNORMAL_TRAFFIC_LIST',
    data: params
  })
}
export function apiSelectSyslogAlarmList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_SYSLOG_ALARM_LIST',
    data: params
  })
}

export function apiSelectNiaTopologyCableList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_TOPOLOGY_CABLE_LIST',
    data: params
  })
}

export function apiSelectNiaPfTopologyCableList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_PF_TOPOLOGY_CABLE_LIST',
    data: params
  })
}

export function apiSelectNiaAgencyList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_NIA_AGENCY_LIST',
    data: params
  })
}

export function apiSelectTopologyNodeList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TOPOLOGY_NODE_LIST',
    data: params
  })
}

export function apiSelectTopologyLinkList(params = {}) {
  return http({
    url: '/selectList',
    method: 'post',
    filePath: filePath,
    sqlId: 'SELECT_TOPOLOGY_LINK_LIST',
    data: params
  })
}
