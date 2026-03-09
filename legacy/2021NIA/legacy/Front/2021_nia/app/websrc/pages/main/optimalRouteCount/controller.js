import BaseController from 'scripts/controller/baseController'

class OptimalRouteCountCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.optimal_route_count;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.selectOptimalRouteAllList = [];
        $scope.selectOptimalRouteList = [];
        $scope.selectProfileList = [];
        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchProfileData = {
            transmissionValue: '',
            receptionValue: '',
            processingTemplate: ''
        }

        
        
        $scope.optimalRouteDetail = (linkId) => {
            $scope.selectOptimalRouteAllList.forEach((linkList) => {
                if(linkList.current_paths[0].link_id === linkId){
                    $scope.selectOptimalRouteList = linkList.current_paths;
                }
            });
        };

        $scope.searchClear = () => {
            $scope.searchProfileData.transmissionValue = '';
            $scope.searchProfileData.receptionValue = '';
            $scope.searchProfileData.processingTemplate = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            // IP_SDN result(전체 E2E 경로 조회)
            $tools.sdnRequest('GET', 'ipsdn/opt/cur_route/all').then(result => {
                isLoading = false
                const data = $scope.selectOptimalRouteAllList = result.data;

                const a = new Set()
                const z = new Set()
                data.map(d => d.current_paths).flat(Infinity).forEach(d => {
                        a.add(d.send_node_name)
                        z.add(d.receive_node_name)
                })
                    $scope.transmissionOptions = [...a] // ... = 리스트로 대입
                    $scope.receptionOptions = [...z]
            })
        };


        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#optimalRouteCount');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {$scope.handleSearch()
            $(".profile-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });
    }
}
OptimalRouteCountCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default OptimalRouteCountCtrl;
