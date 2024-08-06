<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="onloadIpAssign"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 120px)'"
        :prop-data="ipAssignDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        @update:propCellClick="handleClickItem"
      >
        <template slot="text-description">
          <span>
            IP 배정 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="mt-1 d-flex justify-end">
            <el-button icon="el-icon-check" type="primary" size="mini" @click="handleClickIpBlockCheck()">IP블럭 중복체크</el-button>
            <el-button icon="el-icon-document-checked" style="background: #2b5890;" type="primary" size="mini" @click="handleClickIpAssignInsert()">배정</el-button>
            <el-button size="mini" @click="handleClickMergeInsert()">병합</el-button>
          </div>
        </template>
      </comptable>
      <ModalIpBlockDivision ref="ModalIpBlockDivision" />
      <ModalIpAssignDetail ref="ModalIpAssignDetail" />
      <ModalIpAssign ref="ModalIpAssign" />
      <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
      <ModalIpMerge ref="ModalIpMerge" />
      <ModalDetailSummary ref="ModalDetailSummary" />
    </el-col>
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
import ModalIpMerge from '@/views-ipms/modal/ModalIpMerge.vue'
import ModalDetailSummary from '@/views-ipms/modal/ModalDetailSummary.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
const routeName = 'IpAssign'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAssignDetail, ModalCheckTacsIpBlock, ModalIpAssign, ModalIpMerge, ModalDetailSummary },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask' } },
        { key: 'DateRange', props: {} },
        { key: 'IpAddress', props: {} },
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
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '배정상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snull0Yn', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sintgrmYn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              class: row.nsummaryCnt > 0 ? 'red' : '',
              on: { click: () => {
                this.$refs.ModalDetailSummary.open({ row })
            } } }, row.nsummaryCnt)
          }
        },
        { prop: 'nbitmask', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            const isDivisible = (row.sipVersionTypeCd === 'CV0001' && row.nbitmask < 24) ||
                                (row.sipVersionTypeCd === 'CV0002' && row.nbitmask < 64)

            const buttonText = isDivisible ? '분할' : '불가'
            const buttonClass = isDivisible ? '' : 'red'

            return this.$createElement('el-button', {
              class: buttonClass,
              on: {
                click: () => {
                  if (isDivisible) {
                    this.$refs.ModalIpBlockDivision.open({ row })
                  }
                }
              }
            }, buttonText)
          }
        },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      ipAssignDatas: [],
      selectedTable: []
    }
  },
  mounted () {
    this.onloadIpAssign()
    const { ipAddress } = this.$route.query
    if (ipAddress) {
      const compInfo = this.componentList.find(v => v.key === 'IpAddress')
      this.$set(compInfo.props, 'defaultWord', ipAddress)
    }
  },
  methods: {
    async onloadIpAssign(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListAsgnIPMst, requestParameter)
        this.ipAssignDatas = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    handleClickTableCheck(all, cur) {
      this.selectedTable = all
    },
    handleClickItem(row) {
      if (row.column.property === 'nsummaryCnt' || row.column.property === 'nbitmask') return
      this.onClcikRow(row.row)
    },
    onClcikRow(row) {
      this.$refs.ModalIpAssignDetail.open({ row })
    },
    handleClickIpBlockCheck() {
      const rows = this.selectedTable
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
      this.$refs.ModalCheckTacsIpBlock.open({ /* tacsResponse: res */ row: rows[0] })
    },
    handleClickIpAssignInsert() {
      const rows = this.selectedTable
      if (rows.length === 0) {
        onMessagePopup(this, '배정할 대상이 없습니다.')
        return
      }

      const res = rows.map((row, i) => {
        const { nlvlMstSeq, sassignLevelCd, sassignTypeCd } = row

        for (let j = 0; j < i; j++) {
          if (nlvlMstSeq !== rows[j].nlvlMstSeq || nlvlMstSeq !== rows[j].nlvlMstSeq) {
            onMessagePopup(this, '배정할 대상 블록의 계위 정보가 동일하지 않습니다. 확인해주세요.')
            return false
          }

          if (sassignLevelCd !== rows[j].sassignLevelCd || sassignLevelCd !== rows[j].sassignLevelCd) {
            onMessagePopup(this, '배정할 대상 블록의 배정 상태가 동일하지 않습니다. 확인해주세요.')
            return false
          }

          if (sassignTypeCd !== rows[j].sassignTypeCd || sassignTypeCd !== rows[j].sassignTypeCd) {
            onMessagePopup(this, '배정할 대상 블록의 서비스가 동일하지 않습니다. 확인해주세요.')
            return false
          }
        }
        return true
      })
      res.every(r => r === true) && this.$refs.ModalIpAssign.open({ row: this.selectedTable })
    },
    handleClickMergeInsert() {
      const rows = this.selectedTable

      if (rows.length === 0) {
        onMessagePopup(this, '병합할 대상이 없습니다. 선택해주세요.')
        return
      } else if (rows.length === 1) {
        onMessagePopup(this, '병합할 대상은 최소 2개 이상 선택해 주시기 바랍니다.')
        return
      }

          const res = rows.map((row, i) => {
          const { nipBlockSeq, nlvlMstSeq, sassignLevelCd, sassignTypeCd } = row

          for (let j = 0; j < i; j++) {
            if (nipBlockSeq !== rows[j].nipBlockSeq || nipBlockSeq !== rows[j].nipBlockSeq) {
              onMessagePopup(this, '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.')
              return false
            }

            if (nlvlMstSeq !== rows[j].nlvlMstSeq || nlvlMstSeq !== rows[j].nlvlMstSeq) {
              onMessagePopup(this, '병합할 대상 정보들의 계위 정보가 동일하지 않습니다.')
              return false
            }

            if (sassignLevelCd !== rows[j].sassignLevelCd || sassignLevelCd !== rows[j].sassignLevelCd) {
              onMessagePopup(this, '병합할 대상 정보들의 배정 상태가 동일하지 않습니다.')
              return false
            }

            if (sassignTypeCd !== rows[j].sassignTypeCd || sassignTypeCd !== rows[j].sassignTypeCd) {
              onMessagePopup(this, '병합할 대상 정보들의 서비스가 동일하지 않습니다.')
              return false
            }
          }
          return true
        })
        res.every(r => r === true) && this.$refs.ModalIpMerge.open({ row: this.selectedTable })
      }
  }
}
</script>
