import BaseController from 'scripts/controller/baseController'

class NodeDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.node_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.nodeDetail = [];
        $scope.selectModelName = [];
        $scope.selectNodeUseName = [
            { key: 'W', value: '사용' },
            { key: 'N', value: '미사용' },
        ];

        $scope.nodeDetailData = {
            nodeId : param.data.node_id,
            NodeNm : param.data.node_nm,
            modelNm : param.data.model_id,
            ipAddr : param.data.ip_addr,
            macAddr: param.data.mac_addr,
            netconfPort : param.data.netconf_port,
            netconfId : param.data.netconf_id,
            netconfPw : param.data.netconf_pw,
            latitude : param.data.latitude,
            longitude : param.data.longitude,
            nodeUse : param.data.admin_yn
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_CODE_LIST",
            CATEGORY_CD: 'node_model'
        }, function (result) {
            if (!result) return true;
            $scope.selectModelName = result;
        });

        $scope.nodeSave = () => {
            const data = $scope.nodeDetailData;
            console.log('data', data);
            tools.http({
                service: tools.constants.Service.rca,
                action: "UPDATE_NIA_NODE_LIST",
                NODE_NUM: param.data.node_num,
                NODE_ID: data.nodeId,
                NODE_NM: data.NodeNm,
                MODEL_ID: data.modelNm,
                LATITUDE: data.latitude,
                LONGITUDE: data.longitude,
                MAC_ADDR: data.macAddr,
                IP_ADDR: data.ipAddr,
                ADMIN_YN: data.nodeUse,
                NETCONF_PORT: data.netconfPort,
                NETCONF_ID: data.netconfId,
                NETCONF_PW: data.netconfPw,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('수정되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        $scope.nodeDelete = () => {
            const data = $scope.nodeDetailData;
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_NIA_NODE_LIST",
                NODE_NUM: param.data.node_num,
                NODE_ID: data.nodeId,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        angular.element(document).ready(() => {
            $(".node-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

NodeDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default NodeDetailCtrl;
