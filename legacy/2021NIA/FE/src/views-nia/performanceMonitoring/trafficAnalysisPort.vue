<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="trafficAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @cellClicked="cellTemp"
      @sortChanged="sortTemp"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectInOutTrafficList } from '@/api/nia'

const routeName = 'TrafficAnalysisPort'
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
        { label: '감시방식', type: 'select', placeholder: '상태를 선택하세요', model: 'watch', setting: { allOption: { toggle: true } }, options: [
            { label: 'OnDemand', value: 'OnDemand' },
            { label: '실시간', value: 'live' },
        ] },
        { label: '장비', type: 'input', model: 'api_name', placeholder: '장비명을 검색하세요' },
        { label: '인터페이스', type: 'input', model: 'api_name', placeholder: '인터페이스를 검색하세요' },
        { label: '시작일시', type: 'date', model: 'start_date' },
        { label: '종료일시', type: 'date', model: 'start_date' },

      ],
      searchModel: {
        api_name: '',
        status_cd: [],
        create_time: [],
        expird_date: [],
      },
      sortInfo: {}
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'measured_datetime', name: '수집시간', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'node_name', name: '장비명', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'if_name', name: '인터페이스명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: false },
        { type: '', prop: 'if_num', name: '인터페이스 번호', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadTrafficList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadTrafficList()
    },

    // onClickSearchAuth(params) {
    //   this.onLoadTrafficList(params)
    // },
    async onLoadTrafficList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      try {
        const res = await apiSelectInOutTrafficList()
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
                  //  const packetBytesInGbyte = item.packet_bytes / (1024 * 1024); // 바이트를 기가바이트로 변환
                  //   const roundedGbyte = Math.round(packetBytesInGbyte * 100) / 100; // 소수점 두 자리까지 반올림
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

  },
}
</script>

