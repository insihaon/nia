import BaseController from 'scripts/controller/baseController'

class SOPListDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect, param) {
        $scope.config = tools.store.viewType.SOP;
        super($injector, $scope, tools, $http, $timeout);

        $scope.SOPList = [];
        $scope.alarmList = [];

        $scope.loadSopData = {
            startDateTime : '',
            endDateTime : '',
            ticketId : ''
        };

        $scope.rcaSearchTextArea = '';

        $scope.sopPagingTable = {
            selected: 1,
            limit: 30
        };

        $scope.alarmPagingTable = {
            selected: 1,
            limit: 30
        };

        $scope.searchalarmList = {
            equipment: '',
            ifName:'',
            status : '',
            startDateTime : '',
            endDateTime : ''
        };

        $scope.selectStatus = [
            {key: '', value: '전체', selected: true},
            {key: '자동', value: '자동'},
            {key: '수동', value: '수동'}
        ];

        $scope.onInit = () => {

            

            $scope.getEquipmentList();
            $scope.getIFList();

            $scope.ticket = param.ticket;
            $scope.isRecommend = param.message == 'recommend' ? true : false;

            $scope.$on('onChangedSopCode', function(e) {
                $scope.getSopDataList();
                $scope.getAlarmDataList();
            });

            $scope.$watch('sopPagingTable.selected', function () {
                $scope.getSopDataList();
            });

            $scope.$watch('sopPagingTable.limit', function () {
                $scope.getSopDataList();
            });

            $scope.$watch('alarmPagingTable.selected', function () {
                $scope.getAlarmDataList();
            });

            $scope.$watch('alarmPagingTable.limit', function () {
                $scope.getAlarmDataList();
            });
        };

        $scope.getEquipmentList = () => {
            tools.http({
                service: tools.constants.Service.rca,

                action: "SELECT_NIA_EQUIPMENT_LIST",
            }, function (result) {

                $scope.equipmentOptions = result;
            });
        };

        $scope.getIFList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_INTERFACE_LIST",
            }, function (result) {

                $scope.ifOptions = result;
            });
        };

        $scope.openSOPDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.SOP_detail.controller,
                templateUrl: tools.store.viewType.SOP_detail.path,
                locals: {param: {data: item}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        $scope.openALARMDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.syslog_SOP_detail.controller,
                templateUrl: tools.store.viewType.syslog_SOP_detail.path,
                locals: {param: {data: item}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        let isLoading = false

        $scope.getSopDataList = () => {
            if(isLoading) return
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_PAGE2_LIST",
                TICKET_ID:  $scope.loadSopData.ticketId ? $scope.loadSopData.ticketId : null,
                TICKET_TYPE: $scope.ticket ? $scope.ticket.ticket_type : null,             
                START_DATE:   $scope.loadSopData.startDateTime ? moment($scope.loadSopData.startDateTime).format('YYYY-MM-DD 00:00:00') : null,
                END_DATE:  $scope.loadSopData.endDateTime ? moment($scope.loadSopData.endDateTime).format('YYYY-MM-DD 23:59:59') : null,
                OFFSET: ($scope.sopPagingTable.selected-1)*$scope.sopPagingTable.limit,
                LIMIT: $scope.sopPagingTable.limit
            }, function (result) {

                var sopListscroll = document.getElementById('sopListscroll');
                sopListscroll.scrollTop = 0;

                isLoading = false
                if (!result) return true;
                $scope.SOPList  = result;
            });
        };

        $scope.getAlarmDataList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SYSLOG_ALARM_PAGE2_LIST",
                EQUIPMENT: $scope.searchalarmList.equipment ? $scope.searchalarmList.equipment : null,
                IFNAME: $scope.searchalarmList.ifName ? $scope.searchalarmList.ifName : null,
                STATUS: $scope.searchalarmList.status ? $scope.searchalarmList.status : null,
                START_DATE: $scope.searchalarmList.startDateTime ? moment($scope.searchalarmList.startDateTime).format('YYYY-MM-DD 00:00:00') : null,
                END_DATE:  $scope.searchalarmList.endDateTime ? moment($scope.searchalarmList.endDateTime).format('YYYY-MM-DD 23:59:59') : null,
                OFFSET: ($scope.alarmPagingTable.selected-1)*$scope.alarmPagingTable.limit,
                LIMIT: $scope.alarmPagingTable.limit
            }, function (result) {

                var alarmscroll = document.getElementById('alarmscroll');
                alarmscroll.scrollTop = 0;
                $scope.alarmList = result;
            });         
        };

        $scope.searchClear = function () {
            $scope.loadSopData.ticketId = '';
            $scope.rcaSearchTextArea = '';
            $scope.loadSopData.startDateTime = '';
            $scope.loadSopData.endDateTime = '';
            $scope.searchalarmList.equipment = '';
            $scope.searchalarmList.ifName = '';
            $scope.searchalarmList.status = '';
            $scope.searchalarmList.startDateTime = '';
            $scope.searchalarmList.endDateTime = '';

            $scope.getSopDataList();
            $scope.getAlarmDataList();
            
            tools.showToast('초기화 되었습니다');
        };

        $scope.getTicketTypeLabel = (ticket) => {
            if(ticket.root_cause_type) {
                return tools.constants.TicketType[ticket.root_cause_type].text + '장애';
            } else {
                return '장애';
            }
        };

        $scope.exportExcelSop = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['ticket_id', 'fault_classify', 'fault_type', 'fault_detail_content', 'etc_content', 'root_cause_sysnamea', 'root_cause_sysnamea', 'ip_addra', 'root_cause_porta', 'ticket_type', 'request_time', 'receive_time'],
                        header: ['티켓 번호', '장애구분', '장애유형', '조치내용', '기타사항', '장비ID', '장비이름', '마스터IP', '장비 I/F', '티켓 유형', '요청시간', '처리시간']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_SOP_PAGE2_LIST",
                TICKET_TYPE : $scope.ticket ? $scope.ticket.ticket_type : null,
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = 'SOP 리스트_' + moment( new Date()).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#SOPArea');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        $scope.getRecommendDetail = (sop) => {
            tools.$rootScope.$broadcast('broadcastRecommendDetail', sop);
            $mdDialog.hide();
        };

        angular.element(document).ready(() => {
            $(window).resize(function () {
                $("table.SOPList > *").width($("table.SOPList").width() + $("table.SOPList").scrollLeft());
                $("table.alarmList > *").width($("table.alarmList").width() + $("table.alarmList").scrollLeft());
            });
            $("table.SOPList").on('scroll', function () {
                $("table.SOPList > *").width($("table.SOPList").width() + $("table.SOPList").scrollLeft());
            });

            $("table.alarmList").on('scroll', function () {
                $("table.alarmList > *").width($("table.alarmList").width() + $("table.alarmList").scrollLeft());
            });

            // selectbox를 누르고 취소하고 싶을 때 다른 곳을 클릭하면 selectbox가 없어지도록 만듬
            $(".SOP-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    
    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect', 'param', SOPListDialogCtrl];
