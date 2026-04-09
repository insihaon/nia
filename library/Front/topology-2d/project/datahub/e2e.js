// let sampleData = {
//   name: 'FK01C130',
//   children: [
//     {
//       name: 'FK01',
//       children: [
//         { name: 'C.Hehwa-CEA031' },
//         { name: 'C.Hehwa-CEA032' },
//         { name: 'C.Hehwa-CEA033' },
//         { name: 'C.Hehwa-CEA034' },
//         {
//           name: 'C.Hehwa-CEA035',
//           children: [
//             { name: 'C1' },
//             {
//               name: 'D',
//               children: [{ name: 'D1' }, { name: 'D2' }],
//             },
//           ],
//         },
//       ],
//     },
//     { name: 'FK02' },
//     {
//       name: 'FK03',
//       children: [{ name: 'C.Hehwa-CEA040' }, { name: 'C.Hehwa-CEA041' }, { name: 'C.Hehwa-CEA043' }],
//     },
//   ],
// }
// let upTreeData = sampleData
// let downTreeData = sampleData

const rootData = {
  name: 'ROOT',
}
let upData = []
let downData = []

function makeSample() {
  upData.splice(0)
  downData.splice(0)

  const minCount = 1
  const maxCount = 4

  for (let index = 0; index < rand(minCount, maxCount); index++) {
    upData.push({ name: `UP_${(index + 1).toString().padStart(2, '0')}`, hasChildren: rand(0, 3) !== 0 })
  }

  for (let index = 0; index < rand(minCount, maxCount); index++) {
    downData.push({ name: `DOWN_${(index + 1).toString().padStart(2, '0')}`, hasChildren: rand(0, 3) !== 0 })
  }
  
  return { rootData, upData, downData }
}

const duration = 750 
let width, height
class E2e {
  nodeIndex = 0
  newNodeIdx = 0
  fnNodeClick = null
  fnNodeDblClick = null
  lastNode = null
  
  constructor(selector = 'svg', initData = null, fnNodeClick = null, fnNodeDblClick = null) {
    this.SVG_SELECTOR = selector
    this.fnNodeClick = fnNodeClick
    this.fnNodeDblClick = fnNodeDblClick
    this.ResizeObserver = null

    const self = this.self = this
    this.initSvg()
    this.loadData(initData ?? makeSample())
    this.update()

    // var newData = () => setTimeout(() => {
    //   // 새로운 데이터 로드 테스트
    //   this.upRoot = null
    //   this.downRoot = null
      
    //   this.loadData(makeSample())
    //   this.update()
    //   // newData()
    // }, 5000);
    // newData()
  }

  loadData({rootData, upData, downData}) {
    let upTreeData = Object.assign({}, rootData, {
      children: upData.slice()
    })
    let downTreeData = Object.assign({}, rootData, {
      children: downData.slice()
    })

    const collapse = (d) => {
      if (d.children) {
        d._children = d.children
        d._children.forEach(collapse)
        d.children = null
      }
    }

    let downRoot = this.downRoot
    let upRoot  = this.upRoot
    if(!downRoot) {
      downRoot = d3.hierarchy(downTreeData, function (d) {
        return d.children
      })
      downRoot.x0 = height / 2
      downRoot.children && downRoot.children.forEach(collapse)
    }

    if(!upRoot) {
      upRoot = d3.hierarchy(upTreeData, function (d) {
        return d.children
      })
      upRoot.x0 = height / 2
      upRoot.children &&  upRoot.children.forEach(collapse)
    }

    Object.assign(this, {downRoot, upRoot, upTreeData, downTreeData})

    // let upNodes = treemap(upRoot).descendants()
    // let upLinks = upNodes.slice(1)

    return this
  }

  destroy() {
    const { SVG_SELECTOR, resizeObserver } = this
    if(resizeObserver) {
      resizeObserver.disconnect();
    }
  }

  isOldChrome() {
    const userAgent = window.navigator.userAgent;
    const chromeVersion = userAgent.match(/Chrome\/(\d+\.\d+\.\d+\.\d+)/).at(1);
    const isOldChrome = chromeVersion < '110'
    return isOldChrome
  }

  onCanvasSizeChanged(delay = 0) {
    if(this.isOldChrome()) {
      const THIS = this
      setTimeout(() => {
        THIS.update()        
      }, delay);
    }
  }

  initSvg() {
    const { SVG_SELECTOR, self } = this

    const mySvg = document.querySelector(SVG_SELECTOR)
    if(!this.isOldChrome()) {
      this.resizeObserver = new ResizeObserver(entries => {
        self.update()
      })
      this.resizeObserver.observe(mySvg)
    }

    const initAttr = (extend = {}) => {
      const NODE_ATTR = Object.assign({ width: 20, height: 20, centerX: undefined, centerY: undefined }, extend)
      NODE_ATTR.centerX = NODE_ATTR.width / 2
      NODE_ATTR.centerY = NODE_ATTR.height / 2
      return NODE_ATTR
    }

    let margin = { top: 20, right: 90, bottom: 30, left: 90 }
    let NODE_ATTR = initAttr()
    let ROOT_ATTR = initAttr({ width: 30, height: 30 })

    let svg = d3
    .select(SVG_SELECTOR)
    .append('g')
    .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')

    let node_tip = d3.tip()
      .attr("class", "tooltip")
      .offset([-5, 0])
      .html(function (d) {
          let tooltip = "";
          tooltip += d.data?.name ? `<strong>${d.data?.name}</strong>` : "";
          return tooltip;
      });
    svg.call(node_tip);

    Object.assign(this, {svg, node_tip, NODE_ATTR, ROOT_ATTR, margin})

    this.calcSvgSize()
  }

  calcSvgSize() {
    const { SVG_SELECTOR, margin } = this
    const svgEl = document.querySelector(SVG_SELECTOR)
    if(!svgEl) 
      return
    
    width = svgEl.clientWidth - margin.left - margin.right
    height = svgEl.clientHeight - margin.top - margin.bottom
  }

  update(source = this.downRoot) {
    const {self, svg, ROOT_ATTR, NODE_ATTR, upRoot, downRoot} = this

    this.calcSvgSize()

    let treemap = d3.tree().size([height, width])
    let up = treemap(upRoot).descendants().map(x => Object.assign(x, {direction : 'up'}))
    let down = treemap(downRoot).descendants().map(x => Object.assign(x, {direction : 'down'}))

    // UP 트리에서 ROOT를 제거하고, DOWN 트리의 ROOT와 연결한다
    let rootNode = down[0]
    up.forEach(function (d) {
      if(d.depth === 1) {
        d.parent = rootNode
      }
    })
    let nodes = down.concat(up.slice(1))
    let links = nodes.slice(1)
    Object.assign(this, {nodes, links})

    // d.y 값 설정을 위해 그려질 최대 깊이를 계산한다
    let upMaxDepth = up.reduce((a, c, i) => { return Math.max(a, c.depth) }, 0)
    let downMaxDepth = down.reduce((a, c, i) => { return Math.max(a, c.depth) }, 0)
    
    let centerX = width / 2
    nodes.forEach(function (d) {
      // 절대값 으로 계산
      // d.y = centerX + d.depth * 180 * (d.direction == 'down' ? 1 : -1);

      // max-depth 로  계산
      if (d.depth === 0) {
        d.y = centerX 
      } else if (d.direction == 'down') {
        d.y = centerX + (centerX / downMaxDepth) * d.depth
      } else {
        d.y = centerX - (centerX / upMaxDepth) * d.depth
      }
    })
    let node = svg.selectAll('g.node').data(nodes, function (d) {
      return d.id || (d.id = ++self.nodeIndex)
    })
    let hasChild =  d => d.children || d._children || d.data?.hasChildren
    let nodeEnter = node
      .enter()
      .append('g')
      .attr('class', 'node')
      .attr('transform', function (d) {
        return 'translate(' + source.y + ',' + source.x0 + ')'
      })
      
    nodeEnter
      .attr('class', 'node')
      .attr('r', 1e-6)
      .style('fill', function (d) {
        return d.parent ? 'rgb(39, 43, 77)' : '#ddd'
      })
    nodeEnter
      .append('rect')
      .attr('rx', function (d) {
        if (d.parent) return hasChild(d) ? '50%' : 0
        return ROOT_ATTR.width
      })
      .attr('ry', function (d) {
        if (d.parent) return hasChild(d) ? '50%' : 0
        return ROOT_ATTR.height
      })
      .attr('stroke-width', function (d) {
        return d.parent ? 1 : 0
      })
      .attr('stroke', function (d) {
        return hasChild(d) ? 'rgb(3, 192, 220)' : 'rgb(38, 222, 176)'
      })
      .attr('stroke-dasharray', function (d) {
        return hasChild(d) ? '0' : '2.2'
      })
      .attr('stroke-opacity', function (d) {
        return hasChild(d) ? '1' : '0.6'
      })
      .attr('x', 0)
      .attr('y', function (d) {
        return -1 * (d.parent ? NODE_ATTR.centerY : ROOT_ATTR.centerY)
      })
      .attr('width', function (d) {
        return d.parent ? NODE_ATTR.width : ROOT_ATTR.width
      })
      .attr('height', function (d) {
        return d.parent ? NODE_ATTR.height : ROOT_ATTR.height
      })
      .on('click', click)
      .on('dblclick', dblclick)
      .on('mouseover', d => this.node_tip.show(d))
      .on('mouseout', d => this.node_tip.hide(d))
    
    nodeEnter
      .append('text')
      .style('fill', function (d) {
        if (d.parent) {
          return hasChild(d) ? '#aaa' : 'rgb(38, 222, 176)'
        }
        return '#aaa'
      })
      .attr('dy', '.35em')
      .attr('x', function (d) {
        return d.parent ? NODE_ATTR.centerX : ROOT_ATTR.centerX
      })
      .attr('y', function (d) {
        return (d.parent ? NODE_ATTR.centerY : ROOT_ATTR.centerY) + 5
      })
      .attr('text-anchor', function (d) {
        return 'middle'
      })
      .attr('dominant-baseline', function (d) {
        return 'middle'
      })
      .text(function (d) {
        return d.data?.name ?? ''
      })

    let nodeUpdate = nodeEnter.merge(node)

    nodeUpdate
      .transition()
      .duration(duration)
      .style('fill', function (d) {
        if(d.last) return 'rgba(0, 128, 0, .5)'
        return d.parent ? 'rgb(39, 43, 77)' : '#ddd'
      })
      .attr('transform', function (d) {
        return 'translate(' + d.y + ',' + d.x + ')'
      })
    let nodeExit = node
      .exit()
      .transition()
      .duration(duration)
      .attr('transform', function (d) {
        return 'translate(' + source.y + ',' + source.x + ')'
      })
      .remove()
    nodeExit.select('rect').style('opacity', 1e-6)
    nodeExit.select('rect').attr('stroke-opacity', 1e-6)
    nodeExit.select('text').style('fill-opacity', 1e-6)
    let link = svg.selectAll('path.link').data(links, function (d) {
      return d.id
    })
    let linkEnter = link
      .enter()
      .insert('path', 'g')
      .attr('class', 'link')
      .attr('d', function (d) {
        let o = { x: source.x0, y: source.y }
        return diagonal(o, o)
      })

    let linkUpdate = linkEnter.merge(link)
    linkUpdate
      .classed('bold', function (d) {
        if (d?.parent?.last) return true
        if (d.last) return true
        return false
      })
      .transition()
      .duration(duration)
      .attr('d', function (d) {
        // console code
        // links.map(d => `(${d.y}, ${d.x})->(${d.parent.y}, ${d.parent.x})`)
        return diagonal(d, d.parent)
      })
      
    let linkExit = link
      .exit()
      .transition()
      .duration(duration)
      .attr('d', function (d) {
        let o = { x: source.x, y: source.y }
        return diagonal(o, o)
      })
      .remove()
    nodes.forEach(function (d) {
      d.x0 = d.x
      d.y0 = d.y
    })
    function diagonal(s, d) {
      const path = `M ${s.y} ${s.x}
              C ${(s.y + d.y) / 2} ${s.x},
                ${(s.y + d.y) / 2} ${d.x},
                ${d.y} ${d.x}`

      return path
    }
    async function requestChild(d) {
      const wait = (ms = 0) => new Promise((resolve) => setTimeout(resolve, ms))
      const fnNodeClick = self.fnNodeClick
      let children = []
      if(fnNodeClick) {
        children = await fnNodeClick(d)
        children = children.map((c) => {
          return { parent: d, depth: d.depth + 1, data: c }
        })
      }
      else {
        await wait(300)
        const count = 1 + Math.floor(5 * Math.random())
        for (let index = 0; index < count; index++) {
          children.push({ parent: d, depth: d.depth + 1, data: { name: `C.Hehwa-CEA${(++self.newNodeIdx).toString().padStart(3, '0')}`, hasChildren: true } })
        }
      }

      d.data.hasChildren = null
      d._children = children

      if(children.length === 0 ) {
        d._children = null
        d.children = null
      }
    }

    let clickTimer
    function click(d) {
      const ctrlKey = d3.event.ctrlKey
      
      const toggle =  (d) => {
        if (d.children) {
          d._children = d.children
          d.children = null
        } else {
          d.children = d._children
          d._children = null
        }
      }

      if (!clickTimer) {
        clickTimer = setTimeout(async function () {
          if(!ctrlKey) {
            if(d.data?.hasChildren) {
              await requestChild(d)
            }
            toggle(d)
    
            // ROOT 노드 일 경우 DOWN 만 토글되므로 UP 도 토글한다
            if (d === rootNode) toggle(up[0])
          }
          
          if(self.lastNode && self.lastNode.last) {
            self.lastNode.last = false
          }
          d.last = true
          window.node = self.lastNode = d

          self.update(d)

          clickTimer = null
        }, 200) // 클릭과 더블클릭 간격 설정 (예: 200밀리초)
      }
    }

    function dblclick(d) {
      clearTimeout(clickTimer); // 클릭 이벤트 타이머 중지
      clickTimer = null

      console.log("더블클릭", {...d});
      const fnNodeDblClick = self.fnNodeDblClick
      if (fnNodeDblClick) {
        fnNodeDblClick(d)
      }

      // 클릭 이벤트의 전파 중단
      // d3.event.stopPropagation(); 
      // d3.event.preventDefault();
    }
  }
}

function rand(min, max) {
  return min + Math.floor(max * Math.random())
}

function debounce(fn, wait) {
  var timeout
  return function () {
    var context = this
    var args = arguments

    clearTimeout(timeout)

    timeout = setTimeout(function () {
      fn.apply(context, args)
    }, wait)
  }
}

if(window.IS_LIB) {
  // [TEST] many instance 
  window.addEventListener('load', function() {
    const svgElements = document.querySelectorAll('.svg');
    const svgCount = svgElements.length;

    for (let index = 1; index <= svgCount; index++) {
      eval(`window.e${index} = new E2e('.container .svg:nth-child(${index})')`)
    }
  });
}

