import BaseController from 'scripts/controller/baseController'

class OptimalRouteSettingCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.optimal_route_setting;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });
        $scope.param = param;

        $scope.gridResult = [];
        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchBypassRouteData = {
            startDateTime: '',
            endDateTime: '',
            ticketNum: ''
        }

        $scope.searchClear = () => {
            $scope.searchBypassRouteData.startDateTime = '';
            $scope.searchBypassRouteData.endDateTime = '';
            $scope.searchBypassRouteData.ticketNum = '';
            $scope.handleSearch();
        };

        $scope.routeAddDataList = {
            userId: 'test',
            result : '성공',
            handlingType : '복원'
        };
        $scope.isRestore = false;

        $scope.onClickRestore = (sourcedate) => {
            // IP_SDN result(경로 복원)
            // 'psdn/opt/optimization/config/restore?sourcedate=' + sourcedate
            tools.sdnRequest('POST', 'ipsdn/opt/optimization/config/restore?sourcedate='+sourcedate).then(result => {
                if(!result.status){
                    tools.showAlert("경로 복원 대상이 아닙니다.")
                }else{
                    tools.createConfirmDlg(null, "경로를 복원 하시겠습니까?", null).then(() => {
                        const data = $scope.routeAddDataList;
                        tools.http({
                            service: tools.constants.Service.rca,
                            action: "INSERT_NIA_OPTIMAL_ROUTE_LIST",
                            TICKET_ID: $scope.param.ticketInfo.id,
                            USER_ID: data.userId,
                            RESULT: data.result,
                            HANDLING_TYPE: data.handlingType,
                            LINK_ID: $scope.param.optimalRouteInfo[0].link_id,
                        }, function (result) {
                            if (result)
                            tools.showToast('복원되었습니다.');

                        });
                    })
                    }
            })
        }

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_OPTIMAL_ROUTE_PAGE2_LIST",
                START_DATE: $scope.searchBypassRouteData.startDateTime != '' ? moment($scope.searchBypassRouteData.startDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                END_DATE: $scope.searchBypassRouteData.endDateTime != '' ? moment($scope.searchBypassRouteData.endDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                TICKET_NUM: $scope.searchBypassRouteData.ticketNum != '' ? $scope.searchBypassRouteData.ticketNum : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.gridResult = result.list;
            })
        };

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#optimalRouteSetting');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            setTimeout(() => {
                $scope.$watch('historyPagingTable.selected', function () {
                    $scope.handleSearch();
                });
        
                $scope.$watch('historyPagingTable.limit', function () {
                    $scope.handleSearch();
                });
            }, 3000);
        });

        $scope.profileDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.recovery_detail.controller,
                templateUrl: tools.store.viewType.recovery_detail.path,
                locals: {param: {data: item}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            });
        };

        angular.element(document).ready(() => {
            $(".profile-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

        $scope.Options=[
            {id: 1, name:'유'},
            {id: 2, name:'무'},
        ]

    }
}
OptimalRouteSettingCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default OptimalRouteSettingCtrl;
