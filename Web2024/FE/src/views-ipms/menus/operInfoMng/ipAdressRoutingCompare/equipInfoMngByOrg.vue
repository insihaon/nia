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
            조직별 장비 조회결과
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
const routeName = 'EquipInfoManagementByOrg'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true, },
          { prop: '', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'IP주소', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'PORT', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '상위장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'PEER장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '모델명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '타입', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, defaultValueLvl1: 'ALL',
            propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'InputType', props: { label: '장비타입', prop_parameterKey: 'sfcltType' } },
        { key: 'UsageYN', props: { label: '사용여부', prop_parameterKey: 'suseYn',
           defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Y', value: 'Y' },
              { label: 'N', value: 'N' },
            ]
          }
        }
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
