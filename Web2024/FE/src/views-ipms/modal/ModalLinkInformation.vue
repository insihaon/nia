<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        링크정보조회
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <el-col :span="24">
          <DynamicComponentLoader
            class="dynamic-container"
            :component-keys="componentList"
            @handle-search="handleSearch"
          />
        </el-col>
        <el-col :span="24">
          <compTable
            :prop-data="tableDatas"
            :prop-table-height="300"
            :prop-column="tableColumns"
            :prop-is-pagination="false"
            :prop-is-check-box="false"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
            :prop-on-click="handleClickRow"
            :prop-on-dbl-click="handleDbClickRow"
          >
            <template slot="text-description">
              <span>
                링크정보 조회 결과
              </span>
            </template>
          </compTable>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-edit" @click="handleSelect()">선택</el-button>
        <el-button size="mini" type="info" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { onMessagePopup } from '@/utils/index'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import { linkTableDatas } from '@/views-ipms/menus/ipAllocationMng/sample.js'

const routeName = 'ModalLinkInformation'

export default {
  name: routeName,
  components: { DynamicComponentLoader, CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipBlockMstVo: null,
      requestParameter: null,
      selectedRow: null,
      componentList: [
         { key: 'SOffice', props: { label: '자국/대국 수용국', prop_parameterKey: 'sofficescodeSrch' } },
         { key: 'InputType', props: { label: '자국/대국 장비명', prop_parameterKey: 'snealiasSrch' } },
         { key: 'InputType', props: { label: '자국/대국 대표IP', prop_parameterKey: 'smstipSrch' } },
         { key: 'InputType', props: { label: '자국/대국 IF', prop_parameterKey: 'sifipSrch' } },
         { key: 'InputType', props: { label: '인터페이스 \n시리얼 IP', prop_parameterKey: 'pifSerialIpSrch' } },
         { key: 'InputType', props: { label: '전용번호', prop_parameterKey: 'sllnumSrch' } },
      ],
       tableColumns: [
        { prop: 'pifSerialIp', label: '인터페이스 시리얼 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saofficescodeNm', label: '자국 수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sanealias', label: '자국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'samstip', label: '자국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saifname', label: '자국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szofficescodeNm', label: '대국 수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sznealias', label: '대국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szmstip', label: '대국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szifname', label: '대국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: linkTableDatas
    }
  },
  /* row 참고
    {
      "sofficename": "가경국사",
      "ssubscnealias": "ACT-GAGYEONG",
      "ssubscmstip": "112.188.200.127",
      "smodelname": "LOC_E6100"
      "sofficecode": ""
    }
  */
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1400
    },
    onOpen(model, actionMode) {
      if (model.ipBlockMstVo) {
        this.ipBlockMstVo = model.ipBlockMstVo
      }
    },
    onClose() {
      if (this.selectedRow !== null) {
        this.$emit('selected-value', { selectedRow: this.selectedRow, returnFlag: 'allocLn' })
      }
    },
    handleSearch(requestParameter) {
      const { sofficescodeSrch, snealiasSrch, smstipSrch, sifipSrch, pifSerialIpSrch, sllnumSrch } = requestParameter
      const isCheckSearchLen = (snealiasSrch.length >= 4 || smstipSrch.length >= 4 || sifipSrch.length >= 4 || pifSerialIpSrch.length >= 4 || sllnumSrch.length >= 4)
      if (sofficescodeSrch === '' && !isCheckSearchLen) {
        onMessagePopup(this, '수용국을 선택하시지 않으실 경우 검색 값이 최소 4자리 이상이여야 합니다.')
        return
      }
      if (this.ipBlockMstVo) {
        const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ipBlockMstVo
        this._merge(requestParameter, { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd })
      }
      this.requestParameter = requestParameter
      this.onLoadLinkList()
    },
    async onLoadLinkList() {
      /*
        try {
          const res = await api(this.requestParameter)
          this.tableDatas = res.data
        } catch (error) {
          this.error(error)
        }
      */
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    handleClickRow(row) {
      this.selectedRow = row
    },
    handleDbClickRow(row) {
      this.selectedRow = row
      this.close()
    },
  },
}
</script>
<style lang="scss" scoped>
.dynamic-container ::v-deep {
  .optionItem {
    width: 50% !important;
    display: flex;
  }
}
</style>
