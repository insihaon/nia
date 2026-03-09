import BaseController from 'scripts/controller/baseController'

class UserSettingCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, param) {
        $scope.config = tools.store.viewType.user_setting;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.isReadOnly = true;
        $scope.userProfile = {
            id : tools.store.userId,
            username : tools.store.userName,
            usernumber : tools.store.phone_number,
            email : tools.store.user.email,
            agency_name : tools.store.agency_name
        };

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.changeUserInfo = () => {
            if ($scope.userProfile.pw != $scope.userProfile.pwReConfirm) {
                tools.showAlert('비밀번호가 일치하지 않습니다.');
                return;
            } else if (!$scope.userProfile.pw || !$scope.userProfile.pwReConfirm || !$scope.userProfile.username
                || !$scope.userProfile.usernumber || !$scope.userProfile.email || !$scope.userProfile.agency_name) {
                tools.showAlert('사용자 정보를 모두 입력해주십시오.');
                return;
            } else {
                tools.cipher(angular.toJson($scope.userProfile), (data) => {
                    var fd = new FormData();
                    fd.append('data', data);

                    tools.createConfirmDlg(
                        "수정하시겠습니까?", null, null)
                        .then(function () {
                            $http.post("data", {
                                action: "UPDATE_USER_DATA",
                                DATA: data,
                            }).then(function (result) {
                                tools.showAlert('수정되었습니다.');
                                $scope.dlgFunc.cancel();
                                tools.http({
                                    service: tools.constants.Service.rca,
                                    action: "SELECT_USER_DATA",
                                    LOGIN_ID: tools.store.userId
                                }, function (user) {
                                    tools.store.user = user;
                                })
                            });
                        });
                })
            }
        };

        $scope.deleteUserInfo = () => {
            tools.createConfirmDlg(
                "삭제하시겠습니까?", null, null)
                .then(function () {
                        tools.http({
                            service: tools.constants.Service.rca,
                            action: "DELETE_USER",
                            LOGIN_ID: tools.store.userId
                        }, function () {
                            tools.createConfirmDlg('계정이 삭제되었습니다.').then(function () {
                                tools.logout();
                            });
                        });
                    }
                )
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#userSetting');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

    }
}
UserSettingCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog','$element','param'];

export default UserSettingCtrl;
