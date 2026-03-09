import BaseController from 'scripts/controller/baseController'

class NodeAddCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.node_add;
        super($injector, $scope, tools, $http, $timeout);

        $scope.selectModelName = [];
        $scope.selectNodeUseName = [
            { key: 'W', value: '사용' },
            { key: 'N', value: '미사용' },
        ];

        $scope.nodeAddData = {
            nodeId : '',
            NodeNm : '',
            modelNm : '',
            ipAddr : '',
            macAddr: '',
            netconfPort : '',
            netconfId : '',
            netconfPw : '',
            latitude : '',
            longitude : '',
            nodeUse : ''
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_CODE_LIST",
            CATEGORY_CD: "node_model",
        }, function (result) {
            if (!result) return true;
            $scope.selectModelName = result;
        });

        $scope.nodeSave = () => {
            const data = $scope.nodeAddData
            console.log('data', data)
            if(data.NodeNm === ''){
                alert('노드명을 입력해주세요')
                return 
            }
            if(data.modelNm === ''){
                alert('모델명을 입력해주세요')
                return 
            }
            if(data.ipAddr === ''){
                alert('대표IP를 입력해주세요')
                return
            }
            if(data.macAddr === ''){
                alert('MAC주소를 입력해주세요')
                return 
            }

            tools.http({
                service: tools.constants.Service.rca,
                action: "INSERT_NIA_NODE_LIST",
                NODE_ID: data.NodeNm,
                NODE_NM: data.NodeNm,
                MODEL_NAME: data.modelNm,
                LATITUDE: data.latitude,
                LONGITUDE: data.longitude,
                MAC_ADDR: data.macAddr,
                IP_ADDR: data.ipAddr,
                ADMIN_YN: data.nodeUse,
                NETCONF_PORT: data.netconfPort,
                NETCONF_ID: data.netconfId,
                NETCONF_PW: data.netconfPw
            }, function (result) {
                if (!result) return true;
                tools.showAlert('저장되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        }

        angular.element(document).ready(() => {
            $(".node-add-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }

}

NodeAddCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default NodeAddCtrl;
