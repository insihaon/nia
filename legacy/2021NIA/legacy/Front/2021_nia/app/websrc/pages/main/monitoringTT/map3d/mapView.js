if (typeof Array.prototype.find == "undefined") {
    Array.prototype.find = function (fnEquals) {
        var idx = this.findIndex(fnEquals);
        if (idx > 0 && idx < this.length) return this[idx];
        else return null;
    }
}

if (typeof Array.prototype.clearAll == "undefined") {
    Array.prototype.clearAll = function() {
        if (this.length > 0) {
            this.splice(0, this.length);
        }
    }
}

if (typeof Array.prototype.clone == "undefined") {
    Array.prototype.clone = function(source) {
        this.clearAll();
        this.concat(JSON.parse(JSON.stringify(source)));
    }
}

if (typeof Array.prototype.copy == "undefined") {
    Array.prototype.copy = function(source) {
        this.clearAll();
        this.push(...source);
    }
}

if (typeof String.prototype.substrFindAfter == "undefined") {
    String.prototype.substrFindAfter = function(str, bLast = false) {
        var index = bLast ? this.lastIndexOf(str) : this.indexOf(str);
        return this.substring(index + 1);
    };
}

if (typeof String.prototype.substrFindBefore == "undefined") {
    String.prototype.substrFindBefore = function(str, bLast = true) {
        var index = bLast ? this.lastIndexOf(str) : this.indexOf(str);
        index = index < 0 ? this.length : index;
        return this.substring(0, index);
    };
}

(function (global) {

    global.MapView = (function () {

        let instance;
        let ticket, topology, topologyControl, drawing, $scope, tools, events;
        let allLayers, crossLinks, arrFdfLocation;
        let $log = console;
        let topologyDataHistory = [];
        let historyIndex = 0;
        let lastHistoryIndex = -1;
        let layerWidth = 2000;
        let layerHeight = 1000;
        let coodsRange = {xMax: layerWidth / 2 - 300, yMax: layerHeight / 2 - 200}
        let defaultOfficeName;
        let ticks = {};
        coodsRange.xMin = -coodsRange.xMax;
        coodsRange.yMin = -coodsRange.yMax;
        coodsRange.width = coodsRange.xMax * 2;
        coodsRange.height = coodsRange.yMax * 2;

        function startTick(name = 'known') {
            ticks[name] = new Date();
        }

        function endTick(name = 'known') {

            if(!ticks[name]) return;

            function logStack(newErr)
            {
                var stackIndex = 1;
                var trace;
                if(navigator.userAgent.indexOf("Firefox") != -1)
                {
                    trace = newErr.stack.split('\n')[stackIndex + 0]
                }else if(navigator.userAgent.indexOf("Chrome") != -1)
                {
                    trace = newErr.stack.split('\n')[stackIndex + 1]
                }
                return `\n` + trace;
            }

            try {
                var tick = Math.round((new Date().getTime() - ticks[name].getTime()));
                console.log(`TickCheck '${name}' Tick=${tick} ms`, logStack(new Error()));
            } catch (e) {
            }
        }

        function log(msg, item, err) {
            // if ($scope.debug) {
            //     $log.error(msg, item, err);
            // }
        }

        function initialize(parameters) {

            topology = parameters.topology;
            topologyControl = parameters.topologyControl;
            $scope = parameters.$scope;
            tools = parameters.tools;
            events = parameters.events || {};
            ticket = parameters.ticket;
            defaultOfficeName = tools.store.hasProfile('lte') ? null : '가입자';

            if (!topology) {
                createTopology();
            }

            allLayers = $scope.allLayers = [
                {id: 0, position: [0, 0, 0], name: 'Fiber', alias:'Fiber', domain: tools.constants.EquipType.FDF.code, color: '', opacity: 0.2},
                {id: 1, position: [0, 300, 0], name: 'ROADM', alias:'ROADM', domain: tools.constants.EquipType.ROADM.code, color: '#FFFF99', opacity: 0.2},
                {id: 2, position: [0, 600, 0], name: 'OTN', alias:'POTN', domain: tools.constants.EquipType.POTN.code, color: '#00B050', opacity: 0.2},
                {id: 3, position: [0, 900, 0], name: 'PTN', alias:'PTN', domain: tools.constants.EquipType.PTN.code, color: '#00B050', opacity: 0.2},
                {id: 4, position: [0, 1200, 0], name: 'PCM', alias:'PCM', domain: tools.constants.EquipType.MSPP.code, color: '#00B0F0', opacity: 0.2},
                {id: 5, position: [0, 1500, 0], name: 'MSPP', alias:'MSPP', domain: tools.constants.EquipType.MSPP.code, color: '#00B0F0', opacity: 0.2},
                {id: 6, position: [0, 1800, 0], name: 'IP', alias:'IP', domain: 'IP', color: '#fff9fc', opacity: 0.2, layout: 'none'},
                {id: 7, position: [0, 2100, 0], name: 'LTE', alias:'LTE', domain: 'LTE', color: '#fff9fc', opacity: 0.2},
            ];

            if (tools.store.hasProfile('oper') == true) {
                allLayers.splice(6, 2);
            }
        }

        function getLayer(equipType) {

            try {
                var equipType = equipType || "";
                var layer = null;
                const layers = topology.getLayers();

                var type = tools.constants.EquipType.prototype.getType(equipType, false);
                if (type && type.layer) {
                    layer = topology.findLayer(type.layer);;
                }

                if (!layer) {
                    if (equipType.startsWith(tools.constants.EquipType.DU.code) || equipType.startsWith(tools.constants.EquipType.RU.code)) {
                        layer = topology.findLayer(7);
                    } else if (equipType == tools.constants.EquipType.DAS.code || equipType == tools.constants.EquipType.LOC_U9500H.code || equipType == tools.constants.EquipType.CSC_CATALYST6509_E.code || equipType == tools.constants.EquipType.ACT_7750SR_12.code) {
                        layer = topology.findLayer(6);
                    } else {
                        layer = layers.find((l) => (equipType).startsWith(l.element.parameters.domain.substr(0, 3)));
                    }
                }

            } catch (err) {
                throw `장비타입(${equipType})에 맞는 레이어를 찾을 수 없습니다. `;
            }

            return layer;
        }

        function getNodeType(equipType = '', subType) {

            let nodeType = vis.nodeType.EquipNode;

            var type = tools.constants.EquipType.prototype.getType(equipType);
            if (type && type.nodeType) {
                nodeType = type.nodeType
            } else if (equipType.startsWith(tools.constants.EquipType.FDF.code)) {

                if (tools.store.getMode('op_3d_simple_image')) {
                    nodeType = vis.nodeType.FDFNode;
                } else {
                    nodeType = vis.nodeType.FDFNode_Real;
                }

            } else if (equipType.startsWith(tools.constants.EquipType.ROADM.code)) {

                if (tools.store.getMode('op_3d_simple_image')) {
                    nodeType = vis.nodeType.ROADMNNode;
                } else {
                    // 중계기인지 아닌지 어떻게 알지?
                    // UNI 일 경우 모두 단국장치
                    let usage = '_T'; //단국장치(Terminal) or 중계기(Repeater)
                    if (ticket.root_cause_code !== 'UNI' && 'REPEATER' == subType) {
                        //$log.log('is_repeater : ' + data.sysnamea );
                        usage = '_R';
                    }

                    type = tools.constants.EquipType.prototype.getType(equipType + usage);
                    if (type && type.nodeType) {
                        nodeType = type.nodeType
                    } else {
                        nodeType = equipType + usage;
                    }

                }
            } else if (equipType.startsWith(tools.constants.EquipType.PTN.code)) {

                if (tools.store.getMode('op_3d_simple_image')) {
                    nodeType = vis.nodeType.PTNNode;
                } else {
                    nodeType = vis.nodeType.PTNNode_Real;
                }

            } else if (equipType.startsWith(tools.constants.EquipType.MSPP.code)) {

                if (tools.store.getMode('op_3d_simple_image')) {
                    nodeType = vis.nodeType.MSPPNode;
                } else {
                    nodeType = vis.nodeType.MSPPNode;
                }
            }
            return nodeType;
        };

        function createTopology() {

            // 토폴로지 기본생성
            THREE.Topology.Layer = {
                width: 2000,
                height: 1000,
                plane_height: 300,
                color: 0x505050,
                opacity: 0.5,
            };

            THREE.Topology.Node = {
                margin_bottom: 5,
                color: 0xff0000
            };

            THREE.Topology.Link = {
                color: 0x559121,
                opacity: 1,
                alarmSize: 30,
                alarmSpeed: 0.02
            };

            vis.loadObject()
                .then(() => {
                    createDrawing();
                    if (events.onLoadComplete) {
                        // topology.setData({layers});
                        events.onLoadComplete.call(null, instance);
                    }
                });
        }

        function createDrawing() {

            function clearTooltipTimer() {
                clearTimeout($scope.tooltipTimer);
                $scope.tooltipTimer = null;
            }

            function hideTooltip() {
                popup.style.display = 'none';
                popup.innerHTML = "";
            }

            let componentWapperId = 'componentWapperId';
            let backgroundElementId = 'backgroundElementId';
            let topologyElementId = 'topologyElementId';
            let popupElementId = 'popupElementId';

            drawing = instance.drawing = $scope.drawing = vis.drawing = new vis.DrawControl({
                background: {
                    visible: false,
                    container: backgroundElementId,
                    back_src: 'assets/background.png!',
                    front_src: 'assets/backMeteors.png!',
                },
                topology: {
                    visible: true,
                    container: topologyElementId,
                    popupcontainer: popupElementId,
                    object_selection: true
                }
            });
            drawing.render();

            topologyControl = instance.topologyControl = $scope.topologyControl = drawing.topologyControl;
            topology = instance.topology = $scope.topology = topologyControl.getTopology();
            if ($scope.topologyControl != null) {

                try {

                    var w = topologyControl.element_control.domElement.width;
                    var h = topologyControl.element_control.domElement.height;

                    var wh = w / h;
                    var fullHd = 1092 / 1080;
                    if (wh > fullHd) {
                        var poistion = global.CONFIG.position;
                        var z = (poistion.z * wh) / fullHd;
                        topologyControl.controls.object.position.copy(new THREE.Vector3(0, 800, z));
                    }
                } catch (e) {
                }

                var popup = document.getElementById(popupElementId);
                topologyControl.addEventListener('mousedown', function (e) {
                    // $log.log('main ' + e.type, e);
                });
                topologyControl.addEventListener('mouseup', function (e) {
                    //$log.log('main ' + e.type, e);
                });
                topologyControl.addEventListener('object_selected', function (e) {
                    // $log.log('main ' + e.type, e);
                });
                topologyControl.addEventListener('object_clicked', function (e) {
                    // $log.log('main ' + e.type, e);
                    if (e.selected && e.selected.element instanceof vis.Node) {
                        console.log('main ' + e.type, e.selected.element);
                        window.selected = e.selected.element;
                    }
                });
                topologyControl.addEventListener('element_over', function (e) {
                    //$log.log('main ' + e.type, e);

                    clearTooltipTimer();

                    if (e.selected != null) {
                        popup.style.display = 'block';
                        popup.style.left = (e.event.clientX + 20) + "px";
                        popup.style.top = (e.event.clientY + 20) + "px";
                        //popup.style.left = (e.event.layerX + 20) + "px";
                        //popup.style.top = (e.event.layerY + 100) + "px";
                        popup.style.zIndex = 9999;

                        let selectedElement = e.selected.element;
                        if (!selectedElement) {
                            selectedElement = e.selected.parent.element;
                        }

                        if (selectedElement instanceof vis.Node) {
                            let html = makeNodeTooltip(selectedElement);
                            popup.innerHTML = html;
                        } else if (selectedElement instanceof vis.Link) {
                            let html = makeLinkTooltip(selectedElement);
                            popup.innerHTML = html;
                        } else if (selectedElement) {
                            popup.innerHTML = selectedElement.toString().replace(/\n/gi, '<br>');
                        }

                    } else {

                        $scope.tooltipTimer = setTimeout(function () {
                            hideTooltip();
                        }, 200);

                        popup.onmouseover = function () {
                            clearTooltipTimer();

                            popup.onmouseleave = function () {
                                hideTooltip();
                            };
                        }
                    }
                });
                topologyControl.addEventListener('element_clicked', function (e) {
                    // $log.log('main ' + e.type, e);
                    if (e.selected != null) {
                        if (e.selected.element instanceof vis.Node) {
                            $scope.selected_element = e.selected.element;
                            return;
                        }
                    }
                });
                topologyControl.addEventListener('element_db_clicked', function (e) {
                    //$log.log('main ' + e.type, e);
                    if (e.selected != null) {
                        if (e.selected.element instanceof vis.Node) {
                            e.selected.element.toggleVisibleTag('CT');
                            $scope.selected_element = e.selected.element;
                            return;
                        }
                    }
                });
                topologyControl.addEventListener('element_contextmenu', function (e) {
                    $log.log('main ' + e.type, e);
                    window.selected = e.selected.element;

                    hideTooltip();

                    if (e.selected != null) {
                        if (e.selected.element instanceof vis.Node) {
                            $scope.onOpenMenu(e.event, e.selected.element);
                        }
                    }
                });
                topologyControl.addEventListener('element_down', function (e) {
                    //$log.log('main ' + e.type, e);
                });
                topologyControl.addEventListener('element_up', function (e) {
                    //$log.log('main ' + e.type, e);
                });

                //$scope.loadTopology();
                //$scope.topology.setData(vis.topology_save_data);
            }

            // var testControl = new vis.TestControl({container: 'testcontainer'});
            // testControl.animation();

            // var mountingControl = new vis.MountingControl({container: 'mountingcontainer'});
            // mountingControl.animation();

            let makeNodeTooltip = (element) => {

                let node = element;
                let html = '';
                html += '국사 : ' + node.parameters.office_name + '(' + node.parameters.office_code + ')<br>';
                html += '장비명 : ' + tools.toString(node.name) + '<br>';
                html += '설치위치정보 : ' + tools.toString(node.parameters.location) + '<br>';
                let alarm = '';

                if(node.alarmType) {
                    if (node.parameters.related_alarm) {
                        alarm = '<span style="font-weight: bold; color: red">근원장애</span>';
                    } else if (node.alarmType > 1) {
                        alarm = '<span style="color: #ff8c00">파생장애</span>';
                    } else {
                        alarm = '정상';
                    }
                }

                //if(!$scope.isLinkAlarm() &&(node.rootalarmgb || 0) > 0){
                //    alarm = '<span style="font-weight: bold; color: red">근원장애</span>';
                //}

                html += '상태 : ' + alarm + '<br>';
                html += '장애수: ' + (node.alarmCount || 0) + '<br>';

                let connect = '';
                if (node.links) {
                    node.links.forEach(function (link) {
                        var name = link.name || '';
                        if (name.length > 0) {
                            var style = link.getAlarmType() > 1 ? "style='color:red;'" : "";
                            connect += '<br><span ' + style + '>&nbsp;&nbsp;-' + name + '</span>';
                        }
                    })
                }
                if (connect.length > 0)
                    html += '연결정보: ' + connect + '<br>';

                html += `좌표 : x=${Math.floor(node.three.position.x)}, z=${Math.floor(node.three.position.z)}, fixed=${Math.floor(node.fixed)}<br>`;

                if ($scope.debug) {
                    let debegHtml = '';
                    debegHtml += '[DEBUG]<br>';
                    debegHtml += 'UUID : ' + node.uuid + '<br>';
                    debegHtml += 'ID : ' + node.id + '<br>';
                    debegHtml += '장비타입 : ' + node.parameters.equiptype + '<br>';
                    debegHtml += 'SLOT : ' + node.parameters.slot + '<br>';
                    debegHtml += 'MSPPCOT : ' + node.parameters.msppcot + '<br>';
                    debegHtml += '<hr>';
                    html = debegHtml + html;
                    // console.log(html.replace(/(<br>|<hr>)/gi, '\r\n'));
                }

                return html;
            }

            let makeLinkTooltip = (element) => {

                let link = element;
                let html = '';
                html += '광케이블명 : ' + link.parameters.line_alias + '<br>';

                let alarm = "정상";
                if((link.parameters.rank || 5) < 2){
                    alarm = '<span style="font-weight: bold; color: red">' + link.getAlarmTextHtml() + '</span>';
                } else {
                    alarm = '<span style="font-weight: bold; color: orange">' + link.getAlarmTextHtml() + '</span>';
                }
                html += '상태 : ' + alarm + '<br>';
                html += 'Site A: ' + link.source.element.name + ($scope.debug ? 'desc=' + link.source.element.desc : '') + (link.parameters.porta ? (' #' + link.parameters.porta) : '') + '<br>';
                html += 'Site Z: ' + link.target.element.name + ($scope.debug ? 'desc=' + link.target.element.desc : '') + (link.parameters.portz ? (' #' + link.parameters.portz) : '') + '<br>';

                if ($scope.debug) {
                    let debegHtml = '';
                    debegHtml += '링크ID : ' + link.id + '<br>';
                    debegHtml += 'tcseq : ' + link.parameters.transcircuitseq + '<br>';
                    debegHtml += 'key : ' + link.parameters.link_key + '<br>';
                    debegHtml += 'sysnamea : ' + link.parameters.sysnamea + '<br>';
                    debegHtml += 'porta : ' + link.parameters.porta + '<br>';
                    debegHtml += 'sysnamez : ' + link.parameters.sysnamez + '<br>';
                    debegHtml += 'portz : ' + link.parameters.portz + '<br>';
                    debegHtml += '<hr>';
                    html = debegHtml + html;
                    // console.log(html.replace(/(<br>|<hr>)/gi, '\r\n'));
                }

                return html;
            }
        }

        function beginWaitting() {
            $scope.safeApply(() => {
                $scope.isProgressHide = false;
                $scope.$apply();
            });
        }

        function endWaitting() {
            setTimeout(() => {
                $scope.isProgressHide = true;
                $scope.$apply();
            });
        }

        function moveHistoryIndex(offset) {
            let tempIndex = offset == 0 ? lastHistoryIndex : historyIndex + offset;

            if (tempIndex > 0 && tempIndex <= topologyDataHistory.length) {
                lastHistoryIndex = historyIndex;
                historyIndex = tempIndex;

                console.log("historyIndex=" + historyIndex + ", lastHistoryIndex=" + lastHistoryIndex + ", length=" + topologyDataHistory.length);
                return true;
            }

            return false;
        }

        function prev() {
            if (moveHistoryIndex(-1)) reloadTopology();
        }

        function next() {
            if (moveHistoryIndex(1)) reloadTopology();
        }

        function toggle() {
            let result = false;
            if (topologyDataHistory.length == historyIndex) {
                result = moveHistoryIndex(-1);
            } else {
                result = moveHistoryIndex(0);
            }

            if (result) reloadTopology();
        }

        function reloadTopology() {
            if (historyIndex > 0 && historyIndex <= topologyDataHistory.length) {
                let data = topologyDataHistory[historyIndex - 1];
                loadTopology(null, data);
            }
        }

        function loadTopology(newTicket = null, topologyData = null, saved = null) {

            beginWaitting();

            let deferred = $.Deferred();
            let {TicketType, TicketResultCode, LinkAlarmPrediction} = tools.constants;
            let reload = !!topologyData;

            if(newTicket) ticket = newTicket;

            if (topologyData && topologyData.sqllog) {
                console.log(topologyData.sqllog);
            }

            let fn_load_saved = ((result) => {

                loadJson(false, result);
                final();
                return;

            });

            let fn_load_db = ((result) => {

                // if (global.isTestLib) {
                //     console.table(result.LIST);
                //     console.table(result.CROSS_LINK);
                // }

                if (global.isTestLib && result.TICKET) {
                    ticket = result.TICKET;
                }

                topology.clear();

                let topologyData = {layers: allLayers};
                topology.setData(topologyData);

                let nodes = links = [];

                let list = $scope.links = result.LIST;
                arrFdfLocation = [];

                startTick();
                let x = 0;
                list.forEach((item) => {

                    //<editor-fold desc="[# A Node 추가]">
                    {
                        var left;
                        try {
                            var layer = getLayer(item.equiptypea || item.equiptypez);
                            if (layer) {

                                if (item.PROP_EQUIP_LEFT) {
                                    let propArr = item.PROP_EQUIP_LEFT;
                                    for (var i = 0; i < propArr.length; i++) {

                                        try {
                                            let prop = propArr[i];
                                            if (prop && prop.prop_sysname) {

                                                var added = addNode({
                                                    layer_id: layer.element.id,
                                                    position: [(x) * 400, (x++) * 400],
                                                    id: prop.prop_sysname,
                                                    equiptype: prop.prop_equiptype,
                                                    type: getNodeType(prop.prop_equiptype, prop.prop_namecode),
                                                    msppcot: false,
                                                    name: prop.prop_sysname,
                                                    desc: prop.officename,
                                                    office_code: prop.officescode,
                                                    office_name: prop.officename,
                                                    location: prop.instlocation,
                                                    alarmlevel: item.alarm_levela,
                                                    related_alarm: false,
                                                });
                                                //var alarm = covertAlarmLevelToColor(item.alarm_levela, 'int');
                                                //added.element.setAlarm(alarm, alarm > 1);

                                                if (i == propArr.length - 1) {
                                                    left = added.element;
                                                } else if (i > 0) {
                                                    let link = addLink(propArr[i - 1].prop_sysname, prop.prop_sysname);
                                                }
                                            }
                                        }
                                        catch (err) {
                                            log(`PROP_EQUIP_LEFT 추가 실패`, item, err);
                                        }
                                    }
                                }

                                if (item.sysnamea) {
                                    var added = addNode({
                                        layer_id: layer.element.id,
                                        position: [(x) * 400, (x++) * 400],
                                        id: item.sysnamea,
                                        equiptype: item.equiptypea,
                                        type: getNodeType(item.equiptypea),
                                        msppcot: item.msppcota,
                                        name: item.sysnamea,
                                        desc: item.officenamea || defaultOfficeName,
                                        office_code: item.officecodea,
                                        office_name: item.officenamea,
                                        location: item.instlocationa,
                                        instdate: item.instdatea,
                                        alarmlevel: item.alarm_levela,
                                        related_alarm: item.related_alarma == true,
                                        slot: item.slota ? [item.slota] : [],
                                        port: item.porta,
                                        domain: item.domaina,
                                        alarm_type: item.alarm_typea,
                                        alarmmsg: item.alarm_msga,
                                        alarmloc: item.alarm_loca,
                                        alarmpos: item.roadm_alarm_posa || item.alarm_posa,
                                        direction: vis.linkDirection.A,
                                    });

                                    var alarm = covertAlarmLevelToColor(item.alarm_levela, 'int');
                                    added.element.setAlarm(alarm, alarm > 1);

                                    if (left) {
                                        let link = addLink(left.id, added.element.id);
                                    }
                                }

                                var equiptypea = item.equiptypea || '';
                                if (equiptypea.startsWith(tools.constants.EquipType.FDF.code)) {
                                    arrFdfLocation.push(item.instlocationa);
                                }
                            }
                        }
                        catch (err) {
                            log(`A노드 추가 실패`, item, err);
                        }
                    }
                    //</editor-fold>

                    //<editor-fold desc="[# Z Node 추가]">
                    {
                        try {
                            var left;
                            var layer = getLayer(item.equiptypez || item.equiptypea);
                            if (item.sysnamez) {

                                var added = addNode({
                                    layer_id: layer.element.id,
                                    position: [0, 0],//[Math.floor((Math.random() * 1800) - 900), 0],
                                    id: item.sysnamez,
                                    equiptype: item.equiptypez,
                                    type: getNodeType(item.equiptypez),
                                    msppcot: item.msppcotz,
                                    name: item.sysnamez,
                                    desc: item.officenamez || defaultOfficeName,
                                    office_code: item.officecodez,
                                    office_name: item.officenamez,
                                    location: item.instlocationz,
                                    instdate: item.instdatez,
                                    alarmlevel: item.alarm_levelz,
                                    related_alarm: item.related_alarmz == true,
                                    slot: item.slotz ? [item.slotz] : [],
                                    port: item.portz,
                                    domain: item.domainz,
                                    alarm_type: item.alarm_typez,
                                    alarmmsg: item.alarm_msgz,
                                    alarmloc: item.alarm_locz,
                                    alarmpos: item.roadm_alarm_posz || item.alarm_posz,
                                    direction: vis.linkDirection.Z
                                });

                                var alarm = covertAlarmLevelToColor(item.alarm_levelz, 'int');
                                added.element.setAlarm(alarm, alarm > 1);
                                left = added.element;
                            }

                            var equiptypez = item.equiptypez || '';
                            if (equiptypez.startsWith(tools.constants.EquipType.FDF.code)) {
                                arrFdfLocation.push(item.instlocationz);
                            }

                            if (item.PROP_EQUIP_RIGHT) {
                                let propArr = item.PROP_EQUIP_RIGHT;
                                for (var i = 0; i < propArr.length; i++) {
                                    let prop = propArr[i];
                                    if (prop && prop.prop_sysname) {

                                        try {
                                            var added = addNode({
                                                layer_id: layer.element.id,
                                                position: [(x) * 400, (x++) * 400],
                                                id: prop.prop_sysname,
                                                equiptype: prop.prop_equiptype,
                                                type: getNodeType(prop.prop_equiptype, prop.prop_namecode),
                                                msppcot: false,
                                                name: prop.prop_sysname,
                                                desc: prop.officename,
                                                office_code: prop.officescode,
                                                office_name: prop.officename,
                                                location: prop.instlocation,
                                                alarmlevel: false
                                            });

                                            //var alarm = covertAlarmLevelToColor(item.alarm_levela, 'int');
                                            //added.element.setAlarm(alarm, alarm > 1);

                                            {
                                                let link = addLink(left.id, added.element.id);
                                                left = added.element;
                                            }
                                        }
                                        catch (err) {
                                            log(`PROP_EQUIP_RIGHT 추가 실패`, item, err);
                                        }
                                    }
                                }
                            }
                        }
                        catch (err) {
                            log(`Z노드 추가 실패`, item, err);
                        }
                    }
                    //</editor-fold>
                });
                endTick();

                updateUnit();

                // <editor-fold desc="[# CLOUD 추가]">
                // 560340
                if (topology.getNodes().length == 1) {
                    let node = topology.getNodes()[0];

                    if (isPtnBitsLos() || isPtnLanDown()) {
                        var name = isPtnLanDown() ? 'MIH' : 'DOTS';
                        var param = {
                            layer_id: node.element.layer.id,
                            position: [0, 0],
                            id: 'CLOUD',
                            type: vis.nodeType.CloudNode,
                            // type: vis.nodeType.EquipNode,
                            color: 0x7F7F7F,
                            desc: '<strong style="font-size:20px;">' + name + '</strong>',
                            visible: true
                        };

                        topology.addNode(param);
                        topology.addLink(node.element.id, 'CLOUD');
                    }
                }

                // <!--</editor-fold desc="[# CLOUD 추가]">

                //<editor-fold desc="# 링크추가 및 노드/링크 장애 표현">
                let linkMap = new Map();
                list.forEach(((item, index) => {

                    function showPowerDown(isPowerDown, me, you) {

                        var show = false;
                        if ((me.parameters.msppcot == false && you.parameters.msppcot == true)
                            || (/^[a-z]{2}/i.test(me.parameters.name) && /(R1|R2|R3)/g.test(you.parameters.name))
                        ) {

                            if (isPowerDown) {
                                me.overlayIcon(vis.Node.overlayType.PowerFail, 2);
                            }
                            show = true;
                        }
                        return show;
                    }

                    try {
                        //if (!item.line_alias)
                        //    return;

                        let link, oldAlarrmLevel = 0;

                        let from = `${item.sysnamea}#${item.porta}`;
                        let to = `${item.sysnamez}#${item.portz}`;
                        if (from < to) {
                            var temp = from;
                            from = to;
                            to = temp;
                        }
                        let key = `${from}#${to}#${item.line_alias}`;

                        if(item.gubun.startsWith('LTE') && !key && item.line_alias){
                            // item.line_alias 조건 만으로 검색해서 키 값으로 사용
                            let arr = Array.from(linkMap.keys()).filter(n => n.includes(item.line_alias));
                            if(arr.length > 0) {
                                key = arr[0];
                            }
                        }

                        link = linkMap.get(key);
                        let newLink = false;

                        if (!link) {
                            item.link_key = key;
                            link = addLink(item.sysnamea, item.sysnamez, item);
                            linkMap.set(key, link);
                            newLink = true;
                        } else {
                            oldAlarrmLevel = Math.max(link.element.parameters.alarm_levela || "0", link.element.parameters.alarm_levelz || "0");
                        }

                        if (!link) {
                            return true;
                        }

                        let alarmLevel = Math.max(item.alarm_levela || 0, item.alarm_levelz || 0, oldAlarrmLevel);
                        let alarm = covertAlarmLevelToColor(alarmLevel, 'array');
                        let rootCauseType = ticket.root_cause_type;

                        // <editor-fold desc="[# 링크포트 및 전원차단 아이콘]">
                        if (!oldAlarrmLevel && item.gubun != 'FDF' && alarm && link.element) {

                            let porta = item.porta || '';
                            let portz = item.portz || '';

                            let source = link.element.source.element;
                            let target = link.element.target.element;
                            let isPowerDown = isPtnPowerDown();

                            if(!showPowerDown(isPowerDown, source, target)){
                                porta = porta + ' LOS';
                            }

                            if(!showPowerDown(isPowerDown, target, source)){
                                portz = portz + ' LOS';
                            }

                            let options = {
                                fontsize: 22,
                                marginX: 10,
                                marginY: 10,
                                backgroundColor: {r: 255, g: 0, b: 0, a: 0.5},
                                borderColor: {r: 0, g: 0, b: 0, a: 0},
                                fontcolor: {r: 255, g: 255, b: 255, a: 1.0}
                            };
                            link.element.setTag(porta, vis.linkDirection.A, options);
                            link.element.setTag(portz, vis.linkDirection.Z, options);
                        }
                        // <!--</editor-fold desc="[# 링크포트 및 전원차단 아이콘]">

                        // <editor-fold desc="[# 링크 장애 및 alias]">
                        if (alarm && link.element && TicketType.prototype.includes([TicketType.CableCut, TicketType.Linkcut], rootCauseType)) {

                            let {ticket} = $scope;
                            let tc_list = $scope.ticket.tc_list || {};
                            if (TicketType.prototype.includes([TicketType.CableCut], rootCauseType) && (ticket && tc_list)) {

                                if (tc_list && link.source.element.layer.domain == tools.constants.EquipType.FDF.code || link.target.element.layer.domain == tools.constants.EquipType.FDF.code) {

                                    var tc = tc_list.find((tc) => tc.ocaseq == item.transcircuitseq);
                                    if (tc && tc.rank_number) {
                                        let rank = tc.rank_number >= 5 ? 5 : tc.rank_number;
                                        link.element.setAlarmText(LinkAlarmPrediction[rank]);
                                        link.element.parameters.rank = rank;
                                    }
                                    // else if (alarm[0] >= 7) {
                                    //     link.element.setAlarmText("근원장애");
                                    // }
                                }
                            }

                            if (alarm[0] >= 7) {
                                link.element.setAlarm(alarm[3], 0xF12E2E, true);
                            } else if (alarm[0] > 1) {
                                link.element.setAlarm(alarm[3], 0XFFFF00, false);
                            } else {
                                link.element.setAlarm(alarm[3], 0XFFFF00, false);
                            }
                        }

                        // link.element.setAliasText(item.line_alias);
                        // language=HTML
                        link.element.setAliasText(item.line_alias);

                        // <!--</editor-fold desc="[# 링크 장애 및 alias]">
                    }
                    catch (err) {
                        log(`링크 추가 실패`, item, err);
                    }
                }).bind(this));
                //</editor-fold>

                //<editor-fold desc="[# 크로스 링크 추가]">
                crossLinks = result.CROSS_LINK || [];
                crossLinks.forEach((link, i) => {

                    function push(array, node) {
                        array = array || [];

                        if (!array.includes(node)) {
                            array.push(node);
                        }

                        return array;
                    }

                    try {
                        let nodea = topology.findNode(link.sysnamea);
                        let nodez = topology.findNode(link.sysnamez);

                        if (nodea && nodez) {
                            let link = addLink(nodea.id, nodez.id, {}, true);
                            // if (link && $scope.debug) {
                            //     link.element.setAlarmText(i + 1);
                            // }

                            nodea.element.parameters.cross_location = nodez.element.parameters.location;
                            nodez.element.parameters.cross_location = nodea.element.parameters.location;

                            let up, down;
                            if (nodea.element.layer.id > nodez.element.layer.id) {
                                up = nodea.element;
                                down = nodez.element;
                            } else {
                                up = nodez.element;
                                down = nodea.element;
                            }

                            down.parameters.cross_up_nodes = push(down.parameters.cross_up_nodes, up);
                            up.parameters.cross_down_nodes = push(up.parameters.cross_down_nodes, down);
                        }
                    }
                    catch (err) {
                        log(`크로스 링크 추가 실패`, item, err);
                    }
                });
                //</editor-fold>

                // x 좌표 보정
                arrange();

                // Node Filter
                filter();

                //topologyData.nodes = nodes;
                //topologyData.links = links;
                final();

            }).bind(this);

            function final(success = true) {

                endWaitting();

                if (success) {
                    deferred.resolve();
                } else {
                    deferred.reject();
                }
            }

            function callback(result) {

                if (result.error) {

                    if($scope.debug)
                        tools.showAlert('토폴로지 요청 에러', result.error);

                    console.error(result.error);
                    final(false);
                    return;
                }

                if (!reload) {

                    let index = Math.max(historyIndex, lastHistoryIndex);
                    topologyDataHistory.splice(index, topologyDataHistory.length - index, result);
                    historyIndex = topologyDataHistory.length;
                    lastHistoryIndex = historyIndex - 1;
                    // console.log("historyIndex=" + historyIndex + ", lastHistoryIndex=" + lastHistoryIndex + ", length=" + topologyDataHistory.length);
                }


                if (result) {

                    let bReset = global.topology_reset == undefined ? true : global.topology_reset;
                    if(bReset) topologyControl.reset();

                    if (result.IS_SAVED) {
                        fn_load_saved.call(null, result);
                    } else {
                        fn_load_db.call(null, result);
                    }
                }
            }

            try {
                var ticketId = ticket.ticket_id * 1;
                if (!topologyData) {
                    tools.http({
                        service: tools.constants.Service.rca,
                        action: tools.constants.Action.REQUEST_3D_TOPLOGY,
                        TICKET_ID: ticketId,
                        CLUSTER_NO: ticket.cluster_no,
                        LTE: tools.store.hasProfile('lte') ? true : null,
                        // array: [{"name": "1", "size":"2"}, {"name": "3", "size":"4"}],
                        // object: {"key": "value", "k":"v"},
                        saved :  saved != null ? saved.toString() : saved // null: auto, true:저장된맵, false:장애DB
                    }, callback);
                } else {
                    callback(topologyData);
                }
            } catch (e) {
            }
            return deferred.promise();
        }

        function isUnitFail() {
            let {TicketType} = tools.constants;
            return TicketType.prototype.includes([TicketType.UnitFail], ticket.root_cause_type);
        }

        function isNodeFail() {
            let {TicketType} = tools.constants;
            return TicketType.prototype.includes([TicketType.NodeFail], ticket.root_cause_type);
        }

        function isPtnFanHighTemp() {
            let {TicketResultCode} = tools.constants;
            let resultCode = ticket.ticket_rca_result_code;
            return TicketResultCode.prototype.includes(TicketResultCode.PTN_FAN_HIGH_TEMP, resultCode);
        }

        function isPtnPowerDown() {
            let {TicketResultCode} = tools.constants;
            let resultCode = ticket.ticket_rca_result_code;
            return TicketResultCode.prototype.includes(TicketResultCode.PTN_PWR_DOWN, resultCode);
        }

        function isPtnBitsLos() {
            let {TicketResultCode} = tools.constants;
            let resultCode = ticket.ticket_rca_result_code;
            return TicketResultCode.prototype.includes(TicketResultCode.PTN_BITS_LOS, resultCode);
        }

        function isPtnLanDown() {
            let {TicketResultCode} = tools.constants;
            let resultCode = ticket.ticket_rca_result_code;
            return TicketResultCode.prototype.includes(TicketResultCode.PTN_LAN_DOWN, resultCode);
        }

        function isPtnMultiFabricFail() {
            let {TicketResultCode} = tools.constants;
            let resultCode = ticket.ticket_rca_result_code;
            return TicketResultCode.prototype.includes(TicketResultCode.PTN_UNIT_MUL_MT_FABRIC_FAIL, resultCode);
        }

        function addNode(newNodeParameters) {

            let {EquipType} = tools.constants;
            let {topology} = $scope;
            let node;
            let bExistNode = false;

            try {

                node = topology.addNode(newNodeParameters);

                if (!node) {
                    node = topology.findNode(newNodeParameters.id);
                    bExistNode = true;
                }

                if (!node) {
                    throw 'node is null';
                }

                let parameters = node.element.parameters;
                let equipType = parameters.equiptype || '';
                parameters.related_alarm  = parameters.related_alarm || newNodeParameters.related_alarm;

                if (equipType.startsWith('LTED') || equipType.startsWith('DU')) {

                    if (!topology.findNode('L3')) {
                        let l3 = topology.addNode({
                            layer_id: 5,
                            id: 'L3',
                            type: vis.nodeType.EquipNode,
                            desc: 'L3',
                            position: [0, 0],
                            fixed: true
                        });

                        topology.addLink(node.id, l3.id);

                        let primium = topology.addNode({
                            layer_id: 5,
                            id: '프리미엄망',
                            desc: '프리미엄망',
                            type: vis.nodeType.CloudNode,
                            color: 0x7F7F7F,
                            position: [-800, 0],
                            fixed: true
                        });

                        topology.addLink(l3.id, primium.id);

                        let mme = topology.addNode({
                            layer_id: 6,
                            id: 'MME',
                            desc: 'MME',
                            type: vis.nodeType.EquipNode,
                            position: [-800, 0],
                            fixed: true
                        });

                        topology.addLink(primium.id, mme.id);
                    }

                }

                var slot = newNodeParameters.slot;
                if (slot) {
                    parameters.slot = parameters.slot || [];
                    if(parameters.slot instanceof Array) {
                        parameters.slot = new Set();
                    }
                    parameters.slot.add(...slot);
                }

                var alarmpos = newNodeParameters.alarmpos;
                if (alarmpos) {
                    parameters.alarmpos = parameters.alarmpos || '';
                    if (typeof parameters.alarmpos == 'string') {
                        parameters.alarmpos = new Set();
                    }
                    parameters.alarmpos.add(alarmpos);
                }

                if (bExistNode) {
                    return;
                }

                // if (data.alarmpos && !alarmPos.includes(data.alarmpos)) {
                //     parameters.alarmpos = alarmPos + ((alarmPos.length == 0) ? '' : ', ') + data.alarmpos
                // }

                // if(parameters.type == vis.nodeType.DU){
                //     node.element.overlayText('SAMSUNG');
                // }

                // var unit = null;
                // var type = tools.constants.EquipType.prototype.getType(equipType);
                // if (type && type.code) {
                //     unit = type.code;
                // }

            } catch (err) {
                throw `토폴로지에 장비를 추가 할 수 없습니다.(ErrCode = addNode Fail) : ${err}`;
            }

            return node;
        }

        function updateUnit() {

            angular.forEach(topology.getNodes(), (node, i) => {

                let parameters = node.element.parameters;
                let equipType = parameters.equiptype || '';
                var unit = getNodeType(equipType);

                // let direction = ticket.root_cause_checkz ? vis.linkDirection.Z : vis.linkDirection.A;
                if (unit) {

                    if (unit /*&& (bUnitFail || bNodeFail || bFanHighTemp)*/) {
                        let innerHtml = '<div class="more-info-contents">\n장애내역 : ${장애내역}<br>\n장애경보 : ${장애경보}<br>\n장애위치 : ${장애위치}<br>\n</div>';
                        innerHtml = innerHtml.replace('${장애내역}', `${tools.toString(ticket.root_cause_type)}:${tools.toString(ticket.ticket_rca_result_dtl_code)}`);
                        innerHtml = innerHtml.replace('${장애경보}', tools.toString(parameters.alarmloc + parameters.alarmmsg));
                        innerHtml = innerHtml.replace('${장애위치}', parameters.alarmpos ? tools.toString(Array.from(parameters.alarmpos).join(',')) :'');

                        try {
                            var templateId = '#template-' + unit.replace(/_[T|R]$/, '')
                            var html = document.querySelector(templateId).innerHTML;
                            html = html.replace('${시스템명}', parameters.name);
                            html = html.replace('${설치일자}', tools.store.formatDate(parameters.instdate));
                            node.element.setUnitTag(html, innerHtml);

                            // 장애 슬롯 번호 추출방법
                            // MT320-1-1-10,EQPT,NEND  => 10번이 장애 슬롯번호임.
                            // MT\d+-\d+-\d+-(\d+)[^\n\r]*$
                            // let regex = /^MT\d+-\d+-\d+-(\d+)[^\n\r]*$/;
                            // let findstr = regex.exec(parameters.slot);

                            if (parameters.alarmpos && parameters.alarmpos.length > 0 ) {
                                var alarmpos = parameters.alarmpos;
                                if(alarmpos){
                                    parameters.slot.add(...alarmpos);
                                }
                            }

                            // if (parameters.alarmloc) {
                            //     let parseSlot = parameters.alarmloc.split(/-|\./);  // - 와
                            //     if (parseSlot.length > 2) {
                            //         parameters.slot.push(parseSlot[2]);
                            //     }
                            // }

                            if (parameters.slot && parameters.slot.size > 0) {
                                parameters.slot = [...new Set(parameters.slot)];        // 중복제거
                                let slot = parameters.slot;
                                if (isPtnMultiFabricFail() && (!parameters.slot.includes(10) && !parameters.slot.includes(11))) {
                                    slot = angular.copy(parameters.slot);
                                    slot.push(...[10, 11]);
                                }

                                node.element.setUnitAlarm(slot);
                                node.element.setVisibleTag(true, 'CT');
                            } else {
                                node.element.setVisibleTag(false, 'CT');
                            }
                        } catch (err) {
                            $log.debug(`${equipType} 타입을 찾을 수 없습니다.:`, parameters);
                        }
                    }

                    let bBitsLos = isPtnBitsLos();
                    let bLanDown = isPtnLanDown();
                    if (bBitsLos) {

                        let infoHtml = '<div>\n    슬롯 : ${슬롯}<br>\n    알람 : ${알람}<br>\n</div>';
                        infoHtml = infoHtml.replace('${슬롯}', parameters.slot);
                        infoHtml = infoHtml.replace('${알람}', parameters.alarmmsg);
                        node.element.setTag(infoHtml);
                        node.element.overlayIcon(vis.Node.overlayType.Clock, 2);
                    } else if (bLanDown) {

                        let infoHtml = '<div>\n    슬롯 : ${슬롯}<br>\n    알람 : ${알람}<br>\n</div>';
                        infoHtml = infoHtml.replace('${슬롯}', parameters.slot);
                        infoHtml = infoHtml.replace('${알람}', parameters.alarmmsg);
                        node.element.setTag(infoHtml);

                        // let iconHtml = '<image class="icon-tag" style="width:100px, height: 100px;" src=\'images/3d/clock.png\'>\n</image>';
                        // node.element.setTag(iconHtml);

                        node.element.overlayIcon(vis.Node.overlayType.LanFail, 2);
                    }
                }

                // <editor-fold desc="[# Tag를 이용하여 노드의 정보 포현. 우선 주석처리]">
                // if (isEquipAlarm() && tag != true) {
                //     let html = '';
                //     if (node) {
                //         if (data.slot) html += `슬롯번호 : ${data.slot}<br>`;
                //         if (data.port) html += `포트번호 : ${data.port}<br>`;
                //         if (data.alarmmsg) html += `알람메시지 : ${data.alarmmsg}<br>`;
                //         if (data.domain) html += `도메인 : ${data.domain}<br>`;
                //         // if (data.alarm_type) html += `장애타입 : ${data.alarm_type}<br>`;
                //         node.element.setTag(html);
                //     } else {
                //         let findNode = topology.findNode(data.id);
                //         if (findNode) {
                //             if (data.slot) html += `슬롯번호 : ${findNode.slot ? findNode.slot + ',' + data.slot : data.slot}<br>`;
                //             if (findNode.port) html += `포트번호 : ${findNode.port}<br>`;
                //             if (findNode.alarmmsg) html += `알람메시지 : ${findNode.alarmmsg}<br>`;
                //             if (findNode.domain) html += `도메인 : ${findNode.domain}<br>`;
                //             findNode.element.setTag(html);
                //         }
                //     }
                // }
                // <!--</editor-fold desc="[# Tag를 이용하여 노드의 정보 포현. 우선 주석처리]">
            });
        }


        function addLink(from, to, param = {}, isCross) {

            let {topology} = $scope;
            let link;

            try {

                if (!from || !to) {
                    throw `from=${from}, to=${to}, param=${JSON.stringify(param)}`;
                }

                param = Object.assign(param, {cross: !!isCross});
                link = topology.addLink(from, to, param);
                if (!isCross) {
                    let causeType = (ticket.root_cause_code || '').toLowerCase();
                    switch (causeType) {
                        case  "uni" :
                            link.element.setDash(true);
                            break;
                        case  "unknown" :
                            link.element.setVisible(false);
                            break;
                        case  "nni" :
                            link.element.setVisible(true)
                        default  :
                            break;
                    }
                }

            } catch (err) {
                throw `토폴로지에 링크를 추가 할 수 없습니다. message : ${err}`;
            }

            return link;
        }

        function customizeNodePosion(layerType) {

            let arrTargetElement = [];

            var zColumns = new Map();
            let zPositon = new Map();

            var layer = topology.getLayers().find(l => l.element.parameters.domain == layerType);
            if (layer) {
                angular.forEach(layer.element.nodes, node => {
                    let location = node.parameters.location || node.parameters.cross_location;
                    zColumns.set(location, (zColumns.get(location) || 0) + 1);
                });
            }

            if (zColumns.size <= 0) {
                return true;
            }

            let zColumnMax = Array.from(zColumns.values()).reduce(function (previous, current) {
                return previous > current ? previous : current;
            });

            let zStart = zColumnMax > 1 ? 300 : 0;
            let zOffset = zColumnMax > 1 ? (coodsRange.yMax * 2) / (zColumnMax - 1) : 0;

            layer.element.nodeDistanceX = (coodsRange.xMax * 2) / (zColumns.size - 1);
            layer.element.nodeDistanceY = zOffset;

            angular.forEach(crossLinks, (crossLink, index, all) => {

                try {

                    let nodea = topology.findNode(crossLink.sysnamea).element;
                    let nodez = topology.findNode(crossLink.sysnamez).element;

                    let upLayerNode, downLayerNode;
                    if (nodea.layer.id > nodez.layer.id) {
                        upLayerNode = nodea;
                        downLayerNode = nodez;
                    } else {
                        upLayerNode = nodez;
                        downLayerNode = nodea;
                    }

                    let type = upLayerNode.layer.domain || crossLink.equiptype;

                    if (type.startsWith(layerType.substr(0, 3))) {

                        if (upLayerNode && downLayerNode) {

                            if (upLayerNode.fixed == true) return;
                            if (downLayerNode.layer.domain == layerType) return;

                            let location = upLayerNode.parameters.location || upLayerNode.parameters.cross_location;
                            let lastPos = zPositon.get(location);
                            let zPos = lastPos == undefined ? zStart : (lastPos - zOffset);
                            let pos = downLayerNode.three.position;
                            if (upLayerNode.parameters.cross_down_nodes.length == 1 && downLayerNode.parameters.cross_up_nodes.length == 1) {
                                moveNode(upLayerNode, pos.x, pos.z);
                            } else {
                                moveNode(upLayerNode, pos.x, zPos);
                            }
                            zPositon.set(upLayerNode.parameters.location, zPos);

                            downLayerNode.fixed = true;
                            upLayerNode.fixed = true;

                            //console.log(`node fixed : ${s.name}`);
                            //console.log(`node fixed : ${t.name}`);

                            arrTargetElement.push(upLayerNode);
                        }
                    }

                } catch (err) {
                    log(`customizeNodePosion 좌표 보정 실패 layer=${layerType} `);
                }
            });

            // if (zColumnMax <= 1) {
            //     // POTN으로 임시추가. 문제 없으면 이 후 코드도 정리 필요
            //     return true;
            // }

            var uniq = arrTargetElement.reduce(function (a, b) {        // 중복제거
                if (a.indexOf(b) < 0) a.push(b);
                return a;
            }, []);

            return false;
        };

        function moveNode(nodeElement, x = null, z = null) {

            x = x == null ? nodeElement.three.position.x : x;
            z = z == null ? nodeElement.three.position.z : z;

            x = Math.min(x, coodsRange.xMax);
            z = Math.min(z, coodsRange.yMax);
            x = Math.max(x, coodsRange.xMin);
            z = Math.max(z, coodsRange.yMin);

            let y = nodeElement.three.position.y;
            let position = new THREE.Vector3(x, y, z);
            nodeElement.setPosition(position);

            // debuggerTestNode(nodeElement);

            if (global.isTestLib)
                console.log(`move : node=${nodeElement.index}, ${nodeElement.name}, x=${x}, y=${y}, z=${z}`);
        }

        function debuggerTestNode(node) {
            if (global.isTestLib && node.id == 'BS해작사화상회의실-MSPP-0101-01-01-동부산_830_1_S11_P6_대향')
                debugger;
        }

        function displayNodeNumber() {
            if (global.isTestLib) {
                // angular.forEach(topology.getNodes(), (node, i) => {
                //     isCyclic(node.element);
                //     node.element.setAlarmCount(i);
                //     node.element.setVisibleCount(true);
                // });
            }
        }

        function arrange() {

            // 초기화
            angular.forEach(topology.getNodes(), (node, i) => {
                delete node.element.fixed;
            });

            displayNodeNumber();

            let roadmCount = topology.findLayer(1).element.nodes.length;
            clearShowLayer();
            topology.getLayers().forEach((layer, i, layerAll) => {

                try {

                    let nodes = layer.element.nodes;
                    if ((nodes && nodes.length > 0) == false) {
                        layer.element.setVisible(false);
                        return;
                    }

                    $scope.showLayers.push(layer.element);
                    layer.element.setVisible(true);

                    if (layer.element.parameters.layout == 'none') {
                        return;
                    }

                    let domain = layer.element.parameters.domain;
                    console.log(`arrange domain=${domain}`);

                    let firstLayer = $scope.showLayers.length == 1;

                    if ((firstLayer && nodes.length > 2) || (!firstLayer && nodes.length > 1)) {

                        let graph = global.graph = new vis.util.CycleGraph(nodes.length);
                        let cross = [];
                        let arrayNode = [];
                        let arrayLink = [];
                        let fixedNodes = [];
                        let notFixedNodes = [];
                        nodes.forEach((node, i) => {

                            if (node.fixed) fixedNodes.push(node);
                            else notFixedNodes.push(node);

                            arrayNode.push({
                                element: node,
                                nodeId: node.id,
                                fixed: node.fixed || node.parameters.fixed,
                                fixedOriginal: node.fixed,
                                x: node.three.position.x / 18 + 50,
                                y: node.three.position.z / 12 + 50,
                            });

                            node.links.forEach(function (link) {
                                if (link.parameters.cross != true) {
                                    graph.addEdge(link.source.element.indexInLayer, link.target.element.indexInLayer);
                                    arrayLink.push({
                                        elemenet: link,
                                        startNodeId: link.source.element.id,
                                        endNodeId: link.target.element.id,
                                    });
                                } else {
                                    cross.push(link);
                                }
                            })
                        });

                        let singleRowLayout = (notFixedNodes.length == 1 && fixedNodes.length == 1);
                        if (singleRowLayout) {

                            let fixedNode = fixedNodes[0];
                            let notFixedNode = notFixedNodes[0];
                            let posX = Math.abs(fixedNode.three.position.x);
                            if (posX > 300) {
                                posX = fixedNode.three.position.x * -1;
                            } else {
                                posX = coodsRange.xMax - posX;
                            }
                            moveNode(notFixedNode, posX, fixedNode.three.position.z);

                        } else {
                            graph.normalization();
                            // graph.isCyclic();

                            // console.log('isCyclic layer={0}, edges:{1}, path={2}'.format(layer.element.name, graph.getEdges(), graph.getPath()));

                            let width = 100, height = 100;
                            let manager = new vis.util.topology.manager();
                            let options = {
                                screenWidth: width,     // 화면 가로 크기
                                screenHeight: height,   // 화면 세로 크기
                                charge: -100,           // 토폴로지 x,y 위치 값이 없을 경우 자동 위치 설정시 당기는 힘 디폴트 -400
                                distance: 20,           // 토폴로지 x,y 위치 값이 없을 경우 자동 위치 설정시 거리 설정. 디폴트 80
                            };

                            manager
                                .charge(options.charge)
                                .linkDistance(options.distance)
                                .size([options.screenWidth, options.screenHeight]);

                            manager.data({sorted: true, nodes: arrayNode, links: arrayLink});
                            manager.initialize();
                            manager.sortRelationshipPosition();

                            arrayNode.forEach((node, i) => {
                                // if (!node.fixedOriginal) {
                                moveNode(node.element, (node.x - 50) * 18, (node.y - 50) * 12);
                                // }
                            });
                        }
                    } else {

                        let offset = (coodsRange.xMax * 2) / (nodes.length - 1);
                        layer.element.nodeDistanceX = offset;
                        nodes.forEach((node, i) => {

                            debuggerTestNode(node);

                            if (!node.fixed) {
                                let x = coodsRange.xMin + offset * i;
                                moveNode(node, x, 0);
                            }
                        });
                    }

                    if (domain == tools.constants.EquipType.FDF.code) {
                        /*if ((0 < roadmCount && domain == tools.constants.EquipType.ROADM.code) || (0 == roadmCount && domain == tools.constants.EquipType.FDF.code)) {*/
                        customizeNodePosion(tools.constants.EquipType.ROADM.code);
                        customizeNodePosion(tools.constants.EquipType.POTN.code);
                        customizeNodePosion(tools.constants.EquipType.PTN.code);
                        customizeNodePosion(tools.constants.EquipType.MSPP.code);
                        // customizeNodePosion('IP');
                    }

                } catch (err) {
                    log(`x 좌표 보정 실패`, layer);
                }
            });

            let copyLayers = angular.copy($scope.showLayers);

            // FDF 삭제
            const idx = copyLayers.findIndex(function (item) {
                return item.id === 0
            })
            if (idx > -1) copyLayers.splice(idx, 1);

            if (!(copyLayers.length === 1 && copyLayers[0].nodes.length <= 2)) {
                angular.forEach(topology.getNodes(), node => {
                    node.element.setVisibleTag(false, 'CT');
                });
            }
        };

        function changeLayerVisible() {
            $scope.topology.getLayers().forEach(layer => {
                var find = $scope.showLayers.some(s => layer.element.id == s.id);
                layer.element.setVisible(find);
            });

            filter();
        }

        function updateShowLayers() {
            clearShowLayer();
            $scope.topology.getLayers().forEach(layer => {
                if (layer.element.getVisible()) {
                    $scope.showLayers.push(layer.element);
                }
            });
        }

        function clearShowLayer() {

            let showLayers = $scope.showLayers = ($scope.showLayers || []);
            if (showLayers.length > 0) {
                showLayers.splice(0, showLayers.length);
            }

            // $scope.showLayers = [];
        }

        function filter() {

            // 2018.12.17  필터기능 삭제
            return;

            //<editor-fold desc="# FDF 설치위치 기준으로 필터링">
            if (arrFdfLocation.length > 0) {
                topology.getLayers().forEach((layer => {

                    if (layer.visible === false)
                        return;

                    try {
                        let domain = layer.element.parameters.domain;
                        if (domain != tools.constants.EquipType.FDF.code && domain != tools.constants.EquipType.ROADM.code) {
                            var nodes = layer.element.nodes;
                            angular.forEach(nodes, node => {
                                node.setVisible(arrFdfLocation.includes(node.desc));
                            }, this);
                        }
                    } catch (err) {
                        if ($scope.debug) $log.log(`FDF 기준 설치위치로 필터링`, layer);
                    }
                }).bind(this));
            }
            //</editor-fold>
        }

        function isEquipAlarm(causeTypeCode) {

            var {TicketType} = tools.constants;
            return TicketType.prototype.includes([TicketType.UnitFail, TicketType.NodeFail], causeTypeCode || ticket.root_cause_type);
        }

        function covertAlarmLevelToColor(alarmlevel, returnType = 'array') {

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

        function loadJson(confirm = true, savedData) {

            try {
                if (!ticket.ticket_id)
                    return;

                function load(result) {
                    if (result && result.ticket_id) {

                        var contents = (typeof result.contents) == "object" ? result.contents : JSON.parse(result.contents);
                        topology.clear();
                        topology.setData(contents);
                        updateShowLayers();

                        displayNodeNumber();

                        tools.showToast('토폴로지가 로드되었습니다');
                    }
                };

                let fnRequest = () => {
                    if (!savedData) {
                        tools.http({
                            service: tools.constants.Service.rca,
                            action: "SELECT_TICKET_TOPOLOGY",
                            TICKET_ID: ticket.ticket_id,
                        }, load);
                    } else {
                        load(savedData);
                    }
                };

                if (confirm) {
                    $scope.tools.createConfirmDlg(
                        '토폴로지를 로드하시겠습니까?',
                        '확인을 선택하면 토폴로지가 변경됩니다.', null)
                        .then(fnRequest);
                } else {
                    fnRequest();
                }

            } catch (err) {
                return;
            }
        }

        function saveJson() {

            try {
                if (!ticket.ticket_id)
                    return;

                $scope.tools.createConfirmDlg(
                    '토폴로지를 저장하시겠습니까?',
                    '확인을 선택하면 토폴로지가 저장됩니다.', null)
                    .then(function() {
                        var output = {};
                        topology.toJSON(output);
                        let contents = JSON.stringify(output);

                        tools.http({
                            service: tools.constants.Service.rca,
                            action: "UPDATE_TICKET_TOPOLOGY",
                            TICKET_ID: ticket.ticket_id,
                            UPDATE_USER_ID: tools.store.userId,
                            CONTENTS: contents,
                        }, function (result) {
                            if (result) {
                                tools.showToast('토폴로지가 저장되었습니다');
                            }
                        });
                    });
            } catch (err) {
                return;
            }
        }

        function deleteJson (confirm = true){

            try {
                if (!ticket.ticket_id)
                    return;

                let fnRequest = () => {

                    tools.http({
                        service: tools.constants.Service.rca,
                        action: "DELETE_TICKET_TOPOLOGY",
                        TICKET_ID: ticket.ticket_id,
                    }, function (result) {
                        if (result) {
                            $scope.loadTopology();
                            tools.showToast('토폴로지가 초기화되었습니다');
                        }
                    });
                }

                $scope.tools.createConfirmDlg(
                    '토폴로지를 초기화 하시겠습니까?',
                    '확인을 선택하면 토폴로지가 변경됩니다.', null)
                    .then(fnRequest);

            } catch (err) {
                return;
            }
        }

        function createInstance() {
            return {
                initialize,
                prev,
                next,
                toggle,
                reloadTopology,
                loadTopology,
                changeLayerVisible,
                updateShowLayers,
                isEquipAlarm,
                covertAlarmLevelToColor,
                loadJson,
                saveJson,
                deleteJson,
                arrange,
                topology,
                topologyControl,
                drawing,
                ticket,
                beginWaitting,
                endWaitting
            };
        }

        return {
            getInstance: function (param) {
                if (!instance) {
                    instance = createInstance();
                    instance.initialize(param);
                }
                return instance;
            },
            destroy: function () {
                if (instance) {
                    delete instance;
                    instance = null;
                }
            }
        }
    })();

    // <editor-fold desc="[mapView 모듈 사용 테스트]">
    ;(function () {

        if(!global.isTestLib)
            return;

        let mapView;

        global.saveTopology = function () {

            var r = confirm("현재 토폴로지를 DB에 저장하시겠습니까?");
            if (r == true) {
                mapView.saveJson();
            }

        };

        global.prev = function () {
            mapView.prev();
        };

        global.next = function () {
            mapView.next();
        };

        global.toggle = function () {
            mapView.toggle();
        };

        global.reload = function () {
            mapView.reloadTopology();
        };

        global.loadTicket = function (ticketId, saved = false) {

            // <editor-fold desc="[#Mybatis foreach TEST]">
            // var array = [1, 2];
            // var object = {index: 3, id: "id3"};
            // var object_array = [{index: 4, id: "my4"}, {index: 5, id: "my5"}];
            // tools.http({
            //     service: "rca",
            //     action: "FOREACH_SYNTAX_TEST",
            //     array: array,
            //     object_array: object_array,
            //     // object: object   // Object 형태는 안먹히네
            // }, function(result) {
            //     // debugger;
            // });
            // <!--</editor-fold desc="[#Mybatis foreach TEST]">

            if (ticketId) {

                if (saved != null) {
                    mapView.loadTopology({ticket_id: Number(ticketId)}, null, saved).then(() => {
                    });
                } else {

                    // <editor-fold desc="[JSON 파일로드. 사용안함]">
                    mapView.beginWaitting();
                    $.get(`json/${ticketId}.json`, function (ticket) {
                        $.get(`json/${ticketId + (saved ? '_saved' : '_db')}.json`, function (data) {
                            mapView.loadTopology(ticket, data).then(() => {
                            });
                        }, 'json');
                    });
                    // <!--</editor-fold desc="[JSON 파일로드]">
                }
            } else {
                loadTest();
            }

            // $.get("json/sample_saved.json", function (data) {
            //     mapView.loadTopology({ticket_id: '1'}, data).then(() => {
            //     });
            // }, 'json');

        };

        function loadTest() {

            // <editor-fold desc="[data2.js 데이터 로드 테스트]">
            global.loadData.call(null)
                .done(data => {
                    topology.setData(data);
                    setTimeout(() => {
                        topology.getNodes().forEach(function (node, i) {

                            try {
                                var unit = node.element.type;
                                var templateId = '#template-' + unit.replace(/_[T|R]$/, '')
                                var html = document.querySelector(templateId).innerHTML;
                                node.element.setUnitTag(html);
                                node.element.setUnitAlarm([2,5]);
                                node.element.setVisibleTag(false, 'CT');
                            } catch (e) {}
                        });
                    }, 1000);

                    mapView.endWaitting();
                });
            // <!--</editor-fold desc="[data2.js 데이터 로드 테스트]">

            // <editor-fold desc="[API 테스트]">
            // console.log('TEST mapView.loadTopology start');

            // $.get("json/sample_saved.json", function (data) {
            //     mapView.loadTopology({ticket_id: '1'}, data).then(() => {
            //     });
            // }, 'json');

            // global.loadData.call(null)
            //     .done(data => {
            //         mapView.loadTopology(data).then(() => {
            //             console.log('TEST mapView.loadTopology end');
            //         });
            //     });


            // <!--</editor-fold desc="[API 테스트]">
        }

        var tools = {
            showAlert: console.log.bind(null),
            http: function (params, callback) {
                $.ajax({
                    url: params.path || '//localhost:8080/service', // 요청 할 주소
                    async: true, // false 일 경우 동기 요청으로 변경
                    type: 'POST', // GET, PUT
                    data: JSON.stringify(params), // 전송할 데이터
                    dataType: 'json', // xml, json, script, html
                    beforeSend: function (jqXHR) {}, // 서버 요청 전 호출 되는 함수 return false; 일 경우 요청 중단
                    success: callback, // 요청 완료 시
                    error: function (request, status, error) {
                        console.error("code = " + request.status + " message = " + request.responseText + " error = " + error); // 실패 시 처리
                    }, // 요청 실패.
                    complete: function (jqXHR) {} // 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
                })
            },
            store: {
                userId: 'rca',
                hasProfile: () => false,
                getMode: () => false,
                formatDate: () => '',
            },
            toString: function (text) {
                return text ? text.toString() : '';
            },
            constants: {
                Service: {rca: 'rca'},
                Action: {REQUEST_3D_TOPLOGY: 'REQUEST_3D_TOPLOGY'},
                EquipType: {

                    // <editor-fold desc="[#209826 추가]">
                    WTS_WTC_180: {code: 'WTS_WTC-180', layer: 4, nodeType: vis.nodeType.WTS_WTC_180},
                    WTS_WTC_60: {code: 'WTS_WTC-60', layer: 4, nodeType: vis.nodeType.WTS_WTC_60},
                    WTS_WTC_30: {code: 'WTS_WTC-30', layer: 4, nodeType: vis.nodeType.WTS_WTC_30},
                    WTS_WTC_06: {code: 'WTS_WTC-06', layer: 4, nodeType: vis.nodeType.WTS_WTC_06},
                    C_180: {code: 'C-180', layer: 4, nodeType: vis.nodeType.C_180},
                    C_60: {code: 'C-60', layer: 4, nodeType: vis.nodeType.C_60},
                    C_30: {code: 'C-30', layer: 4, nodeType: vis.nodeType.C_30},
                    // <!--</editor-fold desc="[#209826 추가]">

                    // <editor-fold desc="[#191024 추가]">
                    PTNTFM3300: {code: 'PTNTFM3300', layer: 3, nodeType: vis.nodeType.PTNTFM3300},
                    PTNTFM1020: {code: 'PTNTFM1020', layer: 3, nodeType: vis.nodeType.PTNTFM1020},
                    PTNTFM1015: {code: 'PTNTFM1015', layer: 3, nodeType: vis.nodeType.PTNTFM1015},
                    PTNTFM1010: {code: 'PTNTFM1010', layer: 3, nodeType: vis.nodeType.PTNTFM1010},
                    // <!--</editor-fold desc="[#191024 추가]">

                    // <editor-fold desc="[191002 추가]">
                    MSPWRC1200A: {code: 'MSPWRC1200A', layer: 5, nodeType: vis.nodeType.MSPWRC1200A},
                    MSPWRS2488A: {code: 'MSPWRS2488A', layer: 5, nodeType: vis.nodeType.MSPWRS2488A},
                    MSPCOU1060: {code: 'MSPCOU1060', layer: 5, nodeType: vis.nodeType.MSPCOU1060},
                    MSPTFS570: {code: 'MSPTFS570', layer: 5, nodeType: vis.nodeType.MSPTFS570},
                    // <!--</editor-fold desc="[191002 추가]">

                    // <editor-fold desc="[190807 추가]">
                    DAS: {code: 'DAS', layer: 6, nodeType: vis.nodeType.DAS},
                    LOC_U9500H: {code: 'LOC_U9500H', layer: 6, nodeType: vis.nodeType.LOC_U9500H},
                    CSC_CATALYST6509_E: {code: 'CSC_CATALYST6509-E', layer: 6, nodeType: vis.nodeType.CSC_CATALYST6509_E},
                    ACT_7750SR_12: {code: 'ACT_7750SR-12', layer: 6, nodeType: vis.nodeType.ACT_7750SR_12},
                    DU: {code: 'DU', layer: 7, nodeType: vis.nodeType.DU},
                    RU: {code: 'RU', layer: 7, nodeType: vis.nodeType.RU},
                    LTED: {code: 'LTED', layer: 7, nodeType: vis.nodeType.DU},
                    LTER: {code: 'LTER', layer: 7, nodeType: vis.nodeType.RU},
                    FMUX: {code: 'FMUX', layer: 5, nodeType: vis.nodeType.EquipNode},
                    OXC640SC: {code: 'OXC640SC', layer: 5, nodeType: vis.nodeType.EquipNode},
                    LWDCSAL: {code: 'LWDCSAL', layer: 5, nodeType: vis.nodeType.EquipNode},
                    WDC8: {code: 'WDC8', layer: 5, nodeType: vis.nodeType.EquipNode},
                    NDCS: {code: 'NDCS', layer: 5, nodeType: vis.nodeType.EquipNode},
                    WDC7: {code: 'WDC7', layer: 5, nodeType: vis.nodeType.EquipNode},
                    MSPP3HCO: {code: 'MSPP3HCO', layer: 5, nodeType: vis.nodeType.MSPP3HCO},
                    MSPP3HSNH: {code: 'MSPP3HSNH', layer: 5, nodeType: vis.nodeType.MSPP3HSNH},
                    MSPP3HTF: {code: 'MSPP3HTF', layer: 5, nodeType: vis.nodeType.MSPP3HTF},
                    MSPP3HYK: {code: 'MSPP3HYK', layer: 5, nodeType: vis.nodeType.MSPP3HYK},
                    MSPWR4016A: {code: 'MSPWR4016A', layer: 5, nodeType: vis.nodeType.MSPWR4016A},
                    MSPWR4016C: {code: 'MSPWR4016C', layer: 5, nodeType: vis.nodeType.MSPWR4016C},
                    MSPWRM155U: {code: 'MSPWRM155U', layer: 5, nodeType: vis.nodeType.MSPWRM155U},
                    MSPWRR155A: {code: 'MSPWRR155A', layer: 5, nodeType: vis.nodeType.MSPWRR155A},
                    MSPWRS155C: {code: 'MSPWRS155C', layer: 5, nodeType: vis.nodeType.MSPWRS155C},
                    MSPCOU1010: {code: 'MSPCOU1010', layer: 5, nodeType: vis.nodeType.MSPCOU1010},
                    MSPCOU1012: {code: 'MSPCOU1012', layer: 5, nodeType: vis.nodeType.MSPCOU1012},
                    MSPCOU1030: {code: 'MSPCOU1030', layer: 5, nodeType: vis.nodeType.MSPCOU1030},
                    MSPCOU1040: {code: 'MSPCOU1040', layer: 5, nodeType: vis.nodeType.MSPCOU1040},
                    MSPCOU1050: {code: 'MSPCOU1050', layer: 5, nodeType: vis.nodeType.MSPCOU1050},
                    MSPTFS520: {code: 'MSPTFS520', layer: 5, nodeType: vis.nodeType.MSPTFS520},
                    MSPTFS530: {code: 'MSPTFS530', layer: 5, nodeType: vis.nodeType.MSPTFS530},
                    MSPTFS540: {code: 'MSPTFS540', layer: 5, nodeType: vis.nodeType.MSPTFS540},
                    MSPTFS550: {code: 'MSPTFS550', layer: 5, nodeType: vis.nodeType.MSPTFS550},
                    MSPTFS5500: {code: 'MSPTFS5500', layer: 5, nodeType: vis.nodeType.MSPTFS5500},
                    MSPP10GCO: {code: 'MSPP10GCO', layer: 5, nodeType: vis.nodeType.MSPP10GCO},
                    MSPPHSNH: {code: 'MSPPHSNH', layer: 5, nodeType: vis.nodeType.MSPPHSNH},

                    PTN5CALU: {code: 'PTN5CALU', layer: 3, nodeType: vis.nodeType.PTN5CALU},
                    PTN5RALU: {code: 'PTN5CALU', layer: 3, nodeType: vis.nodeType.PTN5RALU},
                    PTN160ALU: {code: 'PTN160ALU', layer: 3, nodeType: vis.nodeType.PTN160ALU},
                    PTN320ALU: {code: 'PTN320ALU', layer: 3, nodeType: vis.nodeType.PTN320ALU},
                    PTN320HALU: {code: 'PTN320HALU', layer: 3, nodeType: vis.nodeType.PTN320HALU},
                    PTNTFM1012: {code: 'PTNTFM1012', layer: 3, nodeType: vis.nodeType.PTNTFM1012},
                    PTNTFM1016: {code: 'PTNTFM1016', layer: 3, nodeType: vis.nodeType.PTNTFM1016},
                    PTNTFM3016: {code: 'PTNTFM3016', layer: 3, nodeType: vis.nodeType.PTNTFM3016},
                    PTNTFM3064: {code: 'PTNTFM3064', layer: 3, nodeType: vis.nodeType.PTNTFM3064},
                    PTNTFM5480: {code: 'PTNTFM5480', layer: 3, nodeType: vis.nodeType.PTNTFM5480},
                    PTNTFM5720: {code: 'PTNTFM5720', layer: 3, nodeType: vis.nodeType.PTNTFM5720},
                    POTN12THW: {code: 'POTN12THW', layer: 2, nodeType: vis.nodeType.POTN12THW},
                    POTNCN5430: {code: 'POTNCN5430', layer: 2, nodeType: vis.nodeType.POTNCN5430},
                    // <!--</editor-fold desc="[190807 추가]">

                    // <editor-fold desc="[#200603 추가]">
                    PTNUT7300: {code: 'PTNUT7300', layer: 3, nodeType: vis.nodeType.PTNUT7300},
                    PTNUT7310: {code: 'PTNUT7310', layer: 3, nodeType: vis.nodeType.PTNUT7310},
                    PTNUT7400: {code: 'PTNUT7400', layer: 3, nodeType: vis.nodeType.PTNUT7400},
                    PTNUT7515: {code: 'PTNUT7515', layer: 3, nodeType: vis.nodeType.PTNUT7515},
                    // <!--</editor-fold desc="[#200603 추가]">

                    // <editor-fold desc="[#191206 추가]">
                    ROADM8THW_T: {code: 'ROADM8THW_T', layer: 1, nodeType: vis.nodeType.ROADM8THW_T},
                    ROADM8THW_R: {code: 'ROADM8THW_R', layer: 1, nodeType: vis.nodeType.ROADM8THW_R},
                    ROADM8HHW_T: {code: 'ROADM8HHW_T', layer: 1, nodeType: vis.nodeType.ROADM8HHW_T},
                    ROADM8HHW_R: {code: 'ROADM8HHW_R', layer: 1, nodeType: vis.nodeType.ROADM8HHW_R},
                    // <!--</editor-fold desc="[#191206 추가]">
                    MSPP: {code: 'MSPP', layer: 5, nodeType: null},
                    PCM: {code: 'PCM', layer: 4, nodeType: null},
                    PTN: {code: 'PTN', layer: 3, nodeType: null},
                    POTN: {code: 'POTN', layer: 2, nodeType: null},
                    ROADM: {code: 'ROADM', layer: 1, nodeType: null},
                    FDF: {code: 'FDF', layer: 0, nodeType: null},
                    // ETC: {code: 'ETC', layer: 0, nodeType: null},
                },
                TicketType: {
                    CableCut: {text: '선로', code: 'CableCut', audio: '선로장애', hex: '#ff7f0e', index: 0},
                    Linkcut: {text: '링크', code: 'Linkcut', audio: '링크장애', hex: '#2ca02c', index: 1},
                    NodeFail: {text: '노드', code: 'NodeFail', audio: '노드장애', hex: '#d62728', index: 2},
                    PowerFail: {text: '전원', code: 'PowerFail', audio: '전원장애', hex: '#9467bd', index: 3},
                    UnitFail: {text: '유닛', code: 'UnitFail', audio: '유닛장애', hex: '#8c564b', index: 4},
                    Etc: {text: '기타', code: 'Etc', audio: '기타장애', hex: '#e377c2', index: 5},
                    //ProvFail: {text: '구성', code: 'ProvFail', audio: '기타장애'},
                    //Intrinsic: {text: 'Intrinsic', code: 'Intrinsic', audio: '기타장애'},
                    //SUB_ALM: {text: 'SUB_ALM', code: 'SUB_ALM', audio: '기타장애'},
                },
                TicketResultCode: {
                    /*전원차단*/
                    PTN_PWR_DOWN: ['MSPP_EQP_RT_PWR_DOWN', 'PTN_EQP_RT_PWR_DOWN'],
                    /*고온이상*/
                    PTN_FAN_HIGH_TEMP: ['PTN_EQP_FAN_HIGH_TEMP'],
                    /*랜장애*/
                    PTN_LAN_DOWN: ['PTN_EQP_MT_LAN_IF_EVENT', 'PTN_EQP_SHELF_LAN_FAIL'],
                    /*클럭부장애*/
                    PTN_BITS_LOS: ['PTN_EQP_BITS_LOS'],
                    /*(멀티)스위칭패브릭장애 : 이 경우에는 10,11 포트에 장애표현 */
                    PTN_UNIT_MUL_MT_FABRIC_FAIL: ['PTN_UNIT_MUL_MT_FABRIC_FAIL'],
                },
                TopologyLoadType: {
                    AUTO: 'AUTO',
                    FROM_LOCAL: 'FROM_LOCAL',
                    FROM_DB: 'FROM_DB',
                },
                LinkAlarmPrediction: {
                    1: '추정(최고)',
                    2: '추정(높음)',
                    3: '추정(보통)',
                    4: '추정(낮음)',
                    5: '추정(최저)',
                }
            },
            showToast: console.log,
            createConfirmDlg: function () {
                var deferred = $.Deferred();
                setTimeout(() => deferred.resolve())
                return deferred.promise();
            }
        };

        global.initMapView = function ($scope) {

            global.$scope = Object.assign($scope || {}, {
                debug: true,
                safeApply: (fn) => {
                    if ($scope.$$phase == '$apply' || $scope.$$phase == '$digest') {
                    } else {
                        setTimeout(() => {
                            if (fn) fn.call(null);
                            $scope.$apply();
                        });
                    }
                },
                tools : tools
            });

            let onLoadComplete = (mapView) => {

                let {topology, topologyControl} = mapView;
                global.topology = topology;
                global.topologyControl = topologyControl;

            };

            let events = {
                onLoadComplete: onLoadComplete.bind(null),
            };

            let parameters = {
                global: global,
                ticket: {},
                $scope: $scope,
                tools: $scope.tools,
                events: events
            };

            let {EquipType, TicketResultCode, TicketType}= parameters.tools.constants;

            EquipType.prototype = {
                getType: function (equipType, notNullNodeType = true) {
                    var ret = null;
                    Object.keys(EquipType).forEach(function (key) {
                        var value = EquipType[key];
                        if (notNullNodeType && !value.nodeType) {
                            return true;
                        }
                        if (equipType.startsWith(value.code)) {
                            ret = value;
                            return false;
                        }
                    });
                    return ret;
                }
            }
            TicketResultCode.prototype = {
                includes: function (type, code) {
                    return (type || []).includes(code);
                },
            };
            TicketType.prototype = {
                getType: function (type) {
                    var ret = null;
                    Object.keys(TicketType).forEach(function (key) {
                        if (type == key) {
                            ret = TicketType[key];
                        }
                    });

                    return ret;
                },
                includes: function (typeArray, typeCode) {

                    var type = TicketType.prototype.getType(typeCode || '');
                    if (!type) {
                        return false;
                    };

                    return (typeArray || []).includes(type);
                },
            };

            global.topology_reset = false;
            mapView = global.mapView = MapView.getInstance(parameters);
        }
    })();
    // <!--</editor-fold desc="[mapView 모듈 사용 테스트]">

})(typeof exports !== 'undefined' ? exports : this)
