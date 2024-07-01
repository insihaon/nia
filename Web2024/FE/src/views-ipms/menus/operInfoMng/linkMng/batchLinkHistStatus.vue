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
            배치 연동 이력 조회결과
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
const routeName = 'BatchLinkHistStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '인터페이스 ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '관련 시스템명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '관련 테이블명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '작업내역', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '작업내역 상세', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '작업 종료여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'DATA 입력 시작시각', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'DATA 입력 종료시각', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'InputType', props: { label: '인터페이스 ID' } },
        { key: 'IncludeYN', props: { label: '작업 종료여부' } },
        { key: 'DateRange', props: { label: 'DATA 입력 \n시작/종료시각' } }
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
