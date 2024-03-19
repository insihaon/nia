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
      @handleClickSearch="onClickSearch"
      @onChangePage="onChangePage"
      @searchClear="searchClear"
    />
    <ModalNodeDetail ref="ModalNodeDetail" />
    <ModalPortManagement ref="ModalPortManagement" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import ModalNodeDetail from '@/views-nia/modal/ModalNodeDetail'
import ModalPortManagement from '@/views-nia/modal/ModalPortManagement'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'
import CellRenderHyperlink from '@/views-nia/components/cellRenderer/CellRenderHyperlink'
import { apiSelectNodeList } from '@/api/nia'

const routeName = 'NodeInquiryList'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, ModalPortManagement, ModalNodeDetail, CellRenderAibuttons, CellRenderHyperlink },
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
      nodeData: [],
      searchItems: [
        { label: '노드명', type: 'input', model: 'node_nm', placeholder: '노드를 검색하세요' },
        { label: '모델명', type: 'input', model: 'model_id', placeholder: '모델명을 검색하세요' },
        { label: '대표 IP', type: 'input', model: 'ip_addr', placeholder: 'IP를 검색하세요' },
        { label: '사용여부', type: 'select', placeholder: '사용여부를 선택하세요', model: 'admin_yn', setting: { allOption: { toggle: true } },
          options: [
            { label: '전체', value: 'all' },
            { label: '사용', value: 'up' },
            { label: '미사용', value: 'down' },
        ] },
        { label: '날짜', type: 'dateTime', model: 'datetime' },

      ],
      searchModel: {
        node_nm: '',
        model_id: '',
        ip_addr: '',
        admin_yn: '',
      },
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rownum', name: '번호', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'node_nm', name: '노드명', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center',
          cellRendererFramework: 'CellRenderHyperlink', cellRendererParams: { type: 'nodeDetail', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: 'model_id', name: '모델명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'mac_addr', name: '맥주소', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'ip_addr', name: '대표IP', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'admin_yn_info', name: '사용여부', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'chng_datetime', name: '수정일', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'rx_pkt_rate', name: '포트', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true,
          cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { type: 'nodeManegement', action: this.handleOpenEditModal.bind(this) } }
      ]
      return { options, columns, data: this.nodeData, getRightClickMenuItems: () => { return [] } }
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
        node_nm: this.searchModel.node_name,
        model_id: this.searchModel.if_name,
        ip_addr: this.searchModel.ip_addr,
        admin_yn: this.searchModel.admin_yn,
      }
      if (this.searchModel?.datetime) {
        const dateTime = this.$refs?.trafficAnalysis.searchModel.datetime
        this._merge(param, { start_date: dateTime[0], end_date: dateTime[1] })
      }
      try {
        const res = await apiSelectNodeList(param)
        this.nodeData = res?.result
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
    handleOpenEditModal(row, type) {
      if (type === 'nodeDetail') {
        this.$refs.ModalNodeDetail.open({ row: row, type: type })
      } else {
        this.$refs.ModalPortManagement.open({ row: row, type: type })
      }
    },

  },
}
</script>

