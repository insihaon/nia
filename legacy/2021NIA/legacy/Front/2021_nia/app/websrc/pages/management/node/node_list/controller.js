import BaseController from 'scripts/controller/baseController'

class NodeListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.node_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.nodeList = [];
        $scope.searchNodeList = {
            nodeNm : '',
            modelId : '',
            ipAddr : '',
            nodeUse : '',
            startDateTime : '',
            endDateTime : ''
        };
        $scope.modelCodeList = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.selectNodeUseName = [
            {key: '', value: '전체', selected: true},
            {key: 'W', value: '사용'},
            {key: 'N', value: '미사용'}
        ];

        $scope.searchClear = function () {
            $scope.searchNodeList.nodeNm = '';
            $scope.searchNodeList.modelId = '';
            $scope.searchNodeList.ipAddr = '';
            $scope.searchNodeList.startDateTime = '';
            $scope.searchNodeList.endDateTime = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_NODE_LIST",
                NODE_NM: $scope.searchNodeList.nodeNm != '' ? $scope.searchNodeList.nodeNm : null,
                MODEL_ID: $scope.searchNodeList.modelId != '' ? $scope.searchNodeList.modelId : null,
                IP_ADDR: $scope.searchNodeList.ipAddr != '' ? $scope.searchNodeList.ipAddr : null,
                ADMIN_YN: $scope.searchNodeList.nodeUse != '' ? $scope.searchNodeList.nodeUse : null,
                START_DATE: $scope.searchNodeList.startDateTime != '' ? moment($scope.searchNodeList.startDateTime).format('YYYY-MM-DD 00:00:00') : null,
                END_DATE: $scope.searchNodeList.endDateTime != '' ? moment($scope.searchNodeList.endDateTime).format('YYYY-MM-DD 23:59:59') : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false
                if (!result) return true;
                $scope.nodeList = result;
            })
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_CODE_LIST",
            CATEGORY_CD: 'node_model',
        }, function (result) {
            if (!result) return true;
            $scope.modelCodeList = result;
        });

        $scope.changeName = (value) => {
            let use = '';
            switch (value) {
                case 'W' : use = '사용';
                    break;
                case 'N':
                case 'S' : use = '미사용';
                    break;
                default:
                    break;
            }
            return use
        };

        $scope.exportExcel = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['rnum', 'node_id', 'node_nm', 'netconf_type', 'model_id', 'latitude', 'longitude', 'mac_addr', 'ip_addr', 'admin_yn', 'chng_datetime'],
                        header: ['번호','장비 ID','장비 이름','장비 타입','모델 이름', '위도', '경도', '맥주소','마스터 IP','사용여부', '수정일']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_NIA_NODE_LIST",
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = '노드관리_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };


        $scope.nodeUpdate = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.node_detail.controller,
                templateUrl: tools.store.viewType.node_detail.path,
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

        $scope.portUpdate = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.port_list.controller,
                templateUrl: tools.store.viewType.port_list.path,
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
            }, function () { });
        };

        $scope.nodeAdd = () => {
            $mdDialog.show({
                controller: tools.store.viewType.node_add.controller,
                templateUrl: tools.store.viewType.node_add.path,
                locals: {param: {}},
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

        $scope.closeDialog = () => {
            $scope.$rootBroadcast('broadcast.menuEvent', 'refresh');
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#nodeList');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".node-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }

}

NodeListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default NodeListCtrl;
