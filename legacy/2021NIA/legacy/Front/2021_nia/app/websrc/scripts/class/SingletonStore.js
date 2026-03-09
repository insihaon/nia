class SingletonStore {
    constructor() {

        this.load();

        window.location.href.replace(/[?&]+([^=&]+)=([^&#]*)/gi, ((m, key, value) => {
            let v = value === 'true' || value === 'false' ? value === 'true' : value;

            if (key === 'mode') {

                var arr = v.split(",").map(pair => pair.split(":"));
                var mode = Object.assign({}, this.mode);
                arr.forEach((([key,value]) => {
                    try {
                        value = (value == undefined) ? true : String(value) == "true";
                        mode[key] = value;
                    } catch (e) {
                        //debugger;
                    }
                }).bind(this)); //{k1: "v1", k2: "v2"}
                v = mode;

                //var table = "k1:v1,k2:v2".split(",") //["k1:v1","k2:v2"]
                //        .map(pair => pair.split(":")); //[["k1","v1"],["k2","v2"]]
                //var result = {};
                //table.forEach(([key,value]) => result[key] = value) //{k1: "v1", k2: "v2"}
            }

            this[key] = v;
        }).bind(this));

        let {debug, sql, mode, sound, isDev, breakpoint, ai, video} = this;
        debug = (debug != undefined) ? debug : isDev();
        Object.assign(this, {
            app: 'StarterApp',
            ai: (ai != undefined) ? ai : isDev(),
            _service_data : {},
            _user: {login_id: 'rca'},
            root: "nia",
            // root_demo: "demo",
            // root_master: "master",
            debug: debug,
            breakpoint: (breakpoint != undefined) ? breakpoint : false,
            sql: (sql != undefined) ? sql : isDev(),
            mode: (mode != undefined) ? mode :
            {
                op_reload_all: true,             // 2019.02.21 네트워크 끊김 후 재연결 시 이벤트 기준이 아닌 모든 티켓을 다 받는다.
                op_3d_simple_image: false,       // 2018.11.20 실사이미지 사용
                op_socket_test: debug,        // TOP 메뉴에 티켓 socket 테스트 메뉴를 활성화 한다. true 이면 티켓의 업데이트도 가능하다.
                op_map3d_no_alarm: false,   // 2018.11.13 3D 토폴로지에 장애 생략
                op_no_sock: false,           // 2018.11.12 소켓을 사용하지 않는 옴션
                op_multiple_page: false,       // 2018.11.21 메인화면을 단일페이지(티켓 + 토폴로지)로 로드한다.
                op_socket_event_test: false,   // 2020.06.23 클라이언트용 소켓 이벤트 로그 기능을 활성화 한다.
                op_simple_ui_mode: true       // 2020.09.24 전체적인 완성도를 낮추기 위해 일부 UI 애니메이션 및 기능을 disable 한다
            },
            alarm_gb: '1',      // 1:케이블컷, 2:유닛장애
            system_gb: 'nia',     // 'rca' or 'master'
            alarm_id: null,      // system_gb=='rca'  클러스터D, system_gb=='master' 이벤트ID
            zoom_mode: false,     // 장애구간 줌인 기능
            appType: 'roadshow',       // undefined, roadshow, ptnResult
            constants : null,

            ticketStorage: null,
            filterStorage: null,
            inspectorTicket: null,
            tickets: [],
            isShowProgressPopup: false,
            ticketSearchDate: { start : '', end : '' },
            ticketExpandMode: true,    // 티켓 접힘/펼침 모드
            isTicketRealTimePause: false,   // 티켓 실시간모드 일시정지
            last_event_sequence: '',
            sound: (sound != undefined) ? sound : true,
            currentUserCount: 0,
            video: (video != undefined) ? video : true, // 로드쇼 비디오 재생 모드
            indexed_db_version: {
                rca: 1
            },
            version: 1.001,
            niaData: {},
            niaList: [],
            niaSyslogList: [],
            niaSysDialogList: [],
            niaSelectedTicket: null,
            niaToken: null,

            aiPageNum: 1
        });

        function buildView() {
            return function (style, viewName, root, type, image, path, controller, templateId) {
                let view = {
                    style: style,
                    viewName: viewName,
                    type: root == null ? type : (root + '.' + type),
                    image: image,
                    path: path,
                    controller: controller
                };

                if (templateId) {
                    view.content = {
                        id: templateId + '_ID',
                        title: viewName,
                        type: 'component',
                        componentName: 'myPane',
                        componentState: {
                            title: viewName,
                            templateId: templateId,
                            templateUrl: path,
                        }
                    };
                }

                return view;
            };
        }

        let createView = buildView();

        // @formatter:off
        this._viewType = {
            main_page: createView(1, "", null, this.root, null, "pages/main/view.html", 'MainPageCtrl'),
            monitoring_tt: createView(1, "티켓 모니터링", this.root, 'monitoring_tt', null, "pages/main/monitoringTT/view.html", 'MonitoringTTCtrl'),
            mon_tt_ticket: createView(1, "티켓", this.root, 'monitoring_tt.rcaTicket', null, "pages/main/monitoringTT/rcaTicket/view.html", 'RcaTicketCtrl'),
            mon_tt_syslog: createView(1, "syslog", this.root, 'monitoring_tt.syslog', null, "pages/main/monitoringTT/syslog/view.html", 'SyslogCtrl'),
            mon_tt_syslog_dialog: createView(1, "syslogDialog", this.root, 'monitoring_tt.syslogDialog', null, "pages/main/monitoringTT/syslog_dialog/view.html", 'SyslogDialogCtrl'),
            mon_tt_template_ticket_ack: createView(1, "티켓인지", this.root, 'monitoring_tt.ticketAck', null, "pages/main/monitoringTT/template_dialog/ticket_ack_template/view.html", 'TicketAckDialogCtrl'),
            mon_tt_template_ticket_fin: createView(1, "티켓마감", this.root, 'monitoring_tt.ticketFin', null, "pages/main/monitoringTT/syslog_dialog/syslog_detail/view.html", 'TicketFinDialogCtrl'),

            /** for Debugging. http://localhost:8080/#/rca/rca.viewMap3d/ */
            map3d: createView(1, "Map3D", this.root, 'map3d', null, "pages/main/monitoringTT/map3d/view.html", 'Map3dCtrl'),

            login_page: createView(1, "로그인페이지", null, 'login_page', null, "pages/login/view.html", 'LoginCtrl'),
            ai_process: createView(1, "AI 프로세스", this.root, 'ai_process', null, "pages/main/aiProcess/view.html", 'AIProcessCtrl'),
            self_configuration: createView(1, "자가구성", this.root, 'self_configuration', null, "pages/main/aiProcess/selfConfiguration/view.html", 'SelfConfigurationCtrl'),
            self_healing: createView(1, "자가회복", this.root, 'self_healing', null, "pages/main/aiProcess/selfHealing/view.html", 'SelfHealingCtrl'),
            ai_history: createView(1, "AI 처리 결과 이력", this.root, 'ai_history', null, "pages/main/aiProcess/aiHistory/view.html", 'AiHistoryCtrl'),

            ai_process_template: createView(1, "AI 프로세스 팝업", this.root, 'ai_process_template', null, "pages/main/aiProcess_template/view.html", 'AIProcessDialogCtrl'),
            self_healing_template: createView(1, "자가회복 팝업", this.root, 'self_healing_template', null, "pages/main/aiProcess_template/selfHealing_template/view.html", 'SelfHealingDialogCtrl'),
            ai_history_template: createView(1, "AI 처리 결과 이력 팝업", this.root, 'ai_history_template', null, "pages/main/aiProcess_template/aiHistory_template/view.html", 'AiHistoryDialogCtrl'),
            self_configuration_template: createView(1, "자가구성 팝업", this.root, 'self_configuration_template', null, "pages/main/aiProcess_template/selfConfiguration_template/view.html", 'SelfConfigurationDialogCtrl'),
            
            config_test_action: createView(1, "자가구성 시험 팝업", this.root, 'config_test_action', null, "pages/main/aiProcess_dialog_template/configTestAction/view.html", 'ConfigTestActionDialogCtrl'),
            bypass_route_list: createView(1, "우회 가능 경로 리스트 팝업", this.root, 'bypass_route_list', null, "pages/main/aiProcess_dialog_template/bypassRouteList/view.html", 'BypassRouteListDialogCtrl'),
            fin_process_dialog: createView(1, "AI 프로세스 마감 팝업", this.root, 'AI', null, "pages/main/aiProcess_dialog_template/fin_process_dialog/view.html", 'FinProcessDialogCtrl'),

            optimal_route_count: createView(1, "최적 경로 산정 팝업", this.root, 'optimal_route_count', null, "pages/main/optimalRouteCount/view.html", 'OptimalRouteCountCtrl'),
            optimal_route_setting: createView(1, "최적 경로 설정 팝업", this.root, 'optimal_route_setting', null, "pages/main/optimalRouteSetting/view.html", 'OptimalRouteSettingCtrl'),

            unverified_traffic_list: createView(1, "사용자 정의를 위한 미확인 트래픽 정의", this.root, 'unverified_traffic', null, "pages/main/unverified_traffic/unverified_traffic_list/view.html", 'UnverifiedTrafficListCtrl'),
            unverified_traffic_detail: createView(1, "미확인 트래픽 상세정보", this.root, 'unverified_traffic_detail', null, "pages/main/unverified_traffic/unverified_traffic_detail/view.html", 'UnverifiedTrafficDetailCtrl'),
            application_add: createView(1, "어플리케이션 등록", this.root, 'application_add', null, "pages/main/unverified_traffic/application_add/view.html", 'ApplicationAddCtrl'),
            traffic_agency_add: createView(1, "이용기관 등록", this.root, 'traffic_agency_add', null, "pages/main/unverified_traffic/agency_add/view.html", 'TrafficAgencyAddCtrl'),

            alarm_management: createView(1, "장애 현황 및 이력 관리 기능", this.root, 'alarm_management', null, "pages/main/alarm_management/view.html", 'AlarmManagementCtrl'),
            syslog_rule_management: createView(1, "SYSLOG RULE 관리 기능", this.root, 'syslog_rule_management', null, "pages/main/syslog_rule_management/view.html", 'SyslogRuleManagementCtrl'),

            equip_by_port: createView(1, "장비 포트별 트래픽 감시 및 이력 조회 기능", this.root, 'equip_by_port', null, "pages/main/equip_by_port/view.html", 'EquipByPortCtrl'),
            equip_amount_used: createView(1, "장비 사용량 감시 및 이력 조회 가능", this.root, 'equip_amount_used', null, "pages/main/equip_amount_used/view.html", 'EquipAmountUsedCtrl'),

            by_agency_statistics_skill: createView(1, "이용기관별 트래픽 분석 및 Top N 통계 기능 팝업", this.root, 'by_agency_statistics_skill', null, "pages/main/byAgencyStatisticsSkill/view.html", 'ByAgencyStatisticsSkillCtrl'),
            by_country_statistics_skill: createView(1, "국가별 트래픽 분석 및 Top N 통계 기능 팝업", this.root, 'by_contry_statistics_skill', null, "pages/main/byCountryStatisticsSkill/view.html", 'ByCountryStatisticsSkillCtrl'),
            by_application_statistics_skill: createView(1, "어플리케이션별 트래픽 분석 및 Top N 통계 기능 팝업", this.root, 'by_application_statistics_skill', null, "pages/main/byApplicationStatisticsSkill/view.html", 'ByApplicationStatisticsSkillCtrl'),

            SOP_edit_list: createView(1, "SOP리스트 팝업", this.root, 'SOP_edit_list', null, "pages/main/aiProcess_dialog_template/SOP/SOPEditList/view.html", 'SOPEditListCtrl'),
            SOP_edit: createView(1, "SOP편집 팝업", this.root, 'SOP_edit', null, "pages/main/aiProcess_dialog_template/SOP/SOPEdit/view.html", 'SOPEditCtrl'),
            
            SOP: createView(1, "SOP리스트", this.root, 'SOP', null, "pages/main/SOP/SOP/view.html", 'SOPCtrl'),
            SOP_detail: createView(1, "TICKET_SOP상세보기", this.root, 'SOP_detail', null, "pages/main/SOP/SOPDetail/view.html", 'SOPDetailCtrl'),
            syslog_SOP_detail: createView(1, "SYSLOG_SOP상세보기", this.root, ' syslog_SOP_detail', null, "pages/main/SOP/syslogSOPDetail/view.html", 'syslogSOPDetailCtrl'),

            user_setting: createView(1, "사용자 관리", this.root, 'user_setting', null, "pages/main/userSetting/view.html", 'UserSettingCtrl'),
            administrator: createView(1, "관리자", this.root, 'administrator', null, "pages/main/administrator/view.html", 'AdministratorDialogCtrl'),
            date_setting: createView(1, "시간설정", this.root, 'date_setting', null, "pages/main/dateSetting/view.html", 'DateSettingCtrl'),

            node_list: createView(1, "노드관리", this.root, 'node_list', null, "pages/management/node/node_list/view.html", 'NodeListCtrl'),
            node_add: createView(1, "노드등록", this.root, 'node_add', null, "pages/management/node/node_add/view.html", 'NodeAddCtrl'),
            node_detail: createView(1, "노드상세보기", this.root, 'node_detail', null, "pages/management/node/node_detail/view.html", 'NodeDetailCtrl'),
            syslog_rule_detail: createView(1, "SYSLOG RULE 상세보기", this.root, 'syslog_rule_detail', null, "pages/main/monitoringTT/syslog_dialog/syslog_rule_detail/view.html", 'SyslogRuleDetailCtrl'),
            syslog_detail: createView(1, "SYSLOG 상세보기", this.root, 'syslog_detail', null, "pages/main/monitoringTT/syslog_dialog/syslog_detail/view.html", 'SyslogDetailCtrl'),
            syslog_configuration_template: createView(1, "SYSLOG구성 팝업", this.root, 'syslog_configuration_template', null, "pages/main/monitoringTT/syslog_dialog/syslogConfiguration_template/view.html", 'SyslogConfigurationDialogCtrl'),
            port_list: createView(1, "포트상세보기", this.root, 'port_list', null, "pages/management/node/port_list/view.html", 'PortListCtrl'),
            port_detail: createView(1, "포트상세보기", this.root, 'port_detail', null, "pages/management/node/port_detail/view.html", 'PortDetailCtrl'),
            link_list: createView(1, "링크관리", this.root, 'link_list', null, "pages/management/link/link_list/view.html", 'LinkListCtrl'),
            link_add: createView(1, "링크등록", this.root, 'link_add', null, "pages/management/link/link_add/view.html", 'LinkAddCtrl'),
            link_detail: createView(1, "링크상세보기", this.root, 'link_detail', null, "pages/management/link/link_detail/view.html", 'LinkDetailCtrl'),
            agency_list: createView(1, "이용기관", this.root, 'agency_list', null, "pages/management/agency/agency_list/view.html", 'AgencyListCtrl'),
            agency_add: createView(1, "이용기관등록", this.root, 'agency_add', null, "pages/management/agency/agency_add/view.html", 'AgencyAddCtrl'),
            agency_detail: createView(1, "이용기관상세보기", this.root, 'agency_detail', null, "pages/management/agency/agency_detail/view.html", 'AgencyDetailCtrl'),
            pro_file_list: createView(1, "프로파일관리", this.root, 'pro_file_list', null, "pages/management/proFile/proFile_list/view.html", 'ProFileListCtrl'),
            recovery: createView(1, "프로파일자가회복", this.root, 'recovery', null, "pages/management/proFile/proFile_recovery/view.html", 'RecoveryCtrl'),
            recovery_detail: createView(1, "프로파일자가회복상세보기", this.root, 'recovery_detail', null, "pages/management/proFile/proFile_recovery_detail/view.html", 'RecoveryDetailCtrl'),

            aiMonitor: createView(1, "aiMonitor", this.root, 'aiMonitor', null, "pages/main/monitoringTT/aiMonitor/view.html", 'AiMonitorCtrl'),
            serverMonitor: createView(1, "serverMonitor", this.root, 'serverMonitor', null, "pages/main/monitoringTT/serverMonitor/view.html", 'ServerMonitorCtrl'),
            dashboard: createView(1, "dashboard", this.root, 'dashboard', null, "pages/main/monitoringTT/dashboard/view.html", 'DashboardCtrl'),
            selfProcess: createView(1, "selfProcess", this.root, 'selfProcess', null, "pages/main/monitoringTT/selfProcess/view.html", 'SelfProcessCtrl'),
            selfProcess_dialog: createView(1, "자가처리 상세보기", this.root, 'selfProcess_dialog', null, "pages/main/monitoringTT/selfProcess/selfProcess_dialog/view.html", 'SelfProcessDialogCtrl'),
            selfProcess_email_dialog: createView(1, "자가처리 이메일 팝업", this.root, 'selfProcess_email_dialog', null, "pages/main/monitoringTT/selfProcess/selfProcess_email_dialog/view.html", 'SelfProcessEmailDialogCtrl'),

        };

        for (var i = 0; i < Object.keys(this._viewType).length; i++) {
            try {
                var value = Object.values(this._viewType)[i];
                value.id = Object.keys(this._viewType)[i];
            } catch (e) {
            }
        }

        /* state TEST
         angular.element($0).injector().get('$state').go("dev_management")
         */

        this._views = [];

        // @formatter:on

        for (let view in this._viewType) {
            this._views.push(this._viewType[view]);
        }

        this.ticketAllSearch = '';
        this.topologyTicket = {ticket_id: null, cluster_no: null};
        this.socketEventBus = null;

        this.save();
    }

    toJSON() {
        let {debug, sql, mode, breakpoint, video} = this;
        return {debug, sql, mode, breakpoint, video};
    }

    getMode(prop) {
        return (this.mode || {})[prop];
    }

    load() {
        try {
            var data = JSON.parse(localStorage.getItem("AppConfig"));
            for (let variable in data) {
                this[variable] = data[variable];
            }
        } catch (e) {
            console.log("로컬 데이터를 가져 올 수 없습니다. .");
        }
    }

    isDev() {
        return window.location.hostname == 'localhost';
    }

    save() {
        var json = JSON.stringify(this.toJSON());
        if (this.isDev() == false) {
            json = json.replace('"sql":false,', ''); // false 일 때 Hidden
        }
        localStorage.setItem("AppConfig", json);
    }

    formatDateTime(value) {
        var date = new Date(value);
        if (value && (date instanceof Date) && !isNaN(date)) {
            return moment(value).format('YYYY-MM-DD HH:mm:ss');
        }
        return "";
    }

    formatYYDateTime(value) {
        var date = new Date(value);
        if (value && (date instanceof Date) && !isNaN(date)) {
            return moment(value).format('YY-MM-DD HH:mm:ss');
        }
        return "";
    }
    formatDate(value) {
        var date = new Date(value);
        if (value && (date instanceof Date) && !isNaN(date)) {
            return moment(value).format('YYYY-MM-DD');
        }
        return "";
    }
    formatSecToTime(timeInSeconds) {
        var seconds = (timeInSeconds/1000);
        var minutes = parseInt(seconds/60, 10);
        var hours = parseInt(minutes/60, 10);
        minutes = minutes%60;

        return (hours ? (hours + '시간 ') : '') + minutes + '분';
    }

    numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    static name() {
        return 'SingletonStore';
    }

    get isLocal() {
        return location.host == 'localhost:8080';
    }

    get profileNames() {
        var profileName = this.service_data["profile.name"] || "";
        return profileName.split(",");
    }

    hasProfile(name) {
        // administrator.name=dev,test
        // hasProfile('dev') == true;
        // hasProfile('test') == true;
        // hasProfile('oper') == false;
        return this.profileNames.indexOf(name) >= 0;
    }

    get optionsNodeEditable() {
        // 서버설정(config.properties)  로그인을 할 것인가?
        return this.service_data["nia.options.node.editable"] == true;
    }

    get authUse() {
        // 서버설정(config.properties)  로그인을 할 것인가?
        return this.service_data["auth.use"] == true;
    }

    get proxyServerStart() {
        // 서버설정(config.properties)  로그인을 할 것인가?
        return this.service_data["proxy.server.start"] == true;
    }

    get socketServerHost() {
        return this.service_data["socket.server.host"] || window.location.hostname;
    }

    get socketServerStart() {
        return this.service_data["socket.server.start"] == true;
    }

    get socketServerPort() {
        return this.service_data["socket.server.port"] || 8084;
    }

    get auth() {
        return this._auth;
    }

    set auth(auth) {
        this._auth = auth;
    }

    get user() {
        return this._user;
    }

    set user(user) {
        this._user = user;
    }

    hasGrant(grant) {
        // hasGrent(UserGrant.MANAGER) == true;
        return ((this.userLevel || 1) & grant) == grant;
    }

    get userLevel() {
        return this.user.LVL || this.user.lvl;
    }

    get userId() {
        return this.user.LOGIN_ID || this.user.login_id || this.user.id;
    }

    get userName() {
        return this.user.NAME || this.user.name;
    }

    get deptCd() {
        return this.user.DEPT_CD || this.user.dept_cd;       /*소속 팀 코드:A08*/
    }

    get deptName() {
        return this.user.DEPT_NAME || this.user.dept_name;     /*소속 팀 명:경영지원팀*/
    }

    get agentCd() {
        return this.user.AGENCY_CD || this.user.agency_cd;     /*소속 본부 코드:A03*/
    }

    get agency_name() {
        return this.user.AGENCY_NAME || this.user.agency_name;   /*소속 본부 명:경영전략실*/
    }

    get companyCd() {
        return this.user.COMPANY_CD || this.user.company_cd;    /*회사코드:1030*/
    }

    get companyName() {
        return this.user.COMPANY_NAME || this.user.company_name;  /*회사명:kt mhows*/
    }

    get phone_number() {
        return this.user.PHONE_NUMBER || this.user.phone_number;  /*연락처: 01000000000*/
    }

    get email() {
        return this.user.email|| this.user.email;  /*연락처: 01000000000*/
    }

    get viewType() {
        return this._viewType;
    }

    get views() {
        return this._views;
    }

    get encryptParams() {
        return this.s;
    }

    get service_data() {
        return this._service_data || {};
    }

    set service_data(service_data) {
        this._service_data = service_data;
    }

    get start_url() {
        return "/";
    }

    get logout_url() {
        return "/logout";
    }

    get login_page() {
        return  this.viewType.login_page.type
    }

    get start_page() {

        return this.isEmail === true ? this.viewType.selfProcess.type :this.viewType.monitoring_tt.type;

        // return this.auth ? this.viewType.monitoring_tt.type : this.login_page;
    }

    get current_user_count() {
        return this.currentUserCount;
    }

    checkAvailability(arr, val) {
        return arr.some(function (arrVal) {
            return val === arrVal;
        });
    }

    static serviceFactory(...injected) {
        SingletonStore.instance = new SingletonStore(...injected);
        return SingletonStore.instance;
    }
}

export default new SingletonStore();
