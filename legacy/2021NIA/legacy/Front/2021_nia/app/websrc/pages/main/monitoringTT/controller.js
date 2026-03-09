import BaseController from 'scripts/controller/baseController'

class MonitoringTTCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSidenav, $mdMedia) {

        $scope.config = tools.store.viewType.monitoring_tt;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSidenav, $mdMedia);

        $scope.onViewEvent = (event, param) => {
            /*
            사용예
            if ($scope.isVisible()) {
                // View 가 활성화 되었을 때 만 처리 한다
            }

            switch (param.type){
                case tools.constants.ViewEventType.SOCKET_OPENED:
                    break;
                case tools.constants.ViewEventType.CHANGED_STATISTICS:
                    break;
            }*/
        };

        $scope.viewType = tools.constants.MapType.LAYERED;

        $scope.isVisibleView = (view) => {
            let isCurrentView = !view ? true : view == $scope.viewType;
            return !tools.store.getMode('op_multiple_page') && isCurrentView;
        }

        $scope.changeView = (view) => {

            if(view && eval(`tools.constants.MapType.${view}`))
            {
                $timeout(() => {
                    $scope.viewType = view;
                })
            }
        }

        $scope.toggleView = () => {
            switch ($scope.viewType) {
                case tools.constants.MapType.LAYERED :
                    $scope.changeView(tools.constants.MapType.CABLE);
                    break;
                case tools.constants.MapType.CABLE :
                    $scope.changeView(tools.constants.MapType.LAYERED);
                    break;
            }
        }

        $scope.moreMenu = [

            {name: '토폴로지 저장', action: 'save', icon: 'fa-save'},
            {name: '토폴로지 로드', action: 'load', icon: 'fa-folder-open'},
            {name: '토폴로지 초기화', action: 'reset', icon: 'fa-retweet'},
        ];

        $scope.menuOptions = {
            rotation: false,
            nodeText: true,
            alarmTag: true,
            nodeBlink: false,
            linkTraffic : false,
            showLayers : [],
            allLayers: [],
            showBackbone: true,
            showNotBackbone: true,
            showAlarm: true
        };

        // <editor-fold desc="[#이벤트 생성 및 전달]">
        $scope.broadcastMenuEvent = function (menuName) {
            $scope.$broadcast('broadcast.menuEvent', menuName);
        }
        // <!--</editor-fold desc="[#이벤트 생성 및 전달]">

        // <editor-fold desc="[#이벤트 처리]">
        $scope.$on('BroadcastOnChangedTicket', function listenStatus(event, param) {
            $scope.changeView(tools.constants.MapType.LAYERED);
        }.bind(this));

        $scope.$on('BroadcastOnChangedTranscircuit', function listenStatus(event, param) {
            $scope.changeView(tools.constants.MapType.CABLE);
            $scope.menuOptions.keyword = "";
            $scope.menuOptions.showBackbone = true;
            $scope.menuOptions.showNotBackbone = true;
            $scope.menuOptions.showAlarm = true;

        }.bind(this));
        // <!--</editor-fold desc="[#이벤트 처리]">

        $scope.applySearch = () => {
            tools.applyDateFilter();
        };

        $scope.isVisibledMedia = true;
        $scope.isSyslogVisibledMedia = false;

        $scope.getMediaOption = () => {
            const isMedia = $scope.isVisibledMedia ? 'gt-sm' : '(min-width: 9999px)';
            return isMedia
        };
        $scope.getSyslogMediaOption = () => {
            const isMedia = $scope.isSyslogVisibledMedia ? 'gt-sm' : '(min-width: 9999px)';
            return isMedia
        };

        $scope.toggleNavHide = function () {
                    if ($mdSidenav('leftRcaTicketNav').isLockedOpen() && $scope.isVisibledMedia == true) {
                        $scope.isVisibledMedia = false;
                    }
                    $mdSidenav('leftRcaTicketNav').close();

                    if ($mdSidenav('leftSyslogNav').isLockedOpen() && $scope.isSyslogVisibledMedia == true) {
                        $scope.isSyslogVisibledMedia = false;
                    }
                    $mdSidenav('leftSyslogNav').close();

            $scope.$broadcast('broadcastSideNavEvent');
        };

        $scope.toggleSideNav = function (action) {
            switch (action) {
                case 'ticketOpen':
                    if (!$mdSidenav('leftRcaTicketNav').isLockedOpen() && $scope.isVisibledMedia == false) {
                        $scope.isVisibledMedia = true;
                        $mdSidenav('leftRcaTicketNav').open();
                    }

                    if ($mdSidenav('leftSyslogNav').isLockedOpen() && $scope.isSyslogVisibledMedia == true) {
                        $scope.isSyslogVisibledMedia = false;
                        $mdSidenav('leftSyslogNav').close();
                    }
                    break;
                case 'syslogOpen':
                    if (!$mdSidenav('leftSyslogNav').isLockedOpen() && $scope.isSyslogVisibledMedia == false) {
                        $scope.isSyslogVisibledMedia = true;
                        $mdSidenav('leftSyslogNav').open();
                    }

                    if ($mdSidenav('leftRcaTicketNav').isLockedOpen() && $scope.isVisibledMedia == true) {
                        $scope.isVisibledMedia = false;
                        $mdSidenav('leftRcaTicketNav').close();
                    }
                    break;
            }
            $scope.$broadcast('broadcastSideNavEvent');
        };
        
        $scope.$watch(() => $mdMedia('gt-sm'), (isOver) => {
            if (!isOver && $scope.isVisibledMedia) {
                $mdSidenav('leftRcaTicketNav').close(); 
                $mdSidenav('leftSyslogNav').open();
            }
            if (isOver && $scope.isSyslogVisibledMedia) {
                $mdSidenav('leftSyslogNav').close(); 
                $mdSidenav('leftRcaTicketNav').open();
            }
        });

    };
    }

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSidenav', '$mdMedia', MonitoringTTCtrl];