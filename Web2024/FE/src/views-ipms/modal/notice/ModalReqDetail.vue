<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="title"
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
              <el-select v-model="resultVo.rboardDivision" size="small">
                <el-option
                  v-for="(option, i) in divisionOptions"
                  :key="i"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </td>
            <th>중요도</th>
            <td>
              <el-select v-model="resultVo.rboardImportance" size="small">
                <el-option value="상">상</el-option>
                <el-option value="중">중</el-option>
                <el-option value="하">하</el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>작성자</th>
            <td>
              <el-input v-model="resultVo.rboardScreateId" size="small" disabled />
            </td>
            <th>희망완료일</th>
            <td>
              <el-date-picker
                v-model="resultVo.rboardDesireDate"
                type="date"
                size="small"
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
                class="upload-demo w-80"
                action="https://jsonplaceholder.typicode.com/posts/"
                :auto-upload="false"
                :on-change="handleFileChange"
              >
                <el-button v-if="viewType !== 'detail'" size="small" type="primary">파일선택</el-button>
              </el-upload>
            </td>

          </tr>
          <template v-if="viewType === 'detail'">
            <tr>
              <th>진행상태</th>
              <td>
                <el-select v-model="resultVo.rboardProgress" size="small">
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
      <template v-if="viewType === 'detail'">
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
import { mapState } from 'vuex'

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
      resultVo: {
        rboardTitle: '',
        rboardPurposeRequest: '',
        rboardDivision: '',
        rboardImportance: '',
        rboardScreateId: '',
        rboardDesireDate: '',
        rboardContent: '',
        rboardProgress: '',
        rboardExpectedDate: '',
        sUserNm: ''
      },
      actionContents: '',
      viewType: null,
      divisionOptions: [
        { label: '전체', value: '' },
        { label: '오류 버그 수정', value: 'RES001' },
        { label: '기능 개발 요청', value: 'RES002' },
        { label: '자료 요청', value: 'RES003' },
        { label: '연동 요청', value: 'RES004' },
      ],
      progressOptions: [
        { label: '전체', value: '' },
        { label: '요청사항 접수', value: 'RES005' },
        { label: '접수 반려', value: 'RES006' },
        { label: '조치 진행 중', value: 'RES006' },
        { label: '조치 완료', value: 'RES007' },
      ],
      selectedFile: ''
    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
    }),
    title() {
      return this.viewType === 'detail' ? '요구사항 상세' : '요구사항 등록'
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.resultVo = model.row
      if (this.viewType === 'create') {
       this.resultVo = {}
      }
    },
    handleFileChange(file, fileList) {
      const allowedExtensions = ['pdf', 'jpg', 'png', 'txt', 'doc', 'ppt', 'xls', 'hwp', 'xlsx', 'pptx']
      const fileName = file.raw.name
      const fileExtension = fileName.split('.').pop().toLowerCase()

      if (!allowedExtensions.includes(fileExtension)) {
        this.$message.error('등록 할 수 없는 파일명입니다.')
        this.$refs.upload.clearFiles()
        return
      }
      this.selectedFile = file.raw
    },
   async fnUpdateSubmit() {
      if (this.resultVo.rboardTitle === '' || this.resultVo.rboardTitle === null) {
        this.$message('요청제목이 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardDivision === '' || this.resultVo.rboardDivision === null) {
        this.$message('요청사항 구분이 선택되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardDesireDate === '' || this.resultVo.rboardDesireDate === null) {
        this.$message('희망완료일자가 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardPurposeRequest === '' || this.resultVo.rboardPurposeRequest === null) {
        this.$message('요청목적이 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardImportance === '' || this.resultVo.rboardImportance === null) {
        this.$message('중요도가 선택되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardContent === '' || this.resultVo.rboardContent === null) {
        this.$message('요청내용이 입력되지 않았습니다.')
        return
      }
      let res
      try {
        const formData = {
            file: this.selectedFile,
            seq: this.resultVo.seq,
            rboardTitle: this.resultVo.rboardTitle,
            rboardDivision: this.resultVo.rboardDivision,
            rboardDesireDate: this.resultVo.rboardDesireDate,
            rboardPurposeRequest: this.resultVo.rboardPurposeRequest,
            rboardImportance: this.resultVo.rboardImportance,
            rboardContent: this.resultVo.rboardContent,
            sUserNm: this.resultVo.sUserNm,
            mail_type: 'Req-Update',
        }
        if (this.adminYn === 'Y') {
          this._merge(formData, {
            actionContents: this.actionContents,
            rboardProgress: this.resultVo.rboardProgress,
            rboardExpectedDate: this.resultVo.actionContents
          })
        }
        res = await apiRequestModel(ipmsModelApis.updateReq, formData)
        if (res.commonMsg) {
          this.$message('요구사항이 정상적으로 수정되었습니다.')
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.log(error)
      }
   },
    fnDeleteSubmit() {
      this.$confirm('정말 삭제 하시겠습니까?', '요구사항 삭제', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const ReqboardVo = {
            seq: this.resultVo.seq
          }
           res = await apiRequestJson(ipmsJsonApis.deleteReq, ReqboardVo)
            if (res.commonMsg === 'SUCCESS') {
              this.$message('요구사항이 정상적으로 삭제되었습니다.')
              this.$emit('reload')
              this.close()
            }
          } catch (error) {
            this.$message.error({ message: `${res.commonMsg}` })
            console.log(error)
          }
        })
    },
    async fnInsertSubmit() {
       if (this.resultVo.rboardTitle === '' || this.resultVo.rboardTitle === null) {
        this.$message('요청제목이 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardDivision === '' || this.resultVo.rboardDivision === null) {
        this.$message('요청사항 구분이 선택되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardDesireDate === '' || this.resultVo.rboardDesireDate === null) {
        this.$message('희망완료일자가 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardPurposeRequest === '' || this.resultVo.rboardPurposeRequest === null) {
        this.$message('요청목적이 입력되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardImportance === '' || this.resultVo.rboardImportance === null) {
        this.$message('중요도가 선택되지 않았습니다.')
        return
      }

       if (this.resultVo.rboardContent === '' || this.resultVo.rboardContent === null) {
        this.$message('요청내용이 입력되지 않았습니다.')
        return
      }
      let res
      try {
        const formData = {
            file: this.selectedFile,
            rboardTitle: this.resultVo.rboardTitle,
            rboardDivision: this.resultVo.rboardDivision,
            rboardDesireDate: this.resultVo.rboardDesireDate,
            rboardPurposeRequest: this.resultVo.rboardPurposeRequest,
            rboardImportance: this.resultVo.rboardImportance,
            rboardContent: this.resultVo.rboardContent,
            sUserNm: this.resultVo.sUserNm,
            mail_type: 'Req-Insert',
        }
        res = await apiRequestModel(ipmsModelApis.insertReq, formData)
        if (res.commonMsg) {
          this.$message('요구사항이 정상적으로 등록되었습니다.')
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.log(error)
      }
    },
    isClose() {
        if (this.viewType === 'edit') {
          this.$confirm('신청정보가 삭제됩니다. 정말 신청취소 하시겠습니까?', '신청정보가 삭제', {
            confirmButtonText: '확인',
            cancelButtonText: '취소'
          }).then(async () => {
            this.close()
          }).catch(() => {
            /*  */
          })
        } else {
          this.close()
        }
      },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>

</style>
