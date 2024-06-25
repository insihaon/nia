<template>
  <!-- 검색 조건 컴포넌트 추가 -->
  <el-row ref="container" class="w-100 h-100" :class="{'px-0': isDashboard}">
    <DynamicComponentLoader
      v-if="!isDashboard"
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="isDashboard ? 300 :'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            NeOSS 조회결과
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
const routeName = 'NeOssOrder'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  props: {
    isDashboard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
        // 오더정보 (select + input)
        { key: 'InputType', props: { label: '고객명', prop_parameterKey: 'scustname' } },
        { key: 'DateRange', props: { label: '접수일', prop_parameterKey: ['searchRecpBgnDe', 'searchRecpEndDe'] } },
        { key: 'DateRange', props: { label: '희망일', prop_parameterKey: ['searchInstBgnDe', 'searchInstEndDe'] } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '상품',
            modalName: 'ModalProductInformation',
            // * key 확인 필요함
            valueName: 'A',
            prop_parameterKey: { sexSvcCd: 'B', sipmsSvcNm: 'C' },
            isReadOnly: true
          }
        },
        // 이용목적
        { key: 'SortType', props: { } }
      ],
      tableColumns: [
        { prop: '', label: '노드국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '오더번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '접수일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '희망일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상품명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '이용목적', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
<style lang="scss" scoped></style>
