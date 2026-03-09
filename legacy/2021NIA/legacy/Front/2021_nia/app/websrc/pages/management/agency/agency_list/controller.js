import BaseController from 'scripts/controller/baseController'

class AgencyListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.agency_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.agencyList = [];

        $scope.searchagencyList = {
            NrenName : '',
            NodeId : '',
            CustomerId : '',
        };

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchClear = function () {
            $scope.searchagencyList.NrenName = '';
            $scope.searchagencyList.NodeId = '';
            $scope.searchagencyList.CustomerId = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_LIST",
                NREN_NAME: $scope.searchagencyList.NrenName !== '' ? $scope.searchagencyList.NrenName : null,
                NODE_ID: $scope.searchagencyList.NodeId !== '' ? $scope.searchagencyList.NodeId : null,
                CUSTOMER_ID: $scope.searchagencyList.CustomerId !== '' ? $scope.searchagencyList.CustomerId : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false
                if (!result) return true;
                $scope.agencyList = result;
            })
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.gbps = (data) => {
            return Math.floor(data / 1000 / 1000 / 1000);
        };

        $scope.exportExcel = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['rnum', 'nren_name','node_id','node_int', 'bandwidth', 'customer_id', 'email'],
                        header: ['번호', '기관명','노드명','IF명','대역폭', '고객ID', '이메일']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_NIA_AGENCY_LIST",
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = '이용기관_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };

        $scope.agencyAdd = () => {
            $mdDialog.show({
                controller: tools.store.viewType.agency_add.controller,
                templateUrl: tools.store.viewType.agency_add.path,
                locals: {param: {}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        $scope.agencyDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.agency_detail.controller,
                templateUrl: tools.store.viewType.agency_detail.path,
                locals: {param: {data: item}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#agencyList');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".agency-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

AgencyListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default AgencyListCtrl;
