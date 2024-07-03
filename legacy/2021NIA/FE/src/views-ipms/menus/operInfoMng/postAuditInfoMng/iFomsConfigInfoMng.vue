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
            i-FOMS 중계 라우터 조회결과
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
const routeName = 'IFomsConfigInfoMng'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '수용국', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '장비명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'I/F IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'I/F 명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Description', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '작업일자', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'InputType', props: { label: '장비명', prop_parameterKey: 'shostNm' } },
        { key: 'InputType', props: { label: '장비 IP', prop_parameterKey: 'shostIp' } },
        { key: 'SOffice', props: {} },
        { key: 'InputType', props: { label: 'I/F명', prop_parameterKey: 'sifNm' } },
        { key: 'InputType', props: { label: 'I/F IP', prop_parameterKey: 'sifIp' } },
        { key: 'SortType', props: {
            sortTypeDefaultVal: 'SHOST_IP',
            label: '정렬', prop_options: [
              { label: '장비IP', value: 'SHOST_IP' },
              { label: 'I/F명', value: 'SIF_NM' },
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
