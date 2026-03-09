import BaseController from 'scripts/controller/baseController'

class SyslogRuleDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.syslog_rule_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.nodeDetail = [];
        $scope.selectModelName = [];

        $scope.model = {
            editType: param.data.editType || 'C',  // or 'U'
            condition: {
            syslog_rule_id : param.data.syslog_rule_id || '',
            syslog_rule_nm : param.data.syslog_rule_nm || '',
            occur_str1 : param.data.occur_str1 || '',
            occur_str2 : param.data.occur_str2 || '',
            occur_str3 : param.data.occur_str3 || '',
            occur_except_str1 : param.data.occur_except_str1 || '',
            occur_except_str2 : param.data.occur_except_str2 || '',
            occur_except_str3 : param.data.occur_except_str3 || '',
            use_yn : param.data.use_yn|| '사용'
            },
        };

        const getAction = () => {
            const {editType} = $scope.model
            return editType === 'C' ? "INSERT_NIA_SYSLOG_RULE" : "UPDATE_NIA_SYSLOG_RULE"
        }
        
        $scope.$watch('model.condition.syslog_rule_nm', function (newVal, oldVal) {
            $scope.isDuplicate = newVal !== param.data.syslog_rule_nm ? true : false
        });
        
        var initialVal = angular.copy($scope.model.condition);

        $scope.$watch('model.condition', function (newVal, oldVal) {
            if (angular.equals(newVal, initialVal)) {
                $scope.isModified = false;
            } else {
                $scope.isModified = true;
            }
        }, true);
        
        $scope.isValidationRulName = false

        $scope.createValidate = () => {
            const {syslog_rule_nm, occur_str1, occur_str2, occur_str3, occur_except_str1, occur_except_str2, occur_except_str3} = $scope.model.condition
            if(!syslog_rule_nm) {
                tools.showAlert('규칙명을 입력해주세요.');
                return;
            }
            if(!occur_str1 && !occur_str2 && !occur_str3 && !occur_except_str1 && !occur_except_str2 && !occur_except_str3) {
                tools.showAlert('키워드를 입력해주세요.');
                return;
            }
            if(!$scope.isValidationRulName){
                tools.showAlert('RULE NAME 중복확인을 해주세요.');
                return;
            }
            return true
        }

        $scope.updateValidate = () => {
            const {occur_str1, occur_str2, occur_str3, occur_except_str1, occur_except_str2, occur_except_str3} = $scope.model.condition
            if(!occur_str1 && !occur_str2 && !occur_str3 && !occur_except_str1 && !occur_except_str2 && !occur_except_str3) {
                tools.showAlert('키워드를 입력해주세요.');
                return;
            }
            if($scope.isDuplicate && !$scope.isValidationRulName){
                tools.showAlert('RULE NAME 중복확인을 해주세요.');
                return;
            }
            return true
        }
        
        $scope.checkRuleName = () => {
            const {syslog_rule_nm} = $scope.model.condition
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_CHECK_RULE_NAME",
                SYSLOG_RULE_NM: syslog_rule_nm
            }, function (result) {
              if (result.count === 1){
                tools.showAlert('중복된 규칙 이름이 존재합니다.').then(function () {
                    $scope.isValidationRulName = false
                });
              }else{
                tools.showAlert('사용가능합니다.').then(function () {
                    $scope.isValidationRulName = true
                });
              }
            });
        }

        $scope.syslogRuleSave = () => {
            if($scope.model.editType === 'C' && !$scope.createValidate()){
                return;
            }else if($scope.model.editType === 'U' && !$scope.updateValidate()){
                return;
            }
            tools.createConfirmDlg("저장하시겠습니까?", null, null)
                .then(function () {
            const {syslog_rule_id, syslog_rule_nm, occur_str1, occur_str2, occur_str3, occur_except_str1, occur_except_str2, occur_except_str3, use_yn} = $scope.model.condition
            tools.http({
                service: tools.constants.Service.rca,
                action: getAction(),
                SYSLOG_RULE_ID: syslog_rule_id,
                SYSLOG_RULE_NM: syslog_rule_nm,
                OCCUR_STR1: occur_str1,
                OCCUR_STR2: occur_str2,
                OCCUR_STR3: occur_str3,
                OCCUR_EXCEPT_STR1: occur_except_str1,
                OCCUR_EXCEPT_STR2: occur_except_str2,
                OCCUR_EXCEPT_STR3: occur_except_str3,
                USE_YN: use_yn
            }, function (result) {
                if (result){
                    tools.showAlert('저장되었습니다.').then(function () {
                        $scope.dlgFunc.result({result: true});
                    });
                };
            });
        });
        }

        $scope.syslogRuleDelete = () => {
            const {syslog_rule_id, syslog_rule_nm} = $scope.model.condition
            const syslogRuleName = param.data.syslog_rule_id === syslog_rule_id ? syslog_rule_nm : ''
            tools.createConfirmDlg(`${syslogRuleName}(${syslog_rule_id}번) 규칙을 삭제 하시겠습니까?`, null, null)
            .then(function (){
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "DELETE_NIA_SYSLOG_RULE",
                    SYSLOG_RULE_ID: syslog_rule_id
                }, function (result) {
                    if (!result) return true;
                    tools.showAlert('삭제되었습니다.').then(function () {
                        $scope.dlgFunc.result({result: 'success'});
                    });
                });
            });
        }

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#syslogRuleDetailDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".node-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

SyslogRuleDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default SyslogRuleDetailCtrl;
