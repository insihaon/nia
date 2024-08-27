<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
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
        업로드
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <form id="inputForm" name="inputForm">
            <h4>양식 다운로드 및 업로드</h4>
            <h4 style="color:red;">주의사항</h4>
            <p style="padding-left: 20px;">
              <span style="color:blue; font-weight:bold;">*&nbsp;양식 다운로드&nbsp;</span>: 계위 선택 및 수용국까지 선택하셔야 합니다.
            </p>
            <p style="padding-left: 20px;">
              <span style="color:blue; font-weight:bold;">*&nbsp;Upload&nbsp;</span>: 계위 선택을 하셔야 하며 선택한 계위로 엑셀 데이터가 등록됩니다.
            </p>
            <p style="padding-left: 81px;">
              업로드 성공 또는 실패시 경고창이 나타납니다. 그 전까지 새로고침 혹은 페이지를 벗어나지 마세요.
            </p>
            <table class="tbl_data entry mt5">
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tbody>
                <tr class="top">
                  <th class="first" scope="row">계위</th>
                  <td colspan="3">
                    <ul class="min_flow">
                      <SsvcLineType
                        ref="SsvcLineType"
                        class="SsvcLineType"
                        label=""
                        :lvl="3"
                        @update-value="onChangeSsvcLineType"
                      />
                    </ul>
                  </td>
                </tr>
                <tr>
                  <th class="first" scope="row">수용국</th>
                  <td colspan="3">
                    <ul class="min_flow">
                      <el-select
                        v-model="formData.sicisofficescodeNe"
                        size="mini"
                        :disabled="disabledOffice"
                      >
                        <el-option
                          v-for="(option, i) in sOfficeOptions"
                          :key="i"
                          :label="option.name"
                          :value="option.code"
                        />
                      </el-select>
                      <!-- <SOffice
                        ref="SOffice"
                        class="SOffice"
                        label=""
                        api-path="ipmgmt/ipuploadmgmt"
                        list-name="selectSearchHostList"
                        vo-name=""
                        :valueKey="{ cd: 'code', nm: 'name' }"
                      /> -->
                      <!-- <select id="sicisofficescodeNe" name="sicisofficescodeNe" v-model="formData.sicisofficescodeNe" /> -->
                    </ul>
                  </td>
                </tr>
                <!-- Conditional input for MIG_PIP_PREFIX_RANGE -->
                <tr>
                  <th class="first" scope="col">파일 Upload</th>
                  <td scope="col" colspan="3">
                    <input id="input_file" type="file" size="500" @change="handleFileUpload" />
                  </td>
                </tr>
              </tbody>
            </table>
          </form>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button icon="el-icon-download" size="mini" @click="downloadFormat('txt')">텍스트 양식 다운로드</el-button>
        <el-button icon="el-icon-download" size="mini" @click="downloadFormat('excel')">엑셀 양식 다운로드</el-button>
        <el-button icon="el-icon-upload2" type="primary" size="mini" @click="fnUpload">Upload</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import SsvcLineType from '@/views-ipms/conditionComponents/SsvcLineType'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson, apiRequestOffice } from '@/api/ipms'

const routeName = 'ModalUploadInsert'

export default {
  name: routeName,
  components: { SsvcLineType },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      disabledOffice: true,
      formData: {
        ssvcLineTypeCd: '',
        ssvcGroupCd: '',
        ssvcObjCd: '',
        sicisofficescodeNe: '',
      },
      sOfficeOptions: [],
      files: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.$set(this.formData, 'sicisofficescodeNe', '')
    },
    onClose() { },
    async onLoadOfficeList(params) {
      // 3계위까지 선택했을 때 요청 / 3계위 없으면 disable
      try {
        const res = await apiRequestOffice('ipmgmt/ipuploadmgmt', params, 'selectSearchHostList')
        if (res.length > 0) {
          this.sOfficeOptions = res
          this.$set(this.formData, 'sicisofficescodeNe', res[0].code)
        } else {
          this.disabledOffice = true
          this.sOfficeOptions = [{ code: 'None', name: '수용국 없음' }]
        }
      } catch (error) {
        this.error(error)
      }
    },
    onChangeSsvcLineType(ssvcArr) {
      const isAllExist = ssvcArr.every(v => v.value !== '')
      this.disabledOffice = !isAllExist
      this.$set(this.formData, 'sicisofficescodeNe', '')
      ssvcArr.forEach(v => { this.$set(this.formData, v.key, v.value) })
      if (isAllExist) {
        this.onLoadOfficeList(ssvcArr.map(v => { return { [v.key]: v.value } }))
      }
    },
    downloadFormat(type) {
      if (!this.fnValidCheck()) return
      const params = this.formData
      // doAjaxExcelSubmit(baseContext, "ipmgmt/ipuploadmgmt/downloadtextformat.json", param);
      // doAjaxExcelSubmit(baseContext, "ipmgmt/ipuploadmgmt/downloadformat.json", param);
    },
    fnValidCheck() {
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sicisofficescodeNe } = this.formData
      if (ssvcLineTypeCd === '') {
        onMessagePopup(this, '1계위 서비스망은 필수로 선택해 주시기 바랍니다.')
        return false
      }
      if (ssvcGroupCd === '' || ssvcGroupCd === '000000') {
        onMessagePopup(this, '2계위 본부는 필수로 선택해 주시기 바랍니다.')
        return false
      }
      if (ssvcObjCd === '' || ssvcObjCd === '000000') {
        onMessagePopup(this, '3계위 국사는 필수로 선택해 주시기 바랍니다.')
        return false
      }
      if (sicisofficescodeNe === '' || this.disabledOffice) {
        onMessagePopup(this, '수용국이 존재하지 않습니다.')
        return false
      }
      return true
    },
    handleFileUpload(e) {
      this.files = e.target.files
    },
    fnUpload() {
      if (this.files.length === 0) {
        onMessagePopup(this, '선택된 파일이 없습니다.')
        return
      }
      // /ipmgmt/ipuploadmgmt/upload.ajax
      const params = Object.assign({}, this.formData, { file: this.files[0] })
    }
  },
}
</script>
<style lang="scss" scoped>
.SsvcLineType ::v-deep {
  width: 100%;
  display: flex;
  label {
    margin-left: 0px !important;
  }
}
</style>
