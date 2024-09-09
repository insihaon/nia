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
        시설정보조회
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <el-col :span="24">
          <DynamicComponentLoader
            class="dynamic-container"
            :component-keys="componentList"
            :is-show-profile="false"
            @handle-search="handleSearch"
          />
        </el-col>
        <el-col :span="24">
          <compTable
            ref="compTable"
            :prop-name="name"
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
                시설정보 조회 결과
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
import { facilityTableDatas } from '@/views-ipms/menus/ipAllocationMng/sample.js'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalFacilityInformation'

export default {
  name: routeName,
  components: { DynamicComponentLoader, CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      defaultVo: {
        sicisofficescodeNe: 'XXXXXX',
        ssubscnealiasNe: '',
        smodelnameNe: '',
        ssubscmstipNe: '',
        sneSrchTypeCd: '1',
        ssvcLineTypeCd: '',
        ssvcGroupCd: null,
        ssvcObjCd: null,
      },
      ipBlockMstVo: null,
      requestParameter: null,
      selectedRow: null,
       tableColumns: [
        { prop: 'sofficename', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smodelname', label: '모델명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscmstip', label: '장비대표 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: [],
      sOfficeOptions: [],
      ssubscnealiasNe: ''
    }
  },
  computed: {
    componentList() {
      const sOfficeOptions = this.sOfficeOptions
      const ssubscnealiasNe = this.ssubscnealiasNe
      return [
         { key: 'SOffice', props: { prop_parameterKey: 'sicisofficescodeNe', prop_options: sOfficeOptions } },
         { key: 'InputType', props: { label: '장비명', prop_parameterKey: 'ssubscnealiasNe', defaultValue: ssubscnealiasNe } },
         { key: 'InputType', props: { label: '장비대표IP', prop_parameterKey: 'ssubscmstipNe' } },
         { key: 'InputType', props: { label: '모델명', prop_parameterKey: 'smodelnameNe' } },
      ]
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
      this.domElement.maxWidth = 700
    },
    onOpen(model, actionMode) {
      if (model.ipBlockMstVo) {
        this.ipBlockMstVo = model.ipBlockMstVo
        this.ssubscnealiasNe = model?.inputText ?? ''
      }
      this.fnViewSearchtNeMst()
    },
    onClose() {
      if (this.selectedRow !== null) {
        this.$emit('selected-value', { selectedRow: this.selectedRow, returnFlag: 'allocNe' })
      }
      this.tableDatas = []
    },
    async fnViewSearchtNeMst() {
      let params = {}
      if (this.ipBlockMstVo !== null) {
        const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ipBlockMstVo
        params = {
          ssvcLineTypeCd,
          ssvcGroupCd,
          ssvcObjCd,
          sicisofficescodeNe: this.sicisofficescodeNe, // 시설 정보 input값
          sneSrchTypeCd: '2', // 1: 할당 시설, 2: 호스트 기반 시설
        }
      } else {
        params = this.defaultVo
      }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewSearchtNeMst, params)
        const offices = res?.result?.data ?? []
        this.$set(this, 'sOfficeOptions', offices)
      } catch (error) {
        this.error(error)
      }
    },
    // \ipAlocMstVo.sicisofficescodeNe	= vOfficeCode; //수용국
    // ipAlocMstVo.ssubscnealiasNe		= vNeAlias;//장비명
    // ipAlocMstVo.ssubscmstipNe		= vNeMstIp;//대표IP
    // ipAlocMstVo.smodelnameNe		= vModelNm;//모델명
    handleSearch(requestParameter) {
      const { sicisofficescodeNe, ssubscnealiasNe, ssubscmstipNe, smodelnameNe } = requestParameter

      const isCheckSearchLen = (ssubscnealiasNe.length >= 4 || ssubscmstipNe.length >= 4 || smodelnameNe.length >= 4)
      if (sicisofficescodeNe === '' && !isCheckSearchLen) {
        onMessagePopup(this, '수용국을 선택하시지 않으실 경우 검색 값이 최소 4자리 이상이여야 합니다.')
        return
      }
      if (this.ipBlockMstVo) {
        const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ipBlockMstVo
        this._merge(requestParameter, { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd })
      }

      this.requestParameter = requestParameter
      this.fnSelectSearchtNeMst()
    },
    async fnSelectSearchtNeMst() {
      const params = Object.assign({}, this.requestParameter, { sneSrchTypeCd: this.ipBlockMstVo ? '2' : '1' })
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchtNeMst, params)
        this.tableDatas = res.ipAllocOperMstVos
      } catch (error) {
        this.error(error)
      }
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
