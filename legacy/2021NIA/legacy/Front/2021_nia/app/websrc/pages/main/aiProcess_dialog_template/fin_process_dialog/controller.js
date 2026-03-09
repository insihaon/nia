import BaseController from 'scripts/controller/baseController'

class FinProcessDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect, param) {
        $scope.config = tools.store.viewType.fin_process_dialog;
        super($injector, $scope, tools, $http, $timeout);

        $scope.ai_accuracy = null;
        $scope.fault_type_content = null;

        $scope.onInit = () => {
            // setTimeout(() => {
            //     document.getElementsByClassName("fin-process-dialog")[0].click();
            //     console.log('open fin-process-dialog');
            // }, 300);

            $scope.ticket = param.ticket;
            $scope.alarm = param.alarm;
            $scope.opinion = param.opinion;

            if($scope.ticket){
                $scope.isTicket = $scope.ticket.root_cause_domain == "TRAFFIC" || '';
            }else{
                $scope.isAlarm = !!$scope.alarm || '';
            }

            $scope.isDataInfo = $scope.ticket ? $scope.isTicket : $scope.isAlarm;

            $scope.currentDate = new Date();
            $scope.startTime =  new Date(!$scope.ticket ? $scope.alarm.alarmtime : $scope.ticket.ticket_generation_time).addMinutes(-30);
            $scope.endTime =  new Date(!$scope.ticket ? $scope.alarm.alarmtime : $scope.ticket.ticket_generation_time).addMinutes(30);
        };

        $scope.changeTime = (type) => {
            if(type == 'start') {
                $scope.endTime =  new Date($scope.startTime).addMinutes(60);
            } else if(type == 'end') {     
                $scope.startTime =  new Date($scope.endTime).addMinutes(-60);
            }
        };

        $scope.submit = () => {
            if($scope.ai_accuracy == null) {
                tools.createConfirmDlg( 'AI 결과 피드백', 'AI 결과 피드백 여부를 선택해 주십시오.', null);
                return ;
            }

            tools.createConfirmDlg( '마감 처리', '확인 버튼 클릭 시 해당 티켓은 최종 마감처리 됩니다.', null).then(function() {
                $scope.successResult = [];
                    if($scope.ticket){
                        tools.http(createTicketParams($scope.ticket), function (result) {
                            $scope.successResult.push(!!(result.result));
                            if ($scope.successResult.length >= $scope.ticket.length) {
                                showToastAndClose();
                            }
                        });
                        showToastAndClose();
                    }else{
                        tools.http(createSyslogParams($scope.alarm), function (result) {
                            $scope.successResult.push(!!(result.result));
                            if ($scope.successResult.length >= $scope.ticket.length) {
                                showToastAndClose();
                            }
                        });
                        showToastAndClose();
                    }
                }, function() {
                });

                function createTicketParams(ticket) {
                    return {
                        service: tools.constants.Service.rca,
                        action: tools.constants.Action.CHANGE_TICKET_STATUS,
                        eventType: 'REQUEST_CHANGE_TICKET_STATUS',
                        ticket_id: ticket.ticket_id,
                        status: tools.constants.TicketStatus.FIN.code,
                        ticket_type: ticket.ticket_type,
                        sop_id : ticket.sop_id,
                        detail: ticket.handling_fin_content,
                        ai_accuracy: $scope.ai_accuracy,
                        fault_classify: $scope.opinion.faultClassify,
                        fault_type: $scope.opinion.faultType,
                        fault_detail_content: $scope.opinion.faultDetail,
                        etc_content: $scope.opinion.etcContent,
                        fault_type_content: $scope.ai_accuracy == 1 ? $scope.fault_type_content : null,
                        start_time:  $scope.ai_accuracy == 1 ? $scope.startTime : null,
                        end_time:  $scope.ai_accuracy == 1 ? $scope.endTime : null,
                        handling_fin_user: tools.store.userName
                    }
                }

                function createSyslogParams(alarm) {
                    return {
                        service: tools.constants.Service.rca,
                        action: tools.constants.Action.CHANGE_SYSLOG_STATUS,
                        eventType: 'REQUEST_CHANGE_SYSLOG_STATUS',
                        alarmno: alarm.alarmno,
                        status: tools.constants.TicketStatus.FIN.code,
                        alarmtime: moment(alarm.alarmtime).format('YYYY-MM-DD HH:mm:ss'),
                        node_num: alarm.node_num,
                        node_nm: alarm.node_nm,
                        alarmloc: alarm.alarmloc || '',
                        alarmmsg: alarm.alarmmsg,
                        etc: alarm.etc,
                        ip_addr: alarm.ip_addr,
                        ai_accuracy: $scope.ai_accuracy,
                        fault_classify: $scope.opinion.faultClassify,
                        fault_type: $scope.opinion.faultType,
                        fault_detail_content: $scope.opinion.faultDetail,
                        etc_content: $scope.opinion.etcContent,
                        fault_type_content: $scope.ai_accuracy == 1 ? $scope.fault_type_content : null,
                        start_time:  $scope.ai_accuracy == 1 ? $scope.startTime : null,
                        end_time:  $scope.ai_accuracy == 1 ? $scope.endTime : null,
                        handling_fin_user: tools.store.userName
                    }
                }

                function showToastAndClose() {
                    $scope.tools.showToast(tools.constants.Message.TICKET_FIN_COMPLETE);
                    $scope.dlgFunc.result({
                        ticket_id: $scope.ticket.ticket_id,
                        status: 'FIN'
                    });
                }
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#finProcessDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        $scope.dlgFunc.cancel = () => {
            $('.angular-meditor-toolbar--show').removeClass('angular-meditor-toolbar--show');
            $mdDialog.cancel();
        };
    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect', 'param', FinProcessDialogCtrl];
