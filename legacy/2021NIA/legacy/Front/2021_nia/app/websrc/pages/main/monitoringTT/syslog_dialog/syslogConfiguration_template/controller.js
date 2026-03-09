import BaseController from 'scripts/controller/baseController'

// class SyslogConfigurationDialogCtrl extends BaseController {
//     constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, param) {
//         $scope.config = tools.store.viewType.ai_process_template;
//         super($injector, $scope, tools, $http, $timeout);
        class SyslogConfigurationDialogCtrl extends BaseController {
            constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, param) {
                $scope.config = tools.store.viewType.ai_process_template;
                super($injector, $scope, tools, $http, $timeout);

        $scope.alarm = param;

        $scope.requestAnomalousOne = {};
        $scope.requestNodeFactorOne = {};

        $scope.emailReceiver = [];
        $scope.model_config = {
            text: '',
            ngModel: ''
        };
        $scope.selectedSop = {
            detail : '',
            fault_classify : '',
            fault_type : '',
            fault_detail_content : ''
        };
        $scope.configSopList = [];
        $scope.syslogSopHistList = [];
        $scope.syslogSopHistOne = {};

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetail: '',
            etcContent: ''
        };

        $scope.progress_activated = true;

        $scope.loadUserList= () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_USER_LIST",
                IS_ORGAN_LINK: true,
                NODE_ID: param.node_nm
            }, function (result) {
                $scope.managerList = result;
                $scope.managerList.map(v => v.selected = false);
            });
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
                SUBJECT : moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss') + " 장비 조치 요청서",
                BODY: $('.selfConfiguration div.angular-meditor-content').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                ALARMNO : $scope.alarm.alarmno,
                NODE_NM: $scope.alarm.node_nm,
                ALARMLOC : $scope.alarm.alarmloc,
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

        $scope.getsyslogSopHistList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SYSLOG_SOP_HIST_LIST",
                IFNAME: $scope.alarm.alarmloc != '' ? $scope.alarm.alarmloc : null,
                EQUIPMENT: $scope.alarm.node_nm != '' ? $scope.alarm.node_nm : null
            }, function (result) {
                if (!result) return true;
                // $scope.syslogSopHistList = result;
                $scope.syslogSopHistList = result.filter(item => item.fault_detail_content !== '');
                $scope.syslogSopHistOne = $scope.syslogSopHistList[0];
            })
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
                $scope.operatorOpinion = {
                    faultClassify: '',
                    faultType: '',
                    faultDetail: '',
                    etcContent: ''
                };
            }

            [$scope.operatorOpinion.faultClassify, $scope.operatorOpinion.faultType, $scope.operatorOpinion.faultDetail, $scope.operatorOpinion.etcContent] = [$scope.selectedSop.fault_classify, $scope.selectedSop.fault_type, $scope.selectedSop.fault_detail_content, $scope.selectedSop.etc_content ];

            document.querySelector('.recommendDetailConfig').innerHTML = "&nbsp;" + ($scope.selectedSop.detail ? $scope.selectedSop.detail : '');
            document.querySelector('.faultClassifyConfig').innerHTML = "&nbsp;" + ($scope.selectedSop.fault_classify ? $scope.selectedSop.fault_classify : $scope.syslogSopHistOne.fault_classify);
            document.querySelector('.faultTypeConfig').innerHTML = "&nbsp;" + ($scope.selectedSop.fault_type ? $scope.selectedSop.fault_type : $scope.syslogSopHistOne.fault_type);
            document.querySelector('.faultDetailConfig').innerHTML = "&nbsp;" + ($scope.selectedSop.fault_detail_content ? $scope.selectedSop.fault_detail_content : $scope.syslogSopHistOne.fault_detail_content);
        };

        $scope.showFinProcessDialog = () => {
            $mdDialog.show({
                controller: tools.store.viewType.fin_process_dialog.controller,
                templateUrl: tools.store.viewType.fin_process_dialog.path,
                locals: { param: { alarm: $scope.alarm, opinion: $scope.operatorOpinion} },
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

        $scope.openConfigTestAction = () => {
            $mdDialog.show({
                controller: tools.store.viewType.config_test_action.controller,
                templateUrl: tools.store.viewType.config_test_action.path,
                locals: { param: { alarm: $scope.alarm, test: 'test' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.onInit = () => {
            $scope.loadUserList();
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#syslogConfigTemplate');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $scope.getsyslogSopHistList();
            // if ($scope.alarm.status == "발생" /* || $scope.alarm.status == "마감" || $scope.alarm.status == "자동 마감" */) {
            setTimeout(() => {
                $scope.model_config.text = document.querySelector('#template-requestDoc-config').innerHTML;
                    $scope.model_config.text = $scope.model_config.text.replace('${AGENCY_NAME}', tools.store.agency_name);
                    $scope.model_config.text = $scope.model_config.text.replace('${발신자}', tools.store.userName);
                    $scope.model_config.text = $scope.model_config.text.replace('${수신자}', ' ');
                    $scope.model_config.text = $scope.model_config.text.replace('${발생시간}', moment($scope.alarm.alarmtime).format('YYYY-MM-DD HH:mm:ss'));
                    $scope.model_config.text = $scope.model_config.text.replace('${알람번호}', $scope.alarm.alarmno);
                    $scope.model_config.text = $scope.model_config.text.replace('${장비명}', $scope.alarm.node_nm);
                    $scope.model_config.text = $scope.model_config.text.replace('${장비번호}', $scope.alarm.node_num);
                    $scope.model_config.text = $scope.model_config.text.replace('${인터페이스}', $scope.alarm.alarmloc || '');
                    $scope.model_config.text = $scope.model_config.text.replace('${알람메시지}', $scope.alarm.alarmmsg);
                    $scope.model_config.text = $scope.model_config.text.replace('${원본메시지}', $scope.alarm.etc);
                    $scope.model_config.text = $scope.model_config.text.replace('${장애구역}', ($scope.alarm.node_nm || '') + (`(${$scope.alarm.alarmloc})` || ''));
                    $scope.model_config.text = $scope.model_config.text.replace('${장애구분}', $scope.syslogSopHistOne.fault_classify);
                    $scope.model_config.text = $scope.model_config.text.replace('${장애유형}', $scope.syslogSopHistOne.fault_type);
                    $scope.model_config.text = $scope.model_config.text.replace('${조치내용}', $scope.syslogSopHistOne.fault_detail_content);
                }, 100);
            // }else {
            //     setTimeout(() => {
            //     $scope.model_config.text = document.querySelector('#deafault-requestDoc-config').innerHTML;
            //     }, 100);
            // }
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

SyslogConfigurationDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', 'param'];
export default SyslogConfigurationDialogCtrl;
// export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', SyslogConfigurationDialogCtrl];

