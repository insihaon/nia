import BaseController from 'scripts/controller/baseController'

class SOPEditCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.SOP_edit;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null
        const { editType, seq, user_id, fault_gb, fault_type, reg_time } = param.data

        $scope.fault_gb_list = [ '장애구분', '장애유형', '조치내용', ]

        $scope.model = {
            editType: editType || 'C',  // or 'U'
            condition: {
                seq: seq || '',
                reg_time: reg_time || '',
                user_id: user_id || tools.store.userId,
                user_nm: tools.store.userName,
                fault_gb: fault_gb || '',
                fault_type: fault_type || '',
            },
        }

        const emit = () => {
            $scope.$rootScope.$broadcast('onChangedSopCode', {});
        }

        const validate = () => {
            const {fault_gb, fault_type} = $scope.model.condition
            if(!fault_gb) {
                tools.showAlert('SOP구분을 입력해주세요.');
                return false
            }
            if(!fault_type) {
                tools.showAlert('항목을 입력해주세요.');
                return false
            }
            return true
        }

        const getAction = () => {
            const {editType} = $scope.model
            return editType === 'C' ? "INSERT_SOP_CODE" : "UPDATE_SOP_CODE"
        }

        $scope.handleSave = () => {
            if (validate() === false) {
                return
            }
            tools.createConfirmDlg("저장하시겠습니까?", null, null)
                .then(function () {
                    const {user_id, fault_gb, fault_type, seq} = $scope.model.condition
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: getAction(),
                        USER_ID: val(user_id),
                        FAULT_GB: val(fault_gb), 
                        FAULT_TYPE: val(fault_type),
                        SEQ: val(seq),
                    }, function (result) {
                        if (result.result) {
                            tools.showAlert('저장되었습니다.').then(() => {
                                $mdDialog.hide();
                                emit();
                            })
                        } else {
                            tools.showAlert('실패했습니다.');
                        }
                    });
                });
        };

        $scope.handleDelete = () => {
            const {seq} = $scope.model.condition
            tools.createConfirmDlg("삭제하시겠습니까?", null, null)
                .then(function () {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: 'DELETE_SOP_CODE',
                        SEQ: val(seq),
                    }, function (result) {
                        if (result.result) {
                            tools.showAlert('삭제되었습니다.').then(() => {
                                $mdDialog.hide();
                                emit();
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
SOPEditCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default SOPEditCtrl;
