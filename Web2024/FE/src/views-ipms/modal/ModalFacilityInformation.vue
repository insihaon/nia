<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
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
      requestParameter: null,
      selectedRow: null,
      componentList: [
         { key: 'SOffice', props: { prop_parameterKey: 'siciofficescodeNe' } },
         { key: 'InputType', props: { label: '장비명', prop_parameterKey: 'ssubscnealiasNe' } },
         { key: 'InputType', props: { label: '장비대표IP', prop_parameterKey: 'ssubscmstipNe' } },
         { key: 'InputType', props: { label: '모델명', prop_parameterKey: 'smodelnameNe' } },
      ],
       tableColumns: [
        { prop: 'sofficename', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smodelname', label: '모델명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscmstip', label: '장비대표 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: [
        { sofficename: '가경국사', ssubscnealias: 'ACT-GAGYEONG', ssubscmstip: '112.188.200.127', smodelname: 'LOC_E6100', sofficecode: 'R02414' },
        { sofficename: '가경국사1', ssubscnealias: 'ACT-GAGYEONG', ssubscmstip: '112.188.200.127', smodelname: 'LOC_E6100', sofficecode: 'R02415' }
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
    },
    onClose() {
      if (this.selectedRow !== null) {
        this.$emit('selected-value', this.selectedRow)
      }
    },
    handleSearch(requestParameter) {
      this.requestParameter = requestParameter
      if (this.requestParameter === null || this.requestParameter['siciofficescodeNe'] === undefined) {
        onMessagePopup(this, '수용국을 선택하시지 않으실 경우 검색값이 최소 4자리 이상이여야 합니다.')
        return
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
