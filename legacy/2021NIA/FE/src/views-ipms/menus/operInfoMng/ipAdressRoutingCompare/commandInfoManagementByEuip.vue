<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            장비별 명령어 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'CommandInfoManagementByEquip'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '장비타입', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '장비명령어', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '할당판단문구', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '할당가능여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '명령어순서', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'EquipmentType', props: {
          prop_parameterKey: 'sfcltType',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'ACCESS-UBIQUOSS_TEST', value: 'ACCESS-UBIQUOSS_TEST' },
              { label: 'KORNET-CISCO_CRS', value: 'KORNET-CISCO_CRS' },
              { label: 'KORNET-JUNIPER', value: 'KORNET-JUNIPER' },
              { label: 'KORNET_CENTER-CISCO_CRS', value: 'KORNET_CENTER-CISCO_CRS' },
              { label: 'KORNET_CENTER-NOKIA_7950XRS', value: 'KORNET_CENTER-NOKIA_7950XRS' },
              { label: 'KORNET_GHT-JUNIPER', value: 'KORNET_GHT-JUNIPER' },
            ]
          }
        },
        { key: 'InputType', props: { label: '장비명령어' } },
        { key: 'UsageYN', props: { label: '사용여부', prop_parameterKey: 'suseYn' } }
      ]
    }
  },
  methods: {
      handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="css" scoped>
</style>
