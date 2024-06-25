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
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            계위코드 조회결과
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

const routeName = 'RankCodeManagement'

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
        { prop: '', label: '코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '계위명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '구분코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '외부연동 유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'ExtrnLnkgs', props: { label: '외부연동유형' } },
        { key: 'InputType', props: { label: '계위명', prop_parameterKey: 'searchWrd' } },
        { key: 'InputType', props: { label: '코드명', prop_parameterKey: 'slvlCd' } },
      ],
      sexLinkUseTypeCd: '',
      searchWrd: '',
      slvlCd: ''
    }
  },
  methods: {
    handleSearch(params) {
      console.log(params)
      /*
      const res = await api(params)
      */
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
