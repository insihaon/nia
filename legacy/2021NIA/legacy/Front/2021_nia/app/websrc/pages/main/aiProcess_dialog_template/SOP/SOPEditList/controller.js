import BaseController from 'scripts/controller/baseController'

class SOPEditListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.SOP_edit_list;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null
        
        $scope.model = {
            condition: {
                start_date: '',
                end_date: '',
                user_id: '',
                fault_gb: '',
                fault_type: '',
            },
            gridResult: {},
            pagination: {
                selected: 1,
                limit: 50,
                total: 0
            }
        }

        $scope.searchClear = () => {
            $scope.model.condition = {};
            $scope.handleSearch();
        };

        $scope.handleSearch = () => {
            const {start_date, end_date, user_id, fault_gb, fault_type} = $scope.model.condition
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SOP_CODE_PAGE2_LIST",
                OFFSET: ($scope.model.pagination.selected - 1) * $scope.model.pagination.limit,
                LIMIT: $scope.model.pagination.limit,
                START_DATE: val(start_date),
                END_DATE: val(end_date),
                USER_ID: val(user_id),
                FAULT_GB: val(fault_gb),
                FAULT_TYPE: val(fault_type),
            }, function (result) {
                if(result && result.list.length > 0) {
                    $scope.model.gridResult = result.list;
                    $scope.model.pagination.total = result.total || 0
                } else {
                    $scope.model.gridResult = {};
                }
            })
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#SOPEditDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };
        
        angular.element(document).ready(() => {
            $scope.handleSearch()
            $scope.$on('onChangedSopCode', function(e){
                $scope.handleSearch()
            });

            setTimeout(() => {
                $scope.$watch('model.pagination.selected', function () {
                    $scope.handleSearch();
                });

                $scope.$watch('model.pagination.limit', function () {
                    $scope.handleSearch();
                });
            }, 3000);
        });


        $scope.handleSopCodeAdd = (editType = 'C', row = {}) => {
            const data = Object.assign(row, { editType })
            $mdDialog.show({
                controller: tools.store.viewType.SOP_edit.controller,
                templateUrl: tools.store.viewType.SOP_edit.path,
                locals: { param: { data } },
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

    }
}
SOPEditListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default SOPEditListCtrl;
