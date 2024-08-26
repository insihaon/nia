<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListIpHostMst"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="handleDbClickRow"
      >
        <template slot="text-description">
          <span>
            운용정보(시설) 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" @click="handleClickEditBtn()">운용정보등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpHostMstInsert ref="ModalIpHostMstInsert" />
    <ModalIpHostMstDetail ref="ModalIpHostMstDetail" />
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
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '삭제', width: 80, align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              on: { click: () => {
                  this.fnDeleteHostIpMst(row)
                }
              }
            }, '삭제')
          }
        },
      ],
      tableDatas: [],
    }
  },
  methods: {
    async fnViewListIpHostMst(requestParameter) {
      const params = requestParameter ?? this.$refs.searchCondition.requestParameter
      Object.assign(params, { sipHostTypeCd: 'HT0010' })
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListIpHostMst, params)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
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
        type: 'success'
      }).then(async () => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteHostIPMst, { nipHostMstSeq: row.nipHostMstSeq })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
            this.fnViewListIpHostMst()
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
  },
}
</script>
<style lang="css" scoped></style>
