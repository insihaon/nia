<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
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
  watch: {
    $route: {
      handler: function(route) {
        if (this.ipms.toParams !== null) {
          const { value } = this.ipms.toParams
          if (value) {
            const compInfo = this.componentList.find(v => v.key === 'SsvcLineType')
            this.$set(compInfo.props, 'defaultValueLvl1', value.ssvcLineTypeCd)
            setTimeout(() => {
              const levelElement = this.$refs.searchCondition.$refs.SsvcLineType[0]
              // this.$set(levelElement.localValue, 1, value.ssvcLineTypeCd)
              this.$set(levelElement.localValue, 2, value.ssvcGroupCd)
              this.$store.dispatch('ipms/setToParam', null)
            }, 200)
            setTimeout(() => {
              this.fnViewListUnAssignIP()
            }, 250)
          }
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.fnViewListUnAssignIP()
  },
  methods: {
    async fnViewListUnAssignIP(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListUnAssignIP, parameter)
        this.tableDatas = res?.result.data
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
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
        this.error(error)
      }
    }
  }
}
</script>
