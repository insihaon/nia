import BaseController from 'scripts/controller/baseController'
import * as Constants from "../../../../scripts/class/Constants";

class SelfConfigurationDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, param) {
        $scope.config = tools.store.viewType.self_configuration;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);

        $scope.ticketInfo = param;

        $scope.tsdnDiscoveryResult = {};
        $scope.tsdnTopologyData = [];

        $scope.trafficList = [];
        $scope.cpuList = [];

        $scope.requestAnomalousOne = {};
        $scope.requestNodeFactorOne = {};

        $scope.emailReceiver = [];
        $scope.model_config = {
            text: '',
            ngModel: ''
        };
        $scope.selectedSop = {};
        $scope.configSopList = [];

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetail: '',
            etcContent: ''
        };

        $scope.progress_activated = true;

        /** View 이벤트 */
        $scope.onViewEvent = (event, param) => {
            switch (param.type) {
                /** 티켓 로드 완료시 */
                case tools.constants.ViewEventType.SUCCESSED_TSDN_RESERVE: {    /* TSDN 예약 성공 */
                    debugger;
                    break;
                }
                case tools.constants.ViewEventType.SUCCESSED_TSDN_SERVICE: {    /* TSDN 서비스 성공 */
                    debugger;
                    break;
                }
                case tools.constants.ViewEventType.FAILED_TSDN_SERVICE: {   /* TSDN 예약 or 서비스 실패 */
                    debugger;
                    break;
                }
            }
        };

        $scope.applyManager = () => {
            $scope.emailReceiver = [];
            var selectedList = [];
            $scope.managerList.map(function (v) {
                if (v.selected == true) {
                    selectedList.push(v.name);
                    $scope.emailReceiver.push(v.email);
                }
            });
            document.querySelector('.selectedManagerConfig').innerHTML = "&nbsp; &nbsp;" + selectedList.join(', ') || '';
        };

        $scope.sendEMail = () => {
            if ($scope.emailReceiver.length == 0) {
                tools.showToast('담당직원을 선택해주십시오.');
                return;
            }
            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_EMAIL',
                RECEIVER: $scope.emailReceiver.join(', '), /*test email: infobiz@koren.kr gidxhwl@hanmail.net  $scope.emailReceiver.join(', ') */
                SUBJECT : moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss초') + " 장비 조치 요청서",
                BODY: $('.selfConfiguration div.angular-meditor-content').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                TICKET_ID : $scope.ticket.ticket_id,
                TICKET_TYPE: $scope.ticket.ticket_type,
                TICKET_RESULT : $scope.ticket.ticket_rca_result_dtl_code || $scope.ticket.ticket_rca_result_code,
                USER_IDS : tools.store.userId,
                FAULT_CLASSIFY: $scope.operatorOpinion.faultClassify || '',
                FAULT_TYPE: $scope.operatorOpinion.faultType || '',
                FAULT_DETAIL_CONTENT: $scope.operatorOpinion.faultDetail || '',
                ETC_CONTENT: $scope.operatorOpinion.etcContent || '',
                HANDLING_ACK_USER: tools.store.userName
            }, function (result) {
                if(result.send_result){
                    tools.createConfirmDlg('담당자에게 메일이 발송되었습니다.', null, null);
                } else { tools.createConfirmDlg('메일 발송이 실패하였습니다.', null, null); }
            });
        };

        $scope.drawChart = () => {
            return c3.generate({
                bindto: '#trafficChart',
                data: {
                    x: 'date',
                    xFormat: '%Y-%m-%d %H:%M:%S',
                    columns: $scope.getChartColumn('traffic'),
                },
                grid: {
                    x: {
                        lines: [
                            { value:  tools.store.formatDateTime($scope.ticket.fault_time), class: 'fault-time-grid' }
                        ]
                    },
                    y: {
                        show: true,
                        lines: [
                            { value: 0 }
                        ]
                    }
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        tick: {
                            format: '%Y-%m-%d %H:%M:%S',
                            count: 5
                        }
                    },
                    y: {
                        type: 'number',
                        tick: {
                            format: d3.format(",")
                        },
                        title:{
                            text: '단위:백만'
                        }
                    }
                },
                tooltip: {
                    format: {
                        name: function (d) { return d == 'traffic' ? undefined : d.toUpperCase(); },
                        value: function (value, ratio, id) { return id == 'traffic' ? undefined : value.toLocaleString(); }
                    }
                },
                zoom: { enabled: true },
                legend: { hide: true }
            });
        };

        $scope.drawCpuChart = () => {
            return c3.generate({
                bindto: '#cpuChart',
                data: {
                    x: 'date',
                    xFormat: '%Y-%m-%d %H:%M:%S',
                    columns: $scope.getChartColumn('cpu'),
                },
                grid: {
                    x: {
                        lines: [
                            { value: tools.store.formatDateTime($scope.ticket.fault_time) , class: 'fault-time-grid' }
                        ]
                    },
                    y: {
                        show: true,
                        lines: [
                            { value: 0 }
                        ],
                        tick: {
                            format: d3.format(",")
                        },
                    }
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        tick: {
                            format: '%Y-%m-%d %H:%M:%S',
                            count: 5
                        }
                    }
                },
                tooltip: {
                    format: {
                        name: function (d) { return d == 'cpu' ? undefined : d.toUpperCase(); },
                        value: function (value, ratio, id) { return id == 'cpu' ? undefined : value.toLocaleString(); }
                    }
                },
                zoom: { enabled: true },
                legend: { hide: true }
            });
        };

        $scope.getChartColumn = (chartType) => {

            let result = [];

            const ticket = $scope.ticket
            
            if(chartType === 'traffic')  {
                // debugger
                if(ticket.ticket_type == 'ATT2') {
                    result = [
                        $scope.makeColumn('date', 'measured_datetime', chartType),
                        $scope.makeColumn('bps_in', 'fltbpsin', chartType),
                        $scope.makeColumn('bps_out', 'fltbpsout', chartType),
                        $scope.makeColumn('pps_in', 'fltppsin', chartType),
                        $scope.makeColumn('pps_out', 'fltppsout', chartType),
                    ]
                } else if(ticket.ticket_type == 'NTT') {
                    result = [
                        $scope.makeColumn('date', 'collect_time', chartType),
                        $scope.makeColumn('strbytes_col', 'strbytes_col', chartType),
                        $scope.makeColumn('strcounts', 'strcounts', chartType),
                    ]
                }else if(ticket.ticket_type == 'NFTT') {
                    result = [
                        $scope.makeColumn('date', 'collect_time', chartType),
                        $scope.makeColumn('strbytes_col', 'strbytes_col', chartType),
                        $scope.makeColumn('strcounts', 'strcounts', chartType),
                    ]
                } 
            } else if(chartType === 'cpu'){
                if(ticket.ticket_type == 'NFTT') {
                    result = [
                        $scope.makeColumn('date', 'measured_datetime', chartType),
                        $scope.makeColumn('mem_usage', 'mem_usage', chartType),
                        $scope.makeColumn('cpu_usage', 'cpu_usage', chartType),
                        // [ "date", "2022-12-13 20:29:54", "2022-12-13 20:34:54", "2022-12-13 20:39:54", "2022-12-13 20:44:54", "2022-12-13 20:49:54", "2022-12-13 20:54:53", "2022-12-13 20:54:54", "2022-12-13 20:59:54", "2022-12-13 21:04:54", "2022-12-13 21:09:53", "2022-12-13 21:09:54", "2022-12-13 21:14:54", "2022-12-13 21:19:54", "2022-12-13 21:24:54", "2022-12-13 21:29:54", "2022-12-13 21:34:54", "2022-12-13 21:39:53", "2022-12-13 21:39:54", "2022-12-13 21:44:54", "2022-12-13 21:49:54", "2022-12-13 21:54:54", "2022-12-13 21:59:54" ],
                            // [ "cpu", "10", "0", "0", "0", "0", "0", "0", "0", "0", "40", "0", "0", "0", "30", "0", "0", "0", "0", "0", "0", "0", "0" ],
                            // [ "memory", "10", "0", "0", "0", "0", "0", "0", "0", "0", "20", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0" ]
                    ]
                }
            } else {
                debugger
            }
            return result;
        };

        $scope.makeColumn = (name, field, chartType ) => {
            const data =  chartType === 'traffic' ? $scope.trafficList : $scope.cpuList
            const result = data.map(v => v[field]);
            if(name == 'traffic') {
                const index = data.findIndex(v => v.collect_time == tools.store.formatDateTime($scope.ticket.fault_time));
                result.forEach((item, i) => {
                    if(i == index) {
                        result[i] =  Math.max(data[index].fltbpsin, data[index].fltbpsout, data[index].fltppsin, data[index].fltppsout);
                    } else result[i] = 0
                })
            }
            result.splice(0, 0, name);
            return result;
        };


        $scope.clickByPassBtn = () => {
            //Restful Request tsdn
            //$scope.selectedByPass[0].source_name => 우회경로 sysnameA
            //$scope.selectedByPass[0].target_name => 우회경로 sysnameZ
            /*
             element_id: "link_1"
             id: "1"
             source_bau: "SH1-S.2"
             source_dir: "1"
             source_id: "10.20.144.201"
             source_name: "중계_밀양_0329B"
             source_pau: "SH1-S.1"
             state: "UP"
             target_bau: "SH1-S.2"
             target_dir: "2"
             target_id: "192.168.200.216"
             target_name: "부산부경대"
             target_pau: "SH1-S.2"
             */

            tools.simulatorRequest(
                Constants.SimulatorService.STEP2,
                {
                    "service-req-key": "roadm_pw_test0003",
                    "node": [
                        {
                            "topology-id": "roadm-cwv-nodes-01",
                            "node-id": "10.10.41.11-WS1",
                            "is-root-node": "true"
                        },
                        {
                            "topology-id": "roadm-cwv-nodes-01",
                            "node-id": "10.10.41.55-WS1",
                            "is-root-node": "true"
                        }
                    ],
                    "line": [
                        {
                            "direction": "bi",
                            "source": {
                                "topology-id": "roadm-cwv-nodes-01",
                                "src-node-id": "10.10.41.11-WS1",
                                "src-tp-id": "CS1-S.3B",
                                "tag-list": [
                                    {
                                        "tag-index": "0",
                                        "tag-type": "lambda",
                                        "tag": "6"
                                    },
                                    {
                                        "tag-index": "1",
                                        "tag-type": "interface-type",
                                        "tag": "GBE"
                                    }
                                ]
                            },
                            "destination": {
                                "topology-id": "roadm-cwv-nodes-01",
                                "dst-node-id": "10.10.41.55-WS1",
                                "dst-tp-id": "CS1-S.3A",
                                "tag-list": [
                                    {
                                        "tag-index": "0",
                                        "tag-type": "lambda",
                                        "tag": 6
                                    },
                                    {
                                        "tag-index": "1",
                                        "tag-type": "interface-type",
                                        "tag": "GBE"
                                    }
                                ]
                            },
                            "cir": "0",
                            "pir": "0",
                            "cbs": "1",
                            "pbs": "1"
                        }
                    ],
                    "result-callback": {
                        "ok-url": location.origin + "/tsdn/selfconfig/service",
                        "fail-url": location.origin + "/tsdn/selfconfig/service"
                    },
                    "use-reserve-pce": "true",
                    "reserved-num": "0",
                    "pce": {
                        "pce": [
                            {
                                "cost": "8",
                                "link": [
                                    {
                                        "src-node-id": "10.10.41.11-WS1",
                                        "src-tp-id": "WS1-S.6",
                                        "dst-node-id": "10.10.41.3-WS1",
                                        "dst-tp-id": "WS1-S.2"
                                    },
                                    {
                                        "src-node-id": "10.10.41.3-WS1",
                                        "src-tp-id": "WS1-S.6",
                                        "dst-node-id": "10.82.48.168-RS",
                                        "dst-tp-id": "RS-S.1"
                                    },
                                    {
                                        "src-node-id": "10.82.48.168-RS",
                                        "src-tp-id": "RS-S.1",
                                        "dst-node-id": "10.10.41.7-WS1",
                                        "dst-tp-id": "WS1-S.2"
                                    },
                                    {
                                        "src-node-id": "10.10.41.7-WS1",
                                        "src-tp-id": "WS1-S.16",
                                        "dst-node-id": "10.10.41.9-WS1",
                                        "dst-tp-id": "WS1-S.12"
                                    },
                                    {
                                        "src-node-id": "10.10.41.9-WS1",
                                        "src-tp-id": "WS1-S.16",
                                        "dst-node-id": "10.85.112.156-RS",
                                        "dst-tp-id": "RS-S.1"
                                    },
                                    {
                                        "src-node-id": "10.85.112.156-RS",
                                        "src-tp-id": "RS-S.1",
                                        "dst-node-id": "10.71.48.139-RS",
                                        "dst-tp-id": "RS-S.1"
                                    },
                                    {
                                        "src-node-id": "10.71.48.139-RS",
                                        "src-tp-id": "RS-S.1",
                                        "dst-node-id": "10.66.177.48-RS",
                                        "dst-tp-id": "RS-S.1"
                                    },
                                    {
                                        "src-node-id": "10.66.177.48-RS",
                                        "src-tp-id": "RS-S.1",
                                        "dst-node-id": "10.10.41.55-WS1",
                                        "dst-tp-id": "WS1-S.2"
                                    }
                                ]
                            }
                        ],
                        "topology-id": "roadm-cwv-nodes-01",
                        "src-node-id": "10.10.41.11-WS1",
                        "src-tp-id": "CS1-S.3B",
                        "dst-node-id": "10.10.41.55-WS1",
                        "dst-tp-id": "CS1-S.3A",
                        "cir": "1",
                        "pir": "1",
                        "count": "1"
                    }
                },
                res => {
                    let result = {
                        serviceId: '1111111',
                        message: '',
                        isSuccess: true,
                        reservedNum: '2'
                    };

                    // tools.http({
                    //     service: tools.constants.Service.rca,
                    //     action: "UPDATE_TSDN_SERVICE_DATA",
                    //     service_id: result.serviceId,
                    //     request_id: result.reservedNum,
                    //     ticket_id: $scope.ticket.ticket_id,
                    //     result: result.isSuccess
                    // });

                    tools.http({
                        service: tools.constants.Service.rca,
                        action: tools.constants.Action.CHANGE_TICKET_STATUS,
                        eventType: 'REQUEST_CHANGE_TICKET_STATUS',
                        status: tools.constants.TicketStatus.ACK.code,
                        ticket_id: $scope.ticket.ticket_id,
                        ticket_type: $scope.ticket.ticket_type,
                        ticket_result: $scope.ticket.ticket_rca_result_dtl_code || $scope.ticket.ticket_rca_result_code,
                        detail: result.message,
                        request_id: result.reservedNum,
                        service_id: result.serviceId,
                        result: result.isSuccess
                    }, function (result) {
                        console.log("ACK result", result);
                        tools.createConfirmDlg('경로우회요청을 하였습니다.', null, null);
                    });

                    setTimeout(() => {
                        tools.http({
                            service: tools.constants.Service.rca,
                            action: tools.constants.Action.CHANGE_TICKET_STATUS,
                            eventType: 'REQUEST_CHANGE_TICKET_STATUS',
                            ticket_id: $scope.ticket.ticket_id,
                            status: tools.constants.TicketStatus.FIN.code,
                            ticket_type: $scope.ticket.ticket_type,
                            sop_id: $scope.ticket.sop_id
                        }, function (result) {
                            console.log("마감 result", result);

                        })
                    }, 7000)
                }//callback
            )

        };

        $scope.openBypassRouteListDialog = () => { // 우회 경로 가능 리스트 팝업
            $mdDialog.show({
                controller: tools.store.viewType.bypass_route_list.controller,
                templateUrl: tools.store.viewType.bypass_route_list.path,
                locals: { param: { data: $scope.ticket } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.openSOPListDialog = () => { // Edit SOP 팝업 오픈
            $mdDialog.show({
                controller: tools.store.viewType.SOP_edit_list.controller,
                templateUrl: tools.store.viewType.SOP_edit_list.path,
                locals: { param: { ticket: $scope.ticket, test: 'test' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };
        
        $scope.openDateSetting = (event) => { //데이터 스냅샷 팝업
            $mdDialog.show({
                controller: tools.store.viewType.date_setting.controller,
                templateUrl: tools.store.viewType.date_setting.path,
                locals: { param: { ticket: $scope.ticket } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: true, // Only for -xs, -sm breakpoints.
                multiple: true
            })
                .then(function (result) {
                }, function () {
                });
        };

        $scope.loadNiaDiscoveryPce = () => {
            tools.simulatorRequest(
                Constants.SimulatorService.STEP1,
                {
                    "input": {
                        "dst-tp-id": "S07-P1",
                        "topology-id": "potn-wrn-ems-01",
                        "count": "2",
                        "pir": "0",
                        "dst-node-id": "DAEJEON",
                        "cir": "0",
                        "src-node-id": "SEOUL",
                        "src-tp-id": "S07-P1"
                    }
                },
                res => {
                    $scope.tsdnDiscoveryResult = res;
                    let data = angular.copy(tools.store.niaData);
                    $scope.tsdnTopologyData = res.output.pce[0].link.map(v =>
                        data.links.find(v2 => {
                            let srcId = v['src-node-id'];
                            let dstId = v['dst-node-id'];
                            return v2.source_id == srcId.substr(0, srcId.indexOf('')) && v2.target_id == dstId.substr(0, dstId.indexOf(''))
                        }))
                }
            )
        };

        $scope.onChangeSelectedSop = (sop) => {
            if(sop.selected) {
                $scope.selectedSop = sop;
                $scope.configSopList.map((list) => {
                    // debugger
                    if(sop.ticket_id == list.ticket_id) {
                        list.selected = true;
                    } else {
                        list.selected = false;
                    }
                });
            } else {
                $scope.selectedSop = null;
            }

            [$scope.operatorOpinion.faultClassify, $scope.operatorOpinion.faultType, $scope.operatorOpinion.faultDetail, $scope.operatorOpinion.etcContent] = [$scope.selectedSop.fault_classify, $scope.selectedSop.fault_type, $scope.selectedSop.fault_detail_content, $scope.selectedSop.etc_content ];

            document.querySelector('.recommendDetailConfig').innerHTML = "&nbsp;" + ($scope.selectedSop ? $scope.selectedSop.detail : '');
            document.querySelector('.faultClassifyConfig').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_classify || '') : '');
            document.querySelector('.faultTypeConfig').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_type || '') : '');
            document.querySelector('.faultDetailConfig').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_detail_content || '') : '');
        };

        let isLoading = false
        $scope.getSopDataList = () => {
            if(isLoading) return 
            isLoading = true
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_SOP_PAGE2_LIST",
                    ROOT_CAUSE_SYSNAMEA: $scope.ticket.root_cause_sysnamea,
                    TICKET_TYPE: $scope.ticket.ticket_type,
                    LIMIT : 100
                }, function (result) {
                    isLoading = false
                    $scope.configSopList  = result;
                    // 연관SOP리스트
                    $scope.relatedSopList = result.list.filter(item => item.fault_detail_content !== '');
                    $scope.model_config.text = $scope.model_config.text.replace('${장애구분}', $scope.relatedSopList.length !== 0 ? $scope.relatedSopList[0].fault_classify : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${장애유형}', $scope.relatedSopList.length !== 0 ? $scope.relatedSopList[0].fault_type : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${조치내용}', $scope.relatedSopList.length !== 0 ? $scope.relatedSopList[0].fault_detail_content : '');
                });
        };

        $scope.showFinProcessDialog = () => {
            $mdDialog.show({
                controller: tools.store.viewType.fin_process_dialog.controller,
                templateUrl: tools.store.viewType.fin_process_dialog.path,
                locals: { param: { ticket: $scope.ticket, opinion: $scope.operatorOpinion} },
                parent: angular.element(document.body),
                disableParentScroll: false,
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            }).then(function (result) {
                $scope.changeTicketStatus(
                    tools.store.niaList.find(v => v.ticket_id == result.ticket_id),
                    result.status
                );
            });
        };

        $scope.openTicketFinModal = (ticket, event, work) => {
            
            let processing = tools.constants.TicketProcessing[work] || {};
            let page = tools.store.viewType[processing.view];

            if(!ticket || !page) {
                console.error('파라미터 입력값 오류');
                return;
            }

            $mdDialog.show({
                locals: {
                    param: { ticket: (Array.isArray(ticket) ? ticket : [ticket]), processing: processing }
                },
                controller: page.controller,
                templateUrl: page.path,
                parent: angular.element(document.querySelector('.md-dialog-container')),
                targetEvent: event,
                clickOutsideToClose: true,
                multiple: true,
                fullscreen: false // Only for -xs, -sm breakpoints.
            }).then(function (result) {
                $scope.changeTicketStatus(
                    tools.store.niaList.find(v => v.ticket_id == result.ticket_id),
                    result.status
                );
            }, function () {
            });
        };

        $scope.changeTicketStatus = (ticket, status) => {
            if(!status) {
                return;
            }
            ticket.status = status;
            $scope.safeApply();
        };

        $scope.loadNiaAlarmList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_CABLE_ALARM_LIST",
            }, (result) => {
                $scope.niaCableAlarms = result;
            });
        };

        $scope.openConfigTestAction = () => {
            $mdDialog.show({
                controller: tools.store.viewType.config_test_action.controller,
                templateUrl: tools.store.viewType.config_test_action.path,
                locals: { param: { ticket: $scope.ticket, test: 'test' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.loadTrafficList = () => {
            tools.http( $scope.getLoadTrafficParam($scope.ticket), (result) => {

                $scope.trafficList = result;
                $scope.trafficList.forEach(list => list.collect_time = tools.store.formatDateTime(list.collect_time));

                if( $scope.ticket.ticket_type == 'NTT' && $scope.trafficList.length > 0 ) {

                    const date  = $scope.trafficList[0].collect_time;

                    $scope.trafficList.splice(0, 0, { collect_time: moment(date).subtract(5, 'minute').format('YYYY-MM-DD HH:mm:ss'), strbytes_col: 0, strcounts: 0 });
                    $scope.trafficList.splice(0, 0, { collect_time: moment(date).subtract(10, 'minute').format('YYYY-MM-DD HH:mm:ss'), strbytes_col: 0, strcounts: 0 });
                    $scope.trafficList.push({ collect_time: moment(date).add(5, 'minute').format('YYYY-MM-DD HH:mm:ss'), strbytes_col: 0, strcounts: 0 });
                    $scope.trafficList.push({ collect_time: moment(date).add(10, 'minute').format('YYYY-MM-DD HH:mm:ss'), strbytes_col: 0, strcounts: 0 });
                }

                if($scope.trafficList.length > 0) {
                    $scope.drawChart();
                }

                $scope.progress_activated = false;
            });
        };

        $scope.getLoadTrafficParam = (ticket) => {
            // debugger
            let param = {};
            if(ticket.ticket_type == 'ATT2') {
                param = {
                    action: 'SELECT_ATT2_TRAFFIC_LIST',
                    NODE: ticket.root_cause_sysnamea || 'daejeon-7712',
                    INTERFACE: ticket.root_cause_porta || 'ce4/1',
                    FAULT_TIME: moment(ticket.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
                };
            } else if(ticket.ticket_type == 'NTT') {
                param = {
                    action: 'SELECT_NTT_TRAFFIC_LIST',
                    TICKET_ID: ticket.ticket_id,
                    START_NODE: ticket.root_cause_sysnamea || '',
                    START_INTERFACE: ticket.root_cause_porta || '',
                    END_NODE: ticket.root_cause_sysnamez || '',
                    END_INTERFACE: ticket.root_cause_portz || '',
                    FAULT_TIME: moment(ticket.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
                };
            }
            return Object.assign( { service: tools.constants.Service.rca }, param);
        };

        // CPU MEM List
        $scope.loadCpuMemList = () => {
            const { ticket } = $scope
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_EQUIP_AMOUNT_USED_LIST",
                NODE: ticket.root_cause_sysnamea || 'daejeon-7712',
                START_DATE: ticket.fault_time ? moment(ticket.fault_time).subtract(10, "minute").format('YYYY-MM-DD HH:mm:ss') : null,
                END_DATE: ticket.fault_time ? moment(ticket.fault_time).add(10, "minute").format('YYYY-MM-DD HH:mm:ss') : null
            }, function (result) {
                if (!result) return;
                $scope.cpuList = result;
                // debugger
                $scope.cpuList.forEach(item => item.measured_datetime = tools.store.formatDateTime(item.measured_datetime));

                if($scope.cpuList.length > 0) {
                    $scope.drawCpuChart();
                }
                $scope.progress_activated = false;
            })
        };

        $scope.requestAnomalousData = () => {
            const { ticket } = $scope
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_ANOMALOUS_ONE",
                NODE: ticket.root_cause_sysnamea || 'daejeon-7712',
                INTERFACE: ticket.root_cause_porta || 'ce4/1',
                FAULT_TIME: moment(ticket.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
            }, function (result) {
                if (!result) return;
                $scope.requestAnomalousOne = result;
            })
        };

        $scope.requestNodeFactorData = () => {
            const { ticket } = $scope
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NODEFACTOR_ONE",
                NODE: ticket.root_cause_sysnamea || 'daejeon-7712',
                FAULT_TIME: moment(ticket.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
            }, function (result) {
                if (!result) return;
                $scope.requestNodeFactorOne = result;
            })
        };

        $scope.onInit = () => {
            $scope.loadTrafficList();
            $scope.loadCpuMemList();
            $scope.getSopDataList();
            $scope.requestAnomalousData();
            $scope.requestNodeFactorData();
        };

        angular.element(document).ready(() => {
            if ($scope.ticket.ticket_type == "PF") {
                $scope.loadNiaAlarmList();
                $scope.loadNiaDiscoveryPce();
            } 

            // if ($scope.ticket.status == "INIT" /* || $scope.ticket.status == "FIN" || $scope.ticket.status == "AUTO_FIN" */) {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_ALARM_EQUIPTYPE_COUNT_LIST",
                    CLUSTERNO: $scope.ticket.cluster_no
                }, function (result) {
                    $scope.damageScaleList = result.map(function (v) {
                        return v.equiptype + ": " + v.equip_count;
                    });

                    $scope.model_config.text = document.querySelector('#template-requestDoc-config').innerHTML;
                    $scope.model_config.text = $scope.model_config.text.replace('${AGENCY_NAME}', tools.store.agency_name);
                    $scope.model_config.text = $scope.model_config.text.replace('${발신자}', tools.store.userName);
                    $scope.model_config.text = $scope.model_config.text.replace('${수신자}', ' ');
                    $scope.model_config.text = $scope.model_config.text.replace('${티켓번호}', $scope.ticket.ticket_id);
                    $scope.model_config.text = $scope.model_config.text.replace('${티켓타입}', $scope.ticket.ticket_type);
                    $scope.model_config.text = $scope.model_config.text.replace('${발생시간}', moment($scope.ticket.fault_time).format('YYYY-MM-DD HH:mm:ss') || '');
                    $scope.model_config.text = $scope.model_config.text.replace('${발생원인}', $scope.ticket.ticket_rca_result_dtl_code);
                    $scope.model_config.text = $scope.model_config.text.replace('${작업요청구간}', ($scope.failureInfo.source_name || '') + ' → ' + ($scope.failureInfo.target_name || ''));
                    $scope.model_config.text = $scope.model_config.text.replace('${피해규모}', ($scope.failureInfo.source_name || '') + ($scope.failureInfo.source_port || '') + ' → ' + ($scope.failureInfo.target_name || '')  + ($scope.failureInfo.target_port || '') );

                    $scope.model_config.text = $scope.ticket.ticket_type==='NTT' || $scope.ticket.ticket_type==='ATT2' ? $scope.model_config.text.replace('${trafficTca-requestDoc-config}', document.querySelector('#trafficTca-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${trafficTca-requestDoc-config}', '');
                    $scope.model_config.text = $scope.ticket.zero1_entropy ? $scope.model_config.text.replace('${available_percentage}',((1-$scope.ticket.zero1_entropy)*100).toFixed(1)+'%') : $scope.model_config.text.replace('${available_percentage}',''); 
                    $scope.model_config.text = $scope.ticket.zero1_entropy ? $scope.model_config.text.replace('${invalidity_percentage}', ($scope.ticket.zero1_entropy*100).toFixed(1)+'%') : $scope.model_config.text.replace('${invalidity_percentage}',''); 

                    $scope.model_config.text = $scope.ticket.ticket_type==='FTT' ? $scope.model_config.text.replace('${ftt-requestDoc-config}', document.querySelector('#ftt-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${ftt-requestDoc-config}', '');
                    $scope.model_config.text = $scope.model_config.text.replace('${issue_percentage}', ($scope.ticket.zero1_entropy*100).toFixed(1)+'%');
                    $scope.model_config.text = $scope.model_config.text.replace('${no_issue_percentage}',((1-$scope.ticket.zero1_entropy)*100).toFixed(1)+'%');
                    
                    $scope.model_config.text = $scope.ticket.ticket_type==='NFTT' ? $scope.model_config.text.replace('${nftt-requestDoc-config}', document.querySelector('#nftt-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${nftt-requestDoc-config}', '');
                    $scope.model_config.text = $scope.model_config.text.replace('${measured_datetime}', $scope.requestNodeFactorOne.measured_datetime);
                    $scope.model_config.text = $scope.model_config.text.replace('${cpu_predicted}', $scope.requestNodeFactorOne.cpu_predicted+'%');
                    $scope.model_config.text = $scope.model_config.text.replace('${mem_predicted}', $scope.requestNodeFactorOne.mem_predicted+'%');

                    $scope.model_config.text = $scope.ticket.ticket_type==='ATT2' ? $scope.model_config.text.replace('${att2-requestDoc-config}', document.querySelector('#att2-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${att2-requestDoc-config}', '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin}', $scope.requestAnomalousOne.fltbpsin != null ? $scope.requestAnomalousOne.fltbpsin.toLocaleString()+' MB' : '') ;
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_predict}', $scope.requestAnomalousOne.fltbpsin_predict != null ? $scope.requestAnomalousOne.fltbpsin_predict.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_threshold_upper}', $scope.requestAnomalousOne.fltbpsin_threshold_upper != null ? $scope.requestAnomalousOne.fltbpsin_threshold_upper.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_threshold_lower}', $scope.requestAnomalousOne.fltbpsin_threshold_lower != null ? $scope.requestAnomalousOne.fltbpsin_threshold_lower.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_anomaly}', $scope.requestAnomalousOne.fltbpsin_anomaly != null ? $scope.requestAnomalousOne.fltbpsin_anomaly : '');
                    
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout}', $scope.requestAnomalousOne.fltbpsout != null ? $scope.requestAnomalousOne.fltbpsout.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_predict}', $scope.requestAnomalousOne.fltbpsout_predict != null ? $scope.requestAnomalousOne.fltbpsout_predict.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_threshold_upper}', $scope.requestAnomalousOne.fltbpsout_threshold_upper != null ? $scope.requestAnomalousOne.fltbpsout_threshold_upper.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_threshold_lower}', $scope.requestAnomalousOne.fltbpsout_threshold_lower != null ? $scope.requestAnomalousOne.fltbpsout_threshold_lower.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_anomaly}', $scope.requestAnomalousOne.fltbpsout_anomaly != null ? $scope.requestAnomalousOne.fltbpsout_anomaly : '');

                });
        });

        $scope.sopCodeList = {}
        $scope.loadSopCodeList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_CODE_PAGE2_LIST"
            }, function (result) {
                if(result && result.list.length > 0) {
                    const list = result.list || []
                    $scope.sopCodeList = list.reduce((acc, cur, index) => {
                        let g = acc[cur.fault_gb] 
                        if(!g) {
                            g = (acc[cur.fault_gb] = [])
                        }
                        g.push(cur)
                        return acc
                    }, {})
                } else {
                    $scope.sopCodeList = {};
                }
            })
        };

        angular.element(document).ready(() => {
            $(window).resize(function () {
                $("table.selfConfig.sopCodeList > *").width($("table.selfConfig.sopCodeList").width() + $("table.selfConfig.sopCodeList").scrollLeft());
                $("table.selfConfig2 > *").width($("table.selfConfig2").width() + $("table.selfConfig2").scrollLeft());
            });
            $("table.selfConfig.sopCodeList").on('scroll', function () {
                $("table.selfConfig.sopCodeList > *").width($("table.selfConfig.sopCodeList").width() + $("table.selfConfig.sopCodeList").scrollLeft());
            });
            $("table.selfConfig2").on('scroll', function () {
                $("table.selfConfig2 > *").width($("table.selfConfig2").width() + $("table.selfConfig2").scrollLeft());
            });
            $(".aiProcess-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
            $scope.loadSopCodeList()
            $scope.$on('onChangedSopCode', function(e){
                $scope.loadSopCodeList()
            });
        });
    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', SelfConfigurationDialogCtrl];
