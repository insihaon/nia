<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사용자 권한 등록"
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
      <div class="popupContentTableTitle">조회조건선택</div>
      <table>
        <colgroup>
          <col width="15%" /><col width="85%" />
        </colgroup>
        <tbody>
          <tr class="top last">
            <th>운용자</th>
            <td>
              <!-- <el-input v-show="false" v-model="resultListVo.suserId" size="small" @change="fnUserIdChange()" />
              <el-input v-show="false" v-model="resultListVo.suserGradeCd" size="small" /> -->
              <div>
                <el-input v-model="registVo.suserNm" size="small" type="text" readonly>
                  <template v-if="viewType === 'I'" #suffix>
                    <el-button size="small" icon="el-icon-search" @click="fnViewSearchUser()" />
                  </template>
                </el-input>
              </div>
            </td>
            <td>
              <el-button v-if="viewType === 'I'" type="primary" size="small" round @click="fnViewDetailUserAuth()">조회</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-loading="tableLoading" class="popupContentTable textcenter">
      <div class="popupContentTableTitle">권한상세</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
        </colgroup>
        <thead>
          <tr>
            <th>사용자명</th>
            <th>권한등급</th>
            <th>서비스망</th>
            <th>본부</th>
            <th>노드</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="resultListVo.length === 0">
            <td colspan="5" class="textcenter">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(item, index) in resultListVo" :key="index">
              <td>{{ item.suserNm }}</td>
              <td>{{ item.suserGradeNm }}</td>
              <td>{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
              <td>{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
              <td>{{ item.tbLvlBasVo.ssvcObjNm }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">권한입력</div>
      <table>
        <tbody>
          <tr>
            <th>권한등급</th>
            <td>
              <el-select v-model="registVo.suserGradeCd" class="w-100" size="small" @change="resetUserGrade">
                <el-option
                  v-for="item in userGradeOp"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </td>
            <th>계위</th>
            <td v-loading="levelLoading" class="textflex">
              <el-select v-model="ssvcLineTypeCd" :disabled="isSystemMng" size="small" popper-class="ssvcLineTypeNmOp" @change="handleChangeLvl1()">
                <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
                <el-option
                  v-for="item in ssvcLineTypeNmOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select v-model="ssvcGroupCd" :disabled="(isSystemMng || isServiceMng) || ssvcLineTypeCd === ''" size="small" popper-class="ssvcGroupCd" @change="handleChangeLvl2()">
                <el-option v-show="false" value="000000" label="-------" />
                <el-option
                  v-for="item in ssvcGroupNmOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select v-model="ssvcObjCd" :disabled="(isSystemMng || isServiceMng || isCenterMng) || ssvcGroupCd === ''" popper-class="ssvcObjCd" size="small">
                <el-option v-show="false" value="000000" label="-------" />
                <el-option
                  v-for="item in ssvcObjNmOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
            <td>
              <el-button v-if="isShowAddBtn" type="primary" size="small" round @click="fnUserAuthDupCheck()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-loading="tableLoading" class="popupContentTable textcenter">
      <div class="popupContentTableTitle">등록예정 권한 정보</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="15%" />
          <col width="15%" />
          <col width="20%" />
          <col width="15%" />
        </colgroup>
        <thead>
          <tr>
            <th>사용자명</th>
            <th>권한등급</th>
            <th>서비스망</th>
            <th>본부</th>
            <th>노드</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in registListVo" :key="index">
            <td>{{ item.suserNm }}</td>
            <td>{{ item.suserGradeNm }}</td>
            <td>{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
            <td>{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
            <td>{{ item.tbLvlBasVo.ssvcObjNm }}</td>
            <td><el-button type="danger" icon="el-icon-delete" size="small" circle @click="fnDeleteUserAuthPrev(index)" /></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-add" round @click.native="fnUserAuthSave()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalSearchUserId ref="ModalSearchUserId" @selected-value="setSelectedRow" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import ModalSearchUserId from '@/views-ipms/modal/grantmgmt/ModalSearchUserId.vue'
import { mapState } from 'vuex'
import _ from 'lodash'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalMngUserAuth'

export default {
  name: routeName,
  components: { ModalSearchUserId },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      levelLoading: false,
      tableLoading: false,
      isShowAddBtn: false,
      registVo: {
        suserId: '',
        suserNm: '',
        suserGradeCd: ''
      },
      resultVo: {},
      resultListVo: [], /* 권한상세 권한 정보  */
      registListVo: [], /* 등록예정 권한 정보  */
      userGradeOp: [],
      ssvcLineTypeNmOp: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcGroupNmOp: [],
      ssvcObjNmOp: [],
      ssvcLineTypeCd: '',
      ssvcGroupCd: '',
      ssvcObjCd: '',
    }
  },
  computed: {
   ...mapState({
      username: (state) => state.user.name,
    }),
    isSystemMng() {
      return this.registVo.suserGradeCd === 'UR0001'
    },
    isServiceMng() {
      return this.registVo.suserGradeCd === 'UR0003'
    },
    isCenterMng() {
      return this.registVo.suserGradeCd === 'UR0004'
    },
    isNodeMng() {
      return this.registVo.suserGradeCd === 'UR0005'
    },
    ssvcObjNmOptions() {
      return this.ssvcObjNmOp.filter(v => v.label !== '전체' && v.label !== '-------')
    },
    ssvcGroupNmOptions() {
      return this.ssvcGroupNmOp.filter(v => v.label !== '전체' && v.label !== '-------')
    },
    ssvcLineTypeNm() {
      return this.ssvcLineTypeNmOp.find(v => v.value === this.ssvcLineTypeCd).label
    },
    ssvcGroupNm() {
      return this.ssvcGroupNmOp.find(v => v.value === this.ssvcGroupCd)?.label ?? ''
    },
    ssvcObjNm() {
      return this.ssvcObjNmOp.find(v => v.value === this.ssvcObjCd)?.label ?? ''
    },
  },
  mounted() {
    this.onLoadSelectUserGradeCds()
  },
  methods: {
    async onLoadSelectUserGradeCds() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectUserGradeCds, {})
        this.userGradeOp = res?.result?.data.filter(v => v.code !== 'UR0000' && v.code !== 'UR0006') ?? []
      } catch (error) {
        this.error(error)
      }
    },
    setSelectedRow(param) {
      Object.assign(this.registVo, { suserId: param.row.suserId, suserNm: param.row.suserNm })
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.isShowAddBtn = false
      this.viewType = model.type
      if (model.type === 'U') {
        this.resultVo = this._cloneDeep(model.row) /* 하위에서 받은 정보 */
        this.registVo = this._cloneDeep(model.row) /* 등록할 정보 */
        this.fnViewDetailUserAuth()
      } else {
        this.resultListVo = []
        this.registListVo = []
        this.registVo = {
          suserId: '',
          suserNm: '',
          suserGradeCd: 'UR0001'
        }
      }
    },
    onClose() {
      this.resetUserGrade()
    },
    async fnViewDetailUserAuth() {
      if (this.viewType === 'I' && this.registVo.suserNm === '') {
        onMessagePopup(this, '운용자를 먼저 선택하세요')
        return
      }
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewInsertUserAuth, { typeFlag: this.viewType === 'U' ? 'U' : '', suserId: this.registVo.suserId })
        const data = res.result?.data
        this.resultListVo = data.tbUserAuthTxnVos ?? []
        if (this.resultListVo[0].tbLvlBasVo === null) {
          this.resultListVo.map(row => {
            const tbLvlBasVo = { ssvcLineTypeNm: '', ssvcGroupNm: '', ssvcObjNm: '' }
            Object.assign(row, { tbLvlBasVo })
          })
        }
        this.registListVo = this._cloneDeep(this.resultListVo)
        this.registVo.suserGradeCd = data.suserGradeCd === 'UR0006' ? 'UR0001' : data.suserGradeCd
        this.isShowAddBtn = true
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    resetUserGrade() {
      this.ssvcLineTypeCd = ''
      this.ssvcGroupCd = ''
      this.ssvcObjCd = ''
    },
    async handleChangeLvl1() {
      try {
        this.levelLoading = true
        const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, { ssvcLineTypeCd: this.ssvcLineTypeCd })
        this.ssvcGroupNmOp = res?.tbLvlBasVos?.map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
        if (this.isCenterMng || this.isNodeMng) {
          this.ssvcGroupCd = this.ssvcGroupNmOptions[0].value
        } else {
          this.ssvcGroupCd = '000000'
        }
        this.ssvcObjCd = '000000'
        this.handleChangeLvl2()
      } catch (error) {
        this.error(error)
      } finally {
        this.levelLoading = false
      }
    },
    async handleChangeLvl2() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd, ssvcGroupCd: this.ssvcGroupCd }
      try {
        this.levelLoading = true
        const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
        this.ssvcObjNmOp = res?.tbLvlBasVos?.map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
        if (this.isNodeMng) {
          this.ssvcObjCd = this.ssvcObjNmOptions[0].value
        } else {
          this.ssvcObjCd = '000000'
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.levelLoading = false
      }
    },
    fnUserAuthDupCheck() {
      // 권한 추가 로직
      const serviceMsg = '서비스망을 입력하세요'
      const centerMsg = '본부를 입력하세요'
      if (this.isServiceMng && (this.ssvcLineTypeCd === '' || this.ssvcLineTypeCd === null)) {
        onMessagePopup(this, serviceMsg)
        return
      } else if (this.isCenterMng) {
        if (!this.ssvcLineTypeCd) {
          onMessagePopup(this, serviceMsg)
          return
        }
        if (!this.ssvcGroupCd) {
          onMessagePopup(this, centerMsg)
          return
        }
      } else if (this.isNodeMng) {
        if (this.ssvcLineTypeCd === '' || this.ssvcLineTypeCd === null) {
          onMessagePopup(this, serviceMsg)
          return
        }
        if (this.ssvcGroupCd === '' || this.ssvcGroupCd === null) {
          onMessagePopup(this, centerMsg)
          return
        }
        if (this.ssvcObjCd === '' || this.ssvcObjCd === null) {
          onMessagePopup(this, '노드를 입력하세요')
          return
        }
      }
      const isCheck = this.registListVo.map(row => {
        const { ssvcLineTypeNm, ssvcGroupNm, ssvcObjNm } = row.tbLvlBasVo

        if (row.suserGradeCd === this.registVo.suserGradeCd &&
          (ssvcLineTypeNm || '') === this.ssvcLineTypeNm &&
          (ssvcGroupNm || '') === this.ssvcGroupNm &&
          (ssvcObjNm || '') === this.ssvcObjNm) {
          return false
        }
        return true
      })
      if (isCheck.some(bool => bool === false)) {
        onMessagePopup(this, '기존에 추가된 권한입니다. 다시 선택하여 추가하세요.')
        return
      }
      this.fnUserAuthAppend()
    },
    async fnUserAuthAppend() {
      const { suserId, suserNm, suserGradeCd } = this.registVo
      const suserGradeNm = this.userGradeOp.find(item => item.code === suserGradeCd).name
      const newResistVo = {
        suserId, suserNm, suserGradeNm, suserGradeCd,
        tbLvlBasVo: {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcLineTypeNm: this.ssvcLineTypeNm === '' ? '전체' : this.ssvcLineTypeNm,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcGroupNm: this.ssvcGroupNm,
          ssvcObjCd: this.ssvcObjCd,
          ssvcObjNm: this.ssvcObjNm
        }
      }
      if (this.registListVo.length > 0) {
        const newGrade = this.registVo.suserGradeCd
        const oldGrade = this.registListVo[0].suserGradeCd
        if (newGrade !== oldGrade) {
          await this.$confirm('선택한 권한등급이 이전 권한등급과 다릅니다.<br/>다른 권한등급 선택시 이전 권한이 모두 삭제 됩니다.<br/>변경 하시겠습니까 ?', '알림', {
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            type: 'warning',
            dangerouslyUseHTMLString: true
          }).then(() => {
            this.registListVo = [newResistVo]
          }).catch(action => {})
        } else {
          this.registListVo.push(newResistVo)
        }
      } else {
        this.registListVo.push(newResistVo)
      }
    },
    fnDeleteUserAuthPrev(index) { /* 등록예정 권한 정보 삭제 */
      this.registListVo.splice(index, 1)
    },
    async fnUserAuthSave() { /* 등록 */
      if (this.registListVo.length === 0) {
        onMessagePopup(this, '등록할 대상이 없습니다.')
        return
      }
       const tbUserAuthListVo = {
        suserId: this.registVo.suserId,
        suserGradeCd: this.registVo.suserGradeCd
      }
      const tbUserAuthTxnVos = []

      this.registListVo.forEach((item) => {
        const userId = this.$store.state.user.info.suserId
        const tbUserAuthVo = {
          // suserNm: this.resultListVo.suserNm,
          suserId: this.registVo.suserId,
          suserGradeCd: item.suserGradeCd,
          smodifyId: userId,
          screateId: userId
        }

        if (this.suserGradeCd !== 'UR0001') {
          const tbLvlBasVo = {
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            ssvcGroupCd: this.ssvcGroupCd,
            ssvcObjCd: this.ssvcObjCd
          }
          Object.assign(tbUserAuthVo, { tbLvlBasVo })
        }
        tbUserAuthTxnVos.push(tbUserAuthVo)
      })
      Object.assign(tbUserAuthListVo, { tbUserAuthTxnVos })

      try {
        const res = await apiRequestJson(ipmsJsonApis.insertUserAuthTxn, tbUserAuthListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '사용자 권한정보를 정상적으로 등록 하였습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnViewSearchUser() { /* 유저 찾기 */
     this.$refs.ModalSearchUserId.open()
    },
  }
}
</script>
<style lang="scss" scoped>
.ModalMngUserAuth ::v-deep{
  .el-select {
    width: 100%;
  }
}
</style>
