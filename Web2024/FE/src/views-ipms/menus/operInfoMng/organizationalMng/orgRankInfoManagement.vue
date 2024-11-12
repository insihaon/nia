<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: 100%"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            조직계위정보 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewInsertLvlBas()">조직계위등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalLvlBasInsert ref="ModalLvlBasInsert" @reload="fnViewListLvlBas" />
    <ModalUpdateLvlBas ref="ModalUpdateLvlBas" @reload="fnViewListLvlBas" />
    <ModalInsertLvlSonMgmt ref="ModalInsertLvlSonMgmt" @reload="fnViewListLvlBas" />
    <ModalInsertLvlRoleSub ref="ModalInsertLvlRoleSub" @reload="fnViewListLvlBas" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalLvlBasInsert from '@/views-ipms/modal/orgmgt/ModalLvlBasInsert.vue'
import ModalUpdateLvlBas from '@/views-ipms/modal/orgmgt/ModalUpdateLvlBas.vue'
import ModalInsertLvlSonMgmt from '@/views-ipms/modal/orgmgt/ModalInsertLvlSonMgmt.vue'
import ModalInsertLvlRoleSub from '@/views-ipms/modal/orgmgt/ModalInsertLvlRoleSub.vue'

const routeName = 'OrgRankInfoManagement'

export default {
  name: routeName,
  components: { CompTable,
                DynamicComponentLoader,
                ModalLvlBasInsert,
                ModalUpdateLvlBas,
                ModalInsertLvlSonMgmt,
                ModalInsertLvlRoleSub },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvchighObjNm', label: '주노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slvlBasLevelCd', label: '계위관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
           formatter: (row, col, value, index) => {
            if (row.slvlBasLevelCd === 'LL0003') {
              return this.$createElement('el-button', {
                attrs: {
                  round: true,
                  plain: true,
                  type: 'primary',
                  size: 'mini'
                },
                on: { click: () => {
                  this.fnViewUpdateLvlBas(row)
                } } }, '관리')
            } else {
              return this.$createElement('span', '관리')
            }
          }
         },
        {
          prop: 'son', label: '오더계위(SON)', children: [
            { prop: 'orderCnt', label: '국사수', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
               formatter: (row, col, value, index) => {
                if (row.slvlBasLevelCd === 'LL0003') {
                  return this.$createElement('el-button', {
                    attrs: {
                      round: true, // Adding the round option
                      plain: true,
                      type: 'primary',
                      size: 'mini'
                    },
                    on: { click: () => {
                      this.fnViewInsertLvlSon(row)
                    } } }, '관리')
                } else {
                  return this.$createElement('span', '관리')
                }
              }
            },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: 'fm', label: '시설계위(FM)', children: [
            { prop: 'fmCnt', label: '국사수', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
              formatter: (row, col, value, index) => {
                if (row.slvlBasLevelCd === 'LL0003') {
                  return this.$createElement('el-button', {
                    attrs: {
                      round: true,
                      plain: true,
                      type: 'primary',
                      size: 'mini'
                    },
                    on: { click: () => {
                      this.fnViewInsertLvlRoleSub(row)
                    } } }, '관리')
                } else {
                  return this.$createElement('span', '관리')
                }
              }
             },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        { prop: 'dcreateDt', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dcreateDt ? this.moment(row.dcreateDt).format('YYYY-MM-DD') : '' } },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 2 } },
        { key: 'InputType', props: { label: '노드명', prop_parameterKey: 'ssvcObjNm' } },
      ],
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListLvlBas()
    })
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListLvlBas(requestParameter)
    },
    async fnViewListLvlBas(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListLvlBas, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListLvlBas()
    },
    fnViewInsertLvlBas() { /* 조직계위 등록 */
     this.$refs.ModalLvlBasInsert.open()
    },
    fnViewUpdateLvlBas(row) { /* 계위관리 */
     this.$refs.ModalUpdateLvlBas.open({ row /* legacy :  res?.result?.nodeListVo */ })
    },
    async fnViewInsertLvlSon(row) { /* 오더계위 관리 */
      this.$refs.ModalInsertLvlSonMgmt.open({ row })
    },
    async fnViewInsertLvlRoleSub(row) { /* 시설계위 관리 */
    this.$refs.ModalInsertLvlRoleSub.open({ row })
    }
  },
}
</script>
<style lang="css" scoped>
</style>
