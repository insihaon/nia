import BaseController from 'scripts/controller/baseController'

class SOPDetailCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.SOP_detail;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;

        $scope.SOPDetail = [];
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
            ticketId : param.data.ticket_id, //티켓번호
            faultClassify : param.data.fault_classify, //장애구분
            faultType : param.data.fault_type, //장애유형
            faultDetailContent : param.data.fault_detail_content, //조치내용
            etcContent: param.data.etc_content, //기타사항
            rootCauseSysnamea : param.data.root_cause_sysnamea, //장비ID & 장비이름
            rootCauseSysnamea : param.data.root_cause_sysnamea, //장비ID & 장비이름
            ipAddra : param.data.ip_addra, //마스터IP
            rootCausePorta : param.data.root_cause_porta, //장비I/F
            ticketType : param.data.ticket_type, //티켓유형
            handlingFinUser : param.data.handling_fin_user, //마감자
            handlingFinTime : param.data.handling_fin_time //마감시간
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

        $scope.SOPSave = () => {
            const data = $scope.SOPDetailData;
            console.log('data', data);
            tools.createConfirmDlg(null, "수정하시겠습니까?", null).then(() => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_NIA_SOP_LIST",
                    TICKET_ID: data.ticketId,
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

        $scope.SOPDelete = () => {
            const data = $scope.SOPDetailData;
            tools.createConfirmDlg(null, "삭제하시겠습니까?", null).then(() => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "DELETE_SOP_LIST",
                NODE_NUM: param.data.SOP_num,
                NODE_ID: data.SOPId,
            }, function (result) {
                if (!result) return true;
                tools.showDemoText('삭제되었습니다.',red).then(function () {
                    emit();
                    $scope.dlgFunc.result({result: 'success'});
                });
            });
        });
        }
        
        $scope.onMoveDialog = () => {
            const element = document.querySelector('#ticketSopDetailDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".SOP-detail-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
            $scope.loadSopCodeList();
        });

    }
}

SOPDetailCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect', '$element','param'];

export default SOPDetailCtrl;
