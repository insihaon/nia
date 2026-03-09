import BaseController from 'scripts/controller/baseController'

class SelfHealingDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect) {
        $scope.config = tools.store.viewType.self_healing;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);

        $scope.emailReceiver = [];
        $scope.model_healing = {
            text: '',
            ngModel: ''
        };

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetail: '',
            etcContent: ''
        };

        $scope.healingSopList = [];

        $scope.onInit = () => {
            $scope.getSopDataList();
            if($scope.ticket.ticket_type == "PF"){
                return ;
            }

            if ($scope.ticket.status == "INIT") {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_ALARM_EQUIPTYPE_COUNT_LIST",
                    CLUSTERNO: $scope.ticket.cluster_no
                }, function (result) {
                    $scope.damageScaleList = result.map(function (v) {
                        return v.equiptype + ": " + v.equip_count;
                    });

                    $scope.model_healing.text = document.querySelector('#template-requestDoc-healing').innerHTML;
                    $scope.model_healing.text = $scope.model_healing.text.replace('${AGENCY_NAME}', tools.store.agency_name);
                    $scope.model_healing.text = $scope.model_healing.text.replace('${발신자}', tools.store.userName);
                    $scope.model_healing.text = $scope.model_healing.text.replace('${수신자}', '');

                    $scope.model_healing.text = $scope.model_healing.text.replace('${티켓번호}', $scope.ticket.ticket_id);
                    $scope.model_healing.text = $scope.model_healing.text.replace('${티켓타입}', $scope.ticket.ticket_type);
                    $scope.model_healing.text = $scope.model_healing.text.replace('${발생원인}', $scope.ticket.ticket_rca_result_dtl_code);
                    // $scope.model_healing.text = $scope.model_healing.text.replace('${작업명}', ' AI 장애대응 자가회복');
                    // $scope.model_healing.text = $scope.model_healing.text.replace('${작업목적}', '자가회복');
                    // $scope.model_healing.text = $scope.model_healing.text.replace('${작업내용}', '자가회복');
                    // $scope.model_healing.text = $scope.model_healing.text.replace('${대상시설}', '[ ' + $scope.damageScaleList.join(' / ') + ' ]');

                    if($scope.failureInfo) {
                        $scope.model_healing.text = $scope.model_healing.text.replace('${작업요청구간}', ($scope.failureInfo.source_name || '') + ' → ' + ($scope.failureInfo.target_name || ''));
                        $scope.model_healing.text = $scope.model_healing.text.replace('${피해규모}', ($scope.failureInfo.source_name || '') + ($scope.failureInfo.source_port || '') + ' → ' + ($scope.failureInfo.target_name || '')  + ($scope.failureInfo.target_port || '') );;
                    }

                });
            } else {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_SEND_EMAIL_DATA",
                    TICKET_ID: $scope.ticket.ticket_id
                }, function (result) {
                    $scope.deafaultModel = document.querySelector('#deafault-requestDoc-healing').innerHTML;
                    $scope.model_healing.text = result.mail_content || $scope.deafaultModel;
                });
            }
        };

        $scope.$on('broadcastRecommendDetail', function(event, param) {
            document.querySelector('.recommendDetailHealing').innerHTML = "&nbsp;" + param.detail;
        }.bind(this));

        $scope.applyManager = () => {
            $scope.emailReceiver = [];
            var selectedList = [];
            $scope.managerList.map(function (v) {
                if (v.selected == true) {
                    selectedList.push(v.name);
                    $scope.emailReceiver.push(v.email);
                }
            });
            document.querySelector('.selectedManagerHealing').innerHTML = "&nbsp; &nbsp;" + selectedList.join(', ') || '';
        };

        $scope.onChangeSelectedSop = (sop) => {
            if(sop.selected) {
                $scope.selectedSop = sop;
                $scope.healingSopList.map((list) => {
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

            document.querySelector('.recommendDetailHealing').innerHTML = "&nbsp;" + ($scope.selectedSop ? $scope.selectedSop.detail : '');
            document.querySelector('.faultClassifyHealing').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_classify || '') : '');
            document.querySelector('.faultTypeHealing').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_type || '') : '');
            document.querySelector('.faultDetailHealing').innerHTML = "&nbsp;" + ($scope.selectedSop ? ($scope.selectedSop.fault_detail_content || '') : '');
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
                BODY: $('.selfHealing div.angular-meditor-content').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
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

        $scope.openBypassRouteListDialog = () => { // 우회 경로 가능 리스트 팝업
            $mdDialog.show({
                controller: tools.store.viewType.bypass_route_list.controller,
                templateUrl: tools.store.viewType.bypass_route_list.path,
                locals: { param: { ticket: $scope.ticket, test: 'test' } },
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

        $scope.showSopRecommend = () => {
            $mdDialog.show({
                controller: tools.store.viewType.sopList.controller,
                templateUrl: tools.store.viewType.sopList.path,
                locals: { param: { ticket: $scope.ticket, message: 'recommend' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            })
        };

        let isLoading = false;
        $scope.getSopDataList = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_PAGE2_LIST",
                TICKET_TYPE: $scope.ticket.ticket_type || 'RT',
                ROOT_CAUSE_SYSNAMEA: $scope.ticket.root_cause_sysnamea
            }, function (result) {
                isLoading = false;
                $scope.healingSopList  = result;
                $scope.relatedSopList = result.list.filter(item => item.fault_detail_content !== '');
            });
        };

        $scope.changeTicketStatus = (ticket, status) => {
            if(!status) {
                return;
            }
            ticket.status = status;
            $scope.safeApply();
        };

        $scope.showFinProcessDialog = () => {
            $mdDialog.show({
                controller: tools.store.viewType.fin_process_dialog.controller,
                templateUrl: tools.store.viewType.fin_process_dialog.path,
                locals: { param: { ticket: $scope.ticket, opinion: $scope.operatorOpinion} },
                parent: angular.element(document.querySelector('.md-dialog-container')),
                disableParentScroll: false,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            }).then(function (result) {
                $scope.changeTicketStatus(
                    tools.store.niaList.find(v => v.ticket_id == result.ticket_id),
                    result.status
                );
            }, function () {
            });
        };

        angular.element(document).ready(() => {
            $(window).resize(function () {
                $("table.selfHealing.sopList > *").width($("table.selfHealing.sopList").width() + $("table.selfHealing.sopList").scrollLeft());
            });
            $("table.selfHealing.sopList").on('scroll', function () {
                $("table.selfHealing.sopList > *").width($("table.selfHealing.sopList").width() + $("table.selfHealing.sopList").scrollLeft());
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

        // test 용도
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

    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', SelfHealingDialogCtrl];
