<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListWireMst"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
        @update:propSelected="handleClickCheck"
      >
        <template slot="text-description">
          <span>
            유선IP 사전 정보관리 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-plus" @click="handleClickRegist()">Community 등록</el-button>
            <el-button size="mini" @click="fnDeleteWireMst()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalWireMstInsert ref="ModalWireMstInsert" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalWireMstInsert from '@/views-ipms/modal/interlink/ModalWireMstInsert.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'WiredIpPriorInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalWireMstInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'skindCd', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'skindCd', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'scommunity', label: 'Community', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'snexthop', label: 'NextHop', align: 'center', columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'ApplyStatus', props: { label: '구분', prop_parameterKey: 'skindCd',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'KORNET', value: 'KORNET' },
              { label: 'PREMIUM', value: 'PREMIUM' }
            ]
          }
        },
        { key: 'InputType', props: { label: 'Community', prop_parameterKey: 'scommunity' } },
        { key: 'InputType', props: { label: 'NextHop', prop_parameterKey: 'snexthop' } },
      ],
      tableDatas: [],
      delRows: null
    }
  },
  mounted () {
    this.fnViewListWireMst()
  },
  methods: {
    async fnViewListWireMst(requestParameter) {
      const params = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListWireMst, params)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    handleClickCheck(rows) {
      this.delRows = rows
    },
    handleClickCell(row) {
      this.$refs.ModalWireMstInsert.open({ viewType: 'wire', fnType: 'update', row })
    },
    handleClickRegist() {
      this.$refs.ModalWireMstInsert.open({ viewType: 'wire', fnType: 'insert' })
    },
    async fnDeleteWireMst() {
      if (this.delRows === null || this.delRows.length === 0) {
        onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
      }
      const delList = []
      this.delRows.forEach(row => { delList.push(row.nwireIpCommuSeq) })
      this.confirm('해당 유선IP 사전 정보를 정말 삭제하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteWireMst, { delList })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '유선IP Community 정보 삭제가 정상적으로 처리되었습니다.')
            this.delRows = null
            this.fnViewListWireMst()
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
