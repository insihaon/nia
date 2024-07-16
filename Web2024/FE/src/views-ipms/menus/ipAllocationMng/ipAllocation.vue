<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 할당 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalIpBlockDivision ref="ModalIpBlockDivision" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDivision from '@/views-ipms/modal/ModalIpBlockDivision.vue'

const routeName = 'IpAllocation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitMask' } },
        { key: 'LineInformation', props: {} },
        {
          key: 'IpBlockStatus', props: {
            label: '할당상태', prop_options: [
              { label: '서비스배정[미할당]', value: 'IA0004' },
              { label: '할당예약', value: 'IA0005' },
              { label: '할당', value: 'IA0006' },
            ]
          }
        },
        { key: 'DateRange', props: { } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '장비명',
            modalName: 'ModalFacilityInformation',
            valueName: 'ssubscnealias',
            prop_parameterKey: { sicisofficescodeNe: 'sofficecode', smodelnameNe: 'smodelname', ssubscmstipNe: 'ssubscmstip', ssubscnealiasNe: 'ssubscnealias' },
          }
        },
        { key: 'SortType', props: { } },
        { key: 'IncludeYN', props: { label: 'Summary 포함 여부', prop_parameterKey: 'snull0Yn' } },
        { key: 'IncludeYN', props: { label: 'DB-라우팅 일치여부', prop_parameterKey: 'sintgrmYn' } },
        { key: 'InputType', props: { label: '라우팅 중복 개수', prop_parameterKey: 'nsummaryCnt' } },
      ],
      tableColumns: [
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'allocationStatus', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },

        { prop: '', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'division', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              class: row.allocationStatus === 'Y' ? 'red' : '',
              on: { click: () => {
                this.$refs.ModalIpBlockDivision.open({ row })
            } } }, row.allocationStatus === 'Y' ? '불가' : '분할')
          }
        },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: [
        { allocationStatus: 'Y' },
        { allocationStatus: 'N' },
      ]
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
  },
}
</script>
<style lang="scss" scoped></style>
