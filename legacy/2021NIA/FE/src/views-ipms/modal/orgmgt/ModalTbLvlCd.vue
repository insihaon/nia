<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'가상 국사 조직 정보'+ isEdit ? ' 수정' : '등록'"
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
          <col width="30%" /><col width="70%" />
        </colgroup>
        <tbody>
          <tr>
            <th>계위코드</th>
            <td> {{ resultVo.slvlCd }} </td>
          </tr>
          <tr>
            <th>계위명</th>
            <td><el-input v-model="resultVo.slvlNm" size="small" /></td>
          </tr>
          <tr>
            <th>계위구분</th>
            <td>
              <el-select v-model="resultVo.sorgOfficeFlagYn" size="small" class="w-100">
                <el-option
                  v-for="item in [
                    { label : '국사' , value: 'N'},
                    { label : '조직' , value: 'Y'}
                  ]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>외부연동코드</th>
            <td><el-input size="small" value="수작업데이터" disabled /></td>
          </tr>
          <tr class="last">
            <th>비고</th>
            <td><el-input v-model="resultVo.scomment" size="small" /></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="isEdit" type="primary" size="small" icon="el-icon-save" round @click="fnProcess()">저장</el-button>
      <el-button v-else type="primary" size="small" icon="el-icon-document-add" round @click="fnProcess()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { mapState } from 'vuex'
import _ from 'lodash'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalTbLvlCd'

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
      resultVo: {
        slvlCd: '',
        slvlNm: '',
        sorgOfficeFlagYn: '',
        scomment: '',
      },
      viewType: ''
    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      suserId: state => state.user.info.Uid,
      suserGradeCd: state => state.ipms.suserGradeCd,
    }),
    isEdit() {
      return this.viewType === 'edit'
    }
  },
  mounted() {

  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      if (this.isEdit) {
        this.fnViewupdateLvlCd(model.slvlCd)
        // this.resultVo = _.cloneDeep(model.row)
      } else {
        this.resultVo = {
        slvlCd: '',
        slvlNm: '',
        sorgOfficeFlagYn: '',
        scomment: '',
      }
      }
    },
    async fnViewupdateLvlCd(slvlCd = null) {
      if (slvlCd === '' || slvlCd === null) {
        return
      }
      try {
        this.viewLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewUpdateTbLvlCdVo, { slvlCd })
        this.resultVo = res.result.data
      } catch (error) {
          this.error(error)
      } finally {
        this.viewLoading = false
      }
    },
    getParameter() {
      const userId = this.$store.state.user.info.suserId
      const { slvlCd, slvlNm, sorgOfficeFlagYn, scomment } = this.resultVo
      const params = { slvlCd, smodifyId: userId, slvlNm, sorgOfficeFlagYn, scomment }
      if (!this.isEdit) {
        Object.assign(params, { screateId: userId, sexLinkUseTypeCd: 'CE0006' })
      }
      return params
    },
    async fnProcess() {
      if (this.resultVo.slvlNm === '') {
        onMessagePopup(this, '파라미터 오류, 계위명을 입력 하세요.')
        return
      }

      if (this.resultVo.sorgOfficeFlagYn === '') {
        onMessagePopup(this, '파라미터 오류, 계위구분을 입력 하세요.')
        return
      }
      const apiKey = this.isEdit ? 'updateTbLvlCdVo' : 'insertTbLvlCdVo'
      try {
        const res = await apiRequestJson(ipmsJsonApis[apiKey], this.getParameter())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, `계위를 정상적으로 ${this.isEdit ? '수정' : '등록'}하였습니다.`)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      }
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
