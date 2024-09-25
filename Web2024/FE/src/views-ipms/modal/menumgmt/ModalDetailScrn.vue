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
        화면상세
        <hr>
      </span>

      <template v-if="viewType === 'detail'">
        <div id="content" class="layer">
          <div class="content_result mt0">
            <h4>화면 정보</h4>
            <table class="tbl_data mt5">
              <colgroup>
                <col width="15%" />
                <col width="30%" />
                <col width="15%" />
                <col width="40%" />
              </colgroup>
              <tbody>
                <tr class="top">
                  <th class="first" scope="row">화면ID</th>
                  <td>{{ resultVo.sscrnId }}</td>
                  <th scope="row">화면유형</th>
                  <td>{{ resultVo.sscrnTypeNm }}</td>
                </tr>
                <tr>
                  <th class="first" scope="row">화면 명</th>
                  <td>{{ resultVo.sscrnNm }}</td>
                  <th scope="row">화면URL유형</th>
                  <td>{{ resultVo.sscrnUrlTypeNm }}</td>
                </tr>
                <tr>
                  <th class="first" scope="row">설계화면ID</th>
                  <td>{{ resultVo.sdgnScrnId }}</td>
                  <th scope="row">화면사용여부</th>
                  <td>{{ resultVo.sscrnUseYn }}</td>
                </tr>
                <tr class="last">
                  <th class="first" scope="row">화면URL정보</th>
                  <td colspan="3">{{ resultVo.sscrnUrlAdr }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>

      <template v-else>
        <div id="content" class="layer">
          <div class="content_result mt0">
            <h4>화면 정보</h4>
            <table class="tbl_data entry mt5">
              <colgroup>
                <col width="15%" />
                <col width="30%" />
                <col width="15%" />
                <col width="40%" />
              </colgroup>
              <tbody>
                <tr class="top">
                  <th class="first" scope="row">화면 ID</th>
                  <td><el-input v-model="insertForm.sscrnId" type="text" class="txt" disabled /></td>
                  <th scope="row">화면 유형</th>
                  <td class="view">
                    <el-radio-group v-model="insertForm.sscrnTypeCd">
                      <el-radio label="UD0000">미분류</el-radio>
                      <el-radio label="UD0001">MAIN</el-radio>
                      <el-radio label="UD0002">SUB</el-radio>
                      <el-radio label="UD0003">POPUP</el-radio>
                    </el-radio-group>
                  </td>
                </tr>
                <tr>
                  <th class="first" scope="row">화면 명</th>
                  <td><el-input v-model="insertForm.sscrnNm" type="text" class="txt" /></td>
                  <th scope="row">화면 URL 유형</th>
                  <td class="view">
                    <el-radio-group v-model="insertForm.sscrnUrlTypeCd">
                      <el-radio label="UU0000">미분류</el-radio>
                      <el-radio label="UU0001">OSS 내부 URL</el-radio>
                      <el-radio label="UU0002">OSS 외부 URL</el-radio>
                    </el-radio-group>
                  </td>
                </tr>
                <tr>
                  <th class="first" scope="row">설계화면 ID</th>
                  <td><el-input v-model="insertForm.sdgnScrnId" type="text" class="txt" /></td>
                  <th scope="row">화면 사용 여부</th>
                  <td class="view">
                    <el-radio-group v-model="insertForm.sscrnUseYn">
                      <el-radio label="Y">사용</el-radio>
                      <el-radio label="N">미사용</el-radio>
                    </el-radio-group>
                  </td>
                </tr>
                <tr class="last">
                  <th class="first" scope="row">화면 URL 정보</th>
                  <td colspan="3"><el-input v-model="insertForm.sscrnUrlAdr" type="text" class="txt w98" /></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="viewType === 'detail'" size="mini" @click="onChangeMode()">{{ '수정' }}</el-button>
        <el-button v-if="viewType !== 'detail'" size="mini" @click="fnInsertScrnBas()">{{ '등록' }}</el-button>
        <el-button v-if="viewType !== 'edit'" size="mini" @click="fnUpdateScrnBas()">{{ '저장' }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="isClose()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
const routeName = 'ModalDetailScrn'

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
      },
      insertForm: {
        sscrnId: '',
        sscrnTypeCd: 'UD0000',
        sscrnNm: '',
        sscrnUrlTypeCd: 'UU0000',
        sdgnScrnId: '',
        sscrnUseYn: 'Y',
        sscrnUrlAdr: '',
      },
      viewType: '',
    }
  },
  computed: {
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      if (model.row) {
        this.resultVo = this._cloneDeep(model.row)
        this.insertForm = this._cloneDeep(model.row)
      } else {
        this.insertForm = {
          sscrnTypeCd: 'UD0000',
          sscrnUrlTypeCd: 'UU0000',
          sscrnUseYn: 'Y',
        }
        this.insertForm.sscrnId = 'S00100'
      }
    },
    async fnInsertScrnBas() { /* 저장 */
      if (this.insertForm.sscrnNm === '' || this.insertForm.sscrnNm === null) {
        this.$message('화면 명을 입력해주세요')
        return
      }
      try {
      const tbScrnBasVo = this.insertForm
      this._merge(tbScrnBasVo, { screateId: this.$store.state.user.info.Uid, smodifyId: this.$store.state.user.info.Uid })
      const res = await apiRequestJson(ipmsJsonApis.insertScrnBas, tbScrnBasVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('화면정보가 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.$message(`${res.message}`)
        }
      } catch (error) {
        console.log(error)
      }
    },
    async fnUpdateScrnBas() { /* 수정 */
    try {
      const tbScrnBasVo = this.insertForm
      this._merge(tbScrnBasVo, { screateId: this.$store.state.user.info.Uid, smodifyId: this.$store.state.user.info.Uid })
      const res = await apiRequestJson(ipmsJsonApis.updateScrnBas, tbScrnBasVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('화면정보가 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.$message(`${res.message}`)
        }
      } catch (error) {
        console.log(error)
      }
    },
    onChangeMode() {
     this.viewType = 'edit'
    },
    isClose() {
        if (this.viewType === 'datail') {
          this.$confirm('작성한 정보가 삭제됩니다. 팝업창을 닫겠습니까?', '신청 정보 저장 알림', {
            confirmButtonText: '확인',
            cancelButtonText: '취소'
          }).then(async () => {
            this.close()
          }).catch(() => {
            return
          })
        } else {
          this.close()
        }
      }
  },
}
</script>
<style lang="scss" scoped>
</style>
