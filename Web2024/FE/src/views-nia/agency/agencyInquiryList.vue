<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="agency"
      :ag-grid="agencyAgGrid"
      :is-excel="true"
      :title="titleName"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="(curPage) => onChangePage(curPage)"
      @searchClear="searchClear"
    />
    <ModalAgencyDetail ref="ModalAgencyDetail" @systemEdit="onLoadAgencyList()" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import ModalAgencyDetail from '@/views-nia/modal/ModalAgencyDetail'
import { apiSelectAgencyList } from '@/api/nia'
import CellRenderHyperlink from '@/views-nia/components/cellRenderer/CellRenderHyperlink'

const routeName = 'AgencyInquiryList'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderHyperlink, ModalAgencyDetail },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      titleName: '이용기관',
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
       agencyData: [],
      searchItems: [
        { label: '기관명', type: 'input', model: 'nren_name', placeholder: '기관명을 검색하세요' },
        { label: '노드명', type: 'input', model: 'node_id', placeholder: '노드명을 검색하세요' },
        { label: '고객 ID', type: 'input', model: 'customer_id', placeholder: 'ID를 검색하세요' },
      ],
      searchModel: {
        nren_name: '',
        node_id: '',
        customer_id: '',
      },
    }
  },

  computed: {
    agencyAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rownum', name: '번호', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'centercenter' },
        { type: '', prop: 'nren_name', name: '기관명', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false,
          cellRendererFramework: 'CellRenderHyperlink', cellRendererParams: { type: 'agencyDetail', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: 'node_id', name: '노드명', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'node_int', name: 'I/F ID', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'if_nm', name: 'I/F명', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'bandwidth', name: '대역폭(Gbps)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'customer_id', name: '고객ID', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'email', name: '이메일', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.agencyData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadAgencyList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadAgencyList(params)
    },
    async onLoadAgencyList() {
      const target = { vue: this.$refs.agency }
      this.openLoading(target)
      const param = {
        nren_name: this.searchModel.nren_name,
        node_id: this.searchModel.node_id,
        customer_id: this.searchModel.customer_id,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      try {
        const res = await apiSelectAgencyList(param)
        this.agencyData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
      onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadAgencyList()
    },
      searchClear() {
      this.searchModel = {}
    },
    handleOpenModalDetail(row, type) {
      this.$refs.ModalAgencyDetail.open({ row, type })
    },
  },
}
</script>
l
