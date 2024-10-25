<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="텍스트 파일 업로드"
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
      <div class="popupContentTableTitle">Upload ( 무선 IP-Checker 에 등록되어 있는 txt 파일만 업로드 )</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>파일 Upload</th>
            <td class="textflex">
              <el-select v-model="selectedOption" size="small">
                <el-option
                  v-for="item in uploadOptions"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                >
                </el-option>
              </el-select>
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
            <td>
              <el-button size="small" type="primary" round @click="uploadAjax">
                Upload
              </el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- <div class="btn_area mt10">
      <span>
        <a href="#none">
          <input
            type="image"
            :src="closeButtonImage"
            value="닫기"
            id="btnClose"
            @mouseover="menuOver"
            @mouseout="menuOut"
            @click="handleClose"
          />
        </a>
      </span>
    </div> -->
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalUploadMst'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedOption: 'COMMU', // Default selected option
      files: null,
      uploadOptions: [
        { value: 'COMMU', label: 'Community' },
        { value: 'NOCOMMU', label: 'No-Community' },
        { value: 'SUMMARY', label: 'Summary' },
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
    },
    onClose() { },
    handleFileChange(event) {
      this.file = event.target.files[0]
    },
    uploadAjax() {
      const skindNm = this.uploadOptions.find(v => v.value === this.selectedOption).label
      this.confirm(skindNm + ' 정보를 전체 삭제 후 업로드가 진행됩니다. 업로드 하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async() => {
        if (!this.file) {
          onMessagePopup(this, '선택된 파일이 없습니다.')
          return
        }
        try {
          const formData = { file: this.file, type: this.selectedOption }
          const res = await apiRequestJson(ipmsJsonApis.uploadMobileMst, formData)
          // URL: opermgmt/intgrmgmt/uploadMobileMst.ajax
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '파일 업로드가 정상적으로 처리되었습니다.')
            this.$emit('reload')
            this.close()
          } else {
            if (res === null || res === '') {
              onMessagePopup(this, '파일 형식이 맞지 않습니다.')
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    handleFileUpload(e) {
      this.file = e.target.files[0]
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
