import BaseController from 'scripts/controller/baseController'

class ByApplicationStatisticsSkillCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.by_application_statistics_skill;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.selectByAppTrafficList = [];

        $scope.searchAppData = {
            srcApplication: '',
            srcPort: '',
            dstApplication: '',
            dstPort: '',
            topN: ''
        }

        $scope.searchClear = () => {
            $scope.searchAppData.srcApplication = '';
            $scope.searchAppData.srcPort = '';
            $scope.searchAppData.dstApplication = '';
            $scope.searchAppData.dstPort = '';
            $scope.searchAppData.topN = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_BYAPP_TRAFFIC_TOP_NVT_LIST",
                SRC_APPLICATION_CODE: $scope.searchAppData.srcApplication != '' ? $scope.searchAppData.srcApplication : null,
                SRC_PORT: $scope.searchAppData.srcPort != '' ? $scope.searchAppData.srcPort : null,
                DST_APPLICATION_CODE: $scope.searchAppData.dstApplication != '' ? $scope.searchAppData.dstApplication : null,
                DST_PORT: $scope.searchAppData.dstPort != '' ? $scope.searchAppData.dstPort : null,
                TOP_N: $scope.searchAppData.topN != '' ? $scope.searchAppData.topN : null,
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.selectByAppTrafficList = result.map(item => {
                    const packetBytesInGbyte = item.packet_bytes / (1024 * 1024 * 1024); // 바이트를 기가바이트로 변환
                    const roundedGbyte = Math.round(packetBytesInGbyte * 100) / 100; // 소수점 두 자리까지 반올림
                    return {
                      ...item,
                      packet_bytes: roundedGbyte
                    };
                  });
            })
        };

        $scope.onInit = () => {
            $scope.getApplicationList();
        }

        $scope.getApplicationList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_APPLICATION_CODE_LIST",
            }, function (result) {
                $scope.SourceApplicationOptions  = result;
                $scope.DestinationApplicationOptions  = result;
            });
        };


        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#byApplicationStatisticsSkill');
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

        $scope.topNOptions=[
            {id: 1, name:10},
            {id: 2, name:30},
            {id: 3, name:50},
            {id: 4, name:100, selected: 'true'}
        ];
    }
}
ByApplicationStatisticsSkillCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default ByApplicationStatisticsSkillCtrl;
