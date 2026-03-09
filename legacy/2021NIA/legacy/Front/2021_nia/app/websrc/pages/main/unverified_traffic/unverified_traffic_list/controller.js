/**
 * Created by HYS on 2019-07-12.
 */
import BaseController from 'scripts/controller/baseController'

class UnverifiedTrafficListCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect) {
        $scope.config = tools.store.viewType.unverified_traffic_list;
        super($injector, $scope, tools, $http, $timeout);

        const val = (value) => value ? value : null

        $scope.tab1 = {
            param: {
                countryName: '',
                countryCode: '',
            },
            gridResult: [],
            pagination : {
                selected: 1,
                limit: 50,
                total: 0
            }
        }

        $scope.tab2 = {
            param: {
                nrenName: '',
                nrenIp: '',
            },
            gridResult: [],
            pagination : {
                selected: 1,
                limit: 50,
                total: 0
            }
        }

        $scope.tab3 = {
            param: {
                appName: '',
                port: '',
            },
            gridResult: [],
            pagination : {
                selected: 1,
                limit: 50,
                total: 0
            }
        }

        $scope.tab1.pagination = {
            selected: 1,
            limit: 50,
            total: 0
        };
        $scope.tab2.pagination = {
            selected: 1,
            limit: 50,
            total: 0
        };
        $scope.tab3.pagination = {
            selected: 1,
            limit: 50,
            total: 0
        };

        $scope.searchClear1 = () => {
            $scope.tab1.param = {}
            $scope.handleSearch1()
        }

        $scope.handleSearch1 = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_COUNTRY_IP_PAGE2_LIST",
                OFFSET: ($scope.tab1.pagination.selected - 1) * $scope.tab1.pagination.limit,
                LIMIT: $scope.tab1.pagination.limit,
                COUNTRY_NAME: val($scope.tab1.param.countryName),
                COUNTRY_CODE: val($scope.tab1.param.countryCode),
            }, function (result) {

                var scrollable = document.getElementById('countryScroll');
                scrollable.scrollTop = 0;

                if(result && result.list.length > 0) {
                    $scope.tab1.gridResult = result.list;
                    $scope.tab1.pagination.total = result.total || 0
                } else {
                    $scope.tab1.gridResult = [];
                }
            });
        };

        $scope.searchClear2 = () => {
                $scope.tab2.param = {}
                $scope.handleSearch2()
        }

        $scope.handleSearch2 = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_IP_PAGE2_LIST",
                OFFSET: ($scope.tab2.pagination.selected - 1) * $scope.tab2.pagination.limit,
                LIMIT: $scope.tab2.pagination.limit,
                NREN_NAME: val($scope.tab2.param.nrenName),
                NREN_IP: val($scope.tab2.param.nrenIp),
            }, function (result) {

                var scrollable = document.getElementById('agencyScroll');
                scrollable.scrollTop = 0;

                if(result && result.list.length > 0) {
                    $scope.tab2.gridResult = result.list;
                    $scope.tab2.pagination.total = result.total || 0
                } else {
                    $scope.tab2.gridResult = [];
                }
            });
        };

        $scope.searchClear3 = () => {
            $scope.tab3.param = {}
            $scope.handleSearch3()
        }

        $scope.handleSearch3 = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_APP_IP_PAGE2_LIST",
                OFFSET: ($scope.tab3.pagination.selected - 1) * $scope.tab3.pagination.limit,
                LIMIT: $scope.tab3.pagination.limit,
                APP_NAME: val($scope.tab3.param.appName),
                PORT: val($scope.tab3.param.port)
            }, function (result) {

                var scrollable = document.getElementById('appScroll');
                scrollable.scrollTop = 0;

                if(result && result.list.length > 0) {
                    $scope.tab3.gridResult = result.list;
                    $scope.tab3.pagination.total = result.total || 0
                } else {
                    $scope.tab3.gridResult = [];
                }
            });
        };

        $scope.handleSearch1();
        $scope.handleSearch2();
        $scope.handleSearch3();

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#alarmArea');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            setTimeout(() => {
                $scope.$watch('tab1.pagination.selected', function () {
                    $scope.handleSearch1();
                });
                $scope.$watch('tab1.pagination.limit', function () {
                    $scope.handleSearch1();
                });

                $scope.$watch('tab2.pagination.selected', function () {
                    $scope.handleSearch2();
                });
                $scope.$watch('tab2.pagination.limit', function () {
                    $scope.handleSearch2();
                });

                $scope.$watch('tab3.pagination.selected', function () {
                    $scope.handleSearch3();
                });
                $scope.$watch('tab3.pagination.limit', function () {
                    $scope.handleSearch3();
                });
            }, 3000);
        });

        $scope.handleApplicationAdd = (editType = 'C', row = {}) => {
            const data = Object.assign(row, { editType })
            $mdDialog.show({
                controller: tools.store.viewType.application_add.controller,
                templateUrl: tools.store.viewType.application_add.path,
                locals: { param: { data, item : $scope } },
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
        
        $scope.handleAgencyAdd = (editType = 'C', row = {}) => {
            const data = Object.assign(row, { editType })
            // debugger
            $mdDialog.show({
                controller: tools.store.viewType.traffic_agency_add.controller,
                templateUrl: tools.store.viewType.traffic_agency_add.path,
                locals: { param: {data: data, item : $scope} },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                // debugger
                if(result) {
                    $scope.handleSearch();
                }
            });
        };

    }
}

UnverifiedTrafficListCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect'];
export default UnverifiedTrafficListCtrl;
