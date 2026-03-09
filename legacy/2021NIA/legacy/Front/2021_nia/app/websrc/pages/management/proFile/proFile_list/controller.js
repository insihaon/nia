import BaseController from 'scripts/controller/baseController'

class ProFileListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.pro_file_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.selectProfileList = [];
        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchProfileData = {
            profileTitle: '',
            networkType: '',
            processingTemplate: ''
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
            $scope.searchProfileData.profileTitle = '';
            $scope.searchProfileData.networkType = '';
            $scope.searchProfileData.processingTemplate = '';
            $scope.handleSearch();
        };

        let isLoading = false
        $scope.handleSearch = () => {
            if(isLoading) return 
            isLoading = true
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_PROFILE_LIST",
                PROFILE_TITLE: $scope.searchProfileData.profileTitle != '' ? $scope.searchProfileData.profileTitle : null,
                NETWORK_TYPE: $scope.searchProfileData.networkType != '' ? $scope.searchProfileData.networkType : null,
                PROCESSING_TEMPLATE: $scope.searchProfileData.processingTemplate != '' ? $scope.searchProcessing($scope.searchProfileData.processingTemplate) : null,
                OFFSET: ($scope.historyPagingTable.selected-1)*$scope.historyPagingTable.limit,
                LIMIT: $scope.historyPagingTable.limit
            }, function (result) {

                var scrollable = document.getElementById('scrollable');
                scrollable.scrollTop = 0;

                isLoading = false
                if (!result) return true;
                $scope.selectProfileList = result;
            })
        };

        $scope.$watch('historyPagingTable.selected', function () {
            $scope.handleSearch();
        });

        $scope.$watch('historyPagingTable.limit', function () {
            $scope.handleSearch();
        });

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});

        // $scope.$on('parentSearch', function(e){
        //     $scope.handleSearch()
        // });

        $scope.exportExcel = () => {
            let csvOptions = {
                seq: {
                    csvColumn: {
                        field: ['rnum', 'profile_title', 'network_type', 'processing_template', 'auto_process_check', 'auto_process_start_datetime', 'auto_process_end_datetime', 'chng_datetime'],
                        header: ['번호','제목','네트워크','장애대응','자동처리기간(상시)','자동처리기간(시작)','자동처리기간(끝)', '수정일']
                    }
                }
            };
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.EXPORT_EXCEL,
                sqlId: "SELECT_PROFILE_LIST",
                OP_EXPORT_EXCEL: 'Y',
                FIELD: csvOptions.seq.csvColumn.field,
                FIELDNAMES: csvOptions.seq.csvColumn.header
            }, function (result) {
                if (result.excelUrl) {
                    let newFileName = '프로파일_' + moment(parseInt(result.excelUrl.split('.')[0])).format('YYYYMMDD') + '.xlsx';
                    tools.fnExportExcelSuccess(result, newFileName);
                }
            });
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

        $scope.proFileAdd = () => {
            $mdDialog.show({
                controller: tools.store.viewType.recovery.controller,
                templateUrl: tools.store.viewType.recovery.path,
                locals: {param: {}},
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

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#proFileList');
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

    }
}
ProFileListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default ProFileListCtrl;
