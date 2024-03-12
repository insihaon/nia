<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="authAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="onChangePage"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { AppOptions } from '@/class/appOptions'
import { apiSelectAppTrafficList } from '@/api/nia'

const routeName = 'TrafficAnalysisApp'
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
      selectedRow: [],
      trafficData: [],
      searchItems: [
        { label: 'Application(S)', type: 'select', multiple: true, placeholder: '', model: 'src_protocol', setting: { allOption: { toggle: true } }, options: [] },
        { label: 'Port (S)', type: 'input', model: 'src_port', placeholder: '' },
        { label: 'Application(S)', type: 'input', model: 'dst_protocol', placeholder: '' },
        { label: 'Port (D)', type: 'select', multiple: true, placeholder: '', model: 'dst_port', setting: { allOption: { toggle: true } }, options: [] },
        { label: 'Top N', type: 'select', multiple: false, placeholder: '', model: 'rank_order', icon: 'el-icon-warning', setting: { allOption: { toggle: true } },
          options: [
            { label: '10', value: '10' },
            { label: '30', value: '30' },
            { label: '50', value: '50' },
            { label: '100', value: '100' }
          ],
        }
      ],
      searchModel: {
        src_protocol: [],
        src_port: '',
        dst_protocol: [],
        dst_port: '',
        rank_order: ''
      },
      sortInfo: {}
    }
  },

  computed: {
    authAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rank_order', name: 'Rank', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'src_protocol', name: 'Application(Source)', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'src_port', name: 'Port(Source)', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'dst_protocol', name: 'Application(Destination)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'dst_port', name: 'Port(Destination)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'packet_bytes', name: 'Gbyte', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      // packet_bytes.toLocaleString
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadTrafficList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
    onSortedChange() {
       this.sortInfo = []
       this.onLoadTrafficList()
    },

    onClickSearch(params) {
      this.onLoadTrafficList(params)
    },
    async onLoadTrafficList() {
     const { pageSize: limit, currentPage: page } = this.paginationInfo
       const param = {
        src_protocol: this.searchModel.src_protocol,
        src_port: this.searchModel.src_port,
        dst_protocol: this.searchModel.dst_protocol,
        dst_port: this.searchModel.dst_port,
        rank_order: this.searchModel.rank_order,
        pageSize: limit,
        currentPage: page
       }
      try {
        const res = await apiSelectAppTrafficList(param)
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
                  //  const packetBytesInGbyte = item.packet_bytes / (1024 * 1024); // 바이트를 기가바이트로 변환
                  //   const roundedGbyte = Math.round(packetBytesInGbyte * 100) / 100; // 소수점 두 자리까지 반올림
      } catch (error) {
        console.error(error)
      } finally {
       /*  */
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSopList()
    },

  },
}
</script>

