import BaseController from 'scripts/controller/baseController'

class AiHistoryDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.ai_history;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);

        $scope.rcaSearchTerm = {
            fromDateTime : '',
            toDateTime : ''
        };

        $scope.rcaTicketSearch = {
            field: ''
        };
        $scope.rcaSearchTextArea = '';

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $timeout( ()=> {
            $scope.getTicketProcessHistory();
        });

        $scope.exportExcelHistory = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['ticket_id', 'root_cause_sysnamea', 'root_cause_sysnamea', 'ip_addra', 'root_cause_porta', 'ticket_type', 'ticket_type', 'ticket_result', 'detail', '기타사항', 'request_time', 'receive_time'],
                        header: ['티켓 번호', '장비ID', '장비이름', '마스터IP', '장비 I/F', '티켓 유형', '장애구분', '장애유형', '조치내용', '기타사항', '요청시간', '처리시간']
                        // field: ['ticket_id','status','ticket_result', 'status', 'request_time', 'receive_time', 'detail'],
                        // header: ['티켓 번호','티켓 타입','발생원인','상태', '요청 시간','처리 시간','상세내용']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_SOP_PAGE2_LIST",
                TICKET_ID: $scope.ticket.ticket_id,
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = '처리이력_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };

        $scope.getTicketProcessHistory = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_PAGE2_LIST",
                TICKET_ID: $scope.ticket.ticket_id || '',
                START_DATE: $scope.rcaSearchTerm.fromDateTime ? moment($scope.rcaSearchTerm.fromDateTime).format('YYYY-MM-DD') : null,
                END_DATE: $scope.rcaSearchTerm.toDateTime ? moment($scope.rcaSearchTerm.toDateTime).format('YYYY-MM-DD') : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {
                $scope.ticketHistory  = result;
            });
        };

        $scope.searchClear = function () {
            $scope.rcaSearchTerm.fromDateTime = $scope.rcaSearchTerm.toDateTime = '';
            $scope.rcaSearchTextArea = '';
            $scope.rcaSearchTerm.fromDateTime = '';

            $scope.getTicketProcessHistory();
            tools.showToast('초기화 되었습니다');
        };

        angular.element(document).ready(() => {
            setTimeout(() => {
                $scope.$watch('historyPagingTable.selected', function () {
                    $scope.getTicketProcessHistory();
                });
        
                $scope.$watch('historyPagingTable.limit', function () {
                    $scope.getTicketProcessHistory();
                });
            }, 3000);
            
            $(window).resize(function () {
                $("table.ticket-alarm.aiHistory > *").width($("table.ticket-alarm.aiHistory").width() + $("table.ticket-alarm.aiHistory").scrollLeft());
            });
            $("table.ticket-alarm.aiHistory").on('scroll', function () {
                $("table.ticket-alarm.aiHistory > *").width($("table.ticket-alarm.aiHistory").width() + $("table.ticket-alarm.aiHistory").scrollLeft());
            });
        });

    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', AiHistoryDialogCtrl];
