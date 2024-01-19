<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="selectDataSet"
      :ag-grid="selectDataSetAgGrid"
      :items="searchItems"
      :search-model="searchModel"
      :pagination-info="paginationInfo"
      title="API 데이터"
      class="w-100 h-50"
      @handleClickSearch="onLoadList"
      @searchClear="handleApiSearchClear"
      @sortedChange="onSortedChange"
    >

      <template #searchCaption>
        <span> Ondemand 서비스만 확인 가능 </span>
      </template>

      <template #inquireButton>
        <span> API서비스 관련 상세내역을 확인할 수 있습니다 </span>
      </template>
    </DataHubComponent>
    <DataHubComponent
      ref="selectApi"
      :json-data="jsonData"
      :is-json="true"
      :is-search="false"
      class="w-100 h-50"
      @handleClickSearch="onLoadList"
      @searchClear="handleApiSearchClear"
      @jsonClear="handleApiSearchClear"
      @handleRequest="requestAPI"
    />

    <!-- <ModalAddDetailColumn ref="ModalAddDetailColumn" /> -->
  </div>
</template>
<script>

import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
import { apiEcho, apiSelectApiList, apiSelectApiTableList, apiSelectDataCatalogList, } from '@/api/dataHub'

import * as Ajv from 'ajv'
const ajv = new Ajv()

const routeName = 'SelectDataSet'
export default {
  name: routeName,
  components: {
    // eslint-disable-next-line vue/no-unused-components
    DataHubComponent, CellRenderButtons, ModalAddDetailColumn, hyperLinkTextRender
  },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 30, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
       jsonData: {
          reqJsonData: { json: {} },
          resJsonData: { json: {} },
       },
       options: { mode: 'code' },
      apiData: [],
      searchItems: [
      { label: 'API명', placeholder: 'API를 선택하세요', type: 'select', size: 8, multiple: false, model: 'api_id', setting: { allOption: { toggle: true } }, options: null },
      ],
      searchModel: {
        api_id: '',
      },
      selectApiList: [],
      apiList: [],
      sortInfo: {}
    }
  },

  computed: {
    selectDataSetAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
        cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenTableModalDetail.bind(this) } },
        { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'data_type', name: '타입', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'create_time', name: '생성시간', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
      ]
      return { options, columns, data: this.apiData, getRightClickMenuItems: () => { return [] } }
    },
  },
  watch: {
    'searchModel.api_id'(n, o) {
      this.onLoadList(n)
      this.loadJson(n)
      this.requestAPI(this.jsonData)
    }
  },
  mounted() {
    this.onLoadList()
  },
  created() {
  },

  methods: {
    onActiveView() {
      this.onLoadApiList()
    },
    async testApi(uri, param) {
      try {
        const res = await apiEcho(uri, param)
        this.jsonData.resJsonData.json = res.result
      } catch (error) {
        console.log(error)
      }
    },
    onSortedChange(param) {
       this.sortInfo = param
       this.onLoadList()
      },
    handleApiSearchClear() {
      this.searchModel.api_id = ''
      this.jsonData.reqJsonData = { json: {} }
      this.jsonData.resJsonData = { json: {} }
      this.onLoadList()
    },
    requestAPI(value) {
      try {
        if (value.reqJsonData.json === null || value.reqJsonData.json === '') {
          throw new Error('요청 데이터가 존재하지 않습니다. API 데이터를 선택하세요')
        }
        var selectApiUrl = ''
        const jsonData = value?.reqJsonData?.json
        const isValid = ajv.validate(true, value?.reqJsonData?.json)
        this.apiList.map(item => {
          if (this.searchModel?.api_id === item?.api_id) {
            selectApiUrl = item?.api_url
          }
        })
        const param = {
          apiUrl: selectApiUrl,
          jsonData: jsonData
        }
        if (isValid) {
          this.testApi(selectApiUrl, jsonData)
        }
      } catch (error) {
        this.$message({
          message: value.reqJsonData.json === null || value.reqJsonData.json === '' ? error.message : '요청 형식이 맞지 않습니다.',
          type: 'error'
        })
      }
    },
    async onLoadApiList() {
      const res = await apiSelectApiList()
      this.apiList = res?.result

      this.selectApiList = this.apiList.map(item => ({ label: item.api_name, value: item.api_id }))
      const tableNmItem = this.searchItems.find(item => item.model === 'api_id')
      if (tableNmItem) {
        tableNmItem.options = this.selectApiList
      }
     if (Object.keys(this.$route.query).length >= 1) {
       const apiInfo = res?.result.find(item => item.api_id === this.$route.query.api_id)
       this.searchModel.api_id = apiInfo.api_id
       this.$router.replace({ path: this.$route.path, query: {} })
       } else {
         this.$router.replace({ path: this.$route.path, query: {} })
       }
    },
  loadJson(param) {
    this.jsonData.resJsonData.json = {}
    const value = this.apiList.find(i => i.api_id === param)

    if (value?.req_param) {
        const parsingData = JSON.parse(value.req_param)
        this.jsonData.reqJsonData.json = parsingData
    }
  },
  async onLoadList(value) {
    const target = ({ vue: this.$refs.selectDataSetAgGrid })
    const api_id = this.searchModel.api_id
    if (!api_id) {
      return
    }

    this.openLoading(target)
    const param = {
      api_id: this.searchModel.api_id,
      limit: this.paginationInfo.pageSize,
      page: this.paginationInfo.currentPage,
      sort_column_name: this.sortInfo.colId,
      sort_type: this.sortInfo.sort
    }
    try {
      const res = await apiSelectApiTableList(param)
      this.searchModel.api_id === '' ? this.apiData = [] : this.apiData = res?.result
      this.paginationInfo.totalCount = res.total
      this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize)
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async handleOpenTableModalDetail(type, row) {
      const param = {
        table_nm: row.table_nm
      }
      const res = await apiSelectDataCatalogList(param)
      const params = res?.result
      this.$modal.show('editMonitoringExcludeAlarm', { type, params, routeName })
    },
}

  }
</script>

<style lang="scss" scope>

  .SelectDataSet {
    font-family: "NanumSquare";
    overflow: hidden !important;
    user-select: none;
    display: flex;
    flex-direction: column;
  }

  .dataComponents {
    display: flex;
    align-content: space-evenly;
    margin-top: 20px;
    min-width: 800px;

    .el-textarea__inner{
      width: 100%;
      height: 95%;
      border: 1px rgb(197, 197, 193) solid;
    }

    .el-textarea__inner::placeholder{
      font-size: large;
      color: rgb(7, 7, 7, 0.3);
    }

    .button-container{
      display: flex;
      align-items: center;
    }
  }

</style>
