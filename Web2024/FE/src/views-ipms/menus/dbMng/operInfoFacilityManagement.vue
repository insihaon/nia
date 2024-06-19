<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="DynamicComponent"
      :component-keys="componentList"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            운용정보(시설) 조회결과
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

const routeName = 'operInfoFacilityManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'SOffice', props: {} },
        { key: 'SipCreateType', props: {} },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { propsParameterKey: 'smodelname', label: '모델명' } },
        { key: 'InputType', props: { propsParameterKey: 'sipHostNm', label: '장비명' } },
        { key: 'IncludeYN', props: { parameterKey: 'sprorityYn', label: '대표여부' } },
        { key: 'InputType', props: { propsParameterKey: 'scomment', label: '용도' } },
        { key: 'SortType', props: { } },
      ],
      tableColumns: [
        { prop: '', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Host IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '모델명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '대표여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
