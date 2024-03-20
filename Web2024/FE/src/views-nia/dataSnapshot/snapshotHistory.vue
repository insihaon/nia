<template>
  <div :class="{ [name]: true }">
    <div class="common-padding">
      <table class="tb-class">
        <tr>
          <th>제목</th>
          <td class="disable">
            <el-input v-model="selectedRow.if_id" style="width : 500px" />
          </td>
        </tr>
        <tr>
          <th>분류</th>
          <td>
            <el-radio-group v-model="selectedRow.resource">
              <el-radio
                v-for="option in options"
                :key="option"
                :label="option"
              />
            </el-radio-group>
          </td>
        </tr>
        <tr>
          <th>시간</th>
          <td class="disable">
            <el-date-picker v-model="selectedRow.time" type="datetimerange" placeholder="Pick a date" style="width: 100%;" />
          </td>
        </tr>
      </table>
      <div class="button-panel my-1 float-right">
        <el-button size="medium" @click.native="close()">
          {{ '이력보기' }}
        </el-button>
        <el-button size="medium" @click.native="close()">
          {{ '저장' }}
        </el-button>
      </div>

      <CompInquiryPannel
        ref="trafficAnalysis"
        :ag-grid="trafficAgGrid"
        :items="searchItems"
        :search-model.sync="searchModel"
        :pagination-info="paginationInfo"
        class="w-100 h-100"
        :is-search="false"
        @handleClickSearch="onClickSearch"
        @onChangePage="onChangePage"
        @searchClear="searchClear"
      />
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectInOutTrafficList } from '@/api/nia'

const routeName = 'SnapshotHistory'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },

      trafficData: [],
      searchItems: [
        { label: '감시방식', type: 'select', placeholder: '상태를 선택하세요', model: 'watch', setting: { allOption: { toggle: true } }, options: [
            { label: 'OnDemand', value: 'OnDemand' },
            { label: '실시간', value: 'live' },
        ] },
        { label: '장비', type: 'input', model: 'node_name', placeholder: '장비명을 검색하세요' },
        { label: '인터페이스', type: 'input', model: 'if_name', placeholder: '인터페이스를 검색하세요' },
        { label: '시작일시', type: 'date', model: 'datetime' },
        { label: '종료일시', type: 'date', model: 'datetime' },

      ],
      searchModel: {
        node_name: '',
        if_name: '',
      },
        selectedRow: {
          if_id: '',
          time: '',
          resource: '',
        },
      options: ['장애', '이상트래픽', '유해 트래픽', '장비부하 장애', '광신호이상', '시설', '시험데이터']
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'measured_datetime', name: '수집시간', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'node_name', name: '장비명', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'if_name', name: '인터페이스명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'if_num', name: '인터페이스 번호', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'rx_bit_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'tx_pkt_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'rx_pkt_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadTrafficList()
  },
  methods: {
    onSortedChange(param) {
       this.onLoadTrafficList()
    },
    onClickSearch(params) {
      this.onLoadTrafficList(params)
    },
    async onLoadTrafficList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      const param = {
        node_name: this.searchModel.node_name,
        if_name: this.searchModel.if_name,
      }
      if (this.searchModel?.datetime) {
        const dateTime = this.$refs?.trafficAnalysis.searchModel.datetime
        this._merge(param, { start_date: dateTime[0], end_date: dateTime[1] })
      }
      try {
        const res = ''/* await apiSelectInOutTrafficList(param) */
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSopList()
    },
    searchClear() {
      this.searchModel = {}
    },
    onSubmit() {

    }

  },
}
</script>

<style lang="scss" scoped>

.SnapshotHistory{
  table.tb-class {
  width: 100%;
  font-size: 12px;
  text-align: left;
  border-collapse: collapse;
  border: solid 1px #1e293b;
  th,
  td {
    padding: 10px;
    border: 1px solid #bdbdbd !important;

  }
  th {
    font-weight: normal;
    background: #c1d3f64d;
    text-align: center;
    font-size: 14px;
    max-width: 15px !important;
  }
  td {
    font-weight: 600;
    max-width: 50px !important;
    min-width: 30px !important;
  }
  td.disable {
    font-weight: 600;
    background: #fff;
  }
}

}

</style>

