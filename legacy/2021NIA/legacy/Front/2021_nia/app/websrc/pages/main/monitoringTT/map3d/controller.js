import BaseController from 'scripts/controller/baseController'

class Map3dCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdPanel) {
        $scope.config = tools.store.viewType.map3d;
        super($injector, $scope, tools, $http, $timeout, $mdPanel);

        let controller = this;
        this.controller = this;
        $scope.ticketRtAlarms = [];           // 알람 리스트
        $scope.ticketPFAlarms = [];           // 알람 리스트
        $scope.ticketTrafficAlarms = [];           // TRAFFIC 알람 리스트
        $scope.ticketAlarmsType = 'RT';       // 알람 리스트 타입 (RT , PF)
        $scope.filteredAlarms = {};         // 필터링된 알람 리스트
        $scope.filteredPFAlarms = {};         // 필터링된 알람 리스트
        $scope.filteredAbnormalTrafficAlarms = {};         // 필터링된 알람 리스트
        $scope.filteredBadTrafficAlarms = {};         // 필터링된 알람 리스트
        $scope.alarmFilterParams = [];      // 알람 필터링 파라미터
        $scope.carrierName = '';            // 캐리어명
        $scope.query = {                    // 알람 리스트 옵션
            order: ['end_time','monitored_object'],
            limit: 500,
            page: 1
        };
        
        const roadm_slots = {
            "192.168.200.213": ["MRPA-A", "MRPA-A", "MRSA-2B", "MRSA-2A", "OCPMA-4", "BLK", "OM24A", "OM24A", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK"],
            "192.168.200.220": ["MRPA-A", "MRPA-A", "OTUC1A-L", "OTUC1A-L", "OTUC1A-L", "OTUC1A-L", "OTUC1A-L", "OTUC2A-L", "OM2C2A-L", "OM2C2A-L", "OM24A", "OM24A", "OM24A", "OM24A", "BLK", "BLK"],
            "192.168.200.215": ["MRPA-A", "MRPA-A", "MRSA-2A", "MRSA-2B", "OCPMA-4", "BLK", "OTUC1A-L", "OTUC1A-L", "BLK", "BLK", "OTUC1A-L", "OTUC1A-L", "BLK", "BLK", "OM24A", "OM24A", "BLK", "BLK"],
            "192.168.200.216": ["MRPA-A", "MRPA-A", "MRSA-2A", "MRSA-2B", "OCPMA-4", "BLK", "OTUC1A-L", "OTUC1A-L", "BLK", "BLK", "OM24A", "OM24A", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK"],
            "192.168.200.218": ["MRPA-A", "MRPA-A", "MRSA-2A", "MRSA-2A", "OCPMA-4", "BLK", "OM24A", "OM24A", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK", "BLK"],
            "192.168.200.210": ["MRPA-A", "MRPA-A", "MRSA-2A", "OCPMA-4", "OTUC1A-L", "OTUC1A-L", "OTUC1A-L", "OTUC2A-L", "OM2C2A-L", "OM2C2A-L", "OM24A", "OM24A", "BLK", "BLK", "BLK", "BLK"]
        };


        $scope.topologyType = 3;
        const jsons = ['data_nia_ip_1.json', 'data_nia_ip_2.json', 'data_nia_ip_3.json'] 
        const defaultMapFile = jsons[$scope.topologyType - 1] || 'data_nia_ip_3.json'
        if (!jsons.includes(localStorage['last_map'])) {
            localStorage['last_map'] = defaultMapFile
        }

        // Topology Reload 이벤트
        $scope.$on('broadcastReloadTopology', function(event, param) {
            if(param == 'reset'){
                $('#topology_container > g').children().remove();
                $('table.alarm-table tbody tr').remove();
                // controller.map.resetZoom(750);
                $scope.loadTicketAlarm();
            }
        }.bind(this));

        // 노드클릭 이벤트
        $scope.$on('broadcastClickNode', function(event, param) {
            const node = controller.map.data.nodes.find(v => v.device_name == param);
            controller.map.zoomInByNode(node, 5, 1000);
        }.bind(this));

        // SlideNav 토글 이벤트
        $scope.$on('broadcastSideNavEvent', function(event) {
            $timeout(this.resize.bind(this), 300);
            setTimeout(() => {
                // $scope.hideAlarmList(true)
                $scope.clickAlarmExpandBtn(null);
            }, 400)
        }.bind(this));

        // 티켓 전표 더블클릭
        $scope.$on('BroadcastOnChangedTicket', function listenStatus(event, param) {
            if ($scope.ticket && $scope.ticket.ticket_id == param.ticket_id) {
                return
            }
            controller.map.resetZoom(750);
            $scope.loadTicketAlarm(param);
        }.bind(this));

        // 티켓 전표 하단 케이블리스트 클릭
        $scope.$on('broadcastClickCableList', function (event, param) {
            console.log("param",param);

            // let d = this.map.data.links.find(v => v.id == param.mba_ticket_cable_id);
            let d = this.map.data.links.find(v => v.id == controller.getLinkIdByCable(param));
            if(!d) { return }

            this.map.selectElement(d, 'link');
        }.bind(this));

        // 토폴로지 상단 메뉴
        $scope.$on('broadcast.menuEvent', async function (event, param) {
            if (!param) return;
            switch (param) {
                case 'properties':
                    if(tools.debug) {
                        $scope.onMenuClick(param);
                    }
                    break;
                case 'changeType':
                    const index = $scope.topologyType  % jsons.length
                    $scope.topologyType = index + 1;
                    const newFileName = jsons.at(index)
                    newFileName && $scope.loadMapByFile(newFileName, true);
                    console.log(`topologyType: ${$scope.topologyType}`)
                    break;
                case 'reload':
                    $scope.loadTicketAlarm();
                    break
                case 'resetZoom':
                    controller.map.resetZoom(750);
                    break;
                case 'nodeZoomTest':
                    {
                        const node = controller.map.data.nodes.find(v => v.device_name == '수원성균관대')
                        controller.map.zoomInByNode(node, 5, 1000);
                    }
                    break;
                case 'linkZoomTest':
                    {
                        const node = controller.map.data.links.find(v => v.id == '44')
                        controller.map.zoomInByLink(node, 5, 1000);
                    }
                    break;
                case 'toggleLabel':
                    {
                        let label = controller.map.options.node.displayField === 'ip_addr' ? 'device_name' : 'ip_addr';
                        controller.map.options.node.displayField = label;
                        controller.map.options.onChangeNodeDisplayField();
                    }
                    break;
                case 'refresh':
                    $scope.loadMapByFile( controller.map.options.fileName, true);
                    break;
                case 'updateTopology':
                    controller.updateNodePosition();
                    break;
            }
        }.bind(this));

        $timeout(() => {
            $('div.map3d div.alarmExpandBtn').css('bottom', $('div.map3d div.alarmListWapper').height() + 14);
        }, 300);

        $scope.loadMapByFile = (fileName = defaultMapFile, isCompareData = false) => {
            controller.map.options.fileName = fileName;
            return controller.loadMapData(fileName).then(async (data) => {
                if(isCompareData) {
                    const result = await $scope.compareNiaData(data.nodes, data.links) || { };
                    data.nodes = result.nodes;
                    data.links = result.linkData;
                }
                $scope.loadMap(data);
                $scope.loadTicketAlarm();
            });
        };

        $scope.compareNiaData = (nodes = [], links = []) => {
            return Promise.all([controller.requestNodes(), controller.requestLinks(), nodes, links]).then((values) => {
                const [nodeData, linkData] = values;
                linkData.map((link1) => {
                    const length = linkData.filter((link2) => {
                        return (link1.source_id == link2.target_id && link1.target_id == link2.source_id && link1.source_pau == link2.target_pau && link1.target_pau == link2.source_pau);
                    }).length;
                    link1.distance = (!link1.equip_type && length > 0) ? 1 : null;
                });
                return { nodes: nodeData, linkData };
            });
        };

        $scope.convertProtocolName = (protocol) => {
            if(protocol == 17) {
                return 'UDP';
            } else if(protocol == 6) {
                return 'TCP';
            } else { return ''; }
        };

        $scope.loadMap = (data) => {
            setTimeout(() => {
                tools.store.niaData = data;
                controller.map.options.load(data);
            }, 100);
        }

        $scope.zoomByAlarm = (alarm) => {
            const sysname = getAlarmSysname(alarm);
            const selector = document.getElementById('node_' + sysname);
            const invisible = selector.classList.contains('node_invisible');
            if (selector && !invisible) {
                const node = d3.select(selector).datum();
                node && $scope.zoom(null, node);
            } else {
                $scope.tools.showToast("장비를 찾을 수 없습니다.", null, "알림", 1500);
            }
        }

        $scope.zoom = (alarmLink, alarmNode) => {
            setTimeout(() => {
                if (alarmLink) {
                    controller.map.selectElement(alarmLink, 'link', true)
                    controller.map.zoomInByLink(alarmLink, 5, 750)
                } else if (alarmNode && alarmNode.visible) {
                    controller.map.selectElement(alarmNode, 'node', true)
                    controller.map.zoomInByNode(alarmNode, 5, 750)
                    controller.map.setInfomation("node", alarmNode, null, $scope.slot);
                } else {
                    controller.map.resetZoom(750)
                }
            }, 200);
        };

        $timeout(() => {
            $('div.map3d div.alarmExpandBtn').css('bottom', $('div.map3d div.alarmListWapper').height()+14);
        }, 300);

        $scope.expandAlarmType = 1;
        // $scope.expandAlarmType = 0,1,2;
        // 0: 최소화
        // 1: 중간크기
        // 2: 최대




        $scope.expandAlarm = false;
        $scope.hideAlarmList = (isChangeSlide = false) => {
            if (!isChangeSlide) {
                $scope.expandAlarm = !$scope.expandAlarm;
            }

            let alarmList = $('div.map3d div.alarmListWapper')[0];
            let alarmListContent = $('div.map3d div.alarmListWapper md-content.alarm-content')[0];
            let alarmListTable = $('div.map3d div.alarmListWapper table.alarm-table')[0];

            let height = $scope.expandAlarm ? '100%' : '210px';
            let height_important = $scope.expandAlarm ? '100% !important' : '';

            alarmList.style.height = height;
            alarmListContent.style.height = height;
            alarmListTable.style = "height:" + height_important;

            setTimeout(() => {
                $('div.map3d div.alarmExpandBtn').css('bottom', $('div.map3d div.alarmListWapper').height() + 14);
            });
        };

        // increase, decrease
        $scope.clickAlarmExpandBtn = (type = 'increase') => {
            switch (type) {
                case 'increase' : $scope.expandAlarmType++; break;
                case 'dbincrease' : $scope.expandAlarmType = 2; break;
                case 'decrease' : $scope.expandAlarmType--; break;
                case 'dbdecrease' : $scope.expandAlarmType = 0; break;
                case null :
                default: break;
            }

            let alarmList = $('div.map3d div.alarmListWapper')[0];
            let alarmListContent = $('div.map3d div.alarmListWapper md-content.alarm-content')[0];
            let alarmListTable = $('div.map3d div.alarmListWapper table.alarm-table')[0];

            var heightType = {
                0: {height: '0', height_important: ''},
                1: {height: '210px', height_important: '100% important'},
                2: {height: '100%', height_important: '100%'}
            };

            alarmList.style.height = heightType[$scope.expandAlarmType].height;
            alarmListContent.style.height = heightType[$scope.expandAlarmType].height;
            alarmListTable.style.height = heightType[$scope.expandAlarmType].height_important;

            setTimeout(() => {
                $('div.map3d div.alarmExpandBtn').css('bottom', $('div.map3d div.alarmListWapper').height() + 14);
            }, 300);
        };

        $scope.clearTicketAlarm = () => {
            let data = controller.map.data
            data.nodes.forEach(node => {
                delete node.alarm_count
                delete node.related_alarm
            });

            data.links.forEach(node => {
                node.status = 1
            });

            controller.map.draw(false);
        }

        $scope.loadTicketAlarm = async (ticket = null) => {
            let reload = false;
            if (ticket == null) {
                ticket = $scope.ticket;
                reload = true;
            }

            if (ticket == null) {
                return;
            }

            $scope.clearTicketAlarm();
            if (reload || ticket && $scope.ticket != ticket) {
                $scope.ticket = ticket;

                var alarms = await controller.loadNiaAlarmList(ticket);
                var alarmLink = await controller.loadNiaCableAlarmList(ticket);

                // var data = controller.map.data;
                var [alarmNode] = await $scope.updateBadgeCount(controller.map.data, alarms);
                $scope.zoom(alarmLink, alarmNode);
                controller.map.draw(false);
            }
        };

        $scope.onMenuClick = (param) => {
            switch (param) {
                case 'properties':
                    if (controller.gui.domElement.style.display == 'none') {
                        controller.gui.show();
                    } else {
                        controller.gui.hide();
                    }
                    break;
            }
        }

        $scope.clickAlarms = (alarm) => {
            controller.map.resetZoom(750);
            $scope.slot= [];
            $scope.clickAlarmData = alarm;
            
            function getAlarmSlot(alarm) {
                if (!alarm) return null
                let slot = alarm.match(/(?<=S[.0]*)\d+/)
                if (slot) {
                    return `S${slot}`
                } else {
                    return null
                }
            }

            if ($scope.ticket.ticket_type == "RT") {
                const slot = getAlarmSlot(alarm.slot)
                if (slot) {
                    $scope.slot.push('.' + slot);
                }
            } else if ($scope.ticket.ticket_type == "PF") {
                const slot = getAlarmSlot(alarm.port)
                if (slot) {
                    $scope.slot.push('.' + slot);
                }
            }

            $('.animation-blink').removeClass('animation-blink');
            d3.selectAll(".node_container").classed('selected focus ', false);
            $scope.zoomByAlarm(alarm)
        };

        $scope.displaySlotAlarm = (select) => {
            $scope.slot= [];
            $('.animation-blink').removeClass('animation-blink');
            if(select.d.device_type){
                $scope.loadTemplate(select, "node");
            }
            let { ticket } = $scope
            if (ticket) {
                if (ticket.ticket_type == "RT") {
                    $scope.ticketRtAlarms.map(function (alarm) {
                        if (alarm.sysname.split('-')[0] == select.d.id && alarm.alarmloc) {
                            $scope.slot.push('.' + alarm.alarmloc.split('-')[0].replace('.', ""));
                        }
                    });
                } else if (ticket.ticket_type == "PF") {
                    $scope.ticketPFAlarms.map(function (alarm) {
                        if (alarm.sysname.split('-')[0] == select.d.id && alarm.port) {
                            $scope.slot.push('.' + alarm.port.split('-')[0].replace('.', ""));
                        }
                    });
                }
            }

            $scope.slot = $scope.slot.filter((v, i) => $scope.slot.indexOf(v) === i);
            return controller.map.setInfomation("node", select.d, select.elements, $scope.slot);
        };

        $scope.loadTemplate = (select , click) => {
            $scope.filteredAgencyList = [];

            function show(id, visible) {
                document.getElementById(id).style.display = visible ? "" : "none"
            }

            if (!select) {
                show('templateUnitArea', false);
                show('templateAgentListArea', false);
                return
            }

            const type = (click == "node") ? select.d.device_type : select.equiptype || controller.map.data.nodes.find(v => v.id == getAlarmSysname(select)).device_type;
            if ($('.componentWapper > .properties').length > 0) {
                $('.componentWapper > .properties').remove();
            }

            if (type == "2" || type == "POTN") {
                document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-2').innerHTML;
                $('.potnSysname').html(click == "node" ? select.d.id : select.sysname);
                show('templateUnitArea', true);
                show('templateAgentListArea', false);
            } else if (type == "0" || type == "ROADM") {
                var roadmSlot;
                document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-3').innerHTML;
                if (click == "node" && roadm_slots[select.d.id]) {
                    roadmSlot = roadm_slots[select.d.id];
                } else if (click == "alarm") {
                    select = controller.map.data.nodes.find(v => v.id == getAlarmSysname(select));
                    roadmSlot = roadm_slots[select.id];
                }

                if (roadmSlot) {
                    for (var i = 0; i < roadmSlot.length; i++) {
                        $('#templateUnitArea .table-wapper tr.slot-table > td')[i].innerHTML = roadmSlot[i];
                    }
                    document.getElementsByClassName('S17')[0].style.display = roadmSlot.length < 17 ? "none" : '';
                    document.getElementsByClassName('S18')[0].style.display = roadmSlot.length < 17 ? "none" : '';
                }
                $('.roadmSysname').html(click == "node" ? select.d.id : select.id);
                show('templateUnitArea', true)
                show('templateAgentListArea', false)
            } else {
                const nodeId = (click == "node") ? select.d.id : select.id;
                const list = $scope.agencyList[nodeId] || [];
                $scope.filteredAgencyList = [...list];
                $scope.safeApply();

                show('templateUnitArea', false);
                show('templateAgentListArea', true);
            }
        };

        function getAlarmSysname(alarm) {
            if (alarm.equiptype == '7712/5812') {
                return alarm.sysname
            } else if (alarm.sysname.includes('n9k') || alarm.sysname.includes('control') || alarm.sysname.includes('cxp') || alarm.sysname.includes('asr9k')) {
                return alarm.sysname
            } else {
                return alarm.sysname.split('-')[0]
            }
        }


        $scope.updateBadgeCount = (data, alarms = []) => {
            let alarmCnt = {};
            let related_alarm = alarms.find(v => v.related_alarm == true);
            let alarmNodes = new Set();
            related_alarm = !related_alarm ? '' : related_alarm.sysname.split('-')[0];

            alarms.map(alarm => getAlarmSysname(alarm)).map(function (sysname) {
                if (sysname in alarmCnt) {
                    alarmCnt[sysname]++;
                } else {
                    alarmCnt[sysname] = 1;
                }
            });
            let key = Object.keys(alarmCnt);

            for(var i = 0; i < key.length; i++){
                data.nodes.map(function (node) {
                    if (node.id == key[i]) {
                        node.alarm_count = alarmCnt[key[i]];
                        alarmNodes.add(node);
                    }
                    if (node.id == related_alarm) {
                        node.related_alarm = true;
                        alarmNodes.add(node);
                    }
                })
            }

            return [...alarmNodes].sort(function (a, b) { a.alarm_count > b.alarm_count });
        };

        $scope.onInit = () => {
            // 템플릿 HTML 세팅
            document.getElementById('componentWapperId').appendChild( document.getElementById('template-3').content.cloneNode(true) );
            document.getElementById('templateUnitArea').innerHTML  = document.querySelector('#template-3').innerHTML;
        }

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

        $scope.getAnomalyDirection = (alarm) => {
            let result = [];

            if(alarm.fltbpsin_anomaly) {
                result.push('INFLOW');
            } else if(alarm.fltbpsout_anomaly) {
                result.push('OUTFLOW');
            }

            return result.join(', ')

        };

        angular.element(document).ready( () => {
            controller.loadNiaAgencyList();
            setTimeout(async function () {
                let map = controller.map = new Map2d();
                let options = map.options;
                let log = window.console.log;

                map.initialize();
                await $scope.loadMapByFile(defaultMapFile, true);

                // xy 좌표에 대한 정보가 없다. 추가 해서 맵을 로드 하면 된다.
                //     const [nodes, links] = values
                //     tools.store.niaData = { nodes, links }
                //     controller.loadMapData().then(data => {
                //         controller.map.options.load(data)
                //         // map.load();
                //     })
                // });

                map.addEventListener(Map2d.eventType.selectChanged, e => {
                    // controller.changeAlarmFilter(e);
                    log('selected changed : ', e);

                    if(e.target_type == 'svg') {
                        $scope.loadTemplate(null, "svg");
                    } else {
                        $scope.displaySlotAlarm(controller.map.select);
                    }
                });

                function startTour() {

                    options.select.node_tour = true;
                    options.select.link_tour = true;
                    setTimeout(() => {
                        options.onChangeNodeSelect();

                        setTimeout(() => {
                            options.onChangeLinkSelect();
                        }, 500);

                    }, 1000);
                }
                // startTour();

                function autoTest() {

                    let wait = (ms = 0) => new Promise(resolve => setTimeout(resolve, ms))

                    function changeOption(property, value, rollback) {

                        var oldValue = this[property];
                        var rollbackTimeout = typeof rollback === "boolean" ? 3000 : rollback
                        if(rollback){
                            setTimeout(() => {
                                changeOption.call(this, property, oldValue, false)
                                map.draw()
                            }, rollbackTimeout);
                        }

                        console.log(`options.${property} = ${value}`)
                        this[property] = value;

                    }

                    let jobList = [
                        options.load,
                        changeOption.bind(options, 'save_map', !options.save_map, true), options.load,
                        changeOption.bind(options.select, 'node_tour', !options.select.node_tour, true), options.onChangeNodeSelect,
                        changeOption.bind(options.select, 'link_tour', !options.select.link_tour, true), options.onChangeLinkSelect,
                        options.onTestLinkAlarmOccur,
                        options.onTestMbaAlarmOccur,
                        changeOption.bind(options.node, 'displayField', 'id', true), options.onChangeNodeDisplayField,
                        changeOption.bind(options.node, 'displayImage', 'icon', true), options.load,
                        changeOption.bind(options.node, 'r', 100, true), options.onChangeNodeR,
                        changeOption.bind(options.node, 'width', 100, true), options.onChangeNodeWidthHeight,
                        changeOption.bind(options.node, 'height', 100, true), options.onChangeNodeWidthHeight,
                        changeOption.bind(options.node, 'badge_size', 100, true), options.onChangeNodeBadgeSize,
                        changeOption.bind(options.link, 'displayField', 'port', true), options.onChangeLinkText,
                        changeOption.bind(options.link, 'stroke_width', 20, true), options.onChangeLink,
                        changeOption.bind(options.link, 'text_color', '#FF0000', true), options.onChangeLinkText,
                        changeOption.bind(options.link, 'text_size', 18, true), options.onChangeLinkText,
                        changeOption.bind(options.link, 'badge_size', 100, true), options.onChangeLink,
                        changeOption.bind(options.link, 'autoReverse', !options.link.autoReverse, true), options.onChangeLink,
                        changeOption.bind(options.link_traffic, 'r', 50, true), options.onChangeLinkTrffic,
                        changeOption.bind(options.link_traffic, 'color', '#FF0000', true), options.onChangeLinkTrffic,
                        changeOption.bind(options.link_traffic, 'opacity', 0.2, true), options.onChangeLinkTrffic,
                        changeOption.bind(options.link_traffic, 'dur', 0.5, true), options.onChangeLinkTrfficAnimation,
                    ]

                    setTimeout(async () => {
                        for (let index = 0; index < jobList.length; index++) {
                            try {
                                const fn = jobList[index];
                                console.log(`AutoTest run function : ${fn.name}`)
                                fn()
                                await wait(300)
                            } catch (e) {
                                console.error(e)
                            }
                        }
                    }, 100);
                }
                options.autoTest = autoTest;
                // autoTest();

                let gui = controller.gui = new dat.GUI();
                gui.hide();

                gui.add(options, 'fileName', ['data.json', 'data_line.json', 'data_mba.json', 'data_mba_1.json', 'data_mba_2.json', 'data_stable.json', 'data_nia_ip_1.json', 'data_nia_2.json', 'data_nia_2_1.json', 'data_nia_2_2.json', 'data_nia_2_3.json', 'data_nia_2_4.json', 'data_nia_2_5.json', 'data_nia_ip_2.json', 'data_nia_3.json' ]).name('파일').onChange(e => { options.save_map = false; options.load(e) }).listen();
                gui.add(options, 'save').name('맵저장')
                gui.add(options, 'exportToFile').name('JSON 파일로 저장')
                gui.add(options, 'save_map').name('저장된 맵 보기').onChange(e => options.load()).listen();
                gui.add(options, 'link_type', ['curved', 'arc', 'line', 'mixed']).name('링크 타입').onChange(options.onChangeAnimated).listen();
                gui.add(options.select, 'node_tour').name('노드 순환').onChange(options.onChangeNodeSelect).listen();
                gui.add(options.select, 'link_tour').name('링크 순환').onChange(options.onChangeLinkSelect).listen();
                gui.add(options, 'mode', [map.mouseMode.select, map.mouseMode.edit]).name('클릭 시 액션');
                gui.open();

                var fTest = gui.addFolder('테스트');
                fTest.add(options, 'autoTest').name('자동테스트');
                fTest.add(options, 'onTestLinkAlarmOccur').name('링크장애 발생/해지');
                fTest.add(options, 'onTestMbaAlarmOccur').name('MBA장애');
                fTest.open();

                let fNode = gui.addFolder('노드');
                fNode.add(options.node, 'displayField', ['mac', 'ip', 'device_name', 'id']).name('레이블').onChange(options.onChangeNodeDisplayField).listen();
                fNode.add(options.node, 'fix_size').name('폰트사이즈고정').onChange(options.onChangeScale).listen(); //
                fNode.add(options.node, 'displayImage', ['image', 'icon']).name('이미지').onChange(e => options.load()).listen();
                fNode.add(options.node, 'show_r').name('범위표시').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'r', 0, 150).name('범위').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'width', 10, 200).name('이미지너비').onChange(options.onChangeNodeWidthHeight).listen();
                fNode.add(options.node, 'height', 10, 200).name('이미지높이').onChange(options.onChangeNodeWidthHeight).listen();
                fNode.add(options.node, 'invisible_node').name('사용여부').onChange(options.onChangeVisibleNodeOption).listen();
                // fNode.add(options.node, 'display_badge', 0, 100).name('뱃지 보기/감추기').onChange(options.onChangeNodeBadgeSize).listen();
                // fNode.add(options.node, 'badge_size', 0, 100).name('뱃지크기').onChange(options.onChangeNodeBadgeSize).listen();
                fNode.open();

                var fLink = gui.addFolder('링크');
                fLink.add(options.link, 'displayField', ['id', 'port', 'speed', 'status', 'index']).name('레이블').onChange(options.onChangeLinkText).listen();
                fLink.add(options.link, 'stroke_width', 3, 20).name('너비').onChange(options.onChangeLink).listen();
                fLink.addColor(options.link, 'text_color').name('색상').onChange(options.onChangeLinkText).listen();
                fLink.add(options.link, 'text_size', 6, 18).name('폰트사이즈').onChange(options.onChangeLinkText).listen();
                fLink.add(options.link, 'badge_size', 0, 100).name('뱃지크기').onChange(options.onChangeLink).listen();
                fLink.add(options.link, 'autoReverse').name('위치보정').onChange(options.onChangeLink).listen();
                fLink.open();

                var fLinkTraffic = gui.addFolder('링크트래픽');
                fLinkTraffic.add(options.link_traffic, 'show').name('보기/감추기').onChange(options.onChangeLinkTrffic).listen();
                fLinkTraffic.add(options.link_traffic, 'show_arrow').name('방향 표시').onChange(options.onChangeLinkTrffic).listen();
                fLinkTraffic.add(options.link_traffic, 'r', 0, 20).name('범위').onChange(options.onChangeLinkTrffic).listen();
                fLinkTraffic.addColor(options.link_traffic, 'color').name('색상').onChange(options.onChangeLinkTrffic).listen();
                fLinkTraffic.add(options.link_traffic, 'opacity', 0.0, 1.0).name('투명도').onChange(options.onChangeLinkTrffic).listen();
                fLinkTraffic.add(options.link_traffic, 'dur', 0.5, 3).name('속도').onChange(options.onChangeLinkTrfficAnimation).listen();
                fLinkTraffic.open();

                let that = this;
                let div = d3.select('div.gripper')
                    .call(d3.drag()
                        .on('start.interrupt', function () {
                            log('stop')
                            div.interrupt();
                            that.mouse = d3.mouse(this);
                        })
                        .on('start drag', function () {
                            // log('drag')
                            div.style('top', d3.event.y - that.mouse[1] + 'px')
                            div.style('left', d3.event.x - that.mouse[0] + 'px')

                            let properties = d3.select('div.properties');
                            properties.style('top', d3.event.y - that.mouse[1] + parseFloat(div.style('height')) + 'px')
                            properties.style('left', d3.event.x - that.mouse[0] + 'px')
                        }));


                /* ##기능 disabled */
                /* 링크 트래픽 애니메이션 비활성화 */
                /*if(tools.store.getMode('op_simple_ui_mode') == undefined ||
                    tools.store.getMode('op_simple_ui_mode') == true) {
                    controller.map.options.link_traffic.show = false;
                    $('head').append('<link rel="stylesheet" href="css/simpleUi.css">');
                }*/


            }, 100);

            // $('table.alarm-table.rt-alarm').on('scroll', this.resize.bind(this));
            // $('table.alarm-table.pf-alarm').on('scroll', this.resize.bind(this)); /* 현재 광레벨 존재 x */
            // $('table.alarm-table.traffic-abnormal-alarm').on('scroll', this.resize.bind(this));
            // $('table.alarm-table.traffic-bad-alarm').on('scroll', this.resize.bind(this));
            $(window).resize(this.resize.bind(this));
        });
    }


    resize() {
        let { $scope } = this;
        // 알람 리스트 resize
        $("table.alarm-table.rt-alarm > *").width($("table.alarm-table.rt-alarm").width() + $("table.alarm-table.rt-alarm").scrollLeft());
        // $("table.alarm-table.pf-alarm > *").width($("table.alarm-table.pf-alarm").width() + $("table.alarm-table.pf-alarm").scrollLeft());  /* 현재 광레벨 존재 x */
        $("table.alarm-table.traffic-abnormal-alarm > *").width($("table.alarm-table.traffic-abnormal-alarm").width() + $("table.alarm-table.traffic-abnormal-alarm").scrollLeft());
        $("table.alarm-table.traffic-bad-alarm > *").width($("table.alarm-table.traffic-bad-alarm").width() + $("table.alarm-table.traffic-bad-alarm").scrollLeft());
        $scope.clickAlarmExpandBtn(null);
    }

    /** 하드코딩 토폴로지 데이터 로드 */
    async loadMapData(fileName) {
        let { tools } = this;
        return new Promise((resolve, reject) => {
            $.get(`json/${fileName}`, function (data) {
                resolve(data)
            }, 'json');
        })
    }
    
    async updateNodePosition () {
        let { $scope, tools, map } = this;
        $scope.tools.createConfirmDlg(
            '토폴로지를 저장하시겠습니까?',
            '확인을 선택하면 데이터가 저장됩니다.', null)
        .then(function () {
            const TYPE = $scope.topologyType.toString()
            const NODES = map.getVisualNodeAll().map(n => {
                return {
                    node_type: n.device_type,
                    node_id: n.id,
                    node_image_path: n.image.path,
                    fx: Math.floor(n.x || n.fx),
                    fy: Math.floor(n.y || n.fy),
                    topology_type: TYPE
                }
            })
            return new Promise((resolve, reject) => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "UPDATE_NODE_POSITION",
                    TYPE: TYPE,
                    NODES: NODES
                }, (result) => {
                    if (result) { resolve(result) }
                    else { reject(result) }
                })
            });
        });
    }

    requestNodes() {
        let { $scope, tools } = this;
        return new Promise((resolve, reject) => {
            let {tools} = this;
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_TOPOLOGY_NODE_LIST",
                TYPE: $scope.topologyType.toString()
            }, (result) => {
                if (result) { resolve(result) }
                else { reject(result) }
            })
        });
    }

    requestLinks() {
        return new Promise((resolve, reject) => {
            let {tools} = this;
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_TOPOLOGY_LINK_LIST"
            }, (result) => {
                if (result) { resolve(result) }
                else { reject(result) }
            })
        });
    }

    loadNiaAgencyList() {
        let { $scope, tools } = this;
        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_NIA_AGENCY_LIST"
        }, function (result) {
            if (!result) return;
            let map = {};
            result.forEach((v,i) => {
                let array = map[v.node_id] || [];
                array.push(v);
                map[v.node_id] = array;
            });

            $scope.agencyList = { ...map };
        })
    }


    /** 알람리스트 로드 */
    loadNiaAlarmList(ticket) {
        if(!ticket) return Promise.resolve();

        let { $scope, tools } = this;
        return new Promise((resolve, reject) => {
            switch (ticket.ticket_type) {
                case 'RT':
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "SELECT_NIA_ALARM_LIST",
                        CLUSTERNO: ticket.cluster_no,
                    }, (result) => {
                        $scope.ticketRtAlarms = result;
                        $scope.ticketAlarmsType = 'RT';
                        // const [a0, a1] = result;
                        // a1 && (a1['related_alarm'] = true);
                        resolve(result)
                        setTimeout(()=> {
                            $('table.alarm-table.rt-alarm').on('scroll', this.resize.bind(this));
                        }, 1000);
                    });
                    break;
                case 'PF':
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "SELECT_NIA_CABLE_ALARM_LIST",
                        TICKET_ID: ticket.ticket_id,
                    }, (result) => {
                        $scope.ticketPFAlarms = result;
                        $scope.ticketAlarmsType = 'PF';
                        resolve(result)
                    });
                    break;
                case 'ATT2':
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "SELECT_NIA_ABNORMAL_TRAFFIC2_LIST",
                        TICKET_ID: ticket.ticket_id
                    }, (result) => {
                        $scope.ticketAbnormalTrafficAlarms = result;
                        $scope.ticketAlarmsType = 'ABNORMAL_TRAFFIC';
                        setTimeout(()=> {
                            $('table.alarm-table.traffic-abnormal-alarm').on('scroll', this.resize.bind(this));
                        }, 1000);
                        resolve(result);
                    });
                    break;
                case 'NTT':
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "SELECT_NIA_BAD_TRAFFIC2_LIST",
                        TICKET_ID: ticket.ticket_id
                    }, (result) => {
                        $scope.ticketBadTrafficAlarms = result;
                        $scope.ticketAlarmsType = 'BAD_TRAFFIC';
                        setTimeout(()=> {
                            $('table.alarm-table.traffic-bad-alarm').on('scroll', this.resize.bind(this));
                        }, 1000);
                        resolve(result);
                    });
                    break;
                case 'TRAFFIC':
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: ticket.ticket_rca_result_code == 'TRAFFIC_BAD_DETECTION' ? "SELECT_NIA_BAD_TRAFFIC_LIST" : "SELECT_NIA_ABNORMAL_TRAFFIC_LIST",
                        TICKET_ID: ticket.ticket_id
                    }, (result) => {
                        if(ticket.ticket_rca_result_code == 'TRAFFIC_BAD_DETECTION') {
                            $scope.ticketBadTrafficAlarms = result;
                            $scope.ticketAlarmsType = 'BAD_TRAFFIC';
                            setTimeout(()=> {
                                $('table.alarm-table.traffic-bad-alarm').on('scroll', this.resize.bind(this));
                            }, 1000);
                        } else {
                            $scope.ticketAbnormalTrafficAlarms = result;
                            $scope.ticketAlarmsType = 'ABNORMAL_TRAFFIC';
                            setTimeout(()=> {
                                $('table.alarm-table.traffic-abnormal-alarm').on('scroll', this.resize.bind(this));
                            }, 1000);
                        }

                        const [first] = result
                        if (first) {
                            $scope.zoomByAlarm(result[0]);
                        } else {
                            controller.map.resetZoom(750);
                        }
                        resolve(result);
                    });
                    break;
            }
        })
    }

    loadNiaCableAlarmList(ticket) {
        let controller = this
        let { $scope, tools } = this;
        return new Promise((resolve, reject) => {
            if ($scope.ticket.ticket_type == 'RT') {
                // controller.map.options.fileName = 'data_nia_ip_1_1.json';
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_TOPOLOGY_CABLE_LIST",
                    TICKET_ID: $scope.ticket.ticket_id
                }, (result) => {
                    const { root_cause_type, root_cause_sysnamea, root_cause_sysnamez } = $scope.ticket
                    let causeLinks = [];
                    let data = controller.map.data
                    let sysnamea = tools.convertNiaNodeId(root_cause_sysnamea);
                    let sysnamez = tools.convertNiaNodeId(root_cause_sysnamez);
                    if(result.length > 0) {
                        let temp = result.sort((a, b) => a.routenum - b.routenum).reduce((r, p, i) => {
                            if (i < result.length - 1) {
                                r.push({ source_id: p.sysname, target_id: (result[i + 1] || {}).sysname });
                            }
                            return r
                        }, []);
    
                        causeLinks = temp.map(item => {
                            let status = 0 
                            let link = data.links.find(v => v.source_id == item.source_id && v.target_id == item.target_id);
                            (sysnamea && item.source_id == sysnamea) && status--
                            (sysnamez && item.target_id == sysnamez) && status--
                            Object.assign(link, { status });
                            return link;
                        });
                    } else {
                        if (root_cause_type == "Linkcut") {
                            const links = data.links.filter(v =>
                                v.source_id == sysnamea && v.target_id == sysnamez ||
                                v.source_id == sysnamez && v.target_id == sysnamea)
                            links.forEach(link => {
                                link.status = -1
                            })
                            causeLinks = [...links]
                        }
                    }

                    causeLinks.sort((a, b) => a.status - b.status);
                    resolve(causeLinks[0]);
                });
            } else if ($scope.ticket.ticket_type == 'PF') {
                // controller.map.options.fileName = 'data_nia_ip_1_2.json';
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_PF_TOPOLOGY_CABLE_LIST",
                    TICKET_ID: $scope.ticket.ticket_id
                }, (result) => {
                    let data = controller.map.data
                    let temp = result.map(v => ({ source_id: tools.convertNiaNodeId(v.sysnamea), target_id: tools.convertNiaNodeId(v.sysnamez) }))
                    let causeLinks = temp.map(item => {
                        let link = data.links.find(v => v.source_id == item.source_id && v.target_id == item.target_id);
                        Object.assign(link, { status: 0 });
                        return link;
                    });
                    resolve(causeLinks[0])
                });
            } else {
                const { root_cause_type, root_cause_sysnamea, root_cause_sysnamez } = $scope.ticket;
                let causeLinks = [];
                let data = controller.map.data
                let sysnamea = tools.convertNiaNodeId(root_cause_sysnamea);
                let sysnamez = tools.convertNiaNodeId(root_cause_sysnamez);
                const links = data.links.filter(v =>
                    v.source_id == sysnamea && v.target_id == sysnamez ||
                    v.source_id == sysnamez && v.target_id == sysnamea);
                links.forEach(link => {
                    link.status = -1
                });
                causeLinks = [...links];
                resolve(causeLinks[0]);
            }
        })
    }

    changeAlarmFilter(e) {
        let {$scope} = this;

        let data = e.d;
        let param = [];
        if(!data && e.target_type == 'svg') {
        } else if(data && e.target_type == 'node') {
            // param.push(new RegExp(e.d.device_name.replace('\(', '\\\(').replace('\)', '\\\)')));
            // 1(IN) o , 2(PAOUT) x , 3(BAIN) x, 4(OUT) o
            param.push(
                new RegExp(data.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+\\(OUT\\)'),
                new RegExp(data.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+\\(IN\\)')
            );
        } else if(data && e.target_type == 'link') {
            let directiona = data.direction;
            let directionz = data.direction;

            // ROADM 송신 : 12 , 수신 : 14
            if(data.source.device_type == '0') {    // a측이 roadm 일경우
                directiona = '-12-';
            } else if(data.target.device_type == '0') {    // z측이 roadm 일경우
                directionz = '-14-';
            }

            param.push(
                new RegExp(data.source.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+' + directiona + '.+\\(OUT\\)'),
                new RegExp(data.target.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+' + directionz + '.+\\(IN\\)')
            )
        }
        angular.copy(param, $scope.alarmFilterParams);
        $scope.safeApply();
    }
    
    getLinkIdByCable(cable) {
        let {tools} = this;
        let data = angular.copy(tools.store.niaData);

        let a = cable.monitored_objecta.substring(0, (cable.monitored_objecta.indexOf('-Shelf0')));
        let z = cable.monitored_objectz.substring(0, (cable.monitored_objectz.indexOf('-Shelf0')));
        let aId = data.nodes.find(v => v.device_name == a).id;
        let zId = data.nodes.find(v => v.device_name == z).id;

        let link = data.links.find(v => v.source_id == aId && v.target_id == zId) || {};
        return link.id;
    }

    getRoaadmDataArray() {
        if("대구NIA"){
            var array = [
                {"S01": "MRPA-A"},

            ]
        }
    }

    /*changeAlarmFilter(e) {
        let {$scope} = this;

        let data = e.d;
        let param = [];
        if(!data && e.target_type == 'svg') {
        } else if(data && e.target_type == 'node') {
            // param.push(new RegExp(e.d.device_name.replace('\(', '\\\(').replace('\)', '\\\)')));
            // 1(IN) o , 2(PAOUT) x , 3(BAIN) x, 4(OUT) o
            param.push(
                new RegExp(data.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+\\(OUT\\)'),
                new RegExp(data.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+\\(IN\\)')
            );
        } else if(data && e.target_type == 'link') {
            let directiona = data.direction;
            let directionz = data.direction;

            // ROADM 송신 : 12 , 수신 : 14
            if(data.source.device_type == '0') {    // a측이 roadm 일경우
                directiona = '-12-';
            } else if(data.target.device_type == '0') {    // z측이 roadm 일경우
                directionz = '-14-';
            }

            param.push(
                new RegExp(data.source.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+' + directiona + '.+\\(OUT\\)'),
                new RegExp(data.target.device_name.replace('\(', '\\\(').replace('\)', '\\\)') + '.+' + directionz + '.+\\(IN\\)')
            )
        }
        angular.copy(param, $scope.alarmFilterParams);
        $scope.safeApply();
    }*/

}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', Map3dCtrl];
