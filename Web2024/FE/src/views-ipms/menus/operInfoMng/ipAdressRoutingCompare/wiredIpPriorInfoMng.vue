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
            무선IP 사전 정보관리 조회결과
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
const routeName = 'WiredIpPriorInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Community', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'NextHop', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'ApplyStatus', props: { label: '구분', prop_parameterKey: 'skindCd',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'KORNET', value: 'KORNET' },
              { label: 'PREMIUM', value: 'PREMIUM' }
            ]
          }
        },
        { key: 'InputType', props: { label: 'Community', prop_parameterKey: 'scommunity' } },
        { key: 'InputType', props: { label: 'NextHop', prop_parameterKey: 'snexthop' } },
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
