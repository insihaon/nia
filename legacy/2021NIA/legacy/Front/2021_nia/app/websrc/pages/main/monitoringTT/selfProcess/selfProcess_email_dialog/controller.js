import BaseController from 'scripts/controller/baseController'

class SelfProcessEmailDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param, $window, $interval) {
        $scope.config = tools.store.viewType.selfProcess_email_dialog;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $window, $interval);

        $scope.param = param.data;

        $scope.alarmList = [];
        $scope.trafficList = [];

        $scope.feedback='';

        $scope.model_config = {
            text: '',
            ngModel: ''
        };
        
        $scope.requestAnomalousOne = {};
        
        $scope.ex_text = '';

        $scope.explanationText = () => {
            $scope.ex_text = document.querySelector('#selfprocess-explanation-config').innerHTML;
            if($scope.param.ticket_id){
                $scope.ex_text = $scope.ex_text.replace('${data_type}', '티켓 번호');
                $scope.ex_text = $scope.ex_text.replace('${data_num}', param.data.ticket_id);
            }else{
                $scope.ex_text = $scope.ex_text.replace('${data_type}', '알람 번호');
                $scope.ex_text = $scope.ex_text.replace('${data_num}', param.data.alarmno);
            }
        }

        const currentUrl = 'http://localhost:8080/#/nia/nia.selfProcess/?'; // 운영 : http://116.89.191.47:8080/#/nia/nia.selfProcess/?
        const separator = currentUrl.includes('?') ? '&' : '?';
        const queryStringType = $scope.param.self_process_group === 'ST' ? 'alarmno=' : 'ticket_id='
        const queryStringData = $scope.param.self_process_group === 'ST' ? $scope.param.alarmno : $scope.param.ticket_id
        const isEmail = true
        $scope.url = currentUrl + queryStringType + queryStringData + separator + 'isEmail='+isEmail + separator +'self_process_group='+ $scope.param.self_process_group + separator + 'self_process_type='+ $scope.param.self_process_type;
        // http://localhost:8080/#/nia/nia.selfProcess/?ticket_id=1531739&isEmail=true&self_process_group=SO&self_process_type=N //유해트래픽
        // http://localhost:8080/#/nia/nia.selfProcess/?ticket_id=1515114&isEmail=true&self_process_group=SO&self_process_type=A //이상트래픽
        // http://localhost:8080/#/nia/nia.selfProcess/?alarmno=10119631&isEmail=true&self_process_group=ST&self_process_type=S //SYSLOG
        // http://localhost:8080/#/nia/nia.selfProcess/?ticket_id=10119631&isEmail=true&self_process_group=ST&self_process_type=F //비장애

        $scope.ticket = tools.store.niaList.find(v => v.ticket_id === param.data.ticket_id)

        $scope.progress_activated = true;

        $scope.openAIProcess = (param) => {
            if (param.clos_status == 'Y') {
                const message = param.self_process_type === 'S' ? '이미 마감된 알람입니다.' : '이미 마감된 티켓입니다.'
                $tools.showAlert('안내', message).then(function (result) {
                    if (result) {
                        param.self_process_type === 'S' ? $scope.openSyslogconfigTemplate(param) : $scope.openSelfconfigTemplate(param)
                        $scope.changeClosType();
                    }
                });
            }else{
                param.self_process_type === 'S' ? $scope.openSyslogconfigTemplate(param) : $scope.openSelfconfigTemplate(param)
                        $scope.changeClosType();
            }
        };

        $scope.openSelfconfigTemplate = (param) => {
            $mdDialog.show({
                controller: tools.store.viewType.ai_process_template.controller,
                templateUrl: tools.store.viewType.ai_process_template.path,
                locals: { param: $scope.ticketDetailData },
                parent: angular.element(document.body),
                disableParentScroll: false,
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false
            }).then(function (result) {
                if (result) {
                    return true;
                }
            });
        }
        $scope.openSyslogconfigTemplate = (param) => {
            const syslogData = Object.assign($scope.syslogDetailData, param)
            $mdDialog.show({
                controller: tools.store.viewType.syslog_configuration_template.controller,
                templateUrl: tools.store.viewType.syslog_configuration_template.path,
                locals: { param: $scope.syslogDetailData },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            }).then(function (result) {
                if (result) {
                    return true;
                }
            });
        }

        $scope.changeClosType = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "UPDATE_SELF_PROCESS_CLOS_TYPE",
                TICKET_ID: $scope.param.ticket_id != '' ? $scope.param.ticket_id : null,
                ALARMNO: $scope.param.alarmno != '' ? $scope.param.alarmno : null
            }, function (result) {
                if (!result) return true;
                // debugger
            })
        }

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
                            { value:  tools.store.formatDateTime($scope.param.fault_time), class: 'fault-time-grid' }
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

        $scope.getChartColumn = (chartType) => {
            let result = [];
            // const ticket = $scope.ticket
            if(chartType === 'traffic')  {
                if($scope.param.ticket_type == 'ATT2') {
                    result = [
                        $scope.makeColumn('date', 'measured_datetime',chartType),
                        $scope.makeColumn('bps_in', 'fltbpsin',chartType),
                        $scope.makeColumn('bps_out', 'fltbpsout',chartType),
                        $scope.makeColumn('pps_in', 'fltppsin',chartType),
                        $scope.makeColumn('pps_out', 'fltppsout',chartType),
                    ]
                } else if($scope.param.ticket_type == 'NTT') {
                    result = [
                        $scope.makeColumn('date', 'collect_time',chartType),
                        $scope.makeColumn('strbytes_col', 'strbytes_col',chartType),
                        $scope.makeColumn('strcounts', 'strcounts',chartType),
                    ]
                }
            }else{
                // debugger
            }
           
            return result;
        };

        $scope.makeColumn = (name, field, chartType ) => {
            const data =  chartType === 'traffic' ? $scope.trafficList : null
            const result = data.map(v => v[field]);
            if(name == 'traffic') {
                const index = data.findIndex(v => v.collect_time == tools.store.formatDateTime($scope.param.fault_time));
                
                result.forEach((item, i) => {
                    if(i == index) {
                        result[i] =  Math.max(data[index].fltbpsin, data[index].fltbpsout, data[index].fltppsin, data[index].fltppsout);
                    } else result[i] = 0
                })
            }
            result.splice(0, 0, name);
            return result;
        };

        $scope.getConditionalTraffic = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SELF_PROCESS_TRAFFIC_ONE",
                TICKET_ID: $scope.param.ticket_id != '' ? $scope.param.ticket_id : null,
            }, function (result) {
                $scope.conditionalTrafficInfo = result
                $scope.getSopDataList($scope.conditionalTrafficInfo);
                tools.http( 
                    $scope.getLoadTrafficParam($scope.conditionalTrafficInfo), (result) => {
                    $scope.trafficList = result;
                    $scope.trafficList.forEach(list => list.collect_time = tools.store.formatDateTime(list.collect_time));
                    if( $scope.param.ticket_type == 'NTT' && $scope.trafficList.length > 0 ) {

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
                })
            })
        }

        $scope.getLoadTrafficParam = (info) => {
            let param = {};
            if($scope.param.ticket_type == 'ATT2') {
                param = {
                    action: 'SELECT_ATT2_TRAFFIC_LIST',
                    NODE: info.root_cause_sysnamea || 'daejeon-7712',
                    INTERFACE: info.root_cause_porta || 'ce4/1',
                    FAULT_TIME: moment($scope.param.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
                };
            } else if($scope.param.ticket_type == 'NTT') {
                param = {
                    action: 'SELECT_NTT_TRAFFIC_LIST',
                    TICKET_ID: info.ticket_id,
                    START_NODE: info.root_cause_sysnamea || '',
                    START_INTERFACE: info.root_cause_porta || '',
                    END_NODE: info.root_cause_sysnamez || '',
                    END_INTERFACE: info.root_cause_portz || '',
                    FAULT_TIME: moment($scope.param.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
                };
            }
            return Object.assign( { service: tools.constants.Service.rca }, param);
        };
        
        $scope.getsyslogSopHistList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SYSLOG_SOP_HIST_LIST",
                IFNAME: $scope.param.alarmloc != '' ? $scope.param.alarmloc : null,
                EQUIPMENT: $scope.param.node_nm != '' ? $scope.param.node_nm : null
            }, function (result) {
                if (!result) return true;
                $scope.syslogSopHistList = result.filter(item => item.fault_detail_content !== '');
                // $scope.syslogDetailData.node_nm && $scope.syslogDetailData.alarmloc 에 대한 과거 sop이력 조회
            })
        };

        let isLoading = false
        $scope.getSopDataList = (info) => {
            if(isLoading) return 
            isLoading = true
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_SOP_PAGE2_LIST",
                    ROOT_CAUSE_SYSNAMEA: info.root_cause_sysnamea,
                    TICKET_TYPE: $scope.param.ticket_type
                }, function (result) {
                    isLoading = false
                    $scope.relatedSopList = result.list.filter(item => item.fault_detail_content !== '');
                });
        };

        $scope.getsyslogDetailInfo = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SELF_PROCESS_SYSLOG_DETAIL_ONE",
                ALARMNO: $scope.param.alarmno != '' ? $scope.param.alarmno : null
            }, function (result) {
                if (!result) return true;
                $scope.syslogDetailData = result;
            })
        };

        $scope.getTicketDetailInfo = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_TICKET_LIST",
                TICKET_ID: $scope.param.ticket_id != '' ? $scope.param.ticket_id : null,
            }, function (result) {
                if (!result) return true;
                $scope.ticketDetailData = result[0];
                $scope.requestAnomalousData();
            })
        };

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetailContent: ''
        };

        const emit = () => {
            $scope.$rootScope.$broadcast('onChangedSopCode', {});
        }

        $scope.selfprocessSendEMail = () => {
            const template = $scope.param.self_process_type === 'A' ? document.querySelector('#att2-requestDoc-config').innerHTML : document.querySelector('#ntt-requestDoc-config').innerHTML
            tools.createConfirmDlg(null, "메일을 전송하시겠습니까?", null).then(() => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: 'SELECT_EMAIL',
                    RECEIVER: 'noc@koren.kr'/* $scope.emailReceiver.join(', ') */, /*test email: infobiz@koren.kr gidxhwl@hanmail.net  $scope.emailReceiver.join(', ') */
                    SUBJECT : moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss') + " 자가 처리 현황 알림",
                    // BODY: $('#att2-requestDoc-config').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                    BODY: $(template).html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                    TICKET_ID : $scope.param.ticket_id,
                    TICKET_TYPE: $scope.param.ticket_type,
                    // TICKET_RESULT : $scope.ticket.ticket_rca_result_dtl_code || $scope.ticket.ticket_rca_result_code,
                    USER_IDS : tools.store.userId,
                    FAULT_CLASSIFY: $scope.operatorOpinion.faultClassify || '',
                    FAULT_TYPE: $scope.operatorOpinion.faultType || '',
                    FAULT_DETAIL_CONTENT: $scope.operatorOpinion.faultDetail || '',
                    ETC_CONTENT: $scope.operatorOpinion.etcContent || '',
                    HANDLING_ACK_USER: tools.store.userName
                }, function (result) {
                    if(result.send_result){
                        tools.createConfirmDlg('담당자에게 메일이 발송되었습니다.', null, null);
                        $scope.isAutoCloseTicket = false;
                    } else { tools.createConfirmDlg('메일 발송이 실패하였습니다.', null, null); }
                });
            });
        };
        // 수동 마감 클릭시 실행하는 함수
        $scope.endManual = () => {
            tools.createConfirmDlg(null, "마감하시겠습니까?", null).then(() => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_TICKET_LIST",
                    PROC_STATUS : $scope.ticketInfoData.proc_status  != '' ? $scope.ticketInfoData.proc_status : null,
                    TICKET_ID: $scope.ticketInfoData.ticket_id  != '' ? $scope.ticketInfoData.ticket_id : null,
                    CONTENT_TEXT: $scope.ticketInfoData.textareaContent != '' ? $scope.ticketInfoData.textareaContent : null
                }, function (result) {
                    if (!result){
                        return true;
                    } 
                    tools.showAlert('마감되었습니다.').then(function () {
                        emit();
                        // $scope.dlgFunc.result({result: 'success'});
                        // $mdDialog.hide();
                        $scope.isAutoCloseTicket = true;
                        $mdDialog.cancel();
                    });
                })
            });
        }

        $scope.requestAnomalousData = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_ANOMALOUS_ONE",
                NODE: $scope.ticketDetailData.root_cause_sysnamea || 'daejeon-7712',
                INTERFACE: $scope.ticketDetailData.root_cause_porta || 'ce4/1',
                FAULT_TIME: moment($scope.ticketDetailData.fault_time).format('YYYY-MM-DD HH:mm:ss') || null
            }, function (result) {
                if (!result) return;
                $scope.requestAnomalousOne = result;
                if(param.data.ticket_type === 'ATT2' || param.data.ticket_type === 'NTT'){
                    $scope.model_config.text = document.querySelector('#selfprocess-ticket-config').innerHTML;
    
                    $scope.model_config.text = param.data.ticket_type==='NTT' ? $scope.model_config.text.replace('${ntt-requestDoc-config}', document.querySelector('#ntt-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${ntt-requestDoc-config}', '');
                    
                    $scope.model_config.text = param.data.ticket_type==='ATT2' && $scope.requestAnomalousOne.fltbpsin != null ? $scope.model_config.text.replace('${att2-requestDoc-config}', document.querySelector('#att2-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${att2-requestDoc-config}', document.querySelector('#ntt-requestDoc-config').innerHTML);
                    
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin}', $scope.requestAnomalousOne.fltbpsin != null ? $scope.requestAnomalousOne.fltbpsin.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_predict}', $scope.requestAnomalousOne.fltbpsin_predict != null ? $scope.requestAnomalousOne.fltbpsin_predict.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_threshold_upper}', $scope.requestAnomalousOne.fltbpsin_threshold_upper != null ? $scope.requestAnomalousOne.fltbpsin_threshold_upper.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_threshold_lower}', $scope.requestAnomalousOne.fltbpsin_threshold_lower != null ? $scope.requestAnomalousOne.fltbpsin_threshold_lower.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsin_anomaly}', $scope.requestAnomalousOne.fltbpsin_anomaly != null ? $scope.requestAnomalousOne.fltbpsin_anomaly.toLocaleString()+' MB' : '');
                    
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout}', $scope.requestAnomalousOne.fltbpsout != null ? $scope.requestAnomalousOne.fltbpsout.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_predict}', $scope.requestAnomalousOne.fltbpsout_predict != null ? $scope.requestAnomalousOne.fltbpsout_predict.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_threshold_upper}', $scope.requestAnomalousOne.fltbpsout_threshold_upper != null ? $scope.requestAnomalousOne.fltbpsout_threshold_upper.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_threshold_lower}', $scope.requestAnomalousOne.fltbpsout_threshold_lower != null ? $scope.requestAnomalousOne.fltbpsout_threshold_lower.toLocaleString()+' MB' : '');
                    $scope.model_config.text = $scope.model_config.text.replace('${fltbpsout_anomaly}', $scope.requestAnomalousOne.fltbpsout_anomaly != null ? $scope.requestAnomalousOne.fltbpsout_anomaly.toLocaleString()+' MB' : '');

                    $scope.model_config.text = $scope.model_config.text.replace('${ticket_id}', $scope.ticketDetailData.ticket_id);
                    $scope.model_config.text = $scope.model_config.text.replace('${ticket_type}', $scope.ticketDetailData.ticket_type);
                    $scope.model_config.text = $scope.model_config.text.replace('${root_cause_sysnamea}', $scope.ticketDetailData.root_cause_sysnamea);
                    $scope.model_config.text = $scope.model_config.text.replace('${root_cause_porta}', $scope.ticketDetailData.root_cause_porta);
                    $scope.model_config.text = $scope.model_config.text.replace('${root_cause_sysnamez}', $scope.ticketDetailData.root_cause_sysnamez);
                    $scope.model_config.text = $scope.model_config.text.replace('${root_cause_portz}', $scope.ticketDetailData.root_cause_portz);
                    $scope.model_config.text = $scope.model_config.text.replace('${fault_time}', moment($scope.ticketDetailData.fault_time).format('YYYY-MM-DD HH:mm:ss'));

                }else if(param.data.ticket_type === 'FTT'){
                    $scope.model_config.text = document.querySelector('#selfprocess-trafficIssue-config').innerHTML;
                    $scope.model_config.text = param.data.ticket_type==='FTT' ? $scope.model_config.text.replace('${ftt-requestDoc-config}', document.querySelector('#ftt-requestDoc-config').innerHTML) : $scope.model_config.text.replace('${ftt-requestDoc-config}', '');
                    $scope.model_config.text = $scope.model_config.text.replace('${issue_percentage}', ($scope.ticketDetailData.zero1_entropy*100).toFixed(1)+'%');
                    $scope.model_config.text = $scope.model_config.text.replace('${no_issue_percentage}',((1-$scope.ticketDetailData.zero1_entropy)*100).toFixed(1)+'%');
                }
            })
        };

        $scope.onInit = () => {
            $scope.getConditionalTraffic();
            $scope.getsyslogSopHistList();
            $scope.getsyslogDetailInfo();
            $scope.getTicketDetailInfo();
            // if($scope.param.self_process_group === 'ST'){
            // }else{
            // }
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#selfProcessDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $scope.explanationText();
        });

    }
}

SelfProcessEmailDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param', '$window' , '$interval'];

export default SelfProcessEmailDialogCtrl;
