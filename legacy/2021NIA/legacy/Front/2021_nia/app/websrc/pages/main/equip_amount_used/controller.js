import BaseController from 'scripts/controller/baseController'

class EquipAmountUsedCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.equip_amount_used;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.gridResult = [];
        
        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchAmountData = {
            startDateTime: '',
            endDateTime: '',
            measuredDatetime: '',
            equipment: '',
            cpu: '',
            mem:''
        }

        $scope.type = 'OnDemand'
        $scope.monitoringType = [
            'OnDemand',
            '실시간'            
        ]

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

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_EQUIP_AMOUNT_USED_PAGE2_LIST",
                START_DATE: $scope.searchAmountData.startDateTime != '' ? moment($scope.searchAmountData.startDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                END_DATE: $scope.searchAmountData.endDateTime != '' ? moment($scope.searchAmountData.endDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                MEASURED_DATETIME: $scope.searchAmountData.measuredDatetime != '' ? $scope.searchAmountData.measuredDatetime : null,
                EQUIPMENT: $scope.searchAmountData.equipment != '' ? $scope.searchAmountData.equipment : null,
                CPU: $scope.searchAmountData.cpu != '' ? ($scope.searchAmountData.cpu) / 100 : null,
                MEM: $scope.searchAmountData.mem != '' ? ($scope.searchAmountData.mem) / 100 : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false
                if (!result) return true;
                $scope.gridResult = result;
            })
        };
        
        $scope.searchClear = () => {
            $scope.searchAmountData.startDateTime = '';
            $scope.searchAmountData.endDateTime = '';
            $scope.searchAmountData.measuredDatetime = '';
            $scope.searchAmountData.equipment = '';
            $scope.searchAmountData.cpu = '';
            $scope.searchAmountData.mem = '';
            
            $scope.handleSearch();
            tools.showToast('초기화 되었습니다');
        };

        $scope.$watch('type', function () {
            const {type}  = $scope
            switch (type) {
                case '실시간':
                    $scope.loop()
                    break;
                default:
                    break;
            }
        });

        $scope.loop = () => {
            const {type}  = $scope
            if(type === '실시간') {
                $scope.handleSearch()
                setTimeout(() => {
                    $scope.loop()
                }, 60 * 1000);
            }
        }

        //페이징 다음페이지 로드시 리스트 뿌려주는 watch
        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });
        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#equipAmountUsed');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
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
EquipAmountUsedCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default EquipAmountUsedCtrl;
