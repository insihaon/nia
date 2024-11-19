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
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
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
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            운용정보(시설) 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="handleClickEditBtn()">운용정보등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpHostMstInsert ref="ModalIpHostMstInsert" />
    <ModalIpHostMstDetail ref="ModalIpHostMstDetail" @reload="fnViewListIpHostMst()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalIpHostMstInsert from '@/views-ipms/modal/ModalIpHostMstInsert.vue'
import ModalIpHostMstDetail from '@/views-ipms/modal/ModalIpHostMstDetail.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'operInfoFacilityManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpHostMstInsert, ModalIpHostMstDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'SOffice', props: { prop_parameterKey: 'srssofficescode', apiPath: '/ipmgmt/hostmgmt', voName: 'tbIpHostMstVos', valueKey: { cd: 'srssofficescode', nm: 'srssofficesNm' } } },
        { key: 'SipCreateType', props: { isAllOption: true } },
        { key: 'IpAddress', props: { isAllOption: true, defaultValue: '' } },
        { key: 'InputType', props: { prop_parameterKey: 'smodelname', label: '모델명' } },
        { key: 'InputType', props: { prop_parameterKey: 'sipHostNm', label: '장비명' } },
        { key: 'IncludeYN', props: { prop_parameterKey: 'sprorityYn', label: '대표여부' } },
        { key: 'InputType', props: { prop_parameterKey: 'scomment', label: '용도' } },
        { key: 'SortType', props: { } },
      ],
      tableColumns: [
        { prop: 'srssofficesNm', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipHostInet', label: 'Host IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipHostNm', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smodelname', label: '모델명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'scomment', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sprorityYn', label: '대표여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => this.moment(row.dmodifyDt).format('YYYY-MM-DD') },
        { prop: '', label: '삭제', width: 80, align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: 'primary',
                size: 'mini'
              },
              on: { click: () => {
                  this.fnDeleteHostIpMst(row)
                }
              }
            }, '삭제')
          }
        },
      ],
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListIpHostMst()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpHostMst(requestParameter)
    },
    async fnViewListIpHostMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      Object.assign(parameter, { sipHostTypeCd: 'HT0010' })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const target = ({ vue: this.$refs.compTable })
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpHostMst, parameter)
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
      this.fnViewListIpHostMst()
    },
    handleDbClickRow(row) {
      this.$refs.ModalIpHostMstDetail.open({ fnType: 'detail', nipHostMstSeq: row.nipHostMstSeq })
    },
    handleClickEditBtn() {
      this.$refs.ModalIpHostMstInsert.open()
    },
    async fnDeleteHostIpMst(row) {
      this.confirm('해당 운용정보를 삭제 하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'info'
      }).then(async () => {
        const target = ({ vue: this.$refs.compTable })
        try {
          this.openLoading(target)
          const res = await apiRequestJson(ipmsJsonApis.deleteHostIPMst, { nipHostMstSeq: row.nipHostMstSeq })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
            this.fnViewListIpHostMst()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        } finally {
          this.closeLoading(target)
        }
      })
      .catch(action => {
      })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListVirtualHostInfoExcel')
    }
  },
}
</script>
<style lang="css" scoped></style>
