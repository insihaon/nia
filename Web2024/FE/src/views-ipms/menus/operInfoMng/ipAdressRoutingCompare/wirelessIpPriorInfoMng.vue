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
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            연동 이력 조회결과
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
const routeName = 'WirelessIpPriorInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Community', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'IP 블록', align: 'center', columnVisible: true, showOverflow: true }
        ],
      componentList: [
        { key: 'ApplyStatus', props: { label: '구분', prop_parameterKey: 'skindCd',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Community', value: 'COMMU' },
              { label: 'No-Community', value: 'NOCOMMU' },
            ]
          }
        },
        { key: 'InputType', props: { label: 'Community/IP블록', prop_parameterKey: 'scommu' } },
        { key: 'InputType', props: { label: '서비스', prop_parameterKey: 'sserviceNm' } },
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
