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
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template v-if="!isDashboard" slot="text-description">
          <span>
            NeOSS 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalOrderAssignInfomation ref="ModalOrderAssignInfomation" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalOrderAssignInfomation from '@/views-ipms/modal/ModalOrderAssignInfomation.vue'

const routeName = 'NeOssOrder'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalOrderAssignInfomation },
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
        { key: 'LineInformation', props: { label: '오더정보', isAllOption: true }
        },
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
        { key: 'ApplyStatus', props: { label: '이용목적',
          prop_parameterKey: 'ssvcUseTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: 'N/A', value: 'SU0000' },
              { label: '사업용', value: 'SU0001' },
              { label: '일반용', value: 'SU0002' },
            ]
          }
        },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: 'SODRREGDT',
            prop_options: [
              { label: '접수일', value: 'SODRREGDT' },
              { label: '희망일', value: 'SODRHOPEDT' },
              { label: '고객명', value: 'SCUSTNAME' },
            ]
          }
        }
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
        { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              props: {
                size: 'mini'
              },
              on: { click: () => {
                  this.$refs.ModalOrderAssignInfomation.open({ row })
              } } }, '할당')
          }
        },
      ],
      tableDatas: [{ }]
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
