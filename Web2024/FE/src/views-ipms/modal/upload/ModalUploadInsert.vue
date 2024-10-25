<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="업로드"
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
    <div class="popupContentTable">
      <ul class="popupContentTableDes">
        <li>양식 다운로드 및 업로드</li>
        <li>
          주의사항
          <ul>
            <li>※ 양식 다운로드 : <span>계위 선택 및 수용국까지 선택하셔야합니다.</span></li>
            <li>※ Upload : <span>계위 선택을 하셔야하며 선택한 계위로 엑셀 데이터가 등록됩니다.<br>업로드 성공 또는 실패시 경고창이 나타납니다. 그 전까지 새로고침 혹은 페이지를 벗어나지 마세요.</span></li>
          </ul>
        </li>
      </ul>
      <table>
        <!-- <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup> -->
        <tr>
          <SsvcLineType
            ref="SsvcLineType"
            class="SsvcLineType"
            label="계위"
            :lvl="3"
            @update-value="onChangeSsvcLineType"
          />
        </tr>
        <tr>
          <th>수용국</th>
          <td class="flex">
            <el-select v-model="formData.sicisofficescodeNe" :disabled="disabledOffice">
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
          </td>
        </tr>
        <!-- Conditional input for MIG_PIP_PREFIX_RANGE -->
        <tr>
          <th>파일 Upload</th>
          <td class="flex">
            <el-upload
              action="https://www.typicode.com/posts/"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :before-remove="beforeRemove"
              multiple
              :limit="3"
              :on-exceed="handleExceed"
              :file-list="fileList"
              @change="handleFileUpload"
            >
              <el-button size="small" type="primary" plain>파일선택</el-button>
              <div slot="tip" class="el-upload__tip">선택된 파일 없음</div>
            </el-upload>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-download" round @click="downloadFormat('txt')">텍스트 양식 다운로드</el-button>
      <el-button type="primary" size="small" icon="el-icon-download" round @click="downloadFormat('excel')">엑셀 양식 다운로드</el-button>
      <el-button type="primary" size="small" icon="el-icon-upload2" round @click="fnUpload">Upload</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
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
      files: [],
      fileList: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
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
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
    },
    beforeRemove(file, fileList) {
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
