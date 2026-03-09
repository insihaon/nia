if (typeof Math.randomInt == "undefined") {
    Math.randomInt = function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };
}

if (typeof Array.prototype.remove == "undefined") {
    Array.prototype.remove = function (value, fnEquals) {
        if (!fnEquals) {
            fnEquals =
                !!value && Array.isArray(value)
                    ? (item, array) => array.includes(item)
                    : (item, value) => item === value;
        }

        for (var i = 0, idx = 0; i < this.length; i++, idx++) {
            if (fnEquals(this[i], value, idx)) {
                this.splice(i, 1);
                i--;
            }
        }
        return this;
    };
}

if (typeof d3.selection.prototype.moveToFront == "undefined") {
    d3.selection.prototype.moveToFront = function () {
        return this.each(function () {
            if (this.parentNode != null) {
                this.parentNode.appendChild(this);
            }
        });
    };
}

if (typeof d3.selection.prototype.moveToBack == "undefined") {
    d3.selection.prototype.moveToBack = function () {
        return this.each(function () {
            if (this.parentNode != null) {
                var refChild = this.parentNode.firstChild;
                if (refChild) {
                    this.parentNode.insertBefore(this, refChild);
                }
            }
        });
    };
}

let log = window.console.log

    ; (function (global) {

        'use strict';

        const className = { selected: 'selected', dragging: 'dragging', invisible: 'invisible', error: 'error', focus: 'focus' }
        const eventType = { selectChanged: 'selectChanged' }
        const elementType = { svg: 'svg', node: 'node', link: 'link', link_selector: 'link_selector' }
        const mouseMode = { select: 'select', edit: 'edit' }
        const nodeAlarm = {
            0: { style: "stat_normal" },
            1: { style: "stat_abnormal" },
            2: { style: "stat_error" },
            3: { style: "stat_down" },
            4: { style: "stat_unknown" },
        };

        const linkAlarm = {
            normal: { color: "#93c62d" }, /* 0beaf5 */
            abnormal: { color: "#f76e5d" },
            potn: { color: "#ffda48" }
        };

        const nodeImage = {
            '0': { path: 'images/node/roadm.png' },
            '1': { path: 'images/node/repeater.png' },
            '2': { path: 'images/node/ptn.png' },
            '3': { path: 'images/node/mspp.png' },
            '4': { path: 'images/node/switch.png' },
            'node_badge': { path: 'images/node/error-node.png'/*, width: 10, height: 10*/ },
            'link-cut': { path: 'images/node/error-link.png', width: 30, height: 30 },
        };

        let Map2d = global.Map2d = function () {

            let ctrl = global.c = this;     // global.c is for debugging only
            let eventListeners = ctrl.eventListeners = []
            let simulation, link_force, charge_force, center_force, drag, zoom, node_tip, link_source_tip,
                link_dest_tip;
            let svg, g_link, g_node, g_desc;
            let defaultOptions = {
                save_map: false,
                mode: mouseMode.edit,
                select: { isOnly: true, weight: 2, node_tour: false, link_tour: false },
                debug_intermediate: true,
                node: { displayField: 'device_name', fix_size: false, displayImage: 'image', show_r: true, r: 30, width: 25, height: 30, badge_size: 30, drag_weight: 2, stroke_color: "#604c45", color: "#634d45", opacity: 0.7, text_size: 20, invisible_node: true },
                link: { displayField: 'index', stroke_width: 2.5, text_color: "#ffc616", text_size: 10, badge_size: 30, selector_weight: 1.5, autoReverse: true },
                link_traffic: { show: true, show_arrow: true, 'r': 3, color: "#555", arrow_color: '#ff0606', opacity: 1.0, dur: 5 },
                filePath: 'json/nia/',
            };
            let fileName = localStorage['last_map'] || 'data_nia_1.json';
            let options =
                Object.assign(objectCopyPrimitive(defaultOptions), {
                    fileName,
                    link_type: 'line',
                    zoomInTest,
                    save,
                    load,
                    exportToFile,
                    onChangeAnimated,
                    onChangeNodeDisplayField,
                    onChangeNodeR,
                    onChangeVisibleNodeOption,
                    onChangeNodeBadgeSize,
                    onChangeNodeSelect,
                    onChangeNodeWidthHeight,
                    onChangeLink,
                    onChangeLinkSelect,
                    onChangeLinkTrffic,
                    onChangeLinkTrfficAnimation,
                    onChangeLinkText,
                    onChangeScale,
                    onTestLinkAlarmOccur,
                    onTestMbaAlarmOccur,
                    getDrawingProperty,
                    getRefX,
                });

            Object.assign(this, {
                className,
                eventType,
                elementType,
                mouseMode,
            });

            function getDrawingProperty(d, directional) {

                let leftHand, reverse;

                if (directional) {
                    if (options.link.autoReverse) {
                        leftHand = true;
                        reverse = d.source.x > d.target.x;
                    } else {
                        leftHand = true;
                        reverse = false;
                    }
                } else {
                    if (options.link.autoReverse) {
                        leftHand = d.source.x < d.target.x;
                        reverse = !leftHand;
                    } else {
                        leftHand = d.source.x < d.target.x;
                        reverse = false;
                    }
                }
                return { leftHand, reverse };
            }

            function getRefX() {

                let multiply_value = Math.max(options.node.r, options.node.height * 0.6, options.node.width * 0.6);

                return (5 + (multiply_value / options.link.stroke_width) * 3);
            }

            function onTestLinkAlarmOccur() {
                let { data } = ctrl;

                let find = data.links.find(l => l.id == '1');
                if (find) {
                    log(`find id == 1 link info : `, find)
                }
                data.links[0].status = (data.links[0].status || 1) * -1
                draw(false);
            }

            function onTestMbaAlarmOccur() {
                throw new Error('에러발생 테스트');
            }

            let isVisualNode = ctrl.isVisualNode.bind(ctrl);

            function onChangeAnimated() {
                let me = d3.select(this);
                if (options.link_type == "curved") {
                    simulation
                        .force('center', center_force)
                        .force('charge', charge_force)
                        .force('link', link_force);

                    $do_layout(true)

                    me.text('Animated');
                } else {
                    simulation
                        .force('center', null)
                        .force('charge', null)
                        .force('link', null);

                    $do_layout(true)

                    me.text('Static');
                }
            }

            function onChangeLinkTrfficAnimation() {
                svg.selectAll('.link_traffic_flow')
                    .attr('dur', function (d) {
                        return options.link_traffic.dur + 's';
                    })
            }

            function onChangeLinkTrffic() {
                svg.selectAll('.link_traffic:not(.error)')
                    .attr('r', options.link_traffic.r)
                    .attr('fill', options.link_traffic.color)
                    .style('fill-opacity', options.link_traffic.opacity)
                    .classed(className.invisible, !options.link_traffic.show);

                svg.select('defs marker path')
                    .style('fill', options.link_traffic.arrow_color);

                svg.select('defs')
                    .classed(className.invisible, !options.link_traffic.show_arrow);
            }

            function onChangeNodeWidthHeight() {
                svg.selectAll('.node_image')
                    .attr('transform', function (d) {
                        let outWidth = (d.image || {}).width || options.node.width,
                            outHeight = (d.image || {}).height || options.node.height,
                            halfWidth = parseFloat(outWidth) / 2,
                            halfHeight = parseFloat(outHeight) / 2;
                        return 'translate(' + (-halfWidth) + ',' + (-halfHeight) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                    })
                svg.select('#arrow')
                    .attr('refX', options.getRefX());
            }

            function onChangeLinkSelect(link_tour, index) {
                try {
                    link_tour = ctrl.options.select.link_tour;
                    if (link_tour) {

                        let links = ctrl.getVisualLinkAll();
                        index = (index || 0) % links.length;

                        if (links && Number.isInteger(index) && links.length > index) {

                            // log(`onChangeLinkSelect : ${index}`);
                            let d = links[index];
                            ctrl.selectElement(d, elementType.link);

                        }
                        setTimeout(onChangeLinkSelect.bind(this, link_tour, ++index), 1000);
                    }

                    $do_one_tick();

                } catch (e) {
                    console.error(e);
                }
            }

            function onChangeNodeSelect(node_tour, index) {

                try {

                    node_tour = ctrl.options.select.node_tour;
                    if (node_tour) {

                        let nodes = ctrl.getVisualNodeAll();
                        index = (index || 0) % nodes.length;

                        if (nodes && Number.isInteger(index) && nodes.length > index) {

                            // log(`onChangeNodeSelect : ${index}`);
                            let d = nodes[index];
                            ctrl.selectElement(d, elementType.node);
                        }
                        setTimeout(onChangeNodeSelect.bind(this, node_tour, ++index), 1000);
                    }

                    $do_one_tick();

                } catch (e) {
                    console.error(e);
                }
            }

            function onChangeNodeBadgeSize() {

                svg.selectAll('.node_badge')
                    .attr('transform', function (d) {

                        let outWidth = options.node.badge_size,
                            outHeight = options.node.badge_size,
                            nodeR = options.node.r;

                        let transform = 'translate(' + 0.5 * (nodeR + outWidth / 2) + ',' + (-1 * (nodeR + outHeight / 2)) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                        // log(transform);
                        return transform;
                    });
            }

            function onChangeNodeR() {

                svg.selectAll('.node_circle')
                    .attr('r', options.node.r * 0.8)
                    .style('fill', options.node.color)
                    .style('stroke', options.node.stroke_color)
                    .style('opacity', options.node.opacity);
                svg.selectAll('.node_icon')
                    .attr('y', options.node.r / 2 + 2)
                    .style('font-size', options.node.r);

                svg.selectAll('.node_container circle')
                    .classed(className.invisible, !options.node.show_r);

                svg.select('#arrow')
                    .attr('refX', options.getRefX());

            }

            function onChangeVisibleNodeOption() {
                svg.selectAll('.node_invisible')
                    .classed(className.invisible, function(d) {
                        return options.node.invisible_node && (d.visible == false || d.source.visible == false || d.target.visible == false);
                    });
            }

            function onChangeLink() {
                $do_one_tick();

                svg.select('#arrow')
                    .attr('refX', options.getRefX());
            }

            function onChangeLinkText() {
                svg.selectAll('.link_label')
                    .style('font-size', options.link.text_size)
                    .style('fill', options.link.text_color)
                    .text(function (d) {
                        switch (options.link.displayField) {
                            case 'id':
                                return d.source.id + '-' + d.target.id;
                            case 'port':
                                return d.source_port_disp + '-' + d.target_port_disp;
                            case 'speed':
                                return d.speed;
                            case 'status':
                                return d.status;
                            case 'index':
                                return d.index;
                            default:
                                return ''
                        }
                    });
            }

            function onChangeScale() {

                if (options.node.fix_size) {
                    let font_size = 20 / (d3.event.transform.k) > 35 ? 35 : 20 / (d3.event.transform.k);
                    svg.select('g.desc').style('font-size', font_size);
                } else { svg.select('g.desc').style('font-size', 20); }

            }

            function onChangeNodeDisplayField() {
                let field = options.node.displayField,
                    desc_con = svg.select('g.desc').selectAll('g.desc_container');

                desc_con.select('text').text(function (d) {
                    return d[field];
                });
            }

            function initialize() {
                // <editor-fold desc="[#create container for links and nodes elements.]">
                svg = d3
                    .select("svg#topology_container")
                    .on("click", function (d, index, all) {
                        ctrl.clickHandler(this, elementType.svg, d, index, all);
                    });


                let bg_image = drawBgImage()
                let bg_element = drawBgElement()

                let links = svg.select("g.links");
                if (links.empty()) {
                    links = svg.append("g").attr("class", "links");
                }

                let nodes = svg.select("g.nodes");
                if (nodes.empty()) {
                    nodes = svg.append("g").attr("class", "nodes");
                }

                let desc = svg.select("g.desc");
                if (desc.empty()) {
                    desc = svg.append("g").attr("class", "desc")
                        .style('font-size', options.node.text_size);
                }

                let inter_nodes;
                if (options.debug_intermediate) {
                    inter_nodes = svg.select("g.intermediate_nodes");
                    if (!inter_nodes.size()) {
                        inter_nodes = svg
                            .append("g")
                            .attr("class", "intermediate_nodes");
                    }
                }
                // <!--</editor-fold desc="[#create container for links and nodes elements.]">

                if (!this.svg) {
                    /*
                            force simulation
                        */
                    simulation = d3.forceSimulation().stop();

                    // <editor-fold desc="[#force]">

                    link_force = d3
                        .forceLink()
                        .id(function (d) {
                            return d.id;
                        })
                        .distance(function (d) {
                            if ("id" in d.source && "id" in d.target) {
                                return 120;
                            } else {
                                return 60;
                            }
                        });

                    charge_force = d3
                        .forceManyBody()
                        .strength(function (d) {
                            if (isVisualNode(d)) {
                                return -160;
                            } else {
                                return -200;
                            }
                        })
                        .distanceMax(300);

                    center_force = d3.forceCenter();

                    // <!--</editor-fold desc="[#force]">

                    // <editor-fold desc="[#gestures]">

                    // drag node
                    drag = d3
                        .drag()
                        .subject(function (d) {
                            return d;
                        })
                        .on("start", function (d) {
                            return ctrl.dragStartHandler(this, elementType.node, d);
                        })
                        .on("drag", function (d) {
                            return ctrl.draggingHandler(this, elementType.node, d);
                        })
                        .on("end", function (d) {
                            return ctrl.dragEndHandler(this, elementType.node, d);
                        });

                    // zoom and drag to move
                    zoom = d3
                        .zoom()
                        .scaleExtent([0.1, 5])
                        .on("zoom", () => {
                            if (ctrl.zoom_scale != d3.event.transform.k) {
                                let scale = (ctrl.zoom_scale = d3.event.transform.k);
                                // log(`zoom=${scale}`);
                            }

                            bg_image.attr("transform", d3.event.transform);
                            bg_element.attr("transform", d3.event.transform);
                            links.attr("transform", d3.event.transform);
                            nodes.attr("transform", d3.event.transform);
                            desc.attr("transform", d3.event.transform);

                            if (ctrl.options.node.fix_size) {
                                ctrl.options.onChangeScale();
                            }
                            // const { x, y, k } = d3.event.transform;
                            // descs.attr('transform',`translate(${x},${y}) scale(${k},${k})`);

                            // descs.select("text").style('font-size', function (d) {
                            //     const zoom = ctrl.zoom_scale
                            //     let scale = d3.scaleLinear().domain([0.1,5]).range([10,20]);
                            //     return scale(zoom) + 'px'
                            // })

                            ctrl.showLinkPortTooltip(null, null);



                            /*
                                        debug: zoom or pan intermediate nodes;
                                    */
                            if (!!inter_nodes) {
                                inter_nodes.attr("transform", d3.event.transform);
                            }
                        });
                    svg.call(zoom);

                    // <!--</editor-fold desc="[#gestures]">

                    // <editor-fold desc="[#tooltip]">

                    node_tip = d3
                        .tip()
                        .attr("class", "tooltip")
                        .offset([-10, 0])
                        .html(function (d) {
                            let tooltip = "";
                            tooltip += d.mac ? `<p><strong class='title'>MAC:</strong>${d.mac}</p>` : "";
                            tooltip += d.ip ? `<p><strong class='title'>IP:</strong>${d.ip}</p>` : "";
                            tooltip += d.netmask ? `<p><strong class='title'>Netmask:</strong>${d.netmask}</p>` : "";
                            tooltip += d.gateway ? `<p><strong class='title'>Gateway:</strong>${d.gateway}</p>` : "";
                            tooltip += d.vlan ? `<p><strong class='title'>VLAN:</strong>${d.vlan}</p>` : "";
                            tooltip += d.device_name ? `<p><strong class='title'>장비:</strong>${d.device_name}</p>` : "";
                            return tooltip;
                        });
                    svg.call(node_tip);

                    link_source_tip = d3.tip().attr("class", "tooltip");
                    link_dest_tip = d3.tip().attr("class", "tooltip");

                    svg.call(link_source_tip);
                    svg.call(link_dest_tip);

                    // <!--</editor-fold desc="[#tooltip]">

                    if (options.link_type == "curved") {
                        simulation
                            .force("center", center_force)
                            .force("charge", charge_force)
                            .force("link", link_force);
                    }

                    // keep track of topology components.
                    Object.assign(ctrl, {
                        svg: svg,
                        simulation: simulation,
                        link_force: link_force,
                        charge_force: charge_force,
                        center_force: center_force,
                        drag: drag,
                        zoom: zoom,
                        zoom_scale: 1,
                        node_tip: node_tip,
                        link_source_tip: link_source_tip,
                        link_dest_tip: link_dest_tip,
                    });
                }
            }

            function zoomInTest() {
                ctrl.zoomInByRandomNode()
                // ctrl.zoomInByRandomLink()
            }

            function save() {
                let data = ctrl.getSaveData()
                let dataString = JSON.stringify(data);
                localStorage.setItem(`saved_${window.pageParams.mode}`, dataString);
                log(dataString);
            }

            function getSaveData() {
                let data = {
                    nodes: ctrl.getVisualNodeAll().map((d) => {
                        let copyed = Object.assign({}, d);
                        delete copyed.fx;
                        delete copyed.fy;
                        delete copyed.vx;
                        delete copyed.vy;
                        return copyed;
                    }),
                    links: ctrl.getVisualLinkAll().map((d) => {
                        let copyed = Object.assign({}, d);
                        delete copyed.source;
                        delete copyed.target;
                        delete copyed.index;
                        delete copyed.intermediate;
                        return copyed;
                    }),
                    config: objectCopyPrimitive(ctrl.getConfigAll())
                };
                return data;
            }

            function draw(forceUpdate = true, newElement = {}) {
                ctrl.clear();
                drawBgImage()
                drawBgElement()
                drawNode(newElement.node);
                drawLink(newElement.link);
                onChangeVisibleNodeOption();
                ctrl.update(forceUpdate);
            }

            function drawBgImage() {
                let g = svg.select("image.bg_image")

                if (g.empty()) {
                    g = svg.append("image")
                        .attr("class", "bg_image")
                        .attr("width", "100%")
                        .attr("height", "100%")
                        // .attr("xlink:href", "images/background4.png");
                }
                return g
            }

            function drawBgElement() {
                let g = svg.select('g')

                if (g.empty()) {
                    g = svg.append('g')
                        .classed('bg_element', true)
                }

                try {
                    g.selectAll("*").remove();

                    const config = ctrl.getConfigAll()
                    if(config?.map?.bg_element) {
                        g.append('path')
                            .attr('d', 'M490 510 L670 290 L1470 290 L1360 510Z')
                            .style('fill', 'gray')
                            .style("fill-opacity", "20%")
                            .style('stroke', '#fff')
                            .style('stroke-width', 0.1)

                        g.append('path')
                            .attr('d', 'M460 775 L630 540 L1390 540 L1270 775Z')
                            .style('fill', 'gray')
                            .style("fill-opacity", "20%")
                            .style('stroke', '#fff')
                            .style('stroke-width', 0.1)
                    }

                } catch (error) {
                }

                return g
            }

            function drawNode(parameter) {

                let nodes = ctrl.getNodeAll();

                if (parameter) {
                    nodes.push(parameter);
                }

                // <editor-fold desc="[#노드 추가]">
                let visualNodes = ctrl.getVisualNodeAll();
                g_node = svg.select('g.nodes').selectAll('g.node_container')
                    .data(visualNodes);
                g_node.exit().remove();

                let new_node = g_node.enter()
                    .append('g')
                    .classed('node_container', true)
                    .attr('id', function (d) {
                        return ctrl.getNodeId(d);
                    })
                    .on('contextmenu', function (d) {
                        log('contextmenu');
                    })
                    .on('click', function (d, index, all) {
                        ctrl.clickHandler(this, elementType.node, d, index, all);
                    })
                    .on('mouseover', function (d) {
                        ctrl.over_node = this;
                        d3.select(this).classed(className.focus, true);
                        return node_tip.show.apply(this, arguments);
                    })
                    .on('mouseout', function (d) {
                        d3.select(this).classed(className.focus, false);
                        return node_tip.hide.apply(this, arguments);
                    })
                    .call(drag);

                new_node.append('circle')
                    .attr('r', options.node.r * 0.8)
                    .classed(className.invisible, !options.node.show_r)
                    .classed('node_circle', true);

                if (ctrl.isIconNodeOption()) {
                    // <editor-fold desc="[#폰트 아이콘을 이용한 노드 그리기]">
                    new_node.append('text')
                        .attr('x', 0)
                        .attr('y', options.node.r / 2 + 2)
                        .style('font-size', options.node.r)
                        .classed('node_icon', true)
                        .classed('ftstcall', true)
                        .text('\ueaf2');
                    // <!--</editor-fold desc="[#폰트 아이콘을 이용한 노드 그리기]">
                } else {
                    // <editor-fold desc="[#이미지를 이용한 노드 그리기]">
                    new_node.append('image')
                        .classed('node_image', true)
                        .attr('xlink:href', function (d) {
                            // return imgMap[d.type];
                            let type = d.device_type || '0'
                            d.image = nodeImage[type] || nodeImage['0'];
                            return d.image.path;
                        })
                        .style('width', function (d) {
                            return 100;
                            // return  (d.image || {}).width || options.node.width;
                        })
                        .style('height', function (d) {
                            return 100;
                            // return (d.image || {}).height || options.node.height;
                        })
                        .attr('transform', function (d) {
                            // let image = d3.select(this)._groups[0][0],
                            let outWidth = (d.image || {}).width || options.node.width,
                                outHeight = (d.image || {}).height || options.node.height,
                                halfWidth = parseFloat(outWidth) / 2,
                                halfHeight = parseFloat(outHeight) / 2;
                            return 'translate(' + (-halfWidth) + ',' + (-halfHeight) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                        });
                    // <!--</editor-fold desc="[#이미지를 이용한 노드 그리기]">
                }

                new_node.each(function (d, i) {
                    if(d.visible == false) {
                        return ;
                    }
                    let selection = d3.select(this);
                    if (d.alarm != undefined) {
                        selection.append('g')
                            .append('image')
                            .attr('class', 'node_badge')
                            .attr('xlink:href', function (d) {
                                d.badge = nodeImage['node_badge'];
                                return d.badge.path;
                            })
                            .style('width', function (d) {
                                return 100;
                            })
                            .style('height', function (d) {
                                return 100;
                            })
                            .attr('transform', function (d) {

                                let outWidth = options.node.badge_size,
                                    outHeight = options.node.badge_size,
                                    nodeR = options.node.r;

                                return 'translate(' + 0.5 * (nodeR + outWidth / 2) + ',' + (-1.0 * (nodeR + outHeight / 2)) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                            });
                    } else if (d.alarm_count != undefined) {
                        selection.append('g')
                            .attr('class', 'node_badge')
                            .append('circle')
                            .attr('r',20)
                            .attr('transform', 'translate(20,-20) scale(0.5 0.5)')
                            .style('fill',  d.related_alarm ? 'red' : '#ff000033')
                            .style('stroke', 'red');

                        selection.select('g.node_badge')
                            .append('text')
                            .attr('transform', 'translate(20,-15) scale(0.4, 0.4)')
                            .style('fill', 'rgb(255 255 255)')
                            .style('font-size', '30px')
                            .html(d.alarm_count);
                    }
                });

                g_node = new_node.merge(g_node);

                g_node
                    .attr('class', function (d) {
                        let alarm = d.alarm || 4;
                        return 'node_container ' + nodeAlarm[alarm].style;
                    })
                    .classed('node_invisible', function(d) {
                        return d.visible == false;
                    });

                // <!--</editor-fold desc="[#노드 추가]">

                // <editor-fold desc="[#노드라벨 추가]">
                g_desc = svg.select('g.desc').selectAll('g.desc_container')
                    .data(visualNodes);

                g_desc.exit().remove();

                let new_desc = g_desc.enter()
                    .append('g')
                    .attr('id', function (d) {
                        return ctrl.getNodeId(d, 'desc');
                    })
                    .classed('desc_container', true)
                    .classed('node_invisible', function(d) { return d.visible == false; });

                new_desc.append('text')
                    .attr('x', 0)
                    .attr('y', 25);

                g_desc = new_desc.merge(g_desc);
                g_desc.select("text").text(function (d) {
                    return d[options.node.displayField];
                });

                if (options.debug_intermediate) {
                    let inter_node = svg.select('g.intermediate_nodes').selectAll('circle')
                        .data(ctrl.getNotVisualNodeAll());

                    inter_node.exit().remove();

                    let new_inter_node = inter_node.enter()
                        .append('circle')
                        .attr('id', function (d) {
                            return d.nodeId;
                        })
                        .attr('r', 3)
                        .attr('fill', 'white');

                    inter_node = new_inter_node.merge(inter_node);
                }
                // <!--</editor-fold desc="[#노드라벨 추가]">
            }

            function drawLink(parameter) {

                let visualLinks = ctrl.getVisualLinkAll();

                if (parameter) {
                    visualLinks.push(parameter);
                }

                // <editor-fold desc="[#링크 추가]">
                g_link = svg.select('g.links').selectAll('g.link_container').data(visualLinks);

                g_link.exit().remove();

                let new_link = g_link.enter()
                    .append('g')
                    .attr('id', function (d) {
                        return ctrl.getLinkId(d);
                    })
                    .classed('node_invisible', function(d) {
                        return d.source.visible == false || d.target.visible == false;
                    })
                    .classed('link_container', true);

                new_link
                    .append('path')
                    .attr('id', function (d) {
                        return ctrl.getLinkId(d, 'link_path');
                    })
                    .style('stroke', function (d) {
                        if (d.status <= 0) {
                            return linkAlarm.abnormal.color;
                        } else {
                            if (d.equip_type && d.equip_type.toLowerCase() == 'potn') {
                                return linkAlarm.potn.color;
                            }
                            return linkAlarm.normal.color;
                        }
                    })
                    .style('stroke-width', function (d) {
                        return options.link.stroke_width + 'px';
                    })
                    .classed('link_path', true)
                    .attr("marker-end", "url(#arrow)");

                const dur = `${options.link_traffic.dur}s`;
                new_link.each(function (d, i) {
                    let _this = d3.select(this);

                    // if (d.status != '0') {

                    let rect = _this
                        .append("circle")
                        .attr("id", function (d) {
                            return ctrl.getLinkId(d, "link_traffic");
                        })
                        .attr("r", options.link_traffic.r)
                        .attr("fill", options.link_traffic.color)
                        .style("fill-opacity", options.link_traffic.opacity)
                        .classed("link_traffic", true)
                        .classed(className.error, d.status <= 0)
                        .classed(className.invisible, !options.link_traffic.show);

                    let motion = _this
                        .append("animateMotion")
                        .classed("link_traffic_flow", true)
                        .attr("xlink:href", function (d) {
                            return "#" + ctrl.getLinkId(d, "link_traffic");
                        })
                        .attr("dur", dur)
                        .attr("fill", "freeze")
                        .attr("repeatCount", "indefinite")
                        .append("mpath")
                        .attr("xlink:href", function (d) {
                            return "#" + ctrl.getLinkId(d, "link_path");
                        });

                    this.parentNode.appendChild(this);
                    d3.select(this).raise();

                    // }

                    if (d.status <= 0) {
                        rect
                            .append("animate")
                            .attr("id", "errorLinkTraffic")
                            .attr("attributeName", "opacity")
                            .attr("values", "1;0.3;0.1")
                            .attr("dur", dur)
                            .attr("repeatCount", "indefinite");
                    }

                    if (d.status < 0) {
                        // http://objjob.phrogz.net/d3/object/499
                        const image = _this
                            .append("image")
                            .classed("link_cut", true)
                            .attr("xlink:href", function (d) {
                                d.image = nodeImage["link-cut"];
                                return d.image.path;
                            })
                            .style("width", 100)
                            .style("height", 100);
                    }
                });

                new_link
                    .append('svg:text')
                    .append('svg:textPath')
                    .classed('link_label', true)
                    .attr('startOffset', '50%')
                    .attr('text-anchor', 'middle')
                    // .attr('alignment-baseline', 'middle')  // hanging, middle, baseline
                    .attr('xlink:href', function (d) {
                        // 텍스트를 항상 좌에서 위로 그리기 위해 link 가 아닌 linkselector 와 맵핑.
                        return '#' + ctrl.getLinkSelectId(d);
                    })
                    // .style('fill', '#000')
                    .style('font-size', options.link.text_size)
                    .style('fill', options.link.text_color)
                    .text(function (d) {
                        switch (options.link.displayField) {
                            case 'id':
                                return d.source.id + '-' + d.target.id;
                            case 'port':
                                return d.source_port_disp + '-' + d.target_port_disp;
                            case 'speed':
                                return d.speed;
                            case 'status':
                                return d.status;
                            case 'index':
                                return d.index;
                            default:
                                return ''
                        }
                    });

                new_link
                    .append('path')
                    .attr('id', function (d) {
                        return ctrl.getLinkSelectId(d);
                    })
                    .style('stroke-width', function (d) {
                        return (options.link.stroke_width * options.link.selector_weight) + 'px';
                    })
                    .classed('link_selector', true)
                    .on('click', function (d, index, all) {
                        ctrl.clickHandler(this, elementType.link, d, index, all);
                    })
                    .on('mouseover', function (d, index, all) {
                        ctrl.linkOverHandler(this, elementType.link, d, index, all);
                    })
                    .on('mouseout', function (d, index, all) {
                        ctrl.linkOutHandler(this, elementType.link, d, index, all);
                    });

                g_link = new_link.merge(g_link);

                ctrl.midpoints = g_link.data().map(d => {
                    return d.intermediate;
                });
                // <!--</editor-fold desc="[#링크 추가]">

                // <editor-fold desc="[#z-index 정렬]">
                new_link
                    .selectAll('.link_traffic')
                    .each(function (d, i) {
                        let traffic = d3.select(this);
                        d3.select(traffic.parentNode).raise();

                    });
                // <!--</editor-fold desc="[#z-index 정렬]">

                svg.select('defs').classed(className.invisible, !options.link_traffic.show_arrow);
            }

            function update(forceUpdate = true) {
                /*
                    노드 링크 로직 적용
                */
                let { data } = ctrl;
                simulation.nodes(data.nodes);
                link_force.links(data.links);

                /*
                    update link, node selection closure.
                    for performance.
                */
                simulation.on("tick", $do_tick.bind(ctrl, g_link, g_node, g_desc));

                $do_layout(forceUpdate);
            }

            function load(data) {

                // <editor-fold desc="[#load: 새로운 노드와 시뮬레이션 링크 적용.]">
                if (typeof data === 'string') {
                    loadFromFile(data)
                    localStorage['last_map'] = data;
                } else if (data) {
                    drawData(data);
                } else if (options.save_map) {
                    let data = JSON.parse(localStorage.getItem(`saved_${window.pageParams.mode}`));
                    drawData(data);
                } else {
                    loadFromFile(options.fileName);
                    localStorage['last_map'] = options.fileName;
                }

                function loadFromFile(fileName) {
                    d3.json(`${options.filePath}${fileName}`, function (error, data) {
                        if (error) {
                            throw error;
                        }
                        log('data:', data);
                        drawData(data);
                        // loadConfigByFile(resetDuration);
                    });
                }
                // <!--</editor-fold desc="[#load: 새로운 노드와 시뮬레이션 링크 적용.]">
            }

            function objectCopyPrimitive(source, target = {}) {
                let sourceObj = source;
                Object.keys(sourceObj).forEach(key => {
                    if (sourceObj[key] && typeof sourceObj[key] === 'object') {
                        target[key] = objectCopyPrimitive(sourceObj[key], target[key])
                    } else {
                        target[key] = sourceObj[key]
                    }
                })
                return target;
            }


            /* config 설정 세팅 및 로드 */
            function loadConfig(data) {

                data.config = data.config || {};

                /* options */
                /* default 옵션으로 reset */

                // Object.assign(options, deepClone(defaultOptions));
                objectCopyPrimitive(defaultOptions, options);

                /* 옵션값 (config > options) */
                if (data.config.options != undefined) {
                    Object.keys(data.config.options).forEach((key) => {
                        if (Array.isArray(options[key])) {
                            options[key] = options[key].concat(data.config.options[key])
                        } else if (options[key] instanceof Object) {
                            Object.assign(options[key], data.config.options[key])
                        } else {
                            options[key] = data.config.options[key]
                        }
                    })
                }

                /* background 이미지 (config > map) */
                let bg_image = svg.select("image.bg_image");
                let bg_width = '100%';
                let bg_height = '100%';
                let bg_image_path = "";
                let bg_opacity = "";
                if (data.config.map != undefined && data.config.map.constructor == Object) {
                    bg_width = data.config.map.width || bg_width;
                    bg_height = data.config.map.height || bg_height;
                    bg_image_path = data.config.map.path || bg_image_path;
                    bg_opacity = data.config.map.opacity || bg_opacity;
                }

                if (bg_image.size()) {
                    bg_image
                        .attr("width", bg_width)
                        .attr("height", bg_height)
                        .attr("xlink:href", bg_image_path)
                        .style("opacity", bg_opacity);
                }

                /* 줌 reset */
                data.config.zoom = data.config.zoom || {};
                const ZOOM_RESET = data.config.zoom.reset == undefined || data.config.zoom.reset == true;
                const ZOOM_SCALE = (data.config.zoom && data.config.zoom.initScale) ? data.config.zoom.initScale : 1;
                const ZOOM_POS = data.config.zoom && data.config.zoom.initPos;

                if (ZOOM_RESET) {
                    ctrl.resetZoom();
                }
                if (ZOOM_POS) {
                    setTimeout(() => {
                        ctrl.zoomIn({ type: 'pos', scale: ZOOM_SCALE, ...ZOOM_POS, duration : 750 });
                    }, 1000)
                }
            }

            function exportToFile(data, filename = 'console.json') {
                if (!data) {
                    data = ctrl.getSaveData()
                }

                if (typeof data === 'object') {
                    data = JSON.stringify(data, undefined, 4)
                }

                var blob = new Blob([data], { type: 'text/json' })
                var e = document.createEvent('MouseEvents')
                var a = document.createElement('a')

                a.download = filename
                a.href = window.URL.createObjectURL(blob)
                a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')
                e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
                a.dispatchEvent(e)
            }

            function drawData(data) {

                loadConfig(data);

                data.nodes = data.nodes || []
                data.links = data.links || []
                // let links = [];

                // A mapping: {node.id: node}
                let node_by_id = d3.map(data.nodes, function (d) {
                    return d.id;
                });
                let visualLinks = [];
                data.links.forEach(function (link, idx) {
                    let source = link.source = node_by_id.get(link.source_id),
                        target = link.target = node_by_id.get(link.target_id),
                        intermediate = {};
                        console.log("node_by_id =========",node_by_id);
                        console.log("link.target_id =========",link.target_id);
                        console.log("node_by_id.get(link.target_id) =========",node_by_id.get(link.target_id));

                    let id = ctrl.getLinkId(link);

                    // 중복체크
                    if (visualLinks.findIndex(e => e.id === id) >= 0) {
                        delete link.index;
                        return true;
                    }

                    link.index = idx;

                    intermediate.nodeId = ctrl.getLinkId(link, 'intermediate');

                    data.nodes.push(intermediate);
                    data.links.push(
                        { 'source': source, 'target': intermediate },
                        { 'source': intermediate, 'target': target }
                    );
                    if(target != null && target != ''){
                        visualLinks.push(Object.assign(link, {
                            'id': link.id,
                            'element_id': id,
                            'index': idx,
                            'source': source,
                            'intermediate': intermediate,
                            'target': target,
                            'target_id': link.target_id,
                            'source_port_disp': (link['source_port_disp'] || '정보없음'),
                            'target_port_disp': (link['target_port_disp'] || '정보없음'),
                        }));
                    }
                });

                // 중복제거
                data.links.remove(link => !('id' in link));

                data.visualLinks = visualLinks;
                ctrl.data = data;

                Object.assign(ctrl, { g_link, g_node, g_desc });

                let forceUpdate = !data.nodes.some(n => n.x && n.y);
                draw(forceUpdate);
            }

            function $do_layout(forceUpdate = true) {

                let { width, height } = ctrl.getViewBoxSize();

                center_force.x(width / 2).y(height / 2);

                if (options.link_type == "curved") {
                    $do_animated_layout(forceUpdate);
                } else {
                    $do_static_layout(forceUpdate);
                }
            }

            function $do_static_layout(forceUpdate = true) {
                /*
                    deregister drag event.
                    register force

                    call simulation.tick() several times
                    call ticked()   -> draw finished layout

                    deregister force
                    register drag event again.
                */

                if (forceUpdate) {
                    // 주석처리를 하지 않으면 읽은 데이터에 x, y 값이 없는 경우 하나로 뭉쳐나온다.
                    if (options.link_type != "curved") {
                        simulation
                            .force('center', center_force)
                            .force('charge', charge_force)
                            .force('link', link_force);
                    }
                    simulation.alpha(1);

                    if (!options.save_map) {
                        for (let i = 0, n = Math.ceil(Math.log(simulation.alphaMin()) / Math.log(1 - simulation.alphaDecay())); i < n; ++i) {
                            simulation.tick();
                        }
                    }
                }

                if (options.link_type != "curved") {
                    simulation
                        .force('center', null)
                        .force('charge', null)
                        .force('link', null);
                }

                $do_one_tick();
            }

            function $do_animated_layout(forceUpdate = true) {
                /*
                    deregister drag event.
                    register force

                    call simulation.tick() several times
                    call ticked()   -> draw finished layout

                    deregister force
                    register drag event again.
                */

                if (forceUpdate === false) {
                    $do_one_tick();
                    return
                }

                if (options.link_type != "curved") {
                    simulation
                        .force('center', center_force)
                        .force('charge', charge_force)
                        .force('link', link_force);
                }
                simulation.alpha(1);

                let ticks_per_render = 5;
                window.requestAnimationFrame(function render() {
                    for (let i = 0; i < ticks_per_render; i++) {
                        simulation.tick();
                    }

                    $do_one_tick();

                    if (simulation.alpha() > simulation.alphaMin()) {
                        window.requestAnimationFrame(render);
                    } else {

                        if (options.link_type != "curved") {
                            simulation
                                .force('center', null)
                                .force('charge', null)
                                .force('link', null);
                        }
                    }
                });

            }

            function $do_tick(link_sel, node_sel, desc_sel) {

                let ctrl = this;

                /*
                    link_selector 업데이트
                */
                link_sel.select('path.link_path').attr('d', function (d) {
                    return ctrl.drawLinkHandler(this, elementType.link, d);
                });

                link_sel.select('path.link_selector').attr('d', function (d) {
                    return ctrl.drawLinkHandler(this, elementType.link_selector, d);
                });

                link_sel.select('image.link_cut').attr('transform', function (d) {
                    return ctrl.drawLinkCutHandler(this, null, d);
                });

                /*
                    nodes 업데이트
                */

                // let dr = d3.select(this).classed(className.dragging);
                node_sel.attr('transform', function (d) {
                    return ctrl.transformHandler(this, elementType.node, d);
                });

                /*
                    description 업데이트
                */
                desc_sel.attr('transform', function (d) {
                    return 'translate(' + d.x + ',' + d.y + ')';
                });

                // /*
                //     debug: intermediate 노드 위치 업데이트
                // */
                if (options.debug_intermediate) {
                    svg.select('g.intermediate_nodes').selectAll('circle')
                        .attr('cx', function (d) {
                            return d.x;
                        })
                        .attr('cy', function (d) {
                            return d.y;
                        });
                }

                svg.select('g.intermediate_nodes').selectAll('circle')
                    .classed(className.invisible, options.link_type != "curved");

            }

            function $do_one_tick() {
                /*
                    handle one tick on graphic elements.
                */
                let link = svg.select('g.links').selectAll('g.link_container'),
                    node = svg.select('g.nodes').selectAll('g.node_container'),
                    desc = svg.select('g.desc').selectAll('g.desc_container');

                $do_tick.call(ctrl, link, node, desc);
            }

            Object.assign(ctrl, {
                initialize,
                load,
                draw,
                update,
                options,
                getSaveData
            })

        }

        Object.assign(global.Map2d, {
            className,
            eventType,
            elementType,
            mouseMode,
        });

        Map2d.prototype = {
            addEventListener: function (type, eventHandler) {
                var listener = {};
                listener.type = type;
                listener.eventHandler = eventHandler;
                this.eventListeners.push(listener);
            },

            dispatchEvent: function (event) {
                for (var i = 0; i < this.eventListeners.length; i++)
                    if (event.eventType === this.eventListeners[i].type)
                        this.eventListeners[i].eventHandler(event);
            },

            fireEvent: function (event) {
                this.dispatchEvent(event);
            },

            // <editor-fold desc="[#Map2d.prototype]">

            /**
             * 엘리먼트를 삭제한다.
             * @param element
             */
            // removeElement: function (element) {
            //     let ctrl = this;
            //     if (element) {
            //         log(`removeElement `, element);
            //         element.remove();
            //     }
            // }

            /**
             * 노드를 추가한다.
             * @param element
             */
            addNode: function (element) {
                let ctrl = this;
                let transform = d3.zoomTransform(ctrl.svg.node());
                let pos = transform.invert(d3.mouse(element));

                let nodes = ctrl.getNodeAll();
                let index = nodes.length;
                let id = `new_node_${index}`;

                nodes.push({
                    id: id,
                    device_name: id,
                    device_type: "1",
                    alarm: Math.randomInt(0, 3),
                    x: pos[0],
                    y: pos[1],
                });

                log(`addNode: ${id}, ${pos[0]}, ${pos[1]}`, nodes[index]);

                ctrl.draw(false);
            },

            /**
             * 노드를 삭제한다.
             * @param d
             * @param element
             */
            deleteNode: function (d, element) {
                let ctrl = this;
                let mylog = (...args) =>
                    !true || console.log.call(this, `deleteNode `, ...args);

                // ctrl.clearSelection();

                // find node
                let nodeElement = ctrl.getNodeElement(d);
                let nodeDescElement = ctrl.getNodeElement(d, "desc");
                mylog(`find node:`, nodeElement, nodeDescElement);

                // find contact-links
                let links = ctrl.getContactLinks(d);
                mylog(`find contact-links:`, links);

                // find intermediate nodeIds
                let intermediateNodeIds = links.map(
                    (link) => link.intermediate.nodeId
                );
                mylog(`find intermediate nodeIds:`, intermediateNodeIds);

                // find intermediate links
                let intermediateLinks = ctrl.getIntermediateLinks(
                    intermediateNodeIds
                );
                mylog(`find intermediate links:`, intermediateLinks);

                let topologyLinks = ctrl.getLinkAll();
                let topologyNodes = ctrl.getNodeAll();
                let visualLinks = ctrl.getVisualLinkAll();
                mylog(`topologyLinks:`, topologyLinks);
                mylog(`topologyNodes:`, topologyNodes);
                mylog(`visualLinks:`, visualLinks);

                /*링크 삭제*/
                topologyLinks.remove(links, (item, links) => links.includes(item));
                visualLinks.remove(links, (item, links) => links.includes(item));
                mylog(
                    `링크 삭제:`,
                    links,
                    "topologyLinks:" + topologyLinks.length,
                    "visualLinks:" + visualLinks.length
                );

                /*중간점 링크 삭제*/
                topologyLinks.remove(intermediateLinks);
                mylog(
                    `중간점 링크 삭제:`,
                    intermediateLinks,
                    "topologyLinks:" + topologyLinks.length
                );

                /*노드 삭제*/
                topologyNodes.remove(d.id, (item, id) => item.id === id);
                mylog(
                    `노드 삭제:`,
                    nodeElement,
                    nodeDescElement,
                    d,
                    "topologyNodes:" + topologyNodes.length
                );

                /*중간점 삭제*/
                topologyNodes.remove(
                    intermediateNodeIds,
                    (item, intermediateNodes) =>
                        intermediateNodes.includes(item.nodeId)
                );
                mylog(
                    `중간점 삭제:`,
                    intermediateNodeIds,
                    "topologyNodes:" + topologyNodes.length
                );

                ctrl.draw(false);
            },

            /**
             * 링크를 삭제한다.
             * @param d
             * @param element
             */
            deleteLink: function (d, element) {
                let ctrl = this;
                ctrl.clearSelection();

                // find link
                let link = d;
                let linkElement = ctrl.getLinkElement(d);

                // find contact-nodes
                let sourceNode = d.source;
                let targetNode = d.target;

                // find intermediate node
                let intermediateNodeId = link.intermediate.nodeId;

                // find intermediate link
                let intermediateLinks = ctrl.getIntermediateLinks([
                    intermediateNodeId,
                ]);

                let topologyLinks = ctrl.getLinkAll();
                let topologyNodes = ctrl.getNodeAll();
                let visualLinks = ctrl.getVisualLinkAll();

                /*링크 삭제*/
                topologyLinks.remove(link);
                visualLinks.remove(link);

                /*중간점 링크 삭제*/
                topologyLinks.remove(intermediateLinks);

                /*중간점 삭제*/
                topologyNodes.remove(
                    intermediateNodeId,
                    (item, intermediateNodeId) => intermediateNodeId == item.nodeId
                );

                ctrl.draw(false);
            },

            /**
             * 노드를 반환한다.
             * @param d
             * @returns {HTMLElement}
             */
            getNodeElement: function (d, prefix) {
                let ctrl = this;
                let id = ctrl.getNodeId(d, prefix);
                let element = document.getElementById(id);
                return element;
            },

            /**
             * 노드ID를 반환한다.
             * @param d
             * @param prefix
             * @returns {string}
             */
            getNodeId: function (d, prefix) {
                return (prefix || "node") + "_" + d.id;
            },

            /**
             * 링크컨테이너를 반환한다.
             * @param d
             * @returns {HTMLElement}
             */
            getLinkElement: function (d) {
                let ctrl = this;
                let id = ctrl.getLinkId(d);
                let element = document.getElementById(id);
                return element;
            },

            /**
             * 링크ID를 반환한다.
             * @param d
             * @param prefix
             * @returns {string}
             */
            getLinkId: function (d, prefix) {
                // try {
                //     var a = !!d && !!d.source && !!d.source.id && Number.isInteger(d.index) && !!d.target && !!d.target.id;
                //     if (!a) debugger;
                // } catch (e) {
                //     debugger;
                // }

                let myid =
                    d.id ||
                    `${d.source.id}:${d.source_port_disp}-${d.target.id}:${d.target_port_disp}`;
                return (prefix || "link") + "_" + myid;
            },

            /**
             * 링크선택영역 ID 를 반환한다.
             * @param d
             * @param prefix
             * @returns {string}
             */
            getLinkSelectId: function (d, prefix) {
                let ctrl = this;
                return ctrl.getLinkId(d, "link_selector");
            },

            /**
             * 모든 노드를 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getNodeAll: function () {
                let ctrl = this;
                return ctrl.data.nodes;
            },

            /**
             * 모든 링크를 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getLinkAll: function () {
                let ctrl = this;
                return ctrl.data.links;
            },

            /**
             * 모든 설정을 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getConfigAll: function () {
                let ctrl = this;
                return ctrl.data.config;
            },

            isVisualNode: (d) => "id" in d,
            isNotVisualNode: (d) => !("id" in d),

            /**
             * 실제링크를 반환한다.
             * @returns {*}
             */
            getVisualNodeAll: function () {
                let ctrl = this;
                return ctrl.getNodeAll().filter(ctrl.isVisualNode);
            },

            /**
             * 가상링크를 반환한다.
             * @returns {Array}
             */
            getVisualLinkAll: function () {
                let ctrl = this;
                return ctrl.data.visualLinks;
            },

            /**
             * 가상노드를 반환한다.
             * @returns {*}
             */
            getNotVisualNodeAll: function () {
                let ctrl = this;
                return ctrl.getNodeAll().filter(ctrl.isNotVisualNode);
            },

            /**
             * 링크를 그린다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             * @returns {*}
             */
            drawLinkHandler: function (element, type, d, index, all) {
                let ctrl = this;
                let { options } = ctrl;

                function fnDrawLinkSelector() {
                    d3.select(element).style("stroke-width", function (d) {
                        let selected = d3
                            .select(this.parentNode)
                            .classed(className.selected);
                        return (options.link.stroke_width * (selected ? options.select.weight * options.link.selector_weight : options.link.selector_weight) + "px");
                    });

                    return drawLink(d, false);
                }

                function fnDrawLink() {
                    d3.select(element).style("stroke-width", function (d) {
                        let selected = d3
                            .select(this.parentNode)
                            .classed(className.selected);
                        return (options.link.stroke_width * (selected ? options.select.weight : 1) + "px");
                    });

                    return drawLink(d, true);
                }

                /**
                 * 옵션에 따른 링크 Path를 반환한다.
                 * @param d
                 * @param leftHand : true 일 경우 왼쪽에서 오른쪽 즉 순방향을 의미.
                 * @returns {string}
                 */
                let drawLink = function () {
                    let ctrl = this;
                    let { options } = ctrl;

                    if (options.link_type == "arc") {
                        return drawArc(...arguments);
                    } else if (options.link_type == "line") {
                        return drawLine(...arguments);
                    }
                    else if (options.link_type == "mixed") {
                        if (d.link_type == "arc") {
                            return drawArc(...arguments);
                        } else if (d.link_type == "line") {
                            return drawLine(...arguments);
                        } else {
                            return drawArc(...arguments);
                        }
                    }
                    else {
                        return drawPath(...arguments);
                    }
                }.bind(ctrl);

                /**
                 * 직선 링크의 Path를 반환한다.
                 * @param d
                 * @param directional : true 일 경우 link , false 일 경우 linkSelector
                 * @returns {string}
                 */
                let drawLine = function (d, directional) {
                    let ctrl = this;
                    let { options } = ctrl;
                    let autoReverse = options.link.autoReverse;
                    let marker = document.getElementsByTagName('marker');

                    let { leftHand, reverse } = options.getDrawingProperty(d, directional);

                    let x1 = leftHand ? d.source.x : d.target.x,
                        y1 = leftHand ? d.source.y : d.target.y,
                        x2 = leftHand ? d.target.x : d.source.x,
                        y2 = leftHand ? d.target.y : d.source.y,
                        siblings = ctrl.getSiblingLinks(d.source, d.target),
                        siblingCount = d.distance || siblings.length,
                        median_link = (siblingCount + 1) / 2;

                    let getLineIndex = d3
                        .scalePoint()
                        .domain(siblings)
                        .range([1, siblingCount]);

                    function getPointsAngle(x1, y1, x2, y2) {

                        let angle = Math.atan((y2 - y1) / (x2 - x1)) / (Math.PI / 180);
                        let pointsAngle;

                        if (options.link_type == "mixed") {
                            pointsAngle = angle + (270 - (getLineIndex(d.index) + 1) * (180 / (siblingCount - 1)));
                        } else { pointsAngle = angle + (270 - (getLineIndex(d.index) - 1) * (180 / (siblingCount - 1))); }

                        /* 위치보정값 false 일 때 링크 위치 수정 */
                        if (!autoReverse && getLineIndex(d.index) < median_link && (d.source.x > d.target.x)) {
                            return pointsAngle + 180;
                        } else if (!autoReverse && getLineIndex(d.index) > median_link && (d.source.x < d.target.x)) {
                            return pointsAngle + 180;
                        } else { return pointsAngle; }

                    }

                    /* 접선의 좌표 구하기 */
                    function getContactPoint(angle) {
                        let radian = (angle) * (Math.PI / 180);

                        let x = Number(Math.cos(radian).toFixed(5)),
                            y = Number(Math.sin(radian).toFixed(5));

                        let r = options.node.r < 10 ? 10 : options.node.r;
                        let length_divideValue = (siblingCount > 2) ? siblingCount : Math.pow(siblingCount, 2);
                        let link_length = (r * options.link.stroke_width) / Math.pow(length_divideValue, 2);

                        return { x: x * link_length, y: y * link_length };
                    }

                    let angle = getPointsAngle(x1, y1, x2, y2);
                    let point = getContactPoint(angle);

                    marker[0].setAttribute('refX', options.getRefX());

                    /* 링크가 홀수 개이고 링크 중 가운데에 긋는 링크일경우 */
                    if (siblingCount % 2 != 0 && getLineIndex(d.index) == (siblingCount + 1) / 2) {
                        return ("M" + parseInt(x1) + "," + parseInt(y1) + "L" + " " + parseInt(x2) + "," + parseInt(y2));
                    } else { return ("M" + parseInt(x1 + point.x) + "," + parseInt(y1 + point.y) + "L" + " " + parseInt(x2 + point.x) + "," + parseInt(y2 + point.y)); }

                }.bind(ctrl);

                /**
                 * Sibling 개수에 따른 Arc 링크의 Path를 반환한다.
                 * @param d
                 * @param leftHand : true 일 경우 왼쪽에서 오른쪽 즉 순방향을 의미. 역방향일 경우 순방향으로 바꾸어 그리도록 한다.
                 * @returns {string}
                 */
                let drawArc = function (d, directional) {
                    let ctrl = this;
                    let { options } = ctrl;
                    let { leftHand, reverse } = options.getDrawingProperty(d, directional);

                    let x1 = leftHand ? d.source.x : d.target.x,
                        y1 = leftHand ? d.source.y : d.target.y,
                        x2 = leftHand ? d.target.x : d.source.x,
                        y2 = leftHand ? d.target.y : d.source.y,
                        dx = x2 - x1,
                        dy = y2 - y1,
                        siblings = ctrl.getSiblingLinks(d.source, d.target),
                        siblingCount = d.distance || siblings.length,
                        distance = Math.sqrt(dx * dx + dy * dy),
                        drx = distance,
                        dry = distance,
                        xRotation = 0,
                        largeArc = 0;

                    if (siblingCount > 1) {
                        // log(siblings);
                        let getArcIndex = d3
                            .scalePoint()
                            .domain(siblings)
                            .range([1, siblingCount]);

                        let getArcCurvature = d3
                            .scaleLog()
                            .base(Math.E)
                            .domain([10, 1])
                            .range([1, 4]);

                        if (!siblings.includes(d.index)) {
                            console.error(`datum not Found!!`, d);
                            return;
                        }

                        let curvature = getArcCurvature(getArcIndex(d.index));

                        // <editor-fold desc="[#거리에 따른 링크 형제 링크의 간격 보정]">

                        /*
                         * curvature 값이 작을수록 간격이 벌어짐(원에 가까워짐)
                         */
                        if (distance < 200) {
                            curvature = siblingCount < 5 ? 2 : (curvature * 2) / getArcIndex(d.index);
                        }

                        if (distance / siblingCount > 200) {
                            curvature = (curvature * siblingCount * 4) / getArcIndex(d.index);
                        }
                        // <!--</editor-fold desc="[#거리에 따른 링크 형제 링크의 간격 보정]">

                        drx = (drx / (1 + (1 / siblingCount) * (getArcIndex(d.index) - 1))) * curvature;
                        dry = (dry / (1 + (1 / siblingCount) * (getArcIndex(d.index) - 1))) * curvature;

                        const leftHandOffset = leftHand ? 0 : 1; /* 화면 노드 위치에 따른 보정값 */
                        const directionOffset = options.link.autoReverse ? 0 : d.source.index < d.target.index ? 0 : 1; /* 장비의 연결관계에 따른 보정값 */
                        const reverseOffset = reverse ? 1 : 0
                        const sweep = (getArcIndex(d.index) + leftHandOffset + directionOffset + reverseOffset) % 2;
                        // log(`${sweep} ${d.value}`);
                        // log(`dr=${dr}, drx=${drx}, dry=${dry}`);
                        // log(`curvature =  ${curvature}`);
                        return ("M" + x1 + "," + y1 + "A" + drx + ", " + dry + " " + xRotation + ", " + largeArc + ", " + sweep + " " + x2 + "," + y2
                        );
                    } else {
                        return "M" + x1 + "," + y1 + "L" + " " + x2 + "," + y2;
                    }
                }.bind(ctrl);

                /**
                 * 중간점을 갖는 링크의 Path를 반환한다.
                 * @param d
                 * @param leftHand
                 * @returns {string}
                 */
                let drawPath = function (d, directional) {
                    let ctrl = this;
                    let { options } = ctrl;
                    let { leftHand, reverse } = options.getDrawingProperty(d, directional);

                    let x1 = leftHand ? d.source.x : d.target.x,
                        y1 = leftHand ? d.source.y : d.target.y,
                        x2 = leftHand ? d.target.x : d.source.x,
                        y2 = leftHand ? d.target.y : d.source.y;

                    // S 대신 Q 를 사용해도 같은 결과
                    return ("M" + x1 + "," + y1 + " S " + d["intermediate"].x + "," + d["intermediate"].y + " " + x2 + "," + y2);
                }.bind(ctrl);

                try {
                    switch (type) {
                        case elementType.link_selector:
                            return fnDrawLinkSelector();
                        case elementType.link:
                            return fnDrawLink();
                    }
                } catch (e) {
                    console.error(e)
                }
            },

            /**
             * 링크컷 아이콘을 그린다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             * @returns {string}
             */
            drawLinkCutHandler: function (element, type, d, index, all) {
                let ctrl = this;
                let { options } = ctrl;

                // https://codepen.io/realjameal/pen/gpzZGw
                let id = ctrl.getLinkId(d, "link_path");
                let linkPathElement = document.getElementById(id);

                if (!linkPathElement) {
                    console.error(`drawLinkCutHandler not found :`, id, d);
                    return;
                }

                let pathLength = linkPathElement.getTotalLength();
                let prcnt = (50 * pathLength) / 100; // Path 의 50% 위치
                let pt = linkPathElement.getPointAtLength(prcnt);

                // let image = this;
                // let width = parseFloat(image.style.width);
                // let height = parseFloat(image.style.height);

                let outWidth = options.link.badge_size,
                    outHeight = options.link.badge_size;

                pt.x = Math.round(pt.x - outWidth / 2);
                pt.y = Math.round(pt.y - outHeight / 2);

                return ("translate(" + pt.x + "," + pt.y + ") scale(" + outWidth / 100 + " " + outHeight / 100 + ")");
            },

            /**
             * 드래그 시작 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            dragStartHandler: function (element, type, d, index, all) {
                let ctrl = this;
                let { simulation } = ctrl;

                if (!d3.event.active) {
                    simulation.alphaTarget(0.3).restart();
                }

                d.fx = d.x;
                d.fy = d.y;

                d3.select(element).classed(className.dragging, true);

                // d3.select(this).transition()
                //     .ease(d3.easeElastic)
                //     .duration(500)
                //     .attr('transform', function (d) {
                //         return ctrl.transformHandler.call(this, ctrl, elementType.node, d);
                //     });
            },

            /**
             * 드래그 중 발생하는 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            draggingHandler: function (element, type, d, index, all) {
                let ctrl = this;
                d.fx = d3.event.x;
                d.fy = d3.event.y;

                // start 에서 해야하는데 그렇게 되면 select 이벤트가 안먹는다
                // element.parentNode.appendChild(element);

                d3.select(element).moveToFront();
            },

            /**
             * 드래그 종료 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            dragEndHandler: function (element, type, d, index, all) {
                let ctrl = this;
                let { simulation } = ctrl;

                if (!d3.event.active) {
                    simulation.alphaTarget(0);
                }

                d.fx = null;
                d.fy = null;

                d3.select(element).classed(className.dragging, false);
            },

            /**
             * 노드 좌표 변경 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             * @returns {string}
             */
            transformHandler: function (element, type, d, index, all) {
                let ctrl = this;
                if (type == elementType.node) {
                    var a = 1;
                }

                return ("translate(" + d.x + "," + d.y + ")".concat(" scale(" + ctrl.getNodeWeightOption(element) + ")"));
            },

            /**
             * 클릭에 대한 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            clickHandler: function (element, type, d, index, all) {
                let ctrl = this;
                let { ctrlKey, shiftKey, altKey } = d3.event;

                if (altKey) {
                    log(`clickHandler:`, element, type, d);
                }

                ctrl.hideContextMenu();

                switch (ctrl.options.mode) {
                    case mouseMode.select:
                        onSelect();
                        break;
                    case mouseMode.edit:
                        if (ctrlKey) onAdd();
                        else if (shiftKey) onDelete(d);
                        else onSelect();
                        break;
                }
                d3.event.stopPropagation();

                function onSelect() {
                    if (type == elementType.svg) {
                        ctrl.clearSelection();
                    } else {
                        ctrl.selectElement.call(ctrl, d, type);
                    }

                    switch (type) {
                        case elementType.link:
                        case elementType.svg:
                            ctrl.options.onChangeLink();
                            break;
                    }
                }

                function onAdd() {
                    if (type == elementType.svg) {
                        ctrl.addNode(element);
                    } else {
                        onSelect();
                    }
                }

                function onDelete(d) {
                    if (d) {
                        switch (type) {
                            case elementType.node:
                                ctrl.deleteNode(d, element);
                                break;
                            case elementType.link:
                            case elementType.link_selector:
                                ctrl.deleteLink(d, element);
                                break;
                        }
                    } else {
                        onSelect();
                    }
                }
            },

            /**
             * linkOver 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            linkOverHandler: function (element, type, d, index, all) {
                let ctrl = this;
                ctrl.showLinkPortTooltip(element, d);
            },

            /**
             * linkOut 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            linkOutHandler: function (element, type, d, index, all) {
                let ctrl = this;
                ctrl.hideLinkPortTooltip(d);
            },

            /**
             * 모든 노드/링크 를 지운다.
             */
            clear: function () {
                let ctrl = this;
                let { svg } = ctrl;

                ctrl.clearSelection();

                let links = svg.select("g.links").selectAll("*");
                let nodes = svg.select("g.nodes").selectAll("*");
                let descs = svg.select("g.desc").selectAll("*");
                let interNodes = svg.select("g.intermediate_nodes").selectAll("*");

                links.remove();
                nodes.remove();
                descs.remove();
                interNodes.remove();
            },

            /**
             * 선택 영역을 초기화 한다.
             */
            clearSelection: function () {
                let ctrl = this;
                const classList = `${className.selected} ${className.focus}`;

                ctrl.hideContextMenu();
                d3.selectAll(".node_container").classed(classList, false);
                d3.selectAll(".link_container").classed(classList, false);
                ctrl.hideNodeTooltip();
                ctrl.hideLinkPortTooltip(null, 0);
                ctrl.setInfomation();

                if (ctrl.select) {
                    ctrl.fireEvent({
                        eventType: eventType.selectChanged,
                        target: null,
                        target_type: elementType.svg,
                        d: null,
                    });
                    ctrl.select = null;
                }
            },

            /**
             * 선택한 element 에 대한 설명을 출력한다.
             * @param type
             * @param d
             * @param element
             */
            setInfomation: function (type, d, element, slot = null) {
                if (type == undefined) {
                    document.querySelector(".properties").style.display = "none";
                    d3.select(".animation-blink").classed("animation-blink", false);
                    return;
                }

                document.querySelector(".properties").style.display = "block";

                switch (type) {
                    case elementType.node:
                        document.querySelector(".properties").classList.add("node-info");
                        document.querySelector(".properties").classList.remove("link-info");
                        document.querySelector(".node-info .alias > *:nth-child(2)").innerText = d.device_name;
                        document.querySelector(".node-info .id > *:nth-child(2)").innerText = d.id;
                        document.querySelector(".node-info .ip > *:nth-child(2)").innerText = d.ip;
                        document.querySelector(".node-info .type > *:nth-child(2)").innerText = d.device_type;
                        document.querySelector(".node-info .mac > *:nth-child(2)").innerText = d.mac;
                        document.querySelector(".node-info .alarm > *:nth-child(2)").innerText = d.alarm;
                        document.querySelector(".node-info .port > *:nth-child(2)").innerText = "";

                        if(slot == null){
                            slot = ".S" + Math.randomInt(10, 20);
                            d3.select(slot).classed("animation-blink", true);
                        } else{
                            if(slot.length > 1){
                                for(let i = 0; i< slot.length; i++){
                                    d3.select(slot[i]).classed("animation-blink", true);
                                }
                            } else { d3.select(slot[0]).classed("animation-blink", true); }
                        }
                        break;
                    case elementType.link:
                        document.querySelector(".properties").classList.add("link-info");
                        document.querySelector(".properties").classList.remove("node-info");
                        document.querySelector(".link-info .alias > *:nth-child(2)").innerText = d.source.device_name;
                        document.querySelector(".link-info .id > *:nth-child(2)").innerText = d.source.id;
                        document.querySelector(".link-info .ip > *:nth-child(2)").innerText = d.source.ip;
                        document.querySelector(".link-info .type > *:nth-child(2)").innerText = d.source.device_type;
                        document.querySelector(".link-info .mac > *:nth-child(2)").innerText = d.source.mac;
                        document.querySelector(".link-info .alarm > *:nth-child(2)").innerText = d.source.alarm;
                        document.querySelector(".link-info .port > *:nth-child(2)").innerText = d.source_port_disp;
                        document.querySelector(".link-info .alias > *:nth-child(3)").innerText = d.target.device_name;
                        document.querySelector(".link-info .id > *:nth-child(3)").innerText = d.target.id;
                        document.querySelector(".link-info .ip > *:nth-child(3)").innerText = d.target.ip;
                        document.querySelector(".link-info .type > *:nth-child(3)").innerText = d.target.device_type;
                        document.querySelector(".link-info .mac > *:nth-child(3)").innerText = d.target.mac;
                        document.querySelector(".link-info .alarm > *:nth-child(3)").innerText = d.target.alarm;
                        document.querySelector(".link-info .port > *:nth-child(3)").innerText = d.target_port_disp;

                        const link_alias = d3.select(document.getElementById(element.id).parentNode).select(".link_label").text();
                        document.querySelector(".link-info .link_alias > *:nth-child(2)").innerText = link_alias;
                        document.querySelector(".link-info .speed > *:nth-child(2)").innerText = d.speed || "";
                        document.querySelector(".link-info .status > *:nth-child(2)").innerText = d.status || "";
                        break;
                    default:
                }
            },

            /**
             * element 를 선택한다.
             * @param id
             * @param type
             */
            selectElement: function (d, type) {
                let ctrl = this;

                if (ctrl.options.select.isOnly) {
                    ctrl.clearSelection();
                }

                let element;
                switch (type) {
                    case elementType.node:
                        element = ctrl.getNodeElement(d);
                        break;
                    case elementType.link:
                        element = ctrl.getLinkElement(d);
                        break;
                    default:
                        break;
                }

                if (!element) return;

                ctrl.select = { d, elements: element };
                let selected = element.classList.contains(className.selected);

                if (ctrl.options.select.isOnly) {
                    // 1개의 선택영역 만 가능
                    if (!selected) {
                        element.classList.add(className.selected);
                        ctrl.setInfomation(type, d, element);
                    }
                } else {
                    // 다수의 선택영역 가능
                    element.classList.toggle(className.selected);
                }

                // 맨 위로 올리기 위해 append를 해준다.
                d3.select(element).moveToFront();

                if (type == elementType.link) {
                    ctrl.showLinkPortTooltipByDatum(d);
                    setTimeout(() => {
                        ctrl.clearSelection();
                    }, 5000);
                }

                ctrl.fireEvent({
                    eventType: eventType.selectChanged,
                    target: element,
                    target_type: type,
                    d: d,
                });
            },

            /**
             * 선택한 링크 element 의 d3.select를 반환한다.
             * @returns {*}
             */
            getSelectLink: function () {
                let ctrl = this;
                let { svg } = ctrl;

                let selected = svg.select(".link_container.selected");
                return selected;
            },

            /**
             * 해당 링크와 연결되 노드가 같은 링크의 데이터를 반환한다.
             * @param source
             * @param target
             * @returns {Array}
             */
            getSiblingLinks: function (source, target) {
                let ctrl = this;
                let links = ctrl.getLinkAll();
                let siblings = [];
                for (let i = 0; i < links.length; ++i) {
                    if ((links[i].source.id == source.id && links[i].target.id == target.id) || (links[i].source.id == target.id && links[i].target.id == source.id)){
                        siblings.push(links[i].index);
                    }
                }
                return siblings;
            },

            /**
             * 노드 datum과 연결된 링크 반환한다.
             * @param d
             * @returns {Array}
             */
            getContactLinks: function (d) {
                let ctrl = this;
                let links = ctrl.getLinkAll();
                let result = [];
                for (let i = 0; i < links.length; ++i) {
                    if (!links[i].element_id) {
                        continue;
                    }
                    if (links[i].source.id == d.id || links[i].target.id == d.id)
                        result.push(links[i]);
                }
                return result;
            },

            /**
             * 해당 중간점을 지나는 모든 중간점 링크를 반환한다.
             * @param array
             * @returns {Array}
             */
            getIntermediateLinks: function (array) {
                let ctrl = this;
                let links = ctrl.getLinkAll();
                let result = [];
                for (let i = 0; i < links.length; ++i) {
                    if (!!links[i].id) {
                        continue;
                    }
                    if (
                        array.includes(links[i].source.nodeId) ||
                        array.includes(links[i].target.nodeId)
                    )
                        result.push(links[i]);
                }
                return result;
            },

            /**
             * 링크와 연결된 포트 정보를 출력한다.
             * @param element
             * @param d
             */
            showLinkPortTooltipByDatum: function (d) {
                let ctrl = this;

                try {
                    let selectElement = ctrl.getLinkElement(d);
                    ctrl.showLinkPortTooltip(selectElement, d);
                } catch (e) {
                    console.error(e);
                }
            },

            /**
             * 링크와 연결된 포트 정보를 출력한다.
             * @param element
             * @param d
             */
            showLinkPortTooltip: function (element, d) {
                let ctrl = this;
                let { options } = ctrl;
                let { svg, link_source_tip, link_dest_tip } = ctrl;

                if (!element) {
                    // let selected = ctrl.getSelectLink();
                    // if (!selected.empty()) {
                    //     ctrl.showLinkPortTooltipByDatum(selected.datum());
                    // }
                    return;
                }

                let src_node, dst_node;
                d = d || d3.select(element).datum();

                /*
                          focus on target link
                      */
                d3.select(element.parentNode).classed(className.focus, true);

                // <editor-fold desc="[#focus on target and source node and show tips.]">
                svg
                    .select("g.nodes")
                    .selectAll("g.node_container")
                    .each(function (node_d) {
                        if (node_d.id == d.source.id) {
                            src_node = d3.select(element).classed("focus focusing", true);
                        } else if (node_d.id == d.target.id) {
                            dst_node = d3.select(element).classed("focus focusing", true);
                        }
                    });

                // <!--</editor-fold desc="[#focus on target and source node and show tips.]">

                try {
                    if (src_node.size() <= 0 || dst_node.size() <= 0) return;
                } catch (e) {
                    return;
                }

                // <editor-fold desc="[#calculate tooltip position]">
                const offset = 5;
                let src_dir,
                    dst_dir,
                    src_offset = [0, 0],
                    dst_offset = [0, 0],
                    min_distance =
                        Math.max(options.node.width, options.node.height) ||
                        options.node.r,
                    x_distance = src_node.datum().x - dst_node.datum().x,
                    y_distance = src_node.datum().y - dst_node.datum().y;

                if (Math.abs(x_distance) > Math.abs(y_distance)) {
                    if (x_distance > 0) {
                        src_dir = "e";
                        src_offset[1] = offset;
                        dst_dir = "w";
                        dst_offset[1] = -offset;
                    } else {
                        src_dir = "w";
                        src_offset[1] = -offset;
                        dst_dir = "e";
                        dst_offset[1] = offset;
                    }

                    if (Math.abs(y_distance) > min_distance) {
                        if (y_distance > 0) {
                            src_dir = "s" + src_dir;
                            src_offset = [-offset, -(Math.sign(src_offset[1]) * offset)];
                            dst_dir = "n" + dst_dir;
                            dst_offset = [offset, -(Math.sign(dst_offset[1]) * offset)];
                        } else {
                            src_dir = "n" + src_dir;
                            src_offset = [offset, -(Math.sign(src_offset[1]) * offset)];
                            dst_dir = "s" + dst_dir;
                            dst_offset = [-offset, -(Math.sign(dst_offset[1]) * offset)];
                        }
                    }
                } else {
                    if (y_distance > 0) {
                        src_dir = "s";
                        src_offset[0] = offset;
                        dst_dir = "n";
                        dst_offset[0] = -offset;
                    } else {
                        src_dir = "n";
                        src_offset[0] = -offset;
                        dst_dir = "s";
                        dst_offset[0] = offset;
                    }

                    if (Math.abs(x_distance) > min_distance) {
                        if (x_distance > 0) {
                            src_dir = src_dir + "e";
                            src_offset = [-(Math.sign(src_offset[0]) * offset), -offset];
                            dst_dir = dst_dir + "w";
                            dst_offset = [-(Math.sign(dst_offset[0]) * offset), offset];
                        } else {
                            src_dir = src_dir + "w";
                            src_offset = [-(Math.sign(src_offset[0]) * offset), offset];
                            dst_dir = dst_dir + "e";
                            dst_offset = [-(Math.sign(dst_offset[0]) * offset), -offset];
                        }
                    }
                }
                // <!--</editor-fold desc="[#calculate tooltip position]">

                link_source_tip
                    .direction(src_dir)
                    .offset(src_offset)
                    .html("<strong>" + d.source_port_disp + "</strong>")
                    .show(src_node.node());

                link_dest_tip
                    .direction(dst_dir)
                    .offset(dst_offset)
                    .html("<strong> " + d.target_port_disp + "</strong>")
                    .show(dst_node.node());

                ctrl.over_link_src = src_node.node();
                ctrl.over_link_dst = dst_node.node();


                // log('over_link_src:', src_node.node());
                // log('over_link_dst:', dst_node.node());
            },

            /**
             * 팝업메뉴를 감춘다
             */
            hideContextMenu: function () {
                d3.selectAll(".jctx").style("display", "none");
            },

            /**
             * 노드 툴팁을 감춘다
             */
            hideNodeTooltip: function () {
                let ctrl = this;
                let { node_tip } = ctrl;
                node_tip.hide();
            },

            /**
             * 링크와 연결된 포트 정보를 감춘다.
             * @param element
             * @param d
             * @param delay
             * @returns {Promise<any>}
             */
            hideLinkPortTooltip: function (d, delay) {
                let ctrl = this;
                let { svg, link_source_tip, link_dest_tip } = ctrl;
                // let src_node, dst_node;
                delay = delay == undefined ? 200 : delay;

                return new Promise((resolve, reject) => {
                    if (delay > 0) {
                        setTimeout(hide, delay || 200);
                    } else {
                        hide();
                    }

                    function hide() {
                        /* move focus away from link. */
                        let focus = svg
                            .selectAll(".link_container:not(.selected).focus")
                            .classed("focus focusing", false);

                        if (focus.size() > 0) {
                            var a = 1;
                        }

                        link_source_tip.hide();
                        link_dest_tip.hide();

                        let selected = ctrl.getSelectLink();
                        if (!selected.empty()) {
                            ctrl.showLinkPortTooltipByDatum(selected.datum());
                        }

                        resolve();
                    }
                });
            },

            /**
             * ViewBox 의 크기를 반환한다.
             * @returns {{width: (number|*|string), height: (number|*|string)}}
             */
            getViewBoxSize: function () {
                let ctrl = this;
                let { svg } = ctrl;
                let width,
                    height,
                    viewBox = svg.attr("viewBox");
                if (viewBox) {
                    let arr = viewBox.split(" ");
                    width = arr[2];
                    height = arr[3];
                } else {
                    width = +svg.style("width").replace("px", "");
                    height = +svg.style("height").replace("px", "");
                }

                return { width, height };
            },

            /**
             * 선택 시 노드 확대 가중치 설정값을 반환한다.
             * @param element
             * @returns {number}
             */
            getNodeWeightOption: function (element) {
                let ctrl = this;

                let selected = d3.select(element).classed(className.selected)
                    ? ctrl.options.select.weight
                    : 1;
                let dragging = d3.select(element).classed(className.dragging)
                    ? ctrl.options.node.drag_weight
                    : 1;
                return Math.max(selected, dragging);
            },

            /**
             * 노드를 아이콘 사용 여부 설정값을 반환한다.
             * @returns {boolean}
             */
            isIconNodeOption: function () {
                let ctrl = this;
                return ctrl.options.node.displayImage == "icon";
            },

            /**
             * 노드 너비 설정값을 반환한다.
             * @returns {*}
             */
            getNodeWidthOption: function () {
                let ctrl = this;
                if (ctrl.isIconNodeOption()) return ctrl.options.node.r;
                else return ctrl.options.node.width;
            },

            /**
             * 노드 높이 설정값을 반환한다.
             * @returns {*}
             */
            getNodeHeightOption: function () {
                let ctrl = this;
                if (ctrl.isIconNodeOption()) return ctrl.options.node.r;
                else return ctrl.options.node.height;
            },

            /**
             * 줌을 초기화 한다.
             * @param duration
             */
            resetZoom: function (duration = 0) {
                let { svg, zoom } = this;

                const zoomDefault = () => {
                    svg.transition().duration(duration).call(
                        zoom.transform,
                        d3.zoomIdentity,
                        d3.zoomTransform(svg.node()).invert([])
                    );
                }

                try {
                    const ctrl = this;
                    const config = ctrl.getConfigAll()
                    const ZOOM_SCALE = (config.zoom && config.zoom.initScale) ? config.zoom.initScale : 1;
                    const ZOOM_POS = config.zoom && config.zoom.initPos;

                    if (ZOOM_POS) {
                        ctrl.zoomIn({ type: 'pos', scale: ZOOM_SCALE, ...ZOOM_POS, duration : 750 });
                        return
                    } 
                } catch (error) {
                }

                zoomDefault()
                
            },

            /**
             * generate 줌인
             * @param obj
             */
            zoomIn: function (obj) {
                if (!obj) { return }

                let ctrl = this;
                let scale = obj.scale || 1;
                let duration = obj.duration || 0;

                switch (obj.type) {
                    case 'pos':
                        let x = obj.x || 0;
                        let y = obj.y || 0;
                        ctrl.zoomInByPos(x, y, scale, duration);
                        break;
                    case 'node':
                        let node = ctrl.data.nodes.find(v => v[obj.key] == obj.value);
                        if (node == undefined) { return }
                        ctrl.zoomInByNode(node, scale, duration);
                        break;
                    case 'link':
                        let link = ctrl.data.links.find(v => v[obj.key] == obj.value);
                        if (link == undefined) { return }
                        ctrl.zoomInByLink(link, scale, duration);
                        break;
                    default:
                        break;
                }
            },

            /**
             * [x,y] 값으로 줌인 한다.
             * @param x
             * @param y
             * @param scale
             * @param duration
             */
            zoomInByPos: function (x, y, scale = 1, duration = 0) {
                let { svg, zoom } = this;

                if (!x || !y) {
                    return;
                }

                x = Math.floor(x);
                y = Math.floor(y);
                svg.transition().duration(duration).call(
                    zoom.transform,
                    d3.zoomIdentity.translate(950, 500).scale(scale).translate(-x, -y)
                );
            },
            /**
             * 노드 값으로 줌인 한다.
             * @param node
             * @param scale
             * @param duration
             */
            zoomInByNode: function (node, scale, duration) {
                let ctrl = this;
                ctrl.zoomInByPos(node.x, node.y, scale, duration);
            },
            zoomInByRandomNode: function (scale = 5, duration = 1000) {
                let ctrl = this;
                let array = ctrl.data.nodes.filter(n => !!n.id);
                let index = Math.floor(Math.random() * array.length);
                ctrl.zoomInByNode(array[index], scale, duration);
            },
            zoomInByRandomLink: function (scale = 5, duration = 1000) {
                let ctrl = this;
                let array = ctrl.data.links.filter(n => !!n.id);
                let index = Math.floor(Math.random() * array.length);
                ctrl.zoomInByLink(array[index], scale, duration);
            },
            /**
             * 링크 값으로 줌인 한다.
             * @param link
             * @param scale
             * @param duration
             */
            zoomInByLink: function (link, scale, duration) {
                let ctrl = this;
                let nodea = ctrl.data.nodes.find(v => v.id == link.source_id);
                let nodez = ctrl.data.nodes.find(v => v.id == link.target_id);
                let pos = [
                    (nodea.x + nodez.x) / 2,
                    (nodea.y + nodez.y) / 2
                ]

                ctrl.zoomInByPos(pos[0], pos[1], scale, duration);
            },
            /**
             * 줌인 테스트
             * @param type
             * @param search
             */
            // zoomInTest: function (type, search) {
            //     let ctrl = this;
            //     switch (type) {
            //         case 'node':
            //             ctrl.zoomInByNode(
            //                 ctrl.data.nodes.find(v => v.device_name == search) || ctrl.data.nodes.find(v => v.id == search),
            //                 5, 1000
            //             );
            //             break;
            //         case 'link':
            //             ctrl.zoomInByLink(
            //                 ctrl.data.links.find(v => v.id == search),
            //                 5, 1000
            //             );
            //             break;
            //     }
            // }

            // <!--</editor-fold desc="[#Map2d.prototype]">
        };

        window.onload = () => { 
            if(!window.libDev) return;

            setTimeout(function () {

                let map = new Map2d();
                let options = map.options;

                map.initialize();
                map.load();

                map.addEventListener(Map2d.eventType.selectChanged, e => {
                    log('selected changed : ', e);
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
                        if (rollback) {
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
                        changeOption.bind(options.node, 'displayField', 'id', true), options.onChangeNodeDisplayField,
                        changeOption.bind(options.node, 'displayImage', 'icon', true), options.load,
                        changeOption.bind(options.node, 'r', 100, true), options.onChangeNodeR,
                        changeOption.bind(options.node, 'width', 100, true), options.onChangeNodeWidthHeight,
                        changeOption.bind(options.node, 'height', 100, true), options.onChangeNodeWidthHeight,
                        changeOption.bind(options.node, 'badge_size', 100, true), options.onChangeNodeBadgeSize,
                        changeOption.bind(options.node, 'invisible_node', 100, true), options.onChangeVisibleNodeOption,
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

                let gui = new dat.GUI();
                // gui.hide();

                gui.add(options, 'fileName', [
                    // 'data_mba_1.json', 'data_mba_2.json', 'data_nia_1.json', 'data_nia_2.json', 'data_nia_3.json', 'data_nia_4.json', 'data_nia_5.json'
                    'data_mba.json', 'data_nia_1.json', 'data_nia_2.json', 'data_nia_3.json', 'data_nia_4.json', 'data_nia_5.json', 'data_nia_ip_1.json', 'data_nia_ip_2.json', 'data_nia_ip_3.json'
                ]).name('파일').onChange(e => { options.save_map = false; options.load(e) }).listen();
                gui.add(options, 'save').name('맵저장')
                gui.add(options, 'exportToFile').name('JSON 파일로 저장')
                gui.add(options, 'save_map').name('저장된 맵 보기').onChange(e => options.load()).listen();
                gui.add(options, 'link_type', ['curved', 'arc', 'line', 'mixed']).name('링크 타입').onChange(options.onChangeAnimated).listen();
                gui.add(options.select, 'node_tour').name('노드 순환').onChange(options.onChangeNodeSelect).listen();
                gui.add(options.select, 'link_tour').name('링크 순환').onChange(options.onChangeLinkSelect).listen();
                gui.add(options, 'mode', [mouseMode.select, mouseMode.edit]).name('클릭 시 액션');
                gui.add(options, 'zoomInTest').name('줌인테스트');
                gui.open();

                var fTest = gui.addFolder('테스트');
                fTest.add(options, 'autoTest').name('자동테스트')
                fTest.add(options, 'onTestLinkAlarmOccur').name('링크장애 발생/해지')
                fTest.open();

                let fNode = gui.addFolder('노드');
                fNode.add(options.node, 'displayField', ['mac', 'ip', 'device_name', 'id']).name('레이블').onChange(options.onChangeNodeDisplayField).listen();
                fNode.add(options.node, 'fix_size').name('폰트사이즈고정').onChange(options.onChangeScale).listen(); //
                fNode.add(options.node, 'displayImage', ['image', 'icon']).name('이미지').onChange(e => options.load()).listen();
                fNode.add(options.node, 'show_r').name('범위표시').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'r', 0, 150).name('범위').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'width', 10, 200).name('이미지너비').onChange(options.onChangeNodeWidthHeight).listen();
                fNode.add(options.node, 'height', 10, 200).name('이미지높이').onChange(options.onChangeNodeWidthHeight).listen();
                fNode.add(options.node, 'badge_size', 0, 100).name('뱃지크기').onChange(options.onChangeNodeBadgeSize).listen();
                fNode.addColor(options.node, 'stroke_color').name('stroke 색상').onChange(options.onChangeNodeR).listen();
                fNode.addColor(options.node, 'color').name('색상').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'opacity', 0.0, 1.0).name('투명도').onChange(options.onChangeNodeR).listen();
                fNode.add(options.node, 'invisible_node').name('사용여부').onChange(options.onChangeVisibleNodeOption).listen();
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
                fLinkTraffic.addColor(options.link_traffic, 'arrow_color').name('화살표색상').onChange(options.onChangeLinkTrffic).listen();
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

            }, 100);

        };

        // 두 점간의 기울기 각도 구하기
        // window.getDegrees = function (x1, y1, x2, y2) {
        //     return Math.atan((y2 - y1) / (x2 - x1)) * (180 / Math.PI)
        // }

        // 각도를 입력하면 좌표를 출력
        // window.getContactPoint = function (degree) {
        //     let radian = degree * (Math.PI / 180)
        //     return { x: Number(Math.cos(radian).toFixed(5)), y: Math.sin(radian) }
        // }

    }
)(typeof exports !== 'undefined' ? exports : this);
