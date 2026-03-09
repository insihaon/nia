import BaseController from 'scripts/controller/baseController'

class RecoveryCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.recovery;
        super($injector, $scope, tools, $http, $timeout);

        $scope.profileKey = [];
        $scope.recoveryData = {
            profileTitle: '',
            profileDesc: '',
            networkType: '',
            processingTemplate: '',
            autoProcessCheck: false,
            autoProcessStartDatetime: '',
            autoProcessEndDatetime: '',
            ticketType: '',
            failureType: '',
            profileNodeName: '',
            autoRecovery: '',
            mailSend: true
        };
        $scope.ticketTypeList = [];
        $scope.failureTypeList = [];
        $scope.selectNodeNameList = [];
        $scope.selectRecoveryList = [];
        $scope.autoRecoveryList = [
            // {key: 'ACK', display_name: '인지'},
            {key: 'FIN', display_name: '마감'},
            // {key: 'AUTO_FIN', display_name: '자동마감'},
        ];

        $scope.managerList = [];
        $scope.emailReceiver = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.disablechk = false;
        $scope.disableProcessing = true;
        $scope.disableTicket = true;
        $scope.disableFailure = true;
        $scope.disableNode = true;

        $scope.changeChk = () => {
            if ($scope.disableProcessing === true && $scope.recoveryData.networkType !== '') {
                $scope.disableProcessing = false;
            } else if ($scope.disableTicket === true && $scope.recoveryData.processingTemplate !== '') {
                // $scope.ticketName();
                $scope.disableTicket = false;
            } else if ($scope.disableFailure === true && $scope.recoveryData.ticketType !== '') {
                // $scope.failName();
                $scope.disableFailure = false;
            } else if ($scope.disableNode === true && $scope.recoveryData.failureType !== '') {
                // $scope.nodeName();
                $scope.disableNode = false;
            } else if($scope.recoveryData.autoProcessCheck === true){
                  $scope.disablechk = true;
            } else {
                $scope.disablechk = false;
            }
        };

        $scope.loadUserList= () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_USER_LIST"
            }, function (result) {
                $scope.managerList = result.filter( v => v.agency_name === 'NOC');
                $scope.managerList.map( v => $scope.emailReceiver.push(v.email));
            });
        };

        $scope.sendEMail = () => {
            let email = $scope.emailReceiver;
            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_EMAIL',
                RECEIVER:  email.join(', '), /*test email: infobiz@koren.kr*/
                SUBJECT : moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss') + " 조치프로파일",
                BODY: $('.sendEmailData').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                profileDesc: $scope.recoveryData.profileDesc
            }, function (result) {
                if(result.send_result){
                    tools.createConfirmDlg('담당자에게 메일이 발송되었습니다.', null, null);
                } else { tools.createConfirmDlg('메일 발송이 실패하였습니다.', null, null); }
            });
        };

        $scope.$watch('recoveryData.networkType', function () {
            $scope.ticketName();
        });

        $scope.$watch('recoveryData.processingTemplate', function () {
            $scope.ticketName();
        });

        $scope.$watch('recoveryData.ticketType', function () {
            $scope.failName();
        });

        $scope.$watch('recoveryData.failureType', function () {
            $scope.nodeName();
        });

        $scope.radioNetwork = [
            { value: '전송', display_name: 'KOREN(전송)'},
            { value: 'IP', display_name: 'KOREN(IP)'}
        ];

        $scope.radioProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            // { value: 'selfProcessing', display_name: '자가구성'},
            // { value: 'gita', display_name: '기타'}
            { value: 'construction', display_name: '공사'}
        ];

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_PROFILE_KEY_LIST",
        }, function (result) {
            if (!result) return true;
            $scope.profileKey = result;
        });

        $scope.ticketName = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_PROFILE_TICKET_TYPE_LIST",
                TICKET_GB: $scope.recoveryData.networkType,
            }, function (result) {
                if (!result) return true;
                $scope.ticketTypeList = result;

            })
        };

        $scope.failName = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_PROFILE_FAIL_TYPE_LIST",
                TICKET_TYPE: $scope.recoveryData.ticketType
            }, function (result) {
                if (!result) return true;
                $scope.failureTypeList = result;
            })
        };

        $scope.nodeName = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_PROFILE_NODE_ID_LIST",
                TICKET_GB: $scope.recoveryData.networkType,
                FAILURETYPE: $scope.recoveryData.failureType,
            }, function (result) {
                if (!result) return true;
                $scope.selectNodeNameList = result;
            })
        };

        $scope.selectRecovery = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_PROFILE_RECOVERY_LIST",
                PROFILE_NUM: $scope.profileKey[0].profile_key,
            }, function (result) {
                if (!result) return true;
                $scope.selectRecoveryList = result;
            })
        };

        $scope.processingTemplateData = (value) => {
            let data = '';
            switch (value) {
                case  "recovery" :
                    data = '자가회복';
                    break;
                case  "construction" :
                    data = '공사';
                    break;
                default  :
                    break;
            }

            return data
        };

        $scope.selectNodeList = [];

        $scope.selectNodeAdd = () => {
            const data = $scope.recoveryData;
            if($scope.recoveryData.profileNodeName === '') {
                tools.showAlert('노드명을 선택해주세요.');
                return
            }
            $scope.selectRecoveryList.push( $scope.recoveryData.profileNodeName.root_cause_sysnamea );
        };

        $scope.selectNodeDelete = (data) => {
            $scope.selectRecoveryList.remove(data);
            tools.showAlert('삭제되었습니다.')
        };

        $scope.nodeAdd = () => {
            const data = $scope.selectRecoveryList;
            for(var i = 0; i < data.length; i++){
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "INSERT_PROFILE_NODE_NAME_LIST",
                    PROFILE_NUM: $scope.profileKey[0].profile_key,
                    NODE_ID: $scope.selectRecoveryList[i]
                }, function (result) {
                    if (!result) return true;
                })
            }
        };

        $scope.profileSave = () => {
            if($scope.recoveryData.profileTitle === ''){
                tools.showAlert('제목을 입력해주세요');
                return ;
            }
            if($scope.recoveryData.profileDesc === ''){
                tools.showAlert('설명을 입력해주세요');
                return ;
            }

            const data = $scope.recoveryData;

            $scope.nodeAdd();
            save();


            function save() {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "INSERT_PROFILE_LIST",
                    PROFILE_NUM: $scope.profileKey[0].profile_key,
                    PROFILE_TITLE: data.profileTitle || '',
                    PROFILE_DESC: data.profileDesc || '',
                    NETWORK_TYPE: data.networkType || '',
                    PROCESSING_TEMPLATE: data.processingTemplate || '',
                    AUTO_PROCESS_CHECK: data.autoProcessCheck || false,
                    AUTO_PROCESS_START_DATETIME: data.autoProcessStartDatetime || null,
                    AUTO_PROCESS_END_DATETIME: data.autoProcessEndDatetime || null,
                    TICKET_TYPE: data.ticketType || '',
                    PROCESS_TYPE: data.failureType || '',
                    AUTO_RECOVERY: data.autoRecovery || '',
                    EMAIL_CHECK: data.mailSend || true,
                }, function (result) {
                    if (!result) return false;
                    tools.showAlert('프로파일이저장되었습니다.').then(function () {
                        $scope.dlgFunc.result({result: 'success'});
                    })
                });
            }
        };

        angular.element(document).ready(() => {
            $(".profile-recovery-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}
RecoveryCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default RecoveryCtrl;
