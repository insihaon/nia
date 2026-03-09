import BaseController from 'scripts/controller/baseController'

class PortDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.port_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.portList = [];
        $scope.portDataChk= [];
        $scope.selectIfTpyeName = [];
        $scope.selectPortUseName = [
            { key: 'up', value: '사용', selected: true},
            { key: 'down', value: '미사용' },
        ];
        $scope.portAddData = {
            if_nm: '',
            if_id: '',
            if_type: '001',
            ip_addr: '',
            ipPrefix: '',
            if_speed: '',
            if_oper_status: ''
        };
        $scope.disableChk = true;
        $scope.autoId = [];
        $scope.isUpdate = true;
        $scope.isEditable = tools.store.optionsNodeEditable == true;

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.onInit = () => {
            $scope.node = param.node || {};
            $scope.isUpdate = param.type == 'add' ? false : true
            if($scope.isUpdate) {
                Object.assign($scope.portAddData, param.port);
            }
        };

        $scope.ifNameMatch = (data) => {
            const result = data.match(/\((.*?)\)/g,'');
            return result ? result[0] : result;
        };

        $scope.ifNameReplace = (data) => {
            return data.replace(/\((.*?)\)/g, "");
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
            NODE_ID: param.node.node_id,
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
                NODE_ID: param.node.node_id,
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

        $scope.addData = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "INSERT_NIA_PORT_LIST",
                NODE_NUM: $scope.node.node_num,
                NODE_ID: $scope.node.node_id,
                IF_ID: $scope.portAddData.if_id,
                IF_NM: $scope.portAddData.if_nm,
                IF_INDEX: $scope.ifNameMatch($scope.portAddData.if_nm),
                IF_DESC: $scope.portAddData.if_nm,
                IF_TYPE: $scope.portAddData.if_type,
                IF_SPEED: $scope.portAddData.if_speed ? ($scope.portAddData.if_speed * 1000000000).toString() : null,
                IP_ADDR: $scope.portAddData.ip_addr,
                MAC_ADDR: $scope.node.mac_addr,
                IF_OPER_STATUS: $scope.portAddData.if_oper_status,
                IP_PREFIX: $scope.portAddData.ipPrefix
            }, function (result) {
                if (!result) return true;
                tools.showAlert('저장되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        };

        $scope.updateData = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "UPDATE_NIA_PORT_LIST",
                IF_OPER_STATUS: $scope.portAddData.if_oper_status,
                NODE_NUM: $scope.portAddData.node_num,
                NODE_ID: $scope.portAddData.node_id,
                IF_NM: $scope.portAddData.if_nm,
                IF_INDEX: $scope.ifNameMatch($scope.portAddData.if_nm),
                IF_TYPE: $scope.portAddData.if_type,
                IF_SPEED: $scope.portAddData.if_speed ? ($scope.portAddData.if_speed * 1000000000).toString() : null,
                IP_ADDR: $scope.portAddData.ip_addr
            }, function (result) {
                if (!result) return true;
                tools.showAlert('수정되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        };

        $scope.portSave = async () => {
            $scope.isValidation = await $scope.validationMessage();

            if (!$scope.isValidation) {
                return false;
            }

            if(!$scope.isUpdate) { // 등록
                $scope.addData();
            } else if($scope.isUpdate) { // 수정
                $scope.updateData();
            }
        };

        $scope.portDelete = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_NIA_PORT_LIST",
                NODE_NUM: $scope.node.node_num,
                IF_NM: $scope.portAddData.if_nm
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        $scope.validationMessage = () => {
            if(!$scope.portAddData.if_nm) {
                tools.showAlert('I/F 명을 입력해주세요.');
            } else if(!$scope.portAddData.if_type) {
                tools.showAlert('I/F 타입을 선택해주세요.');
                return false;
            } else if(!$scope.portAddData.if_speed) {
                tools.showAlert('대역폭을 입력해주세요.');
                return false;
            } else if(!$scope.portAddData.if_oper_status) {
                tools.showAlert('사용여부 선택해주세요.');
                return false;
            } else return true;
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#nodeDetail');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".port-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }
}

PortDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default PortDetailCtrl;
