import BaseController from 'scripts/controller/baseController'

class LinkListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.link_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.linkList = [];

        $scope.searchLinkList = {
            srcNodeId : '',
            srcIfId : '',
            destNodeId : '',
            destIfId : '',
            linkDesc : ''
        };

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchClear = function () {
            $scope.searchLinkList.srcNodeId = '';
            $scope.searchLinkList.srcIfId = '';
            $scope.searchLinkList.destNodeId = '';
            $scope.searchLinkList.destIfId = '';
            $scope.searchLinkList.linkDesc = '';
            $scope.handleSearch();
        };


        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_LINK_LIST",
                SRC_NODE_ID: $scope.searchLinkList.srcNodeId != '' ? $scope.searchLinkList.srcNodeId : null,
                SRC_IF_ID: $scope.searchLinkList.srcIfId != '' ? $scope.searchLinkList.srcIfId : null,
                DEST_NODE_ID: $scope.searchLinkList.destNodeId != '' ? $scope.searchLinkList.destNodeId : null,
                DEST_IF_ID: $scope.searchLinkList.destIfId != '' ? $scope.searchLinkList.destIfId : null,
                LINK_DESC: $scope.searchLinkList.linkDesc != '' ? $scope.searchLinkList.linkDesc : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.linkList = result;
            })
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.exportExcel = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['rnum', 'src_node_id','src_if_id','dest_node_id', 'dest_if_id', 'bandwidth', 'link_desc', 'circuit_num','chng_datetime'],
                        header: ['번호', '시작점노드','시작점IF','끝점노드','끝점IF', '대역폭','링크용도','회선번호', '수정일']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_NIA_LINK_LIST",
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = '링크관리_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
        };

        $scope.linkAdd = () => {
            $mdDialog.show({
                controller: tools.store.viewType.link_add.controller,
                templateUrl: tools.store.viewType.link_add.path,
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
        }

        $scope.linkDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.link_detail.controller,
                templateUrl: tools.store.viewType.link_detail.path,
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

        $scope.closeDialog = () => {
            $scope.$rootBroadcast('broadcast.menuEvent', 'refresh');
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#linkList');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".link-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }
}

LinkListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default LinkListCtrl;
