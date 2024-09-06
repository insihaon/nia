<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            권한메뉴관계 정보 조회결과
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

const routeName = 'MenuAuthManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '메뉴명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '메뉴 ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면 URL', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '메뉴권한 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
      { key: 'AuthLevel', props: { isAllOption: false, defaultValue: 'UR0001' } },
      { key: 'BoardSearchCondition', props: { defaultValue: 'smenuNm', prop_options: [
        { label: '메뉴명', value: 'smenuNm' },
        { label: '메뉴ID', value: 'smenuId' },
        ] }
      }],
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
