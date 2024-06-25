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
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            요구사항 조회결과
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

const routeName = 'IssueDataRequest'

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
        { prop: '', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '요청사항구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '희망완료일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '완료예정일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '중요도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'ApplyStatus', props: {
          label: '요청사항 구분',
          prop_parameterKey: 'RboardDivision',
          prop_options: [
              { label: '전체', value: '' },
              { label: '오류 버그 수정', value: 'RES001' },
              { label: '기능 개발 요청', value: 'RES002' },
              { label: '자료 요청', value: 'RES003' },
              { label: '연동 요청', value: 'RES004' },
            ]
          }
        },
        { key: 'BoardSearchCondition', props: {} },
        {
          key: 'ApplyStatus', props: {
          label: '진행상태',
          prop_parameterKey: 'RboardProgress',
          prop_options: [
              { label: '전체', value: '' },
              { label: '요청사항 접수', value: 'RES005' },
              { label: '접수 반려', value: 'RES006' },
              { label: '조치 진행 중', value: 'RES006' },
              { label: '조치 완료', value: 'RES007' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '등록기간' } },
      ],
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
