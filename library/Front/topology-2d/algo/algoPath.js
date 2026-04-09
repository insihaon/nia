
function find_exits(g, id, func) {
    var visited = {};
    var list = {};
    var v = g[id];
    var q = [[id, [id]]];

    visited[id] = true;
    if (v.exit) list[id] = [id];

    while (q.length) {
        var x = q.shift();
        var path = x[1];

        id = x[0];                
        v = g[id];

        for (var i = 0; i < v.edge.length; i++) {
            var next =  v.edge[i];
            if (next in visited) continue;

            visited[next] = true;
            if (g[next].exit) {
                list[next] = path.concat([next]);
            }
            q.push([next, path.concat([next])]);
        }
        path.pop();
    }

    return list;
}

// Combine two exit lists, choose the shorter path if there
// is an exit in both lists
function union(a, b, isShortest = true) {
    var u = {};

    const compare = (a, b) => {
        if (isShortest) {
            return a[x].length < b[x].length
        }
        return a[x].length > b[x].length
    }

    for (var x in a) u[x] = a[x];
    for (var x in b) {
        if (x in a && compare(a, b)) continue;
        u[x] = b[x];
    }
    return u;
}

function findPath(graph, isShortest = true) {

    var cw = {
        A: {
            entry: true,
            edge: ['B']
        },
        B: {
            entry: true,
            exit: true,
            edge: ['C']
        },
        C: {
            entry: true,
            edge: ['D']
        },
        D: {
            edge: ['E', 'F']
        },
        E: {
            exit: true
        },
        F: {
            entry: true,
            exit: true,
            edge: ['G']
        },
        G: {
            exit: true,
            edge: ['H']
        },
        H: {
            edge: ['A']
        }
    };
    
    var ccw = {
        A: {
            edge: ['H']
        },
        B: {
            entry: true,
            edge: ['A']
        },
        C: {
            exit: true,
            edge: ['B']
        },
        D: {
            entry: true,
            edge: ['C']
        },
        E: {
            entry: true,
            edge: ['D']
        },
        F: {
            entry: true,
            exit: true,
            edge: ['D']
        },
        G: {
            entry: true,
            edge: ['F']
        },
        H: {
            edge: ['G']
        }
    };
    
    graph = graph || [cw, ccw]
    
    // 여러 하위 그래프의 네트워크에 대한 관리 개체
    var network = {
        graphs: graph,
        traverse: function(func) {
            for (var i = 0; i < this.graphs.length; i++) {
                func(this.graphs[i]);
            }
        }
    }
    
    // 모든 vertex에 edge, exit, entry가 있는지 확인
    network.traverse(function(g) {
        for (var id in g) {
            var v = g[id];
    
            if (!("edge" in v)) v.edge = [];
            if (!("entry" in v)) v.entry = false;
            if (!("exit" in v)) v.exit = false;
        }
    });
    
    // 실제 길 찾기
    
    var start = 'B';
    var exits = {};
    
    network.traverse(function(g) {
        if (g[start].entry) {
            exits = union(exits, find_exits(g, start), isShortest);
        }
    });
    
    console.log(exits);
    return exits
}

// for test
findPath(undefined, false)