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
        :prop-max-select="1"
        :prop-is-cell-click-check="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="(row) => hadleClickIpAllocDetail(row)"
        :prop-on-select="handleClickTableCheck"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @update:propCellClick="handleClickCell"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP 할당 조회결과
          </span>
        </template>
        <template slot="left-features">
          <el-button icon="el-icon-copy-document" type="primary" size="mini" round @click="handleClickViewChange">
            {{ viewMode === 'autoAloc' ? '시설용 IP할당 이동' : 'IPv4 시설용 IP 자동할당 이동' }}
          </el-button>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <!-- <el-button icon="el-icon-check" type="primary" size="mini" round @click="fnViewCheckTacsIpBlock_">IP블럭 중복체크</el-button> -->
            <el-button v-if="viewMode === 'autoAloc'" icon="el-icon-thumb" type="primary" size="mini" round @click="fnViewDetailAlcIpMst()">
              IPv6로 자동할당
            </el-button>
            <el-button v-else icon="el-icon-thumb" type="primary" size="mini" round @click="fnViewDetailAlcIpMst()">
              할당
            </el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <!-- 분할 -->
    <ModalIpBlockDivision ref="ModalIpBlockDivision" @reload="fnViewListIpAllocMst()" />
    <!-- 할당 처리 -->
    <ModalIpAllocInsert ref="ModalIpAllocInsert" @reload="fnViewListIpAllocMst()" />
    <!-- 할당 상세 -->
    <ModalIpAllocDetail ref="ModalIpAllocDetail" @alocCallBtnClick="fnInsertAlcBtnClick" />
    <!-- 할당 처리 전 계위 설정 -->
    <ModalIpAllocOrgSetting ref="ModalIpAllocOrgSetting" @alocDetailCallBtnClick="(row) => hadleClickIpAllocDetail(row, true)" />
    <!-- 라우팅 중복 개수 -->
    <ModalDetailSummary ref="ModalDetailSummary" />
    <!-- IP블록병합 -->
    <ModalIpAssignMerge ref="ModalIpAssignMerge" @reload="fnViewListIpAllocMst()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpAllocInsert from '@/views-ipms/modal/alloc/ModalIpAllocInsert.vue'
import ModalIpAllocDetail from '@/views-ipms/modal/alloc/ModalIpAllocDetail.vue'
import ModalIpAllocOrgSetting from '@/views-ipms/modal/alloc/ModalIpAllocOrgSetting.vue'
import ModalCheckTacsIpBlock from '@/views-ipms/modal/ModalCheckTacsIpBlock.vue'
import ModalIpBlockDivision from '@/views-ipms/modal/ModalIpBlockDivision.vue'
import ModalDetailSummary from '@/views-ipms/modal/ModalDetailSummary.vue'
import ModalIpAssignMerge from '@/views-ipms/modal/assign/ModalIpAssignMerge.vue'

import { fnViewCheckTacsIpBlock } from '@/views-ipms/js/common-function'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson, apiRequestExcel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'FacilitesipAllocation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAllocDetail, ModalIpAllocOrgSetting, ModalCheckTacsIpBlock, ModalIpAllocInsert, ModalIpAssignMerge, ModalDetailSummary },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      viewMode: 'manualAloc', // autoAloc (v4-> v6 자동할당)/ manualAloc (v6 수동할당)
      selectedRow: null,
      sassignTypeCds: [],
    }
  },
  computed: {
    tableColumns() {
      return [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nipAllocMstCnt', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubsclgipportdescription', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snull0Yn', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.snull0Yn?.length === 0 ? '-' : row.snull0Yn } },
        { prop: 'sintgrmYn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.nsummaryCnt?.length === 0) {
              return ''
            } else {
              return this.$createElement('el-button', {
                attrs: {
                  round: true, // Adding the round option
                  plain: true,
                  disabled: this.viewMode === 'manualAloc',
                  type: row.nsummaryCnt > 0 ? 'danger' : 'primary'
                },
                on: { click: () => {
                  this.$refs.ModalDetailSummary.open({ row })
              } } }, row.nsummaryCnt)
            }
          }
        },
        { prop: '', label: '시설 DB 일치 여부', align: 'center', sortable: true, showOverflow: true, columnVisible: this.viewMode === 'autoAloc' },
        { prop: 'division', label: '분할', align: 'center', sortable: true, showOverflow: true, columnVisible: this.viewMode === 'manualAloc',
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              // class: row.sassignLevelCd === 'IA0004' ? '' : 'red',
              attrs: {
                round: true, // Adding the round option
                plain: true,
                disabled: true,
                type: row.sassignLevelCd === 'IA0004' ? 'primary' : 'danger'
              },
              on: { click: () => {
                if (row.sassignLevelCd === 'IA0004') {
                  this.$refs.ModalIpBlockDivision.open({ row, typeFlag: 'Aloc' })
                }
            } } }, row.sassignLevelCd === 'IA0004' ? '분할' : '불가')
          }
        },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.scomment?.length > 0 ? 'Y' : 'N' } },
      ]
    },
    componentList() {
      return [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: { apiPath: '/ipmgmt/linemgmt', voName: 'tbIpAssignSubVos', valueKey: { cd: 'sofficecode', nm: 'sofficename' } } },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 10, /* prop_options: this.sassignTypeCds */ } },
        { key: 'IpAddress', props: { defaultValue: this.viewMode === 'autoAloc' ? 'CV0001' : 'CV0002', disabled: this.viewMode === 'autoAloc' } },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask', valueType: 'number' } },
        { key: 'LineInformation', props: {} },
        {
          key: 'IpBlockStatus', props: {
            label: '할당상태', prop_options: [
              { label: '서비스배정[미할당]', value: 'IA0004' },
              { label: '할당', value: 'IA0006' },
            ], defaultValue: this.viewMode === 'manualAloc' ? 'IA0004' : 'IA0006',
            isShowAll: false, disabled: this.viewMode === 'autoAloc'
          }
        },
        { key: 'DateRange', props: { } },
        { key: 'SortType', props: { sortOrdrDefaultVal: 'ASC' } },
        { key: 'InputType', props: { label: '비고', prop_parameterKey: 'scomment' } },
      ]
    }
  },
  /*
    - row 선택 후 할당 버튼 클릭시
      2/3계위 선택되었을 경우 [할당] 화면
      선택 안되었을 경우 계위 설정화면 : ModalIpAllocOrgSetting
    - row 클릭 시
      2/3계위 선택되었을 경우 [할당 상세] 화면
      선택 안되었을 경우 계위 설정화면
  */
  mounted() {
    this.viewMode = 'manualAloc'
    this.$store.dispatch('ipms/setIsFacilitesOption', true)
  },
  beforeDestroy() {
    this.$store.dispatch('ipms/setIsFacilitesOption', false)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpAllocMst(requestParameter)
    },
    async fnViewListIpAllocMst(requestParameter = null) {
      this.selectedRow = null
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex, isFacilities: true })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpAllocMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleClickViewChange() {
      if (this.viewMode === 'manualAloc') {
        this.viewMode = 'autoAloc'
        this.$route.meta.title = 'IPv4 시설용 IP 자동할당'
      } else {
        this.viewMode = 'manualAloc'
        this.$route.meta.title = '시설용 IP할당'
      }
      this.pagination.data = []
      this.pagination.total = 0
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListIpAllocMst()
    },
    handleClickCell(params) {
      this.selectedRow = params?.row
    },
    fnViewDetailAlcIpMst(receviedRow = null, isSetOrg = false) {
      const row = receviedRow ?? this.selectedRow
      const isSettingOrg = row.ssvcGroupCd !== '000000' && row.ssvcObjCd !== '000000'
      if (this.viewMode === 'manualAloc') {
        if ((isSettingOrg || isSetOrg) || row.sassignLevelCd === 'IA0006') {
          this.$refs.ModalIpAllocDetail.open({ row, menuType: this.viewMode })
        } else if (!isSettingOrg) {
          this.$refs.ModalIpAllocOrgSetting.open({ row })
        }
      }
    },
    hadleClickIpAllocDetail(row = null, isSetOrg = false) {
      this.fnViewDetailAlcIpMst(row, isSetOrg)
    },
    handleClickTableCheck(all, cur) {
      this.selectedRow = cur
    },
    fnViewCheckTacsIpBlock_() {
      fnViewCheckTacsIpBlock(this, this.selectedRow)
    },
    fnInsertAlcBtnClick(receivedRow = null) {
      const row = receivedRow ?? this.selectedRow
      if (row === null) {
        onMessagePopup(this, '할당 할 대상이 없습니다.')
        return
      }
      /* 체크대상여부 확인 */
       /* Step 01. 서비스배정상태 체크 */
        if (row.sassignLevelCd !== 'IA0004') {
          onMessagePopup(this, '할당 대상 블록 중 서비스배정이 아닌 블록이 있습니다.')
          return
        }
        /* Step 02. 계위 및 서비스 유형 동일 선택 체크 */
        const { sassignTypeCd, nlvlMstSeq, nbitmask, sneossDdYn, ssvcLineTypeCd, sipCreateTypeCd, sipVersionTypeCd } = row

        // let linkYn = 'N'
        // let linkYn2 = 'N'
        // if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd)) {
        //   if (sneossDdYn === 'N' && [29, 30].includes(nbitmask)) {
        //     linkYn = 'Y'
        //   }
        // }
        // for (let j = 0; j < i; j++) {
        //   if (sassignTypeCd !== rows[j].sassignTypeCd || nlvlMstSeq !== rows[j].nlvlMstSeq) {
        //     onMessagePopup(this, '선택하신 할당 대상 블록의 계위/서비스 정보가 동일하지 않습니다. 확인해주세요.')
        //     return
        //   }
        //   if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd)) {
        //     if (rows[j].sneossDdYn === 'N' && [29, 30].includes(rows[j].nbitmask)) {
        //       linkYn2 = 'Y'
        //     }
        //   }
        // }
        // if (linkYn !== linkYn2) {
        //   onMessagePopup(this, '선택하신 할당 대상 블록의 할당구분(회선/시설/링크) 정보가 동일하지 않습니다. 확인해주세요.')
        //   return
        // }
        if ((sipVersionTypeCd === 'CV0001' && nbitmask < 16) || (sipVersionTypeCd === 'CV0002' && nbitmask < 48)) {
          onMessagePopup(this, '/16(IPv4), /48(IPv6) 보다 큰 IP블록은 할당할 수 없습니다.')
          return
        }
        if (ssvcLineTypeCd === 'CL0005' && sipCreateTypeCd !== 'CT0001') {
          onMessagePopup(this, 'VPN망은 공인 IP블록만 할당할 수 있습니다.')
          return
        }
        // cidr 검증, 중복 ip 검증 서비스 추가
        this.$refs.ModalIpAllocInsert.open({ ipAllocOperMstVos: [row] })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListIpAllocMstExcel')
    }
   },
}
</script>
<style lang="scss" scoped></style>
