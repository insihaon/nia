vis.topology_sample_data = {
    layers: [
        { id: 0, position: [0, 0, 0], name: 'Level 0 (Fiber)' },
        { id: 1, position: [0, 300, 0], name: 'Level 1 (ROADM)' },
        { id: 2, position: [0, 600, 0], name: 'Level 2 (PTN)' },
        { id: 3, position: [0, 900, 0], name: 'Level 2 (MSPP)' }
    ],
    nodes: [
        { layer_id: 3, position: [-900, 0], id: 31, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'RT', },
        { layer_id: 3, position: [-600, 0], id: 32, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', },
        { layer_id: 3, position: [-300, 0], id: 33, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', alarm: { count: 10, type: 5, blink: true } },
        { layer_id: 3, position: [300, 0], id: 34, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', alarm: { count: 10, type: 5, blink: true } },
        { layer_id: 3, position: [600, 0], id: 35, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', },
        { layer_id: 3, position: [900, 0], id: 36, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'RT', },
        { layer_id: 2, position: [-150, 0], id: 21, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', alarm: { count: 10, type: 5, blink: true } },
        { layer_id: 2, position: [150, 0], id: 22, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, name: 'COT', alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 1, position: [-150, 0], id: 11, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 1, position: [150, 0], id: 12, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 0, position: [-150, 0], id: 1, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 0, position: [150, 0], id: 2, type: vis.nodeType.AlarmNode, color: 0x7F7F7F, alarm: { count: 10, type: 5, blink: true }, },
        { layer_id: 2, position: [-900, 0], id: 0, type: vis.nodeType.AlarmNode, color: 0xFF0000, name: '더미', alarm: { count: 10, type: 5, blink: true }, },
    ],
    links: [
        { source_id: 31, target_id: 32, id: 0 },
        { source_id: 32, target_id: 33, id: 1 },
        { source_id: 33, target_id: 34, id: 2 },
        { source_id: 34, target_id: 35, id: 3 },
        { source_id: 35, target_id: 36, id: 4 },
        { source_id: 36, target_id: 35, id: 5 },
        { source_id: 35, target_id: 34, id: 6 },
        { source_id: 34, target_id: 33, id: 7 },
        { source_id: 33, target_id: 32, id: 8 },
        { source_id: 32, target_id: 31, id: 9 },
        { source_id: 21, target_id: 22, id: 10 },
        { source_id: 22, target_id: 21, id: 11 },
        { source_id: 11, target_id: 12, id: 12 },
        { source_id: 12, target_id: 11, id: 13 },
        { source_id: 1, target_id: 2, id: 14 },
        { source_id: 2, target_id: 1, id: 15 },
        { source_id: 33, target_id: 11, id: 16 },
        { source_id: 12, target_id: 34, id: 17 },
        { source_id: 11, target_id: 1, id: 18 },
        { source_id: 2, target_id: 12, id: 19 },
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
