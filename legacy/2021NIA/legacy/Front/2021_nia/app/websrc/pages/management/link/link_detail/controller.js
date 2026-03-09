import BaseController from 'scripts/controller/baseController'

class LinkDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.link_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.selectSrcNode = [];
        $scope.selectSrcInt = [];
        $scope.selectDestNode = [];
        $scope.selectDestInt = [];

        $scope.linkDataTF = [
            {key: 'True', value: 'True'},
            {key: 'False', value: 'False'}
        ];

        $scope.linkDetailData = {
            srcNodeId : param.data.src_node_id,
            srcIfId : param.data.src_if_id,
            destNodeId : param.data.dest_node_id,
            destIfId : param.data.dest_if_id,
            srcIpAddr : param.data.src_ip_addr,
            destIpAddr : param.data.dest_ip_addr,
            bandwidth: param.data.bandwidth,
            linkDesc : param.data.link_desc,
            VLAN : param.data.VLAN,
            TAG : param.data.TAG,
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_LINK_START_NODE_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.selectSrcNode = result;
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_LINK_IF_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.selectSrcInt = result;
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_LINK_END_NODE_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.selectDestNode = result;
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_LINK_IF_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.selectDestInt = result;
        });

        $scope.linkDelete = () => {
            const data = $scope.linkDetailData;

            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_NIA_LINK_LIST",
                SRC_NODE_ID: data.srcNodeId,
                SRC_IF_ID: data.srcIfId,
                DEST_NODE_ID: data.destNodeId,
                DEST_IF_ID: data.destIfId,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        };

        $scope.linkSave = () => {
            const data = $scope.linkDetailData;

            tools.http({
                service: tools.constants.Service.rca,
                action: "UPDATE_NIA_LINK_LIST",
                SRC_NODE_ID: data.srcNodeId,
                SRC_IF_ID: data.srcIfId,
                DEST_NODE_ID: data.destNodeId,
                DEST_IF_ID: data.destIfId,
                LINK_DESC: data.linkDesc,
                BANDWIDTH: data.bandwidth ? (data.bandwidth).toString() : null,
                VLAN: data.VLAN,
                TAG: data.TAG,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('수정되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#linkDetail');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".link-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

LinkDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default LinkDetailCtrl;
