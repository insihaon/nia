import BaseController from 'scripts/controller/baseController'

class ConfigTestActionDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect, param, $sce) {
        $scope.config = tools.store.viewType.config_test_action;
        super($injector, $scope, tools, $http, $timeout, $sce);

        $scope.testApi = '';
        $scope.testResult = null;
        $scope.bad_crc = '';
        $scope.interface_config = '';

        $scope.controlSelect = '';
        $scope.remoteResult = '';

        $scope.showPingTxt = false;

        // 외부 'http://183.107.19.101:9995/ping.txt'
        // 운용
        // $scope.pingFilePath = 'http://116.89.191.47:8080/ping/' + $scope.ticket.ip_addra + '.txt';
        // $scope.pingFilePath = window.origin + '/ping_'+ $scope.ticket.ip_addra +'.txt';

        $scope.trustSrc = function() {
                const date = moment().format('YYYYMMDD');
                const fileName = $scope.ticket ? $scope.ticket.ip_addra : $scope.alarm.ip_addr + '_' + tools.store.userId;
                $scope.pingFilePath = `http://116.89.191.47:8080/ping/` + fileName + `.txt`;
                return $sce.trustAsResourceUrl($scope.pingFilePath);
        };

        $scope.equip_ip = [];
        $scope.agency_ip = [];

        $scope.compareAccuracy = (source_list, target_list) => {
            source_list.map((source) => {
              const list = target_list.filter((target) => target.ip == source.ip);
              if(list.length > 0) {
                  source.accuracy = true;
              } else {
                  source.accuracy = false;
              }
          })
        };

        $scope.loadAgencyIpList = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_AGENCY_IP_LIST",
                NODE_ID: !$scope.ticket ?  $scope.alarm.node_nm : $scope.ticket.root_cause_sysnamea
            }, async function (result) {
                $scope.agency_ip = result.map((ip) => { return {ip: ip.nren_ip, accuracy: true }});

                await $scope.compareAccuracy($scope.equip_ip , $scope.agency_ip);
                await $scope.compareAccuracy($scope.agency_ip , $scope.equip_ip);
            });
        };

        $scope.onInit = () => {
            $scope.alarm = param.alarm
            $scope.ticket = param.ticket;
            $scope.node_name = $scope.ticket && $scope.ticket.root_cause_sysnamea || $scope.alarm && $scope.alarm.node_nm;
            $scope.ip_addr = $scope.ticket && $scope.ticket.ip_addra || $scope.alarm && $scope.alarm.ip_addr
            $scope.interface = $scope.ticket && $scope.ticket.root_cause_porta || $scope.alarm && $scope.alarm.alarmloc

            
            $scope.niaRequest('GET', 'crc', {nodename: !$scope.ticket ?  $scope.alarm.node_nm : $scope.ticket.root_cause_sysnamea || 'busan-5812', ifname: !$scope.ticket ?  $scope.alarm.alarmloc : $scope.ticket.root_cause_porta || 'xe41'}, (result_crc) => {
                // console.log("NIA CRC API RESULT: ", result_crc);
                $scope.bad_crc = result_crc.data[0];

                $scope.niaRequest('GET', 'config', {nodename: !$scope.ticket ?  $scope.alarm.node_nm : $scope.ticket.root_cause_sysnamea || 'busan-5812', ifname: !$scope.ticket ?  $scope.alarm.alarmloc : $scope.ticket.root_cause_porta || 'xe41'}, (result_config) => {
                    // console.log("NIA CONFIG API RESULT: ", result_config);
                    $scope.interface_config = result_config.data[0];

                    if(Array.isArray($scope.interface_config.ipAddr)) {
                        $scope.equip_ip = $scope.interface_config.ipAddr;
                    } else {
                        $scope.equip_ip[0] = {ip:$scope.interface_config.ipAddr};
                    }
                });
            });

            $scope.interval_crc = setInterval(() => {
                $scope.niaRequest('GET', 'crc', {nodename: !$scope.ticket ?  $scope.alarm.node_nm : $scope.ticket.root_cause_sysnamea || 'busan-5812', ifname: !$scope.ticket ?  $scope.alarm.alarmloc : $scope.ticket.root_cause_porta || 'xe41'}, (result) => {
                    // console.log("NIA CRC API RESULT: ", result);
                    $scope.bad_crc = result.data[0];
                });
            }, 10000);

            $scope.loadAgencyIpList();
        };

        $scope.reloadPingTxt = () => {
            let element = document.getElementById('ping');
            if (element) {
                element.src += '';
            }
        };

        $scope.onClickExecuteTest = () => {
            if($scope.controlSelect == 'ping') {
                $scope.showProgressBar = true;
                $scope.showPingTxt = false;
                if($scope.ticket ? !$scope.ticket.ip_addra : !$scope.alarm.ip_addr) {
                    return tools.showAlert('해당 장비의 IP가 존재하지 않습니다.');
                }
                return tools.http({
                    service: tools.constants.Service.rca,
                    action: "SEND_PING",
                    IP: $scope.ticket ? $scope.ticket.ip_addra : $scope.alarm.ip_addr,
                    USER_ID: tools.store.userId
                }, function (result) {
                    $scope.pingMessage
                    $scope.reloadPingTxt();
                    $scope.showPingTxt = true;
                    $scope.showProgressBar = false;
                });
            } else {
                $scope.showPingTxt = false;
                if(tools.debug) {
                    $tools.showToast('개발자 모드일때는 원격제어를 제한합니다.');
                    return;
                }
                $scope.testResult = '시험 결과 내용 출력 텍스트' + ( $scope.testApi || '' );

                $scope.niaRequest('POST', $scope.controlSelect, {nodename: !$scope.ticket ?  $scope.alarm.node_nm : $scope.ticket.root_cause_sysnamea || 'busan-5812', ifname: !$scope.ticket ?  $scope.alarm.alarmloc : $scope.ticket.root_cause_porta || 'xe41'}, (result) => {
                    if(!result || result.status === false) {
                        // console.log('======= API 호출 결과가 존재하지 않습니다. =======');
                        $tools.showToast('원격 제어에 실패하였습니다.');
                        return ;
                    }
                    $scope.remoteResult = result.data;
                    $tools.showToast('원격 제어가 완료되었습니다.');
                });
            }
        };

        const NIA_API = {
            crc: { service: '/services/stat/badcrc'},
            config: { service: '/services/config/interfaces'},
            shutdown: { service: '/services/config/interfaces/shutdown'},
            noshut: { service: '/services/config/interfaces/noshut'},
            login: { service: '/auth/login'}
        };
        $scope.niaRequest = (method = 'GET', path = 'crc', param = {nodename: 'busan-5812', ifname: 'xe41'}, callback = (res => {})) => {
            // if(path == 'shutdown' || path == 'noshut') {
            //     return ;
            // }

            const service = 'ipsdn' + NIA_API[path].service;
            const parameter = '?nodename=' + param.nodename + '&ifname=' + param.ifname;

            tools.http({
                service: tools.constants.Service.rca,
                action: "REQUEST_NIA_API",
                METHOD: method,
                SERVICE: service,
                PARAM: parameter,
                TOKEN: tools.store.niaToken
            }, callback);
        };

        const objectToParam = function(obj) {
            var str = [];
            for (var p in obj)
              if (obj.hasOwnProperty(p)) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
              }
            return str.join("&");
          }

        $scope.clickCloseDialog = () => {
            clearInterval($scope.interval_crc);
            $scope.interval_crc = null;

            $scope.dlgFunc.cancel();
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#configTestAction');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };

        angular.element(document).ready(() => {
            $(".alarm-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });
            $(document).keyup(function(e) {
                if (e.key === "Escape") {
                    $scope.clickCloseDialog();
                } else return;
            });
        });

    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect', 'param', '$sce', ConfigTestActionDialogCtrl];
