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
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP주소 라우팅 비교/점검
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

const routeName = 'IpAdressRoutingCompare'

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
        {
          key: 'SsvcLineType', props: { lvl: 3, defaultValueLvl1: 'CL0001',
          propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'ConditionByBlocksize', props: {} },
        { key: 'ServiceOrg', props: {} },
        { key: 'IpBlockStatus', props: { isMulti: true } },
        { key: 'SipCreateType', props: {} },
        { key: 'IpAddress', props: { isShowSelecteBox: false } },
        { key: 'InputType', props: { prop_parameterKey: 'nbitmask', label: 'BitMask' } },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'IP', value: 'PIP_PREFIX' },
              { label: 'BitMask', value: 'NBITMASK' }
            ]
          }
        },
        { key: 'Progress', props: {} },
        { key: 'ExceptionYN', props: {} }
      ],
      tableColumns: [
        { prop: '', label: '구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
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
