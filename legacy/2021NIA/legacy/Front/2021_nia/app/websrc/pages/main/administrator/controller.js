/**
 * Created by HYS on 2019-07-12.
 */
import BaseController from 'scripts/controller/baseController'

class AdministratorDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect) {
        $scope.config = tools.store.viewType.administrator;
        super($injector, $scope, tools, $http, $timeout);


        Object.assign($scope.dlgFunc, {
            result: function (item, changeLvl) {
                $scope.tools.createConfirmDlg(
                    '설정 하시겠습니까?',
                    '확인을 선택하면 데이터가 저장됩니다.', null)
                .then(function () {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "UPDATE_USER_GRANT_LIST",
                        NAME: item.name,
                        ID: String(item.id),
                        LVL_VALUE: Number(item.lvl_copy),
                        OFFSET: 0,
                        KEYWORD: $scope.rcaSearchTextArea ? String($scope.rcaSearchTextArea) : null
                    }, function (result) {
                        if (!result) return true;
                        angular.forEach(result, function (item) {
                            item.chanagedGrantArray = [];
                            angular.forEach(tools.constants.UserGrant, function (key) {
                                if ((item.lvl >>> 0).toString(2).split('').reverse()[key.index] == 1) {
                                    item.chanagedGrantArray.push(key)
                                } else {
                                    return;
                                }
                            })
                        });
                        // $scope.gridOptions.data = result;
                        $scope.handleSearch();
                    }).then(function() {
                        tools.showAlert('수정되었습니다.');
                    });
                });
            }
        });

        $scope.gridOptions = {};
        $scope.handleSearch = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_USER_LIST",
                IS_ALL: true
            }, function (result) {
                if (!result) return true;
    
                $scope.gridOptions.data = result;
    
                $scope.isCheck = (item, value) => {
                    return !!(item.lvl & value);
                }
                $scope.lvlTable.totalCount = result.count;
    
                angular.forEach(tools.constants.UserGrant, function (key) {
                    if ((item.lvl & key.value) != 0) {
                        item.chanagedGrantArray.push(key)
                    }
                });
            });
        }

        $scope.$watch('lvlTable.selected', (n, o) => {
            if (n === o){ return; }

            $scope.gridOptions = {};

            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_USER_LIST',
                IS_ALL: true
            }, function (result) {
                angular.forEach(result, function (item) {
                    item.chanagedGrantArray = [];
                    angular.forEach(tools.constants.UserGrant, function (key) {
                        if ((item.lvl >>> 0).toString(2).split('').reverse()[key.index] == 1) {
                            item.chanagedGrantArray.push(key)
                        } else {
                            return;
                        }
                    })
                })
                $scope.gridOptions.data = result;
            });
        });

        $scope.checkAll = function () {
            return true;
        };
        $scope.lvlTable = {
            selected: 1,
            totalCount: 0,
            lastPage: 0,
            pages: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            searchEditNumber: 1
        };

        $scope.searchClear = function () {
            $scope.gridOptions = {};

            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_USER_LIST',
                IS_ALL: true
            }, function (result) {
                angular.forEach(result, function (item) {
                    item.chanagedGrantArray = [];
                    angular.forEach(tools.constants.UserGrant, function (key) {
                        if ((item.lvl >>> 0).toString(2).split('').reverse()[key.index] == 1) {
                            item.chanagedGrantArray.push(key)
                        } else {
                            return;
                        }
                    })
                })
                $scope.gridOptions.data = result;
                $scope.rcaSearchTextArea = null;
                $scope.checkAll = function () {
                    return true
                };
                $scope.lvlTable.selected = 1;
            });
        };

        $scope.getSelectedText = (item) => {
            var arr = item.chanagedGrantArray;
            if (arr && arr.length > 0) {
                return `${arr.length} 선택됨`
            } else {
                return "선택하세요";
            }
        };
        $scope.applyRcaSearch = function (rcaSearchTextArea, selector) {
            var grant = 0;
            selector.forEach(selector => {
                if (selector) {
                    angular.forEach(tools.constants.UserGrant, function (key) {
                        var grantValue = 0;
                        if (selector == key.text) {
                            grantValue = key.value;
                        }
                        grant = grant | grantValue;
                    });
                }
            });
            $scope.gridOptions = {};
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_USER_LIST",
                IS_ALL: true,
                NAME: rcaSearchTextArea,
                LVL_VALUE: Number(grant),
            }, function (result) {
                angular.forEach(result, function (item) {
                    item.chanagedGrantArray = [];
                    angular.forEach(tools.constants.UserGrant, function (key) {
                        if ((item.lvl >>> 0).toString(2).split('').reverse()[key.index] == 1) {
                            item.chanagedGrantArray.push(key)

                        } else {
                            return;
                        }
                    });
                });

                if (!result) return true;
                $scope.gridOptions.data = result;
                $scope.isCheck = (item, value) => {
                    return !!(item.lvl & value);
                }
            })
        };

        $scope.onSelectOpen = function (item, value) {
            var lvl = item.lvl_copy;
            $scope.grantName = angular.forEach(tools.constants.UserGrant, function (key) {
            });
            $scope.isCheck = (item, value) => {
                return !!(item.lvl & value);
            }
        };

        $scope.grantName = angular.forEach(tools.constants.UserGrant, function (key) {
        });

        $scope.init = function (item) {
            item.chanagedGrantArray = [];
            $scope.grantName = angular.forEach(tools.constants.UserGrant, function (key) {
                if ((item.lvl >>> 0).toString(2).split('').reverse()[key.index] == 1) {
                    item.chanagedGrantArray.push(key)
                } else {
                    return;
                }
            })
        }

        $scope.onSelectChanged = function (item) {
            var value = 0;
            item.lvl_copy = 0;

            angular.forEach(item.chanagedGrantArray, function (key) {
                value = key.value

                item.lvl_copy |= value;
            })
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#administrator');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $scope.handleSearch();

            $(window).resize(function () {
                $("table.ticket-alarm > *").width($("table.ticket-alarm").width() + $("table.ticket-alarm").scrollLeft());
            });
            $("table.ticket-alarm").on('scroll', function () {
                $("table.ticket-alarm > *").width($("table.ticket-alarm").width() + $("table.ticket-alarm").scrollLeft());
            });
            $("md-dialog.alarm-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
        });

    }
}

AdministratorDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect'];
export default AdministratorDialogCtrl;
