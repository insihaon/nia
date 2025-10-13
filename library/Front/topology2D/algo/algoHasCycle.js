(function (global) {

    'use strict';

    var vis = global.vis || (global.vis = {});

    // https://www.geeksforgeeks.org/detect-cycle-undirected-graph/
    global.AlgoHasCycle = (function (vertex) {

        if(!vertex) throw 'vertex 값이 없습니다.';

        var V = vertex;    // No. of vertices
        var adj = Array(V).fill(null).map(function () {return Array();});    // Pointer to an array containing adjacency lists
        var cyclePath;
        var log = global.AlgoHasCycle.log

        function normalization(v, w) {
            for (var i = 0; i < adj.length ; i++) {
                var temp = [];
                Array.prototype.push.apply(temp, Array.from(new Set(adj[i])));    // 중복제거
                adj[i] = temp;
            }
            return this
        }

        function addEdge(v, w) {
            // if (v != w) {
                adj[v].push(w); // Add w to v’s list.
                adj[w].push(v); // Add v to w’s list.
            // }
        }

        function getEdges(v) {
            if (v) {
                return adj[v];
            } else  {
                return adj;
            }
        }

        function getPath() {
            return cyclePath;
        }

        // A recursive function that uses visited[] and parent to detect
        // cycle in subgraph reachable from vertex v.
        function isCyclicUtil(v, visited, parent, path)
        {
            function trace(v, visited, parent, next, result) {
                // log('v=' + v + ', visited=', visited, ', parent=' + parent + ', next=' + next + ', result=' + result )
            }

            // Mark the current node as visited
            visited[v] = true;

            log('visited log : v=' + v + ', visited=', visited, ', parent=' + parent);

            // 이 Vertex 에 인접한 모든 Vertex 에 대해 반복
            for (var i = 0; i < adj[v].length; i++) {

                var next = adj[v][i];
                var nextPath = [];

                // 인접 Vertex 을 방문하지 않은 경우 인접 Vertex에 대해 반복하십시오.
                if (!visited[next]) {
                    if (isCyclicUtil(next, visited, v, nextPath)) {
                        path.push(next);
                        Array.prototype.push.apply(path, Array.from(nextPath));
                        trace(v, visited, parent, next, true);
                        return true;
                    }
                } else if (next != parent) {

                    path.push(next);
                    Array.prototype.push.apply(path, Array.from(nextPath));

                    // 인접 vertex 을 방문하고 현재 정점의 부모가 아닌 경우 사이클 포함됨
                    trace(v, visited, parent, next, true);
                    return true;
                }
            }

            trace(v, visited, parent, null, false);
            return false;
        }

        // 그래프에 사이클이 포함되어 있으면 true를, 그렇지 않으면 false를 반환합니다.
        function isCyclic()
        {
            // Mark all the vertices as not visited and not part of
            // recursion stack
            var visited = Array(V).fill(false);

            // Call the recursive helper function to detect cycle in
            // different DFS trees
            cyclePath = [];
            for (var u = 0; u < V; u++) {
                if (!visited[u]) // Don't recur for u if already visited
                    if (isCyclicUtil(u, visited, -1, cyclePath))
                        return true;
            }

            return false;
        }

        return {
            normalization: normalization,
            addEdge: addEdge,
            getEdges: getEdges,
            isCyclic: isCyclic,
            getPath: getPath
        };

    });
    global.AlgoHasCycle.log = () => { };
    // global.AlgoHasCycle.log = console.log.bind(null, '[AlgoHasCycle]');

    // <editor-fold desc="[AlgoHasCycle 모듈 테스트]">
    (function () {

        // if (true) return;
        var log = console.log;
        var getResult = (g) => g.isCyclic() ? "AlgoHasCycle contains cycle" : "AlgoHasCycle doesn't contains cycle"

        log("AlgoHasCycle 모듈 테스트");
        var g1 = new global.AlgoHasCycle(6);
        var obj = {}
        g1.addEdge(1, 0);
        g1.addEdge(0, 2);
        g1.addEdge(2, 1);
        g1.addEdge(0, 3);
        g1.addEdge(3, 4);
        g1.addEdge(4, 0);
        log("g1. ", getResult(g1), g1.getPath(), g1.normalization().getPath());

        var g2 = new global.AlgoHasCycle(3);
        g2.addEdge(0, 1);
        g2.addEdge(1, 2);
        log("g2. ", getResult(g2), g2.getPath());

        var g3 = new global.AlgoHasCycle(2);
        g3.addEdge(1, 0);
        g3.addEdge(0, 1);
        g3.normalization()
        log("g3. ", getResult(g3), g3.getPath());

        // Output :
        //  AlgoHasCycle contains cycle
        //  AlgoHasCycle doesn't contain cycle
        //  AlgoHasCycle doesn't contain cycle
    }
    )();
    // <!--</editor-fold desc="[AlgoHasCycle 모듈 테스트]">


})(typeof exports !== 'undefined' ? exports : this);