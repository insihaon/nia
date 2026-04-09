class AlgoUnidirectedGraph {
    constructor(direction = 'twoway') {
        this.AdjList = new Map()
        this.direction = direction   // 'oneway', 'twoway'
    }
    addVertex(v) {
        this.AdjList.set(v, [])
    }
    addEdge(v, w) {
        this.AdjList.get(v).push(w) // get the list for the vertex
        this.direction === 'twoway' && this.AdjList.get(w).push(v) // since it's unidirected add edge from w to v
    }
    printGraph() {
        const keys = this.AdjList.keys()
        for (let k of keys) {
            const values = this.AdjList.get(k)
            let conc = ''
            for (let j of values)
                conc += j + ' '
            console.log(k + ' -> ', conc)
        }
    }
    bfs(startingNode) {
        const visited = []

        const q = [] // Implement Queue
        visited[startingNode] = true
        q.unshift(startingNode)

        while (!(q.length === 0)) {
            const queueElement = q.shift()
            console.log(queueElement)
            const children = this.AdjList.get(queueElement)

            for (let child of children) {
                if (!visited[child]) {
                    visited[child] = true
                    q.unshift(child)
                }
            }

        }
    }
    dfs(startingNode) {
        const visited = []
        this.DSFUtil(startingNode, visited)
    }
    DSFUtil(vert, visited) {
        visited[vert] = true
        const neighbours = this.AdjList.get(vert)
        console.log(vert)
        for (let neighbour of neighbours) {
            if (!visited[neighbour]) {
                this.DSFUtil(neighbour, visited)
            }
        }
    }
}

const ugr = new AlgoUnidirectedGraph()
const vertices = ['A', 'B', 'C', 'D', 'E', 'F'];

vertices.forEach(v => {
    ugr.addVertex(v)
})

ugr.addEdge('A', 'B')
ugr.addEdge('A', 'D')
ugr.addEdge('A', 'E')
ugr.addEdge('B', 'C')
ugr.addEdge('D', 'E')
ugr.addEdge('E', 'C')
ugr.addEdge('C', 'F')

ugr.printGraph()

console.log('BFS')
ugr.bfs('A')

console.log('DFS')
ugr.dfs('A')