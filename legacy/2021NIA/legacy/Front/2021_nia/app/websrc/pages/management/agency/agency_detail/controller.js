import BaseController from 'scripts/controller/baseController'

class AgencyDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.agency_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.agencyNodeName = [];
        $scope.agencyNodeInt = [];
        $scope.agencyNodeId = [];
        $scope.agencyIfId = [];
        $scope.agencyIpList = [];
        $scope.agencyIpchkList = [];

        $scope.agencyinfoData = {
            nrenId: param.data.nren_id,
            nrenName: param.data.nren_name,
            nodeId: param.data.node_id,
            nodeInt: param.data.node_int,
            customerId: param.data.customer_id,
            bandwidth: param.data.bandwidth,
            email: param.data.email,
            ip: '',
        };

        $scope.onInit = () => {
            $scope.onLoadInterfaceList();
        };

        $scope.dataclear = () => {
            $scope.agencyinfoData.ip = ''
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_AGENCY_NODE_ID_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.agencyNodeId = result;
        });

        $scope.onLoadInterfaceList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_AGENCY_IF_ID_LIST",
                NODE_NAME: $scope.agencyinfoData.nodeId
            }, function (result) {
                if (!result) return true;
                $scope.agencyIfId = result;
            });
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            let data = [];
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_IP_LIST",
                NREN_ID: $scope.agencyinfoData.nrenId,
                NREN_NAME: $scope.agencyinfoData.nrenName,
            }, function (result) {
                isLoading = false
                if (!result) return true;
                data = result;
                $scope.agencyIpList = data.map(v => Object.assign({}, v, { selected: false }));
            });
        };

        $scope.$watch('agencyinfoData.nrenId', function () {
            $scope.handleSearch();
        });

        $scope.agencyIpAdd = () => {
            
            if($scope.agencyinfoData.ip === ''){
                tools.showAlert('IP를 작성해주세요.');
                return
            }

            tools.http({
                service: tools.constants.Service.rca,
                action: "INSERT_NIA_AGENCY_IP_LIST",
                NREN_ID: $scope.agencyinfoData.nrenId,
                NREN_NAME: $scope.agencyinfoData.nrenName,
                NREN_IP: $scope.agencyinfoData.ip
            }, function (result) {
                if (!result) return true;
                tools.showAlert('저장되었습니다.').then(function() {
                    // $scope.dlgFunc.result({result: 'success'});
                    $scope.dataclear();
                    $scope.handleSearch();
                });
            });
        };

        $scope.agencyIpDelete = (item) => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "DELETE_NIA_AGENCY_IP_LIST",
                    NREN_ID: item.nren_id,
                    NREN_NAME: item.nren_name,
                    NREN_IP: item.nren_ip
                }, function (result) {
                    if (!result) return true;
                    tools.showAlert('삭제되었습니다.').then(function() {
                        // $scope.dlgFunc.result({result: 'success'});
                        $scope.handleSearch();
                    });
                });
        };

        $scope.gbps = (data) => {
            return Math.floor(data / 1000 / 1000 / 1000);
        };

        $scope.agencySave = () => {
            const data = $scope.agencyinfoData;

            tools.http({
                service: tools.constants.Service.rca,
                action: "UPDATE_AGENCY_NODE_INT_LIST",
                NREN_ID: data.nrenId,
                NREN_NAME: data.nrenName,
                NODE_ID: data.nodeId,
                NODE_INT: data.nodeInt,
                BANDWIDTH: data.bandwidth * 1000 * 1000 * 1000,
                CUSTOMER_ID: data.customerId,
                EMAIL: data.email,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('수정되었습니다.').then(function() {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        };

        $scope.agencyDelete = () => {
            const data = $scope.agencyinfoData;

            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_AGENCY_NODE_INT_LIST",
                NREN_ID: data.nrenId,
                NREN_NAME: data.nrenName,
                NODE_ID: data.nodeId,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function() {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#agencyDetail');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".agency-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

AgencyDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default AgencyDetailCtrl;
