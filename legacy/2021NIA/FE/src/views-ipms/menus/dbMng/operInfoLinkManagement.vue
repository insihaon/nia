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
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="handleDbClickRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            운용정보(링크) 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" @click="handleClickEditBtn('insert')">운용정보등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpLinkMstInsert ref="ModalIpLinkMstInsert" @reload="fnViewListIpLinkMst()" />
    <ModalIpLinkMstDetail ref="ModalIpLinkMstDetail" @reload="fnViewListIpLinkMst()" @reOpenUpdate="(param) => handleClickEditBtn('update', param)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalIpLinkMstInsert from '@/views-ipms/modal/ModalIpLinkMstInsert.vue'
import ModalIpLinkMstDetail from '@/views-ipms/modal/ModalIpLinkMstDetail.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'OperInfoLinkManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpLinkMstInsert, ModalIpLinkMstDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'pifSerialIp', label: '링크IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saofficescodeNm', label: '자국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sanealias', label: '자국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'samstip', label: '자국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saifname', label: '자국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szofficescodeNm', label: '대국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szofficescode', label: '대국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szmstip', label: '대국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szifname', label: '대국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssaid', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sconnalias', label: '수용회선명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row?.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD') : '' } },
        { prop: 'del', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
            on: { click: () => {
                this.fnDeleteLinkIpMst(row)
              }
          } }, '삭제')
}
        },
      ],
      tableDatas: [],
      // sOfficeOptions: []
    }
  },
  computed: {
    componentList() {
      return [
        { key: 'InputType', props: { prop_parameterKey: 'searchWrd', label: '링크IP블록', valueType: 'ip' } },
        { key: 'SOffice', props: { prop_parameterKey: 'srssofficescode', apiPath: '/ipmgmt/linkmgmt', voName: 'tbIpLinkMstVos', valueKey: { cd: 'srssofficescode', nm: 'srssofficesNm' } } },
        { key: 'InputType', props: { prop_parameterKey: 'smodelNm', label: '장비명' } },
        { key: 'InputType', props: { prop_parameterKey: 'sifNm', label: 'IF명' } },
        { key: 'LineInformation', props: {} },
        { key: 'InputType', props: { prop_parameterKey: 'sconnalias', label: '수용회선명' } },
        { key: 'SortType', props: { } },
      ]
    }
  },
  mounted () {
    setTimeout(() => {
      this.fnViewListIpLinkMst()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpLinkMst(requestParameter)
    },
    async fnViewListIpLinkMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpLinkMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
          this.closeLoading(target)
        }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListIpLinkMst()
    },
    handleDbClickRow(row) {
      this.$refs.ModalIpLinkMstDetail.open({ nipLinkMstSeq: row.nipLinkMstSeq })
    },
    handleClickEditBtn(fnType, nipLinkMstSeq = null) {
      const param = { fnType, nipLinkMstSeq }
      fnType === 'insert' && Object.assign(param, { dataReset: true })
      this.$refs.ModalIpLinkMstInsert.open(param)
    },
    async fnDeleteLinkIpMst(row) {
      if (row.allocCnt > 0) {
        onMessagePopup(this, `해당 정보로 할당 테이블에 ${row.allocCnt}건이 링크 할당이 되어 있으므로 삭제 불가합니다.`)
        return
      }
      this.confirm('해당 운용정보를 삭제 하시겠습니까?', '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'success'
        }).then(async () => {
          try {
            const res = await apiRequestJson(ipmsJsonApis.deleteLinkIpMst, { nipLinkMstSeq: row.nipLinkMstSeq })
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
              this.fnViewListIpLinkMst()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
        .catch(action => {
        })
    }
  }
}
</script>
<style lang="scss" scoped></style>
