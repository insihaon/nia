<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'요구사항 '+(viewType === 'detail' ? '상세' : '등록')"
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
    <div v-loading="viewLoading" class="popupContentTable">
      <table>
        <colgroup>
          <col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>요청제목</th>
            <td colspan="4">
              <el-input v-model="resultVo.rboardTitle" name="sboardTitle" size="small" />
            </td>
          </tr>
          <tr>
            <th>요청 목적</th>
            <td colspan="4">
              <el-input v-model="resultVo.rboardPurposeRequest" type="text" name="sRequestPurpose" size="small" />
            </td>
          </tr>
          <tr>
            <th>요청사항 구분</th>
            <td>
              <el-select v-model="resultVo.rboardDivision" size="small" :disabled="isDisabled" popper-class="rboardDivision">
                <el-option value="" label="전체" />
                <el-option
                  v-for="(option, i) in requestOptions"
                  :key="i"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </td>
            <th>중요도</th>
            <td>
              <el-select v-model="resultVo.rboardImportance" size="small" :disabled="isDisabled" popper-class="rboardImportance">
                <el-option value="" label="전체" />
                <el-option value="상">상</el-option>
                <el-option value="중">중</el-option>
                <el-option value="하">하</el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>작성자</th>
            <td>
              <el-input v-model="resultVo.sUserNm" size="small" disabled />
            </td>
            <th>희망완료일</th>
            <td>
              <el-date-picker
                v-model="resultVo.rboardDesireDate"
                type="date"
                size="small"
                popper-class="rboardDesireDate"
                :disabled="isDisabled"
              />
            </td>
          </tr>
          <tr>
            <th>요청내용</th>
            <td colspan="3">
              <el-input v-model="resultVo.rboardContent" type="textarea" rows="4" size="small" />
            </td>
          </tr>
          <tr>
            <template v-if="viewType === 'detail'">
              <th>조치 내용</th>
              <td colspan="3">
                <el-input v-model="actionContents" type="textarea" rows="4" size="small" />
              </td>
            </template>
          </tr>
          <tr>
            <th>첨부파일</th>
            <td colspan="3">
              <el-upload
                ref="upload"
                :file-list="files"
                :show-file-list="true"
                :on-preview="handleFileClick"
                class="upload-demo w-80"
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
              >
                <el-button v-if="viewType !== 'detail' || (ownerYn === 'Y' && resultVo.rboardProgress === 'RES005')" size="small" type="primary">파일선택</el-button>
              </el-upload>
              <!-- @click="downloadReqFile(resultVo.rboardFileSeq)" -->
              <a v-show="false" ref="fileDownload" :href="resultVo.rboardDownloadPath">
                {{ resultVo.rboardFileOriginName }}
              </a>
            </td>
          </tr>
          <template v-if="viewType === 'detail'">
            <tr>
              <th>진행상태</th>
              <td>
                <el-select v-model="resultVo.rboardProgress" size="small">
                  <el-option value="" label="전체" />
                  <el-option
                    v-for="(option, i) in progressOptions"
                    :key="i"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>
              </td>
              <th>완료예정일</th>
              <td>
                <el-date-picker v-model="resultVo.rboardExpectedDate" type="date" size="small" />
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <template v-if="viewType === 'detail' && ownerYn === 'Y'">
        <el-button type="primary" size="small" icon="el-icon-delete" round @click="fnDeleteSubmit()">삭제</el-button>
        <el-button type="primary" size="small" icon="el-icon-edit" round @click="fnUpdateSubmit()">수정</el-button>
      </template>
      <template v-if="viewType === 'create'">
        <el-button type="primary" size="small" icon="el-icon-document-add" round @click="fnInsertSubmit()">등록</el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup, changeDownloadFileHost } from '@/utils/index'
import { AppOptions } from '@/class/appOptions'

const routeName = 'ModalReqDetail'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewLoading: false,
      resultVo: {},
      defaultResultVo: {
        rboardTitle: '',
        rboardPurposeRequest: '',
        rboardDivision: '',
        rboardImportance: '',
        rboardScreateId: this.$store.state.user.info.suserId,
        rboardDesireDate: '',
        rboardContent: '',
        rboardProgress: '',
        rboardExpectedDate: '',
        sUserNm: this.$store.state.user.info.suserNm
      },
      actionContents: '',
      viewType: null,
      requestOptions: [],
      progressOptions: [],
      files: [],
      ownerYn: '',
      adminYn: '',
    }
  },
  computed: {
    isDisabled() { // 글작성자 또는 관리자가 아니거나 rboardProgress가 접수상태가 아닐 때(진행중, 완료) disabled처리
      return this.viewType === 'detail' && (this.ownerYn !== 'Y' || this.resultVo.rboardProgress !== 'RES005')
    }
  },
  mounted() {
    this.loadOptionType()
  },
  methods: {
    async loadOptionType() {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertReq, {})
        this.requestOptions = res.result.requestType.map(v => { return { value: v.rboardTypeSubCd, label: v.rboardTypeSubnm } })
        this.progressOptions = res.result.progressType.map(v => { return { value: v.rboardTypeSubCd, label: v.rboardTypeSubnm } })
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewDetailReq(seq = null) {
      if (seq === null) return
        try {
          this.viewLoading = true
          const res = await apiRequestModel(ipmsModelApis.viewDetailReq, { seq })
          this.adminYn = res.adminYn
          this.ownerYn = res.ownerYn
          this.resultVo = res.result
          if (res?.result?.rboardFileSeq) {
            this.files = [{
            name: res.result.rboardFileOriginName,
            downloadUrl: res.result.rboardDownloadPath, // changeDownloadFileHost(res.resultVo.rboardDownloadPath),
            previewUrl: res.result.rboardDownloadPath
          }]
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.viewLoading = false
      }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    onOpen(model, actionMode) {
      this.files = []
      this.viewType = model.type
      this.resultVo = this._cloneDeep(this.defaultResultVo)
      if (model.type === 'detail') {
        this.fnViewDetailReq(model.seq)
      }
    },
    handleFileChange(file, fileList) {
      const allowedExtensions = ['pdf', 'jpg', 'png', 'txt', 'doc', 'ppt', 'xls', 'hwp', 'xlsx', 'pptx']
      const fileName = file.raw.name
      const fileExtension = fileName.split('.').pop().toLowerCase()

      if (!allowedExtensions.includes(fileExtension)) {
        onMessagePopup(this, '등록 할 수 없는 파일명입니다.')
        this.$refs.upload.uploadFiles = this.files
        return
      }
      this.files = [file.raw]
    },
    handleFileRemove(file, fileList) {
      this.files = fileList
    },
    handleFileClick(file) {
      this.$refs.fileDownload.click()
    },
    deleteFile() {
      this.files = []
    },
    getParameter(progressType) {
      const { rboardTitle, rboardDivision, rboardDesireDate, rboardPurposeRequest, rboardImportance, rboardContent, sUserNm } = this.resultVo
      const formData = new FormData()
      if (progressType === 'update') {
        formData.append('seq', this.resultVo.seq)
        if (this.adminYn === 'Y') {
          formData.append('actionContents', this.actionContents)
          formData.append('rboardProgress', this.resultVo.rboardProgress)
          formData.append('rboardExpectedDate', this.moment(this.resultVo.rboardExpectedDate).format('YYYY-MM-DD'))
        }
      }
      formData.append('file', this.files[0])
      formData.append('rboardTitle', rboardTitle)
      formData.append('rboardDivision', rboardDivision)
      formData.append('rboardDesireDate', this.moment(rboardDesireDate).format('YYYY-MM-DD'))
      formData.append('rboardPurposeRequest', rboardPurposeRequest)
      formData.append('rboardImportance', rboardImportance)
      formData.append('rboardContent', rboardContent)
      formData.append('sUserNm', sUserNm)
      formData.append('mail_type', 'Req-Insert')
      return formData
    },
    downloadReqFile(seq) {
      // 파일 다운로드
      // window.location = AppOptions.instance.baseURL + 'opermgmt/requiremgmt/ReqDownLoad.do?seq=' + seq
    },
    async fnInsertSubmit() {
      /*
      파일 업로드 path 디버깅 필요
      opermgmt/requiremgmt/updateReq.ajax
      request.getSession().getServletContext().getRealPath("/").concat("resources") + "\\upload";
      */
      if (!this.fnCheckValidate()) return
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertReq, this.getParameter('insert'))
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '요구사항이 정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.log(error)
      }
    },
    async fnUpdateSubmit() {
      if (!this.fnCheckValidate()) return
      try {
        const res = await apiRequestJson(ipmsJsonApis.updateReq, this.getParameter('update'))
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '요구사항이 정상적으로 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.log(error)
      }
    },
    fnDeleteSubmit() {
      this.$confirm('정말 삭제 하시겠습니까?', '요구사항 삭제', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const ReqboardVo = {
            seq: this.resultVo.seq
          }
          const res = await apiRequestJson(ipmsJsonApis.deleteReq, ReqboardVo)
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '요구사항이 정상적으로 삭제되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            console.log(error)
          }
        })
    },
    fnCheckValidate() {
      if (this.resultVo.rboardTitle === '' || this.resultVo.rboardTitle === null) {
        onMessagePopup(this, '요청제목이 입력되지 않았습니다.')
        return false
      }

       if (this.resultVo.rboardDivision === '' || this.resultVo.rboardDivision === null) {
        onMessagePopup(this, '요청사항 구분이 선택되지 않았습니다.')
        return false
      }

       if (this.resultVo.rboardDesireDate === '' || this.resultVo.rboardDesireDate === null) {
        onMessagePopup(this, '희망완료일자가 입력되지 않았습니다.')
        return false
      }

       if (this.resultVo.rboardPurposeRequest === '' || this.resultVo.rboardPurposeRequest === null) {
        onMessagePopup(this, '요청목적이 입력되지 않았습니다.')
        return false
      }

       if (this.resultVo.rboardImportance === '' || this.resultVo.rboardImportance === null) {
        onMessagePopup(this, '중요도가 선택되지 않았습니다.')
        return false
      }

       if (this.resultVo.rboardContent === '' || this.resultVo.rboardContent === null) {
        onMessagePopup(this, '요청내용이 입력되지 않았습니다.')
        return false
      }
      if (this.adminYn === 'Y' && this.viewType === 'detail' && (this.resultVo.rboardExpectedDate === '' || this.resultVo.rboardExpectedDate === null)) {
        onMessagePopup(this, '완료예정일이 입력되지 않았습니다.')
        return false
      }
      return true
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-upload-list__item {
  width: fit-content;
}
</style>
