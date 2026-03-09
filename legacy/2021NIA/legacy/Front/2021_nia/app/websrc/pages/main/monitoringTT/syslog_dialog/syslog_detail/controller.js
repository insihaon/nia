import BaseController from 'scripts/controller/baseController'

class SyslogDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.syslog_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.selectModelName = [];

        $scope.model = {
            node_num : param.data.node_num,
            node_nm : param.data.node_nm,
            alarmtime : param.data.alarmtime,
            alarmmsg : param.data.alarmmsg,
            status: param.data.status,
            etc : param.data.etc
        }

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetailContent: '',
            etcContent: ''
        };

        $scope.sopCodeList = {}
        $scope.loadSopCodeList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_CODE_PAGE2_LIST"
            }, function (result) {
                if(result && result.list.length > 0) {
                    const list = result.list || []
                    $scope.sopCodeList = list.reduce((acc, cur, index) => {
                        let g = acc[cur.fault_gb] 
                        if(!g) {
                            g = (acc[cur.fault_gb] = [])
                        }
                        g.push(cur)
                        return acc
                    }, {})
                } else {
                    $scope.sopCodeList = {};
                }
            })
        };

        $scope.openSOPListDialog = () => { 
            $mdDialog.show({
                controller: tools.store.viewType.SOP_edit_list.controller,
                templateUrl: tools.store.viewType.SOP_edit_list.path,
                locals: { param: { ticket: $scope.ticket, test: 'test' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.fin = () => {
            $scope.tools.createConfirmDlg(
                '해당 항목을 마감 하시겠습니까?',
                '확인을 선택하면 마감처리 됩니다.', null)
            .then(function() {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_NIA_SYSLOG_STATUS",
                    ALARMNO:param.data.alarmno,
                    FAULT_CLASSIFY: $scope.operatorOpinion.faultClassify,
                    FAULT_TYPE: $scope.operatorOpinion.faultType,
                    FAULT_DETAIL_CONTENT: $scope.operatorOpinion.faultDetailContent,
                    ETC_CONTENT: $scope.operatorOpinion.etcContent,
                    STATUS: 'FIN'
                }, function (result) {
                    $mdDialog.hide();
                })
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#syslogDetailDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".syslog-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
            $scope.loadSopCodeList()
            $scope.$on('onChangedSopCode', function(e){
                $scope.loadSopCodeList()
            });
        });

    }
}

SyslogDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default SyslogDetailCtrl;
