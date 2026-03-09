import BaseController from 'scripts/controller/baseController'

class AgencyAddCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.agency_add;
        super($injector, $scope, tools, $http, $timeout);

        $scope.agencyNrenId = [];
        $scope.agencyNodeId = [];
        $scope.agencyIfId = [];
        $scope.agencyIpList = [];
        $scope.agencyIpchk = '';

        $scope.agencyinfoData = {
            nrenId: '자동생성',
            nrenName: '',
            nodeId: '',
            ifId: '',
            customerId:'',
            bandwidth: '',
            email: '',
            ip: ''
        };

        $scope.dataclear = () => {
            $scope.agencyinfoData.ip = ''
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NREN_KEY_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.agencyNrenId = result;
        });

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
                NREN_ID: $scope.agencyNrenId[0].nren_id,
                NREN_NAME: $scope.agencyinfoData.nrenName,
            }, function (result) {
                isLoading = false
                if (!result) return true;
                data = result;
                $scope.agencyIpList = data.map(v => Object.assign({}, v, { selected: false }));
            });
        };

        $scope.selectIpList = [];

        $scope.selectIpAdd = () => {
            const data = $scope.agencyinfoData;
            if(data.ip === '') {
                tools.showAlert('IP를 작성해주세요.');
                return
            }
            $scope.selectIpList.push({ip: data.ip, selected: false});
            $scope.dataclear();
        };

        $scope.selectIpDelete = () => {
            $scope.selectIpList.remove($scope.selectIpList.filter(v => v.selected === true));
            tools.showAlert('삭제되었습니다.')
        };

        $scope.agencyIpAdd = () => {
            const data = $scope.selectIpList;
            for(var i = 0; i < data.length; i++){
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "INSERT_NIA_AGENCY_IP_LIST",
                    NREN_ID: $scope.agencyNrenId[0].nren_id,
                    NREN_NAME: $scope.agencyinfoData.nrenName,
                    NREN_IP: data[i].ip
                }, function (result) {
                    if (!result) return true;
                });
            }
        };

        $scope.agencyIpDelete = () => {
            let data = $scope.agencyIpList.filter(v => v.selected === true);
            if(data.length === 0) {
                tools.showAlert('삭제할 IP를 선택하세요.').then(function() {
                    return true
                });
            }
            for(var i =0; i < data.length; i++) {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "DELETE_NIA_AGENCY_IP_LIST",
                    NREN_ID: data[i].nren_id,
                    NREN_NAME: data[i].nren_name,
                    NREN_IP: data[i].nren_ip
                }, function (result) {
                    if (!result) return true;
                    tools.showAlert('삭제되었습니다.').then(function() {
                        $scope.handleSearch();
                    });
                });
            }
        };

        $scope.agencySave = () => {
            if($scope.agencyinfoData.nrenName === ''){
                alert('기관명을 입력해주세요')
                return
            } else if($scope.agencyinfoData.nodeId === '') {
                alert('노드명을 선택해주세요')
                return
            } else if($scope.agencyinfoData.ifId === '') {
                alert('I/F명을 선택해주세요')
                return
            } else if($scope.agencyinfoData.bandwidth === '') {
                alert('대역폭을 입력해주세요')
                return
            } else if(isNaN($scope.agencyinfoData.bandwidth)) {
                alert('대역폭에 숫자만 입력해주세요')
                return
            } else if($scope.agencyinfoData.customerId === '') {
                alert('고객ID를 입력해주세요')
                return
            } else if($scope.agencyinfoData.email === '') {
                alert('이메일을 입력해주세요')
                return
            }
            
            $scope.agencyIpAdd();

            tools.http({
                service: tools.constants.Service.rca,
                action: "INSERT_NIA_AGENCY_LIST",
                NREN_ID: $scope.agencyNrenId[0].nren_id,
                NREN_NAME: $scope.agencyinfoData.nrenName,
                NODE_ID: $scope.agencyinfoData.nodeId,
                IF_ID: $scope.agencyinfoData.ifId,
                BANDWIDTH: ($scope.agencyinfoData.bandwidth * 1000 * 1000 * 1000).toString(),
                CUSTOMER_ID: $scope.agencyinfoData.customerId,
                EMAIL: $scope.agencyinfoData.email
            }, function (result) {
                if (!result) return true;
                tools.showAlert('저장되었습니다.').then(function() {
                    $scope.dlgFunc.result({result: 'success'});
                });
            });

        }

        angular.element(document).ready(() => {
            $(".agency-add-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

AgencyAddCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default AgencyAddCtrl;
