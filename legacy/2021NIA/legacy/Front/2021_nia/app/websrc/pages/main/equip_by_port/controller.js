import BaseController from 'scripts/controller/baseController'

class EquipByPortCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.equip_by_port;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null

        $scope.isReadOnly = true;
        $scope.gridResult = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchByPortData = {
            equipment: '',
            interface: '',
            startDateTime: '',
            endDateTime: ''
        }

        $scope.chkProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            { value: 'construction', display_name: '공사'}
        ];

        $scope.searchProcessing= (data) =>{
            return $scope.chkProcessingTemplate.filter(v => v.display_name.includes(data)).map(val => val.value).join()
            
        }
        $scope.searchClear = () => {
            $scope.searchByPortData.startDateTime = '';
            $scope.searchByPortData.endDateTime = '';
            $scope.searchByPortData.equipment = '';
            $scope.searchByPortData.interface = '';
            
            $scope.handleSearch();
            tools.showToast('초기화 되었습니다');
        };
        
        $scope.type = 'OnDemand'
        $scope.monitoringType = [
            'OnDemand',
            '실시간'            
        ]
        $scope.performanceList = [
            {id:'1', list:'성능항목1'},
            {id:'2', list:'성능항목2'},
            {id:'3', list:'성능항목3'},
        ]

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_INOUT_TRAFFIC_BYPORT_NEW_PAGE2_LIST",
                // action: "SELECT_NIA_INOUT_TRAFFIC_BYPORT_PAGE2_LIST",
                EQUIPMENT: $scope.searchByPortData.equipment != '' ? $scope.searchByPortData.equipment : null,
                INTERFACE: $scope.searchByPortData.interface != '' ? $scope.searchByPortData.interface : null,
                START_DATE: $scope.searchByPortData.startDateTime != '' ? moment($scope.searchByPortData.startDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                END_DATE: $scope.searchByPortData.endDateTime != '' ? moment($scope.searchByPortData.endDateTime).format('YYYY-MM-DD HH:mm:ss') : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false;
                if (!result) return true;
                $scope.gridResult = result;
            })
        };
        
        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

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
            const element = document.querySelector('#equipPyPort');
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
EquipByPortCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default EquipByPortCtrl;
