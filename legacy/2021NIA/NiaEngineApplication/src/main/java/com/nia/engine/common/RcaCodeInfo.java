package com.nia.engine.common;

public class RcaCodeInfo {

	//ROADM
	public static final String ALARM_ROADM_NVRAM_FAIL = "NVRAM_FAIL";
	public static final String ALARM_ROADM_COW_FAN_M1_FAIL = "COW_FAN_M1_FAIL";
	public static final String ALARM_ROADM_COW_FAN_M2_FAIL = "COW_FAN_M2_FAIL";
	public static final String ALARM_ROADM_COW_FAN_M3_FAIL = "COW_FAN_M3_FAIL";
	public static final String ALARM_ROADM_COW_FAN_M4_FAIL = "COW_FAN_M4_FAIL";
	public static final String ALARM_ROADM_CARD_OUT = "CARD_OUT";
	public static final String ALARM_ROADM_WSS_ERR = "WSS_ERR";
	public static final String ALARM_ROADM_IPC_FAIL = "IPC_FAIL";
	public static final String ALARM_ROADM_PDU_PWR_A_FAIL = "PDU_PWR_A_FAIL";
	public static final String ALARM_ROADM_FAN_PWR_A_FAIL = "FAN_PWR_A_FAIL";
	public static final String ALARM_ROADM_RACK_PWR_A_FAIL = "RACK_PWR_A_FAIL";
	public static final String ALARM_ROADM_PDU_PWR_B_FAIL = "PDU_PWR_B_FAIL";
	public static final String ALARM_ROADM_FAN_PWR_B_FAIL = "FAN_PWR_B_FAIL";
	public static final String ALARM_ROADM_RACK_PWR_B_FAIL = "RACK_PWR_B_FAIL";
	public static final String ALARM_ROADM_HW_FAIL = "HW_FAIL";
	public static final String ALARM_ROADM_DCC_FAIL = "DCC_FAIL";
	public static final String ALARM_ROADM_WDM_LOS = "WDM_LOS";
	public static final String ALARM_ROADM_WDM_PWR_LOW = "WDM_PWR_LOW";
	public static final String ALARM_ROADM_WDM_LOCAL_SD = "WDM_LOCAL_SD";
	public static final String ALARM_ROADM_WDM_PWR_HIGH = "WDM_PWR_HIGH";
	public static final String ALARM_ROADM_UNI_LOCAL_LOS = "UNI_LOCAL_LOS";
	public static final String ALARM_ROADM_UNI_REMOTE_SF = "UNI_REMOTE_SF";
	public static final String ALARM_ROADM_UNI_PWR_LOW = "UNI_PWR_LOW";
	public static final String ALARM_ROADM_UNI_PWR_HIGH = "UNI_PWR_HIGH";

	// POTN
	public static final String ALARM_POTN_NODE_ISOLATION = "NODE_ISOLATION";
	public static final String ALARM_POTN_LINK_UNIT_OUT = "LINK_UNIT_OUT";
	public static final String ALARM_POTN_FAN_UNIT_OUT = "FAN_UNIT_OUT";
	public static final String ALARM_POTN_LOGIC_BOARD_OUT = "LOGIC_BOARD_OUT";
	public static final String ALARM_POTN_SWITCH_FABRIC_UNIT_OUT = "SWITCH_FABRIC_UNIT_OUT";
	public static final String ALARM_POTN_ETH_UNIT_OUT = "ETH_UNIT_OUT";
	public static final String ALARM_POTN_CONTROL_UNIT_OUT = "CONTROL_UNIT_OUT";
	public static final String ALARM_POTN_OTN_UNIT_OUT = "OTN_UNIT_OUT";
	public static final String ALARM_POTN_STM_UNIT_OUT = "STM_UNIT_OUT";
	public static final String ALARM_POTN_LINK_UNIT_FAIL = "LINK_UNIT_FAIL";
	public static final String ALARM_POTN_FAN_UNIT_FAIL = "FAN_UNIT_FAIL";
	public static final String ALARM_POTN_SWITCH_FABRIC_UNIT_FAIL = "SWITCH_FABRIC_UNIT_FAIL";
	public static final String ALARM_POTN_ETH_UNIT_FAIL = "ETH_UNIT_FAIL";
	public static final String ALARM_POTN_PWR_FAIL = "PWR_FAIL";
	public static final String ALARM_POTN_CONTROL_UNIT_FAIL = "CONTROL_UNIT_FAIL";
	public static final String ALARM_POTN_OTN_UNIT_FAIL = "OTN_UNIT_FAIL";
	public static final String ALARM_POTN_SM_UNIT_FAIL = "SM_UNIT_FAIL";
	public static final String ALARM_POTN_STM_UNIT_FAIL = "STM_UNIT_FAIL";
	public static final String ALARM_POTN_UAS_UNIT_FAIL = "UAS_UNIT_FAIL";
	public static final String ALARM_POTN_ETH_LOS = "ETH_LOS";
	public static final String ALARM_POTN_OTN_LOS = "OTN_LOS";
	public static final String ALARM_POTN_STM_LOS = "STM_LOS";
	public static final String ALARM_POTN_WDM_LOS = "WDM_LOS";
	public static final String ALARM_POTN_LOGIN_FAIL = "LOGIN_FAIL";
	public static final String ALARM_POTN_BACKUP_FAIL = "BACKUP_FAIL";
	public static final String ALARM_POTN_IPC_FAIL = "IPC_FAIL";
	public static final String ALARM_POTN_CONTROL_UNIT_INIT = "CONTROL_UNIT_INIT";
	public static final String ALARM_POTN_CONTROL_CHANNEL_FAIL = "CONTROL_CHANNEL_FAIL";
	public static final String ALARM_POTN_CONTROL_UNIT_IPC_FAIL = "CONTROL_UNIT_IPC_FAIL";
	public static final String ALARM_POTN_SYNC_FAIL = "SYNC_FAIL";
	public static final String ALARM_POTN_SYNC_LOS = "SYNC_LOS";
	public static final String ALARM_POTN_ASON_OSPF_TCA = "ASON_OSPF_TCA";
	public static final String ALARM_POTN_CPU_TCA = "CPU_TCA";
	public static final String ALARM_POTN_DCN_FAIL = "DCN_FAIL";
	public static final String ALARM_POTN_GNE_CONNECT_FAIL = "GNE_CONNECT_FAIL";
	public static final String ALARM_POTN_NTP_CONNECTION_FAIL = "NTP_CONNECTION_FAIL";
	public static final String ALARM_POTN_ODU_FAIL = "ODU_FAIL";
	public static final String ALARM_POTN_ODU_UNIT_IPC_FAIL = "ODU_UNIT_IPC_FAIL";
	public static final String ALARM_POTN_OTN_FAIL = "OTN_FAIL";
	public static final String ALARM_POTN_OTN_LOSS_OF_CLK = "OTN_LOSS_OF_CLK";
	public static final String ALARM_POTN_OTN_UNIT_IPC_FAIL = "OTN_UNIT_IPC_FAIL";
	public static final String ALARM_POTN_OTU_FAIL = "OTU_FAIL";
	public static final String ALARM_POTN_RSVP_FAIL = "RSVP_FAIL";
	public static final String ALARM_POTN_SFPP_LOS = "SFPP_LOS";
	public static final String ALARM_POTN_STM_FAIL = "STM_FAIL";
	public static final String ALARM_POTN_STM_LOSS_OF_CLK = "STM_LOSS_OF_CLK";
	public static final String ALARM_POTN_STM_UNIT_IPC_FAIL = "STM_UNIT_IPC_FAIL";
	public static final String ALARM_POTN_TE_LINK_FAIL = "TE_LINK_FAIL";
	public static final String ALARM_POTN_VC3_SIGNAL_FAIL = "VC3_SIGNAL_FAIL";
	public static final String ALARM_POTN_VC4_SIGNAL_FAIL = "VC4_SIGNAL_FAIL";
	public static final String ALARM_POTN_VC4_SD = "VC4_SD";
	public static final String ALARM_POTN_WDM_CLK_FAIL = "WDM_CLK_FAIL";
	public static final String ALARM_POTN_OPTIC_TEMP_HIGH = "OPTIC_TEMP_HIGH";
	public static final String ALARM_POTN_DATA_ERR = "DATA_ERR";
	public static final String ALARM_POTN_LOG_ERR = "LOG_ERR";
	public static final String ALARM_POTN_SECURITY_ERR = "SECURITY_ERR";
	public static final String ALARM_POTN_SERVICE_ERR = "SERVICE_ERR";
	public static final String ALARM_POTN_ETH_ERR = "ETH_ERR";
	public static final String ALARM_POTN_ETH_UNIT_ERR = "ETH_UNIT_ERR";
	public static final String ALARM_POTN_SYNC_ERR = "SYNC_ERR";
	public static final String ALARM_POTN_CLK_ERR = "CLK_ERR";
	public static final String ALARM_POTN_TUNNEL_ERR = "TUNNEL_ERR";
	public static final String ALARM_POTN_PASSWORD_ERR = "PASSWORD_ERR";
	public static final String ALARM_POTN_APS_ERR = "APS_ERR";
	public static final String ALARM_POTN_BUS_ERR = "BUS_ERR";
	public static final String ALARM_POTN_DCN_ERR = "DCN_ERR";
	public static final String ALARM_POTN_OCH_ERR = "OCH_ERR";
	public static final String ALARM_POTN_ODU_ERR = "ODU_ERR";
	public static final String ALARM_POTN_ODU_MSI_CONF_MISMATCH = "ODU_MSI_CONF_MISMATCH";
	public static final String ALARM_POTN_OSRP_ERR = "OSRP_ERR";
	public static final String ALARM_POTN_OTN_OPTIC_CONF_MISMATCH = "OTN_OPTIC_CONF_MISMATCH";
	public static final String ALARM_POTN_OTN_UNIT_TEMP_HIGH = "OTN_UNIT_TEMP_HIGH";
	public static final String ALARM_POTN_OTN_UNIT_ERR = "OTN_UNIT_ERR";
	public static final String ALARM_POTN_OTU_ERR = "OTU_ERR";
	public static final String ALARM_POTN_STM_OPTIC_CONF_MISMATCH = "STM_OPTIC_CONF_MISMATCH";
	public static final String ALARM_POTN_STM_CONF_MISMATCH = "STM_CONF_MISMATCH";
	public static final String ALARM_POTN_STM_ERR = "STM_ERR";
	public static final String ALARM_POTN_STM_MS_ERR = "STM_MS_ERR";
	public static final String ALARM_POTN_STM_UNIT_TEMP_HIGH = "STM_UNIT_TEMP_HIGH";
	public static final String ALARM_POTN_STM_UNIT_ERR = "STM_UNIT_ERR";
	public static final String ALARM_POTN_TE_LINK_ERR = "TE_LINK_ERR";
	public static final String ALARM_POTN_WDM_OPTIC_CONF_MISMATCH = "WDM_OPTIC_CONF_MISMATCH";
	public static final String ALARM_POTN_WDM_UNIT_TEMP_HIGH = "WDM_UNIT_TEMP_HIGH";
	public static final String ALARM_POTN_ETH_RX_PWR_LOW = "ETH_RX_PWR_LOW";
	public static final String ALARM_POTN_ETH_RX_PWR_HIGH = "ETH_RX_PWR_HIGH";
	public static final String ALARM_POTN_OCH_RX_PWR_LOW = "OCH_RX_PWR_LOW";
	public static final String ALARM_POTN_OCH_RX_PWR_HIGH = "OCH_RX_PWR_HIGH";
	public static final String ALARM_POTN_ODU_RX_PWR_HIGH = "ODU_RX_PWR_HIGH";
	public static final String ALARM_POTN_ODU_RX_PWR_LOW = "POTN_ODU_RX_PWR_LOW";
	public static final String ALARM_POTN_OTN_RX_PWR_LOW = "OTN_RX_PWR_LOW";
	public static final String ALARM_POTN_STM_RX_PWR_HIGH = "STM_RX_PWR_HIGH";
	public static final String ALARM_POTN_STM_RX_PWR_LOW = "STM_RX_PWR_LOW";
	public static final String ALARM_POTN_OTN_SD = "OTN_SD";
	public static final String ALARM_POTN_STM_SD = "STM_SD";
	public static final String ALARM_POTN_STM_MS_SD = "STM_MS_SD";
	public static final String ALARM_POTN_ADMIN_LOCKED = "ADMIN_LOCKED";
	public static final String ALARM_POTN_LINK_LOF = "LINK_LOF";
	public static final String ALARM_POTN_LINK_BDI = "LINK_BDI";
	public static final String ALARM_POTN_LINK_RDI = "LINK_RDI";
	public static final String ALARM_POTN_ETH_LOF = "ETH_LOF";
	public static final String ALARM_POTN_ETH_FFI = "ETH_FFI";
	public static final String ALARM_POTN_ETH_LFI = "ETH_LFI";
	public static final String ALARM_POTN_ETH_RDI = "ETH_RDI";
	public static final String ALARM_POTN_TUNNEL_LOC = "TUNNEL_LOC";
	public static final String ALARM_POTN_APS_ADMIN_LOCKED = "APS_ADMIN_LOCKED";
	public static final String ALARM_POTN_AU_LOP = "AU_LOP";
	public static final String ALARM_POTN_AU_AIS = "AU_AIS";
	public static final String ALARM_POTN_AU_RDI = "AU_RDI";
	public static final String ALARM_POTN_GFP_LOF = "GFP_LOF";
	public static final String ALARM_POTN_OCH_LOF = "OCH_LOF";
	public static final String ALARM_POTN_ODU_LABEL_MISMATCH = "ODU_LABEL_MISMATCH";
	public static final String ALARM_POTN_ODU_LOCAL_FAULT = "ODU_LOCAL_FAULT";
	public static final String ALARM_POTN_ODU_LOCKED = "ODU_LOCKED";
	public static final String ALARM_POTN_ODU_LOF = "ODU_LOF";
	public static final String ALARM_POTN_ODU_BDI = "ODU_BDI";
	public static final String ALARM_POTN_ODU_CSF = "ODU_CSF";
	public static final String ALARM_POTN_ODU_RDI  = "ODU_RDI";
	public static final String ALARM_POTN_OTN_LOF = "OTN_LOF";
	public static final String ALARM_POTN_OTN_LFI = "OTN_LFI";
	public static final String ALARM_POTN_OTU_LOF = "OTU_LOF";
	public static final String ALARM_POTN_STM_LOF = "STM_LOF";
	public static final String ALARM_POTN_STM_LFI = "STM_LFI";
	public static final String ALARM_POTN_STM_MS_AIS = "STM_MS_AIS";
	public static final String ALARM_POTN_STM_RDI = "STM_RDI";
	public static final String ALARM_POTN_TE_LINK_TCA = "TE_LINK_TCA";
	public static final String ALARM_POTN_VC3_LABEL_MISMATCH = "VC3_LABEL_MISMATCH";
	public static final String ALARM_POTN_VC3_SD = "VC3_SD";
	public static final String ALARM_POTN_VC3_LOP = "VC3_LOP";
	public static final String ALARM_POTN_VC3_LO_LABEL_MISMATCH = "VC3_LO_LABEL_MISMATCH";
	public static final String ALARM_POTN_VC3_LO_NO_PAYLOAD = "VC3_LO_NO_PAYLOAD";
	public static final String ALARM_POTN_VC3_LO_BER_TCA = "VC3_LO_BER_TCA";
	public static final String ALARM_POTN_VC3_LO_RDI = "VC3_LO_RDI";
	public static final String ALARM_POTN_VC3_AIS = "VC3_AIS";
	public static final String ALARM_POTN_VC3_BER_TCA = "VC3_BER_TCA";
	public static final String ALARM_POTN_VC3_RDI = "VC3_RDI";
	public static final String ALARM_POTN_VC3_TU_AIS = "VC3_TU_AIS";
	public static final String ALARM_POTN_VC4_LABEL_MISMATCH = "VC4_LABEL_MISMATCH";
	public static final String ALARM_POTN_VC4_LOP = "VC4_LOP";
	public static final String ALARM_POTN_VC4_AIS = "VC4_AIS";
	public static final String ALARM_POTN_VC4_RDI = "VC4_RDI";
	public static final String ALARM_POTN_ADMIN_LOGIN = "ADMIN_LOGIN";
	public static final String ALARM_POTN_ETH_LOOPBACK = "ETH_LOOPBACK";
	public static final String ALARM_POTN_SYNC_EVENT = "SYNC_EVENT";
	public static final String ALARM_POTN_CLK_EVENT = "CLK_EVENT";
	public static final String ALARM_POTN_TEST_EVENT = "TEST_EVENT";
	public static final String ALARM_POTN_APS_EVENT = "APS_EVENT";
	public static final String ALARM_POTN_LAG_EVENT = "LAG_EVENT";
	public static final String ALARM_POTN_MSP_APS_EVENT = "MSP_APS_EVENT";
	public static final String ALARM_POTN_ODU_LOOPBACK = "ODU_LOOPBACK";
	public static final String ALARM_POTN_ODU_EVENT = "ODU_EVENT";
	public static final String ALARM_POTN_ODU_SNCP_EVENT = "ODU_SNCP_EVENT";
	public static final String ALARM_POTN_OTN_LOOPBACK = "OTN_LOOPBACK";
	public static final String ALARM_POTN_OTN_EVENT = "OTN_EVENT";
	public static final String ALARM_POTN_SNCP_APS_EVENT = "SNCP_APS_EVENT";
	public static final String ALARM_POTN_STM_LOOPBACK = "STM_LOOPBACK";
	public static final String ALARM_POTN_ODU_LOS = "ODU_LOS";
	public static final String ALARM_POTN_OCH_LOS = "OCH_LOS";
	public static final String ALARM_POTN_MPLS_PROT_SWITCH_LOCAL_REQ = "MPLS_PROT_SWITCH_LOCAL_REQ";
	public static final String ALARM_POTN_MPLS_PROT_SWITCH_REMOTE_REQ = "MPLS_PROT_SWITCH_REMOTE_REQ";
	public static final String ALARM_POTN_ETH_PORT_15M_TCA_THRESHOLD_OVER = "ETH_PORT_15M_TCA_THRESHOLD_OVER";
	public static final String ALARM_POTN_ETH_PORT_1D_TCA_THRESHOLD_OVER = "ETH_PORT_1D_TCA_THRESHOLD_OVER";
	public static final String ALARM_POTN_ODU_AIS = "ODU_AIS";
	public static final String ALARM_POTN_OTU_LOS = "OTU_LOS";
	public static final String ALARM_POTN_PW_LOC = "PW_LOC";

	// SWITCH
	public static final String ALARM_SWITCH_PING_UNREACHABLE = "PING_UNREACHABLE";
	public static final String ALARM_SWITCH_SNMP_TIME_OUT = "SNMP_TIME_OUT";
	public static final String ALARM_SWITCH_PORT_DOWN = "PORT_DOWN";

	// 도메인 ID
	public static final String DOMAIN_ROADM = "ROADM";							// ROADM 도권망 코위버 장비 도메인
	public static final String DOMAIN_POTN = "POTN";							// POTN 화웨이 장비 도메인
	public static final String DOMAIN_SWITCH = "SWITCH";							// SWITCH 장비 도메인

	// RCA OBJECT TYPE
	public static final String RCA_OBJECT_BASIC = "BASIC";
	public static final String RCA_OBJECT_ROADM = "ROADM";
	public static final String RCA_OBJECT_PTN = "PTN";

	// RCA Result Type
	public static final String RCA_RESULT_CABLECUT = "CableCut";				// 1. 선로장애
	public static final String RCA_RESULT_LINKCUT = "Linkcut";					// 2. 링크장애
	public static final String RCA_RESULT_UNIT_FAIL = "UnitFail";				// 3. 유닛장애
	public static final String RCA_RESULT_NODE_FAIL = "NodeFail";				// 4. 장치장애
	public static final String RCA_RESULT_COMM_FAIL = "CommFail";				// 5. 통신장애
	public static final String RCA_RESULT_CLOCK_FAIL = "ClockFail";				// 6. 클럭장애
	public static final String RCA_RESULT_POWER_FAIL = "PowerFail";				// 7. 전원장애
	public static final String RCA_RESULT_ETC_FAIL = "EtcFail";					// 8. 기타장애
	public static final String RCA_RESULT_CIRCUIT_FAIL = "CircuitFail";			// 9. 회선장애
	public static final String RCA_RESULT_SWITCH_FAIL = "SwitchFail";			// 10. SWITCH장애

	//RCA Ticket Type
	public static final String TICKET_TYPE_PERFORMACE = "PF"; //Cable Inspecter Ticket
	public static final String TICKET_TYPE_RCATICKET = "RT"; //RCA Ticket

	//RCA Ticket Status Code
	public static final String TICKET_STATUS_INIT = "INIT"; //시작
	public static final String TICKET_STATUS_ACK  = "ACK";  //인지
	public static final String TICKET_STATUS_FIX  = "FIX";  //조치
	public static final String TICKET_STATUS_FIN  = "FIN";  //마감
	public static final String TICKET_STATUS_AUTO_FIN  = "AUTO_FIN";  //자동마감
	public static final String TICKET_STATUS_SUB_ALM  = "SUB_ALM";  //분석 불가

	public static final String UI_TICKET_TYPE_NEW = "TICKET_NEW";
	public static final String UI_TICKET_TYPE_UPDATE = "TICKET_UPDATE";
	public static final String UI_TICKET_TYPE_MERGE = "TICKET_MERGE";
	public static final String UI_TICKET_TYPE_DELETE = "TICKET_DELETE";
	public static final String UI_TICKET_TYPE_TICKET_CABLECUT_LINK_UPDATE = "TICKET_CABLECUT_LINK_UPDATE";

	//RCA TOPOLOGY GB
	public static final String TOPOLOGY_GB_NNI = "NNI";
	public static final String TOPOLOGY_GB_UNI = "UNI";
	public static final String TOPOLOGY_GB_UNKNOWN = "UNKNOWN";

	public static final String DATA_SHARE_NAME_TICKET_LIST = "ticketList";
	public static final String DATA_SHARE_NAME_CLEAR_AL_LIST = "clearAlarmList";
	public static final String DATA_SHARE_NAME_CLEAR_TICKET_LIST = "ticketClearList";

	// UI MESSAGE
	public static final String UI_REQUEST_DATA_SNAPSHOT = "REQUEST_DATA_SNAPSHOT";

	public static final String DATA_SHARE_NAME_IS_START = "isStart";
}
