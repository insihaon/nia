import BaseController from 'scripts/controller/baseController'

class LinkAddCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.link_add;
        super($injector, $scope, tools, $http, $timeout);

        $scope.disableChk = true;
        $scope.disableEndChk = true;
        $scope.selectSrcNode = [];
        $scope.selectSrcInt = [];
        $scope.selectDestNode = [];
        $scope.selectDestInt = [];

        $scope.linkDataTF = [
            {key: 'True', value: 'True'},
            {key: 'False', value: 'False'}
        ];

        $scope.linkAddData = {
            srcNodeId : '',
            srcIfId : '',
            destNodeId : '',
            destIfId : '',
            srcIpAddr : '',
            destIpAddr: '',
            bandWidth: '',
            linkDesc : '',
            VLAN : '',
            TAG : '',
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_LINK_START_NODE_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.selectSrcNode = result;

        });

        $scope.startNodeClick = (data) => {
            $scope.disableChk = false;
            $scope.linkStartIf(data);
            $scope.linkEndNode(data);
        };

        $scope.endNodeClick = (data) => {
            $scope.disableEndChk = false;
            $scope.linkEndIf(data);
        };

        $scope.linkStartIf = (value) => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_LINK_IF_LIST",
                NODE_ID: value,
            }, function (result) {
                if (!result) return true;
                $scope.selectSrcInt = result;
                $scope.linkAddData.srcIfId = $scope.selectSrcInt[0].int_name;
            });
        };

        $scope.startIfClick = (data) => {
            $scope.linkAddData.srcIpAddr = data.ip_addr ? data.ip_addr : '-';
            $scope.linkAddData.bandWidth = data.if_speed ? parseInt(data.if_speed) : 0;
        };

        $scope.linkEndNode = (value) => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_LINK_END_NODE_LIST",
                NODE_ID: value,
            }, function (result) {
                if (!result) return true;
                $scope.selectDestNode = result;
            });
        };

        $scope.linkEndIf = (value) => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_LINK_IF_LIST",
                NODE_ID: value,
            }, function (result) {
                if (!result) return true;
                $scope.selectDestInt = result;

                // $scope.linkAddData.destIpAddr = $scope.selectDestInt[0].ip_addr ? null: '-';
            });
        };

        $scope.endIfClick = (data) => {
            $scope.linkAddData.destIpAddr = data.ip_addr ? data.ip_addr: '-';
        };


        $scope.linkSave = () => {
            const data = $scope.linkAddData;

            if(data.srcNodeId === ''){
                tools.showAlert('시작점노드를 선택해주세요');
                return
            }else if(data.srcIfId === '') {
                tools.showAlert('시작점I/F를 선택해주세요');
                return
            }else if(data.destNodeId === '') {
                tools.showAlert('끝점노드를 선택해주세요');
                return
            }else if(data.destIfId === '') {
                tools.showAlert('끝점I/F를 선택해주세요');
                return
            }else if(data.srcIpAddr === '') {
                tools.showAlert('시작점IP를 입력해주세요');
                return
            }else if(data.destIpAddr === '') {
                tools.showAlert('끝점IP를 입력해주세요');
                return
            }else if(data.bandWidth === '') {
                tools.showAlert('대역폭을 입력해주세요');
                return
            }else if(data.linkDesc === '') {
                tools.showAlert('설명을 입력해주세요');
                return
            }else if(data.VLAN === '') {
                tools.showAlert('VLAN을 선택해주세요');
                return
            }else if(data.TAG === '') {
                tools.showAlert('TAG을 선택해주세요');
                return
            }

            tools.http({
                service: tools.constants.Service.rca,
                action: "INSERT_NIA_LINK_LIST",
                SRC_NODE_ID: data.srcNodeId,
                SRC_IF_ID: data.srcIfId,
                DEST_NODE_ID: data.destNodeId,
                DEST_IF_ID: data.destIfId,
                SRC_IP_ADDR: data.srcIpAddr,
                DEST_IP_ADDR: data.destIpAddr,
                BANDWIDTH: data.bandwidth ? (data.bandwidth).toString() : null,
                LINK_DESC: data.linkDesc,
                VLAN: data.VLAN,
                TAG: data.TAG,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('저장되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        angular.element(document).ready(() => {
            $(".link-add-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }
}

LinkAddCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default LinkAddCtrl;
