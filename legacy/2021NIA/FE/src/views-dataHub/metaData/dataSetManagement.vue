<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="reqNewDataSet"
      :ag-grid="DatasetReqHistAgGrid"
      :is-button-slot="true"
      :items="searchItems"
      :search-model="searchModel"
      :pagination-info="paginationInfoNewReq"
      class=" w-100 h-50"
      @handleClickSearch="onClickSearchDataSet"
      @keyupEnter="onClickSearchDataSet"
      @selectedRow="selectedItems"
      @searchClear="handleDataSearchClear"
      @sortedChange="onSortedChangeNewDataSet"
    >

      <template #inquireButton>
        <span> API데이터 신청 현황입니다</span>
      </template>

      <template slot="button-container">
        <div class="button-panel my-1">
          <el-button type="danger" plain size="mini" class="float-left " :disabled="!btnDisabled || selectedColRow.length < 1" @click.native="rejectAuth()">선택 반려</el-button>
          <el-button size="mini" class="float-right common-btn" :disabled="!btnDisabled || selectedColRow.length < 1" @click.native="approveSelection()">선택 승인</el-button>
        </div>
      </template>

    </DataHubComponent>
    <DataHubComponent
      ref="dataSet"
      :ag-grid="dataSetAgGrid"
      :is-search="false"
      :pagination-info="paginationInfoDataset"
      title="API 데이터"
      class=" w-100 h-50"
      @selectedRow="selectedColumns"
      @sortedChange="onSortedChangeDataSet"
    >
      <template #inquireButton>
        <span>신청 현황의 체크 박스 선택시 API데이터의 관련 파라미터 확인이 가능합니다</span>
      </template>
    </DataHubComponent>
    <ModalAddDetailColumn ref="ModalAddDetailColumn" />
  </div>
</template>

  <script>

  import { Base } from '@/min/Base.min'
  import DataHubComponent from '@/views-dataHub/components/CompTemplate'
  import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
  import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
  import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
  import { apiSelectDataSetHistList, apiSelectDataSetColumnList, apiUpdateDataSetHistListProc, apiSelectDataCatalogList } from '@/api/dataHub'
  import Eventbus from '@/utils/event-bus'
  import { mapState } from 'vuex'
  import { AppOptions } from '@/class/appOptions'

  const routeName = 'DataSetManagement'
  export default {
    name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { DataHubComponent, CellRenderButtons, ModalAddDetailColumn, hyperLinkTextRender, },
    extends: Base,

    data() {
      return {
        name: routeName,
        src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        paginationInfoNewReq: {
        currentPage: 1, // 현재 페이지
        pageSize: 30, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
        paginationInfoDataset: {
        currentPage: 1, // 현재 페이지
        pageSize: 30, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
        selectedColRow: [],
        selectedCol: [],
        reqHistData: [],
        dataSetColData: [],
        searchItems: [
          { label: 'API 데이터명', type: 'input', size: 8, model: 'dataset_nm', placeholder: 'API 데이터명을 검색하세요', },
          { label: '상태', type: 'select', size: 8, model: 'status_cd', placeholder: '상태를 선택하세요', multiple: true, setting: { allOption: { toggle: true } }, options: [
          { label: '요청', value: 'A' }, { label: '승인', value: 'G' }, { label: '반려', value: 'R' }]
          }
        ],
        searchModel: {
          dataset_nm: '',
          status_cd: []
        },
        rejectedColumn: {},
        sortInfo: {}
      }
    },

    computed: {
      ...mapState({
        blackDtlList: (state) => state.user.blackDtlList
     }),
     hasNoAddGrant() {
       return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
     },
     hasApplyCode() {
      return this.selectedColRow.some(row => row.status_cd_desc === this.CONSTANTS.authDataSet.APPLY.label)
    },
     btnDisabled() {
       return this.hasNoAddGrant && this.hasApplyCode
    },
    DatasetReqHistAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
        }
        const columns = [
          { type: '', prop: 'dataset_nm', name: 'API 데이터명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'exec_mode_cd_desc', name: '연동 방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'create_time', name: '요청일시', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'status_cd_desc', name: '상태', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'dataset_desc', name: '용도 및 요청 설명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: '', name: '기능', minWidth: 150, suppressMenu: true, alignItems: 'left', cellRendererFramework: 'CellRenderButtons', sortable: false, cellRendererParams: { title: '승인', key: 'grant', type: 'reject', action: this.isMode.bind(this) } },
        ]

        return { options, columns, data: this.reqHistData, getRightClickMenuItems: () => { return [] } }
      },
      dataSetAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
        }
        const columns = [
          { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
          cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenModalDetail.bind(this.dataSetColData) } },
          { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'data_type', name: '타입', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'update_time', name: '수정일시', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        ]
        return { options, columns, data: this.dataSetColData, getRightClickMenuItems: () => { return [] } }
      },
    },
    watch: {
    },
    mounted() {
      Eventbus.$on('requestDataSetData', () => {
        this.onLoadList()
      })
      this.onLoadList()
    },

    created() {

    },
    beforeDestroy() {
      Eventbus.$off('requestDataSetData')
    },

    methods: {
        onSortedChangeNewDataSet(param) {
            this.sortInfo = param
            this.onLoadList()
        },
        onSortedChangeDataSet(param) {
            this.sortInfo = param
            this.onLoadColList()
        },
        handleDataSearchClear() {
            this.searchModel.dataset_nm = ''
            this.searchModel.status_cd = []
            this.dataSetColData = []
            this.onLoadList()
        },
        isMode(data, type) {
            if (type === 'reject') {
                this.rejectAuth(data, type)
            } else {
                this.approveSelection(data, type)
            }
        },
        onClickSearchDataSet(params) {
            this.onLoadList(params)
        },
        async onLoadList(params) {
            const target = ({
                vue: this.$refs.reqNewDataSet
            })
            this.openLoading(target)
            const param = {
                dataset_nm: this.searchModel.dataset_nm,
                status_cd: this.searchModel.status_cd,
                page: this.paginationInfoNewReq.currentPage,
                limit: this.paginationInfoNewReq.pageSize,
                sort_column_name: this.sortInfo.colId,
                sort_type: this.sortInfo.sort
            }
            try {
                const res = await apiSelectDataSetHistList(param)
                this.reqHistData = res?.result
                this.paginationInfoNewReq.totalCount = res.total
                this.paginationInfoNewReq.totalPages = Math.ceil(this.paginationInfoNewReq.totalCount / this.paginationInfoNewReq.pageSize)
            } catch (error) {
                console.error(error)
            } finally {
                this.closeLoading(target)
            }
        },
        async onLoadColList(params) {
            const target = ({
                vue: this.$refs.reqNewDataSet
            })
            this.openLoading(target)
            const param = {
                dataset_id: this.selectedColRow[0]?.dataset_id,
                page: this.paginationInfoDataset.currentPage,
                limit: this.paginationInfoDataset.pageSize,
                sort_column_name: this.sortInfo.colId,
                sort_type: this.sortInfo.sort
            }
            try {
                const res = await apiSelectDataSetColumnList(param)
                this.dataSetColData = res?.result
                this.paginationInfoDataset.totalCount = res.total
                this.paginationInfoDataset.totalPages = Math.ceil(this.paginationInfoDataset.totalCount / this.paginationInfoDataset.pageSize)
            } catch (error) {
                console.error(error)
            } finally {
                this.closeLoading(target)
            }
        },
        approveSelection(data, type) {
            let res = ''
            const dataNm = data?.dataset_nm ?? this.selectedColRow?.length + '건의'
            const rowData = Array.isArray(data) ? data : [data]
            this.$confirm(dataNm + ' 데이터를 승인 하시겠습니까?', '확인', {
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                type: 'warning'
            }).then(async () => {
                const CD = this.selectedColRow.map((v) => {
                    return v.status_cd
                })
                if (CD.some(item => item !== 'A')) {
                    this.$message.error({
                        message: `'요청'인 상태만 승인이 가능합니다.`
                    })
                    return false
                }
                try {
                    for (const item of this.selectedColRow ?? rowData) {
                        const params = {
                            dataset_id: item.dataset_id ?? item,
                            status_cd: 'G'
                        }
                        res = await apiUpdateDataSetHistListProc(params)
                    }
                    if (res.result) {
                        this.$message('승인 처리 되었습니다.')
                    }
                    this.selectedCol = []
                    this.onLoadList()
                } catch (error) {
                    this.$message('승인 처리에 실패했습니다.')
                    console.log(error)
                }
            })
        },
        rejectAuth(row) {
            const colNm = this.selectedCol.map((v) => `{${v.column_nm}}`).join(', ')
            this.$prompt("내용을 입력하고 '확인'을 선택하면 반려 작업을 수행합니다.", '알림', {
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                inputValue: colNm,
                inputPattern: '',
                inputErrorMessage: '값을 입력하세요.'
            }).then(async ({
                value
            }) => {
                const CD = this.selectedColRow.map((v) => {
                    return v.status_cd
                })
                if (CD.some(item => item !== 'A')) {
                    this.$message.error({
                        message: `'요청'인 상태만 반려가 가능합니다.`
                    })
                    return false
                }
                try {
                    const params = this.selectedColRow.map((item) => {
                        return {
                            dataset_id: item.dataset_id,
                            status_cd: 'R',
                            refuse_msg: value
                        }
                    })

                    const res = await apiUpdateDataSetHistListProc(params[0])
                    if (res.result) {
                        this.$message('반려 처리 되었습니다.')
                    }
                    this.selectedCol = []
                    this.onLoadList()
                } catch (error) {
                    console.error(error)
                    this.$message.error({
                        message: `반려 작업에 실패했습니다.`
                    })
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '취소'
                })
            })
        },
        selectedItems(param) {
            this.selectedColRow = param
            if (this.selectedColRow[0]) {
                this.onLoadColList()
            } else {
                this.dataSetColData = []
            }
        },
        selectedColumns(param) {
            this.selectedCol = param
        },
        async handleOpenModalDetail(type, row) {
            const param = {
                table_nm: row.table_nm
            }
            const res = await apiSelectDataCatalogList(param)
            const params = res?.result
            this.$refs.ModalAddDetailColumn.open({
                type,
                params,
                routeName
            })
        },
    }
  }
  </script>
  <style lang="scss" scoped>
  .dataSetManagement {
    font-family: "NanumSquare";
    padding: 25px;
    overflow: hidden !important;
    user-select: none;
  }
  </style>

