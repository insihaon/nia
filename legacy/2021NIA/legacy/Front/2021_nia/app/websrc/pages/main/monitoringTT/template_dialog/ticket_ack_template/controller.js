import BaseProcessingController from 'scripts/controller/baseProcessingController'

class TicketAckDialogCtrl extends BaseProcessingController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, param) {
        $scope.config = tools.store.viewType.mon_tt_template_ticket_ack;
        super($injector, $scope, tools, $http, $timeout, param);

        $scope.rca_accuracy = '';
        $scope.handling_content = '';
        $scope.selectedData = {
            reason: [
                {
                    action: 'SELECT_FAULTREASON_LV1_LIST',
                    selected: '',
                    values: []
                },
                {
                    action: 'SELECT_FAULTREASON_LV2_LIST',
                    selected: '',
                    values: []
                },
                {
                    action: 'SELECT_FAULTREASON_LV3_LIST',
                    selected: '',
                    values: []
                }
            ],
            charge: [
                {
                    action: 'SELECT_FAULTCHARGE_LV1_LIST',
                    selected: '',
                    values: []
                },
                {
                    action: 'SELECT_FAULTCHARGE_LV2_LIST',
                    selected: '',
                    values: []
                }
            ]
        };

        let loadSavedData = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_TICKET_HANDLING_CURRENT_LIST",
                TICKET_ID: $scope.ticketInfo[$scope.selectedTicketIndex].ticket_id,
                TICKET_TYPE: tools.constants.RcaTicketType.TICKET.value,
                MAX_DAYS: 31
            }, function (result) {
                if (!result || !result[0]) return true;
                $scope.rca_accuracy = result[0].rca_accuracy || '';
                $scope.handling_content = result[0].handling_content || '';

                $scope.defaultReason = [
                    result[0].reason_level1 || '',
                    result[0].reason_level2 || '',
                    result[0].reason_level3 || ''
                ];
                $scope.defaultCharge = [
                    result[0].charge_level1 || '',
                    result[0].charge_level2 || ''
                ];
            });
        };

        $scope.submit = () => {
            let self = this;
            tools.createConfirmDlg(
                $scope.isModified ? tools.constants.Message.TICKET_ACK_MODIFIED
                    : tools.constants.Message.TICKET_ACK,
                tools.constants.Message.CONFIRM_WITH_SAVE, null)
                .then(function () {
                    /*$scope.ticketInfo.forEach(ticket => {
                        tools.http(createParams(ticket), function (result) {
                            if (result.result) {
                                self.deleteNewLabel(ticket.ticket_id);
                                $scope.successResult.push(true);
                            } else {
                                $scope.successResult.push(false);
                            }
                            if ($scope.successResult.length >= $scope.ticketInfo.length) {
                                showToastAndClose();
                            }
                        });
                    });*/
                    showToastAndClose();
                }, function () {
                });

            function createParams(ticket) {
                return {
                    service: tools.constants.Service.rca,
                    action: tools.constants.Action.CHANGE_TICKET_STATUS,
                    TICKET_ID: ticket.ticket_id,
                    STATUS: tools.constants.TicketStatus.ACK.code,
                    RCA_ACCURACY: $scope.rca_accuracy,
                    HANDLING_CONTENT: $scope.handling_content || '',
                    REASON_LEVEL1: $scope.selectedData.reason[0].selected,
                    REASON_LEVEL2: $scope.selectedData.reason[1].selected,
                    REASON_LEVEL3: $scope.selectedData.reason[2].selected,
                    CHARGE_LEVEL1: $scope.selectedData.charge[0].selected,
                    CHARGE_LEVEL2: $scope.selectedData.charge[1].selected,
                    HANDLING_USER: $scope.userInfo.handling_user,
                    HANDLING_DEPT: $scope.userInfo.handling_dept,
                    HANDLING_AGENCY: $scope.userInfo.handling_agency
                }
            }

            function showToastAndClose() {
                let count = !$scope.isBatch ? 1 : $scope.successResult.filter(res => res == true).length;
                let msg = !$scope.isModified ? tools.constants.Message.TICKET_ACK_COMPLETE
                    : tools.constants.Message.TICKET_ACK_MODIFIED_COMPLETE;

                $scope.tools.showToast(count + '건의 장애가 ' + msg);
                $scope.dlgFunc.result({
                    ticket_id: $scope.ticketInfo[0].ticket_id,
                    status: 'ACK'
                });
            }
        };

        $scope.onInit = () => {
            if (!$scope.isBatch && $scope.isModified) {
                // loadSavedData();
            }
        };

    }
}

TicketAckDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', 'param'];

export default TicketAckDialogCtrl;
