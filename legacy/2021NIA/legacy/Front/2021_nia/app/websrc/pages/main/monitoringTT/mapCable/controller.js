import BaseController from 'scripts/controller/baseController'

class MapCableCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdPanel) {
        $scope.config = tools.store.viewType.mapCable;
        super($injector, $scope, tools, $http, $timeout, $mdPanel)
        {
            let controller = this;
            $scope.ticket = null;
            $scope.ocaseq = null;
            $scope.ocaname = null;
            $scope.inspector_seq = null;
            $scope.model = {
                transcircuit_list: [],
                backbone_count: 0,
            }

            $scope.not_backbone_count = function () {
                let {model} = this;
                return Math.max(0, model.transcircuit_list.length - model.backbone_count);
            }

            $scope.$watch(() => $scope.ocaseq_input, (n, o) => {
                if (n === o)
                    return;

                if (!n || n.length <= 0)
                    return;

                let {$scope} = this;
                $scope.ticket = {ticket_id: null};
                $scope.ocaseq = n;
                $scope.ocaname = '';
                $scope.inspector_seq = null;
                this.load();
            });


            // <editor-fold desc="[#상위 스코프에서 보내준 이벤트 처리]">

            $scope.$on('BroadcastOnChangedTranscircuit', function listenStatus(event, param) {
                let {$scope} = this;
                let transcircuit = param.transcircuit || {};
                $scope.ticket = param.ticket || {ticket_id: null};
                $scope.ocaseq = param.OCASEQ || transcircuit.ocaseq;
                $scope.ocaname = param.OCANAME || transcircuit.ocaname;
                $scope.inspector_seq = param.INSPECTOR_SEQ;
                this.load();
            }.bind(this));

            $scope.$on('broadcast.menuEvent', function(event, param) {

                if (!param) return;

                let {control} = $scope;

                switch (param) {
                    case 'loadTopology':control.load();break;
                    case 'theme':control.toggleTheme();break;
                    case 'screenshot':control.screenshot();break;
                }
            }.bind(this));

            // $scope.$watch(() => $scope.menuOptions.keyword, (n, o) => {
            //     if (n === o)
            //         return;
            // });
            // <!--</editor-fold desc="[#상위 스코프에서 보내준 이벤트 처리]">

            $scope.getSystemNameA = function (transcircuit) {
                let sysnamea = transcircuit.sysnamea || transcircuit.nescodea;
                let sysnamez = transcircuit.sysnamez || transcircuit.nescodez;
                return (sysnamea == null && sysnamez == null) ? transcircuit.tcsysnamea : sysnamea;
            };

            $scope.getSystemNameZ = function (transcircuit) {
                let sysnamea = transcircuit.sysnamea || transcircuit.nescodea;
                let sysnamez = transcircuit.sysnamez || transcircuit.nescodez;
                return (sysnamea == null && sysnamez == null) ? transcircuit.tcsysnamez : sysnamez;
            };

            $scope.backboneFilter = function (transcircuit) {

                let show = true;

                if (transcircuit.description) {
                    return $scope.menuOptions.showBackbone;
                } else {
                    return $scope.menuOptions.showNotBackbone;
                }
            };

            $scope.covertAlarmLevelToColor = (alarmlevel, returnType = 'array') => {

                var mapAlarm = {
                    "1": [1, 'clear', '#78BE22', 0x78BE22],
                    "3": [3, 'warning', '#63B1FF', 0x63B1FF],
                    "4": [4, 'minor', '#EF6060', 0xEF6060],
                    "5": [5, 'major', '#EF6060', 0xEF6060],
                    "7": [7, 'critical', '#EF6060', 0xEF6060],
                };

                var alarm = mapAlarm[alarmlevel];

                if (!alarm)
                    alarm = mapAlarm["1"];

                switch (returnType) {
                    case "array" :
                        return alarm;
                    case "int" :
                        return alarm[0];
                    case "class" :
                        return alarm[1];
                    case "color" :
                        return alarm[2];
                    case "color_hex" :
                        return alarm[3];
                }
            };

            angular.element(document).ready(function () {
                controller.initAlarmList();
            }.bind(this));
        }
    }

    initAlarmList() {

        let {$scope, $q} = this;

        $scope.selected = [];
        $scope.limitOptions = [500, 1000, 10000];

        $scope.options = {
            showOption: false,
            rowSelection: false,
            multiSelect: false,
            autoSelect: false,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };

        $scope.query = {
            order: '-alarmtime', // or ['-alarmtime','-receivetime'],
            limit: 500,
            page: 1
        };

        $scope.ticketAlarms = {};

        $scope.rootClass = '.mapCable' + ' ';
        $($scope.rootClass + 'table.alarm-table').on('scroll', function () {
            $($scope.rootClass + "table.alarm-table > *").width($($scope.rootClass + "table.alarm-table").width() + $($scope.rootClass + "table.alarm-table").scrollLeft());
        });

        let rootElement = angular.element($scope.rootClass);
        $scope.$watch(function() { return rootElement.is(':visible') }, function(newValue, oldValue, $scope) {
            if (newValue === oldValue) return;

            if (newValue) {
                $scope.control.resize();
            }

        });

        $(window).resize(this.resize.bind(this));
        this.resize();
        this.endWaitting();

    }

    toggleTheme() {
        let element = document.querySelector(".mapCable");
        element.classList.toggle('theme');

        this.beginWaitting(3000);
        setTimeout( function () {
            this.endWaitting();
        }.bind(this), 1000);
    }

    screenshot() {
        html2canvas(document.querySelector('.wrap-container .contents'), {
            backgroundColor: "#000000"   /*배경 색 선택. 기본값 : 흰색, 투명으로 하려면(transparent)*/
        }).then(function (canvas) {

            let {$scope} = this;
            if ($scope.debug) {
                // <editor-fold desc="[#이미지 새창에서 보기]">
                var base64image = canvas.toDataURL("image/png");
                window.open(base64image, "_blank").document.write('<img src="' + base64image + '" />');
                // <!--</editor-fold desc="[#이미지 새창에서 보기]">
            } else {
                // <editor-fold desc="[#이미지 다운로드]">
                let aTag = document.createElement('a');
                aTag.href = canvas.toDataURL("image/jpeg").replace("image/jpeg", "image/octet-stream");
                aTag.download = `케이블_${$scope.ocaname}.png`;
                aTag.click();
                // <!--</editor-fold desc="[#이미지 다운로드]">
            }

            // <editor-fold desc="[#body에 이미지 붙이기]">
            // document.body.appendChild(canvas);
            // <!--</editor-fold desc="[#body에 이미지 붙이기]">

        }.bind(this));
    }

    load()
    {
        setTimeout( function () {

            if (this.isVisible() == false)
                return;

            let {$scope} = this;
            let {$q} = $scope;

            this.beginWaitting(1000);
            let q1 = this.loadAlarmList();
            let q2 = this.loadTranscircuitList();
            let q3 = this.loadBackboneCount();

            $q.all([q1, q2, q3]).finally(this.endWaitting.bind(this));

        }.bind(this), 100);

    }

    loadTranscircuitList()
    {
        let {$scope} = this;
        let {tools, ticket, ocaseq, inspector_seq} = $scope;
        let {model, menuOptions, $q} = $scope;
        var defered = $scope.$q.defer();

        model.transcircuit_list.clearAll();
        model.backbone_count = 0;

        let fn_success = (result => {

            let {$scope} = this;
            let {model} = $scope;

            model.transcircuit_list.copy(result);
            console.log(model.transcircuit_list);
            defered.resolve();
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: ticket.ticket_id ? `SELECT_CABLE_TRANSCIRCUIT_LIST` : `SELECT_CABLE_TRANSCIRCUIT2_LIST`,
            TICKET_ID: ticket.ticket_id,
            OCASEQ: ocaseq,
            INSPECTOR_SEQ: inspector_seq,
        }, fn_success.bind(this));

        return defered.promise;
    }

    loadAlarmList()
    {
        let {$scope, tools} = this;
        let {$log, ticketAlarms, ticket, ocaseq, inspector_seq, $q} = $scope;
        ticketAlarms.data = [];
        ticketAlarms.count = 0;
        var defered = $q.defer();

        if (tools.store.getMode("op_map3d_no_alarm") == true) {
            return defered.reject();
        }

        let fn_success = ((result) => {
            ticketAlarms.data = result;
            ticketAlarms.count = result.length;
            defered.resolve();
        });

        // if (!(ticket.ticket_id && ticket.cluster_no)) {
        //     $log.debug("티켓정보를 알 수 없어 알람을 조회할 수 없습니다.", ticket);
        //     defered.reject();
        // }

        // console.error('TICKET_ID='+ ticket.ticket_id, 'OCASEQ='+ transcircuit.ocaseq);
        tools.http({
            service: tools.constants.Service.rca,
            action: ticket.ticket_id ? `SELECT_TICKET_ALARM_LIST` : `SELECT_TICKET_ALARM2_LIST`,
            TICKET_ID: ticket.ticket_id,
            OCASEQ: ocaseq,
            INSPECTOR_SEQ: inspector_seq,
            TICKET_TIME: tools.store.formatDateTime(ticket.ticket_generation_time)
        }, fn_success.bind(this));


        return defered.promise;
    }

    loadBackboneCount()
    {
        let {$scope} = this;
        let {tools, ticket, ocaseq, $q} = $scope;
        var defered = $q.defer();

        let fn_success = (result => {

            let {$scope} = this;
            let {model} = $scope;
            try {
                model.backbone_count = result.count;
            } catch (e) {
                model.backbone_count = 0;
            }

            defered.resolve();
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: ticket.ticket_id ? `SELECT_CABLE_BACKBONE_COUNT` : `SELECT_CABLE_BACKBONE2_COUNT`,
            OCASEQ: ocaseq
        }, fn_success.bind(this));

        return defered.promise;
    }

    isVisible() {
        // let {$scope} = this;
        // let rootElement = angular.element($scope.rootClass);
        // return rootElement.is(':visible');

        let {$scope} = this;
        let {tools} = $scope;
        return $scope.isVisibleView(tools.constants.MapType.CABLE);
    }

    resize(delay = 300) {

        let {$timeout, $scope} = this;
        if (this.isVisible() == false)
            return;

        let old = $scope.window_wh || {};
        let wh = {w: window.innerWidth, h: window.innerHeight};
        if (old.w == wh.w && old.h == wh.h) {
            return;
        }
        $scope.window_wh = wh;

        $timeout(() => {
            $($scope.rootClass + "table.alarm-table > *").width($($scope.rootClass + "table.alarm-table").width() + $($scope.rootClass + "table.alarm-table").scrollLeft());
        }, delay);

    }

    beginWaitting(minMilliseconds) {
        let {$scope} = this;

        setTimeout(function () {
            $scope.isProgressHide = false;
            $scope.isProgressHidePrevent = true;
            $scope.isProgressHideTry = false;
            $scope.$apply();

            setTimeout(function () {
                $scope.isProgressHidePrevent = false;
                if ($scope.isProgressHideTry) {
                    $scope.isProgressHideTry = false;
                    $scope.control.endWaitting();
                }
            }, minMilliseconds);
        });
    }

    endWaitting() {
        let {$scope} = this;

        setTimeout(function () {

            if ($scope.isProgressHidePrevent) {
                $scope.isProgressHideTry = true;
                return;
            }

            $scope.isProgressHide = true;
            $scope.$apply();
        });
    }

}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', MapCableCtrl];
