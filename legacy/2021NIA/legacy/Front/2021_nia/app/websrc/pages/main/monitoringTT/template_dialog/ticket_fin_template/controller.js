import BaseProcessingController from 'scripts/controller/baseProcessingController'

class TicketFinDialogCtrl extends BaseProcessingController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, param) {
        $scope.config = tools.store.viewType.mon_tt_template_ticket_fin;
        super($injector, $scope, tools, $http, $timeout, param);

        $scope.handling_time = tools.store.formatDateTime(new Date().getTime());
        $scope.rca_accuracy = '';
        $scope.handling_content = '';
        $scope.handling_fin_content = '';
        $scope.action_content = '';
        $scope.action_fin_category = '';
        $scope.action_fin_content = '';
        $scope.action_handling_content = '';

        // $scope.appendActionContent= () => {
        //     if($scope.action_fin_category != '4'&& $scope.action_fin_content) {
        //         $scope.action_handling_content='';
        //         $scope.action_content = `${$scope.action_fin_category} > ${$scope.action_fin_content}`;
        //     } else {
        //         $scope.action_content = '조치 내용을 선택해주세요';
        //     }
        // };

        $scope.submit = () => {

            tools.createConfirmDlg(
                $scope.isModified ? tools.constants.Message.TICKET_FIN_MODIFIED
                    : tools.constants.Message.TICKET_FIN,
                tools.constants.Message.CONFIRM_WITH_SAVE, null)
            .then(function() {
                $scope.ticketInfo.forEach(ticket => {
                    tools.http(createParams(ticket), function (result) {
                        $scope.successResult.push(!!(result.result));
                        if ($scope.successResult.length >= $scope.ticketInfo.length) {
                            showToastAndClose();
                        }
                    });
                });
                showToastAndClose();
            }, function() {
            });

            function createParams(ticket) {
                return {
                    service: tools.constants.Service.rca,
                    action: tools.constants.Action.CHANGE_TICKET_STATUS,
                    eventType: 'REQUEST_CHANGE_TICKET_STATUS',
                    ticket_id: ticket.ticket_id,
                    status: tools.constants.TicketStatus.FIN.code,
                    ticket_type: ticket.ticket_type,
                    sop_id : ticket.sop_id,
                    detail: ticket.handling_fin_content,
                    rca_accuracy: $scope.rca_accuracy
                }
            }

            function showToastAndClose() {
                let count = !$scope.isBatch ? 1 : $scope.successResult.filter(res => res == true).length;
                let msg = !$scope.isModified ? tools.constants.Message.TICKET_FIN_COMPLETE
                    : tools.constants.Message.TICKET_FIN_MODIFIED_COMPLETE;

                $scope.tools.showToast(count + '건의 장애가 ' + msg);
                $scope.dlgFunc.result({
                    ticket_id: $scope.ticketInfo[0].ticket_id,
                    status: 'FIN'
                });
            }
        };

    }
}
TicketFinDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog','param'];

export default TicketFinDialogCtrl;
