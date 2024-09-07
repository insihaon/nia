<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListUnAssignIP"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 미배정 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalNotAssignDetail ref="ModalNotAssignDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalNotAssignDetail from '@/views-ipms/modal/ModalNotAssignDetail.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
const routeName = 'IpunAllocatedStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalNotAssignDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableDatas: [],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 2 } },
      ]
    }
  },
  computed: {
    tableColumns() {
      const _THIS = this
      return [
        // { prop: 'nlvlMstSeq', label: '', align: 'center', sortable: true, columnVisible: false, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        // { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: false, showOverflow: true },
        { prop: 'nUnAssignBlockCnt', label: '미배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.nUnAssignBlockCnt > 0) {
              return this.$createElement('el-button', {
                on: { click: () => {
                  this.viewDetailCrtIPMst(row, '미배정')
                } } }, row.nUnAssignBlockCnt)
            } else {
              return this.$createElement('span', { class: 'txtred' }, row.nUnAssignBlockCnt)
            }
          }
        },
        { prop: 'nReserveAssignBlockCnt', label: '예비배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.nReserveAssignBlockCnt > 0) {
              return this.$createElement('el-button', {
                on: { click: () => {
                  this.viewDetailCrtIPMst(row, '예비배정')
                } } }, row.nReserveAssignBlockCnt)
            } else {
              return this.$createElement('span', { class: 'txtred' }, row.nReserveAssignBlockCnt)
            }
          }
        }
      ]
    }
  },
  mounted() {
    this.fnViewListUnAssignIP()
  },
  methods: {
    async fnViewListUnAssignIP(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListUnAssignIP, requestParameter)
        this.tableDatas = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    async viewDetailCrtIPMst(row, type) {
      const { sipVersionTypeCd, sassignLeveslCd, ssvcLineTypeNm, ssvcGroupNm } = row
      const nlvlMstSeq = type === '미배정' ? 'IA0001' : 'IA0002'

      try {
        const param = {
          nlvlMstSeq: nlvlMstSeq,
          sipVersionTypeCd: sipVersionTypeCd,
          sassignLeveslCd: sassignLeveslCd,
          ssvcLineTypeNm: ssvcLineTypeNm,
          ssvcGroupNm: ssvcGroupNm,
        }
        const res = await apiRequestModel(ipmsModelApis.viewDetailUnAssignIP, param)
        this.$refs.ModalNotAssignDetail.open({ row: res?.result.data, type: type })
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>
