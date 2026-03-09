export const ServicePath = {
    service: 'service',
    data: 'data'
};

export const Service = {
    main: 'main',
    data: 'data',
    rca: 'rca',
};

export const Action = {
    SELECT_RCA_ALARM : 'SELECT_RCA_ALARM',
    GET_TOPOLOGY : 'GET_TOPOLOGY',
    REQUEST_3D_TOPLOGY : 'REQUEST_3D_TOPLOGY',
    CHANGE_TICKET_STATUS: 'CHANGE_TICKET_STATUS',
    CHANGE_SYSLOG_STATUS: 'CHANGE_SYSLOG_STATUS',
    EXPORT_EXCEL: 'EXPORT_EXCEL',
    RESET_TICKET: 'RESET_TICKET',
    SELECT_TICKET_CUR_LIST : 'SELECT_TICKET_CUR_LIST',
    GET_TICKET_ALL : 'GET_TICKET_ALL',
    UPDATE_FILE : 'UPDATE_FILE',
    INSERT_ISSUE_COMMENT : 'INSERT_ISSUE_COMMENT',
    INSERT_QNA_COMMENT : 'INSERT_QNA_COMMENT',

    GET_DEFAULT_CHART: 'GET_DEFAULT_CHART',

    SELECT_AL_EQUIP2TC_LIST: 'SELECT_AL_EQUIP2TC_LIST',
    SELECT_AL_EQUIP2SERVICE_LIST: 'SELECT_AL_EQUIP2SERVICE_LIST',
    SELECT_ISSUE_LIST:'SELECT_ISSUE_LIST',
    SELECT_QNA_LIST:'SELECT_QNA_LIST',
    SELECT_MONITOR_SYSTEM:'SELECT_MONITOR_SYSTEM',
    SELECT_SEND_DATA_SNAPSHOT_LIST: 'SELECT_SEND_DATA_SNAPSHOT_LIST',

    REQUEST_RESTFUL: 'REQUEST_RESTFUL',
    REQUEST_HTTPS_RESTFUL: 'REQUEST_HTTPS_RESTFUL',
    REQUEST_POST: 'REQUEST_POST',
    REQUEST_SIMULATOR: 'REQUEST_SIMULATOR',

    REQUEST_DATA_SNAPSHOT: 'REQUEST_DATA_SNAPSHOT',

    //STATISTICS_DAY: 'STATISTICS_DAY',
    //STATISTICS_WEEK: 'STATISTICS_WEEK',
    //STATISTICS_MONTH: 'STATISTICS_MONTH',
    //SELECT_SHOPPINGMALL_PURCHASING_CUSTOMER_LIST: 'SELECT_SHOPPINGMALL_PURCHASING_CUSTOMER_LIST',
    //SELECT_SHOPPINGMALL_CUSTOMER_SEARCH_LIST: 'SELECT_SHOPPINGMALL_CUSTOMER_SEARCH_LIST',
    //SHOPPINGMALL_CUSTOMER_INFORMATION_PURCHASE_LIST: 'SHOPPINGMALL_CUSTOMER_INFORMATION_PURCHASE_LIST',
    //SELECT_CUSTOMER_MANAGEMENT_LIST: 'SELECT_CUSTOMER_MANAGEMENT_LIST',
    //SELECT_CUSTOMER_INFORMATION: 'SELECT_CUSTOMER_INFORMATION',
    //SELECT_CUSTOMER_MANAGEMENT_DETAILED_INFORMATION: 'SELECT_CUSTOMER_MANAGEMENT_DETAILED_INFORMATION',
    //SELECT_CUSTOMER_MANAGEMENT_SURVEY_LIST: 'SELECT_CUSTOMER_MANAGEMENT_SURVEY_LIST',
    //PAYMENT_INFO: 'PAYMENT_INFO',
    //SELECT_CUSTOMER_MANAGEMENT_ADVICE_LIST: 'SELECT_CUSTOMER_MANAGEMENT_ADVICE_LIST',
    //SELECT_CUSTOMER_MANAGEMENT_RESERVATION_LIST: 'SELECT_CUSTOMER_MANAGEMENT_RESERVATION_LIST',
    //SELECT_CUSTOMER_MANAGEMENT_SMS_HISTORY_LIST: 'SELECT_CUSTOMER_MANAGEMENT_SMS_HISTORY_LIST',
    //POINT_INFO: 'POINT_INFO',
    //PROCEED_CUSTOMER_CODE_INFO: 'PROCEED_CUSTOMER_CODE_INFO',
    //PROCEED_CUSTOMER_LIST: 'PROCEED_CUSTOMER_LIST',
    //PROCEED_CUSTOMER_PLAN_LIST: 'PROCEED_CUSTOMER_PLAN_LIST',
    //PROCEED_CUSTOMER_INSERTPLAN: 'PROCEED_CUSTOMER_INSERTPLAN',
    //PROCEED_CUSTOMER_UPDATEPLAN: 'PROCEED_CUSTOMER_UPDATEPLAN',
    //PROCEED_CUSTOMER_DELETEPLAN: 'PROCEED_CUSTOMER_DELETEPLAN',
    //PROCEED_CUSTOMER_SHIFTPLAN: 'PROCEED_CUSTOMER_SHIFTPLAN',
    //PROCEED_CUSTOMER_MEMO_ADD: 'PROCEED_CUSTOMER_MEMO_ADD',
    //PROCEED_CUSTOMER_MEMO_MODIFY: 'PROCEED_CUSTOMER_MEMO_MODIFY',
    //PROCEED_CUSTOMER_MEMO_DELETE: 'PROCEED_CUSTOMER_MEMO_DELETE',

};

export const EquipType = {
    // <!--</editor-fold desc="[#191206 추가]">
    MSPP: {code: 'MSPP', layer: 5, nodeType: null},
    PCM: {code: 'PCM', layer: 4, nodeType: null},
    PTN: {code: 'PTN', layer: 3, nodeType: null},
    POTN: {code: 'POTN', layer: 2, nodeType: null},
    ROADM: {code: 'ROADM', layer: 1, nodeType: null},
    FDF: {code: 'FDF', layer: 0, nodeType: null},
    // ETC: {code: 'ETC', layer: 0, nodeType: null},
}

EquipType.prototype = {
    getType: function (equipType, notNullNodeType = true) {
        var ret = null;
        Object.keys(EquipType).forEach(function (key) {
            var value = EquipType[key];
            if (notNullNodeType && !value.nodeType) {
                return true;
            }
            if (equipType.startsWith(value.code)) {
                ret = value;
                return false;
            }
        });
        return ret;
    }
}

export const TicketType = {
    CableCut: {text: '선로', code: 'CableCut', audio: '선로장애', hex:'#ff7f0e', index: 0},
    Linkcut: {text: '링크', code: 'Linkcut', audio: '링크장애', hex:'#2ca02c', index: 1},
    NodeFail: {text: '노드', code: 'NodeFail', audio: '노드장애', hex:'#d62728', index: 2},
    PowerFail: {text: '전원', code: 'PowerFail', audio: '전원장애', hex:'#9467bd', index: 3},
    UnitFail: {text: '유닛', code: 'UnitFail', audio: '유닛장애', hex:'#8c564b', index: 4},
    CommFail: {text: '통신', code: 'CommFail', audio: '통신장애', hex:'#d62728', index: 5},
    ClockFail: {text: '클럭', code: 'ClockFail', audio: '클럭장애', hex:'#d62728', index: 6},
    CircuitFail: {text: '회선', code: 'CircuitFail', audio: '회선장애', hex:'#9467bd', index: 7},
    SwitchFail: {text: '스위치', code: 'SwitchFail', audio: '스위치장애', hex:'#9467bd', index: 8},
    TrafficFail: {text: '트래픽', code: 'TrafficFail', audio: '트래픽장애', hex:'#9467bd', index: 9},
    FactorFail: {text: '장비부하', code: 'FactorFail', audio: '장비부하장애', hex:'#9467bd', index: 10},
    Etc: {text: '기타', code: 'Etc', audio: '기타장애', hex:'#e377c2', index: 11},
    NoIssue: {text: '비', code: 'NoIssue', audio: '비장애', hex:'#9467bd', index: 12},
    //ProvFail: {text: '구성', code: 'ProvFail', audio: '기타장애'},
    //Intrinsic: {text: 'Intrinsic', code: 'Intrinsic', audio: '기타장애'},
    //SUB_ALM: {text: 'SUB_ALM', code: 'SUB_ALM', audio: '기타장애'},
 /*   getType : function () {
     return sadfklasfdlk
     }*/
};

TicketType.prototype = {

    getType: function (type) {
        var ret = null;
        Object.keys(TicketType).forEach(function (key) {
            if (type == key) {
                ret = TicketType[key];
            }
        });

        return ret;
    },
    includes: function (typeArray, typeCode) {

        var type = TicketType.prototype.getType(typeCode || '');
        if (!type) {
            return false;
        };

        return (typeArray || []).includes(type);
    },
}

export const TicketStatus = {
    INIT: {text: '발생', code: 'INIT', color: 'red', hex: '#b14948', index: 0},
    ACK: {text: '인지', code: 'ACK', color: 'orange', hex: '#F7AA17', index: 1},
    //FIX: {text: '조치', code: 'FIX', color: 'blue', hex: '#2421c1', index: 2},
    FIN: {text: '마감', code: 'FIN', color: 'green', hex: '#52A43A', index: 2},
    AUTO_FIN: {text: '자동마감', code: 'AUTO_FIN', color: 'yellow-green', hex: '#adcc1e', index: 4},
    UNKNOWN: {text: '미분석', code: 'UNKNOWN', color: 'gray', hex: '#848486', index: 3},
    // PROCEED: { text: '진행중', code: 'PROCEED', color: 'yellow', hex: '#dede00', index: 5},
    // FAIL: { text: '실패', code: 'FAIL', color: 'red', hex: '#ff0000', index: 6},
};

export const TicketProcessing = {
    ACK: {text: '인지처리', code: 'ACK', view: 'mon_tt_template_ticket_ack'},
    ACK_MODIFIED: {text: '인지수정', code: 'ACK_MODIFIED', view: 'mon_tt_template_ticket_ack'},
    ACK_BATCH: {text: '인지 일괄처리', code: 'ACK_BATCH', view: 'mon_tt_template_ticket_ack'},
    FIN: {text: '마감처리', code: 'FIN', view: 'mon_tt_template_ticket_fin'},
    FIN_MODIFIED: {text: '마감수정', code: 'FIN_MODIFIED', view: 'mon_tt_template_ticket_fin'},
    FIN_BATCH: {text: '마감 일괄처리', code: 'FIN_BATCH', view: 'mon_tt_template_ticket_fin'},

    INSPECTOR_ACK: {text: '선로장애 인지처리', code: 'INSPECTOR_ACK', view: 'mon_tt_template_inspector_ack'},
    INSPECTOR_ACK_MODIFIED: {text: '선로장애 인지수정', code: 'INSPECTOR_ACK_MODIFIED', view: 'mon_tt_template_inspector_ack'},
    INSPECTOR_ACK_BATCH: {text: '선로장애 인지 일괄처리', code: 'INSPECTOR_ACK_BATCH', view: 'mon_tt_template_inspector_ack'},
    INSPECTOR_FIN: {text: '선로장애 마감처리', code: 'INSPECTOR_FIN', view: 'mon_tt_template_inspector_fin'},
    INSPECTOR_FIN_MODIFIED: {text: '선로장애 마감수정', code: 'INSPECTOR_FIN_MODIFIED', view: 'mon_tt_template_inspector_fin'},
    INSPECTOR_FIN_BATCH: {text: '선로장애 마감 일괄처리', code: 'INSPECTOR_FIN_BATCH', view: 'mon_tt_template_inspector_fin'},
};

export const CableTicketStatus = {
    INIT: {text: '발생', code: 'INIT', color: 'red', hex: '#b14948', index: 0},
    ACK: {text: '인지', code: 'ACK', color: 'orange', hex: '#F7AA17', index: 1},
    FIN: {text: '마감', code: 'FIN', color: 'green', hex: '#52A43A', index: 2}
};

export const TicketResultCode = {
    /*전원차단*/
    PTN_PWR_DOWN: ['MSPP_EQP_RT_PWR_DOWN', 'PTN_EQP_RT_PWR_DOWN'],
    /*고온이상*/
    PTN_FAN_HIGH_TEMP: ['PTN_EQP_FAN_HIGH_TEMP'],
    /*랜장애*/
    PTN_LAN_DOWN: ['PTN_EQP_MT_LAN_IF_EVENT', 'PTN_EQP_SHELF_LAN_FAIL'],
    /*클럭부장애*/
    PTN_BITS_LOS: ['PTN_EQP_BITS_LOS'],
    /*(멀티)스위칭패브릭장애 : 이 경우에는 10,11 포트에 장애표현 */
    PTN_UNIT_MUL_MT_FABRIC_FAIL :  ['PTN_UNIT_MUL_MT_FABRIC_FAIL'],
};

export const TicketEleStatus = {
    NORMAL: { coment: '기본', size: 115 },
    EXPAND: { coment: '확장', size: 190 },
    OPEN: { coment: '펼침', size: 295 },
}

TicketResultCode.prototype = {
    includes: function (type, code) {
        return (type || []).includes(code);
    },
}

export const AdditionalTicketStatus = {
    FM: { text: 'FM유무', index: 0 },
    NATURE: { text: '자연회복', index: 1, code: 'NATURE', color: 'khaki', hex: '#70802b'},
    WORK_FAIL: { text: '공사장애', index: 2, code: 'WORK_FAIL'},
    POST: { text: '우정망', index: 3, code: 'POST'},
};

export const TicketOrg = [
    { text: '강남/서부', code: '강남네트워크운용본부', new_code: '강남/서부NW운용본부', index: 0 },
    { text: '강북/강원', code: '강북네트워크운용본부', new_code: '강북/강원NW운용본부', index: 1 },
    { text: '충남/충북', code: '충청네트워크운용본부', new_code: '충남/충북NW운용본부', index: 2 },
    { text: '전남/전북', code: '호남네트워크운용본부', new_code: '전남/전북NW운용본부', index: 3 },
    { text: '대구/경북', code: '대구네트워크운용본부', new_code: '대구/경북NW운용본부', index: 4 },
    { text: '부산/경남', code: '부산네트워크운용본부', new_code: '부산/경남NW운용본부', index: 5 },
    { text: '제주', code: '제주고객본부', new_code: '제주단', index: 6 },
    { text: '국제', code: '국제통신운용센터', index: 7 },
    { text: 'KT_SAT', code: 'KT_SAT', index: 8 },
];

export const LinkAlarmPrediction = {
    1: '추정(최고)',
    2: '추정(높음)',
    3: '추정(보통)',
    4: '추정(낮음)',
    5: '추정(최저)',
};

export const SocketEventBusAddress = {
    BROADCAST_MESSAGE : 'broadcastMessage',
    BROADCAST_TICKET : 'broadcastTicket',
    BROADCAST_SYSLOG : 'broadcastSyslog',
    BROADCAST_HEATBEAT : 'broadcastHeatbeat',
    ADDR_IN_SESSION : 'requestSession',
    ADDR_OUT_SESSION : 'session',
    BROADCAST_NOTICE: 'broadcastNotice',
    BROADCAST_CHART: 'broadcastChart',
    BROADCAST_MONITORING : 'broadcastMonitoring',
    BROADCAST_UPDATE_STATUS : 'broadcastUpdateStatus',
    BROADCAST_UNKNOWN : 'broadcastUnknown',
};

export const SocketEventType = {
    TICKET_NEW: 'TICKET_NEW',
    TICKET_UPDATE: 'TICKET_UPDATE',
    TICKET_MERGE: 'TICKET_MERGE',
    TICKET_DELETE: 'TICKET_DELETE',
    TICKET_TOPOLOGY_CHANGE: 'TICKET_TOPOLOGY_CHANGE',
    TICKET_CABLECUT_LINK_UPDATE: 'TICKET_CABLECUT_LINK_UPDATE',
};

export const ViewEventType = {
    SOCKET_OPENED: 'SOCKET_OPENED',
    CHANGED_STATISTICS: 'CHANGED_STATISTICS',
    CHANGED_TICKET_COUNT: 'CHANGED_TICKET_COUNT',
    CHANGED_TICKET_TOPOLOGY: 'CHANGED_TICKET_TOPOLOGY',
    MONITORING_LOADED: 'MONITORING_LOADED',
    DASHBOARD_LOADED: 'DASHBOARD_LOADED',
    CHANGED_CURRENT_USER_COUNT: 'CHANGED_CURRENT_USER_COUNT',
    CHANGED_MONITORING_TT_TABLE : 'CHANGED_MONITORING_TT_TABLE',
    COMPLETE_LOAD_TICKET: 'COMPLETE_LOAD_TICKET',
    COMPLETE_LOAD_INSPECTOR_TICKET: 'COMPLETE_LOAD_INSPECTOR_TICKET',

    CHANGED_NIA_TICKET: 'CHANGED_NIA_TICKET',
    SUCCESSED_TSDN_RESERVE: 'SUCCESSED_TSDN_RESERVE',
    SUCCESSED_TSDN_SERVICE: 'SUCCESSED_TSDN_SERVICE',
    FAILED_TSDN_SERVICE: 'FAILED_TSDN_SERVICE'
};

export const Properties = {
    TOTAL: 'TOTAL',
    FORMAT: 'FORMAT',
    TIME: 'TIME',
    LABEL_COUNT: 'LABEL_COUNT',
};

export const TopologyLoadType = {
    AUTO: 'AUTO',
    FROM_LOCAL: 'FROM_LOCAL',
    FROM_DB: 'FROM_DB',
};

export const UserGrant = {
    USER: { text:"사용자", value:1, index:0},
    MANAGER: {text:"담당자", value:2,index:1},
    ADMIN: {text:"관리자", value:4, index:2},
    // DROP:{text:"이슈편집",value:8 ,index:3},
    // ARAM:{text:"알람기능",value:16, index:4}
};

export const IndexedDB = {
    RCA_DB: 'RCA_DB',
    TICKET: {
        STORE: 'TicketStore',
        KEY: 'Ticket'
    },

};

export const MapType = {
    LAYERED : "LAYERED",
    CABLE: "CABLE",
};

export const MonitoringType = {
    CONTROL: {index: 0, key:'CONTROL', text: "관제"},
    WALLBOARD: {index: 1, key:'WALLBOARD', text: "WallBoard"},
    INSPECTOR_WALLBOARD: {index: 2, key:'INSPECTOR_WALLBOARD', text: "케이블"},
    // DESKTOP: {index: 2, key:'DESKTOP', text: "Desktop"},
};

export const RcaTicketType = {
    TICKET: { code: 'TICKET', value: 'RT', display: 'RCA TT', text: '장비장애', data: 'tools.store.tickets', key: 'ticket_id' },
    INSPECTOR: { code: 'INSPECTOR', value: 'CIT2', display: 'CI TT', text: '선로장애', data: 'tools.store.inspectorTicket.data', key: 'inspector_seq' }
}

export const LteEquipType = [
    'DU10SAME',
    'DU20SAME',
    'DU30SAME',
    'DU10NSNN',
    'DU20NSNN',
    'DU30NSNN',
    'DU10LGES',
    'DU20LGES',
    'DU30LGES',
];

export const SimulatorService = {
    STEP1: 'restconf/operations/koren-tsdn-nbi-service:discovery-pce',
    STEP2: 'restconf/operations/koren-tsdn-nbi-service:reserve-e2e-service',
    STEP3: 'restconf/operations/koren-tsdn-nbi-service:reserve-e2e-service',
}


export const Message = {
    SAVE: '저장하시겠습니까?',
    CREATE: '생성하시겠습니까?',
    UPDATE: '수정하시겠습니까?',
    DELETE: '삭제하시겠습니까?',
    COPY: '복사하시겠습니까?',
    SELECT: '선택하시겠습니까?',
    PLEASE_SELECT: '선택해 주세요',
    PLEASE_ENTER: '입력해 주세요',
    COMPLETE: '처리되었습니다.',
    CHECK_INPUT: '입력데이터를 확인하세요',
    QUIT_WITH_SAVE: '변경된 데이터가 있습니다. 저장하시겠습니까?',
    CONFIRM_WITH_SAVE: '확인을 선택하면 데이터가 저장됩니다',

    TICKET_ACK: '인지 하시겠습니까?',
    TICKET_ACK_MODIFIED: '인지를 수정 하시겠습니까?',
    TICKET_ACK_COMPLETE: '인지 되었습니다',
    TICKET_ACK_MODIFIED_COMPLETE: '인지수정 되었습니다',
    TICKET_FIN: '마감 하시겠습니까?',
    TICKET_FIN_MODIFIED: '마감을 수정 하시겠습니까?',
    TICKET_FIN_COMPLETE: '마감 되었습니다',
    TICKET_FIN_MODIFIED_COMPLETE: '마감수정 되었습니다',
}
