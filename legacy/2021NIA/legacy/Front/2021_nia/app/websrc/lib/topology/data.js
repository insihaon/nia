vis.topology_sample_data = {
    layers: [
        { id: 0, position: [0, 0, 0], name: 'Layer 0 (Fiber)', color : '#FFFFFF', opacity: 0.2 },
        { id: 1, position: [0, 300, 0], name: 'Layer 1 (ROADM)', color : '#FFFF99', opacity: 0.2 },
        { id: 2, position: [0, 600, 0], name: 'Layer 2 (PTN)', color : '#00B050', opacity: 0.2 },
        { id: 3, position: [0, 900, 0], name: 'Layer 2 (MSPP)', color : '#00B0F0', opacity: 0.2 },
    ],
    nodes: [
        { layer_id: 3, position: [-900, 0], id: 31, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '봉덕우체국', desc : 'DG봉덕2동우체국-MSPP-0101-01-01'},
        { layer_id: 3, position: [-600, 0], id: 32, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '봉덕', desc : '봉덕-MSPP-L-0604-01-01', alarm: { type: 1, blink: false } },
        { layer_id: 3, position: [-450, 300], id: 33, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '남대구', desc : '남대구-MSPP-L-0613-01-01', alarm: { type: 1, blink: false } },
        { layer_id: 3, position: [-300, 0], id:34, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '북대구', desc : '북대구-MSPP-L-0630-02-02', alarm: { count:1, type: 3, blink: false } },
        { layer_id: 3, position: [300, 0], id: 35, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '동대구', desc : '동대구-MSPP-L-0630-01-01-#3', alarm: { count: 1, type: 3, blink: false } },
        { layer_id: 3, position: [600, 0], id: 36, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '포항중', desc : '포항중-MSPP-L-0905-01-01-#5', alarm: { type: 1, blink: false } },
        { layer_id: 3, position: [450, 300], id: 37, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: 'KT서안동', desc : '서안동-MSPP-L-0505-01-01-#5', alarm: { type: 1, blink: false }},
        { layer_id: 3, position: [900, 0], id: 38, type: vis.nodeType.MSPPNode, color: 0x7F7F7F, name: '안동우체국', desc : 'DG안동우체국-MSPP-0101-01-01-FRT안동#5대향', alarm: { type: 1, blink: false } },
        { layer_id: 2, position: [-900, 0], id: 21, type: vis.nodeType.PTNNode, color: 0x7F7F7F, name: '대구교육대', desc : '대구교육대학교', alarm: { type: 1, blink: false } },
        { layer_id: 2, position: [-600, 0], id: 22, type: vis.nodeType.PTNNode, color: 0x7F7F7F, name: '북대구', desc : '북대구-PTS-1415-02-04', alarm: { count: 1, type: 3, blink: false }, },
        { layer_id: 2, position: [600, 0], id: 23, type: vis.nodeType.PTNNode, color: 0x7F7F7F, name: '서안동', desc : '서안동-PTS-0504-01-01', alarm: { count: 1, type: 3, blink: false } },
        { layer_id: 2, position: [900, 0], id: 24, type: vis.nodeType.PTNNode, color: 0x7F7F7F, name: '안동대', desc : '안동대학교', alarm: { count: 1, type: 3, blink: false }, },
        { layer_id: 1, position: [-300, 0], id: 11, type: vis.nodeType.ROADMNNode, color: 0x7F7F7F, name: '북대구', desc : '북대구-ROADM-1226-01-1링', alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 1, position: [0, -300], id: 12, type: vis.nodeType.ROADMNNode, color: 0x7F7F7F, name: '포항중', desc : '포항중-ROADM-0814-01-1링', alarm: { type: 3, blink: false }, },
        { layer_id: 1, position: [300, 0], id: 13, type: vis.nodeType.ROADMNNode, color: 0x7F7F7F, name: '동대구', desc : '동대구-ROADM-0337-01-1링', alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 0, position: [-300, 0], id: 1, type: vis.nodeType.FDFNode, color: 0x7F7F7F, name: '북대구FDF', desc : '북대구-FDF#5', alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 0, position: [300, 0], id: 2, type: vis.nodeType.FDFNode, color: 0x7F7F7F, name: '동대구FDF', desc : '동대구-FDF#5', alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 0, position: [-900, 300], id: 'ROADM8THW_T', type: vis.nodeType.ROADM8THW_T, color: 0x7F7F7F, name: 'ROADM8THW(단국)', desc : 'ROADM8THW_T' },
        { layer_id: 0, position: [-600, 300], id: 'ROADM8THW_R', type: vis.nodeType.ROADM8THW_R, color: 0x7F7F7F, name: 'ROADM8THW(중계)', desc : 'ROADM8THW_R' },
        { layer_id: 0, position: [-150, 300], id: 'ROADM8HHW_T', type: vis.nodeType.ROADM8HHW_T, color: 0x7F7F7F, name: 'ROADM8HHW(단국)', desc : 'ROADM8HHW_T' },
        { layer_id: 0, position: [150, 300], id: 'ROADM8HHW_R', type: vis.nodeType.ROADM8HHW_R, color: 0x7F7F7F, name: 'ROADM8HHW(중계)', desc : 'ROADM8HHW_R' },
        { layer_id: 0, position: [600, 300], id: 'ROADM8HCO_T', type: vis.nodeType.ROADM8HCO_T, color: 0x7F7F7F, name: 'ROADM8HCO(단국)', desc : 'ROADM8HCO_T' },
        { layer_id: 0, position: [900, 300], id: 'ROADM8HCO_R', type: vis.nodeType.ROADM8HCO_R, color: 0x7F7F7F, name: 'ROADM8HCO(중계)', desc : 'ROADM8HCO_R' },
    ],
    links: [
        { source_id: 31, target_id: 32, id: 'L30', name: '' },
        { source_id: 31, target_id: 32, id: 'L30', name: '' },
        { source_id: 32, target_id: 33, id: 'L31', name: '' },
        { source_id: 33, target_id: 32, id: 'L31', name: '' },
        { source_id: 33, target_id: 34, id: 'L32', name: '' },
        { source_id: 34, target_id: 33, id: 'L32', name: '' },
        { source_id: 34, target_id: 32, id: 'L33', name: '' },
        { source_id: 32, target_id: 34, id: 'L33', name: '' },
        { source_id: 35, target_id: 36, id: 'L34', name: '' },
        { source_id: 36, target_id: 35, id: 'L34', name: '' },
        { source_id: 36, target_id: 37, id: 'L35', name: '' },
        { source_id: 37, target_id: 36, id: 'L35', name: '' },
        { source_id: 35, target_id: 37, id: 'L36', name: '' },
        { source_id: 37, target_id: 35, id: 'L36', name: '' },
        { source_id: 36, target_id: 38, id: 'L37', name: '' },
        { source_id: 38, target_id: 36, id: 'L37', name: '' },
        { source_id: 21, target_id: 22, id: 'L20', name: '' },
        { source_id: 22, target_id: 21, id: 'L20', name: '' },
        { source_id: 23, target_id: 24, id: 'L21', name: '' },
        { source_id: 24, target_id: 23, id: 'L21', name: '' },
        { source_id: 11, target_id: 12, id: '186680', name: '북대구-서안동-FO-D001 OCA 186680' },
        { source_id: 12, target_id: 11, id: '3382', name: '서안동-진보R1-FO-D003 OCA 3382' },
        { source_id: 11, target_id: 12, id: '3383', name: '청송-진보R1-FO-D003 OCA 3383' },
        { source_id: 12, target_id: 11, id: '173579', name: '청송-포항중-FO-D002 OCA 173579' },
        { source_id: 12, target_id: 13, id: '64510', name: '동대구-동대중-FO-B144 OCA 64510' },
        { source_id: 13, target_id: 12, id: '167876', name: '영천-동대중-FO-D001 OCA 167876' },
        { source_id: 13, target_id: 12, id: '2036', name: '영천-포항중-FO-D001 OCA 2036' },
        { source_id: 11, target_id: 13, id: '65843', name: '동대구-북대구-FO-0001, OCA 65843' },
        { source_id: 13, target_id: 11, id: 'L12-2', name: '동대구-북대구-FO-0001' },
        { source_id: 34, target_id: 11, id: 'L1', name: '' },
        { source_id: 11, target_id: 34, id: 'L1', name: '' },
        { source_id: 11, target_id: 22, id: 'L2', name: '' },
        { source_id: 22, target_id: 11, id: 'L2', name: '' },
        { source_id: 35, target_id: 13, id: 'L3', name: '' },
        { source_id: 13, target_id: 35, id: 'L3', name: '' },
        { source_id: 13, target_id: 23, id: 'L4', name: '' },
        { source_id: 23, target_id: 13, id: 'L4', name: '' },
        { source_id: 11, target_id: 1, id: 'L5-1', name: '' },
        { source_id: 1, target_id: 11, id: 'L5-2', name: '' },
        { source_id: 13, target_id: 2, id: 'L6-1', name: '' },
        { source_id: 2, target_id: 13, id: 'L6-2', name: '' },
        { source_id: 1, target_id: 2, id: 'L7-1', name: '동대구-북대구-FO-0001, OCA 65843' },
        { source_id: 2, target_id: 1, id: 'L7-2', name: '동대구-북대구-FO-0001' },
    ]
};

/*
 모든 알람 발생
 links.forEach( function(v, i) {
 v.element.setAlarm(0x0000ff, 0xffff00, false)
 })

 모두 클리어
 links.forEach( function(v, i) {
 v.element.setAlarm(null, null, false)
 })

 장애링크
 links[14].element.setAlarm(0x7F7F7F, 0xffffff, true);

 노드 숨기기 , [알람카운트는 안 없어진다.]
 nodes[10].element.setVisibleLabel(false);
 nodes[10].element.setVisible(false);
 nodes[10].element.setVisibleCount(false);
 nodes[11].element.setVisibleLabel(false);
 nodes[11].element.setVisible(false);
 nodes[11].element.setVisibleCount(false);

 하이라이트 링크
 topology.highlightLink(1, 1, 0.1);
 topology.highlightClear();

 */

vis.topology_save_data = {
    "layers": [{"id": 0, "position": [660.6248434132157, 1290.7392622814439, 0]}, {
        "id": 1,
        "position": [-84.3150278599027, 87.99229638629532, -933.207774445229]
    }, {"id": 2, "position": [-935.7528283440486, -18.60894155102301, 131.21504451147075], visible: false}],
    "nodes": [{
        "layer_id": 0,
        "position": [0, 0],
        "id": 1,
        "type": "AlarmNode",
        "name": "node1",
        "alarm": {}
    }, {
        "layer_id": 1,
        "position": [100, 50],
        "id": 2,
        "type": "NormalNode",
        "name": "node2",
        "alarm": {}
    }, {
        "layer_id": 2,
        "position": [300, 100],
        "id": 3,
        "type": "AlarmNode",
        "name": "node3",
        "alarm": {}
    }, {
        "layer_id": 2,
        "position": [-100, 100],
        "id": 4,
        "type": "AlarmNode",
        "name": "node3-2",
        "color": 16711680,
        "alarm": {"count": 10, "type": 5, "blink": true}
    }],
    "links": [{"source_id": 1, "target_id": 2, "id": 1, "name": "link1"}, {
        "source_id": 2,
        "target_id": 1,
        "id": 2,
        "name": "link2"
    }, {"source_id": 2, "target_id": 3, "id": 3, "name": "link3"}, {
        "source_id": 3,
        "target_id": 4,
        "id": 4,
        "name": "link4"
    }, {"source_id": 4, "target_id": 3, "id": 5, "name": "link5"}]
};
