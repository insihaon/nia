<template>
  <div :class="[{ [name]: true, ['h-100']: true, }]">
    <div
      class="w-100"
      style="height:100%;"
    >
      <div style="height:40%; width: 100%">
        <span class="topologyTableHeader">토폴로지</span>
        <span v-if="debug">nescode:{{ tag.tagData.nescode }}</span>
        <svg
          ref="e2e"
          :class="[{ [`${tag.tagData.nescode}`]: true, ['e2e']: true }]"
          style="width: 100%; height: calc(100% - 20px)"
        />
      </div>

      <div style="height: 30%; width: 100%">
        <span class="topologyTableHeader">상위 연결 시설 내역</span>
        <div>
          <div>
            <div style="text-align: right">검색결과 : {{ paginationInfoApi.totalCountString }}건</div>
          </div>
        </div>
        <CompAgGrid
          ref="higherEquipTable"
          v-model="higherEquipTableModel"
          style="width: 100%; height: calc(100% - 20px)"
          @changeSelectedRows="higherEquipTableChangeSelectedRows"
          @cellClicked="higherEquipTableCellClicked"
          @sortChanged="higherEquipTableOnSortChanged"
        />
      </div>

      <div style="height: 30%; width: 100%">
        <span class="topologyTableHeader">영향 시설 내역</span>
        <div>
          <div>
            <div style="text-align: right">검색결과 : {{ paginationInfoApi2.totalCountString }}건</div>
          </div>
        </div>
        <CompAgGrid
          ref="lowerEquipTable"
          v-model="lowerEquipTableModel"
          style="width: 100%; height: calc(100% - 40px)"
          @changeSelectedRows="lowerEquipTableChangeSelectedRows"
          @cellClicked="lowerEquipTableCellClicked"
          @sortChanged="lowerEquipTableOnSortChanged"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { defaultNescode } from '@/store/modules/influenceCircitStore'
import CompAgGrid from '@/components/aggrid_/CompAgGrid.vue'
import { Base } from '@/min/Base.min'
import { apiSELECT_NEA_DOWN_LIST, apiSELECT_NEA_UPPER_LIST } from '@/api/dataHub'
import EventBus from '@/utils/event-bus'
import { defaultZero } from '@/views-dataHub/commonFormat'

const routeName = 'influenceCircitContent'
export default {
  name: routeName,
  components: { CompAgGrid },
  extends: Base,
  props: {
    tag: {
      type: Object,
      default: () => { return {} }
    }
  },

  data() {
    return {
      name: routeName,
      paginationInfoApi: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalCountString: '0', // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      paginationInfoApi2: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalCountString: '0', // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      higherEquipTableData: [],
      lowerEquipTableData: [],
      e2e: null
    }
  },

  computed: {
    higherEquipTableModel() {
      const options = { name: this.name + 'higherEquipTable' + '_' + this.tag.tagData.nescode, rowGroupPanel: false, rowHeight: 40, rowSelection: 'single', rowMultiSelection: true }
      const columns = [
        { type: '', prop: 'order_num', name: '번호', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'officenamescode', name: '관리국사', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'svcnetdescription', name: '서비스망', width: 120, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'maindescription', name: '대분류', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'subdescription', name: '소분류', width: 150, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'description', name: '용도', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'modelname', name: '모델명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'nealias', name: '장치명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        {
          type: '', name: '수용고객', suppressMenu: true, sortable: true, alignItems: 'center',
          children: [
            { name: '고객', prop: 'nea_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_svc_cnt', width: 100, formatter: defaultZero },
            { name: '분류', prop: '', width: 100 },
          ]
        },
        {
          type: '', name: '영향고객', suppressMenu: true, sortable: true,
          children: [
            { name: '고객', prop: 'nea_relation_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_relation_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_relation_svc_cnt', width: 100, formatter: defaultZero },
          ]
        },
        {
          type: '', name: '장비위치', suppressMenu: true, sortable: true,
          children: [
            { name: '설치국사', prop: 'officename', width: 120 },
            { name: '주소', prop: 'full_address', width: 200 },
            { name: '빌딩', prop: 'bldgname', width: 120 },
            { name: '설치위치', prop: '', width: 100 },
          ]
        }
      ]
      return { options, columns, data: this.higherEquipTableData }
    },

    lowerEquipTableModel() {
      const options = { name: this.name + 'lowerEquipTable' + '_' + this.tag.tagData.nescode, rowGroupPanel: false, rowHeight: 40, rowSelection: 'single', rowMultiSelection: true }
      const columns = [
        { type: '', prop: 'order_num', name: '번호', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'officenamescode', name: '관리국사', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'svcnetdescription', name: '서비스망', width: 120, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'maindescription', name: '대분류', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'subdescription', name: '소분류', width: 150, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'description', name: '용도', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'modelname', name: '모델명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'nealias', name: '장치명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        {
          type: '', name: '수용고객', suppressMenu: true, sortable: true, alignItems: 'center',
          children: [
            { name: '고객', prop: 'nea_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_svc_cnt', width: 100, formatter: defaultZero },
            { name: '분류', prop: '', width: 100 },
          ]
        },
        {
          type: '', name: '영향고객', suppressMenu: true, sortable: true,
          children: [
            { name: '고객', prop: 'nea_relation_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_relation_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_relation_svc_cnt', width: 100, formatter: defaultZero },
          ]
        },
        {
          type: '', name: '장비위치', suppressMenu: true, sortable: true,
          children: [
            { name: '설치국사', prop: 'officename', width: 120 },
            { name: '주소', prop: 'full_address', width: 200 },
            { name: '빌딩', prop: 'bldgname', width: 120 },
            { name: '설치위치', prop: '', width: 100 },
          ]
        }
      ]
      return { options, columns, data: this.lowerEquipTableData }
    },

  },

  watch: {
    'tag.tagData.nescode'(n, o) {
      if (n === o) return

      if (o === 'checkToggle_defaultCode') {
        this.load()
      }
    },
  },

  async mounted() {
    const { AppOptions } = require('@/class/appOptions')
    if (AppOptions.debug || this.tag.tagData.nescode !== defaultNescode) {
      this.load()
    }
  },

  created() {
    const THIS = this
    EventBus.$on('onCollapseChanged', (delay) => {
      THIS.e2e?.onCanvasSizeChanged(delay)
    })
  },
  unmounted() {
    EventBus.$off('onCollapseChanged')
  },

  beforeDestroy() {
    if (this.e2e) {
      this.e2e.destroy()
      delete this.e2e
      console.log('장치 상세 검색이 삭제됩니다.' + this.tag.tagData.nescode)
    }
  },

  methods: {
    async load() {
      console.log(`reload: ${this.tag.tagData.nescode}`)

      const [upData, downData] = await Promise.all([
        this.loadhigherEquipTableModel(),
        this.loadlowerEquipTableModel()
      ])

      const rootData = this.tag.tagData
      this.initMap({ rootData: equipToNode(rootData), upData: equipToNode(upData), downData: equipToNode(downData) })
    },

    initMap(initData) {
      const THIS = this
      if (this.tag.tagData.nescode === 'checkToggle_defaultCode') {
        return
      }

      const fnNodeClick = async (d) => {
        // d.data?.nescode
        // console.log('fnNodeClick', d?.direction, d?.data)

        // 디버깅용
        // return []

        let res
        if (d?.direction === 'up') {
          res = await THIS.getHigherEquipList(d.data?.nescode)
        } else {
          res = await THIS.getLowerEquipList(d.data?.nescode)
        }
        return equipToNode(res.result)
      }
      const fnNodeDblClick = (d) => {
        // d.data?.nescode
        // console.log('fnNodeClick', d?.direction, d?.data)
        this.$emit('e2eNodeClick', d)
      }

      this.$nextTick(async () => {
        const selector = `svg.e2e.${this.tag.tagData.nescode}`
        // eslint-disable-next-line no-undef
        window.e = this.e2e = new E2e(selector, initData, fnNodeClick, fnNodeDblClick)
      })
    },

    async getHigherEquipList(nescode) {
      const param = {
        NESCODE: nescode,
      }
      return await apiSELECT_NEA_UPPER_LIST(param)
    },

    async getLowerEquipList(nescode) {
      const param = {
        NESCODE: nescode,
      }
      return await apiSELECT_NEA_DOWN_LIST(param)
    },

    async loadhigherEquipTableModel() {
      const target = ({ vue: this.$refs.higherEquipTable })
      this.openLoading(target)

      try {
        const res = await this.getHigherEquipList(this.tag.tagData.nescode)
        this.higherEquipTableData = res.result
        this.paginationInfoApi.totalCount = res.total
        this.paginationInfoApi.totalCountString = this.humanNumber(res.total)
        return res.result
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
      return null
    },

    async loadlowerEquipTableModel() {
      const target = ({ vue: this.$refs.lowerEquipTable })
      this.openLoading(target)

      try {
        const res = await this.getLowerEquipList(this.tag.tagData.nescode)
        this.lowerEquipTableData = res.result
        this.paginationInfoApi2.totalCount = res.total
        this.paginationInfoApi2.totalCountString = this.humanNumber(res.total)
        return res.result
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
      return null
    },

    lowerEquipTableChangeSelectedRows() {
    },
    lowerEquipTableCellClicked() {
    },
    lowerEquipTableOnSortChanged() {
    },
    higherEquipTableChangeSelectedRows() {
    },
    higherEquipTableCellClicked() {
    },
    higherEquipTableOnSortChanged() {
    },

  }

}

function equipToNode(equip) {
  if (!equip) {
    return []
  }
  const isArray = Array.isArray(equip)
  const array = isArray ? equip : [equip]
  for (let index = 0; index < array.length; index++) {
    array[index].name = array[index].nealias
    array[index].hasChildren = array[index].is_leaf === false
  }
  return isArray ? array : array.at(0)
}

</script>

<style lang="scss">
.influenceCircitContent{

  .topologyTableHeader{
    margin-left: 5px;
    height: 20px;
    color: #254772 !important;
    font-size: 14px;
  }

  svg.e2e {
    flex: 1;
    width: 100%;
    height: 100%;
    /* max-height: 300px; */
    background-color: rgb(39, 43, 77);
  }

  .node {
    cursor: pointer;
  }

  .node circle {
  }

  .node text {
    font: 10px sans-serif;
  }

  .link {
    fill: none;
    stroke: rgba(255, 255, 255, .4);
    stroke-width: 1px;
  }
}

.tooltip {
  color: white;
  font-size: 11px;
  transition: opacity 1s;
  z-index: 9999;
}

.bold {
  stroke-width: 4px !important;
}

</style>
