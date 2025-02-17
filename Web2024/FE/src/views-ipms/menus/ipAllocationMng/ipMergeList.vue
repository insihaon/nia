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
        :prop-is-cell-click-check="true"
        :prop-max-select="pagination.data.length"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="hadleClickIpAllocDetail"
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
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-menu" type="primary" size="mini" round @click="fnMergeBtnClick">병합</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <!-- IP블록병합 -->
    <ModalIpAssignMerge ref="ModalIpAssignMerge" @reload="fnViewListIpAllocMst()" />
    <!-- 병합가능 리스트 상세 -->
    <ModalIpMergeInfoDetail ref="ModalIpMergeInfoDetail" @reload="fnViewListIpAllocMst()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpAssignMerge from '@/views-ipms/modal/assign/ModalIpAssignMerge.vue'
import ModalIpMergeInfoDetail from '@/views-ipms/modal/alloc/ModalIpMergeInfoDetail.vue'

import { fnViewCheckTacsIpBlock } from '@/views-ipms/js/common-function'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestExcel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'ipMergeList'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpAssignMerge, ModalIpMergeInfoDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: { apiPath: '/ipmgmt/linemgmt', voName: 'tbIpAssignSubVos', valueKey: { cd: 'sofficecode', nm: 'sofficename' } } },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitMask' } },
        // { key: 'LineInformation', props: {} },
        // {
        //   key: 'IpBlockStatus', props: {
        //     label: '할당상태', prop_options: [
        //       { label: '서비스배정[미할당]', value: 'IA0004' },
        //       { label: '할당예약', value: 'IA0005' },
        //       { label: '할당', value: 'IA0006' },
        //     ]
        //   }
        // },
        { key: 'DateRange', props: { } },
        // {
        //   key: 'InputSearchDetail',
        //   props: {
        //     label: '장비명',
        //     modalName: 'ModalFacilityInformation',
        //     valueName: 'ssubscnealias',
        //     prop_parameterKey: { sicisofficescodeNe: 'sofficecode', smodelnameNe: 'smodelname', ssubscmstipNe: 'ssubscmstip', ssubscnealiasNe: 'ssubscnealias' },
        //   }
        // },
        { key: 'SortType', props: { sortOrdrDefaultVal: 'ASC' } },
        // { key: 'IncludeYN', props: { label: 'Summary 포함 여부', prop_parameterKey: 'snull0Yn' } },
        // { key: 'IncludeYN', props: { label: 'DB-라우팅 일치여부', prop_parameterKey: 'sintgrmYn' } },
        // { key: 'RoutingDuplCount', props: { label: '라우팅 중복 개수', prop_parameterKey: 'summaryCnt', valueType: 'number' } },
        // { key: 'InputType', props: { label: '비고', prop_parameterKey: 'scomment' } },
      ],
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '병합가능 블록 수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            if (this.isValidJSON(row.ipList)) {
              return this.$createElement('el-button', {
                attrs: {
                  round: true, // Adding the round option
                  plain: true,
                  type: 'primary'
                },
                on: { click: () => {
                  /* 병합 가능 목록 상세 화면
                    groupid 파라미터 셋팅해서 화면에 데이터 출력 할당쿼리 사용 , groupid 조건만 추가
                  */
                 this.$refs.ModalIpMergeInfoDetail.open({ row })
              } } }, JSON.parse(row.ipList).length)
            } else {
              return 0
            }
          }
          // formatter: (row) => { return this.isValidJSON(row.ipList) ? JSON.parse(row.ipList).length : 0 }
         },
        // { prop: 'groupId', label: '그룹ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.scomment?.length > 0 ? 'Y' : 'N' } },
      ],
      selectedRows: []
    }
  },
  methods: {
    isValidJSON(data) {
      try {
        JSON.parse(data)
        return true
      } catch (error) {
        return false
      }
    },
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpAllocMst(requestParameter)
    },
    async fnViewListIpAllocMst(requestParameter = null) {
      this.selectedRows = []
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpMergeMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListIpAllocMst()
    },
    handleClickCell(params) {
      this.selectedRows = [params?.row] || []
    },
    fnViewDetailAlcIpMst(row) {
      this.selectedRows = [row]
      this.$refs.ModalIpAllocDetail.open({ row })
    },
    hadleClickIpAllocDetail(row) {
      this.fnViewDetailAlcIpMst(row)
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    fnMergeBtnClick() {
      const checkedList = this._orderBy(this.selectedRows, ['asc', 'nipAllocMstCnt'])
      if (checkedList.length === 0) {
        onMessagePopup(this, '병합할 대상이 없습니다.')
        return
      } else if (checkedList.length === 1) {
        onMessagePopup(this, '병합할 대상은 최소 2개이상 선택해 주시기 바랍니다.')
        return
      }
      const { nipBlockMstSeq, sassignLevelCd, sassignTypeCd, nlvlMstSeq, sipCreateTypeCd } = checkedList[0]
      for (const row of checkedList) {
        if (nipBlockMstSeq !== row.nipBlockMstSeq) {
          onMessagePopup(this, '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.')
          return
        }
        if (sassignLevelCd !== row.sassignLevelCd) {
          onMessagePopup(this, '병합할 대상 정보들의 작업 상태가 동일하지 않습니다')
          return
        }
        if (sassignTypeCd !== row.sassignTypeCd) {
          onMessagePopup(this, '병합할 대상 정보들의 서비스가 동일하지 않습니다.')
          return
        }
        if (nlvlMstSeq !== row.nlvlMstSeq) {
          onMessagePopup(this, '병합할 대상 정보들의 계위(조직)정보가 동일하지 않습니다.')
          return
        }
        if (sipCreateTypeCd !== row.sipCreateTypeCd) {
          onMessagePopup(this, '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.')
          return
        }
        if (row.sassignLevelCd === 'IA0005' || row.sassignLevelCd === 'IA0006') {
          onMessagePopup(this, '병합할 대상 정보들의 작업 상태가 할당확정/할당예약 일 경우 병합할 수 없습니다.')
          return
        }
      }
      const tbIpAssignMstListVo = { typeFlag: 'Aloc', tbIpAssignMstVos: [] }
      checkedList.forEach(row => { tbIpAssignMstListVo.tbIpAssignMstVos.push({ nipAssignMstSeq: row.nipAssignMstSeq }) })
      this.$refs.ModalIpAssignMerge.open({ tbIpAssignMstListVo })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListIpAllocMstExcel')
    }
   },
}
</script>
<style lang="scss" scoped></style>
