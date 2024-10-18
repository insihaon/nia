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
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-is-cell-click-check="true"
        :prop-max-select="1"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            조직별 장비 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-plus" @click="handleClickProcessBtn('insert')">신규생성</el-button>
            <el-button size="mini" @click="handleClickProcessBtn('update')">수정</el-button>
            <el-button size="mini" @click="fnDeleteTacsFcltMst()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalFcltMstInsert ref="ModalFcltMstInsert" @reload="fnViewListTacsFcltMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalFcltMstInsert from '@/views-ipms/modal/interlink/ModalFcltMstInsert'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'TacsEquipInfoManagementByOrg'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalFcltMstInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true, },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'pipFcltInet', label: 'IP주소', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'nportFclt', label: 'PORT', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sfcltPromptNm', label: '프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sfcltModelNm', label: '모델명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sfcltType', label: '타입', align: 'center', columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, defaultValueLvl1: '',
          propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'InputType', props: { label: '장비타입', prop_parameterKey: 'sfcltType' } }
      ],
      selectedRow: null,
    }
  },
  mounted () {
    setTimeout(() => {
      this.fnViewListTacsFcltMst()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListTacsFcltMst(requestParameter)
    },
    async fnViewListTacsFcltMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTacsFcltMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
        setTimeout(() => {
          this.$refs?.compTable?.$refs?.table?.selection.push(this.tableDatas[0])
          this.selectedRow = this.pagination.data[0]
        }, 10)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListTacsFcltMst()
    },
    handleClickCell(row) {
      this.selectedRow = row
      this.$refs.ModalFcltMstInsert.open({ viewType: 'tacsMng', fnType: 'update', row: this.selectedRow })
    },
    handleClickProcessBtn(type) {
      const params = { viewType: 'tacsMng', fnType: type }
      if (type === 'update') {
        Object.assign(params, { row: this.selectedRow })
      }
      this.$refs.ModalFcltMstInsert.open(params)
    },
    async fnDeleteTacsFcltMst() {
      const delRow = this.selectedRow
      if (delRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      if (delRow.ntacsFcltMstSeq !== null && delRow.ntacsFcltMstSeq !== '') {
        this.confirm('해당 계위의 등록장비 정보를 정말 삭제하시겠습니까?', '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warnning'
        }).then(async () => {
          try {
            const res = await apiRequestJson(ipmsJsonApis.deleteTacsFcltMst, { ntacsFcltMstSeq: delRow.ntacsFcltMstSeq })
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '조직 장비 삭제가 정상적으로 처리되었습니다.')
              this.fnViewListTacsFcltMst()
            }
          } catch (error) {
            this.error(error)
          }
        })
      } else {
        onMessagePopup(this, '해당 계위에는 등록된 장비 내역이 없습니다.')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
