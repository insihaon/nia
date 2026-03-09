import BaseController from 'scripts/controller/baseController'

class ByAgencyStatisticsSkillCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.by_agency_statistics_skill;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.selectByAgencyTrafficList = [];

        $scope.searchAgencyData = {
            srcAgencyId: '',
            dstAgencyId: '',
            srcAgencyIp: '',
            dstAgencyIp: '',
            topN: ''
        }

        $scope.searchClear = () => {
            $scope.searchAgencyData.srcAgencyId = '';
            $scope.searchAgencyData.dstAgencyId = '';
            $scope.searchAgencyData.srcAgencyIp = '';
            $scope.searchAgencyData.dstAgencyIp = '';
            $scope.searchAgencyData.topN = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_BYAGENCY_TRAFFIC_TOP_NVT_LIST",
                SRC_AGENCY_ID: $scope.searchAgencyData.srcAgencyId != '' ? $scope.searchAgencyData.srcAgencyId : null,
                DST_AGENCY_ID: $scope.searchAgencyData.dstAgencyId != '' ? $scope.searchAgencyData.dstAgencyId : null,
                SRC_AGENCY_IP: $scope.searchAgencyData.srcAgencyIp != '' ? $scope.searchAgencyData.srcAgencyIp : null,
                DST_AGENCY_IP: $scope.searchAgencyData.dstAgencyIp != '' ? $scope.searchAgencyData.dstAgencyIp : null,
                TOP_N: $scope.searchAgencyData.topN != '' ? $scope.searchAgencyData.topN : null,
            }, function (result) {
                isLoading = false;
                if (!result) return true;
                $scope.selectByAgencyTrafficList = result.map(item => {
                    const packetBytesInGbyte = item.packet_bytes / (1024 * 1024); // 바이트를 메가바이트로 변환
                    const roundedGbyte = Math.round(packetBytesInGbyte * 100) / 100; // 소수점 두 자리까지 반올림
                    return {
                      ...item,
                      packet_bytes: roundedGbyte
                    };
                  });
            })
        };

        $scope.onInit = () => {
            $scope.handleSearch();
            $scope.getAgencyList();
        }

        $scope.getAgencyList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_CODE_LIST",
            }, function (result) {
                $scope.SourceAgencyOptions  = result;
                $scope.DestinationAgencyOptions  = result;
            });
        };


        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#byAgencyStatisticsSkill');
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
ByAgencyStatisticsSkillCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default ByAgencyStatisticsSkillCtrl;
