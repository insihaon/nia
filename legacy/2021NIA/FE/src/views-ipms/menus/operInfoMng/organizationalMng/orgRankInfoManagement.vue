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
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            조직계위정보 조회결과
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

const routeName = 'OrgRankInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '서비스방', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '주노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '계위관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        {
          prop: 'son'/* 컬럼 설정시 필요함 빈 값으로 두지마세요 */, label: '오더계위(SON)', children: [
            { prop: '', label: '국사수', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: 'fm'/* 컬럼 설정시 필요함 빈 값으로 두지마세요 */, label: '시설계위(FM)', children: [
            { prop: '', label: '국사수', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        { prop: '', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 2 } },
        { key: 'InputType', props: { label: '노드명', prop_parameterKey: 'ssvcObjNm' } },
      ]
    }
  },
  methods: {
    handleSearch(params) {
      console.log(params)
      /* const res = await api(params) */
    }
  },
}
</script>
<style lang="css" scoped>
</style>
