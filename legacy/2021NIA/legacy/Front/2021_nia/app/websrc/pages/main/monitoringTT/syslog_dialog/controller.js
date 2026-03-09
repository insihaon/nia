import BaseController from 'scripts/controller/baseController'

class SyslogDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.mon_tt_syslog_dialog;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.onInit = () => {
            $scope.getEquipmentList();
        }

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchSyslogData = {
            alarmno: '',
            startDateTime: '',
            endDateTime: '',
            equipment: '',
            nodeNum:'',
            alarmmsg:''
        }

        $scope.searchClear = () => {
            $scope.searchSyslogData.alarmno = '';
            $scope.searchSyslogData.startDateTime = '';
            $scope.searchSyslogData.endDateTime = '';
            $scope.searchSyslogData.equipment = '';
            $scope.searchSyslogData.nodeNum = '';
            $scope.searchSyslogData.alarmmsg = '';

            $scope.handleSearch();
            tools.showToast('초기화 되었습니다');
        };

        $scope.syslogLength = tools.store.niaSysDialogList.length || 0;

        // $scope.handleSearch = () => {
        //     tools.http({
        //         service: tools.constants.Service.rca,
        //         action: "SELECT_NIA_SYSLOG_PAGE2_LIST",
        //         START_DATE: $scope.searchSyslogData.startDateTime != '' ? $scope.searchSyslogData.startDateTime : null,
        //         END_DATE: $scope.searchSyslogData.endDateTime != '' ? $scope.searchSyslogData.endDateTime : null,
        //         EQUIPMENT: $scope.searchSyslogData.equipment != '' ? $scope.searchSyslogData.equipment : null,
        //         OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
        //         LIMIT: $scope.historyPagingTable.limit
        //     }, function (result) {
        //             if (!result) return true;
        //             $scope.niaSysDialogList = result;
        //         })
        //     };
        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_SYSLOG_PAGE2_LIST",
                ALARMNO: $scope.searchSyslogData.alarmno != '' ? $scope.searchSyslogData.alarmno : null,
                START_DATE: $scope.searchSyslogData.startDateTime != '' ? moment($scope.searchSyslogData.startDateTime).format('YYYY-MM-DD 00:00:00') : null,
                END_DATE: $scope.searchSyslogData.endDateTime != '' ? moment($scope.searchSyslogData.endDateTime).format('YYYY-MM-DD 23:59:59') : null,
                EQUIPMENT: $scope.searchSyslogData.equipment != '' ? $scope.searchSyslogData.equipment : null,
                NODENUM: $scope.searchSyslogData.nodeNum != '' ? $scope.searchSyslogData.nodeNum : null,
                ALARMMSG: $scope.searchSyslogData.alarmmsg != '' ? $scope.searchSyslogData.alarmmsg : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, (result) => {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false
                    if(!result || result.length <= 0) {
                        tools.store.niaSysDialogList = [];
                    } else {
                        tools.store.niaSysDialogList = result;
                        $scope.syslogLength = tools.store.niaSysDialogList.length || 0
                    }
            });
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        // $scope.$on('parentSearch', function(e){
        //     $scope.handleSearch()
        // });

        $scope.exportExcel = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['alarmno', 'alarmtime', 'node_num', 'node_nm', 'alarmmsg', 'alarmlvl', 'etc', 'rule_id'],
                        header: ['알람 번호','알람 시간','장비ID','장비명','알람 메세지','알람 레벨','원본 메세지', 'syslog rule id']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_NIA_SYSLOG_PAGE2_LIST",
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = 'SYSLOG_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };

        $scope.onChangeDev = (val) => {
            $scope.devFilter = val;
            $scope.calculateSyslogLength();
        };

        $scope.onChangeStatus = (val) => {
            $scope.statusFilter = val;
            $scope.calculateSyslogLength();
        };

        $scope.calculateSyslogLength = () => {
            $scope.syslogLength = tools.store.niaSysDialogList.filter((data) => true).length;
        };

        // todo: 이름변경 ticketAllSearch ->  searchKeyword
        $scope.$watch('tools.store.ticketAllSearch', function (n, o) {
            if(n == o) {
                return;
            } 
            $scope.calculateSyslogLength();
        });

        $scope.getEquipmentList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_EQUIPMENT_LIST",
            }, function (result) {
                $scope.equipmentOptions  = result;
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#syslogDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".profile-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}
SyslogDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default SyslogDialogCtrl;
