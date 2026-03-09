import BaseController from 'scripts/controller/baseController'

class AlarmManagementCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.alarm_management;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.gridResult = {};

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchAlarmData = {
            ticketId:'',
            occurStartTime: '',
            occurEndTime: '',
            deadlineStartTime: '',
            deadlineEndTime: '',
            ticketType: ''
        }

        $scope.chkProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            { value: 'construction', display_name: '공사'}
        ];

        $scope.searchProcessing= (data) =>{
            return $scope.chkProcessingTemplate.filter(v => v.display_name.includes(data)).map(val => val.value).join()

        }

        $scope.ticketType = [
            {id:'1', ticket:'장비장애(RT)', value : 'RT'},
            {id:'2', ticket:'유해트래픽(NTT)', value : 'NTT'},
            {id:'3', ticket:'이상트래픽(ATT2)', value : 'ATT2'},
        ]

        $scope.searchClear = () => {
            $scope.searchAlarmData.ticketId = '';
            $scope.searchAlarmData.occurStartTime = '';
            $scope.searchAlarmData.occurEndTime = '';
            $scope.searchAlarmData.deadlineStartTime = '';
            $scope.searchAlarmData.deadlineEndTime = '';
            $scope.searchAlarmData.ticketType = '';
            $scope.handleSearch();
        };

        $scope.handleSearch = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_ALARM_MANAGEMAENT_PAGE2_LIST",
                TICKET_ID: $scope.searchAlarmData.ticketId != '' ? $scope.searchAlarmData.ticketId : null,
                OCCUR_START_DATE: $scope.searchAlarmData.occurStartTime != '' ? moment($scope.searchAlarmData.occurStartTime).format('YYYY-MM-DD HH:mm:ss') : null,
                OCCUR_END_DATE: $scope.searchAlarmData.occurEndTime != '' ? moment($scope.searchAlarmData.occurEndTime).format('YYYY-MM-DD HH:mm:ss') : null,
                DEADLINE_START_DATE: $scope.searchAlarmData.deadlineStartTime != '' ? moment($scope.searchAlarmData.deadlineStartTime).format('YYYY-MM-DD HH:mm:ss') : null,
                DEADLINE_END_DATE: $scope.searchAlarmData.deadlineEndTime != '' ? moment($scope.searchAlarmData.deadlineEndTime).format('YYYY-MM-DD HH:mm:ss') : null,
                TICKET_TYPE: $scope.searchAlarmData.ticketType != '' ? $scope.searchAlarmData.ticketType : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                if (!result) return true;
                $scope.gridResult = result;
            })
        };

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
        
        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#alarmManagement');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".profile-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });

            setTimeout(() => {
                $scope.$watch('historyPagingTable.selected', function () {
                    $scope.handleSearch();
                });

                $scope.$watch('historyPagingTable.limit', function () {
                    $scope.handleSearch();
                });

            }, 5 * 1000);

        });

        $scope.Options=[
            {id: 1, name:'유'},
            {id: 2, name:'무'},
        ]

    }
}
AlarmManagementCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default AlarmManagementCtrl;
