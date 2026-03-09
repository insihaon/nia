import BaseController from 'scripts/controller/baseController'

class SelfProcessDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param/* , $location */) {
        $scope.config = tools.store.viewType.selfProcess_dialog;
        super($injector, $scope, tools, $http, $timeout/* , $location */);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.selfProcessType = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchProcessData = {
            startDateTime:'',
            endDateTime:'',
            selfProcessType: '',
            closType: '',
            closStatus:''
        }

        $scope.closType = [
            {id:'1', status:'자동', value : 'A'},
            {id:'2', status:'수동', value : 'M'}
        ]
        $scope.closStatus = [
            {id:'1', status:'마감', value : 'Y'},
            {id:'2', status:'발생', value : 'N'}
        ]

        const recoveryType = [{id:'1', status:'비장애성', value : 'F'},{id:'2', status:'SYSLOG', value : 'S'}]
        const optimizationType = [{id:'1', status:'이상트래픽', value : 'A'},{id:'2', status:'유해트래픽', value : 'N'}]
        $scope.selfProcessType = param.type === 'recovery' ? recoveryType:optimizationType

        $scope.dialogTitle = param.type === 'recovery' ? '자가 회복 이력조회' : '자가 최적화 이력조회'


        $scope.searchProcessing= (data) =>{
            return $scope.chkProcessingTemplate.filter(v => v.display_name.includes(data)).map(val => val.value).join()

        }

        $scope.changeData = (value) => {
            let data = '';
            if(value.auto_process_check === true){
                data = '상시'
            }else if(value.auto_process_start_datetime && value.auto_process_end_datetime) {
                let startData = value.auto_process_start_datetime;
                let endData =  value.auto_process_end_datetime;

                data = startData + ' ~ ' + endData
            }
            return data
        };

        $scope.searchClear = () => {
            $scope.searchProcessData.startDateTime = '';
            $scope.searchProcessData.endDateTime = '';
            $scope.searchProcessData.selfProcessType = '';
            $scope.searchProcessData.closType = '';
            $scope.searchProcessData.closStatus = '';
            $scope.handleSearch();
        };


        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SELF_PROCESS_DETAIL_PAGE2_LIST",
                SELF_PROCESS_TYPE: $scope.searchProcessData.selfProcessType != '' ? $scope.searchProcessData.selfProcessType : null,
                CLOS_TYPE: $scope.searchProcessData.closType != '' ? $scope.searchProcessData.closType : null,
                TYPE : param.type != '' ? param.type : null,
                DATETYPE : param.dateType != '' ? param.dateType : null,
                DATETIME : param.data.hourly_time != '' ? param.data.hourly_time : null,
                CLOS_STATUS : $scope.searchProcessData.closStatus != '' ? $scope.searchProcessData.closStatus : null,
                START_DATE: $scope.searchProcessData.startDateTime != '' ? moment($scope.searchProcessData.startDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                END_DATE: $scope.searchProcessData.endDateTime != '' ? moment($scope.searchProcessData.endDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.processDetailInfo = result
            })
        };

        $scope.openDetail = (item) => {
            $mdDialog.show({
                controller: tools.store.viewType.selfProcess_email_dialog.controller,
                templateUrl: tools.store.viewType.selfProcess_email_dialog.path,
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
            }, function () {
            });
        };
        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#selfProcessDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $scope.handleSearch();
            $scope.$watch('historyPagingTable.selected', function () {
                $scope.handleSearch();
            });

            $scope.$watch('historyPagingTable.limit', function () {
                $scope.handleSearch();
            });
        });
    }
}
SelfProcessDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default SelfProcessDialogCtrl;
