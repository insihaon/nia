import BaseController from 'scripts/controller/baseController'

class PortListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.port_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.portList = [];
        $scope.portDataChk= [];
        $scope.selectIfTpyeName = [];
        $scope.selectPortUseName = [
            { key: 'up', value: '사용', selected: true},
            { key: 'down', value: '미사용' },
        ];
        $scope.portAddData = {
            ifNm: '',
            ifId: '',
            ifType: '001',
            ipAddr: '',
            ipPrefix: '',
            ifSpeed: '',
            portUse: '',
        };
        $scope.disableChk = true;
        $scope.autoId = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.clearData = () => {
            $scope.portAddData.ifNm= '',
            $scope.portAddData.ifType= '001',
            $scope.portAddData.ipAddr= '',
            $scope.portAddData.ipPrefix= '',
            $scope.portAddData.ifSpeed= '',
            $scope.portAddData.portUse= 'up'
        };

        $scope.ifTypeData = (value) => {
            let data = $scope.selectIfTpyeName.filter(v => v.code_id == value);

            return data[0] ? data[0].code_nm : value;
        };

        $scope.gbps = (value) => {
            let data = value / 1000 / 1000 / 1000;

            return data
        };

        $scope.ifNmMatch = (data) => {
            return data.match(/\((.*?)\)/g,'')
        };

        $scope.ifNmReplace = (data) => {
            return data.replace(/\((.*?)\)/g, "");
        };

        $scope.ifUseType = (data) => {
            return $scope.selectPortUseName.find((item) => {return item.key == data.if_oper_status}).value
        };

        $scope.disableBtn = () => {
            if ($scope.disableChk == true) {
                $scope.disableChk = false;
                $scope.nodeIdAuto();
            } else {
                $scope.disableChk = true;
            }

        };

        $scope.nodeIdAuto = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NODE_KEY_LIST",
            }, function (result) {
                if (!result) return true;
                $scope.autoId = result;
            });
        };


        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_CODE_LIST",
            CATEGORY_CD: 'if_type'
        }, function (result) {
            if (!result) return true;
            $scope.selectIfTpyeName = result;
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_PORT_LIST",
            NODE_ID: param.data.node_id,
            OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
            LIMIT: $scope.historyPagingTable.limit
        }, function (result) {
            if (!result) return true;
            $scope.portDataChk = result;
        });

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_PORT_LIST",
                NODE_ID: param.data.node_id,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.portList = result;
            });
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.openPortAddDialog = (type, port = null) => {
            $mdDialog.show({
                controller: tools.store.viewType.port_detail.controller,
                templateUrl: tools.store.viewType.port_detail.path,
                locals: {param: {type: type, node: param.data, port: port}},
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


        $scope.portDelete = (data) => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_NIA_PORT_LIST",
                NODE_NUM: data.node_num,
                IF_NM: data.if_nm
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.handleSearch();
                });
            });
        }

        angular.element(document).ready(() => {
            $(".port-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }
}

PortListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default PortListCtrl;
