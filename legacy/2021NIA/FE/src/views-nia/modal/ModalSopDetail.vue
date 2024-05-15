<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.minHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          {{ viewType }} SOP 상세보기
          <hr>
        </span>
        <div class="d-flex flex-column h-100 rounded justify-center" style="border: solid 1px #1e293b">
          <el-form ref="sopForm" :model="updateInfo" autocomplete="on" label-position="left">
            <el-row class="p-1">
              <el-col v-for="(item, index) in formInfo" :key="index" :span="isMobile ? 24: 12">
                <el-form-item :ref="item.model" :label="item.label" class="d-flex items-center m-0">
                  <el-input v-if="viewType === 'TICKET'" v-model="sopInfo[item.model]" size="mini" readonly />
                  <el-input v-if="viewType === 'SYSLOG'" v-model="syslogInfo[item.model]" size="mini" readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="w-100 d-flex justify-center items-center text-white font-semibold text-small rounded" style="background-color: #1e293b">수정사항</div>
            <el-row class="d-flex flex-column p-1">
              <el-col v-for="(item, index) in formUpdate" :key="index" :span="24">
                <el-form-item :ref="item.model" :label="item.label" class="d-flex items-center m-0">
                  <el-select v-model="updateInfo[item.model]" :collapse-tags="isViewport('<', 'sm')" size="mini" popper-class="selectMenu-popper-option" filterable default-first-option>
                    <el-option v-for="option in item.option" :key="option.value" :label="option.value" :value="option.value" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
          <hr>
          <el-button size="mini" icon="el-icon-edit" @click.native="close()">수정</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click.native="close()">삭제</el-button>
          <el-button size="mini" icon="el-icon-close" type="info" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSelectSopCode } from '@/api/nia'
import { getTicketStatus } from '@/views-nia/js/commonFormat'

const routeName = 'ModalSopDetail'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      viewType: 'TICKET',
      sopCodeList: [],
      visible: false,
      codeKeys: { gubun: '장애구분', type: '장애유형', content: '조치내용' },
      selectOption: {
        gubun: [],
        type: [],
        content: [],
      },
      sopInfo: {
        ticket_id: '',
        ticket_type: '',
        node_num: '',
        ip_addr: '',
      },
      syslogInfo: {
        alarmno: '',
        node_nm: '',
        alarmloc: '',
        etc: '',
        status: '',
      },
      updateInfo: {
        gubun: '',
        type: '',
        content: '',
      },
    }
  },
  computed: {
    formInfo() {
      let form
      if (this.viewType === 'TICKET') {
        form = [
          { label: 'TICKET NO.', model: 'ticket_id' },
          { label: 'TICKET 유형', model: 'ticket_type' },
          { label: '장비 이름', model: 'root_cause_sysnamea' },
          { label: '장비 ID', model: 'root_cause_sysnamea' },
          { label: '마스터 IP', model: 'ip_addra' },
        ]
      } else {
        form = [
          { label: '알람번호', model: 'alarmno' },
          { label: '장비명', model: 'node_nm' },
          { label: '인터페이스', model: 'alarmloc' },
          { label: 'syslog 원본메시지', model: 'etc' },
          { label: '처리상태', model: '_status' },
        ]
      }
      return form
    },
    formUpdate() {
      return [
        { label: '장애구분', model: 'gubun', option: this.selectOption.gubun },
        { label: '장애유형', model: 'type', option: this.selectOption.type },
        { label: '조치내용', model: 'content', option: this.selectOption.content },
      ]
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
      this.viewType = model?.type

      this.syslogInfo['_status'] = getTicketStatus(null, null, this.selectedRow.status)
      this._merge(this.viewType === 'TICKET' ? this.sopInfo : this.syslogInfo, this.selectedRow)
      this.onLoadSopCodeList()
    },
    async onLoadSopCodeList() {
      try {
        const res = await apiSelectSopCode({ IS_OPTION: true })
        this.sopCodeList = res?.result
        this.setSopCode()
      } catch (error) {
        this.error(error)
      }
    },
    setSopCode() {
      const codeKeys = Object.keys(this.codeKeys)
      codeKeys.forEach((key) => {
        const sopCodeObj = this.sopCodeList.find((d) => d.fault_gb === this.codeKeys[key])
        this.selectOption[key] = sopCodeObj?.code_arr?.map((v) => {
          return { value: v }
        })
      })
    },
    onClose() {
      /* for Override */
    },
  },
}
</script>
<style lang="scss" scoped>

::v-deep .el-form-item__label {
  width: 90px;
  margin-left: 5px;
  line-height: 20px;
}
::v-deep .el-form-item__content {
  width: calc(100% - 90px);
}
</style>
