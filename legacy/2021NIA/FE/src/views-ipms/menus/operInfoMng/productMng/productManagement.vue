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
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            상품 조회결과
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
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'ProductManagement'

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
            { prop: '', label: 'SEQ', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
          {
            prop: '', label: '서비스 분류', children: [
              { prop: '', label: '대분류', align: 'center', columnVisible: true, showOverflow: true },
              { prop: '', label: '소분류', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            ],
            align: 'center',
            columnVisible: true,
            showOverflow: true,
          },
          { prop: '', label: '상품명(고정)', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '이용목적', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '외부연계', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'NeOSS상품코드', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '비고', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '수정일', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },

        ],
      componentList: [
        { key: 'NetworkClassification', props: { lvl: 3 } },
        { key: 'ApplyStatus', props: { label: '이용목적',
          prop_parameterKey: 'ssvcUseTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '사업용', value: 'SU0001' },
              { label: '일반용', value: 'SU0002' },
            ]
          }
        },
        { key: 'ExtrnLnkgs', props: {} },
        { key: 'ServiceOrg', props: { isMulti: false } },
        { key: 'IncludeYN', props: { } },
        { key: 'InputType', props: { label: '상품명' } },
      ]
    }
  },
  methods: {
    handleSearch(params) {
      console.log(params)
      /* const res = await api(params) */
    }
  }
}
</script>
<style lang="css" scoped>
</style>
