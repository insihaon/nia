import BaseController from 'scripts/controller/baseController'

class ByCountryStatisticsSkillCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.by_country_statistics_skill;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.selectByCountryTrafficList = [];
        $scope.selectSrcCountryList = [];

        $scope.searchCountryData = {
            srcCountryCode: '',
            dstCountryCode: '',
            topN: ''
        }

        $scope.chkProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            { value: 'construction', display_name: '공사'}
        ];

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

        $scope.searchClear = () => {
            $scope.searchCountryData.srcCountryCode = '';
            $scope.searchCountryData.dstCountryCode = '';
            $scope.searchCountryData.topN = '';
            $scope.handleSearch();
        };


        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_BYCOUNTRY_TRAFFIC_TOP_NVT_LIST",
                SRC_COUNTRY_CODE: $scope.searchCountryData.srcCountryCode != '' ? $scope.searchCountryData.srcCountryCode : null,
                DST_COUNTRY_CODE: $scope.searchCountryData.dstCountryCode != '' ? $scope.searchCountryData.dstCountryCode : null,
                TOP_N: $scope.searchCountryData.topN != '' ? $scope.searchCountryData.topN : null,
            }, function (result) {
                isLoading = false
                if (!result) return true;
                $scope.selectByCountryTrafficList = result.map(item => {
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
            $scope.getCountryList();
            $scope.handleSearch();
        }

        $scope.getCountryList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_COUNTRY_CODE_LIST",
            }, function (result) {
                $scope.SourceCountryOptions = result;
                $scope.DestinationCountryOptions  = result;
            });
        };
        
        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#byCountryStatisticsSkill');
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
ByCountryStatisticsSkillCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default ByCountryStatisticsSkillCtrl;
