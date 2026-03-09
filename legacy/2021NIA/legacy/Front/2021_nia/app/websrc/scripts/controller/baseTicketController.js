import BaseController from 'scripts/controller/baseController'

class BaseTicketController extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdToast, $q) {
        super($injector, $scope, tools, $http, $timeout);

        const TEMP_TYPE = this.TEMP_TYPE = $scope.TEMP_TYPE = { ALARM: 'ALARM', LOC: 'LOC' };
        const TEMP_STATE = this.TEMP_STATE = $scope.TEMP_STATE = { OPEN: 'OPEN', CLOSE: 'CLOSE' };

        $scope.ticketType = {};
        $scope._idProp = '';

        $scope.filteredTickets = {};        /** 필터링 된 티켓 */
        $scope.selectedTicketId = 0;        /** 상세보기(토폴로지) 활성화 티켓ID */
        $scope.selectedTickets = [];        /** 선택된 티켓 */

        $scope.isFilterMenuOpened = false;  /** 필터메뉴 open(확장) 여부 */

        /** 필터객체 & 필터 컨트롤러 */
        $timeout(() => {
            tools.store.ticketSearchDate = angular.copy(tools.store.filterStorage.filter.date);
        }, tools.store.filterStorage.filter ? 0 : 200)

        $scope.filterCtrls = {
            // Example
            // state: new FilterController('ticketFilter1.state', $scope),
            // type: new FilterController('ticketFilter1.type', $scope),
        };

        var animating = false;
        var showChangeDelay = 0;
        var prevent = false;

        $scope.ticketCtrl= {};
        $scope.setPrevent = this.setPrevent;

        /** 티켓 실시간 모니터링 일시정지 */
        $scope.ticketRealTimeToggle = () => {
            tools.store.isTicketRealTimePause = !tools.store.isTicketRealTimePause;

            // 일시정지 => 시작
            if (!tools.store.isTicketRealTimePause) {
                tools.reloadTickets();
            }
        }

        /** 텔플릿 컨트롤러 세팅 */
        function resetTemplateCtrl(tickets = tools.store.tickets) {
            Object.assign($scope.ticketCtrl, tickets.filter(ticket => !$scope.ticketCtrl[ticket[$scope._idProp]])
                .reduce((obj, ticket) => (obj[ticket[$scope._idProp]] = this.getDefaultTicketCtrl(ticket), obj), {})
            );
        };

        /** 티켓 이벤트 */
        $scope.ticketClick = function (ticket) {
            if (!prevent) {
                sysAzOpen(ticket);
                this.setPrevent(false);
            }
        };
        function sectionOpen(ticket, tempType) {
            var id = ticket[$scope._idProp];
            var domList = document.getElementById('section-' + id).classList;

            if($scope.ticketCtrl[id].tempState == $scope.TEMP_STATE.OPEN) {
                if($scope.ticketCtrl[id].tempType != tempType) {
                    reopenDetail(id);
                }
                return;
            }

            if (animating) {
                return;
            }
            animating = true;

            ticket.ticket_el_size = tools.constants.TicketEleStatus.OPEN.size;
            domList.add('flip-step-delay', 'flip-step1', 'active');
            $timeout(() => {
                domList.remove('flip-step-delay');
            }, 400);
            animating = false;
        };
        function reopenDetail(id) {
            if (animating) {
                return;
            }
            animating = true;
            showChangeDelay = 500;
            var domList = document.getElementById('section-' + id).classList;
            domList.add('flip-step-delay');
            domList.remove('flip-step1', 'active');
            $timeout(() => {
                domList.add('flip-step1', 'active');
                showChangeDelay = 0;
                $timeout(() => {
                    domList.remove('flip-step-delay');
                }, 400);
            }, 500);
            animating = false;
        };
        function sysAzOpen(ticket) {
            var id = ticket[$scope._idProp];
            if($scope.ticketCtrl[id].tempState == $scope.TEMP_STATE.OPEN
                && $scope.ticketCtrl[id].tempType == $scope.TEMP_TYPE.ALARM) {
                return;
            }

            /*tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_MBA_CABLE_LIST",
                id: id
            }, function (result) {
                $scope.ticketCtrl[id].azList = result;

                tools.store.niaList.forEach(function (v) {
                    if (v.ticket_id == id){
                        v.azList = result;
                    }
                });

            });*/

            sectionOpen(ticket, $scope.TEMP_TYPE.ALARM);

            $timeout(() => {
                $scope.ticketCtrl[id].tempType = TEMP_TYPE.ALARM;
                $scope.ticketCtrl[id].tempState = TEMP_STATE.OPEN;
            });
        };

        $scope.closeDetail = function (ticket) {
            var id = ticket[$scope._idProp];
            var domList = document.getElementById('section-' + id).classList;
            $timeout(() => {
                $scope.ticketCtrl[id].azList = [];
            }, 300);
            if (animating) return;
            animating = true;

            ticket.ticket_el_size = tools.constants.TicketEleStatus.NORMAL.size;
            domList.add('flip-step-delay');
            $timeout(() => {
                domList.remove('flip-step-delay');
                $scope.ticketCtrl[id].tempState = TEMP_STATE.CLOSE;
            }, 400);
            domList.remove('flip-step1', 'active');
            animating = false;
        };

        $scope.showMap3d = function (ticket, event) {
            if (event) {
                event.stopPropagation();
                this.setPrevent(true);
            }

            tools.store.niaSelectedTicket = ticket;
            tools.store.topologyTicket = ticket;

            $scope.$rootBroadcast('BroadcastOnChangedTicket', tools.store.topologyTicket);

            this.setPrevent(false, 200);
        };



        $scope.init = (ticketType) => {

            $scope.ticketType = ticketType;
            $scope.tickets = tools.store.tickets;
            $scope._idProp = ticketType.key;

            (resetTemplateCtrl).bind(this)($scope.tickets);

            $scope.$watch(ticketType.data, onTicketChange.bind(this));
            $scope.$watchCollection(ticketType.data, onTicketChange.bind(this));
        }

        function onTicketChange(n, o) {
            if (n === o) {
                return;
            }
            (resetTemplateCtrl).bind(this)(n);
        }
    }

    getDefaultTicketCtrl(ticket) {
        let hasTcList = !!ticket.tc_list;
        return {
            tempType: hasTcList ? this.TEMP_TYPE.LOC : this.TEMP_TYPE.ALARM,        /** 템플릿 타입 - 알람 : 'ALARM' / 케이블 : 'LOC' */
            tempState: hasTcList ? this.TEMP_STATE.OPEN : this.TEMP_STATE.CLOSE,    /** 템플릿 상태 - OPEN / CLOSE */
            azList: []                                                               /** sys A-Z 알람 리스트 */
        }
    }

    setPrevent(value, ms = 0) {
        let {$timeout} = this;
        if(value == this.prevent) { return; }

        $timeout(() => {
            this.prevent = false;
        }, ms);
    }

}

export default BaseTicketController;
