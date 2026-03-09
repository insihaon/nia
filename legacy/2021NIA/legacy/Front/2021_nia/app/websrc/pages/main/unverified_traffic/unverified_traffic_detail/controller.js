import BaseController from 'scripts/controller/baseController'

class UnverifiedTrafficDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.recovery_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.recoveryData = {
            profileTitle: param.data.profile_title,
            profileDesc: param.data.profile_desc,
            networkType: param.data.network_type,
            processingTemplate: param.data.processing_template,
            autoProcessCheck: param.data.auto_process_check,
            autoProcessStartDatetime: param.data.auto_process_start_datetime,
            autoProcessEndDatetime: param.data.auto_process_end_datetime,
            ticketType: param.data.ticket_type,
            failureType: param.data.process_type,
            profileNodeName: '',
            autoRecovery: param.data.auto_recovery,
            mailSend: param.data.email_check,
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

        $scope.changeChk = () => {
            if($scope.recoveryData.autoProcessCheck === true){
                $scope.disablechk = true;
            } else {
                $scope.disablechk = false;
            }
        };

        $scope.radioNetwork = [
            { value: '전송', display_name: 'KOREN(전송)'},
            { value: 'IP', display_name: 'KOREN(IP)'}
        ];

        $scope.radioProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            { value: 'construction', display_name: '공사'}
        ];

        $scope.$watch('recoveryData.autoProcessCheck', function () {
            $scope.changeChk();
        });

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

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_USER_LIST"
        }, function (result) {
            $scope.managerList = result.filter( v => v.agency_name === 'NOC');
            $scope.managerList.map( v => $scope.emailReceiver.push(v.email));
        });

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

        $scope.sendEMail = () => {
            let email = $scope.emailReceiver;
            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_EMAIL',
                RECEIVER:  email.join(', '), /*test email: infobiz@koren.kr*/
                // RECEIVER:  'lke234@naver.com', /*test email: infobiz@koren.kr*/
                SUBJECT : moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss') + " 조치프로파일",
                // BODY: $('.selfConfiguration div.angular-meditor-content').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                BODY: $('.sendEmailData').html().replace(/\s{0,}[\r\n]/gi, '\r\n'),
                profileDesc: $scope.recoveryData.profileDesc
            }, function (result) {
                if(result.send_result){
                    tools.createConfirmDlg('담당자에게 메일이 발송되었습니다.', null, null);
                } else { tools.createConfirmDlg('메일 발송이 실패하였습니다.', null, null); }
            });
        };

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
                PROFILE_NUM: param.data.profile_num,
            }, function (result) {
                if (!result) return true;
                $scope.selectRecoveryList = result;
            })
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.selectRecovery();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.selectRecovery();
        });

        $scope.selectNodeList = [];

        $scope.selectNodeAdd = () => {
            const data = $scope.recoveryData;
            if(data.profileNodeName === '') {
                tools.showAlert('노드명을 선택해주세요.');
                return
            }
            $scope.selectRecoveryList.push({ node_id: $scope.recoveryData.profileNodeName.root_cause_sysnamea});
        };

        $scope.selectNodeDelete = (data) => {
            $scope.selectNodeList.remove(data);
            tools.showAlert('삭제되었습니다.')
        };

        $scope.nodeAdd = () => {
            const data = $scope.selectRecoveryList;

            for(var i = 0; i < data.length; i++){
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "INSERT_PROFILE_NODE_NAME_LIST",
                    PROFILE_NUM: param.data.profile_num,
                    NODE_ID: $scope.selectRecoveryList[i].node_id
                }, function (result) {
                    if (!result) return true;
                })
            }

        };

        $scope.nodeDelete = (value) => {
            const data = $scope.selectRecoveryList;
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_PROFILE_NODE_NAME_LIST",
                PROFILE_NUM: param.data.profile_num,
                NODE_ID: value.node_id

            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.selectRecovery();
                });
            })
        };

        $scope.parentSearch = null;

        $scope.profileSave = () => {

            if($scope.recoveryData.profileTitle === ''){
                tools.showAlert('제목을 입력해주세요');

                return
            }
            if($scope.recoveryData.profileDesc === ''){
                tools.showAlert('설명을 입력해주세요');
                return
            }

            const data = $scope.recoveryData;

            $scope.nodeAdd();
            save();


            function save() {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_PROFILE_LIST",
                    PROFILE_NUM: param.data.profile_num,
                    PROFILE_TITLE: data.profileTitle || '',
                    PROFILE_DESC: data.profileDesc || '',
                    TICKET_TYPE: data.ticketType || '',
                    PROCESS_TYPE: data.failureType || '',
                    NETWORK_TYPE: data.networkType || '',
                    PROCESSING_TEMPLATE: data.processingTemplate || '',
                    AUTO_PROCESS_CHECK: data.autoProcessCheck || false,
                    AUTO_PROCESS_START_DATETIME: data.autoProcessStartDatetime || null,
                    AUTO_PROCESS_END_DATETIME: data.autoProcessEndDatetime || null,
                    AUTO_RECOVERY: data.autoRecovery || '',
                    EMAIL_CHECK: data.mailSend || false,
                }, function (result) {
                    if (!result) return false;
                    tools.showAlert('수정되었습니다.').then(function () {
                        $scope.dlgFunc.result({result: 'success'});
                    })
                });
            }
        };

        $scope.profileDelete = () => {
            const data = $scope.recoveryData;
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_PROFILE_LIST",
                PROFILE_NUM: param.data.profile_num,
            }, function (result) {
                if (!result) return true;
                tools.showAlert('삭제되었습니다.').then(function () {
                    $scope.dlgFunc.result({result: 'success'});
                })
            })
        };

        angular.element(document).ready(() => {
            $(".profile-recovery-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}
UnverifiedTrafficDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element', 'param'];

export default UnverifiedTrafficDetailCtrl;
