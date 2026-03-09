import BaseTicketController from 'scripts/controller/baseTicketController'
import FilterController from 'scripts/services/filterController'

class SyslogCtrl extends BaseTicketController {
    constructor($injector, $scope, $window, tools, $http, $timeout, $mdDialog, $mdToast, $q, $mdSelect) {
        $scope.config = tools.store.viewType.mon_tt_syslog;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $mdToast, $q);

        $scope.syslogMsg = [
            { value: 'ALL', text: '전체' },
            { value: 'IF_DOWN', text: 'IF_DOWN' }
        ];

        const syslogLevel = {
            "1": { level: 1, text: 'clear', color: '#78BE22' },
            "3": { level: 3, text: 'warning', color: '#63B1FF' },
            "4": { level: 4, text: 'minor', color: '#EF6060' },
            "5": { level: 5, text: 'major', color: '#EF6060' },
            "7": { level: 7, text: 'critical', color: '#EF6060' },
        };
        
        $scope.getSyslogLevels = () => {
            return Object.keys(syslogLevel).map(d => {
                return { value: d.level, text: d.text }
            }).unshift({ value: 'ALL', text: '전체' })
        }

        $scope.syslogLength = tools.store.niaSyslogList.length || 0;

        $scope.onInit = () => {
            $scope.statusFilter =  new FilterController("ticketFilter1.state", $scope);

            $scope.devFilter = { value: 'ALL', text: '전체' };
            $scope.statusFilter = { value: 'ALL', text: '전체' };
            // tools.constants.Service.rca

            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_SYSLOG_LIST"
            }, (result) => {
                if(!result || result.length <= 0) {
                    tools.store.niaSyslogList = [];
                } else {
                    tools.store.niaSyslogList = result;
                    $scope.syslogLength = tools.store.niaSyslogList.length || 0
                }
            });
        };
        
        $scope.soundTest= () => {
            if(tools.store.sound == true){
                let audio = document.getElementById("선로장애");
                if (audio) {
                    audio.play();
                } else { return ; }
            }
        };
        
        $scope.openSyslogDetail = (row = {}) => {
            const data = Object.assign(row)
            $mdDialog.show({
                controller: tools.store.viewType.syslog_detail.controller,
                templateUrl: tools.store.viewType.syslog_detail.path,
                locals: {param: {data}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        $scope.showSyslogTemplate = (alarm) => {
            if (alarm.status == '마감' || alarm.status == '자동 마감') {
                $tools.showAlert('안내', '이미 마감된 알람입니다.').then(function (result) {
                    if (result) {
                        $scope.openSyslogTemplate(alarm);
                    }
                });
            }else{
                $scope.openSyslogTemplate(alarm);
            }
        };
        $scope.openSyslogTemplate = (alarm) => {
            $mdDialog.show({
                controller: tools.store.viewType.syslog_configuration_template.controller,
                templateUrl: tools.store.viewType.syslog_configuration_template.path,
                locals: { param: alarm },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            })
        };

        // 등급컬러를 가져올때 사용하면 됨.
        $scope.getSyslogLevelColor = (syslog) => {
            let color = '';
            try {
                return syslogLevel[syslog.alarmlvl].color
            } catch (error) {
                return '#78BE22'
            }
        };

        $scope.openDevContextMenu = (event) => {
            if (!tools.debug) { return }

            $(`button#devContextMenuTriggerBtn`).css({
                top: event.clientY,
                left: event.clientX
            });
            $(`button#devContextMenuTriggerBtn`).trigger('click');
        };

        $scope.onChangeDev = (val) => {
            $scope.devFilter = val;
            $scope.calculateSyslogLength();
        };

        $scope.onChangeStatus = (val) => {
            $scope.statusFilter = val;
            $scope.calculateSyslogLength();
        };

        $scope.calculateSyslogLength = () => {
            $scope.syslogLength = tools.store.niaSyslogList.filter((data) => true).length;
        };

        // todo: 이름변경 ticketAllSearch ->  searchKeyword
        $scope.$watch('tools.store.ticketAllSearch', function (n, o) {
            if(n == o) {
                return;
            } 
            $scope.calculateSyslogLength();
        });
    }
}

export default ['$injector', '$scope', '$window', 'tools', '$http', '$timeout', '$mdDialog', '$mdToast', '$q', '$mdSelect',  SyslogCtrl];
