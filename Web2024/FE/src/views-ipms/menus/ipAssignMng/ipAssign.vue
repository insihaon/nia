<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-max-select="pagination.data.length"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        :prop-on-dbl-click="fnViewClickAsgnIPMst"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP 배정 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-check" type="primary" size="mini" round @click="handleClickIpBlockCheck()">IP블럭 중복체크</el-button>
            <el-button icon="el-icon-document-checked" type="primary" size="mini" round @click="fnUpdateBtnClick()">배정</el-button>
            <el-button icon="el-icon-menu" type="primary" size="mini" round @click="handleClickMergeInsert()">병합</el-button>
          </div>
        </template>
      </comptable>
    </el-col>
    <ModalIpBlockDivision ref="ModalIpBlockDivision" @reload="onloadIpAssign($refs.searchCondition.requestParameter)" />
    <ModalIpAssignDetail ref="ModalIpAssignDetail" @reload="onloadIpAssign($refs.searchCondition.requestParameter)" />
    <ModalIpAssign ref="ModalIpAssign" @reload="onloadIpAssign($refs.searchCondition.requestParameter)" />
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <ModalIpAssignMerge ref="ModalIpAssignMerge" @reload="fnViewListIpAllocMst($refs.searchCondition.requestParameter)" />
    <ModalDetailSummary ref="ModalDetailSummary" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDivision from '@/views-ipms/modal/ModalIpBlockDivision.vue'
import ModalIpAssignDetail from '@/views-ipms/modal/ModalIpAssignDetail.vue'
import ModalIpAssign from '@/views-ipms/modal/ModalIpAssign.vue'
import ModalCheckTacsIpBlock from '@/views-ipms/modal/ModalCheckTacsIpBlock.vue'
import ModalIpAssignMerge from '@/views-ipms/modal/assign/ModalIpAssignMerge.vue'
import ModalDetailSummary from '@/views-ipms/modal/ModalDetailSummary.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'IpAssign'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAssignDetail, ModalCheckTacsIpBlock, ModalIpAssign, ModalIpAssignMerge, ModalDetailSummary },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask' } },
        { key: 'DateRange', props: {} },
        { key: 'IpAddress', props: { defaultWord: '' } },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpBlockStatus', props: { label: '배정상태' } },
        { key: 'SortType', props: {} },
        { key: 'IncludeYN', props: { label: 'Summary 포함 여부', prop_parameterKey: 'snull0Yn' } },
        { key: 'IncludeYN', props: { label: 'DB-라우팅 일치 여부', prop_parameterKey: 'sintgrmYn' } },
        { key: 'InputType', props: { label: '라우팅 중복 개수', prop_parameterKey: 'nsummaryCnt' } },
      ],
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nclassCnt', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'sassignLevelNm', label: '배정상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snull0Yn', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sintgrmYn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              // class: row.nsummaryCnt > 0 ? 'red' : '',
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: row.nsummaryCnt > 0 ? 'danger' : 'primary'
              },
              on: { click: () => {
                this.$refs.ModalDetailSummary.open({ row })
            } } }, row.nsummaryCnt)
          }
        },
        { prop: 'nbitmask', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            const isDivisible = (row.sipVersionTypeCd === 'CV0001' && row.nbitmask < 24) || (row.sipVersionTypeCd === 'CV0002' && row.nbitmask < 64)
            return this.$createElement('el-button', {
              // class: isDivisible ? '' : 'red',
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: isDivisible ? 'primary' : 'danger'
              },
              on: {
                click: () => {
                  if (isDivisible) {
                    this.$refs.ModalIpBlockDivision.open({ row })
                  }
                }
              }
            }, isDivisible ? '분할' : '불가')
          }
        },
      ],

      ipAssignDatas: [],
      selectedRows: [],
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        if (this.ipms.toParams !== null) {
          const { value } = this.ipms.toParams
          if (value) {
            const compInfo = this.componentList.find(v => v.key === 'IpAddress')
            this.$set(compInfo.props, 'defaultWord', value)
          }
          this.$store.dispatch('ipms/setToParam', null)
          setTimeout(() => {
            this.onloadIpAssign()
          }, 10)
        }
      },
      immediate: true
    }
  },
  mounted () {
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.onloadIpAssign(requestParameter)
    },
    async onloadIpAssign(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListAsgnIPMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.onloadIpAssign()
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    //  handleClickCell(params) { /* 더블클릭시 this.selectedRow 저장되는 이슈로 임시 주석처리 */
    //   this.selectedRows = [params?.row] || []
    // },
    fnViewClickAsgnIPMst(row, column) {
      if (column?.property === 'nsummaryCnt' || column?.property === 'nbitmask') return
      this.fnViewDetailAsgnIPMst(row)
    },
    async fnViewDetailAsgnIPMst(row) {
      this.$refs.ModalIpAssignDetail.open({ row })
    },
    handleClickIpBlockCheck() {
      const rows = this.selectedRows
      if (rows.length === 0) {
        onMessagePopup(this, 'IP블럭 중복체크할 대상이 없습니다. 선택해주세요.')
        return
      }
      if (rows.length > 1) {
        onMessagePopup(this, 'IP블럭 중복체크할 대상을 다건 선택 할 수 없습니다. 확인해주세요.')
        return
      }
      const { sipVersionTypeCd, ssvcLineTypeCd, nipAssignMstSeq } = rows[0]
      if (sipVersionTypeCd !== 'CV0001') {
        onMessagePopup(this, 'IP블럭 중복체크는 IPv4만 가능합니다.')
        return
      }
      if (ssvcLineTypeCd !== 'CL0001' && ssvcLineTypeCd !== 'CL0002' && ssvcLineTypeCd !== 'CL0003') {
        onMessagePopup(this, 'IP블럭 중복체크는 KOREAN, PREMIUM, MOBILE망만 가능합니다.')
        return
      }
      this.$refs.ModalCheckTacsIpBlock.open({ row: rows[0] })
    },
    fnUpdateBtnClick() {
      const rows = this.selectedRows
      if (rows.length === 0) {
        onMessagePopup(this, '배정할 대상이 없습니다.')
        return
      }

        const { nlvlMstSeq, sassignLevelCd, sassignTypeCd } = rows[0]

        for (const row of rows) {
            if (nlvlMstSeq !== row.nlvlMstSeq) {
              onMessagePopup(this, '배정할 대상 블록의 계위 정보가 동일하지 않습니다. 확인해주세요.')
              return false
            }

            if (sassignLevelCd !== row.sassignLevelCd) {
              onMessagePopup(this, '배정할 대상 블록의 배정 상태가 동일하지 않습니다. 확인해주세요.')
              return false
            }

            if (sassignTypeCd !== row.sassignTypeCd) {
              onMessagePopup(this, '배정할 대상 블록의 서비스가 동일하지 않습니다. 확인해주세요.')
              return false
            }
          }

        const tbIpAssignMstListVo = { tbIpAssignMstVos: [] }
         rows.forEach(row => {
          tbIpAssignMstListVo.tbIpAssignMstVos.push({ ...row })
        })
        this.$refs.ModalIpAssign.open({ tbIpAssignMstListVo })
    },
    handleClickMergeInsert() {
      const rows = this.selectedRows

      if (rows.length === 0) {
        onMessagePopup(this, '병합할 대상이 없습니다. 선택해주세요.')
        return
      } else if (rows.length === 1) {
        onMessagePopup(this, '병합할 대상은 최소 2개 이상 선택해 주시기 바랍니다.')
        return
      }

      const { nipBlockSeq, nlvlMstSeq, sassignLevelCd, sassignTypeCd } = rows[0]

      for (const row of rows) {
        if (nipBlockSeq !== row.nipBlockSeq) {
          onMessagePopup(this, '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.')
          return
        }

        if (nlvlMstSeq !== row.nlvlMstSeq) {
          onMessagePopup(this, '병합할 대상 정보들의 계위 정보가 동일하지 않습니다.')
          return
        }

        if (sassignLevelCd !== row.sassignLevelCd) {
          onMessagePopup(this, '병합할 대상 정보들의 배정 상태가 동일하지 않습니다.')
          return
        }

        if (sassignTypeCd !== row.sassignTypeCd) {
          onMessagePopup(this, '병합할 대상 정보들의 서비스가 동일하지 않습니다.')
          return
        }
      }

      const tbIpAssignMstListVo = { typeFlag: 'Aloc', tbIpAssignMstVos: [] }
      rows.forEach(row => {
        tbIpAssignMstListVo.tbIpAssignMstVos.push({ nipAssignMstSeq: row.nipAssignMstSeq })
      })
      this.$refs.ModalIpAssignMerge.open({ tbIpAssignMstListVo })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListAsgnIPMstExcel')
    }
  }
}
</script>
