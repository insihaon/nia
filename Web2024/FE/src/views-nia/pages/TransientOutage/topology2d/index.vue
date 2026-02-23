<template>
  <div :class="{ [name]: true }">
    <!-- <div id="componentWapperId" class="componentWapper" style="overflow:hidden;position: relative"> -->
    <div style="position: relative;" class="w-100 h-100">
      <div class="background" />
      <svg id="topology_container" viewBox="0 0 1920 1080" style="border: 1px solid gray;" class="jctx-host jctx-id-foo">
        <defs>
          <marker id="arrow" viewBox="0 0 10 10" orient="auto" markerWidth="3" markerHeight="3" refX="23" refY="5">
            <path fill="red" d="M 0 0 L 10 5 L 0 10 z" />
          </marker>
        </defs>
      </svg>
      <div class="gripper" />
      <div class="properties flex-column" style="display: none;" />
    </div>
    <!-- </div> -->
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import { apiRcaRequest } from '@/api/nia'
import moment from 'moment'

const routeName = 'MbaTopology2D'

export default {
  name: routeName,
  components: {},
  directives: {},
  extends: Base,
  props: {
    ticket: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      query: {},
      result: {
        table: []
      },
      map: null,
      // map_v1
      // mapData: {
      //   config: {
      //     converter: 'mba',
      //     options: {
      //       node: { /* 'fix_size': true, */ 'r': 60, 'width': 60, 'height': 60, 'badge_size': 40, 'opacity': 0, 'dragable': true },
      //       link: { 'displayField': 'name' },
      //       link_traffic: { 'show_arrow': false, 'r': 5, 'color': '#fff' },
      //       link_type: 'arc'
      //     },
      //     zoom: { initScale: 1.5, sscale: 2 },
      //   },
      //   trunk_name: '',
      //   data: { nodes: [], links: [] }
      // }
      // map_v2
      mapData: {
        config: {
          converter: 'mba',
          options: {
            node_r_width: 0,
            node_width: 60,
            node_height: 60,
            node_text_size: 20,
            node_badge_size: 40,
            // node_fix_size: true,
            link_text_field: 'name',
            link: { displayField: 'name', autoReverse: false },
            link_traffic_arrow_show: false,
            link_traffic_r: 5,
            link_traffic_r_color: '#fff',
            link_width: 4,
            link_type: 'arc'
          },
          zoom: { initScale: 1.5, sscale: 2 },
          trunk_name: ''
        },
        data: { nodes: [], links: [] }
      }
    }
  },
  computed: {
    nodes() {
      return this.map.getVisualNodeAll()
    },
    links() {
      return this.map.getVisualLinkAll()
    }
  },
  watch: {
    ticket(newValue, oldValue) {
      if (newValue?.ticket_id !== oldValue?.ticket_id) {
        this.loadTopology2d()
      }
    }
  },
  mounted() {
    const ctrl = this
    ctrl.initMap()
  },
  created() {
    this.addLink([
      // './extlib/map2d/style/jsutcontext.css' // v1
      './extlib/map2d/lib/d3/d3-context-menu.css' // v2
    ])
    const async = false
    const js = this.debug ? [
      './extlib/map2d/lib/d3/d3.v4.min.js',
      './extlib/map2d/lib/d3/d3-tip.min.js',
      './extlib/map2d/lib/d3/dat.gui.min.js',
      './extlib/map2d/lib/d3/d3-context-menu.js',
      './extlib/map2d/lib/global.js'] : []

    this.addScript([
      ...js,
      './extlib/map2d/lib/map2d.min.js'
    ], async)
  },
  methods: {
    onActiveView() {},
    initMap() {
      const THIS = this

      setTimeout(() => {
      // topology-svg index.js의 window.onload에 해당

        if (!window.Map2d) {
          THIS.initMap()
          return
        }
        const map = this.map = THIS.map = new window.Map2d()
        this.map.initialize(THIS.elId)
        // this.map.load()

        map.addEventListener(window.Map2d.eventType.selectChanged, e => {
          console.log(e?.d)
          this.$emit('selectedTopologyItem', e)
        })
        THIS.load()
      }, 100)
    },
    handleClearAll() {
      this.result.table = []
    },
    load() {
      this.map.load(this.mapData)
    },
    async handleSearch() {
      try {
        /* 임시주석처리 */
        // this.openLoading()
        // const res = await apiGetObjectFromJsonFile('data_mba_1.json')
        // const { data } = res
        // this.result.table = data
        // this.map.load(data)
      } catch (error) {
        console.log(error)
      } finally {
        this.closeLoading()
      }
    },
    async loadTopology2d() {
      const { ticket } = this
      if (!ticket?.ticket_id) return
      // let topologyData = {}
      let nodes = []
      let links = []
      const target = { vue: '#topology_container' }
      try {
        this.openLoading(target)
        const params = {
          TICKET_TYPE: 'RT',
          TICKET_ID: ticket.ticket_id,
          ISMBA: true,
          FAULT_TIME: moment(ticket.fault_time).format('YYYY-MM-DD HH:mm:ss'),
          DIRECTION: ticket.direction || '',
          MAX_DAYS: 31
        }
        await apiRcaRequest('SELECT_TICKET_ROOT_ALARM_LIST', params).then(async(result) => {
          const result2 = await apiRcaRequest('SELECT_MBA_TOPOLOGY_LIST', { TRUNK_NAME: ticket.trunk_name || '' })
          const nodeList = result2?.data || []
          const linkList = result?.data || []

          nodes = nodeList
          links = nodeList?.length > 0 ? linkList : []
        })
        this.map.load(Object.assign({}, this.mapData, { data: { nodes: nodes, links: links } }))
      } catch (error) {
        this.error(error)
      } finally {
        this.$nextTick(() => {
          this.$emit('loadList', { nodes: nodes, links: links })
        })
        this.closeLoading()
      }
    },
    async autoTest() {
      Base.methods.autoTest.apply(this, arguments)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

<style lang="scss">
  :root {
      --focus-color: rgba(0, 0, 0, 0.7);
  }
  // 상세정보 테이블 깜빡임
  @keyframes blinking {
      0% {background-color: rgba(255, 0, 0, 0.7);}
      100% {background-color: transparent;}
  }
  // 링크/노드 선택 시 dash 움직임
  @keyframes stroke {
      to {
          stroke-dashoffset: 0;
      }
  }

  /*<editor-fold desc="[tooltip style]">*/
  .tooltip {
      line-height: 1;
      font-weight: bold;
      padding: 12px;
      background: rgba(0, 0, 0, 0.8);
      color: #fff;
      border-radius: 10px;
      z-index: 100;
  }

  .tooltip strong.title {
      display: inline-block;
      width: 75px;
  }
  /*</editor-fold desc="[tooltip style]">*/

.MbaTopology2D {
  border-radius: 17px 17px 0 0;
  .background {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      /* background: url("../../images/background.jpg") no-repeat center center fixed;  */
      border-radius: 17px 17px 0 0;
      background-color: #032c37;
      background-size: contain;
      // filter: blur(4px);
      // -webkit-filter: blur(4px);
  }
  text {
      fill: white;
      /*fill: #000;*/
      font-family: 'Open Sans';
  }

  /*<editor-fold desc="[상세정보 테이블]">*/
  th, td {
      border: 1px dotted grey;
      padding: 0.5em;
  }
  .flex-column {
      display: flex;
      flex-direction: column;
  }
  .flex-row {
      display: flex;
      flex-direction: row;
  }
  /* .flex-item {
  } */
  .controls {
      position: absolute;
      top: 1rem;
      left: 1rem;
      /*transform: translateY(50%);*/
      display: flex;
      flex-direction: column;
      visibility: hidden;
  }
  .controls input {
      width: 12rem;
      margin-bottom: 1rem;
  }
  .controls label {
      display: flex;
      justify-content: space-between;
      color: white;
      // font-size: .85rem;
      font-family: system-ui, -apple-system, sans-serif;
  }
  .gripper {
      font-size: .7rem;
      background: black;
      width: 1em;
      height: 1em;
      position: absolute;
      top: 0em;
      left: 1em;
  }
  .properties {
      font-size: .7rem;
      font-family: "NanumGothic", "맑은고딕", "Fira Mono", Monaco, "Andale Mono", "Lucida Console", "Bitstream Vera Sans Mono", "Courier New", Courier, monospace;
      position: absolute;
      top: 1em;
      left: 1em;
  }
  .properties table {
      /*width: 100%;*/
      border: 1px dotted grey;
      border-collapse: collapse;
      table-layout: fixed;
  }
  /* .properties table tr > td{
      width: 15em;
  } */
  .properties.flex-column .flex-item:not(:first-of-type) {
      margin-top: 1.5em;
  }
  .properties table tr > *:first-child {
      text-align: right;
      width: 8em;
  }
  .properties.node-info table tr > *:nth-child(3) {
      display: none;
  }
  .properties.node-info .flex-item:nth-child(2) {
      display: none;
  }
  .properties.link-info .flex-item:nth-child(3) {
      display: none;
  }
  div.title-wapper div.left {float: left;overflow: hidden;text-overflow: ellipsis;max-width: calc(100% - 90px);display: block;}
  div.title-wapper div.right {float: right;display: block;}
  div.title-wapper:after {content: "";clear: both;display: block}
  div.template.TSS-160 {width: 300px;}
  div.template.TSS-160 div.table-wapper {overflow: hidden;}
  div.template.TSS-160 div.table-wapper > table {float: left;width: 170px !important;}
  div.template.TSS-160 div.table-wapper > div {float: left;}
  div.template.TSS-160 div.table-wapper > div {padding: 10px 10px;}
  div.template.TSS-160 div.table-wapper table {table-layout:initial !important;}
  div.template.TSS-160 div.table-wapper tr td {width: 30px;}
  .animation-blink {animation: blinking 1s infinite;}
  /*</editor-fold desc="[상세정보 테이블]">*/

  svg#topology_container {
      width: 100%;
      height: 100%;
      position: relative;
      border-radius: 17px 17px 0 0;
      background-color: transparent !important;
  }
  .ftstcall {
      font-family: "fontastic-all" !important;
  }
  .invisible {
      visibility: hidden;
  }

  /*<editor-fold desc="[link style]">*/
  .links .link_container .link_path {
      stroke: #999;
      fill: none;
      // stroke-width: 5px;
  }
  .links .link_container .link_selector {
      stroke: rgba(170, 160, 100, 0);
      fill: none;
      // stroke-width: 7px;
  }
  .links .link_container.focus .link_path {
      stroke: var(--focus-color) !important;
  }
  /*</editor-fold desc="[link style]">*/

  /*<editor-fold desc="[link-label style]">*/
  /*</editor-fold desc="[link-label style]">*/

  /*<editor-fold desc="[node style]">*/
  .nodes {
      cursor: pointer;
  }
  .nodes .node_container text {
      text-anchor: middle;
      font-size: 25px;
      fill: #444444;
  }
  .nodes .node_container circle {
      // stroke-width: 5px;
  }
  .links .link_container.selected .link_path,
  .nodes .node_container.selected circle
  {
      fill: none;
      stroke: #000;
      /*vector-effect: non-scaling-stroke;*/
      stroke-dasharray: 4px;
      animation: stroke 0.2s linear infinite;
      shape-rendering: geometricPrecision;
      stroke-dashoffset: 8px;
  }
  .nodes .node_container.stat_normal circle {
      stroke: #1ca2db;
      fill: rgba(53, 186, 246, 0.67);
  }
  .nodes .node_container.stat_abnormal circle {
      stroke: #E5F1B6;
      fill: rgba(229, 241, 182, 0.67);
  }
  .nodes .node_container.stat_error circle {
      stroke: #ff4200;
      fill: rgba(214, 100, 62, 0.67);
  }
  .nodes .node_container.stat_down circle {
      stroke: #adabab;
      fill: rgba(193, 193, 193, 0.67);
  }
  .nodes .node_container.stat_unknown circle {
      stroke: #604c45;
      fill: rgba(99, 77, 69, 0.67);
  }
  .nodes .node_container.focus circle {
      stroke: var(--focus-color) !important;
  }
  /*</editor-fold desc="[node style]">*/

  /*<editor-fold desc="[description style]">*/
  .desc g text {
      text-anchor: middle;
  }
  /*</editor-fold desc="[description style]">*/

  /*<editor-fold desc="[debug: show intermediate nodes]">*/
  /* debug: show intermediate nodes */
  .intermediate_nodes {
      stroke: #000;
      // stroke-width: 1.5px;
  }
  /*</editor-fold desc="[debug: show intermediate nodes]">*/

  /*<editor-fold desc="[animation]">*/
  .animation_resume {
      /* running | paused | initial | inherit */
      -webkit-animation-play-state: running;
      -moz-animation-play-state: running;
      animation-play-state: running;
  }
  .animation_stop {
      -webkit-animation-play-state: paused;
      -moz-animation-play-state: paused;
      animation-play-state: paused;
  }
  /*</editor-fold desc="[animaition]">*/
}
</style>
