import * as Constants from "../../scripts/class/Constants";

var menus = function($css, $state, $mdDialog, tools, storeService, $timeout) {
    return {
    	restrict: 'EA',
        replace: true,
        scope: {
            type: '@',
            toggleEdit: '&',
            toggleHeader: '&'
        },
        templateUrl: 'pages/directive/top-menu.html',
        link: function($scope, element, attrs) {

        	$scope.menus = storeService.store.menus;
            $scope.current = $state.current;
            $scope.disabled = true;
        	tools.injection($scope);

            $scope.openMonitoring = () => {
                tools.statego(tools.store.viewType.monitoring_tt.type);
            };

            $scope.openEquipByPort = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.equip_by_port.controller,
                    templateUrl: tools.store.viewType.equip_by_port.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openEquipAmountUsed = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.equip_amount_used.controller,
                    templateUrl: tools.store.viewType.equip_amount_used.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openAlarmManagement = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.alarm_management.controller,
                    templateUrl: tools.store.viewType.alarm_management.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openSyslogRuleManagement = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.syslog_rule_management.controller,
                    templateUrl: tools.store.viewType.syslog_rule_management.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openSyslogDialog = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.mon_tt_syslog_dialog.controller,
                    templateUrl: tools.store.viewType.mon_tt_syslog_dialog.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
        };

            $scope.openunverifiedTrafficList = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.unverified_traffic_list.controller,
                    templateUrl: tools.store.viewType.unverified_traffic_list.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openByApplicationStatisticsSkill = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.by_application_statistics_skill.controller,
                    templateUrl: tools.store.viewType.by_application_statistics_skill.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openByCountryStatisticsSkill = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.by_country_statistics_skill.controller,
                    templateUrl: tools.store.viewType.by_country_statistics_skill.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openByAgencyStatisticsSkill = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.by_agency_statistics_skill.controller,
                    templateUrl: tools.store.viewType.by_agency_statistics_skill.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            
            // $scope.openOptimalRouteSetting = (event) => {
            //     $mdDialog.show({
            //         controller: tools.store.viewType.optimal_route_setting.controller,
            //         templateUrl: tools.store.viewType.optimal_route_setting.path,
            //         locals: {param: null},
            //         parent: angular.element(document.body),
            //         disableParentScroll: false,
            //         targetEvent: event,
            //         clickOutsideToClose: false,
            //         fullscreen: false
            //     })
            // };

            // $scope.openOptimalRouteCount = (event) => {
            //     $mdDialog.show({
            //         controller: tools.store.viewType.optimal_route_count.controller,
            //         templateUrl: tools.store.viewType.optimal_route_count.path,
            //         locals: {param: null},
            //         parent: angular.element(document.body),
            //         disableParentScroll: false,
            //         targetEvent: event,
            //         clickOutsideToClose: false,
            //         fullscreen: false
            //     })
            // };

        	$scope.openAIProcess = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.ai_process_template.controller,
                    templateUrl: tools.store.viewType.ai_process_template.path,
                    locals: {param: null},
                    parent: angular.element(document.body),
                    disableParentScroll: false,
                    targetEvent: event,
                    clickOutsideToClose: false,
                    fullscreen: false
                })
            };

            $scope.openSOP = () => {
                $mdDialog.show({
                    controller: tools.store.viewType.SOP.controller,
                    templateUrl: tools.store.viewType.SOP.path,
                    locals: { param: {ticket: null} },
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false,
                    multiple: true
                })
            };

        	$scope.openUserSetting = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.user_setting.controller,
                    templateUrl: tools.store.viewType.user_setting.path,
                    locals: {param: {}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openAdmin = () => {
                $mdDialog.show({
                    controller: tools.store.viewType.administrator.controller,
                    templateUrl: tools.store.viewType.administrator.path,
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openDateSetting = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.date_setting.controller,
                    templateUrl: tools.store.viewType.date_setting.path,
                    locals: { param: { ticket: 'ALL' } },
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openProFile = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.pro_file_list.controller,
                    templateUrl: tools.store.viewType.pro_file_list.path,
                    locals: {param: {}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openNode = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.node_list.controller,
                    templateUrl: tools.store.viewType.node_list.path,
                    locals: {param: {}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openLink = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.link_list.controller,
                    templateUrl: tools.store.viewType.link_list.path,
                    locals: {param: {}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openAgency = (event) => {
                $mdDialog.show({
                    controller: tools.store.viewType.agency_list.controller,
                    templateUrl: tools.store.viewType.agency_list.path,
                    locals: {param: {}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false // Only for -xs, -sm breakpoints.
                })
                    .then(function (result) {
                    }, function () {
                    });
            }

            $scope.openAiMonitor = (event) => {
                $scope.statego(tools.store.viewType.aiMonitor.type);
            };

            $scope.openServerMonitor = (event) => {
                $scope.statego(tools.store.viewType.serverMonitor.type);
            };

            $scope.openDashboard = (event) => {
                $scope.statego(tools.store.viewType.dashboard.type);
            };

            $scope.openSelfProcessMonitor = (event) => {
                $scope.statego(tools.store.viewType.selfProcess.type);
            };

            $scope.logout = () => {
                tools.logout();
            };

        	$scope.restTest = () => {
        	    // tools.restfulRequest("POST", "127.0.0.1", "3000", "calculator/test/10/20", "", res => {
                //     debugger;
                // });
            }
        	$scope.postTest = () => {
        	    // tools.postRequest("http://127.0.0.1:3000/calculator/test/10/20", "", res => {
        	    //     debugger;
                // });
            }

            angular.element(document).ready(function () {
                // $timeout(()=>{
                //     if(tools.store.socketEventBus){
                //         try {
                //             tools.store.socketEventBus.publish(Constants.SocketEventBusAddress.ADDR_IN_SESSION, {});
                //         } catch (e) {
                //            console.error(e);
                //         }
                //     }
                // },1000);
            });

            (function updateTime() {
                var tmpDate = new Date();
                var y = tmpDate.getFullYear();
                var m = (tmpDate.getMonth() + 1);
                var d = tmpDate.getDate();

                var days = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
                var day = days[tmpDate.getDay()];
                var apm = tmpDate.getHours() < 12 ? "AM" : "PM";
                var hh = tmpDate.getHours() % 12 == 0 ? "12" : tmpDate.getHours() % 12;
                var mm = tmpDate.getMinutes() < 10 ? "0" + tmpDate.getMinutes() : tmpDate.getMinutes();
                var ss = tmpDate.getSeconds() < 10 ? "0" + tmpDate.getSeconds() : tmpDate.getSeconds();

                $(".topTime").text(y + ". " + m + ". " + d + ". " + day + " " + apm + " " + hh + ":" + mm + ":" + ss);
                setTimeout(updateTime, 1000);
            })();

            $scope.simpleUiToggle = () => {
                if(!tools.isDebug()) { return }

                if($('link[href*="simpleUi.css"]').length > 0) {
                    $('link[href*="simpleUi.css"]').remove();
                } else {
                    $('head').append('<link rel="stylesheet" href="css/simpleUi.css">')
                }


            }
        }
    }
};

export default ['$css', '$state', '$mdDialog', 'tools', 'storeService', '$timeout', menus]
