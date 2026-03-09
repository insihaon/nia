import Encrypt from '../class/Encrypt'

function loginCtrl($scope, $http, $timeout, focus, tools, storeService) {
    tools.$debug('loginCtrl');

    storeService.loadData(() => {
        $scope.form = {GROUP_ID: "orviche"};
        $scope.groups = storeService.store.groups;

        var localData = tools.localStorage.orviche;
        if (localData != null && localData != '') {
            let encrypt = new Encrypt();
            let array = localData.split('|');
            if (array.length > 1) {
                let key = array[0];
                let data = array[1];
                $scope.form = JSON.parse(encrypt.decrypt(data, key));
            }
        }

        var showAlert = function (response) {
            tools.showAlert('로그인', '아이디 또는 비밀번호를 다시 확인하세요.')
                .then(function () {
                    $scope.form.password = null;
                    focus(getFocusId());
                });
        };

        var getFocusId = function () {
            if ($scope.form.GROUP_ID == null)
                return 'focusGroup';
            else if ($scope.form.username == null)
                return 'focusUsername';
            else
                return 'focusPassword';
        };

        $scope.login = function (invalid) {
            //console.log("login click");
            ///console.log($scope.form);

            if (!$scope.form.save) {
                tools.localStorage.orviche = '';
            }

            if (invalid) {
                $timeout(function () {
                    document.getElementById("formSubmit").click();

                    if (document.getElementsByClassName('form-invalid').length > 0) {
                        document.getElementsByClassName('form-invalid')[0].scrollIntoView(true);
                    }
                }, 0);

//	            var toast = $mdToast.simple()
//	                .textContent('입력되지 않은 항목이 있습니다.')
//	                .position('bottom right');
                //
//	            $mdToast.show(toast);
                return;
            }

            tools.cipher(angular.toJson($scope.form), (data) => {
                var fd = new FormData();
                fd.append('data', data);
                //fd.append('username', $scope.form.username);
                //fd.append('password', $scope.form.password);
                //fd.append('groupid', $scope.form.GROUP_ID);
                $http.post("loginhandler", fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                    .then(function (response) {
                        tools.$info(response.data);
                        if ('Login successful' == response.data) {
                            if ($scope.form.save) {
                                let encrypt = new Encrypt(null, JSON.stringify($scope.form));
                                tools.localStorage.orviche = encrypt.key + '|' + encrypt.data;
                            }
                            if (location.href.indexOf("login.html") != -1) {
                                location.replace(storeService.store.start_url);
                            } else {
                                $scope.statego(storeService.store.start_page);
                            }
                        } else {
                            showAlert(response);
                        }
                    }, function (response) {
                        showAlert(response);
                    });
            });
        }
    });

    //window.$scope = $scope;
}

export default ['$scope', '$http', '$timeout', 'focus', 'tools', 'storeService', loginCtrl]
