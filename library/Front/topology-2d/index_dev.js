d3.selection.prototype.selectAppend = function (name) {
    var select = d3.selector(name),
        create = d3.creator(name);
    return this.select(function () {
        return select.apply(this, arguments)
            || this.appendChild(create.apply(this, arguments));
    });
};

let fnConvert = {
    arrange_debug: function (json) {
        const arrNodes = json.nodes
        const arrLinks = json.links
        
        // create random link
        if(false) {
            const max_node = arrNodes.length
            const max_link = 20
            for (let index = 0; index < max_link; index++) {
                const sid = String(Math.randomInt(0, max_node))
                const tid = String(Math.randomInt(0, max_node))
                arrLinks.push(
                    { "id": `${sid}-${tid}`, "source_id": sid, "target_id": tid, status: 1 },
                )
            }
        }
        
        return { nodes: arrNodes, links: arrLinks, config: json.config }
    },
    performance_debug: function (json) {
        var arrNodes = arrLinks = []
        var static_pos = [
            {x: 0, y: 0},
            {x: 1920, y: 0},
            {x: 960, y: 540},
            {x: 0, y: 1080},
            {x: 1920, y: 1080},
        ]

        arrNodes = static_pos.reduce((acc, cur, index) => {
            const myid =`${cur.x},${cur.y}`
            if (acc.length > 0) {
                const prev = acc[index - 1]
                arrLinks.push(
                    { "id": `${prev.id}-${myid}`, "source_id": prev.id, "target_id": myid, status: Math.randomInt(-1, 1) },
                )
            }

            acc.push({
                "id": myid,
                "device_name": `(${cur.x},${cur.y})`,
                "device_type": "0",
                "fx": cur.x,
                "fy": cur.y
            })
            return acc;
        }, [])

        const max_node = 100
        const max_link = 100

        for (let index = 0; index < max_node; index++) {
            const x = Math.randomInt(0, 1920)
            const y = Math.randomInt(0, 1080)
            arrNodes.push({
                "id": String(index),
                "device_name": index,
                "device_type": "0",
                "fx": x,
                "fy": y
            })
        }

        for (let index = 0; index < max_link; index++) {
            const sid = String(Math.randomInt(0, max_node))
            const tid = String(Math.randomInt(0, max_node))
            arrLinks.push(
                { "id": `${sid}-${tid}`, "source_id": sid, "target_id": tid, status: 1 },
            )
        }

        return { nodes: arrNodes, links: arrLinks, config: json.config }
    },
    rca_v2: function (json) {
        const data = json.data || []
        const arrNodeType = ['SERVICE', 'MSPP', 'PCM', 'PTN', 'POTN', 'ROADM', 'FDF']
        let arrNodes = [];
        let arrLinks = [];
        let locationPath = {};
        let lineHeight = 100;
        let groupHeight = lineHeight * 1.5;
        let indent = lineHeight * 1.5;
        let cloneable = false
        const level0 = data.find(e => e.lv === 0)
        const level1 = data.filter(e => e.lv === 1)
        let hierarchyForDebug = {}

        function crossCheckAnd(row, property, value1, value2) {
            if(!(row && property)) return false
            const propertyName = (surfix) => `${property}${surfix}`
            return  (row[propertyName('a')] === value1 && row2[propertyName('z')] === value2) || 
                    (row[propertyName('z')] === value2 && row2[propertyName('a')] === value1) 
        }

        function crossCheckOr(row, property, value) {
            if(!(row && property)) return false
            const propertyName = (surfix) => `${property}${surfix}`
            return  row[propertyName('a')] === value || row[propertyName('z')] === value 
        }

        // 노드 ID를 반환한다.
        function getNodeId(row, surfix) {
            const {group, lv} = row
            const nescode = row[`nescode${surfix}`] || '서비스'
            // group::level::id
            return `${group}::${lv}::${nescode}`
        }

        // data 를 분석하여 그룹/레벨을 생성하고, 그에 따른 nodes, links 를 생성한다.
        function normalize() {
            if (!level0) {
                return
            }
            
            // 진기범대리님이 instlocation 을 기준으로 level1 을 추출하라고 했는데... 
            // 주석처리 - 임시로 생략함. 즉 level1을 그대로 사용함.
            // 나중에 필요하면 다시 복원한다
            if(false) {
                let existFullPathlevel1 = level1.some(e => crossCheckAnd(e, 'instlocation', level0.instlocationa, level0.instlocationz))
                if(existFullPathlevel1) {
                    // level0 의 a-z 양단 location 을 가지는 level1 이 있는 경우, 양단 location 중 하나 이상 같으면 별도의 그룹으로 판단
                    level1 = level1.filter(e => crossCheckOr(e, 'instlocation', level0.instlocationa) || crossCheckOr(e, 'instlocation', level0.instlocationz))
                } else {
                    let {instlocationa, instlocationz} = level0
                    let arrInstlocation = []

                    for (var x in level1) {
                    }
                }
            }

            const hierarchy = {}
            const idsInGroups = level1.map(d => [d.tc])
            const indexInGroups = []
            const visualData = [level0].map(d => (d.group = 1, d))
            const clone = (obj) => JSON.parse(JSON.stringify(obj))

            data.forEach(d => {
                if (d.lv < 1) return true

                idsInGroups.forEach((g, i) => {
                    if(g.includes(d.asn) || g.includes(d.tc)) {
                        g.push(d.tc)
                        indexInGroups[i] = (indexInGroups[i] || []).concat(d.index)
                    }
                })
            });
            // output => indexInGroups = [[1, 3, 4, 5, 6, 7, 8, 10, 13, 14, 15, 16], [2, 8, 9, 10, 11, 12, 16, 17, 18]]
            
            indexInGroups.forEach((arr, i) => {
                const groupName = `G${i}`
                const group = hierarchy[groupName] || {}
                arr.forEach(index => {
                    const row = clone(data[index])
                    const levelName = `L${row.lv}`
                    row.group = i
                    group[levelName] = (group[levelName] || []).concat(row)
                    hierarchyForDebug[`${groupName}#${levelName}#${row.index}`] = JSON.parse(JSON.stringify(row, ['instlocationa', 'instlocationz', 'sysnamea', 'sysnamez']))
                    visualData.push(row)
                })
                hierarchy[groupName] = group
            })
            // output 
                // => hierarchy = { G0: { L1: [], L2: [], L3: [] }, G1: { L1: [], L2: [], L3: [] } }
                // => hierarchyForDebug = [ G0#L1#1: {}, G0#L2#3: {} ... ]

            // gennerate links and nodes
            visualData.forEach(d => gennerateLinkData(d))
            log('arrNodes', arrNodes)
            log('arrLinks', arrLinks)

            let mapIdToNode = assignRef(arrNodes, arrLinks)
            log('mapIdToNode', mapIdToNode)

            return hierarchy
        }
     
        function gennerateNodeData (row, nodeDir = 'a') {
            const surfix = nodeDir.toLowerCase()
            if(!['a', 'z'].includes(surfix)) return null
            const propertyName = (property) => `${property}${surfix}`
            const sysname = row[propertyName('sysname')] 
            const nescode = row[propertyName('nescode')] || sysname
            const nodeType = getNodeType(row, surfix)
            if (!(sysname && nodeType)) {
                console.error(`노드 생성 실패 : sysname=${sysname}, nescode=${nodeType}`);
                return null
            }
            const id = getNodeId(row, nodeDir)
            const findNode = arrNodes.find(n => n.id === id)
            if (findNode) return findNode
            const node = {
                group: row.group,
                lv: row.lv,
                index: row.index,
                id: id,
                device_name: sysname,
                device_type: getNodeType(row, surfix),
                sysname: sysname,
                nescode: nescode,
                pportid: row[propertyName('pportid')],
                instlocation: row[propertyName('instlocation')],
                debugDesc: `nescode${surfix} 노드 생성`
            }

            arrNodes.push(node)
            return node
        }

        function gennerateLinkData(row) {
            if (!row) return
            const nodea = gennerateNodeData(row, 'a')
            const nodez = gennerateNodeData(row, 'z')
            if (!(nodea && nodez)) {
                console.error(`링크 생성 실패 : nodea=${nodea}, nodez=${nodez}`);
                return null
            }
            const id = `${nodea.id}-${nodez.id}`
            const findLink = arrLinks.find(l => l.id === id)
            if (findLink) return findLink

            addLocationPath(nodea, nodez)

            const link = {
                id: id,
                source_id: nodea.id,
                target_id: nodez.id,
                dashLine: nodea.lv === nodez.lv ? '' : '1 5',
                debugDesc: `lv.${nodea.lv}-lv.${nodea.lv} 링크 생성`
            }
            
            Object.assign(link, {
                group: row.group,
                lv: row.lv,
                index: row.index,
                llnum: row.llnum,
                asn: row.asn,
                tc: row.tc,
                tcname: row.tcname,
                pportida: row.pportida,
                ptpnamea: row.ptpnamea,
                pportidz: row.pportidz,
                ptpnamez: row.ptpnamez,
                debugDesc: link.debugDesc + `(tc:${row.tc})`
            })

            arrLinks.push(link)
            return link

        }

        function addLocationPath(nodea, nodez) {
            const locationa = nodea.instlocation
            const locationz = nodez.instlocation
            if(locationa && locationz) {
                locationPath[locationa] = (locationPath[locationa] || new Set()).add(locationz)
                locationPath[locationz] = (locationPath[locationz] || new Set()).add(locationa)
            }
        }

        function getNodeType(row, surfix = 'a') {
            if(row.maincls === "5") return 'SERVICE'
            const equipType = row[`equiptype${surfix}`]
            let nodeType = 'UNKNOWN'    
            try {
                let uppercaseType = equipType.toUpperCase();
                nodeType = arrNodeType.find(t => uppercaseType.startsWith(t));
            } catch (error) {
            }
            return nodeType
        }

        function findLocationPath(path = [], next, end, result = [], depth = 0) {
            depth = depth    + 1
            if(!next) return []

            let pathString = (path.push(next), path.join('/'))
            const arrNext = [...locationPath[next]].filter(p => !path.includes(p))
            arrNext.forEach((n, i) => {
                const newPath = [...path, n]
                // log(newPath)

                pathString = path.join('/')
                const findIndex = result.findIndex(r => pathString.includes(r) )

                if (findIndex < 0) {
                    result.push(pathString)
                } else {
                    result[findIndex] = pathString
                }

                if(!end || next !== end) {
                    findLocationPath(pathString.split('/'), n, end, result, depth)
                }
            })
            return result
        }

        const b = normalize();
        log(b)

        const arrlocationOrder = findLocationPath([], 'BSDLB0011'/* level0.instlocationa, level0.instlocationz */)
            .sort().reverse()
            .reduce((acc, cur) => {
                if (acc.filter(a => a.includes(cur)).length === 0) acc.push(cur)
                return acc
            }, [])
            .sort()
        
        log(JSON.stringify(arrNodes, ['device_name', 'instlocation']))
        
        // return { nodes: arrNodes, links: arrLinks, config: json.config }
        return this.rca(json)
    },
    rca: (json) => {
        const data = json.data || []
        const llname = (data[0] && data[0].llname) || ''
        const services = ['서비스A', '서비스Z']
        const arrNodeType = ['SERVICE', 'MSPP', 'PCM', 'PTN', 'POTN', 'ROADM', 'FDF']
        const instlocationa = (data[0] && data[0].tc_instlocationa) || ''
        const instlocationz = (data[0] && data[0].tc_instlocationz) || ''
        let arrNodes = [];
        let arrLinks = [];

        const getNodeType = (equipType = '') => {
            let uppercaseType = equipType.toUpperCase();
            let nodeType = arrNodeType.find(t => uppercaseType.startsWith(t));
            return nodeType || 'UNKNOWN'
        }

        services.forEach((node, index, array) => {
            arrNodes.push({
                id: node,
                device_name: node,
                device_type: 'SERVICE',
                instlocation: [instlocationa, instlocationz][index],
                fy: 100,
                debugDesc: "서비스 노드 생성"
            });

            if (index > 0) {
                let node0 = array[index - 1]
                arrLinks.push({
                    id: `${node0}-${node}`,
                    name: llname,
                    source_id: node0,
                    target_id: node,
                    debugDesc: "서비스 링크 생성"
                });
            }
        })

        for (const row of data) {
            arrNodes.push({
                id: row.nescodea,
                device_name: row.sysnamea || row.nealiasa,
                device_type: getNodeType(row.equiptypea || row.nealiasa),
                sysname: row.sysnamea,
                nealias: row.nealiasa,
                nescode: row.nescodea,
                pportid: row.pportida,
                instlocation: row.instlocationa,
                debugDesc: "nescodea 노드 생성"
            });
            arrNodes.push({
                id: row.nescodez,
                device_name: row.sysnamez || row.nealiasz,
                device_type: getNodeType(row.equiptypez || row.nealiasz),
                sysname: row.sysnamez,
                nealias: row.nealiasz,
                nescode: row.nescodez,
                pportid: row.pportidz,
                instlocation: row.instlocationz,
                debugDesc: "nescodez 노드 생성"
            });

            arrLinks.push({
                id: `${row.nescodea}-${row.nescodez}`,
                name: row.crname,
                source_id: row.nescodea,
                target_id: row.nescodez,
                llnum: row.llnum,
                llname: row.llname,
                ll: row.ll,
                cr: row.cr,
                crname: row.crname,
                ptpnamea: row.ptpnamea,
                ptpnamez: row.ptpnamez,
                debugDesc: "nescodea-nescodez 링크 생성"
            });

            // 일시적으로 wdmname 노드 및 링크는 감추도록 한다. 
            // continue

            if (row.wdmnamea) {
                let id = row.wdmnescodea || row.wdmnamea
                arrNodes.push({
                    id: id,
                    device_name: row.wdmnamea,
                    device_type: getNodeType(row.wdmequiptypea || "ROADM"),
                    debugDesc: "wdmnamea 노드 생성"
                });

                arrLinks.push({
                    id: `${row.nescodea}-${id}`,
                    source_id: row.nescodea,
                    target_id: id,
                    dashLine: '5 5',
                    debugDesc: "nescodea-wdmnamea 링크 생성 (dash)"
                });
            }

            if (row.wdmnamez) {
                let id = row.wdmnescodez || row.wdmnamez
                arrNodes.push({
                    id: id,
                    device_name: row.wdmnamez,
                    device_type: getNodeType(row.wdmequiptypez || "ROADM"),
                    debugDesc: "wdmnamez 노드 생성"
                });

                arrLinks.push({
                    id: `${row.nescodez}-${id}`,
                    source_id: row.nescodez,
                    target_id: id,
                    dashLine: '5 5',
                    debugDesc: "nescodez-wdmnamez 링크 생성 (dash)"
                });
            }
        }

        // let id1 = '남청중-ROADM-8005'
        // let id2 = 'CBCJ16583'
        // arrLinks.push({
        //     id: `${id1}-${id2}`,
        //     source_id: id1,
        //     target_id: id2,
        //     dashLine: '1 10',
        //     debugDesc: "테스트링크"
        // });

        // id1 = '영동중-ROADM-8103'
        // id2 = 'CBYD02093'
        // arrLinks.push({
        //     id: `${id1}-${id2}`,
        //     source_id: id1,
        //     target_id: id2,
        //     dashLine: '1 10',
        //     debugDesc: "테스트링크"
        // });

        arrNodes = arrNodes.deduplication('id')
        arrLinks = arrLinks.deduplication('id')

        let mapIdToNode = assignRef(arrNodes, arrLinks)
        let sortedNodes = arrNodes.slice().sort( (a, b) => {
            // device_type > instlocation > linkCount 순으로 정렬
    
            if (a.device_type < b.device_type) {
                return -1;
            } else if (a.device_type > b.device_type) {
                return 1;
            } else {
                if (a.instlocation < b.instlocation) {
                    return -1;
                } else if (a.instlocation > b.instlocation) {
                    return 1;
                } else {
                    return a.$nodes.length - b.$nodes.length
                }
            }
            return 0

            // 검증 필요. 아래 코드와 같지 않을까?
            // a.device_type.localeCompare(b.device_type) 
            //  || a.instlocation.localeCompare(b.instlocation) 
            //  || a.$nodes.length.localeCompare(b.$nodes.length) 
        });

        let types = sortedNodes.map(node => node.device_type)
        const typeIndexes = types.deduplication().map(type => arrNodeType.indexOf(type)).sort()
        const myNodeTypes = typeIndexes.map(index => arrNodeType[index])
        log('myNodeTypes=', myNodeTypes)

        let serviceLinkA, serviceLinkZ;
        sortedNodes.forEach((node, i) => {
            if (myNodeTypes[1] && node.device_type === myNodeTypes[1]) {
                if (node.instlocation === instlocationa && !serviceLinkA) {
                    arrLinks.push({
                        id: `${services[0]}-${node.id}`,
                        source_id: services[0],
                        target_id: node.id,
                        debugDesc: "서비스A-instlocationa 링크 생성"
                    });
                    serviceLinkA = true
                }
                if (node.instlocation === instlocationz && !serviceLinkZ) {
                    let node1 = sortedNodes[i + 1]
                    if ((node1 === undefined
                        || node1.instlocation !== node.instlocation
                        || node1.device_type === node.device_type)) {
                        arrLinks.push({
                            id: `${services[1]}-${node.id}`,
                            source_id: services[1],
                            target_id: node.id,
                            debugDesc: "서비스Z-instlocationz 링크 생성"
                        });
                        serviceLinkZ = true
                    }
                }
            }

            /* 
             * 같은 설치위치 장비에 대한 임의 연결 추가
             * 정렬 된 노드의 인접한 노드이고, instlocation 이 같고, 서로 링크가 없는 경우 점선으로 표현 
             */
            if (i > 0) {
                const node0 = sortedNodes[i - 1]
                if (node0.device_type === node.device_type
                    && node.instlocation && node0.instlocation === node.instlocation
                    && node.$nodes.length < 2
                    && node0.$nodes.length < 2
                    && node0.$nodes.findIndex(neighbor => neighbor === node) < 0) {

                    arrLinks.push({
                        id: `${node0.id}-${node.id}`,
                        source_id: node0.id,
                        target_id: node.id,
                        dashLine: '1 5',
                        debugDesc: "instlocation-instlocation 링크 생성 (dot)"
                    });
                }
            }
        })
        
        mapIdToNode = assignRef(arrNodes, arrLinks)
        log('sortedNodes=', sortedNodes)

        const mapTypeToNodes = classify(arrNodes, myNodeTypes, 'device_type')
        

        // yLevel0 >  y 좌표 설정
        const minX = 100
        const maxX = 1800
        const minY = 500
        const distanceY = 200
        setNodePos(mapIdToNode['서비스A'], minX, minY)
        setNodePos(mapIdToNode['서비스Z'], maxX, minY)

        // yLevel1 정렬(a-z 연결에 따라 재배열) > y 좌표 설정
        let arrSortedLevel1 = []
        if (myNodeTypes[1]) {
            let prev, node = mapIdToNode['서비스A'].$nodes[1]
            let x = minX;
            let distanceX = (maxX - minX) / ((mapTypeToNodes[myNodeTypes[1]]).length - 1);
            while (node) {
                // log('node', node.id, node.device_name)
                setNodePos(node, x, minY + distanceY)
                x += distanceX
                arrSortedLevel1.push(node)
                let next = node.$nodes.find(node0 => node0 !== prev && node0.device_type === node.device_type)
                prev = node
                node = next
            }
            mapTypeToNodes[myNodeTypes[1]] = arrSortedLevel1;
        }

        const getLevel = (nodeType) => myNodeTypes.findIndex(t => t === nodeType)
        const getYPos = node => {
            let level = getLevel(node.device_type)
            return level > 0 ? node.fy = minY + (level * distanceY) : null
        }

        // yLevel1 배열과 직접 연결된 노드에 대한 x,y 좌표 설정
        log(arrSortedLevel1)
        arrSortedLevel1.forEach(node => {
            let nodes = node.$nodes.filter( neighbor =>
                neighbor !== node
                && neighbor.device_type !== node.device_type
                && !neighbor.fx
                && !neighbor.fy)
            nodes.forEach(neighbor => {
                setNodePos(neighbor, neighbor.$links.length === 1 ? node.fx : null, getYPos(neighbor))
            })
        })

        // todo: 나머지 노드 fix
        // 나머지 노드에 대해 cycle 가 존재하는지 확인
        let otherNodes = []
        arrNodes.forEach(node => {
            (node.fx || node.fy)
        })
        log(otherNodes)

        // 좌표가 지정되지 않은 노드에 대해 일괄 y 좌표 설정
        // arrNodes.forEach(node => {
        //     if (node.fx || node.fy) { return true }
        //     let level = myNodeTypes.findIndex(t => t === node.device_type)
        //     if (level > 0) {
        //         node.fy = minY + (level * distanceY)
        //     }
        // })

        let serviceZ = mapIdToNode['서비스Z']
        setNodePos(serviceZ, (serviceZ.$nodes[1] || {}).fx, null)

        // let roadm1 = mapIdToNode['남청중-ROADM-8005']
        // let roadm2 = mapIdToNode['CBCJ16583']
        // setPos(roadm2, roadm1?.fx, roadm1?.fy + 100)

        // let roadm3 = mapIdToNode['영동중-ROADM-8103']
        // let roadm4 = mapIdToNode['CBYD02093']
        // setPos(roadm4, roadm3?.fx, roadm3?.fy + 100)

        // let roadm = mapIdToNode['CBBU01282']
        // setPos(roadm, 1433,952)

        // roadm = mapIdToNode['CBOC01778']
        // setPos(roadm, 1435,1052)

        arrNodes.forEach(node => {
            Object.keys(node).filter(k => k.startsWith('$')).forEach(k => { delete node[k] })
        })
        arrLinks.forEach(link => {
            Object.keys(link).filter(k => k.startsWith('$')).forEach(k => { delete link[k] })
        })

        return { nodes: arrNodes, links: arrLinks, config: json.config }
    },
    oasis: (json) => {
        const data = json.data || []
        const config = json.config

        if(!json.data && json.nodes) {
            return json
        }

        data.forEach(obj => {
            if (!obj['lat'] && !obj['lng'] && obj['n'] && obj['e']) {
                obj.lat = latlngFormatter(obj.n);
                obj.lng = latlngFormatter(obj.e);
            }
        })

        let p0 = config.map.screen_range.p0
        let p1 = config.map.screen_range.p1
        let converter = new latlngConverter();

        if (p0 && p1) {
            converter.setPosition(p0, p1);
        }

        let nodes = data.map(obj => {
            let pos = converter.latlngToScreenXY(obj.lat, obj.lng);
            return Object.assign({}, obj, { fx: pos.x, fy: pos.y })
        });

        
        return { nodes: nodes, config: config, converter: converter }
    },
    oasis2: (json) => {
        const data = json.data || []
        const config = json.config

        if(!json.data && json.nodes) {
            return json
        }

        data.forEach(obj => {
            if (!obj['lat'] && !obj['lng'] && obj['n'] && obj['e']) {
                obj.lat = latlngFormatter(obj.n);
                obj.lng = latlngFormatter(obj.e);
            }
        })

        let gyeongnam_p0 = config.map.screen_range.gyeongnam.p0
        let gyeongnam_p1 = config.map.screen_range.gyeongnam.p1
        let busan_p0 = config.map.screen_range.busan.p0
        let busan_p1 = config.map.screen_range.busan.p1

        let gyeongnam_converter = new latlngConverter();
        let busan_converter = new latlngConverter();

        gyeongnam_converter.setPosition(gyeongnam_p0, gyeongnam_p1);
        busan_converter.setPosition(busan_p0, busan_p1);

        let nodes = data.map(obj => {
            let converter;

            if (obj.data_type == '경상남도') {
                converter = gyeongnam_converter
            } else if (obj.data_type == '부산광역시') {
                converter = busan_converter
            } else {
                return {}
            }
            
            let pos = converter.latlngToScreenXY(obj.lat, obj.lng);
            return Object.assign({}, obj, { fx: pos.x, fy: pos.y })
        });

        return { nodes: nodes, config: config, converter: { gyeongnam: gyeongnam_converter, busan: busan_converter } }
    },
    mba: (json) => {
        const config = json.config
        const filterFn = config.trunk_name ? (row => row.trunk_name == config.trunk_name) : (row => row)
        let width = 800

        const alData = (json?.data?.links || json?.links || [])
        const data = (json?.data?.nodes || json?.nodes || []).filter(filterFn)
            .sort((a,b) => a.routenum - b.routenum)

        let center_pos = [960, 540]
        let posList;

        if (data.length <= 5) {
            width = 1000;
            let start_x = center_pos[0] - (width / 2)
            let end_x = center_pos[0] + (width / 2)
            let range = end_x - start_x
            let tick = range / (data.length - 1)

            let arr = []
            for (var i=0; i < data.length; i++) {

                let x = start_x + (tick * i)
                let y = center_pos[1]
                log('i' + i + ' x:' + x + ' y:' + y);
                arr.push([x, y])
            }
            posList = arr
        } else {
            posList = calCirclePosList(width, data.length + (data.length % 2 != 0 ? 1 : 0), center_pos)
        }

        let nodes = data.map((val, idx) => {
            const device = {
                'ROADM': '0',
                'REPEATER': '1'
            }
            return {
                id: val.sysname,
                device_name: val.sysname,
                device_type: device[val.name_code] || device['ROADM'],
                index: idx,
                routenum: val.routenum,
                fx: posList[idx][0],
                fy: posList[idx][1]
            }
        })

        let arrA = alData.map(v=>v.root_cause_sysnamea)
        let arrZ = alData.map(v=>v.root_cause_sysnamez)

        let startSysname = arrA.find(sys => !arrZ.includes(sys))
        let startNode = nodes.find(v => v.id == startSysname) || {}
        let isStartRepeater = startNode.device_type == '1' || false


        let links = nodes.reduce((r, p) => {
            if(p.index < nodes.length - 1) {
                let tmp = createLink(p)
                r.push(tmp[0])
                r.push(tmp[1])
            }
            return r

            function createLink(p) {
                let source = p.id
                let target = nodes[p.index + 1].id
                let getStatus = (isReverse = false) => {

                    if (!isReverse) {
                        let hasAlNode = alData.find(v => v.root_cause_sysnamea == source && v.root_cause_sysnamez == target)
                        if(hasAlNode) {
                            if (!isStartRepeater) {
                                return (startSysname == source ? -1 : 0 )
                            } else {
                                return 0
                            }
                        } else {
                            if (!isStartRepeater) {
                                return undefined
                            } else {
                                let tmpSource = alData.find(v => v.root_cause_sysnamea == source || v.root_cause_sysnamez == source)
                                return ((startSysname == target && tmpSource == undefined) ? -1 : undefined )
                            }
                        }
                    } else  {
                        let reverseSource = target;
                        let reverseTarget = source;

                        let hasAlNode = alData.find(v => v.root_cause_sysnamea == reverseSource && v.root_cause_sysnamez == reverseTarget)
                        if(hasAlNode) {
                            if (!isStartRepeater) {
                                return (startSysname == reverseSource ? -1 : 0 )
                            } else {
                                return 0
                            }
                        } else {
                            if (!isStartRepeater) {
                                return undefined
                            } else {
                                let tmpSource = alData.find(v => v.root_cause_sysnamea == reverseSource || v.root_cause_sysnamez == reverseSource)
                                return ((startSysname == reverseTarget && tmpSource == undefined) ? -1 : undefined )
                            }
                        }
                    }

                }

                return [
                    {
                        id: r.length,
                        source_id: source,
                        target_id: target,
                        status: getStatus()

                    },
                    {
                        id: r.length + 1,
                        source_id: target,
                        target_id: source,
                        status: getStatus(true)
                    }
                ]
            }
        }, [])

        
        function calCirclePosList(width, nodeCount, centerPos = [0, 0]) {
            let radius = width / 2;
            let positions = []

            for (let i = 0; i < 270; i += 270 / nodeCount) {
                let x = Math.floor(radius * Math.cos(degreesToRadians(i)));
                let y = -Math.floor(radius * Math.sin(degreesToRadians(i)));

                x += centerPos[0]
                y += centerPos[1]
                log('i' + i + ' x:' + x + ' y:' + y);
                positions.push([x, y]);
            }

            return positions
        }

        // 2021-07-14 잘못된 AL 구간 데이터에 대한 예외 처리
        if (links.filter(v => v.status == 0 || v.status == -1).length <= 0) {
            // 중계기 데이터가 없어서 구간을 건너 뛴 경우 (ROADM <-> ROADM)
            if (alData.length == 1) {
                let roadms = nodes.filter(v => v.routenum == 0 || v.routenum == 20).map(v => v.id)
                if(roadms.includes(alData[0].root_cause_sysnamea) && roadms.includes(alData[0].root_cause_sysnamez)) {
                    let causeLink = links.find(v => v.target_id == alData[0].root_cause_sysnamez)
                    causeLink.status = -1;
                }
            }
        }

        return { nodes: nodes, links: links, config: config } 
    },
    mba_v2: (json) => {
        const config = json.config
        const filterFn = config.trunk_name ? (row => row.trunk_name == config.trunk_name) : (row => row)
        let width = 800

        const alData = (json?.data?.links || json?.links || [])
        let data = (json?.data?.nodes || json?.nodes || []).filter(filterFn)
            .sort((a,b) => a.routenum - b.routenum)
        if(data.length > 5) {
            data = data.sort((a,b) => b.routenum - a.routenum)
        }
        let center_pos = [960, 540]
        let posList;

        if (data.length <= 5) {
            width = 1000;
            let start_x = center_pos[0] - (width / 2)
            let end_x = center_pos[0] + (width / 2)
            let range = end_x - start_x
            let tick = range / (data.length - 1)

            let arr = []
            for (var i=0; i < data.length; i++) {

                let x = start_x + (tick * i)
                let y = center_pos[1]
                if(data.length > 3 && i%2 === 0) {
                    y+=100
                }
                log('i' + i + ' x:' + x + ' y:' + y);
                arr.push([x, y])
            }
            posList = arr
        } else {
            posList = calCirclePosList(width, data.length + (data.length % 2 != 0 ? 1 : 0), center_pos)
        }

        let nodes = data.map((val, idx, arr) => {
            const device = {
                'ROADM': '0',
                'REPEATER': '1'
            }
            let ntdDiff 
            if(idx+1 < arr.length ) {
                ntdDiff = Math.abs(((arr[idx+1].node_total_deviation) - arr[idx].node_total_deviation).toFixed(1))
            }
            return {
                id: val.sysname,
                device_name: val.sysname,
                device_type: device[val.name_code] || device['ROADM'],
                index: idx,
                routenum: val.routenum,
                direction: val.direction,
                rx_tx: val.rx_tx,
                span_gain: val.span_gain,
                ntd: val.node_total_deviation.toFixed(1),
                ntdDiff: ntdDiff,
                fx: posList[idx][0],
                fy: posList[idx][1]
            }
        })
        let maxSysname = null
        if(nodes.filter(v=> v.ntdDiff).every(v=> v.ntdDiff < 2)) {
            const maxObj = nodes.length > 0 && nodes.reduce( (prev, value) => {
                return (prev?.ntdDiff ?? 0) >= (value?.ntdDiff ?? 0) ? prev : value
            })
            maxSysname = maxObj?.device_name ?? ''
        }
        
        
        let links = nodes.reduce((r, p) => {
            if(p.index < nodes.length - 1) {
                let tmp = createLink(p)
                const downPort = nodes.length > 5 ? 1 : 0
                const upPort = nodes.length > 5 ? 0 : 1
                if(p.direction === 'DOWN') {
                    r.push(tmp[downPort])
                } else {
                    r.push(tmp[upPort])
                }
            }
            return r

            function createLink(p) {
                let source = p.id
                let target = nodes[p.index + 1].id
                let ntdDiff = p.ntdDiff

                return [
                    {
                        id: r.length,
                        source_id: source,
                        target_id: target,
                        maxSysname: maxSysname,
                        ntdDiff: ntdDiff
                    },
                    {
                        id: r.length + 1,
                        source_id: target,
                        target_id: source,
                        maxSysname: maxSysname,
                        ntdDiff: ntdDiff
                    }
                ]
            }
        }, [])
        
        const fn_link_color = (d, options) => {
            const red = "#f76e5d"
            const green = "#93c62d"
            if(config.reason && config.reason === 'case_1') { // Node Total Deviation 변화
                if(d.maxSysname !== null) {
                    return d.source_id === maxSysname ? red : green
                } else {
                    return Math.abs(d.ntdDiff) >= 2 ? red : green
                }
            } else {
                return green
            }
        }
        
        links = (links || []).map(d => {
            return Object.assign(d, {
                fn_link_color
            })
        })

        
        function calCirclePosList(width, nodeCount, centerPos = [0, 0]) {
            let radius = width / 2;
            let positions = []

            for (let i = 0; i < 270; i += 270 / nodeCount) {
                let x = Math.floor(radius * Math.cos(degreesToRadians(i)));
                let y = -Math.floor(radius * Math.sin(degreesToRadians(i)));

                x += centerPos[0]
                y += centerPos[1]
                log('i' + i + ' x:' + x + ' y:' + y);
                positions.push([x, y]);
            }

            return positions
        }

        // 2021-07-14 잘못된 AL 구간 데이터에 대한 예외 처리
        if (links.filter(v => v.status == 0 || v.status == -1).length <= 0) {
            // 중계기 데이터가 없어서 구간을 건너 뛴 경우 (ROADM <-> ROADM)
            if (alData.length == 1) {
                let roadms = nodes.filter(v => v.routenum == 0 || v.routenum == 20).map(v => v.id)
                if(roadms.includes(alData[0].root_cause_sysnamea) && roadms.includes(alData[0].root_cause_sysnamez)) {
                    let causeLink = links.find(v => v.target_id == alData[0].root_cause_sysnamez)
                    causeLink.status = -1;
                }
            }
        }

        return { nodes: nodes, links: links, config: config } 
    },
    bcn: (json) => {
        const config = json.config
        const filterFn = config.trunk_name ? (row => row.trunk_name == config.trunk_name) : (row => row)
        let width = 800

        const nodes = (json.nodes || []).map(d => {
            return Object.assign(d, {
                'id' : d.equipmentID,
                'device_name' : d.userDefinedName,
                'device_type' : window.libDev ? '103' : d.typeOfSystem?.toString(),
            })
        })
        const links = (json.links || []).map(d => {
            return Object.assign(d, {
                'id': `${d.fromEquipmentId}_${d.toEquipmentId}`,
                'source_id': d.fromEquipmentId,
                'target_id': d.toEquipmentId
            })
        })
        
        // 
        // 1925 * 891
        const svg = document.querySelector('svg#topology_container')

        const {initScale} = config.zoom
        let center_pos = [svg.clientWidth / initScale / 2, svg.clientHeight / initScale / 2]
        
        const spaceX = 300
        const spaceY = 150
        const area = { minx: 200 , miny: 100, maxx: center_pos[0] * 1.8, maxy: 0}

        let xIndex = -1
        let yIndex = 0
        let level = null

        nodes.forEach(node => {
            if(area.minx + (++xIndex) * spaceX > area.maxx ||
                (level && level != node.level)) {
                xIndex = 0
                yIndex += 1
            }
            level = node.level
            node.fx = area.minx + xIndex * spaceX
            node.fy = area.maxy = area.miny + yIndex * spaceY
        })

        // 초기줌 에니메이션 조정
        config.zoom.initPos = { x: center_pos[0], y: center_pos[1] }
        config.zoom.sx = center_pos[0]
        config.zoom.sy = center_pos[1] + 300
        config.zoom.sscale = initScale

        // random link generate
        if(window.libDev) {
            const a = Math.randomInt(0, nodes.length-1)
            for (let index = 0; index < 10; index++) {
                const z = Math.randomInt(0, nodes.length-1)
                if(a === z) {
                    index -= 1
                    continue
                }
                try {
                    links.push({
                        id: index,
                        source_id: nodes[a].id, 
                        target_id: nodes[z].id, 
                    })
                } catch (error) {
                }
            }
        }

        return { nodes: nodes, links: links, config: config } 
    },
    nia: (json) => {
        let {config, links} = json

        const fn_link_cut = (d) => d?.status < 0
        const fn_link_alarm = (d) => d?.status <= 0
        const fn_link_color = (d, options) => fn_link_alarm(d) ? "#f76e5d" : 
            d.equip_type == 'potn' ? "#ffda48" : "#93c62d"

        links = (links || []).map(d => {
            return Object.assign(d, {
                fn_link_cut,
                fn_link_alarm,
                fn_link_color
            })
        })
    },
    commonConvert:({config, nodes, links, data}) => {
        const fn_alarm_count = (d) => d?.alarm_count || d?.related_alarm || 0
        const fn_alarm_count_show = (d) => (d?.fn_alarm_count && d?.fn_alarm_count(d) > 0) == false
        const fn_alarm_count_color = (d) => (d?.related_alarm ? ['green'] : ['red'])

        nodes = (nodes || data?.nodes || []).map(d => {
            return Object.assign({
                fn_alarm_count: d.fn_alarm_count ?? fn_alarm_count,
                fn_alarm_count_show: d.fn_alarm_count_show ?? fn_alarm_count_show,
                fn_alarm_count_color: d.fn_alarm_count_color ?? fn_alarm_count_color,
            }, d)
        })

        const fn_link_cut = (d) => d?.status < 0
        const fn_link_alarm = (d) => d?.status <= 0
        const fn_link_color = (d, options) => fn_link_alarm(d) ? "#f76e5d" : "#93c62d"
        links = (links || data?.links || []).map(d => {
            return Object.assign({
                fn_link_cut,
                fn_link_alarm,
                fn_link_color
            }, d)
        })

        return { nodes: nodes, links: links, data: data, config: config } 
    }
}

let log = window.console.log

    ; (function (global) {
        'use strict';
        const SVG_SELECTOR = 'svg#topology_container'
        const className = { selected: 'selected', dragging: 'dragging', invisible: 'invisible', error: 'error', focus: 'focus' }
        const eventType = { selectChanged: 'selectChanged', contextmenu: 'contextmenu' }
        const elementType = { svg: 'svg', node: 'node', link: 'link', link_selector: 'link_selector' }
        const mouseMode = { select: 'select', edit: 'edit', none: 'none', selectBlur: 'selectBlur' }
        const nodeAlarm = {
            0: { style: "stat_normal" },
            1: { style: "stat_abnormal" },
            2: { style: "stat_error" },
            3: { style: "stat_down" },
            4: { style: "stat_unknown" },
        };

        const defaultNodeImage = {
            "UNKNOWN": { "path": "images/node/question.png" },
            '0': { path: 'images/node/roadm.png' },
            '1': { path: 'images/node/repeater.png' },
            '2': { path: 'images/node/ptn.png' },
            '3': { path: 'images/node/mspp.png' },
            '4': { path: 'images/node/switch.png' },
            'SERVICE': { path: 'images/node/cloud-city.png' }
        };

        const defaultNodeMenu = (d, i) => [
            { title: '노드메뉴' },
            { title: 'Item #1', action: function(element, d, i) { log('clicked Item #1: ', element, d, i); } },
            { divider: true },
            { title: 'Item #2', action: function(element, d, i) { log('clicked Item #2: ', element, d, i); } },
            { title: 'SUB1',
              children: [
                { title: 'SUB2' }, 
                { disabled: true, title: '비활성메뉴', action: function () { } }
            ]}
        ]

        const defaultLinkMenu = function(d, i) {
            if (i % 2 > 0) {
                return [{ title: '링크메뉴1', }, { title: 'Item #1', action: function(element, d, i) { log('clicked Item #2: ', element, d, i); } },];
            } else {
                return [{ title: '링크메뉴2', }, { title: 'Item #2', action: function(element, d, i) { log('clicked Item #2: ', element, d, i); } }];
            }
        };

        const contextMenu = {
            node: defaultNodeMenu,
            link: null,
            map: null,
        }
        
        const iconImage = {
            'node_error_icon': { path: 'images/node/error-node.png'/*, width: 10, height: 10*/ },
            'link_cut_icon': { path: 'images/node/error-link.png', width: 30, height: 30 },
        };

        let Map2d = global.Map2d = function () {

            const ctrl = global.c = this;     // global.c is for debugging only
            ctrl.dragging = false
            ctrl.zooming = false
            ctrl.centerWorldCoords = []
            ctrl.screenRectWorldCoords = null
            let eventListeners = ctrl.eventListeners = []
            let simulation, link_force, charge_force, center_force, drag, zoom, node_tip, link_source_tip,
                link_dest_tip;
            let svg, g_link, g_node, g_desc;
            let defaultOptions = {
                "--focus-color": "rgba(0, 0, 0, 0.7)",
                "--node-selector-color": "rgba(226, 228, 114, 0.7)",
                "--zoom-scale": 1,
                "--blur-level": 1,
                "--svg-background-color": "#032c37",
                "--svg-text-color": "#fff",
                "--svg-info-text-color": "#22ffe5",
                "--properties-background-color": "rgba(32, 32, 32, 0.7)",
                "--properties-text-color": "#fff",
                "--link-selector-color": "rgb(170, 160, 100, 0)",
                "--keyword-color": "darkolivegreen",

                "properties-use": true,
                "properties-gripper-use": false,
                "svg_viewbox": "0 0 1920 1080", // false: svg 사이즈에 영향을 받지 않는다. 단, 반응형이 아님.
                "cloak_use": false,
                "focus_target": "all",  //"only", "parent", "child", "all"

                "node_type": 'image',
                "node_width": 40,
                "node_height": 30,
                "node_fix_size": false,
                "node_r_in_color": "#604c45",
                "node_r_out_color": "#604c45",
                "node_r_opacity": 0.7,
                "node_r_show": true,
                "node_r_width": 30,
                "node_r_width_weight": 1.5,
                "node_badget_weight": 1,
                "node_text_distance": 5,
                "node_text_size": 12,
                "node_text_field": 'device_name',
                "node_info_show": false,
                "node_info_field": 'rx_tx',
                "node_info_text_size": 12,
                "node_info_offset_top": 15,
                "node_badge_show": false,
                "node_badge_size": 30,
                "node_error_count_show": true,
                "node_drag_use": true,
                "node_drag_scale": 2,
                "node_selector_weight": 2,
                "link_type": 'line',
                "link_color": "#aaa064",
                "link_width": 2.5,
                "link_text_color": "#828282",
                "link_text_field": "none",
                "link_text_size": 10,
                "link_badge_size": 30,
                "link_selector_min_width": 20,
                "link_selector_weight": 1.5,
                "link_direction_auto": true,
                "link_traffic_show": true,
                "link_traffic_arrow_show": false,
                "link_traffic_arrow_color": "#ff0606",
                "link_traffic_r": 3,
                "link_traffic_r_color": "#555",
                "link_traffic_r_opacity": 1.0,
                "link_traffic_r_duration": 5,
                "load_from_local": false,
                "action_mode": mouseMode.select,
                "select_single": true,
                "select_node_test": false,
                "select_link_test": false,
                "zoom_scale_init": 1,
                "zoom_scale_min": 0.1,
                "zoom_scale_max": 5,
                "grid_mode": true,
                "grid_cell_width": 10,
                "bg_grid": false,
                "node_alarm_test": true,
                "layout_link_distance": 120, 
                "layout_link_strength": 0.1, 
                "layout_charge_strength": -160, 
                "layout_charge_distance_max": 300, 
                "layout_charge_distance_min": 0, 
                "layout_simulation_strength_x": 0.5, 
                "layout_simulation_strength_y": 0.5, 
                "layout_simulation_collide_radius": 30, 
                "layout_simulation_collide_strength": 0.2, 
                "random_node_count": 30,
                "random_link_count": 30,
                filePath: 'json/',
                keyword: '',
                nodeImage: defaultNodeImage,
                debug_intermediate: false && window.libDev === true
            };

            // todo: simulationRestart 값에 따라 레이아웃 force가 달라짐. 
            // 노드에 대한 변화가 생길 때 만  false 그 외 기본 업데이트는 true 로 하는 것이 효율적일 듯
            /* 
                * 고정좌표는 차이가 없다. 
                + true: force 에 따른 좌표를 출력한다. 드로윙이 자연스러워 보인다.
                    - data_oasis_3 등의 좌표가 달라진다. 
                + false: force 에 대한 좌표를 계산하고, 마지막 좌표를 기준으로 출력한다. 
                    - ??
            */
            let simulationRestart = false

            // if (simulationRestart) {
            //     defaultOptions = Object.assign(defaultOptions, {
            //         "layout_link_distance": 0,
            //         "layout_link_strength": 0.47000000000000003,
            //         "layout_charge_strength": -20,
            //         "layout_charge_distance_max": 300,
            //         "layout_charge_distance_min": 0,
            //         "layout_simulation_strength_x": 0,
            //         "layout_simulation_strength_y": 0,
            //         "layout_simulation_collide_radius": 50,
            //         "layout_simulation_collide_strength": 0.47000000000000003,
            //         "random_node_count": 60,
            //         "random_link_count": 40
            //     })
            // }
            

            let fileName = localStorage['last_map'] || 'data_nia_ip_1.json';
            let options =
                Object.assign(objectCopyPrimitive(defaultOptions), {
                    fileName,
                    zoomInTest,
                    resetZoom,
                    draw,
                    redraw,
                    redrawNode,
                    redrawNodeTest,
                    save,
                    load,
                    exportToFile,
                    updateKeyword,
                    updateAnimated,
                    updateVisible,
                    updateNodeDisplayField,
                    updateNodeInfoDisplayField,
                    updateNodeR,
                    updateNodeBadgeSize,
                    updateNodeSelect,
                    updateNodeWidthHeight,
                    updateLink,
                    updateLinkColor,
                    updateLinkSelect,
                    updateLinkTrffic,
                    updateLinkTrfficAnimation,
                    updateLinkText,
                    updateScale,
                    updateForce,
                    genneratRandomNode,
                    genneratRandomLink,
                    loadForce,
                    saveForce,
                    onArrange,
                    onTestLinkAlarmOccur,
                    onTestMbaAlarmOccur,
                    getDrawingProperty,
                    getRefX
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
                    if (options['link_direction_auto']) {
                        leftHand = true;
                        reverse = d.source.x > d.target.x;
                    } else {
                        leftHand = true;
                        reverse = false;
                    }
                } else {
                    if (options['link_direction_auto']) {
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
                // return options['node_r_width'] / 2;
                return 30
            }

            /**
             * 특정 수치에 대한 줌 계산
             * 특정값 / zoom scale
             * @param {Number} value 계산값
             * @param {String} type 줌 옵션 타입
             * {Number} min_scale 최소 scale
             * {Number} max_scale 최대 scale
             * {Number} min_value 최소 결과값
             * {Number} max_value 최대 결과값
             * @returns 
             */
            function getPixelByCurrentZoomScale(value, type) {
                try {
                    let zoom_options = ((ctrl.data || {}).config || {}).zoom || {}
                    let { min_scale, max_scale, min_value, max_value } = zoom_options[type] || {};
                    let zoom, result;
                    
                    zoom = ctrl.zoom_scale || 1;
                    min_scale = min_scale || zoom_options.initScale || 1
                    if (min_scale) {
                        zoom = zoom >= min_scale ? zoom : min_scale;
                    }

                    max_scale = max_scale || min_scale * 2 || 1
                    if (max_scale) {
                        zoom = zoom <= max_scale ? zoom : max_scale;
                    }

                    result = value / zoom;
                    if (min_value) {
                        result = result >= min_value ? result : min_value;
                    }
                    if (max_value) {
                        result = result <= max_value ? result : max_value;
                    }

                    return result;
                } catch (e) {
                    console.error(e);
                }
            }

            function onTestLinkAlarmOccur() {
                let { data } = ctrl;

                let find = data.links.find(l => l.id == '1');
                if (find) {
                    log(`find id == 1 link info : `, find)
                }

                if (data.links.length > 0) {
                    data.links[0].status = (data.links[0]?.status || 1) * -1
                    draw(false);
                }
            }

            function onTestMbaAlarmOccur() {
                throw new Error('에러발생 테스트');
            }

            let isVisualNode = ctrl.isVisualNode.bind(ctrl);

            function updateKeyword(keyword = options['keyword']) {
                const nodes = ctrl.getNodeAll()

                nodes.forEach(d => {
                    try {
                        if (keyword.trim() !== ''
                            && (d.device_name.toUpperCase().includes(keyword.toUpperCase()) || d.id.toUpperCase().includes(keyword.toUpperCase()))
                        ) {
                            ctrl.getNodeElement(d)?.classList.add('keyword');
                        } else {
                            ctrl.getNodeElement(d)?.classList.remove('keyword');
                        }
                    } catch (error) {
                        ctrl.getNodeElement(d)?.classList.remove('keyword');
                    }
                });
            }

            function updateAnimated() {
                let me = d3.select(this);
                if (options['link_type'] == "curved") {
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

            function updateLinkTrfficAnimation() {
                svg.selectAll('.link_traffic_flow')
                    .attr('dur', function (d) {
                        return options['link_traffic_r_duration'] + 's';
                    })
            }

            function updateLinkTrffic() {
                svg.selectAll('.link_traffic:not(.error)')
                    .attr('r', options['link_traffic_r'])
                    .attr('fill', options['link_traffic_r_color'])
                    .style('fill-opacity', options['link_traffic_r_opacity'])
                    .classed(className.invisible, !options['link_traffic_show']);

                svg.select('defs marker path')
                    .style('fill', options['link_traffic_arrow_color']);

                svg.select('defs')
                    .classed(className.invisible, !options['link_traffic_arrow_show']);
            }

            function updateNodeWidthHeight() {
                svg.selectAll('.node_image')
                    .attr('transform', function (d) {
                        let outWidth = (d.image || {}).width || options['node_width'],
                            outHeight = (d.image || {}).height || options['node_height'],
                            halfWidth = parseFloat(outWidth) / 2,
                            halfHeight = parseFloat(outHeight) / 2;
                        return 'translate(' + (-(getPixelByCurrentZoomScale(halfWidth, 'node'))) + ',' + (-(getPixelByCurrentZoomScale(halfHeight, 'node'))) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                    })
                svg.select('#arrow')
                    .attr('refX', options.getRefX());
            }

            function onArrange() {
                let nodes = ctrl.getVisualNodeAll()
                for (let index = 0; index < nodes.length; index++) {
                    const node = nodes[index];
                    if (options['grid_mode']) {
                        const unit = options['grid_cell_width']
                        const x = node.fx || node.x
                        const y = node.fy || node.y
                        node.fx = Math.floor(x / unit) * unit
                        node.fy = Math.floor(y / unit) * unit
                    }
                }
                ctrl.draw(true);
            }

            function updateLinkSelect(link_test, index) {
                try {
                    link_test = ctrl.options['select_link_test'];
                    if (link_test) {

                        let links = ctrl.getVisualLinkAll();
                        index = (index || 0) % links.length;

                        if (links && Number.isInteger(index) && links.length > index) {
                            // log(`updateLinkSelect : ${index}`);
                            let d = links[index];
                            ctrl.selectElement(elementType.link, d, true);

                        }
                        setTimeout(updateLinkSelect.bind(this, link_test, ++index), 1000);
                    }

                } catch (e) {
                    console.error(e);
                }
            }

            function updateNodeSelect(nodeTest, index) {
                try {
                    nodeTest = ctrl.options['select_node_test'];
                    if (nodeTest) {
                        let nodes = ctrl.getVisualNodeAll();
                        index = (index || 0) % nodes.length;

                        if (nodes && Number.isInteger(index) && nodes.length > index) {
                            // log(`updateNodeSelect : ${index}`);
                            let d = nodes[index];
                            ctrl.selectElement(elementType.node, d, true);
                        }
                        setTimeout(updateNodeSelect.bind(this, nodeTest, ++index), 1000);
                    }
                } catch (e) {
                    console.error(e);
                }
            }

            function updateNodeBadgeSize() {
                svg.selectAll('g.node_error_icon')
                    .select('image')
                        .attr('transform', function (d) {
                            let outWidth = options['node_badge_size'],
                                outHeight = options['node_badge_size'],
                                nodeR = options['node_r_width'];
                            let transform = 'translate(' + 0.5 * (nodeR + outWidth / 2) + ',' + (-1 * (nodeR + outHeight / 2)) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                            // log(transform);
                            return transform;
                        });

                // svg.selectAll('g.node_error_icon')
                //     .classed(className.invisible, !options['node_badge_show']);
            }

            function updateNodeR() {
                svg.selectAll('.node_circle')
                    .attr('r', getPixelByCurrentZoomScale(options['node_r_width'] * 0.8, 'node'))
                    .style('fill', options["node_r_in_color"])
                    .style('stroke', options["node_r_out_color"])
                    .style('opacity', options["node_r_opacity"]);

                svg.selectAll('.node_icon')
                    .attr('y', options['node_r_width'] / 2 + 2)
                    .style('font-size', options['node_r_width']);

                svg.selectAll('.node_container circle.node_circle')
                    .classed(className.invisible, !options['node_r_show']);

                svg.select('#arrow')
                    .attr('refX', options.getRefX());

                // svg.selectAll('g.desc text')
                //     .style('fill', options["--svg-text-color"]);
            }

            function updateVisible() {
                if(ctrl.dragging === true) return
                if(options['cloak_use'] !== true) return
                updateNodeVisible()
                updateLinkVisible()
                updateDescVisible()
            }

            function updateNodeVisible() {
                if(!g_node) return 
                g_node.each(function (d, i) {
                    const node = d3.select(this)
                    const { minx, miny, maxx, maxy } = ctrl.screenRectWorldCoords || {}
                    const fx = d.x ?? d.fx ?? null
                    const fy = d.y ?? d.fy ?? null 
                    let cloak = false
                    if(fx != null  && fy != null && !Number.isNaN(minx * miny * maxx * maxy)) {
                        cloak = ((minx <= fx && fx <= maxx) && (miny <= fy && fy <= maxy)) !== true
                    }
                    d.cloak = cloak
                    node.classed('cloak', cloak)
                    // log(`updateNodeVisible: d=`, d, `, display=`, !cloak)
                })
            }

            function updateLinkVisible() {
                if(!g_link) return 
                g_link.each(function (d, i) {
                    const link = d3.select(this)
                    if (link.classed('selected')) return
                    const cloak = d.source.cloak || d.target.cloak
                    link.classed('cloak', cloak)
                    // log(`updateLinkVisible: d=`, d, `, display=`, !cloak)
                })
            }

            function updateDescVisible() {
                if(!g_desc) return 
                g_desc.each(function (d, i) {
                    const desc = d3.select(this)
                    const cloak = d.cloak
                    desc.classed('cloak', cloak)
                    // log(`updateDescVisible: d=`, d, `, display=`, !cloak)
                })
            }

            function updateLink() {
                $do_one_tick();

                svg.select('#arrow')
                    .attr('refX', options.getRefX());
            }
            
            function updateLinkColor() {
                svg.selectAll('.link_container .link_path')
                    .style('stroke', function (d) {
                        return options["link_color"]
                    })
            }

            function updateLinkText() {
                svg.selectAll('.link_label')
                    .style('font-size', options['link_text_size'])
                    .style('fill', options["link_text_color"])
                    .text(function (d) {
                        switch (options['link_text_field']) {
                            case 'id':
                                return d.id;
                            case 'id-id':
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
                                return d[(options['link_text_field'] || '')];
                        }
                    });
            }

            function genneratRandomNode() {
                const {data} = ctrl
                const arrNodes = []
                const arrLinks = data.links
                const max_node = options['random_node_count']
                for (let index = 0; index < max_node; index++) {
                    const x = Math.randomInt(0, 1920)
                    const y = Math.randomInt(0, 1080)
                    arrNodes.push({
                        "id": String(index),
                        "device_name": index,
                        "device_type": "0"
                    })
                }
                loadData({ nodes: arrNodes, links: arrLinks, config: data.config }, false)
            }

            function genneratRandomLink() {
                const {data} = ctrl
                const arrNodes = data.nodes
                const arrLinks = []
                const nodes = arrNodes.shuffle()
                const max_node = arrNodes.length
                const max_link = options['random_link_count']
                for (let index = 0; index < max_link; index++) {
                    const sid = nodes.at(index * 2)?.id || String(Math.randomInt(0, max_node))
                    const tid = nodes.at(index * 2 + 1)?.id ||String(Math.randomInt(0, max_node))
                    arrLinks.push(
                        { "id": `${sid}-${tid}`, "source_id": sid, "target_id": tid, status: 1 },
                    )
                }
                loadData({ nodes: arrNodes, links: arrLinks, config: data.config }, false)
            }

            function loadForce() {
                const force = JSON.parse(localStorage.getItem(`saved_force`));
                Object.assign(options, force)
                genneratRandomNode()
                genneratRandomLink()
                updateForce()
                log('load force=' , force)
            }

            function saveForce() {
                const {
                    layout_link_distance, 
                    layout_link_strength, 
                    layout_charge_strength,
                    layout_charge_distance_max, 
                    layout_charge_distance_min,
                    layout_simulation_strength_x,
                    layout_simulation_strength_y,
                    layout_simulation_collide_radius,
                    layout_simulation_collide_strength,
                    random_node_count,
                    random_link_count
                } = options

                const force = {
                    layout_link_distance, 
                    layout_link_strength, 
                    layout_charge_strength,
                    layout_charge_distance_max, 
                    layout_charge_distance_min,
                    layout_simulation_strength_x,
                    layout_simulation_strength_y,
                    layout_simulation_collide_radius,
                    layout_simulation_collide_strength,
                    random_node_count,
                    random_link_count
                }
                localStorage.setItem(`saved_force`, JSON.stringify(force));
                log('saved force=' , force)
            }

            function updateForce() {
                // console.log('updateForce')    
                updateSimulation()
                updateLinkForce()
                updateChangeForce()
                redraw()
            }

            function updateSimulation() {
                const x = options['layout_simulation_strength_x']
                const y = options['layout_simulation_strength_y']
                const radius = options['layout_simulation_collide_radius']
                const strength = options['layout_simulation_collide_strength']
                let { width, height } = ctrl.getViewBoxSize();

                simulation
                    // .force("center", center_force)
                    // .force("charge", charge_force)
                    // .force("link", link_force)
                    // .force('x', d3.forceX(width / 2).strength(x))
                    // .force('y', d3.forceY(height / 2).strength(y))
                    .force('collide', d3.forceCollide().radius(radius).strength(strength))
            }

            function updateLinkForce() {
                // var weightScale = d3.scaleLinear()
                //     .domain(d3.extent(links, function (d) { return d.weight }))
                //     .range([.1, 1])

                link_force = d3
                        .forceLink()
                        .id(function (d) {
                            return d.id;
                        })
                        .distance(function (d) {
                            if ("id" in d.source && "id" in d.target) {
                                return options['layout_link_distance']
                            } else {
                                return 60
                            }
                        })
                        .strength(options['layout_link_strength'])
                        // .strength(d => 1 / Math.randomInt(50, 100))
                        // d => 1 / Math.min(count(link.source), count(link.target)) 
                return link_force
            }

            function updateChangeForce() {
                charge_force = d3
                    .forceManyBody()
                    .strength(function (d) {    /* charge */
                        if (isVisualNode(d)) {
                            return options['layout_charge_strength']
                        } else {
                            return -200;
                        }
                    })
                    .distanceMax(options['layout_charge_distance_max'])
                    .distanceMin(options['layout_charge_distance_min'])
                return charge_force
            }

            function updateScale() {
                const fixSize = options['node_fix_size']
                if (fixSize) {
                    // 노드 라벨 폰트사이즈
                    let font_size = getPixelByCurrentZoomScale(options['node_text_size'], 'desc');
                    svg.select('g.desc').style('font-size', font_size);

                    // 노드 라벨 위치
                    svg.select('g.desc')
                        .selectAll('text')
                            .attr('y', getPixelByCurrentZoomScale(options['node_height'] + options['node_text_distance'], 'node'));

                    // 노드 이미지 사이즈
                    svg.select('g.nodes')
                        .selectAll('.node_image')
                            .style('width', function (d) {
                                return getPixelByCurrentZoomScale(100, 'node') * fixSize
                            })
                            .style('height', function (d) {
                                return getPixelByCurrentZoomScale(100, 'node') * fixSize
                            })
                            .attr('transform', function (d) {
                                let outWidth = (d.image || {}).width || options['node_width'],
                                    outHeight = (d.image || {}).height || options['node_height'],
                                    halfWidth = parseFloat(outWidth) / 2,
                                    halfHeight = parseFloat(outHeight) / 2;
                                return 'translate(' + (-(getPixelByCurrentZoomScale(halfWidth, 'node')* fixSize)) + ',' + (-(getPixelByCurrentZoomScale(halfHeight, 'node')* fixSize)) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                            })

                    // 노드 circle 사이즈
                    svg.select('g.nodes')
                        .selectAll('circle.node_circle')
                            .attr('r', getPixelByCurrentZoomScale(options['node_r_width'] * 0.8, 'node'))

                    svg.select('g.nodes')
                        .selectAll('g.node_error_count')
                        .attr('transform', d => {
                            const r = getPixelByCurrentZoomScale(100 * 0.2, 'nodeError')
                            const s = r / 100
                            return `translate(${r * 0.8},${-r * 1.5}) scale(${s}, ${s})`
                        })
                        
                } else {
                    // default 값으로 세팅
                    svg.select('g.desc').style('font-size', options['node_text_size']);

                    svg.select('g.desc')
                        .selectAll('text')
                        .attr('y', options['node_height'] + options['node_text_distance']);

                    svg.select('g.nodes')
                        .selectAll('.node_image')
                        .style('width', function (d) {
                            return 100;
                        })
                        .style('height', function (d) {
                            return 100;
                        })
                        .attr('transform', function (d) {
                            let outWidth = (d.image || {}).width || options['node_width'],
                                outHeight = (d.image || {}).height || options['node_height'],
                                halfWidth = parseFloat(outWidth) / 2,
                                halfHeight = parseFloat(outHeight) / 2;
                            return 'translate(' + (-halfWidth) + ',' + (-halfHeight) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                        })

                    svg.select('g.nodes')
                        .selectAll('circle.node_circle')
                        .attr('r', (options['node_r_width'] * 0.8))
                }
            }

            function updateNodeDisplayField() {
                let field = options['node_text_field'],
                    desc_con = svg.select('g.desc').selectAll('g.desc_container');

                desc_con.select('text').text(function (d) {
                    return d[field];
                });
            }
            
            function updateNodeInfoDisplayField() {
                let field = options['node_info_field'],
                    desc_con = svg.select('g.desc').selectAll('g.info_container');
                // updateForce()
                updateChangeForce()
                desc_con.select('text').text(function (d) {
                    return d[field];
                });
            }

            function initialize(menu) {
                let savedOptions
                document.addEventListener('visibilitychange', function () {
                    window.libDev && log('changed visibilitychange', document.hidden)
                    if(document.hidden) {
                        savedOptions = objectCopyPrimitive(options)
                        options['link_traffic_show'] = false
                        options.updateLinkTrffic()
                    } else {
                        options = objectCopyPrimitive(savedOptions)
                        options.updateLinkTrffic()
                        options.redrawNode()
                    }
                })

                Object.assign(contextMenu, menu)

                d3
                    .select("body")
                    .on("keydown", function () {
                        switch (d3.event.keyCode) {
                            case 27: // ESC key    
                                ctrl.clearSelection();
                                break;
                            case 38: // Arrow Up
                                break;
                            case 40: // Arrow Down 
                                break;
                        }
                    })

                /* #region [#create container for links and nodes elements.] */
                svg = d3
                    .select(SVG_SELECTOR)
                    .on("click", function (d, index, all) {
                        ctrl.clickHandler(this, elementType.svg, d, index, all);
                    })
                    // .on('contextmenu', d3.contextMenu(contextMenu.map))
                    .on('contextmenu', function (d, i, a) {
                        try { return d3.contextMenu(contextMenu.map).call(this, d, i, a) } catch (error) { }
                    })

                let bg_image = drawBgImage()
                let bg_grid = drawBgGrid()
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
                        .style('font-size', options['node_text_size']);
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
                /* #endregion "[#create container for links and nodes elements.]"> */

                if (!this.svg) {

                    /*
                      force simulation
                    */
                    simulation = d3
                        .forceSimulation()
                        .stop()

                    // test
                    // simulation.force('x', d3.forceX(400).strength(0.015))
                    // simulation.force('y', d3.forceY(400).strength(0.015))
                    // simulation.force('collide', d3.forceCollide().radius(30).strength(0.2))

                    /* #region [#force] */
                    
                    link_force = updateLinkForce()
                    charge_force = updateChangeForce()
                    center_force = d3.forceCenter()

                    /* #endregion "[#force]"> */

                    /* #region [#gestures] */

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
                    const { zoom_scale_min: minScale, zoom_scale_max: maxScale} = ctrl.options

                    zoom = d3
                        .zoom()
                        .scaleExtent([minScale, maxScale])
                        .on("zoom", () => {
                            const {transform} = d3.event

                            ctrl.zoomStartHandler(transform)

                            bg_image.attr("transform", transform);
                            bg_element.attr("transform", transform);
                            bg_grid.attr("transform", transform);
                            links.attr("transform", transform);
                            nodes.attr("transform", transform);
                            desc.attr("transform", transform);

                            ctrl.zoomEndHandler(transform)

                            /*
                               debug: zoom or pan intermediate nodes;
                               */
                            if (!!inter_nodes) {
                                inter_nodes.attr("transform", d3.event.transform);
                            }
                        })

                    // 줌 이벤트 발생
                    svg.call(zoom);

                    // 패닝(panning)시 줌 이벤트 강제 발생
                    svg.call(zoom.transform, d3.zoomIdentity);   

                    /* #endregion "[#gestures]"> */
                    /* #region [#tooltip] */

                    node_tip = d3.tip()
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

                    /* #endregion "[#tooltip]"> */

                    if (options['link_type'] == "curved") {
                        simulation
                            .force("center", center_force)
                            .force("charge", charge_force)
                            .force("link", link_force);
                    }

                    // keep track of topology components.
                    Object.assign(ctrl, {
                        svg: svg,
                        simulation,
                        link_force,
                        charge_force,
                        center_force,
                        drag: drag,
                        zoom: zoom,
                        zoom_scale: options['zoom_scale_init'],
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

            function resetZoom(duration = 750) {
                ctrl.resetZoom(duration)
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
                        copyed.fx = copyed.x;
                        copyed.fy = copyed.y;
                        delete copyed.x;
                        delete copyed.y;
                        delete copyed.vx;
                        delete copyed.vy;
                        delete copyed.selected;
                        delete copyed.image;
                        delete copyed.badge;
                        return copyed;
                    }),
                    links: ctrl.getVisualLinkAll().map((d) => {
                        let copyed = Object.assign({}, d);
                        delete copyed.source;
                        delete copyed.target;
                        delete copyed.index;
                        delete copyed.intermediate;
                        delete copyed.selected;
                        delete copyed.image;
                        return copyed;
                    }),
                    config: Object.assign({}, ctrl.getConfigAll())
                };
                return data;
            }

            function clearAlarm() {
                let nodes = ctrl.getNodeAll()
                let links = ctrl.getLinkAll()

                nodes.forEach(node => {
                    delete node.alarm_count
                });

                links.forEach(node => {
                    node.status = 1
                });
            }

            function redraw() {
                draw()
            }
            function redrawNodeAll() {
                redrawNode(ctrl.getVisualNodeAll())
            }

            function redrawNode(dArray) {
                if (document.hidden) {
                    dArray?.forEach(d => {d.updatedNode = true})
                    return
                }

                if(!dArray) {
                    const allNode = ctrl.getVisualNodeAll()
                    dArray = allNode.filter(d => d.updatedNode)
                }

                dArray.forEach(d => {
                    const g_node = d3.select('#' + ctrl.getNodeId(d))
                    updateNodeComponent(g_node, 'errorCount') 
                    d.updatedNode && (delete d.updatedNode)
                })
                updateScale();
            }

            var redrawTimer
            function redrawNodeTest() {
                const test = ctrl.options['node_alarm_test']

                clearInterval(redrawTimer)
                redrawTimer = null

                if (!test) {
                    return
                }

                // 실시간 노드를 업데이트 하는 성능을 테스트 한다.
                redrawTimer = setInterval(() => {
                    // clearAlarm()
                    // 노드 3개 뽑아서 경보수를 변경한다.
                    const nodes = ctrl.getNodeAll()
                    nodes.slice(0, 3).forEach(n => n.alarm_count = Math.randomInt(0, 10000))
                    nodes.slice(3, 6).forEach(n => n.related_alarm = Math.randomInt(0, 10000))
                    for (let index = 0; index < 6; index++) {
                        nodes[index] && redrawNode([nodes[index]])
                    }
                }, 2000)
            }

            function draw(forceUpdate = true) {
                // log(`called draw()`, forceUpdate)
                ctrl.clear();
                drawBgImage()
                drawBgElement()
                drawBgGrid()
                drawNode();
                drawLink();
                ctrl.updateLayout(forceUpdate);
                updateScale();
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

            function drawBgGrid() {
                let g = svg.select('g')

                if (g.empty()) {
                    g = svg.append('g')
                        .classed('bg_grid', true)
                }

                try {
                    g.selectAll("*").remove();

                    const show = options['bg_grid']
                    if(show) {
                        const w = 1920
                        const h = 1080
                        const offset = 20
                        const strong = 100
                        const color = '#fff'

                        for (let x = 0; x < w; x+=offset) {
                            const isStrong = x % strong === 0 
                            if(isStrong) {
                                g.append('text')   
                                    .attr('x', x)
                                    .attr('y', '-50px')
                                    .style('font-size', '20px')
                                    .style('fill', color)
                                    .style('text-anchor', 'middle')
                                    .style('alignment-baseline', 'central')
                                    .html(x);
                            }
                            g.append('line')
                                .attr('x1', x)
                                .attr('y1', 0)
                                .attr('x2', x)
                                .attr('y2', h)
                                .style('stroke', color)
                                .style('stroke-width', isStrong ? 1 : 0.1)
                        }

                        for (let y = 0; y < h; y+=offset) {
                            const isStrong = y % strong === 0 
                            if(isStrong) {
                                g.append('text')   
                                    .attr('x', '-50px')
                                    .attr('y', y)
                                    .style('font-size', '20px')
                                    .style('fill', color)
                                    .style('text-anchor', 'middle')
                                    .style('alignment-baseline', 'central')
                                    .html(y);
                            }
                            g.append('line')
                                .attr('x1', 0)
                                .attr('y1', y)
                                .attr('x2', w)
                                .attr('y2', y)
                                .style('stroke', color)
                                .style('stroke-width', isStrong ? 1 : 0.1)
                        }
                    }

                } catch (error) {
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

            function drawNode(nodes) {
                let visualNodes = nodes || ctrl.getVisualNodeAll();

                /* #region [#노드 추가] */
                g_node = svg.select('g.nodes').selectAll('g.node_container')
                    .data(visualNodes);
                g_node.exit().remove();

                
                // 1. 신규 노드
                let new_node = g_node.enter()
                    .append('g')
                    .classed('node_container', true)
                    // .style('display',  d => {
                    //     const { minx, miny, maxx, maxy } = ctrl.screenRectWorldCoords || {}
                    //     const fx = d.x || d.fx
                    //     const fy = d.y || d.fy
                    //     if(fx && fy && !Number.isNaN(minx * miny * maxx * maxy)) {
                    //         return (minx <= fx && fx <= maxx) && (miny <= fy && fy <= maxy) ? 'block' : 'none'
                    //     }
                    //     return 'block';
                    // })

                updateNodeComponent(new_node, 'node');

                // 2. 노드 circle 사이즈
                updateNodeComponent(new_node, 'circle');

                // 3. 노드 아이콘 or 이미지
                /* #region [#폰트 아이콘을 이용한 노드 그리기] */
                updateNodeComponent(new_node, 'icon');
                /* #endregion "[#폰트 아이콘을 이용한 노드 그리기]"> */
                /* #region [#이미지를 이용한 노드 그리기] */
                updateNodeComponent(new_node, 'image');
                /* #endregion "[#이미지를 이용한 노드 그리기]"> */
                

                // 노드 에러 이미지
                updateNodeComponent(new_node, 'errorIcon') 

                // 노드 에러 카운트
                updateNodeComponent(new_node, 'errorCount') 
                
                // 노드 UPDATE - 맵1->맵2 로드 시 노드 element 와 data 간에 불일치를 제거한다.
                g_node.each(function (d, i) {
                    let selector = d3.select(this)
                    updateNodeComponent(selector, 'node');
                    updateNodeComponent(selector, 'circle'); 
                    updateNodeComponent(selector, 'icon')
                    updateNodeComponent(selector, 'image')
                    updateNodeComponent(selector, 'errorIcon') 
                    updateNodeComponent(selector, 'errorCount') 
                })

                updateNodeR()
                // updateNodeBadgeSize()
                // updateNodeWidthHeight()
                updateScale()

                g_node = new_node.merge(g_node);
                /* #endregion "[#노드 추가]"> */

                /* #region [#노드라벨 추가] */
                g_desc = svg.select('g.desc').selectAll('g.desc_container')
                    .data(visualNodes);
                g_desc.exit().remove();

                // 1. 신규 노드 라벨
                let new_desc = g_desc.enter()
                    .append('g')   
                
                // 2. 노드 라벨 추가
                updateNodeComponent(new_desc, 'desc');
                updateNodeComponent(new_desc, 'label');
                
                if(options['node_info_show']) {
                    let new_info = g_desc.enter()
                        .append('g') 
                    updateNodeComponent(new_info, 'info');
                    updateNodeComponent(new_info, 'label2');
                }

                // 3. 노드 라벨 UPDATE - 맵1->맵2 로드 시 노드 element 와 data 간에 불일치를 제거한다.
                g_desc.each(function (d, i) {
                    let selector = d3.select(this)
                    updateNodeComponent(selector, 'desc');
                    updateNodeComponent(new_desc, 'label'); 
                })

                g_desc = new_desc.merge(g_desc);

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
                /* #endregion "[#노드라벨 추가]"> */
            }

            function updateNodeComponent(selector, compName) {
                try {
                    const {options} = ctrl
                    switch(compName) {
                        case 'errorIcon':
                            if(!ctrl.options['node_badge_show'] ) {
                                selector.select('g').remove()
                                return
                            }
                            const g_nodeError = selector.selectAppend('g')
                            g_nodeError
                                .attr('class', 'node_error_icon')
                                .classed(className.invisible, d => !options['node_badge_show'] || d.alarm == undefined)

                            const image = g_nodeError.selectAppend('image')
                            image
                                .attr('xlink:href', function (d) {
                                    d.badge = iconImage['node_error_icon'];
                                    return d.badge.path;
                                })
                                .attr('transform', function (d) {
                                    let outWidth = options['node_badge_size'],
                                        outHeight = options['node_badge_size'],
                                        nodeR = options['node_r_width'];
                                    return 'translate(' + 0.5 * (nodeR + outWidth / 2) + ',' + (-1.0 * (nodeR + outHeight / 2)) + ') scale(' + outWidth / 100 + ' ' + outHeight / 100 + ')';
                                })
                                .attr('width', 100)
                                .attr('height', 100)
                            break;
                        case 'errorCount':
                            if(!ctrl.options['node_error_count_show'] ) {
                                selector.select('g').remove()
                                return
                            }

                            const g_nodeCount = selector.selectAppend('g');
                            g_nodeCount
                                .attr('class', 'node_error_count')
                                // .attr('transform', d => {
                                //     updateScale() 참고
                                // })
                                .style('width', 150)
                                .style('height', 100)
                                .classed(className.invisible, d => d?.fn_alarm_count_show(d))

                            // error count update animation transition (like heartbeat)
                            g_nodeCount.merge(g_nodeCount)
                                .transition()
                                .duration(100)
                                .ease(d3.easeExpOut) 
                                .attr('transform', d => {
                                    const r = getPixelByCurrentZoomScale(100 * 0.2, 'nodeError')
                                    const s = r / 100 * 1.3
                                    return `translate(${r * 0.8},${-r * 1.5}) scale(${s}, ${s})`
                                })
                                .transition()
                                .duration(100)
                                .ease(d3.easeExpOut) 
                                .attr('transform', d => {
                                    const r = getPixelByCurrentZoomScale(100 * 0.2, 'nodeError')
                                    const s = r / 100
                                    return `translate(${r * 0.8},${-r * 1.5}) scale(${s}, ${s})`
                                })

                            const rect = g_nodeCount.selectAppend('rect')
                            rect
                                .attr('x', '0')
                                .attr('y', '0')
                                .attr('rx', '25')
                                .attr('ry', '25')
                                .attr('width', 140 * options['node_badget_weight'])
                                .attr('height', 80 * options['node_badget_weight'])
                                .style('fill', d => d?.fn_alarm_count_color(d)[0] ?? 'red')
                                .style('stroke', '#fff')
        
                            const text = g_nodeCount.selectAppend('text')
                            text
                                .attr('x', 70 * options['node_badget_weight'])
                                .attr('y', 40 * options['node_badget_weight'])
                                .style('font-size', `${50 * options['node_badget_weight']}px`)
                                .style('fill', d => d?.fn_alarm_count_color(d)[1] ?? '#fff')
                                .style('text-anchor', 'middle')
                                .style('alignment-baseline', 'central')
                                .html(d => d?.fn_alarm_count(d));
                            break;
                        case 'node':
                            selector
                                .attr('id', function (d) {
                                    return ctrl.getNodeId(d)
                                })
                                .attr('class', function (d) {
                                    let alarm = d.alarm || 4;
                                    let selected = d.selected ? className.selected : ''
                                    return `node_container ${nodeAlarm[alarm].style} ${selected}`
                                })
                                .on('contextmenu', function (d, i, a) {
                                    let { ctrlKey, shiftKey } = d3.event;
                                    if(ctrlKey || shiftKey ) return
                                    try { return d3.contextMenu(contextMenu.node).call(this, d, i, a) } catch (error) { }
                                })
                                // .on('contextmenu', function (d) {
                                //     if (ctrl.hasEventListener(eventType.contextmenu)) {
                                //         d3.event.preventDefault();
                                //     }
                                //     ctrl.fireEvent({
                                //         eventType: eventType.contextmenu,
                                //         target: ctrl.getNodeElement(d),
                                //         target_type: elementType.node,
                                //         d: d,
                                //     });
                                // })
                                // .on('click', function (d, index, all) {
                                //     node click 의 경우 dragEndHandler 에서 clickHandler 호출하기 때문에 아래코드가 필요없다.(중복호출제거) 
                                //     ctrl.clickHandler(this, elementType.node, d, index, all)
                                // })
                                .on('mouseover', function (d) {
                                    if(ctrl.dragging) return 
                                    ctrl.over_node = this;
                                    d3.select(this).classed(className.focus, true)
                                    return node_tip.show.apply(this, arguments)
                                })
                                .on('mouseout', function (d) {
                                    d3.select(this).classed(className.focus, false)
                                    return node_tip.hide.apply(this, arguments)
                                })
                                .call(drag);
                            break;
                        case 'icon':
                            if(!ctrl.isIconNodeOption()) {
                                selector.select('text').remove()
                                return
                            }
                            const g_text = selector.selectAppend('text')
                            g_text
                                .attr('x', 0)
                                .attr('y', options['node_r_width'] / 2 + 2)
                                .style('font-size', options['node_r_width'])
                                .classed('node_icon', true)
                                // .classed('ftstcall', true)
                                .text('\ueaf2');
                            break;
                        case 'circle':
                            if(!options['node_r_show']) {
                                selector.select('circle').remove()
                                return
                            }
                            const g_circle = selector.selectAppend('circle')
                            g_circle
                                .classed('node_circle', true)
                                .attr('r', getPixelByCurrentZoomScale(options['node_r_width'] * 0.8, 'node'))
                            break;
                        case 'image':
                            if(ctrl.isIconNodeOption()) {
                                selector.select('image').remove()
                                return
                            }
                            const g_image = selector.selectAppend('image')
                            g_image
                                .classed('node_image', true)
                                .attr('xlink:href', function (d) {
                                    // return imgMap[d.type];
                                    let type = d.device_type || 'UNKNOWN'
                                    d.image = options.nodeImage[type] || options.nodeImage['UNKNOWN']
                                    return d.image.path;
                                })
                                .style('width', function (d) {
                                    return getPixelByCurrentZoomScale(100, 'node')
                                    // return  (d.image || {}).width || options['node_width'];
                                })
                                .style('height', function (d) {
                                    return getPixelByCurrentZoomScale(100, 'node')
                                    // return (d.image || {}).height || options['node_height'];
                                })
                            break;
                        case 'desc':
                            selector
                                .classed('desc_container', true)
                                .attr('id', function (d) {
                                    return ctrl.getNodeId(d, 'desc')
                                })
                                .on('click', function (d, index, all) {
                                    ctrl.clickHandler(ctrl.getNodeElement(d), elementType.node, d, index, all)
                                })
                                // .on('mouseover', function (d, index, all) {
                                // })
                                // .on('mouseout', function (d, index, all) {
                                // });
                            break;
                        case 'label':
                            const g_label = selector.selectAppend('text')
                            g_label
                                .attr('x', 0)
                                .attr('y', getPixelByCurrentZoomScale(options['node_r_width'], 'node'))
                                .style('fill', d => d.text_color ?? options['--svg-text-color'])
                                .text(d => d[options['node_text_field']]);
                            break;
                        case 'info':
                            selector
                                .classed('info_container', true)
                                .attr('id', function (d) {
                                    return ctrl.getNodeId(d, 'info')
                                })
                                .on('click', function (d, index, all) {
                                    ctrl.clickHandler(ctrl.getNodeElement(d), elementType.node, d, index, all)
                                })
                            break;
                        case 'label2':
                            const g_label2 = selector.selectAppend('text')
                            g_label2
                                .attr('x', 0)
                                .attr('y', getPixelByCurrentZoomScale(options['node_r_width'], 'node'))
                                .style('fill', d => d.text_color ?? options['--svg-info-text-color'])
                                .style('font-size', options['node_info_text_size'])
                                .text(d => d[options['node_info_field']]);
                            break;
                    }
                } catch(e) {}
            }

            function isLinkCut(d) {
                return d.fn_link_cut ? d.fn_link_cut(d) : false
            }

            function isLinkAlarm(d) {
                return d.fn_link_alarm ? d.fn_link_alarm(d) : false
            }

            function getLinkColor(d) {
                return d.fn_link_color ? d.fn_link_color(d, options) : options["link_color"]
            }

            function drawLink(parameter) {
                let visualLinks = ctrl.getVisualLinkAll();

                if (parameter) {
                    visualLinks.push(parameter);
                }

                g_link = svg.select('g.links').selectAll('g.link_container').data(visualLinks);
                g_link.exit().remove();

                //#region draw Link
                let new_link = g_link.enter()
                    .append('g')
                    .attr('id', function (d) {
                        return ctrl.getLinkId(d);
                    })
                    .on('mouseover', function (d) {
                        ctrl.over_node = this;
                        d3.select(this).classed(className.focus, true);
                    })
                    .on('mouseout', function (d) {
                        d3.select(this).classed(className.focus, false);
                    })
                    .on('contextmenu', function (d, i, a) {
                        try { return d3.contextMenu(contextMenu.link).call(this, d, i, a) } catch (error) { }
                    })
                    .classed('link_container', true)
                //#endregion draw Link

                //#region draw LinkPath
                new_link
                    .append('path')
                    .attr('id', function (d) {
                        return ctrl.getLinkId(d, 'link_path');
                    })
                    .style('stroke', function (d) {
                        return getLinkColor(d)
                    })
                    .style('stroke-width', function (d) {
                        return options['link_width'] + 'px';
                    })
                    .style('stroke-dasharray', function (d, index, elements) {
                        return d.dashLine || '';
                    })
                    .style('stroke-linecap', "round")
                    .classed('link_path', true)
                    .attr("marker-end", "url(#arrow)")
                //#endregion draw LinkPath
                    
                //#region draw LinkTraffic + LinkCut
                const dur = `${options['link_traffic_r_duration']}s`;
                const linkTrafficShow = options['link_traffic_show']
                new_link.each(function (d, i) {
                    let _this = d3.select(this);
                    let rect;

                    if(linkTrafficShow) {
                        rect = _this
                            .append("circle")
                            .attr("id", function (d) {
                                return ctrl.getLinkId(d, "link_traffic");
                            })
                            .attr("r", options['link_traffic_r'])
                            .attr("fill", options['link_traffic_r_color'])
                            .style("fill-opacity", options['link_traffic_r_opacity'])
                            .classed("link_traffic", true)
                            .classed(className.error, isLinkAlarm(d))
                            .classed(className.invisible, !options['link_traffic_show'] && !isLinkAlarm(d));

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
                    }

                    this.parentNode.appendChild(this);
                    d3.select(this).raise();

                    if (isLinkCut(d)) {
                        rect && rect
                            .append("animate")
                            .attr("id", "errorLinkTraffic")
                            .attr("attributeName", "opacity")
                            .attr("values", "1;0.3;0.1")
                            .attr("dur", dur)
                            .attr("repeatCount", "indefinite");

                        // http://objjob.phrogz.net/d3/object/499
                        const linkcut_image = _this
                            .append("image")
                            .classed("link_cut", true)
                            .attr("xlink:href", function (d) {
                                d.image = iconImage["link_cut_icon"];
                                return d.image.path;
                            })
                            .style("width", 100)
                            .style("height", 100);
                    }
                });
                //#endregion draw LinkTraffic + LinkCut

                //#region draw LinkLabel
                if(options['link_text_field'] !== 'none') {
                    new_link
                        .append('svg:text')
                        .append('svg:textPath')
                        .classed('link_label', true)
                        .attr('startOffset', '50%')
                        .attr('text-anchor', 'middle')
                        // .attr('alignment-baseline', 'middle')  // hanging, middle, baseline
                        .attr("xlink:href", function (d) {
                            // 텍스트를 항상 좌에서 위로 그리기 위해 link 가 아닌 linkselector 와 맵핑.
                            return "#" + ctrl.getLinkId(d, "link_path");
                            
                            // getLinkSelectId 로 하면 한 쪽 방향으로  텍스트가 출력 된다.
                            // return '#' + ctrl.getLinkSelectId(d);
                        })
                        // .style('fill', '#000')
                        .style('font-size', options['link_text_size'])
                        .style('fill', options["link_text_color"])
                }
                //#endregion draw LinkLabel

                //#region draw LinkSelector
                new_link
                    .append('path')
                    .attr('id', function (d) {
                        return ctrl.getLinkSelectId(d);
                    })
                    .style('stroke-width', function (d) {
                        return Math.max(options['link_selector_min_width'], (options['link_width'] * options['link_selector_weight'])) + 'px';
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
                //#endregion draw LinkSelector

                g_link = new_link.merge(g_link);

                ctrl.midpoints = g_link.data().map(d => {
                    return d.intermediate;
                });

                /* #region [#z-index 정렬] */
                new_link
                    .selectAll('.link_traffic')
                    .each(function (d, i) {
                        let traffic = d3.select(this);
                        d3.select(traffic.parentNode).raise();

                    });
                /* #endregion "[#z-index 정렬]"> */

                updateLinkText()
                updateLinkTrffic()
            }

            let xxx = 0 
            function updateLayout(forceUpdate = true) {
                /*
                    노드 링크 로직 적용
                */
                let { data } = ctrl;
                let { width, height } = ctrl.getViewBoxSize();
                simulation.nodes(data.nodes);
                link_force && link_force.links(data.links) 
                center_force && center_force.x(width / 2).y(height / 2) 
                
                /*
                    update link, node selector closure.
                    for performance.
                */
                simulation.on("tick", $do_tick.bind(ctrl, g_link, g_node, g_desc));
                $do_layout(forceUpdate);
            }

            async function load(data) {
                /* #region [#load: 새로운 노드와 시뮬레이션 링크 적용.] */
                let mapData
                if (typeof data === 'string') {
                    const fileName = data;
                    mapData = await loadFromFile(fileName)
                    localStorage['last_map'] = fileName;
                } else if (data) {
                    mapData = (fnConvert[data.config && data.config.converter] && fnConvert[data.config.converter](data)) || data
                    mapData = fnConvert['commonConvert'](mapData)
                } else if (options['load_from_local]']) {
                    mapData = JSON.parse(localStorage.getItem(`saved_${window.pageParams.mode}`));
                } else {
                    const fileName = options.fileName;
                    mapData = await loadFromFile(fileName);
                    localStorage['last_map'] = fileName;
                }

                loadData(mapData);

                function loadFromFile(fileName) {
                    return new Promise((resolve, reject) => {
                        d3.json(`${options.filePath}${fileName}`, function (error, data) {
                            if (error) {
                                throw error;
                            }
                            log('before convert data:', data);
                            if(data.config) {
                                log('converter:', data.config.converter);
                                data = (fnConvert[data.config && data.config.converter] && fnConvert[data.config.converter](data)) || data
                                data = fnConvert['commonConvert'](data)
                            }
                            log('after convert data:', data);
                            resolve(data);
                        });
                    })
                }
                /* #endregion "[#load: 새로운 노드와 시뮬레이션 링크 적용.]"> */
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
                let config = data.config || { options };
                data.config = config 

                if(config.dataOnly) {
                    return config
                }

                /* options */
                /* default 옵션으로 reset */
                objectCopyPrimitive(defaultOptions, options);

 
                /* 옵션값 (config > options) */
                if (config.options != undefined) {
                    Object.keys(config.options).forEach((key) => {
                        if (Array.isArray(options[key])) {
                            options[key] = options[key].concat(config.options[key])
                        } else if (options[key] instanceof Object) {
                            Object.assign(options[key], config.options[key])
                        } else {
                            options[key] = config.options[key]
                        }
                    })
                }

                Object.keys(options).filter(k => k.startsWith('--')).forEach(k => {
                    setStyleProperty(k, options[k])
                })

                const viewBox = options['svg_viewbox']
                if (viewBox) document.querySelector(SVG_SELECTOR).setAttribute("viewBox", viewBox);
                else document.querySelector(SVG_SELECTOR).removeAttribute("viewBox");
               
                /* background 이미지 (config > map) */
                let bg_image = svg.select("image.bg_image");
                let bg_width = '100%';
                let bg_height = '100%';
                let bg_image_path = "";
                let bg_opacity = "";
                if (config.map != undefined && config.map.constructor == Object) {
                    bg_width = config.map.width || bg_width;
                    bg_height = config.map.height || bg_height;
                    bg_image_path = config.map.path || bg_image_path;
                    bg_opacity = config.map.opacity || bg_opacity;
                }

                if (bg_image.size()) {
                    bg_image
                        .attr("width", bg_width)
                        .attr("height", bg_height)
                        .attr("xlink:href", bg_image_path)
                        .style("opacity", bg_opacity);
                }

                /* 줌 reset */
                const configZoom = config.zoom || {};
                configZoom.initScale = (configZoom.initScale !== null && configZoom.initScale !== undefined) ? configZoom.initScale : 1;
                ctrl.options.zoom = configZoom

                if (options.zoom_scale_min || options.zoom_scale_max) {
                    const min = ctrl.options.zoom_scale_min || options.zoom_scale_min
                    const max = ctrl.options.zoom_scale_max || options.zoom_scale_max
                    ctrl.zoom.scaleExtent([min, max])
                }

                // const { x, y, k} = d3.zoomTransform(svg)    // current
                const x = (config?.zoom?.sx)
                const y = (config?.zoom?.sy)
                const scale = (config?.zoom?.sscale || configZoom.initScale || 1)
                ctrl.zoomInByPos(x, y, scale, 0);
                setTimeout(() => {
                    resetZoom()  
                }, 500);

                svg.call(configZoom.disable ? d3.zoom() : zoom);

                return config
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

            function loadData(data, isloadConfig = true) {
                // log(`called loadData()`, data)
                const config = isloadConfig ? loadConfig(data) : data.config;
                
                data.nodes = data.nodes || []
                data.links = data.links || []

                // A mapping: {node.id: node}
                let node_by_id = d3.map(data.nodes, function (d) {
                    return d.id;
                });
                let visualLinks = [];
                let addedNodes = []
                let addedLinks = []
                data.links.forEach((link, idx) => {
                    let source = link.source = node_by_id.get(link.source_id),
                        target = link.target = node_by_id.get(link.target_id),
                        intermediate = {};

                    if (!source || !target)
                        return true

                    let id = ctrl.getLinkId(link);

                    // 중복체크
                    if (visualLinks.findIndex(e => e.id === id) >= 0) {
                        delete link.index;
                        return true;
                    }

                    link.index = idx;

                    if(options.debug_intermediate) {
                        intermediate.nodeId = ctrl.getLinkId(link, 'intermediate');
                        addedNodes.push(intermediate);
                        addedLinks.push(
                            { 'source': source, 'target': intermediate },
                            { 'source': intermediate, 'target': target }
                        );
                    }

                    if (!source || !target || (options.debug_intermediate && !intermediate)) {
                        debugger
                    }

                    visualLinks.push(Object.assign(link, {
                        'id': link.id,
                        'index': idx,
                        'source': source,
                        'intermediate': intermediate,
                        'target': target,
                        'target_id': link.target.id,
                        'source_port_disp': (link['source_port_disp'] || '정보없음'),
                        'target_port_disp': (link['target_port_disp'] || '정보없음'),
                    }));
                });

                if(options.debug_intermediate) {
                    data.links.push(...addedLinks)
                    data.nodes.push(...addedNodes)
                    data.visualLinks = visualLinks

                    // 중복제거
                    data.links.remove(link => !('id' in link));
                } else {
                    data.links = visualLinks
                }
                
                ctrl.data = data;
                Object.assign(ctrl, { g_link, g_node, g_desc })

                // let fixNodes = data.nodes.filter(n => n.fx || n.fy)
                // draw(fixNodes !== data.nodes.length);
                draw(true);
            }

            function $do_layout(forceUpdate = true) {
                if (simulationRestart) {
                    updateSimulation()
                    updateLinkForce()
                    updateChangeForce()
                    simulation.alpha(1).restart();
                } else {
                    if (options['link_type'] == "curved") {
                        $do_animated_layout(forceUpdate);
                    } else {
                        $do_static_layout(forceUpdate);
                    }
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
                    // if (options['link_type'] != "curved") {
                    simulation
                        .force('center', center_force)
                        .force('charge', charge_force)
                        .force('link', link_force);
                    // }
                    simulation.alpha(1);

                    if (!options['load_from_local]']) {
                        for (let i = 0, n = Math.ceil(Math.log(simulation.alphaMin()) / Math.log(1 - simulation.alphaDecay())); i < n; ++i) {
                            simulation.tick();
                        }
                    }
                }

                if (options['link_type'] != "curved") {
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

                if (options['link_type'] != "curved") {
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
                        if (options['link_type'] != "curved") {
                            simulation
                                .force('center', null)
                                .force('charge', null)
                                .force('link', null);
                        }
                    }
                });

            }

            function $do_tick(link_sel, node_sel, desc_sel) {
                const ctrl = this;

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
                // node_sel.attr('transform', function (d) {
                //     return ctrl.transformHandler(this, elementType.node, d);
                // });

                // test
                // nudge nodes to proper foci
                {
                    // var k = simulation.alpha() * 0.3;
                    node_sel.attr('transform', function (d) {
                        return ("translate(" + d.x + "," + d.y + ")".concat(" scale(" + ctrl.getNodeWeightOption(this) + ")"));
                    });
                }

                /*
                    description 업데이트
                */
                desc_sel.attr('transform', function (d) {
                    return 'translate(' + d.x + ',' + d.y + ')';
                });
                /* 
                    information 업데이트
                */
                const info_sel = svg.select('g.desc').selectAll('g.info_container');
                info_sel.attr('transform', function (d) {
                    const y = Number(d.y) + options['node_info_offset_top'];
                    return 'translate(' + d.x + ',' + y + ')';
                });

                /*
                    debug: intermediate 노드 위치 업데이트
                */
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
                    .classed(className.invisible, options['link_type'] != "curved");

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
                updateLayout,
                options,
                getSaveData,
                $do_one_tick
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
            hasEventListener: function (type) {
                return this.eventListeners.some(e => e.eventType === type)
            },
            dispatchEvent: function (event) {
                for (var i = 0; i < this.eventListeners.length; i++)
                    if (event.eventType === this.eventListeners[i].type)
                        this.eventListeners[i].eventHandler(event);
            },
            fireEvent: function (event) {
                this.preFireEvent(event);
                this.dispatchEvent(event);
            },
            preFireEvent: function (event) {
                this.blurElement(event)
            },

            blurElement: function (event) {
                const ctrl = this;
                const { target_type } = event

                // only eventType.selectChanged
                if(event.eventType !== eventType.selectChanged) return 

                // only mouseMode.selectBlur
                if (ctrl.options['action_mode'] !== mouseMode.selectBlur) return

                if(target_type === "svg") {
                    document.querySelectorAll("svg g.blur").forEach(el => {
                        el.classList.remove('blur');
                    });
                    return 
                }

                document.querySelectorAll("svg > g > g").forEach(el => {
                    el.classList.add('blur');
                });

                if(target_type === "link") {
                    const link = event.d
                    removeLinkBlur(link);
                    return 
                } 
                
                if(target_type === "node") {
                    const node = event.d
                    removeNodeBlur(node)

                    // find contact-links
                    let links = ctrl.getContactLinks(node);
                    links.forEach(l => { removeLinkBlur(l) })
                    return
                } 

                function removeNodeBlur(node) {
                    ctrl.getNodeElement(node)?.classList.remove('blur');
                    ctrl.getNodeElement(node,'desc')?.classList.remove('blur');
                     
                }
                
                function removeLinkBlur(link) {
                    ctrl.getLinkElement(link)?.classList.remove('blur');
                    removeNodeBlur(link.source)
                    removeNodeBlur(link.target)
                }
            },

            /**
             * 엘리먼트를 삭제한다.
             * @param element
             */
            // removeElement: function (element) {
            //     const ctrl = this;
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
                const ctrl = this;
                let transform = d3.zoomTransform(ctrl.svg.node());
                let pos = transform.invert(d3.mouse(element));

                let nodes = ctrl.getNodeAll();
                let index = nodes.length;
                let id = `new_node_${index}`;

                nodes.push({
                    id: id,
                    device_name: id,
                    device_type: '1',
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
                const ctrl = this;
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
                const ctrl = this;
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
                const ctrl = this;
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
                try {
                    return (prefix || "node") + "_" + (d.id?.replace(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g, '_') || '')
                } catch (error) {
                }
            },

            /**
             * 링크컨테이너를 반환한다.
             * @param d
             * @returns {HTMLElement}
             */
            getLinkElement: function (d) {
                const ctrl = this;
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
                
                return (prefix || "link") + "_" + (myid ? String(myid).replace(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g, '_') : '')    
            },

            /**
             * 링크선택영역 ID 를 반환한다.
             * @param d
             * @param prefix
             * @returns {string}
             */
            getLinkSelectId: function (d, prefix) {
                const ctrl = this;
                return ctrl.getLinkId(d, "link_selector");
            },

            /**
             * 모든 노드를 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getNodeAll: function () {
                const ctrl = this;
                return ctrl.data.nodes;
            },

            /**
             * 모든 링크를 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getLinkAll: function () {
                const ctrl = this;
                return ctrl.data.links;
            },

            /**
             * 모든 설정을 반환한다. (가상 + 실제)
             * @returns {*}
             */
            getConfigAll: function () {
                const ctrl = this;
                return ctrl.data.config ;
            },

            isVisualNode: (d) => "id" in d,
            isNotVisualNode: (d) => !("id" in d),

            /**
             * 실제링크를 반환한다.
             * @returns {*}
             */
            getVisualNodeAll: function () {
                const ctrl = this;
                return ctrl.getNodeAll().filter(ctrl.isVisualNode);
            },

            /**
             * 가상링크를 반환한다.
             * @returns {Array}
             */
            getVisualLinkAll: function () {
                const ctrl = this;
                return ctrl.data.visualLinks || ctrl.data.links;
            },

            /**
             * 가상노드를 반환한다.
             * @returns {*}
             */
            getNotVisualNodeAll: function () {
                const ctrl = this;
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
                const ctrl = this;
                let { options } = ctrl;
                let selected = d3.select(element.parentNode).classed(className.selected);
                const linkWidth = (function (d) {
                    return (options['link_width'] * (selected ? options['node_selector_weight'] : 1));
                })()
                
                function fnDrawLinkSelector() {
                    d3.select(element)
                        .style("stroke-width", Math.max(options['link_selector_min_width'], (linkWidth * options['link_selector_weight'])) + "px")
                    return drawLink(d, false);
                }

                function fnDrawLink() {
                    d3.select(element)
                        .style("stroke-width", linkWidth + "px")
                        .style("stroke-dasharray", selected ? linkWidth * 2 + "px" : "unset")
                        .style("stroke-dashoffset", selected ? linkWidth * 4 + "px" : "unset");
                    return drawLink(d, true);
                }

                /**
                 * 옵션에 따른 링크 Path를 반환한다.
                 * @param d
                 * @param leftHand : true 일 경우 왼쪽에서 오른쪽 즉 순방향을 의미.
                 * @returns {string}
                 */
                let drawLink = function () {
                    const ctrl = this;
                    let { options } = ctrl;

                    if (options['link_type'] == "arc") {
                        return drawArc(...arguments);
                    } else if (options['link_type'] == "line") {
                        return drawLine(...arguments);
                    }
                    else if (options['link_type'] == "mixed") {
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
                    const ctrl = this;
                    let { options } = ctrl;
                    let autoReverse = options['link_direction_auto'];
                    let marker = document.getElementsByTagName('marker');

                    let { leftHand, reverse } = options.getDrawingProperty(d, directional);
                    let log = false ? console.log : () => {}
                    
                    let x1 = leftHand ? d.source.x : d.target.x,
                        y1 = leftHand ? d.source.y : d.target.y,
                        x2 = leftHand ? d.target.x : d.source.x,
                        y2 = leftHand ? d.target.y : d.source.y,
                        siblings = ctrl.getSiblingLinks(d.source, d.target),
                        siblingCount = siblings.length,
                        median_link = (siblingCount + 1) / 2;
              
                    let getLineIndex = d3
                        .scalePoint()
                        .domain(siblings)
                        .range([1, siblingCount]);

                    /* 링크가 홀수 개이고 링크 중 가운데에 긋는 링크 일 경우 */
                    const isCenterLine = siblingCount % 2 != 0 && getLineIndex(d.index) == (siblingCount + 1) / 2

                    let angle = getPointsAngle(x1, y1, x2, y2);
                    let point = getContactPoint(angle);

                    log({ i: d.index, i2: getLineIndex(d.index), angle, point, x1, y1, x2, y2 })
                    marker[0].setAttribute('refX', options.getRefX());

                    if (isCenterLine) {
                        return ("M" + x1 + "," + y1 + "L" + " " + x2 + "," + y2);
                    } else { 
                        const line = ("M" + (x1 + point.x) + "," + (y1 + point.y) + "L" + " " + (x2 + point.x) + "," + (y2 + point.y))
                        // log({x1, px: point.x, line})
                        return line; 
                    }

                    function getPointsAngle(x1, y1, x2, y2) {
                        let angle = Math.atan((y2 - y1) / (x2 - x1)) / (Math.PI / 180);
                        let pointsAngle = 0;
                        
                        if (options['link_type'] == "mixed") {
                            pointsAngle = angle + (270 - (getLineIndex(d.index) + 1) * (180 / (siblingCount - 1)));
                        } else { 
                            pointsAngle = angle + (270 - (getLineIndex(d.index) - 1) * (180 / (siblingCount - 1))); 
                        }

                        /* 위치보정값 false 일 때 링크 위치 수정 */
                        if (!autoReverse && getLineIndex(d.index) < median_link && (d.source.x > d.target.x)) {
                            pointsAngle = pointsAngle + 180;
                        } else if (!autoReverse && getLineIndex(d.index) > median_link && (d.source.x < d.target.x)) {
                            pointsAngle = pointsAngle + 180;
                        } 

                        // 결과 값 범위  angle:-90~90, pointsAngle:0~360
                        // log(`angle=${angle}, pointsAngle=${pointsAngle}`)
                        return pointsAngle;
                    }

                    /* 접선의 좌표 구하기 */
                    function getContactPoint(angle) {
                        let radian = (angle) * (Math.PI / 180);
                        let x = Number(Math.cos(radian).toFixed(5)),
                            y = Number(Math.sin(radian).toFixed(5));

                        let r = options['node_r_width'] < 10 ? 10 : (options['node_r_width']) / 2;
                        let divideValue = (siblingCount > 2) ? siblingCount : Math.pow(siblingCount, 2);
                        // let linkDistance = (r * options['link_width']) / Math.pow(divideValue, 2);
                        let linkDistance = d.distance ||  (r * options['link_width']) / Math.pow(divideValue, 2);

                        // 수직인 직선일 경우 예외 (3개까지는 되는데 4개부터 중복이 생긴다. 나중에 수정하자)
                        // if (x1 === x2 && y1 !== y2) {
                        //     x = x * (d.index >= siblingCount / 2) ? leftHand : -leftHand
                        //     return { x: x * linkDistance, y: y };
                        // }

                        if (x1 === x2 && y1 !== y2) {
                            let index = getLineIndex(d.index) - 1 
                            if (siblingCount % 2 == 0 && siblingCount / 2 <= index) {
                                index = index + 1
                            }
                            x = (index - Math.floor(siblingCount / 2)) * linkDistance
                            return { x: x, y: y };
                        }

                        return { x: x * linkDistance, y: y * linkDistance };
                    }
                }.bind(ctrl);

                /**
                 * Sibling 개수에 따른 Arc 링크의 Path를 반환한다.
                 * @param d
                 * @param leftHand : true 일 경우 왼쪽에서 오른쪽 즉 순방향을 의미. 역방향일 경우 순방향으로 바꾸어 그리도록 한다.
                 * @returns {string}
                 */
                let drawArc = function (d, directional) {
                    const ctrl = this;
                    let { options } = ctrl;
                    let { leftHand, reverse } = options.getDrawingProperty(d, directional);

                    let x1 = leftHand ? d.source.x : d.target.x,
                        y1 = leftHand ? d.source.y : d.target.y,
                        x2 = leftHand ? d.target.x : d.source.x,
                        y2 = leftHand ? d.target.y : d.source.y,
                        dx = x2 - x1,
                        dy = y2 - y1,
                        siblings = ctrl.getSiblingLinks(d.source, d.target),
                        siblingCount = siblings.length,
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

                        /* #region [#거리에 따른 링크 형제 링크의 간격 보정] */

                        /*
                         * curvature 값이 작을수록 간격이 벌어짐(원에 가까워짐)
                         */
                        if (distance < 200) {
                            curvature = siblingCount < 5 ? 2 : (curvature * 2) / getArcIndex(d.index);
                        }

                        if (distance / siblingCount > 200) {
                            curvature = (curvature * siblingCount * 4) / getArcIndex(d.index);
                        }
                        /* #endregion "[#거리에 따른 링크 형제 링크의 간격 보정]"> */

                        drx = (drx / (1 + (1 / siblingCount) * (getArcIndex(d.index) - 1))) * curvature;
                        dry = (dry / (1 + (1 / siblingCount) * (getArcIndex(d.index) - 1))) * curvature;

                        const leftHandOffset = leftHand ? 0 : 1; /* 화면 노드 위치에 따른 보정값 */
                        const directionOffset = options['link_direction_auto'] ? 0 : d.source.index < d.target.index ? 0 : 1; /* 장비의 연결관계에 따른 보정값 */
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
                    const ctrl = this;
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
                const ctrl = this;
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

                let outWidth = options['link_badge_size'],
                    outHeight = options['link_badge_size'];

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
                // log("dragStartHandler")
                
                const ctrl = this;
                let { simulation } = ctrl;   

                if(d3.select(element).classed('blur')) {
                    return 
                }

                ctrl.dragging = true
                ctrl.hideContextMenu();

                if (!d3.event.active) {
                    simulation.alphaTarget(0.3).restart();
                }

                d.fx = d.x;
                d.fy = d.y;
                d.sx = d.x;
                d.sy = d.y;

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
                // log("draggingHandler")
                const ctrl = this;

                if (ctrl.options['node_drag_use'] === false) { return }
                if (ctrl.options['action_mode'] === mouseMode.none) { return }
                if (d3.select(element).classed('blur')) { return }

                if (ctrl.options['grid_mode']) {
                    const unit = ctrl.options['grid_cell_width']
                    d.fx = Math.ceil(d3.event.x / unit) * unit
                    d.fy = Math.ceil(d3.event.y / unit) * unit
                } else {
                    d.fx = d3.event.x;
                    d.fy = d3.event.y;
                }

                // start 에서 해야하는데 그렇게 되면 select 이벤트가 안먹는다
                // element.parentNode.appendChild(element);

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
                // log("dragEndHandler")
                const ctrl = this;
                let { simulation } = ctrl;

                if (!d3.event.active) {
                    simulation.alphaTarget(0);
                }

                d3.select(element).classed(className.dragging, false);

                if(d.fx == d.sx && d.fy == d.sy) {
                   ctrl.clickHandler(element, type, d, index, all) 
                } else {
                    d.fx = d.x;
                    d.fy = d.y;
                }

                delete d.sx
                delete d.sy

                ctrl.dragging = false
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
                const ctrl = this;
                if (type == elementType.node) {
                    var a = 1;
                }

                return ("translate(" + d.x + "," + d.y + ")".concat(" scale(" + ctrl.getNodeWeightOption(element) + ")"));
            },

            zoomStartHandler: function (transform) {
                const ctrl = this;
                const { k, x, y } = transform

                ctrl.hideContextMenu();
                ctrl.zoomDebounceHandler(ctrl, d3.event.transform)

                if (ctrl.zoom_scale != k) {
                    let scale = (ctrl.zoom_scale = k);
                    setStyleProperty("--zoom-scale", scale)
                }
            },

            zoomEndHandler: function (transform) {
                const ctrl = this;
                const { k, x, y } = transform

                if (ctrl.options['node_fix_size']) {
                    ctrl.options.updateScale();
                }

                ctrl.showLinkPortTooltip(null, null);
            },

            zoomDebounceHandler: debounce((ctrl, transform) => {
                if(ctrl.dragging) return
                
                const { width, height } = ctrl.getViewBoxSize();
                const { k } = transform
                const [x, y] = transform.invert([width / 2, height / 2])
                const { options } = ctrl.getConfigAll()

                if (options && options['svg_viewbox']) {
                    const diffW = (ctrl.svg.node().clientWidth - width) / 2
                    const diffH = (ctrl.svg.node().clientHeight - height) / 2
                    const [minx, miny] = transform.invert([-diffW, -diffH])
                    const [maxx, maxy] = transform.invert([width + diffW, height + diffH])
                    ctrl.screenRectWorldCoords = { minx, miny, maxx, maxy } 
                } else {
                    const [minx, miny] = transform.invert([0, 0])
                    const [maxx, maxy] = transform.invert([width, height])
                    ctrl.screenRectWorldCoords = { minx, miny, maxx, maxy } 
                }

                // svg panning 거리가 짧으면 클릭으로 처리
                const [x0, y0] = ctrl.centerWorldCoords
                if (Math.abs(x0 - x) + Math.abs(y0 - y) < 10) {
                    ctrl.clearSelection();
                }
                ctrl.centerWorldCoords = [x, y]

                ctrl.options.updateVisible()

                log(`zoom=${k.toFixed(2)}, x=${x.toFixed(2)}, y=${y.toFixed(2)}, screen=`, ctrl.screenRectWorldCoords);
            }, 200),

            /**
             * 클릭에 대한 이벤트를 받아 처리한다.
             * @param element
             * @param type
             * @param d
             * @param index
             * @param all
             */
            clickHandler: function (element, type, d, index, all) {
                const ctrl = this;
                const svgNode = ctrl.svg.node()
                                
                let { ctrlKey, shiftKey, altKey } = d3.event;
                if (altKey) {
                    log(`clickHandler:`, element, type, d);
                    const transform = d3.zoomTransform(svgNode)
                    const screenPos = d3.mouse(svgNode)
                    const worldCoords = transform.invert(screenPos)
                    log(`screenCoords=`, screenPos, `, worldCoords=`, worldCoords);
                }

                if(d3.select(element).classed('blur')) {
                    return 
                }

                ctrl.hideContextMenu();

                switch (ctrl.options['action_mode']) {
                    case mouseMode.none:
                    case mouseMode.select:
                    case mouseMode.selectBlur:
                        ctrl.onSelect(type, d);
                        break;
                    case mouseMode.edit:
                        if (ctrlKey) ctrl.onAdd(type, d);
                        else if (shiftKey) ctrl.onDelete(type, d);
                        else ctrl.onSelect(type, d);
                        break;
                }
                d3.event.stopPropagation && d3.event.stopPropagation();
            },

            onSelect: function(type, d) {
                const ctrl = this;
                if (type == elementType.svg) {
                    ctrl.clearSelection();
                } else {
                    // d3.select(element).classed(className.dragging, true);
                    ctrl.selectElement.call(ctrl, type, d, false);
                }

                switch (type) {
                    case elementType.link:
                    case elementType.svg:
                        ctrl.options.updateLink();
                        break;
                }
            },

            onAdd: function(type, d) {
                const ctrl = this;
                if (type == elementType.svg) {
                    ctrl.addNode(element);
                } else {
                    ctrl.onSelect(type, d);
                }
            },

            onDelete: function(type, d) {
                const ctrl = this;
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
                    ctrl.onSelect(type, d);
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
                const ctrl = this;
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
                const ctrl = this;
                ctrl.hideLinkPortTooltip(d);
            },

            /**
             * 모든 노드/링크 를 지운다.
             */
            clear: function (exclude = []) {
                const ctrl = this;
                let { svg } = ctrl;

                ctrl.clearSelection();

                let contents = {
                    links: svg.select("g.links").selectAll("*"),
                    nodes: svg.select("g.nodes").selectAll("*"),
                    desc: svg.select("g.desc").selectAll("*"),
                    interNodes: svg.select("g.intermediate_nodes").selectAll("*")
                }

                Object.keys(contents).forEach(key => {
                    if (!exclude.includes(key)) {
                        contents[key].remove()
                    }
                })

                /* 
                let links = svg.select("g.links").selectAll("*");
                let nodes = svg.select("g.nodes").selectAll("*");
                let desc = svg.select("g.desc").selectAll("*");
                let interNodes = svg.select("g.intermediate_nodes").selectAll("*");

                links.remove();
                nodes.remove();
                desc.remove();
                interNodes.remove(); 
                */
            },

            /**
             * 선택 영역을 초기화 한다.
             */
            clearSelection: function () {
                const ctrl = this;
                const classList = `${className.selected} ${className.focus}`;

                ctrl.hideContextMenu();
                d3.selectAll(".node_container")
                    .classed(classList, false)
                    .data().forEach(d => d[className.selected] && delete d[className.selected])
                d3.selectAll(".link_container")
                    .classed(classList, false)
                    .data().forEach(d => d[className.selected] && delete d[className.selected])

                ctrl.hideNodeTooltip();
                ctrl.hideLinkPortTooltip(null, 0);
                ctrl.setInfomation();

                if (ctrl.select) {
                    ctrl.select = null;
                    ctrl.fireEvent({
                        eventType: eventType.selectChanged,
                        target: null,
                        target_type: elementType.svg,
                        d: null,
                    });
                }
            },

            /**
             * 선택한 element 에 대한 설명을 출력한다.
             * @param type
             * @param d
             * @param element
             */
            setInfomation: function (type, d, element, slot = null) {
                const ctrl = this;

                if (type == undefined) {
                    document.querySelector(".properties").style.display = "none";
                    d3.select(".animation-blink").classed("animation-blink", false);
                    return;
                }

                const showProperties = ctrl.options['properties-use']
                document.querySelector('div.properties').style.display = showProperties ? 'block' :'none';

                const setText = (element, text) => {
                    element && (element.innerText = text)
                }

                switch (type) {
                    case elementType.node:
                        document.querySelector(".properties").classList.add("node-info");
                        document.querySelector(".properties").classList.remove("link-info");
                        setText(document.querySelector(".node-info .alias > *:nth-child(2)"), d.device_name);
                        setText(document.querySelector(".node-info .id > *:nth-child(2)"), d.id);
                        setText(document.querySelector(".node-info .type > *:nth-child(2)"), d.device_type);
                        setText(document.querySelector(".node-info .xy > *:nth-child(2)"), `${d.x?.toFixed(0)},${d.y?.toFixed(0)}`);
                        setText(document.querySelector(".node-info .instlocation > *:nth-child(2)"), d.instlocation);
                        setText(document.querySelector(".node-info .debugDesc > *:nth-child(2)"), d.debugDesc);

                        if (slot == null) {
                            slot = ".S" + Math.randomInt(10, 20);
                            d3.select(slot).classed("animation-blink", true);
                        } else {
                            if (slot.length > 1) {
                                for (let i = 0; i < slot.length; i++) {
                                    d3.select(slot[i]).classed("animation-blink", true);
                                }
                            } else { 
                                d3.select(slot[0]).classed("animation-blink", true); 
                            }
                        }
                        break;
                    case elementType.link:
                        document.querySelector(".properties").classList.add("link-info");
                        document.querySelector(".properties").classList.remove("node-info");
                        setText(document.querySelector(".link-info .alias > *:nth-child(2)"), d.source.device_name);
                        setText(document.querySelector(".link-info .id > *:nth-child(2)"), d.source.id);
                        setText(document.querySelector(".link-info .type > *:nth-child(2)"), d.source.device_type);
                        setText(document.querySelector(".link-info .xy > *:nth-child(2)"), `${d.source.x.toFixed(0)},${d.source.y.toFixed(0)}`);
                        setText(document.querySelector(".link-info .instlocation > *:nth-child(2)"), d.source.instlocation);
                        setText(document.querySelector(".link-info .debugDesc > *:nth-child(2)"), d.source.debugDesc);

                        setText(document.querySelector(".link-info .alias > *:nth-child(3)"), d.target.device_name);
                        setText(document.querySelector(".link-info .id > *:nth-child(3)"), d.target.id);
                        setText(document.querySelector(".link-info .type > *:nth-child(3)"), d.target.device_type);
                        setText(document.querySelector(".link-info .xy > *:nth-child(3)"), `${d.target.x.toFixed(0)},${d.target.y.toFixed(0)}`);
                        setText(document.querySelector(".link-info .instlocation > *:nth-child(3)"), d.target.instlocation);
                        setText(document.querySelector(".link-info .debugDesc > *:nth-child(3)"), d.target.debugDesc);

                        let link_alias
                        try {
                            link_alias = d3.select(document.getElementById(element.id).parentNode).select(".link_label")?.text();    
                        } catch (error) {
                        }
                        setText(document.querySelector(".link-info > div:nth-child(2) .id > *:nth-child(2)"), d.id);
                        setText(document.querySelector(".link-info > div:nth-child(2) .link_alias > *:nth-child(2)"), link_alias);
                        setText(document.querySelector(".link-info > div:nth-child(2) .debugDesc > *:nth-child(2)"), d.debugDesc);
                        break;
                    default:
                }
            },

            /**
             * element 를 선택한다.
             * @param d
             * @param type
             */
            selectElement: function (type, d, forceUpdate = false) {
                const ctrl = this;
                const selectToggle = ctrl.select?.d === d && ctrl.options['action_mode'] === mouseMode.selectBlur
                
                if (ctrl.options['select_single']) {
                    ctrl.clearSelection();
                }

                if(selectToggle) return;
                
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

                ctrl.select = { d, element };
                let selected = element.classList.contains(className.selected);

                if (ctrl.options['select_single']) {
                    // 1개의 선택영역 만 가능
                    if (!selected) {
                        d[className.selected] = true
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
                    const dx = Math.pow(d.source.x-d.target.x, 2)
                    const dy = Math.pow(d.source.y-d.target.y, 2)
                    log(`distance: ${d3.format(".2f")(Math.sqrt(dx+dy))} px`)
                }

                forceUpdate && ctrl.$do_one_tick();

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
                const ctrl = this;
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
                const ctrl = this;
                let links = ctrl.getLinkAll();
                let siblings = [];
                for (let i = 0; i < links.length; ++i) {
                    try {
                        if (
                            (links[i].source.id == source.id && links[i].target.id == target.id) ||
                            (links[i].source.id == target.id && links[i].target.id == source.id)
                        ) {
                            siblings.push(links[i].index);
                        }
                    } catch (error) {
                        console.error(error)
                    }
                }
                return siblings;
            },

            /**
             * 노드 datum과 연결된 링크 반환한다. 1-Depth
             * @param d
             * @returns {Array}
             */
            getContactLinks: function (d) {
                const ctrl = this;
                let links = ctrl.getLinkAll();
                let result = [];
                for (let i = 0; i < links.length; ++i) {
                    if (!links[i].id) {
                        continue;
                    }
                    if (links[i].source.id == d.id || links[i].target.id == d.id)
                        result.push(links[i]);
                }
                return result;
            },

            getContactNodes: function (d) {
                
            },

            /**
             * 노드 datum과 연결된 링크 반환한다. All-Depth
             * @param d
             * @returns {Array}
             */
            getContactLinksAll: function (d) {
                // let result = [];
                // let links = getContactLinks(d)
                // let nextNodes = links.map(l => )
            },

            /**
             * 해당 중간점을 지나는 모든 중간점 링크를 반환한다.
             * @param array
             * @returns {Array}
             */
            getIntermediateLinks: function (array) {
                const ctrl = this;
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
                const ctrl = this;

                // 선택했을 때 툴팁을 보여주지 않는다. (툴팁 좌표가 이상하게 계산된다.)
                // try {
                //     let selectElement = ctrl.getLinkElement(d);
                //     ctrl.showLinkPortTooltip(selectElement, d);
                // } catch (e) {
                //     console.error(e);
                // }
            },

            /**
             * 링크와 연결된 포트 정보를 출력한다.
             * @param element
             * @param d
             */
            showLinkPortTooltip: function (element, d) {
                const ctrl = this;
                let { options } = ctrl;
                let { svg, link_source_tip, link_dest_tip } = ctrl;

                if(ctrl.dragging) return 
                if (!element) return;

                let src_node, dst_node;
                d = d || d3.select(element).datum();

                /*
                    focus on target link
                */
                // d3.select(element.parentNode).classed(className.focus, true);

                /* #region [#focus on target and source node and show tips.] */
                svg
                    .select("g.nodes")
                    .selectAll("g.node_container")
                    .each(function (node_d) {
                        if (node_d.id == d.source.id) {
                            src_node = d3.select(element);
                        } else if (node_d.id == d.target.id) {
                            dst_node = d3.select(element);
                        }
                    });
                /* #endregion "[#focus on target and source node and show tips.]"> */

                try {
                    if (src_node.size() <= 0 || dst_node.size() <= 0) return;
                } catch (e) {
                    return;
                }

                /* #region [#calculate tooltip position] */
                const offset = 5;
                let src_dir,
                    dst_dir,
                    src_offset = [0, 0],
                    dst_offset = [0, 0],
                    min_distance =
                        Math.max(options['node_width'], options['node_height']) ||
                        options['node_r_width'],
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
                /* #endregion "[#calculate tooltip position]"> */

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
                d3.contextMenu('close');
            },

            /**
             * 노드 툴팁을 감춘다
             */
            hideNodeTooltip: function () {
                const ctrl = this;
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
                const ctrl = this;
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
                            .classed("focus", false);

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
                const ctrl = this;
                let { svg } = ctrl;
                let width,
                    height,
                    viewBox = (svg || d3.select(SVG_SELECTOR)).attr("viewBox");

                try {
                    if (viewBox) {
                        let arr = viewBox.split(" ");
                        width = arr[2];
                        height = arr[3];
                    } else 
                    {
                        width = +svg.style("width").replace("px", "");
                        height = +svg.style("height").replace("px", "");
                    }
                } catch (error) {
                    width = 1920
                    height = 1080
                }
                // log(width + ' , ' + height);
                return { width: Number(width), height:Number(height) };
            },

            /**
             * 선택 시 노드 확대 가중치 설정값을 반환한다.
             * @param element
             * @returns {number}
             */
            getNodeWeightOption: function (element) {
                const ctrl = this;

                let selected = d3.select(element).classed(className.selected)
                    ? ctrl.options['node_selector_weight']
                    : 1;
                let dragging = d3.select(element).classed(className.dragging)
                    ? ctrl.options['node_drag_scale']
                    : 1;

                return Math.max(selected, dragging);
            },

            /**
             * 노드를 아이콘 사용 여부 설정값을 반환한다.
             * @returns {boolean}
             */
            isIconNodeOption: function () {
                const ctrl = this;
                return ctrl.options['node_type'] == "icon";
            },

            /**
             * 노드 너비 설정값을 반환한다.
             * @returns {*}
             */
            getNodeWidthOption: function () {
                const ctrl = this;
                if (ctrl.isIconNodeOption()) return ctrl.options['node_r_width'];
                else return ctrl.options['node_width'];
            },

            /**
             * 노드 높이 설정값을 반환한다.
             * @returns {*}
             */
            getNodeHeightOption: function () {
                const ctrl = this;
                if (ctrl.isIconNodeOption()) return ctrl.options['node_r_width'];
                else return ctrl.options['node_height'];
            },

            /**
             * 줌을 초기화 한다.
             * @param duration
             */
            resetZoom: function (duration = 0) {
                const ctrl = this;
                const { svg, zoom, options } = ctrl;
                const configZoom = options.zoom
                const scale = configZoom.initScale

                if (configZoom.initElement) {
                    ctrl.zoomIn({ ...configZoom.initElement, duration });
                } else if (configZoom.initPos) {
                    const { x, y } = configZoom.initPos
                    ctrl.zoomInByPos(x, y, (scale || 1), duration);
                } else if (scale > 0) {
                    ctrl.scale(scale, duration);
                }

                // 초기 버전 - 위 코드가 안정화 되면 삭제 해도 됨
                // svg.transition().duration(duration).call(
                //     zoom.transform,
                //     d3.zoomIdentity,
                //     d3.zoomTransform(svg.node()).invert([])
                // );
            },

            scale: function (scale = 1, duration = 0) {
                let { svg, zoom } = this;
                let { width, height } = this.getViewBoxSize();

                svg.transition().duration(duration).call(
                    zoom.transform,
                    d3.zoomIdentity.translate(width / 2, height / 2)
                        .scale(scale).translate(-width / 2, -height / 2)
                );
            },

            /**
             * generate 줌인
             * @param obj
             */
            zoomIn: function (obj) {
                if (!obj) { return }

                const ctrl = this;
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
            zoomInByPos: function (x, y, scale, duration = 0) {
                let { svg, zoom, zoom_scale } = this;
                let { width, height } = this.getViewBoxSize();

                if (typeof scale !== "number") {
                    scale = d3.zoomTransform(svg).k || zoom_scale || 1;
                }
                
                if (typeof x !== "number" || typeof y !== "number") {
                    // 정의 되지 않았을 때 컨트롤의 중심을 계산한다.
                    // 정확한 중심은 [1920 / 2, 1080 / 2] 이다
                    x = 1920 / 2 || d3.zoomTransform(svg).x
                    y = 1080 / 2 || d3.zoomTransform(svg).y
                }

                console.log(`zoomInByPos: `, { x, y, scale, width, height })

                // x = Math.floor(x);
                // y = Math.floor(y);
                this.zooming = true
                svg.transition().duration(duration).call(
                    zoom.transform,
                    d3.zoomIdentity
                        .translate(width / 2, height / 2)
                        .scale(scale)
                        .translate(-x, -y)
                )
                .on("end", () => {
                    this.zooming = false
                });
            },
            /**
             * 노드 값으로 줌인 한다.
             * @param node
             * @param scale
             * @param duration
             */
            zoomInByNode: function (node, scale, duration) {
                const ctrl = this;
                ctrl.zoomInByPos(node.x, node.y, scale, duration);
            },
            zoomInByRandomNode: function (scale = 5, duration = 1000) {
                const ctrl = this;
                // let array = ctrl.data.nodes.filter(n => !!n.id);
                // let index = Math.floor(Math.random() * array.length);
                // ctrl.zoomInByNode(array[index], scale, duration);
                ctrl.zoomInByPos(300, 300, 5, duration = 750) 
            },
            zoomInByRandomLink: function (scale = 5, duration = 1000) {
                const ctrl = this;
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
                const ctrl = this;
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
            //     const ctrl = this;
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

                    // oasis TEST
                    if (map.options.fileName.indexOf('oasis') == -1) {
                        return; 
                    }
                    let nodes = map.data.nodes
                    
                    nodes.forEach(v => {
                        if (e.target 
                            && e.d.id.indexOf('DU') != -1 
                            && e.d.id != v.id 
                            && v.parent_id != e.d.id) {
                            addBlurNode(v);
                        } else {
                            removeBlurNode(v);
                        }   
                    })
                   
                });

                function addBlurNode(d) {
                    let elNode = document.getElementById(`node_${d.id}`)
                    let elDesc = document.getElementById(`desc_${d.id}`)
                    elNode?.classList.add('blur');
                    elDesc?.classList.add('blur');
                }

                function removeBlurNode(d) {
                    let elNode = document.getElementById(`node_${d.id}`)
                    let elDesc = document.getElementById(`desc_${d.id}`)
                    elNode?.classList.remove('blur');
                    elDesc?.classList.remove('blur');
                }

                function startTour() {
                    options['select_node_test'] = true;
                    options['select_link_test'] = true;
                    setTimeout(() => {
                        options.updateNodeSelect();

                        setTimeout(() => {
                            options.updateLinkSelect();
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

                        log(`options.${property} = ${value}`)
                        this[property] = value;

                    }

                    let jobList = [
                        options.load,
                        changeOption.bind(options, 'save_map', !options['load_from_local]'], true), options.load,
                        changeOption.bind(options, 'select_node_test', !options['select_node_test'], true), options.updateNodeSelect,
                        changeOption.bind(options, 'select_link_test', !options['select_link_test'], true), options.updateLinkSelect,
                        options.onTestLinkAlarmOccur,
                        changeOption.bind(options, 'node_text_field', 'id', true), options.updateNodeDisplayField,
                        changeOption.bind(options, 'node_info_field', 'rx_tx', true), options.updateNodeInfoDisplayField,
                        changeOption.bind(options, 'node_type', 'icon', true), options.load,
                        changeOption.bind(options, 'node_r_show', 100, true), options.updateNodeR,
                        changeOption.bind(options, 'node_width', 100, true), options.updateNodeWidthHeight,
                        changeOption.bind(options, 'node_height', 100, true), options.updateNodeWidthHeight,
                        changeOption.bind(options, 'node_badge_size', 100, true), options.updateNodeBadgeSize,
                        changeOption.bind(options, 'link_text_field', 'port', true), options.updateLinkText,
                        changeOption.bind(options, 'link_width', 20, true), options.updateLink,
                        changeOption.bind(options, 'link_text_color', '#FF0000', true), options.updateLinkText,
                        changeOption.bind(options, 'link_text_size', 18, true), options.updateLinkText,
                        changeOption.bind(options, 'link_badge_size', 100, true), options.updateLink,
                        changeOption.bind(options, 'link_direction_auto', !options['link_direction_auto'], true), options.updateLink,
                        changeOption.bind(options, 'link_traffic_r', 50, true), options.updateLinkTrffic,
                        changeOption.bind(options, 'link_traffic_r_color', '#FF0000', true), options.updateLinkTrffic,
                        changeOption.bind(options, 'link_traffic_r_opacity', 0.2, true), options.updateLinkTrffic,
                        changeOption.bind(options, 'link_traffic_r_duration', 0.5, true), options.updateLinkTrfficAnimation,
                    ]

                    setTimeout(async () => {
                        for (let index = 0; index < jobList.length; index++) {
                            try {
                                const fn = jobList[index];
                                log(`AutoTest run function : ${fn.name}`)
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
                gui.add(options, 'redraw').name('새로고침');
                gui.add(options, 'keyword').name('keyword').onChange(options.updateKeyword);
                gui.add(options, 'fileName', [
                    'data_dev_1.json',  /* performance */
                    'data_dev_2.json',  /* arrange */
                    'data_gis.json',
                    'data_gis_2000x3000.json',
                    // 'data_dev.json',
                    // 'data_dev_line.json',
                    'data_dev_config.json',
                    'data_bcn_1.json',
                    'data_nia_ip_1.json',
                    'data_nia_ip_2.json',
                    'data_nia_ip_3.json',
                    'data_oasis_1.json',
                    'data_oasis_2.json',
                    'data_oasis_3.json',
                    'data_oasis_4.json',
                    'data_oasis_5.json',
                    'data_rca_1.json',
                    'data_rca_2.json',
                    'data_mba_1.json',
                    'data_mba_2.json',
                    'data_mba_3.json',
                    'data_mba_4.json',
                    'data_mba_5.json',
                    'data_mba_6.json',
                    'data_mba_7.json',
                    'data_nia_0.json',
                    'data_nia_1.json',
                    'data_nia_2.json',
                    'data_nia_3.json',
                    'data_nia_4.json',
                    'data_nia_5.json']).name('파일').onChange(e => { options['load_from_local]'] = false; options.load(e) }).listen();
                gui.add(options, 'save').name('맵저장')
                gui.add(options, 'exportToFile').name('JSON 파일로 저장')
                gui.add(options, 'load_from_local').name('저장된 맵 보기').onChange(e => options.load()).listen();
                gui.add(options, 'link_type', ['curved', 'arc', 'line', 'mixed']).name('링크 타입').onChange(options.updateAnimated).listen();
                gui.add(options, 'action_mode', [mouseMode.select, mouseMode.selectBlur, mouseMode.edit, mouseMode.none]).name('클릭 시 액션').listen();
                gui.add(options, 'resetZoom').name('ZOOM 초기화');
                gui.add(options, 'grid_mode').name('그리드').onChange(options.onArrange).listen();
                gui.add(options, 'grid_cell_width', 5, 50, 5).name('그리드단위').onChange(options.onArrange);
                gui.add(options, 'bg_grid').name('그리드보기').onChange(options.redraw).listen();
                gui.add(options, 'cloak_use').name('클로킹').onChange(options.redraw).listen();
                gui.open();

                var fLayout = gui.addFolder('Layout');
                fLayout.add(options, 'redraw').name('redraw')
                fLayout.add(options, 'loadForce').name('loadForce')
                fLayout.add(options, 'saveForce').name('saveForce')
                fLayout.add(options, 'random_node_count', 0, 200, 10).name('random_node').onChange(options.genneratRandomNode).listen()
                fLayout.add(options, 'random_link_count', 0, 200, 10).name('random_link').onChange(options.genneratRandomLink).listen()

                fLayout.add(options, 'layout_simulation_strength_x', 0, 1, 0.25).name('simulation_x').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_simulation_strength_y', 0, 1, 0.25).name('simulation_y').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_simulation_collide_radius', 0, 150, 10).name('simulation_radius').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_simulation_collide_strength', 0, 1, 0.01).name('simulation_strength').onChange(options.updateForce).listen()

                fLayout.add(options, 'layout_link_distance', 0, 200, 20).name('link_distance').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_link_strength', 0, 1, 0.01).name('link_strength').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_charge_strength', -200, 200, 20).name('charge_strength').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_charge_distance_max', 0, 500, 20).name('charge_distance_max').onChange(options.updateForce).listen()
                fLayout.add(options, 'layout_charge_distance_min', 0, 200, 20).name('charge_distance_min').onChange(options.updateForce).listen()
                fLayout.open();
                
                var fTest = gui.addFolder('테스트');
                gui.add(options, 'zoomInTest').name('ZOOM 테스트');
                fTest.add(options, 'onTestLinkAlarmOccur').name('링크장애 발생/해지')
                fTest.add(options, 'node_alarm_test').name('노드경보테스트').onChange(options.redrawNodeTest);
                gui.add(options, 'select_node_test').name('노드 순환').onChange(options.updateNodeSelect).listen();
                gui.add(options, 'select_link_test').name('링크 순환').onChange(options.updateLinkSelect).listen();
                fTest.add(options, 'autoTest').name('자동테스트')
                window.libDev && options.node_alarm_test && options.redrawNodeTest()
                fTest.open();
                
                let fNode = gui.addFolder('노드');
                fNode.add(options, 'node_text_field', ['mac', 'ip', 'device_name', 'id', 'instlocation']).name('레이블').onChange(options.updateNodeDisplayField).listen();
                fNode.add(options, 'node_info_field', ['rx_tx', 'span_gain', 'ntd']).name('레이블정보').onChange(options.updateNodeInfoDisplayField).listen();
                fNode.add(options, 'node_fix_size').name('노드사이즈고정').onChange(options.updateScale).listen(); //                
                fNode.add(options, 'node_type', ['image', 'icon']).name('이미지').onChange(e => options.load()).listen();
                fNode.add(options, 'node_r_show').name('범위표시').onChange(options.redraw).listen();
                fNode.add(options, 'node_r_width', 0, 150).name('범위').onChange(options.updateNodeR).listen();
                fNode.add(options, 'node_width', 10, 200).name('이미지너비').onChange(options.updateNodeWidthHeight).listen();
                fNode.add(options, 'node_height', 10, 200).name('이미지높이').onChange(options.updateNodeWidthHeight).listen();
                fNode.add(options, 'node_badge_show').name('뱃지 보기/감추기').onChange(options.redraw).listen();
                fNode.add(options, 'node_badge_size', 0, 100).name('뱃지크기').onChange(options.updateNodeBadgeSize).listen();
                fNode.add(options, 'node_error_count_show').name('카운트 보기/감추기').onChange(options.redraw).listen();
                fNode.addColor(options, 'node_r_out_color').name('stroke 색상').onChange(options.updateNodeR).listen();
                fNode.addColor(options, 'node_r_in_color').name('색상').onChange(options.updateNodeR).listen();
                fNode.addColor(options, '--svg-text-color').name('라벨 색상').onChange(options.updateNodeR).listen();
                fNode.add(options, 'node_r_opacity', 0.0, 1.0).name('투명도').onChange(options.updateNodeR).listen();
                // fNode.open();

                var fLink = gui.addFolder('링크');
                fLink.add(options, 'link_text_field', ['none', 'id', 'id-id', 'name', 'port', 'speed', 'status', 'index']).name('레이블').onChange(options.redraw).listen();
                fLink.add(options, 'link_width', 1, 10).name('너비').onChange(options.updateLink).listen();
                fLink.addColor(options, 'link_color').name('색상').onChange(options.updateLinkColor).listen();
                fLink.addColor(options, 'link_text_color').name('폰트색상').onChange(options.updateLinkText).listen();
                fLink.add(options, 'link_text_size', 6, 18).name('폰트사이즈').onChange(options.updateLinkText).listen();
                fLink.add(options, 'link_badge_size', 0, 100).name('뱃지크기').onChange(options.updateLink).listen();
                fLink.add(options, 'link_direction_auto').name('위치보정').onChange(options.updateLink).listen();
                // fLink.open();

                var fLinkTraffic = gui.addFolder('링크트래픽');
                fLinkTraffic.add(options, 'link_traffic_show').name('보기/감추기').onChange(options.redraw).listen();
                fLinkTraffic.add(options, 'link_traffic_arrow_show').name('방향 표시').onChange(options.updateLinkTrffic).listen();
                fLinkTraffic.add(options, 'link_traffic_r', 0, 20).name('범위').onChange(options.updateLinkTrffic).listen();
                fLinkTraffic.addColor(options, 'link_traffic_r_color').name('색상').onChange(options.updateLinkTrffic).listen();
                fLinkTraffic.addColor(options, 'link_traffic_arrow_color').name('화살표색상').onChange(options.updateLinkTrffic).listen();
                fLinkTraffic.add(options, 'link_traffic_r_opacity', 0.0, 1.0).name('투명도').onChange(options.updateLinkTrffic).listen();
                fLinkTraffic.add(options, 'link_traffic_r_duration', 0.5, 3).name('속도').onChange(options.updateLinkTrfficAnimation).listen();
                fLinkTraffic.open();

                // dat.GUI.toggleHide();
                // gui.close();

                const that = this;
                const showProperties = options['properties-use']
                const useGripper = options['properties-gripper-use']
                const selector = useGripper ? 'div.gripper' : 'div.properties'

                document.querySelector('div.gripper').style.display = showProperties && useGripper ? 'block' :'none';
                
                const gripper = d3.select(selector)
                    .call(d3.drag()
                        .on('start.interrupt', function () {
                            log('stop')
                            gripper.interrupt();
                            that.mouse = d3.mouse(this);
                        })
                        .on('start drag', function () {
                            // log('drag')
                            gripper.style('top', d3.event.y - that.mouse[1] + 'px')
                            gripper.style('left', d3.event.x - that.mouse[0] + 'px')

                            if(useGripper) {
                                let properties = d3.select('div.properties');
                                properties.style('top', d3.event.y - that.mouse[1] + parseFloat(gripper.style('height')) + 'px')
                                properties.style('left', d3.event.x - that.mouse[0] + 'px')
                            }
                        }));

            }, 100);
        };
    }
    )(typeof exports !== 'undefined' ? exports : this);

function degreesToRadians(degrees) {
    const pi = Math.PI;
    return degrees * (pi / 180);
}

// 두 점간의 기울기 각도 구하기
// window.getDegrees = function (x1, y1, x2, y2) {
//     return Math.atan((y2 - y1) / (x2 - x1)) * (180 / Math.PI)
// }

// 각도를 입력하면 좌표를 출력
// window.getContactPoint = function (degree) {
//     let radian = degree * (Math.PI / 180)
//     return { x: Number(Math.cos(radian).toFixed(5)), y: Math.sin(radian) }
// }

function assignRef(arrNodes, arrLinks) {
    const mapIdToNode = {}
    arrNodes.forEach(node => { mapIdToNode[node.id] = node; })
    log('mapIdToNode=', mapIdToNode)

    arrNodes.forEach(node => {
        let array = arrLinks.filter(link => (link.source_id !== link.target_id) && (link.source_id === node.id || link.target_id === node.id))
        node.$links = array
        node.$nodes = array.map(link => link.source_id === node.id ? mapIdToNode[link.target_id] : mapIdToNode[link.source_id])
    })
    arrLinks.forEach(link => {
        link.$nodea = mapIdToNode[link.source_id]
        link.$nodez = mapIdToNode[link.target_id]
    })
    return mapIdToNode;
}

function setNodePos (node, x, y) {
    log(`setNodePos=`, node.id, `x:${x}, y:${y}`)

    node.fx = (x !== null && x !== undefined) ? x : node.fx
    node.fy = (y !== null && y !== undefined) ? y : node.fy

    // node.fx = x || node.fx
    // node.fy = y || node.fy
}

// how to use
// classify(array, ['MSPP', 'PTN'], 'device_type')
// classify(array, ['MSPP', 'PTN'], function(d) {
//     // d = one of array
//     // this =  one of arrCategory
//     return d.device_type === this  
// })
function classify(array, arrCategory, fn) {
    const indexMap = {}
    if (typeof fn === 'string') {
        const property = fn
        fn = function (d) {
            return d[property] === String(this)
        }
    }
    arrCategory.forEach(c => {
        indexMap[c] = [].concat(array.filter(fn.bind(c)))
    })
    log('classify result=', indexMap)
    return indexMap
}

function latlngConverter() {
    // start pos 
    this.p0 = {
        scrX: 0,            // Minimum X position on screen
        scrY: 0,            // Minimum Y position on screen
        lat: null,          // Latitude
        lng: null           // Longitude
    }
    // end pos
    this.p1 = {
        scrX: 1920,         // Maximum X position on screen
        scrY: 1080,         // Maximum Y position on screen
        lat: null,          // Latitude
        lng: null           // Longitude
    }
    const radius = 6.371;     //Earth Radius in Km

    this.setPosition = function (p0 = {}, p1 = {}) {
        Object.assign(this.p0, p0);
        Object.assign(this.p1, p1);
    }

    this.latlngToGlobalXY = function (lat, lng) {
        let x = radius * lng * Math.cos((this.p0.lat + this.p1.lat) / 2);
        let y = radius * lat;
        return { x: x, y: y }
    }
    this.latlngToScreenXY = function (lat, lng) {
        let pos = this.latlngToGlobalXY(lat, lng);
        this.p0.pos = this.latlngToGlobalXY(this.p0.lat, this.p0.lng);
        this.p1.pos = this.latlngToGlobalXY(this.p1.lat, this.p1.lng);
        pos.perX = ((pos.x - this.p0.pos.x) / (this.p1.pos.x - this.p0.pos.x));
        pos.perY = ((pos.y - this.p0.pos.y) / (this.p1.pos.y - this.p0.pos.y));

        return {
            x: this.p0.scrX + (this.p1.scrX - this.p0.scrX) * pos.perX,
            y: this.p0.scrY + (this.p1.scrY - this.p0.scrY) * pos.perY
        }
    }
};

function latlngFormatter(value) {
    let temp = (value).split(':');
    return (parseInt(temp[0]) + (parseFloat(temp[1]) / 60) + (parseFloat(temp[2]) / 3600)).toFixed(5);
}

function setStyleProperty(key, value) {
    document.querySelector(':root').style.setProperty(key, value)
}

function debounce(fn, wait) {
	var timeout;
	return function() {
		var context = this;
        var args = arguments;
		
        clearTimeout(timeout);
        
		timeout = setTimeout(function() {
		    fn.apply(context, args);
		}, wait);
	};
};

