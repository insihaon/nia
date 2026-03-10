<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-row ref="tableContainer" :gutter="20" style="height: calc(100% - 260px)">
      <el-col class="h-100" :span="12">
        <compTable
          ref="seonbeonjangTable"
          :prop-name="name"
          :prop-table-height="'calc(100% - 80px)'"
          :prop-data="tableDatas"
          :prop-column="tableColumns"
          :prop-is-pagination="false"
          :prop-is-check-box="true"
          prop-grid-menu-id="seonbeonjang"
          :prop-grid-indx="1"
        >
          <template slot="text-description">
            <span>
              IP 선번장 조회결과
            </span>
          </template>
        </compTable>
      </el-col>
      <el-col class="h-100" :span="12">
        <el-collapse v-model="activeNames">
          <el-collapse-item title="HOST 관리" name="1">
            <DynamicComponentLoader
              ref="hostSearch"
              class="hostSearchContainer"
              :component-keys="hostSearchComponentList"
              @handle-search="handleSearchHost"
            />
          </el-collapse-item>
        </el-collapse>
        <compTable
          v-if="tableDatas.length > 0"
          ref="hostTable"
          :prop-name="name"
          :prop-table-height="'calc(100% - 80px)'"
          :prop-data="hostTableDatas"
          :prop-column="hostTableColumns"
          :prop-is-pagination="false"
          :prop-is-check-box="false"
          prop-grid-menu-id="host"
          :prop-grid-indx="1"
          :prop-on-click="handleClickHostRow"
          :style="{height: activeNames.length > 0 ? 'calc(100% - 241px)' : 'calc(100% - 50px)'}"
        >
          <template slot="text-description">
            <span>
              IP HOST 정보
            </span>
          </template>
        </compTable>
      </el-col>
    </el-row>
    <ModalIpBlockDivision ref="ModalIpBlockDivision" />
    <ModalHostInfoDetail ref="ModalHostInfoDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalIpBlockDivision from '@/views-ipms/modal/ModalIpBlockDivision.vue'
import ModalHostInfoDetail from '@/views-ipms/modal/ModalHostInfoDetail.vue'
// import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'IpSeonbeonjang'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalHostInfoDetail },
  extends: Base,
  // mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      activeNames: [],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { isMulti: false } },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask' } },
        {
          key: 'LineInformation', props: {
            isAllOption: true, defaultValue: '',
            prop_options: [
              {
                value: 'llnum',
                label: '전용번호',
                txtKey: 'sllnum'
              },
              {
                value: 'said',
                label: 'SAID',
                txtKey: 'ssaid'
            }]
          }
        },
        { key: 'InputType', props: { label: '용도', prop_parameterKey: '' } },
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
        { key: 'SortType', props: {} },
      ],
      hostSearchComponentList: [
        { key: 'InputType', props: { label: 'IP주소', prop_parameterKey: '' } },
        { key: 'SOffice', props: {} },
        { key: 'InputType', props: { label: '용도', prop_parameterKey: '' } },
        { key: 'InputType', props: { label: '장비명', prop_parameterKey: '' } },
        { key: 'InputType', props: { label: 'I/F명', prop_parameterKey: '' } },
        { key: 'InputType', props: { label: '모델명', prop_parameterKey: '' } },
      ],
      tableColumns: [
        { prop: 'gubun', label: '구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'service', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ipBlock', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              on: { click: () => {
                this.$refs.ModalIpBlockDivision.open({ row, isSeonbeonjang: true })
            } } }, '분할')
          }
        },
      ],
      hostTableColumns: [
        { prop: 'srssofficesNm', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipHostInet', label: 'Host IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'scomment', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipIfNm', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipHostNm', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true }

      ],
      tableDatas: [
        { gubun: '공인', service: 'POOL-M2M', ipBlock: '1.100.0.0/17' },
        { allocationStatus: 'N' },
      ],
      hostTableDatas: [
        { srssofficesNm: 'DATA망(혜화)', pipHostInet: '1.102.3.1', scomment: 'linux백신업데이트', sipIfNm: '', sipHostNm: '리눅스업데이트서버' },
      ]
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
    handleSearchHost(requestParameter) {
      console.log(requestParameter)
    },
    handleClickHostRow(row) {
      this.$refs.ModalHostInfoDetail.open(row)
    }
  },
}
</script>
<style lang="scss" scoped>
.hostSearchContainer ::v-deep {
  .optionRow .optionItem {
    width: 50% !important;
  }
}
</style>
