import BaseController from 'scripts/controller/baseController'

class ApplicationAddCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.application_add;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null
        const { editType, protocol, port_num, description, mapp_serial } = param.data
        const scope = param.item.$scope

        $scope.model = {
            editType: editType || 'C',  // or 'U'
            protocol: protocol || '',
            port_num: port_num || '',
            description: description || '',
            mapp_serial: mapp_serial || '',
        }

        const validate = () => {
            const {protocol, port_num} = $scope.model
            if(!protocol) {
                tools.showAlert('프로토콜을 입력해주세요.');
                return false
            }
            if(!port_num) {
                tools.showAlert('포트를 입력해주세요.');
                return false
            }
            if(!Number.isInteger(Number(port_num)) ) {
                tools.showAlert('포트는 정수 형태로 입력가능 합니다.');
                return false
            }
            return true
        }

        const getAction = () => {
            const {editType} = $scope.model
            return editType === 'C' ? "INSERT_NIA_APP_IP" : "UPDATE_NIA_APP_IP"
        }

        $scope.handleSave = () => {
            if (validate() === false) {
                return
            }
            tools.createConfirmDlg("저장하시겠습니까?", null, null)
                .then(function () {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: getAction(),
                        PROTOCOL: val($scope.model.protocol),
                        PORT_NUM: val($scope.model.port_num),
                        DESCRIPTION: val($scope.model.description),
                        MAPP_SERIAL: val($scope.model.mapp_serial),
                    }, function (result) {
                        if (result.result) {
                            tools.showAlert('저장되었습니다.').then(function(){
                                // $mdDialog.hide();
                                $scope.dlgFunc.result({result: 'success'});
                                scope.handleSearch3();
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
                        action: 'DELETE_NIA_APP_IP',
                        MAPP_SERIAL: val(mapp_serial),
                    }, function (result) {
                        if (result.result) {
                            tools.showAlert('삭제되었습니다.').then(function() {
                                // $mdDialog.hide();
                                $scope.dlgFunc.result({result: 'success'});
                                scope.handleSearch3();
                            })
                        } else {
                            tools.showAlert('실패했습니다.');
                        }
                    });
                });
        };

        angular.element(document).ready(() => {
        });

    }
}
ApplicationAddCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default ApplicationAddCtrl;
