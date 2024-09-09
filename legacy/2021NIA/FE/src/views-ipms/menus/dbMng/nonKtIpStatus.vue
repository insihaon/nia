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
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            Non-KT IP 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'nonKtIpStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SOffice', props: { prop_parameterKey: 'sicisofficescode', label: '노드국' } },
        { key: 'IpMngProgress', props: {} },
        { key: 'InputType', props: { prop_parameterKey: 'sipBlock', label: 'IP정보' } },
        { key: 'InputType', props: { prop_parameterKey: 'scustname', label: '고객명' } },
        { key: 'InputType', props: { prop_parameterKey: 'sasNum', label: 'AS번호' } },
        { key: 'LineInformation', props: { defaultValue: 'said',
          prop_parameterKey: 'searchCnd',
          prop_textFixKey: 'searchWrd',
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
            }],
          }
        },
      ],
      tableColumns: [
        { prop: '', label: '노드국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'BGP영문고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상품명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'AS번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="css" scoped></style>
