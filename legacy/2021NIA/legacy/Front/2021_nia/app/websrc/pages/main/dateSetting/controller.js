import BaseController from 'scripts/controller/baseController'

class DateSettingCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, param) {
        $scope.config = tools.store.viewType.date_setting;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        // $scope.actionMode = $scope.ticket_id ?  "TICKET": "ALL";

        const allCategoryList = [
            { value: 'fault', label: '장애' },
            { value: 'ticket-att2', label: '이상 트래픽' },
            { value: 'ticket-ntt', label: '유해 트래픽' },
            { value: 'ticket-nftt', label: '장비부하 장애' },
            { value: 'perf', label: '광신호이상' },
            { value: 'resources', label: '시설' },
            { value: 'test', label: '시험데이터' }
        ];
        const ticketCategoryList = [
            // {value: 'ticket-att', label: '이상 트래픽'},
            { value: 'ticket-ntt', label: '유해 트래픽' },
            { value: 'ticket-att2', label: '이상 트래픽' },
            { value: 'ticket-nftt', label: '장비부하장애' }
        ];

        $scope.isOpenHistory = false;
        $scope.isDeleteBtn = false;
        $scope.snapshotData = {
            title: '',
            detail: 'test',
            startTime: new Date().setHours(0, 0, 0, 0),
            endTime: new Date().setHours(23, 59, 59, 0),
            eventNo: '',
            history: []
        };

        let ticket = $scope.param.ticket || {}
        if (ticket === "ALL") {
            ticket = $scope.param.ticket = {
                ticket_id: "ALL"
            }
        }

        if (ticket.ticket_id === "ALL") {
            $scope.ticketCategory = allCategoryList
            // $scope.ticketCategoryDisabled = false
        } else {
            $scope.ticketCategory = ticketCategoryList
            $scope.ticketCategoryDisabled = true
            let detail
            switch (ticket.ticket_type) {
                // case 'ATT':
                //     detail = 'ticket-att'
                //     break;
                case 'ATT2':
                    detail = 'ticket-att2'
                    break;
                case 'NFTT':
                    detail = 'ticket-nftt'
                    break;
                case 'NTT':
                default:
                    detail = 'ticket-ntt'
                    break;
            }
            $scope.snapshotData.detail = detail
        }
        $scope.changeLoadHistory = (detail) => {
            $scope.snapshotData.detail = detail
            $scope.loadHistory()
        }

        const emit = () => {
            $scope.$broadcast('onChangedHistoryCode', {});
        }

        $scope.showHistory = () => {
            $scope.isOpenHistory = !$scope.isOpenHistory;
        };

        $scope.loadHistory = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.SELECT_SEND_DATA_SNAPSHOT_LIST,
                EVENT_GB: $scope.snapshotData.detail
            }, function (result) {
                $scope.snapshotData.history = result;
            })
        };

        $scope.deleteListTest = (item) => {
            tools.createConfirmDlg("삭제하시겠습니까?", null, null).then(() => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "DELETE_DATA_SNAPSHOT_LIST",
                    EVENT_NO: item.eventno
                }, function (result) {
                    if (result) {
                        $tools.showAlert("삭제되었습니다.").then(function () {
                            emit();
                        })
                    } else {
                        return false;
                    }
                })
            })

            // var check_length = document.getElementsByName("checkList").length;
            // tools.createConfirmDlg("삭제하시겠습니까?",null,null).then(()=>{
            //     for (var i=0; i<check_length; i++) {
            //             var checkList = document.getElementsByName("checkList")[i]
            //             var eventNoVal = JSON.parse(document.getElementsByName("checkList")[i].value).eventno;
            //         if (checkList.checked === true) {
            //             $scope.isDeleteBtn = true;
            //             tools.http({
            //                 service: tools.constants.Service.rca,
            //                 action: "DELETE_DATA_SNAPSHOT_LIST",
            //                 EVENT_NO: eventNoVal
            //             }, function (result) {
            //                     if(result){
            //                         $tools.showAlert("삭제되었습니다.");
            //                         $scope.loadHistory();
            //                         return true;
            //                     }else{
            //                         return false;
            //                     }
            //             })
            //         }
            //     }
            // })
        }

        $scope.save = () => {
            if (!$scope.snapshotData.title) {
                tools.showAlert('제목을 입력해주십시오.');
                return;
            }
            if (!$scope.snapshotData.startTime) {
                tools.showAlert('시작 시간을 입력해주십시오.');
                return;
            }
            if (!$scope.snapshotData.endTime) {
                tools.showAlert('종료 시간을 입력해주십시오.');
                return;
            }

            if (new Date($scope.snapshotData.endTime).getTime() - new Date($scope.snapshotData.startTime).getTime() > 604800000) {
                tools.showAlert('시작 시간과 종료 시간의 차이는 7일 이상 차이 날 수 없습니다.');
                return;
            }

            tools.createConfirmDlg("저장하시겠습니까?", null, null).then(function () {
                const ticket_id = $scope.param.ticket.ticket_id
                const param = {
                    service: tools.constants.Service.rca,
                    action: tools.constants.Action.CHANGE_TICKET_STATUS,
                    eventType: 'REQUEST_DATA_SNAPSHOT',
                    title: $scope.snapshotData.title,
                    startTime: $scope.snapshotData.startTime,
                    endTime: $scope.snapshotData.endTime,
                    detail: $scope.snapshotData.detail,
                    ticket_id: ticket_id
                }
                ticket_id === 'ALL' && delete param.ticket_id

                tools.http(param, function (result) {
                    tools.showAlert('저장 되었습니다.');
                    $scope.loadHistory();
                })
            })
        };

        $scope.getMinStartDate = (date) => {
            return new Date(date).addDays(-7).getTime();
        };

        $scope.getMaxStartDate = (date) => {
            return new Date(date).addDays(7).getTime();
        };

        $scope.onInit = () => {
            $scope.loadHistory();
            $scope.$on('onChangedHistoryCode', function (e) {
                $scope.loadHistory();
            });
        }

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#dateSetting');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

    }
}
DateSettingCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', 'param'];

export default DateSettingCtrl;
