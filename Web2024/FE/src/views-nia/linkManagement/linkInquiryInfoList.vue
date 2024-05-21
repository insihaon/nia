<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="linkManagement"
      :ag-grid="linkAgGrid"
      :is-excel="true"
      :title="titleName"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="(curPage) => onChangePage(curPage)"
      @onDebugTest="autoTest"
      @searchClear="searchClear"
    >
      <template slot="add-function">
        <el-button type="info" size="mini" icon="el-icon-edit" @click="handleOpenModalDetail('', 'OPEN')">등록</el-button>
      </template>
    </CompInquiryPannel>
    <ModalLinkDetail ref="ModalLinkDetail" @systemEdit="onLoadLinkList()" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CellRenderHyperlink from '@/views-nia/components/cellRenderer/CellRenderHyperlink'
import ModalLinkDetail from '@/views-nia/modal/ModalLinkDetail'
import { apiSelectLinkList } from '@/api/nia'

const routeName = 'LinkInquiryInfoList'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderHyperlink, ModalLinkDetail },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      titleName: '링크 정보',
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
       linkData: [],
      searchItems: [
         { label: '시작점노드', type: 'input', model: 'src_node_id', placeholder: '노드를 검색하세요' },
         { label: '끝점 노드', type: 'input', model: 'dest_node_id', placeholder: '노드를 검색하세요' },
         { label: '시작점 I/F명', type: 'input', model: 'src_if_name', placeholder: '노드를 검색하세요' },
         { label: '끝점 I/F명', type: 'input', model: 'dest_if_name', placeholder: '노드를 검색하세요' },
         { label: '링크 용도', type: 'input', model: 'link_desc', placeholder: '노드를 검색하세요' },
      ],
      searchModel: {
       src_node_id: '',
       dest_node_id: '',
       src_if_name: '',
       dest_if_name: '',
       link_desc: ''
      },
    }
  },

  computed: {
    linkAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rownum', name: '번호', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'src_node_id', name: '시작점 노드', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false,
         cellRendererFramework: 'CellRenderHyperlink', cellRendererParams: { type: 'linkDetail', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: 'src_if_id', name: '시작점 I/F ID', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'src_if_name', name: '시작점 I/F명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'dest_node_id', name: '끝점 노드', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'dest_if_id', name: '끝점 I/F ID', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'dest_if_name', name: '끝점 I/F명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'bandwidth', name: '대역폭(Gpbs)', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'link_desc', name: '링크용도', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'chng_datetime', name: '수정일', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.linkData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadLinkList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadLinkList(params)
    },
    async onLoadLinkList() {
      const target = { vue: this.$refs.linkManagement }
      this.openLoading(target)
      const param = {
        src_node_id: this.searchModel.src_node_id,
        dest_node_id: this.searchModel.dest_node_id,
        src_if_name: this.searchModel.src_if_name,
        dest_if_name: this.searchModel.dest_if_name,
        link_desc: this.searchModel.link_desc,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      try {
        const res = await apiSelectLinkList(param)
        this.linkData = res?.result
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
      this.onLoadLinkList()
    },
    searchClear() {
      this.searchModel = {}
    },
    handleOpenModalDetail(row, type) {
      this.$refs.ModalLinkDetail.open({ row, type })
    },
    async autoTest() {
     const { assert, wait, onLoadLinkList } = this
     await onLoadLinkList()
     assert(this.linkData.length > 0)
     await wait(1000)
     window.ref.ModalLinkDetail.open({ row: this.linkData[3] })
     await wait(1000)
     window.ref.ModalLinkDetail.updateLinkData('test')
     await wait(1000)
     document.querySelector(Base.confirmBtn).click()
     await wait(1000)
     window.ref.ModalLinkDetail.deleteLinkData('test')
     await wait(1000)
     document.querySelector(Base.confirmBtn).click()
     await wait(1000)
     window.ref.ModalLinkDetail.close()
     await wait(1000)
     document.querySelector(Base.exportExcel).click()
    }
  }
}
</script>
