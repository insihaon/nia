import BaseController from 'scripts/controller/baseController'

class AIProcessDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, param) {
        $scope.config = tools.store.viewType.ai_process_template;
        super($injector, $scope, tools, $http, $timeout);

        $scope.ticket = param || tools.store.niaList[0] || {};

        $scope.selectedByPass = [];
        $scope.failureInfo = {
            source_name: '',
            target_name: '',
            source_image: {path: ''},
            target_image: {path: ''}
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#aiProcessDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        $scope.onInit = () => {
            if($scope.ticket && $scope.ticket.ticket_type == 'RT') {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_TOPOLOGY_CABLE_LIST",
                    TICKET_ID : $scope.ticket.ticket_id
                }, (result) => {
                    if(result.length > 0) {
                        let temp = result.sort((a, b) => a.routenum - b.routenum).reduce((r, p, i)=>{
                            if(i < result.length - 1) {
                                r.push({source_id: p.sysname, target_id: (result[i + 1] || {}).sysname});
                            }
                            return r
                        }, []);

                        let causeId = '';
                        if($scope.ticket.root_cause_alarm_noa) {
                            causeId = tools.convertNiaNodeId($scope.ticket.root_cause_sysnamea);
                        } else if($scope.ticket.root_cause_alarm_noz) {
                            causeId = tools.convertNiaNodeId($scope.ticket.root_cause_sysnamez);
                        }

                        let causeLinks = temp.map(item => {
                            let link = tools.store.niaData.links.find(v => v.source_id == item.source_id && v.target_id == item.target_id);
                            Object.assign(link, {
                                "status": (item.source_id == causeId || item.target_id == causeId) ? '-1' : '0'
                            });
                            return link;
                        });
                        $scope.failureInfo = causeLinks.find(v => v.status == '-1') || causeLinks[0] || {};
                        $scope.getFailureImage();
                    } else {
                        $scope.setDefaultInfo();
                    }
                });

            } else if($scope.ticket && $scope.ticket.ticket_type == 'PF') {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_PF_TOPOLOGY_CABLE_LIST",
                    TICKET_ID : $scope.ticket.ticket_id
                }, (result) => {
                    if(result) {
                        let temp = result.map(v => ({ source_id: tools.convertNiaNodeId(v.sysnamea),  target_id: tools.convertNiaNodeId(v.sysnamez) }));

                        let causeLinks = temp.map(item => {
                            let link = tools.store.niaData.links.find(v => v.source_id == item.source_id && v.target_id == item.target_id);
                            return link;
                        });
                        $scope.failureInfo = causeLinks[0];
                        $scope.getFailureImage();
                    }
                });
            } else {
                $scope.setDefaultInfo();
            }

            $scope.loadUserList();
        };

        $timeout(()=> {
            if($scope.ticket && $scope.ticket.ticket_type == 'RT'){
                $('md-dialog.aiProcess-dialog md-tab-item')[0].style.display = 'none';
                $('md-dialog.aiProcess-dialog md-tab-item')[1].click();
            } else if($scope.ticket && $scope.ticket.ticket_type != 'RT'){
                $('md-dialog.aiProcess-dialog md-tab-item')[1].style.display = 'none';
            }
        });

        $scope.setDefaultInfo = () => {
            $scope.failureInfo.source_name = $scope.ticket.root_cause_sysnamea || '';
            $scope.failureInfo.target_name = $scope.ticket.root_cause_sysnamez || '';

            $scope.failureInfo.source_port = $scope.ticket.root_cause_porta ? `(${$scope.ticket.root_cause_porta})` : '';
            $scope.failureInfo.target_port = $scope.ticket.is_organ_system ? '' :  ($scope.ticket.root_cause_portz ? `(${$scope.ticket.root_cause_portz})` : '');

            $scope.failureInfo.source_image.path = '../../../../images/node/switch.png';
            $scope.failureInfo.target_image.path = '../../../../images/node/switch.png';
        };

        $scope.loadUserList= () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_USER_LIST",
                IS_ORGAN_LINK: $scope.ticket.is_organ_link,
                NODE_ID: $scope.ticket.root_cause_sysnamea
            }, function (result) {
                $scope.managerList = result;
                $scope.managerList.map(v => v.selected = false);
            });
        };

        $scope.getFailureImage = () => {
            if($scope.failureInfo){
                Object.assign($scope.failureInfo, {
                    "source_image": tools.store.niaData.nodes.find(v=>v.device_name == $scope.failureInfo.source_name).image,
                    "target_image": tools.store.niaData.nodes.find(v=>v.device_name == $scope.failureInfo.target_name).image
                });
            }
        };

        $scope.getTicketTypeLabel = (ticket) => {
            if(ticket.root_cause_type) {
                return tools.constants.TicketType[ticket.root_cause_type].text + '장애';
            } else {
                return '장애';
            }
        };

        $scope.dlgFunc.cancel = () => {
            $('.angular-meditor-toolbar--show').removeClass('angular-meditor-toolbar--show');
            $mdDialog.cancel();
        }
    }
}

AIProcessDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', 'param'];
export default AIProcessDialogCtrl;
