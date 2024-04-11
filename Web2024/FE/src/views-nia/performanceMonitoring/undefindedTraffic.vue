<template>
  <div :class="{ [name]: true }">
    <el-tabs class="h-100 common-padding" style="flex:1">
      <el-tab-pane label="국가">
        <div class="h-100 flex-fill" sytle="height: 100%">
          <CompInquiryPannel
            ref="trafficNation"
            :ag-grid="nationAgGrid"
            :items="searchNationItems"
            :search-model.sync="searchModel"
            :pagination-info="nationPaginationInfo"
            class="w-100 flex-fill"
            :style="{ height : 'calc(100vh - 200px)'}"
            @handleClickSearch="onClickSearch"
            @onChangePage="(curPage) => onChangePage(curPage, 'NATION')"
            @searchClear="searchClear"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="이용기관">
        <div class="h-90 flex-fill" sytle="height: 100%">
          <CompInquiryPannel
            ref="trafficAgency"
            :ag-grid="agencyAgGrid"
            :items="searchAgencyItems"
            :search-model.sync="searchModel"
            :pagination-info="agencyPaginationInfo"
            class="w-100 flex-fill"
            :style="{ height : 'calc(100vh - 200px)'}"
            @handleClickSearch="onClickSearch"
            @onChangePage="(curPage) => onChangePage(curPage, 'AGENCY')"
            @searchClear="searchClear"
          >
            <template slot="button-area">
              <div class="button-panel">
                <el-button class="float-right" type="info" @click="handleOpenEditModal('','AGENCY')">등록</el-button>
              </div>
            </template>
          </CompInquiryPannel>
        </div>

      </el-tab-pane>
      <el-tab-pane label="어플리케이션">
        <div class="h-100 flex-fill" sytle="height: 100%">
          <CompInquiryPannel
            ref="trafficApp"
            :ag-grid="appAgGrid"
            :items="searchAppItems"
            :search-model.sync="searchModel"
            :pagination-info="appPaginationInfo"
            class="w-100 flex-fill"
            :style="{ height : 'calc(100vh - 200px)'}"
            @handleClickSearch="onClickSearch"
            @onChangePage="(curPage) => onChangePage(curPage, 'APP')"
            @searchClear="searchClear"
          >
            <template slot="button-area">
              <div class="button-panel">
                <el-button class="float-right" size="mini" type="info" @click="handleOpenEditModal('', 'APP')">등록</el-button>
              </div>
            </template>
          </CompInquiryPannel>
        </div>
      </el-tab-pane>
    </el-tabs>
    <ModalEditTrafficData ref="ModalEditTrafficData" @systemEdit="refreshData()" />
    <ModalAddTrafficData ref="ModalAddTrafficData" @systemEdit="refreshData()" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import ModalEditTrafficData from '@/views-nia/modal/ModalEditTrafficData'
import ModalAddTrafficData from '@/views-nia/modal/ModalAddTrafficData'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'
import { apiSelectUnidentifiedNationList, apiSelectUnidentifiedAgencyList, apiSelectUnidentifiedAppList } from '@/api/nia'

const routeName = 'UndefindedTraffic'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderAibuttons, ModalEditTrafficData, ModalAddTrafficData },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      nationPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      agencyPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      appPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
       nationData: [],
       agencyData: [],
       appData: [],
      searchNationItems: [
       { label: '국가명', type: 'input', model: 'country_name', placeholder: '국가명을 검색하세요' },
       { label: '국가코드', type: 'input', model: 'country_code', placeholder: '국가코드를 검색하세요' },
      ],
      searchAgencyItems: [
       { label: '이용기관명', type: 'input', model: 'nren_name', placeholder: '이용기관명을 검색하세요' },
       { label: 'IP주소', type: 'input', model: 'nren_ip', placeholder: 'IP주소를 검색하세요' },
      ],
        searchAppItems: [
       { label: '어플리케이션명', type: 'input', model: 'protocol', placeholder: '어플리케이션명을 검색하세요' },
       { label: 'PORT번호', type: 'input', model: 'port_num', placeholder: 'PORT번호를 검색하세요' },
      ],
      searchModel: {
        country_name: '',
        country_code: '',
        nren_name: '',
        nren_ip: '',
        protocol: '',
        port_num: '',
      },
      sortInfo: {}
    }
  },

  computed: {
    nationAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'country_name', name: '국가명', minWidth: 120, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'koica', name: '국가코드', minWidth: 120, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.nationData, getRightClickMenuItems: () => { return [] } }
    },
     agencyAgGrid() {
      const options = {
        name: this.name + 'table2', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'nren_name', name: '이용기관명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'nren_ip', name: 'IP주소', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
         { type: '', prop: '', name: '', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center',
          cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '수정/삭제', icon: 'edit', type: 'editAgency', action: this.handleOpenEditModal.bind(this) } }
      ]
      return { options, columns, data: this.agencyData, getRightClickMenuItems: () => { return [] } }
    },
    appAgGrid() {
      const options = {
        name: this.name + 'table3', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'protocol', name: '어플리케이션명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'port_num', name: 'PORT 번호', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: '', name: '', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center',
          cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '수정/삭제', icon: 'edit', type: 'editApp', action: this.handleOpenEditModal.bind(this) } }
      ]
      return { options, columns, data: this.appData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadNationList()
    this.onLoadAgencyList()
    this.onLoadAppList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadNationList(params)
      this.onLoadAgencyList(params)
      this.onLoadAppList(params)
    },
    async onLoadNationList(params) {
      const target = { vue: this.$refs.trafficNation }
      this.openLoading(target)
      const param = {
        country_name: this.searchModel.country_name,
        country_code: this.searchModel.country_code,
        limit: this.nationPaginationInfo.pageSize,
        page: this.nationPaginationInfo.currentPage,
      }
      try {
        const res = await apiSelectUnidentifiedNationList(param)
        this.nationData = res?.result
        this.nationPaginationInfo.totalCount = res.total
        this.nationPaginationInfo.totalPages = Math.ceil(this.nationPaginationInfo.totalCount / this.nationPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onLoadAgencyList() {
      const target = { vue: this.$refs.trafficAgency }
      this.openLoading(target)
      const param = {
        nren_name: this.searchModel.nren_name,
        nren_ip: this.searchModel.nren_ip,
        limit: this.agencyPaginationInfo.pageSize,
        page: this.agencyPaginationInfo.currentPage,
      }
      try {
        const res = await apiSelectUnidentifiedAgencyList(param)
        this.agencyData = res?.result
        this.agencyPaginationInfo.totalCount = res.total
        this.agencyPaginationInfo.totalPages = Math.ceil(this.agencyPaginationInfo.totalCount / this.agencyPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onLoadAppList(params) {
      const target = { vue: this.$refs.trafficApp }
      this.openLoading(target)
       const param = {
        protocol: this.searchModel.protocol,
        port_num: this.searchModel.port_num,
        limit: this.appPaginationInfo.pageSize,
        page: this.appPaginationInfo.currentPage,
      }
      try {
        const res = await apiSelectUnidentifiedAppList(param)
        this.appData = res?.result
        this.appPaginationInfo.totalCount = res.total
        this.appPaginationInfo.totalPages = Math.ceil(this.appPaginationInfo.totalCount / this.appPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
     onChangePage(curPage, type) {
      if (type === 'NATION') {
        this.nationPaginationInfo.currentPage = curPage
        this.onLoadNationList()
      } else if (type === 'AGENCY') {
        this.agencyPaginationInfo.currentPage = curPage
        this.onLoadAgencyList()
      } else {
        this.appPaginationInfo.currentPage = curPage
        this.onLoadAppList()
      }
    },
    searchClear() {
      this.searchModel = {}
    },
    handleOpenEditModal(row, type) {
      if (type === 'AGENCY' || type === 'APP') {
          this.$refs.ModalAddTrafficData.open({ type: type })
      } else {
        this.$refs.ModalEditTrafficData.open({ row: row, type: type })
      }
    },
    refreshData() {
       this.onLoadAgencyList()
       this.onLoadAppList()
    }
  },
}
</script>

