import BaseTicketController from 'scripts/controller/baseTicketController'
import FilterController from 'scripts/services/filterController'

class RcaTicketCtrl extends BaseTicketController {
    constructor($injector, $scope, $window, tools, $http, $timeout, $mdDialog, $mdToast, $q, $mdSelect) {
        $scope.config = tools.store.viewType.mon_tt_ticket;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $mdToast, $q);

        $scope.selectedText ="전체";

        $scope.ticket_type_items = [
            { value: 'ALL', text: '전체' },
            { value: 'RT', text: '장애' },
            { value: 'FTT', text: '비장애' },
            { value: 'PF', text: '광레벨' },
            { value: 'ATT2', text: '이상 트래픽' },
            { value: 'NTT', text: '유해 트래픽' },
            { value: 'NFTT', text: '장비부하장애' },
            // { value: 'TRAFFIC', text: '트래픽(TRAFFIC)' }
        ];

        $scope.ticket_status_items = [
            { value: 'ALL', text: '전체' },
            { value: 'INIT', text: '발생' },
            { value: 'ACK', text: '인지' },
            { value: 'FIN', text: '마감' },
            { value: 'AUTO_FIN', text: '자동마감' }
        ];

        $scope.scrollIndex = 0;
        $scope.scrollTimer = null;

        $scope.ticketLength = tools.store.niaList.length || 0;

        $scope.onInit = () => {
            $scope.statusFilter =  new FilterController("ticketFilter1.state", $scope);

            $scope.devFilter = { value: 'ALL', text: '전체' };
            $scope.statusFilter = { value: 'ALL', text: '전체' };
            // tools.constants.Service.rca
        };
        
        $scope.soundTest= () => {
            if(tools.store.sound == true){
                let audio = document.getElementById("선로장애");
                if (audio) {
                    audio.play();
                } else { return ; }
            }
        };

        $scope.clickMdList = (item, ticket) => {
            console.log(item);

            if(!tools.store.niaSelectedTicket || tools.store.niaSelectedTicket.ticket_id != ticket.ticket_id) {
                $scope.showMap3d(ticket);
                $timeout(() => {
                    $scope.$rootBroadcast('broadcastClickCableList', item);
                }, 500);
                return;
            }
            $scope.$rootBroadcast('broadcastClickCableList', item);
        };

        /** 티켓 처리, 상태변경 */
        $scope.ticketProcessing = (ticket, event, work) => {
            let processing = tools.constants.TicketProcessing[work] || {};
            let page = tools.store.viewType[processing.view];

            if(!ticket || !page) {
                console.error('파라미터 입력값 오류');
                return;
            }

            $mdDialog.show({
                locals: {
                    param: { ticket: (Array.isArray(ticket) ? ticket : [ticket]), processing: processing }
                },
                controller: page.controller,
                templateUrl: page.path,
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                bindToController: true,
                autoWrap: false,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                onRemoving: function () {
                    $mdSelect.hide();
                }
            }).then(function (result) {
                $scope.changeTicketStatus(
                    tools.store.niaList.find(v => v.ticket_id == result.ticket_id),
                    result.status
                );
            }, function () {
            });
        };

        $scope.showSopList = (ticket) => {
            $mdDialog.show({
                controller: tools.store.viewType.SOP.controller,
                templateUrl: tools.store.viewType.SOP.path,
                locals: { param: {ticket: ticket} },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };
        
        $scope.showAIProcess = (ticket) => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_SELF_PROCESS_DETAIL_PAGE2_LIST",
                TICKET_ID: ticket.ticket_id != '' ? ticket.ticket_id : null,
            }, function (result) {
                if (!result) return true;
                $scope.processInfo = result.list[0];
                if (ticket.status === 'INIT') {
                    if(ticket.ticket_type == 'NTT' || ticket.ticket_type == 'ATT2'){
                        if ($scope.processInfo.clos_type === '자동') {
                            let htmlContent = {first : `장애대응 화면으로 전환 하면`,second:`티켓번호가 NIA #.${ticket.ticket_id} 의 티켓은`,third:`수동 마감으로 전환됩니다.`,fourth:`전환 하시겠습니까?`}
                            tools.createTicketConfirmDlg("장애대응", null, null, htmlContent)
                                .then(function (confirmed) {
                                    if (confirmed) {
                                        tools.http({
                                            service: tools.constants.Service.rca,
                                            action: "UPDATE_SELF_PROCESS_CLOS_TYPE",
                                            TICKET_ID: ticket.ticket_id != '' ? ticket.ticket_id : null,
                                        }, function (result) {
                                            if (!result) return true;
                                            $scope.openAIProcess(ticket);
                                        })
                                    }
                                });
                        } else if ($scope.processInfo.clos_type === '수동') {
                            $scope.openAIProcess(ticket);
                        }
                    }else{
                        $scope.openAIProcess(ticket);
                    }
                } else if (ticket.status === 'FIN' || ticket.status === 'AUTO_FIN') {
                    tools.showAlert('안내', '이미 마감된 티켓입니다.')
                        .then(function (result) {
                            if (result) {
                                $scope.openAIProcess(ticket);
                            }
                        });
                }
            });
        };
        $scope.openAIProcess = (ticket) => {
            $mdDialog.show({
                controller: tools.store.viewType.ai_process_template.controller,
                templateUrl: tools.store.viewType.ai_process_template.path,
                locals: { param: ticket },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false,
                multiple: true
            })
        };

        /** View 이벤트 */
        $scope.onViewEvent = (event, param) => {
            switch (param.type) {
                /** 티켓 로드 완료시 */
                case tools.constants.ViewEventType.COMPLETE_LOAD_TICKET: {
                    break;
                }
                case tools.constants.ViewEventType.CHANGED_NIA_TICKET:
                    $scope.safeApply();
                    break;
            }
        };

        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_TICKET_LIST"
        }, (result) => {
            if(!result || result.length <= 0) {
                tools.store.niaList = [
                  /*  {ticket_id: '10', ticket_type: 'RT', status: 'INIT'},
                    {ticket_id: '11', ticket_type: 'PF', status: 'INIT'}*/
                ];
            } else {
               /* $timeout(() => {
                    tools.store.niaList = [];
                    result.forEach((v, i) => {
                        $timeout(() => {
                            tools.store.niaList.push(v)
                        }, i * 300)
                    })
                    tools.showToast('장애가 발생했습니다.');
                }, 10000)*/
                tools.store.niaList = result;
                $scope.ticketLength = tools.store.niaList.length || 0
            }

        });

        $scope.getTicketColorClass = (ticket) => {
            let ticketStatus = tools.constants.TicketStatus;
            let color = 'bg_color_';
            if (ticket.ticket_type == 'PF') {
                color += 'yellow';
            } else if (ticket.ticket_type == 'SUB_ALM' || !ticketStatus[ticket.status]) {
                color += ticketStatus.UNKNOWN.color;
            } else if(ticketStatus[ticket.status]) {
                color += ticketStatus[ticket.status].color;
            }
            return color;
        }

        $scope.getTicketBorderColorClass = (ticket) => {
            let ticketStatus = tools.constants.TicketStatus;
            let color = 'border_color_';
            if (ticket.ticket_type == 'PF') {
                color += 'yellow';
            } else if (ticket.ticket_type == 'SUB_ALM' || !ticketStatus[ticket.status]) {
                color += ticketStatus.UNKNOWN.color;
            } else if(ticketStatus[ticket.status]) {
                color += ticketStatus[ticket.status].color;
            }
            return color;
        }

        $scope.getTicketStatusColor = (ticket) => {
            let color = '';
            if( (ticket.ticket_type =="RT" && ticket.status == "ACK") || ticket.status == "FIN" || ticket.status == "AUTO_FIN"){
                color = "green";
            } else if((ticket.ticket_type =="PF" && ticket.status == "ACK")){
                color = '#bdbd00';
            } else if(ticket.status == "FAIL"){
                color = "red";
            } else { color = "grey"; }
            return color;
        };

        $scope.getTicketTypeLabel = (ticket) => {
            if(ticket.root_cause_type) {
                return tools.constants.TicketType[ticket.root_cause_type].text + '장애';
            } else {
                return '장애';
            }
        };

        $scope.getTicketStatusText = (ticket) => {
            // if(ticket.ticket_type == "RT"){
            //     return tools.constants.TicketStatus[ticket.status].text;
            // } else if(ticket.ticket_type == "PF"){
            //     if(ticket.status == "ACK"){
            //         return "요청중";
            //     } else { 
            //         return tools.constants.TicketStatus[ticket.status].text; 
            //     }
            // }
            if(ticket.status == 'AUTO_FIN'){
                return tools.constants.TicketStatus[ticket.status].text;
            }
            return;
        }

        $scope.changeTicketStatus = (ticket, status) => {
            if(!status) {
                return;
            }
            ticket.status = status;
            $scope.safeApply();
        };

        $scope.resetTicket = () => {
            /* reset  엔진쪽으로 보내기 */
            tools.http({
                service: tools.constants.Service.rca,
                action: tools.constants.Action.RESET_TICKET,
                EVENT_TYPE: "REQUEST_RESTART_DEMO"
            }, function (result) {
                // tools.simulateTicket();
                tools.resetTopologyData();
            });
            setTimeout(() => {
                $('#ticket_0').dblclick();
            },2500);

        };


        $scope.openDevContextMenu = (event) => {
            if (!tools.debug) { return }

            $(`button#devContextMenuTriggerBtn`).css({
                top: event.clientY,
                left: event.clientX
            });
            $(`button#devContextMenuTriggerBtn`).trigger('click');
        };

        $scope.devTypeFilter = (val) => {
            if($scope.devFilter && $scope.devFilter.value == 'ALL') {
                return true;
            }

            // ATT, ATT2 둘 다 필터링 하기 위해서
            // return val.ticket_type.startsWith($scope.devFilter.value)
            
            return val.ticket_type == $scope.devFilter.value;
        };

        $scope.devStatusFilter = (val) => {
            if($scope.devFilter && $scope.statusFilter.value == 'ALL') {
                return true;
            }
            return val.status == $scope.statusFilter.value;
        };

        $scope.onChangeDev = (val) => {
            $scope.devFilter = val;
            $scope.calculateTicketLength();
        };

        $scope.onChangeStatus = (val) => {
            $scope.statusFilter = val;
            $scope.calculateTicketLength();
        };

        $scope.calculateTicketLength = () => {
            $scope.ticketLength = tools.store.niaList.filter((data)=> {
                return ( $scope.devFilter.value == 'ALL' ? true : data.ticket_type == $scope.devFilter.value ) &&
                    ( tools.store.ticketAllSearch.length > 0 ? JSON.stringify(data).toLocaleLowerCase().includes(tools.store.ticketAllSearch.toLocaleLowerCase()) : true ) &&
                    ( $scope.statusFilter.value == 'ALL' ? true : ($scope.statusFilter.value == data.status) );
            }).length;
        };

        $scope.isShow = (index) => {
            return $scope.scrollIndex < index + 20 && $scope.scrollIndex > index - 20;
        };

        $scope.$watch('tools.store.ticketAllSearch', function (n, o) {
            if(n == o) {
                return ;
            } else if(!n) {
                setTimeout(() => {
                    $("div.ticket-container").scrollTop(5);
                }, 500);
            }
            $scope.calculateTicketLength();
        });

        tools.http({
            service: tools.constants.Service.rca,
            action: "REQUEST_LOGIN_NIA"
        }, function (result) {
            const nia_login_result = JSON.parse(result.result);
            const tokenArray = Object.entries(nia_login_result.data);
            tokenArray.forEach(function (item, index) {
                tools.store.niaToken = (tools.store.niaToken || '') + `${item[0]}=${item[1]}`;
                if(index != tokenArray.length - 1) {
                    tools.store.niaToken = tools.store.niaToken + '; ';
                }
            });
            console.log("NIA API LOGIN RESULT: ", nia_login_result);
            console.log("NIA API LOGIN TOKEN: ", tools.store.niaToken);
        });

        angular.element(document).ready(() => {
            $("div.ticket-container").scroll(function() {
                clearTimeout($scope.scrollTimer);
                $scope.scrollTimer=null;
                $scope.scrollTimer = setTimeout(function() {
                    $scope.scrollIndex = $("div.ticket-container").scrollTop() / $('.ticket-area').height();
                    clearTimeout($scope.scrollTimer);
                    $scope.scrollTimer=null;
                    $scope.safeApply();
                }, 250);
            });
        });
    }
}

export default ['$injector', '$scope', '$window', 'tools', '$http', '$timeout', '$mdDialog', '$mdToast', '$q', '$mdSelect',  RcaTicketCtrl];
