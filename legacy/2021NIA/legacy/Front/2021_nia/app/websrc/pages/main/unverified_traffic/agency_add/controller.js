import BaseController from 'scripts/controller/baseController'

class TrafficAgencyAddCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.traffic_agency_add;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null
        const { editType, nren_id, nren_ip, nren_name } = param.data
        const scope = param.item.$scope
        // debugger
        
        $scope.model = {
            editType: editType || 'C',  // or 'U'
            nren_id: nren_id || '',
            nren_ip: nren_ip || '',
            nren_name: nren_name || '',
            agencyList: [],
            disableEdit: false,
        }
        $scope.nren_ip = '';

        const validateNrenId = () => {
            const {nren_id} = $scope.model
            if(!nren_id) {
                tools.showAlert('프로토콜을 입력해주세요.');
                return false
            }
            return true
        }
        // IP 형식 검사
        const validateNrenIp = () => {
            const {nren_ip} = $scope.model
            if(nren_ip === ''){
                tools.showAlert('IP을 입력해주세요.');
                return false;
            }else{
                // if(!ipformat.test(nren_ip)) {
                //     tools.showAlert('IP주소를 형식에 맞게 입력해주세요.');
                //     return false
                // }
            }
            return true
        }

        const getAction = () => {
            const {editType} = $scope.model
            return editType === 'C' ? "INSERT_NIA_AGENCY_IP" : "UPDATE_NIA_AGENCY_IP"
        }

        $scope.loadAgency = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_CODE_LIST",
            }, function (result) {
                if('error' in result === false) {
                    const { model } = $scope
                    model.agencyList = result
                    setTimeout(() => {
                        model.disableEdit = (model.editType !== 'C')
                    });
                }
            });
        };

        $scope.handleSave = () => {
            if (validateNrenId() === false)  {
                return
            }
            if(validateNrenIp() === false){
                return
            }
            $scope.model.agencyList.forEach(value => {
                if(value.id === $scope.model.nren_id){
                    $scope.model.nren_name = value.name
                }
            });
            tools.createConfirmDlg("저장하시겠습니까?", null, null)
                .then(function () {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: getAction(),
                        NREN_ID: val($scope.model.nren_id),
                        UPDATE_NREN_IP: val($scope.model.nren_ip),
                        NREN_IP : val($scope.nren_ip),
                        NREN_NAME: val($scope.model.nren_name)
                    }, function (result) {
                        if(result.result) {
                            tools.showAlert('저장되었습니다.').then(function(){
                                // $mdDialog.hide();
                                $scope.dlgFunc.result({result: 'success'});
                                scope.handleSearch2();
                            })
                        } else {
                            tools.showAlert('실패했습니다.');
                        }
                    });
                });
        };

        $scope.handleDelete = () => {
            tools.createConfirmDlg("삭제하시겠습니까?", null, null)
                .then(function () {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: 'DELETE_NIA_AGENCY_IP',
                        NREN_ID: val(nren_id),
                        NREN_IP: val(nren_ip),
                        NREN_NAME: val(nren_name),
                    }, function (result) {
                        if(result.result) {
                            tools.showAlert('삭제되었습니다.').then(function() {
                                // $mdDialog.hide();
                                $scope.dlgFunc.result({result: 'success'});
                                scope.handleSearch2();
                            })
                        } else {
                            tools.showAlert('실패했습니다.');
                        }
                    });
                });
        };
        $scope.onInit = () => {
            $scope.loadAgency();
            if($scope.model.editType === 'U'){
                $scope.nren_ip = $scope.model.nren_ip
            }else{ return false; }
        }
        // angular.element(document).ready(() => {
        //     $scope.model.editType === 'C' ? $scope.model.nren_ip : $scope.nren_ip = $scope.model.nren_ip
        //     if($scope.model.editType === 'U'){
        //         $scope.nren_ip = $scope.model.nren_ip
        //     }else{ return false; }
        //     debugger
        // });

        // const ipformat = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    }
}
TrafficAgencyAddCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default TrafficAgencyAddCtrl;
