<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
      >
        <template slot="text-description">
          <span>
            NEOSS 연동 오류 현황
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
const routeName = 'NeossLinkErrorStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: 'IP블록', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '비트마스크', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'LacpBlock여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '전용회선번호', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'ssaid', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '조치여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '날짜', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        {
          key: 'ApplyStatus', props: {
            label: '조치여부',
            defaultValue: '',
            prop_parameterKey: 'errorResultCd',
            prop_options: [
              { label: '전체', value: '' },
              { label: '미조치', value: 'NE0001' },
              { label: '조치', value: 'NE0002' },
            ]
          }
        },
        {
          key: 'BoardSearchCondition', props: {
            defaultValue: 'ipBlock',
            prop_options: [
              { label: 'IP블럭', value: 'ipBlock' },
              { label: '전용회선번호', value: 'sllnum' },
            ]
          }
        },
        {
          key: 'SortType', props: { label: '정렬조건', sortTypeDefaultVal: 'dcreate_dt',
            prop_options: [
              { label: '신청일', value: 'dcreate_dt' },
              { label: '신청번호', value: 'seq' },
            ]
          }
        },
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
