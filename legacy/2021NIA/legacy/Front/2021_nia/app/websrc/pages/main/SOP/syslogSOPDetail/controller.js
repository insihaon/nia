import BaseController from 'scripts/controller/baseController'

class syslogSOPDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.syslog_SOP_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        
        $scope.ALARMetail = [];
        $scope.selectModelName = [];
        $scope.selectNodeUseName = [
            { key: 'W', value: '사용' },
            { key: 'N', value: '미사용' },
        ];

        $scope.operatorOpinion = {
            faultClassify: '',
            faultType: '',
            faultDetailContent: ''
        };

        $scope.SOPDetailData = {
            faultClassify : param.data.fault_classify, //장애구분
            faultType : param.data.fault_type, //장애유형
            faultDetailContent : param.data.fault_detail_content, //조치내용
            alarmno : param.data.alarmno,
            node_nm : param.data.node_nm,
            alarmloc : param.data.alarmloc,
            fault_detail_content : param.data.fault_detail_content,
            fault_classify : param.data.fault_classify,
            fault_type : param.data.fault_type,
            etc : param.data.etc,
            alarm_ouccur_time : param.data.time,
            handling_fin_time : param.data.handling_fin_time,
            handling_fin_user : param.data.handling_fin_user,
            status : param.data.status,
        };

        // tools.http({
        //     service: tools.constants.Service.rca,
        //     action: "SELECT_SOP_LIST",
        //     CATEGORY_CD: 'SOP_model'
        // }, function (result) {
        //     if (!result) return true;
        //     $scope.selectModelName = result;
        // });

        const emit = () => {
            $scope.$rootScope.$broadcast('onChangedSopCode', {});
        }

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

        $scope.ALARMSave = () => {
            const data = $scope.SOPDetailData;
            console.log('data', data);
            tools.createConfirmDlg(null, "수정하시겠습니까?", null).then(() => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_NIA_ALARM_LIST",
                    ALARMNO: data.alarmno,
                    FAULT_CLASSIFY: $scope.operatorOpinion.faultClassify != '' ? $scope.operatorOpinion.faultClassify : null,
                    FAULT_TYPE: $scope.operatorOpinion.faultType != '' ? $scope.operatorOpinion.faultType : null,
                    FAULT_DETAIL_CONTENT: $scope.operatorOpinion.faultDetailContent != '' ? $scope.operatorOpinion.faultDetailContent : null
                }, function (result) {
                    if (!result) return true;
                    tools.showAlert('수정되었습니다.').then(function () {
                        emit();
                        $scope.dlgFunc.result({result: 'success'});
                    });
                })
            });
        }

        $scope.ALARMDelete = () => {
            const data = $scope.SOPDetailData;
            tools.createConfirmDlg(null, "삭제하시겠습니까?", null).then(() => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_ALARM_LIST",
                ALARMNO: data.alarmno,
            }, function (result) {
                if (!result) return true;
                    tools.showAlert('삭제되었습니다.').then(function () {
                        emit();
                        $scope.dlgFunc.result({result: 'success'});
                    });
                })
            });
        }

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#syslogSopDetailDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".SOP-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });

            $scope.loadSopCodeList()
        });

    }
}

syslogSOPDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default syslogSOPDetailCtrl;
