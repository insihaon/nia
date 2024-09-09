<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListFcltMst"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-data="tableDatas"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-is-cell-click-check="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
        @update:propSelected="handleClickCheck"
      >
        <template slot="text-description">
          <span>
            조직별 장비 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-plus" @click="handleClickProcessBtn('insert')">신규생성</el-button>
            <!-- <el-button size="mini" @click="handleClickProcessBtn('update')">수정</el-button> -->
            <el-button size="mini" @click="fnDeleteFcltMst()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalFcltMstInsert ref="ModalFcltMstInsert" @reload="fnViewListFcltMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalFcltMstInsert from '@/views-ipms/modal/interlink/ModalFcltMstInsert'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'EquipInfoManagementByOrg'

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
      tableColumns: [
          { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true, },
          { prop: 'ssvcGroupNm', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'ssvcObjNm', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'ptelnetIp', label: 'IP주소', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'nportFclt', label: 'PORT', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'pipUpperFclt', label: '상위장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'pipPeerFclt', label: 'PEER장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'shostNm', label: '프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sfcltModelNm', label: '모델명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sfcltType', label: '타입', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'suseYn', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3,
            propsLvlOptions: { defaultValueLvl1: 'ALL',
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'InputType', props: { label: '장비타입', prop_parameterKey: 'sfcltType' } },
        { key: 'UsageYN', props: { label: '사용여부', prop_parameterKey: 'suseYn',
           defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Y', value: 'Y' },
              { label: 'N', value: 'N' },
            ]
          }
        }
      ],
      // selectedRow: null,
      tableDatas: [],
      delRows: []
    }
  },
  mounted () {
    this.fnViewListFcltMst()
  },
  methods: {
    async fnViewListFcltMst(requestParameter) {
      const params = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListFcltMst, params)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    handleClickCell(row) {
      // this.selectedRow = row
      this.$refs.ModalFcltMstInsert.open({ viewType: 'ipRouting', fnType: 'update', row })
    },
    handleClickCheck(rows) {
      this.delRows = rows
    },
    handleClickProcessBtn(type) {
      const params = { viewType: 'ipRouting', fnType: type }
      // if (type === 'update') {
      //   Object.assign(params, { row: this.selectedRow })
      // }
      this.$refs.ModalFcltMstInsert.open(params)
    },
    async fnDeleteFcltMst() {
      const delRows = this.delRows
      if (delRows.length === 0) {
        onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
        return
      }
      const delList = []
      delRows.forEach(row => { delList.push(row.nfcltMstSeq) })
      this.confirm('해당 계위의 등록장비 정보를 정말 삭제하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteFcltMst, { delList })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '조직 장비 삭제가 정상적으로 처리되었습니다.')
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
