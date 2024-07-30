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
        :prop-on-click="onClcikRow"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
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
            <el-button size="mini">병합</el-button>
          </div>
        </template>
      </comptable>
      <ModalIpBlockDivision ref="ModalIpBlockDivision" />
      <ModalIpAssignDetail ref="ModalIpAssignDetail" />
      <ModalIpAssign ref="ModalIpAssign" />
      <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
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
const routeName = 'IpAssign'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAssignDetail, ModalCheckTacsIpBlock, ModalIpAssign },
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
        { prop: 'nsummaryCnt', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snull0Yn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sintgrmYn', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      ipAssignDatas: [
        {
          ssvcLineTypeNm: 'MOBILE',
          ssvcGroupNm: '미디어운용센터',
          ssvcObjNm: '백석GMC운용팀',
          sipCreateTypeNm: '공인',
          sassignTypeNm: 'IPTV',
          pipPrefix: '192.168.0.0/24',
          sfirstAddr: '192.168.0.1',
          slastAddr: '192.168.0.254',
          nclassCnt: '0300390625',
          dmodifyDt: '2024-06-18',
          sassignLevelNm: '서비스배정[미할당]',
          nsummaryCnt: 'Y',
          snull0Yn: 'Y',
          sintgrmYn: 0,
          nbitmask: '23',
          sipVersionTypeCd: 'CV0001',
          ncnt: '64',
          nfreeIpCnt: '64',
          nuseIpCnt: '0',
          sipVersionTypeNm: 'IPv4',
          ssvcLineTypeCd: 'CL0003',
        },
         {
          ssvcLineTypeNm: 'MOBILE',
          ssvcGroupNm: 'DATA망',
          ssvcObjNm: '백석GMC운용팀',
          sipCreateTypeNm: '공인',
          sassignTypeNm: 'IPTV',
          pipPrefix: '192.168.0.0/24',
          sfirstAddr: '192.168.0.1',
          slastAddr: '192.168.0.254',
          nclassCnt: '0300390625',
          dmodifyDt: '2024-06-18',
          sassignLevelNm: '서비스배정[미할당]',
          nsummaryCnt: 'Y',
          snull0Yn: 'Y',
          sintgrmYn: 0,
          nbitmask: '65',
          sipVersionTypeCd: 'CV0001',
          ncnt: '64',
          nfreeIpCnt: '64',
          nuseIpCnt: '0',
          sipVersionTypeNm: 'IPv4',
          ssvcLineTypeCd: 'CL0003',
        },
        {
          ssvcLineTypeNm: 'MOBILE',
          ssvcGroupNm: 'DATA망',
          ssvcObjNm: '백석GMC운용팀',
          sipCreateTypeNm: '공인',
          sassignTypeNm: 'IPTV',
          pipPrefix: '192.168.0.0/24',
          sfirstAddr: '192.168.0.1',
          slastAddr: '192.168.0.254',
          nclassCnt: '0300390625',
          dmodifyDt: '2024-06-18',
          sassignLevelNm: '서비스배정[미할당]',
          nsummaryCnt: 'Y',
          snull0Yn: 'Y',
          sintgrmYn: 0,
          nbitmask: '65',
          sipVersionTypeCd: 'CV0002',
          ncnt: '64',
          nfreeIpCnt: '64',
          nuseIpCnt: '0',
          sipVersionTypeNm: 'IPv6',
          ssvcLineTypeCd: 'CL0003',
        },
      ],
      selectedRow: [],
      selectedTable: []
    }
  },
  mounted () {
    const { ipAddress } = this.$route.query
    if (ipAddress) {
      const compInfo = this.componentList.find(v => v.key === 'IpAddress')
      this.$set(compInfo.props, 'defaultWord', ipAddress)
    }
  },
  methods: {
    onloadIpAssign(requestParameter) {
    /* try {
        const res = await api(requestParameter)
        this.ipAssignDatas = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
    onClcikRow(row) {
      this.$refs.ModalIpAssignDetail.open({ row })
      this.selectedRow = row
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
      this.$refs.ModalIpAssign.open({ row: this.selectedRow })
    },
    handleClickTableCheck(all, cur) {
      this.selectedTable = all
    },
  }
}
</script>
