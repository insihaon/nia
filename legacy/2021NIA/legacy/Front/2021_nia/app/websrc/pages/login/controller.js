import Encrypt from "../../scripts/class/Encrypt";

class LoginCtrl {
    constructor($scope, $http, $timeout, focus, tools, storeService) {
        $scope.config = tools.store.viewType.login_page;

        tools.init_store();

        $scope.showLogin = true;
        $scope.form = { };
        $scope.findId = { };
        $scope.findPw = { };

        $scope.sign = {
            id: '',
            username: '',
            usernumber:'',
            email: ''
        };

        var getFocusId = function () {
            if ($scope.form.username == null)
                return 'focusUsername';
            else if($scope.form.pw == null)
                return 'focusPassword';
        };

        var showAlertLoginFail = function (response) {

            let msg = '아이디 또는 비밀번호를 다시 확인하세요.';
            let error = response.data;
            let start = error.indexOf('comment: ');
            let end = error.indexOf(', data');
            if(error &&  start >= 0 && end >= 0){
                msg = error.substring(start + 'comment: '.length, end - 1 );

                if(error.indexOf('data 0004') >= 0){
                    msg += 'kate에서 비밀번호를 변경하세요';
                }
            }

            tools.showAlert('로그인', msg)
                .then(function () {
                    $scope.form.pw = null;
                    focus(getFocusId());
                });
        };

        $scope.clickChangeBtn = ( isShowLogin, changePage ) => {
            $scope.showLogin = isShowLogin;
            $scope.changePage = changePage;
        };

        $scope.updateUser = () => {

            if($scope.sign.pw != $scope.sign.pwReconfirm ){
                tools.showAlert('비밀번호가 일치하지 않습니다.');
                return ;
            } else if( !$scope.sign.id || !$scope.sign.pw || !$scope.sign.pwReconfirm ) {
                tools.showAlert('아이디 또는 비밀번호가 입력되지 않았습니다..');
                return ;
            } else {
                tools.cipher(angular.toJson($scope.sign), (data) => {
                    var fd = new FormData();
                    fd.append('data', data);

                    tools.createConfirmDlg(
                        "계정을 등록하시겠습니까?", null, null).then(function () {
                        $http.post("data", {
                            action: "INSERT_USER",
                            DATA: data,
                        }).then(function (result) {
                            if(result.data.message == true){
                                $scope.sign = {};
                                $scope.form = {};
                                tools.showAlert('등록되었습니다.');
                                $scope.showLogin = true;
                            } else { tools.showAlert("동일한 아이디가 존재합니다."); }
                        });
                    });
                })
            }
        };

        $scope.findUser = (data, deployValue) => {
            
            switch (deployValue) {
                case 'id' :
                    if(!data.userName || !data.userNumber){
                        tools.showAlert('사용자 정보를 입력해주세요.');
                        return ;
                    }

                    $http.post("data", {
                        action: "SELECT_FIND_USER_LIST",
                        NAME: data.userName,
                        USER_NUMBER: data.userNumber
                    }).then( function (result) {
                        tools.showAlert(data.userName + '님의 아이디는 ' + result.data.LIST[0].id+ ' 입니다. ');
                        $scope.findId={ };
                    });

                    break;
                case 'pw' :
                    if(!data.userId || !data.userName || !data.userNumber){
                        tools.showAlert('사용자 정보를 입력해주세요.');
                        return ;
                    }

                    $http.post("data", {
                        action: "SELECT_FIND_USER_LIST",
                        LOGIN_ID: data.userId,
                        NAME: data.userName,
                        USER_NUMBER: data.userNumber
                    }).then(function (result) {
                        result = result.data.LIST[0];
                        if (result.id) {
                            $scope.findPw = {};
                            tools.createPromptDlg('비밀번호 재설정', null, null)
                                .then(function (pw) {
                                    result.pw = pw;
                                    tools.cipher(angular.toJson(result), (data) => {
                                        var fd = new FormData();
                                        fd.append('data', data);
                                        $http.post("data", {
                                                action: "UPDATE_USER_DATA",
                                                DATA: data,
                                        });
                                    })
                                }).then(function () {
                                tools.showAlert("비밀번호 재설정이 완료되었습니다.");
                            });
                        } else { tools.showAlert('일치하는 정보가 없습니다.'); }
                    });
                    break;
                default: break;
            }
        };

        // $scope.$on('BroadcastKeyDown', function (event, param) {
        //     switch (param) {
        //         case 13:
        //             // let button = document.getElementById('button_submit');
        //             // if(button) button.click();
        //             break;
        //     }
        // }.bind(this));

        {
            {
                var localData = tools.localStorage.auth;
                if (localData != null && localData != '') {
                    let encrypt = new Encrypt();
                    let array = localData.split('|');
                    if (array.length > 1) {
                        let key = array[0];
                        let data = array[1];
                        $scope.form = JSON.parse(encrypt.decrypt(data, key));

                        if ($scope.form.save) {
                            focus('focusSubmit');
                        }
                    }
                }
            }
        }

        storeService.loadData(() => {});

        $scope.doLogin = function (invalid, then) {

            then = then || function (success) {}

            if (!$scope.form.save) {
                tools.localStorage.auth = '';
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

                then(false);
                return;
            }

            tools.cipher(angular.toJson($scope.form), (data) => {
                var fd = new FormData();
                fd.append('data', data);
                $http.post("loginhandler", fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                    .then(function (response) {
                        tools.$info(response.data);
                        if ('Login successful' == response.data.message) {
                            if ($scope.form.save) {
                                let encrypt = new Encrypt(null, JSON.stringify($scope.form));
                                tools.localStorage.auth = encrypt.key + '|' + encrypt.data;
                            }

                            then(true);
                            storeService.store.auth = true;
                            storeService.store.user = response.data;

                            if (location.href.indexOf("login.html") != -1) {
                                tools.logout();
                            } else {
                                $scope.statego(storeService.store.start_page);
                            }
                        } else {
                            then(false);
                            showAlertLoginFail(response);
                        }
                    }, function (response) {
                        then(false);
                        showAlertLoginFail(response);
                    });
            });
        };

        $scope.login = (e, invalid) => {
            let validCheck = getFocusId();
            if (validCheck != null){
                focus(validCheck);
                return;
            }

            $scope.doLogin(invalid, (result) => {

            });
        };

        function checkBrowser() {
            if( navigator.userAgent.toLowerCase().indexOf("chrome") != -1 ||
                $('.md-dialog-content').length > 0) { return; }

            tools.createConfirmDlg(
                '브라우저가 크롬이 아닙니다.',
                '최적화된 환경에서 실행할 수 있도록 크롬을 다운로드 하시겠습니까?', null)
                .then(function(){
                    downloadChrome();
                }, function(){
                });
        }

        function downloadChrome(){
            var path = 'http://' + window.location.host + '/chrome/';
            //window.location.assign(path)
            var a = document.createElement('A');
            var fileName = 'ChromeStandaloneSetup';

            if((navigator.userAgent.indexOf("WOW64") != -1 ||
                navigator.userAgent.indexOf("Win64") != -1 )){
                fileName = fileName + '64' + '.exe';
            } else {
                fileName = fileName+ '.exe';
            }
            a.href = path + fileName;
            a.download =  fileName;

            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        };
        $scope.downloadChromeClick = function(){
            downloadChrome();
        };

        $timeout(()=>{
            checkBrowser();
        },1000);
    }
}

export default ['$scope', '$http', '$timeout', 'focus', 'tools', 'storeService', LoginCtrl];
