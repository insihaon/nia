<template>
  <el-row class="w-100 h-100" :class="{'px-12': !isDashboard}">
    <DynamicComponentLoader
      ref="DynamicComponent"
      :component-keys="componentList"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
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

const routeName = 'IpAdressRoutingCompare'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  props: {
    isDashboard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        {
          key: 'SsvcLineType', props: { lvl: 3, propsValueLvl1: 'CL0001',
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
        { key: 'IpBlockStatus', props: {} },
        { key: 'SipCreateType', props: {} },
        // IP주소(input type)
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: '',
            propsOptions: [
              { label: '전체', value: '' },
              { label: 'IP', value: 'PIP_PREFIX' },
              { label: 'BitMask', value: 'NBITMASK' }
            ]
          }
        },
        { key: 'Progress', props: {} },
        { key: 'exceptionYN', props: {} }
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
}
</script>
<style lang="css" scoped></style>
