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
    <div v-loading="loadingPage" class="popupContentTable">
      <div class="popupContentTableTitle">조회조건 선택</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>운용자</th>
            <td>
              <el-input v-model="suserNm" size="mini" type="text" class="txt w-100" readonly="readonly" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-loading="loadingPage" class="popupContentTable textcenter">
      <div class="popupContentTableTitle">권한 상세</div>
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
          <tr v-if="totalCount === 0">
            <td colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(item, index) in resultDetailListVo" :key="index">
              <td>{{ item.suserNm }}</td>
              <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
              <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
              <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
              <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div v-loading="loadingPage" class="popupContentTable">
      <div class="popupContentTableTitle">권한 입력</div>
      <table>
        <tbody>
          <tr>
            <th>권한등급</th>
            <td>
              <el-select v-model="suserGradeCode" size="small" @change="handleChangeAuthCd()">
                <el-option v-for="item in userGradeOp" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </td>
            <th>계위</th>
            <td class="textflex">
              <el-select v-model="ssvcLineTypeCd" :disabled="isSystemMng" size="small" @change="handleChangeLvl1()">
                <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
                <el-option v-for="item in ssvcLineTypeNmOp" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <el-select v-model="ssvcGroupCd" :disabled="isSystemMng || isServiceMng || ssvcLineTypeCd === ''" size="small" @change="handleChangeLvl2()">
                <el-option v-for="item in ssvcGroupNmOp" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <el-select v-model="ssvcObjCd" :disabled="isSystemMng || isServiceMng || isCenterMng || ssvcGroupCd === ''" size="small">
                <el-option v-for="item in ssvcObjNmOp" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <td>
              <el-button type="primary" size="small" round @click="fnUserAuthDupCheck()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-loading="loadingPage" class="popupContentTable textcenter">
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
          <tr v-for="(item, index) in resultListVo" :key="index">
            <td :title="item.suserNm">{{ item.suserNm }}</td>
            <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
            <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
            <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
            <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
            <td class="textcenter"><el-button class="el-icon-delete" type="danger" size="mini" circle @click.native="fnDeleteUserAuthPrev(index)" /></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-add" round @click.native="fnUserAuthSave()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">
        {{ $t('exit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { mapState } from 'vuex'
import { onMessagePopup } from '@/utils/index'
import _ from 'lodash'

const routeName = 'ModalInsertUserAuth.'

export default {
  name: routeName,
  components: {},
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      resultListVo: [],
      resultDetailListVo: [],
      ssvcLineTypeNmOp: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' },
      ],
      ssvcGroupNmOp: [],
      ssvcObjNmOp: [],
      ssvcLineTypeCd: '',
      ssvcGroupCd: '',
      ssvcObjCd: '',
      userGradeOp: [],
      selectedUserAuth: {
        suserNm: this.$store.state.user.info.suserNm, // 사용자명
        suserGradeNm: '', // 권한등급
        ssvcLineTypeNm: '', // 계위
        ssvcGroupNm: '', // 본부
        ssvcObjNm: '', // 노드
      },
      suserGradeCode: null,
      addTableData: false,
      resultListDetailVo: [],
      isDeleteMode: false,
      totalCount: 0,
      suserNm: '',
      loadingPage: false,
    }
  },
  computed: {
    ...mapState({
      username: (state) => state.user.name,
    }),
    isSystemMng() {
      return this.suserGradeCode === 'UR0001'
    },
    isServiceMng() {
      return this.suserGradeCode === 'UR0003'
    },
    isCenterMng() {
      return this.suserGradeCode === 'UR0004'
    },
  },
  watch: {
    suserGradeCode(newVal) {
      this.selectedUserAuth.suserGradeNm = this.userGradeOp.find((item) => item.code === this.suserGradeCode)?.name
    },
    ssvcLineTypeCd(newVal) {
      this.selectedUserAuth.ssvcLineTypeNm = this.ssvcLineTypeNmOp.find(/* get value of selected label */ (item) => item.value === this.ssvcLineTypeCd)?.label
    },
    ssvcGroupCd(newVal) {
      this.selectedUserAuth.ssvcGroupNm = this.ssvcGroupNmOp.find(/* get value of selected label */ (item) => item.value === this.ssvcGroupCd)?.label
    },
    ssvcObjCd(newVal) {
      this.selectedUserAuth.ssvcObjNm = this.ssvcObjNmOp.find(/* get value of selected label */ (item) => item.value === this.ssvcObjCd)?.label
    },
  },
  mounted() {
    this.updateDisabledStates()
  },
  methods: {
    updateDisabledStates() {
      if (this.suserGradeCd === 'UR0001') {
        this.ssvcLineTypeCd = ''
        this.ssvcGroupCd = ''
        this.ssvcObjCd = ''
      } else if (this.suserGradeCd === 'UR0003') {
        this.ssvcGroupCd = ''
        this.ssvcObjCd = ''
      } else if (this.suserGradeCd === 'UR0004') {
        this.ssvcObjCd = ''
      } else if (this.suserGradeCd === 'UR0005') {
        // v-model 값 유지
      }
    },
    async onLoadSelectUserGradeCds() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectUserGradeCds, {})
        this.userGradeOp = res?.result?.data?.filter(v => v.code !== 'UR0000' && v.code !== 'UR0006') ?? []
      } catch (error) {
        this.error(error)
      }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.$nextTick(() => {
        if (model.type) {
          this.fnViewInsertGrant(model.row)
          this.onLoadSelectUserGradeCds()
        }
      })
    },
    async fnViewInsertGrant(row) {
      try {
        this.loadingPage = true
        const tbUserAuthVo = {
          suserId: this.$store.state.user.info.suserId,
          typeFlag: 'U',
        }
        const res = await apiRequestModel(ipmsModelApis.viewInsertUserAuthSubs, tbUserAuthVo)
        this.resultListVo = res.result.data
        this.selectedUserAuth = this._cloneDeep(this.resultListVo)
        this.resultDetailListVo = this._cloneDeep(this.resultListVo)
        this.suserNm = res.result.data[0].suserNm
        this.totalCount = res.result.totalCount
        this.suserGradeCode = res.result.data[0].suserGradeCd
      } catch (error) {
        console.error(error)
      } finally {
        this.loadingPage = false
      }
    },
    handleChangeAuthCd() {
      this.ssvcLineTypeCd = ''
      this.ssvcGroupCd = ''
      this.ssvcObjCd = ''
    },
    async handleChangeLvl1() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd }

      this.ssvcGroupCd = ''
      this.ssvcObjCd = ''

      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupNmOp = res?.tbLvlBasVos
        ?.filter((v) => v.ssvcGroupNm !== '전체' && v.ssvcGroupNm !== '-------')
        .map((v) => {
          return { value: v.ssvcGroupCd, label: v.ssvcGroupNm }
        })

      this.ssvcObjNmOp = []
    },
    async handleChangeLvl2() {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.ssvcLineTypeCd,
        ssvcGroupCd: this.ssvcGroupCd,
      }

      this.ssvcObjCd = ''

      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjNmOp = res?.tbLvlBasVos
        ?.filter((v) => v.ssvcObjNm !== '전체' && v.ssvcObjNm !== '-------')
        .map((v) => {
          return { value: v.ssvcObjCd, label: v.ssvcObjNm }
        })
    },

    fnUserAuthDupCheck() {
      // 1. 테이블에 항목이 없는 경우
      if (this.resultListVo.length === 0) {
        const newRow = {
          suserNm: this.$store.state.user.info.suserNm,
          suserGradeNm: this.selectedUserAuth.suserGradeNm,
          ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
          ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
          ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
          suserGradeCd: this.suserGradeCode,
        }
        this.resultListVo.push(newRow)
        return
      }

      // 2. 권한에 따른 필수 항목 체크
      if (this.suserGradeCode === 'UR0003' && !this.ssvcLineTypeCd) {
        onMessagePopup(this, '서비스망을 입력하세요')
        return
      } else if (this.suserGradeCode === 'UR0004') {
        if (!this.ssvcLineTypeCd) {
          onMessagePopup(this, '서비스망을 입력하세요')
          return
        }
        if (!this.ssvcGroupCd) {
          onMessagePopup(this, '본부를 입력하세요')
          return
        }
      } else if (this.suserGradeCode === 'UR0005') {
        if (!this.ssvcLineTypeCd) {
          onMessagePopup(this, '서비스망을 입력하세요')
          return
        }
        if (!this.ssvcGroupCd) {
          onMessagePopup(this, '본부를 입력하세요')
          return
        }
        if (!this.ssvcObjCd) {
          onMessagePopup(this, '노드를 입력하세요')
          return
        }
      }

      // 3. 중복 확인 (동일 권한 확인)
      const isDuplicate = this.resultListVo.some((item) => {
        return item.suserGradeCd === this.suserGradeCode && item.ssvcLineTypeNm === this.selectedUserAuth.ssvcLineTypeNm && item.ssvcGroupNm === this.selectedUserAuth.ssvcGroupNm && item.ssvcObjNm === this.selectedUserAuth.ssvcObjNm
      })

      if (isDuplicate) {
        onMessagePopup(this, '기존에 추가된 권한입니다. 다시 선택하여 추가하세요.')
        return
      }

      // 4. 권한 등급이 다를 경우 사용자에게 확인
      if (this.resultListVo[0].suserGradeCd !== this.suserGradeCode) {
        this.$confirm(
          `선택한 권한등급이 이전 권한등급과 다릅니다.
       다른 권한등급 선택 시 이전 권한이 모두 삭제됩니다.
       변경하시겠습니까?`,
          '확인',
          {
            confirmButtonText: '확인',
            cancelButtonText: '취소',
          }
        )
          .then(() => {
            // 기존 권한 초기화 후 새 권한 추가
            this.resultListVo = []
            const newRow = {
              suserNm: this.$store.state.user.info.suserNm,
              suserGradeNm: this.selectedUserAuth.suserGradeNm,
              ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
              ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
              ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
              suserGradeCd: this.suserGradeCode,
            }
            this.resultListVo.push(newRow)
          })
          .catch(() => {
            return
          })
      } else {
        // 권한 등급이 동일할 경우 바로 row 추가
        const newRow = {
          suserNm: this.$store.state.user.info.suserNm,
          suserGradeNm: this.selectedUserAuth.suserGradeNm,
          ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
          ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
          ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
          suserGradeCd: this.suserGradeCode,
        }

        // 최종 중복 확인 및 추가
        const finalDuplicateCheck = this.resultListVo.some((item) => {
          return item.suserGradeCd === newRow.suserGradeCd && item.ssvcLineTypeNm === newRow.ssvcLineTypeNm && item.ssvcGroupNm === newRow.ssvcGroupNm && item.ssvcObjNm === newRow.ssvcObjNm
        })

        if (!finalDuplicateCheck) {
          this.resultListVo.push(newRow)
        } else {
          onMessagePopup(this, '기존에 추가된 권한입니다. 다시 선택하여 추가하세요.')
        }
      }
    },
    fnDeleteUserAuthPrev(index) {
      /* 등록예정 권한 정보 삭제 */
      this.resultListVo.splice(index, 1)
      this.isDeleteMode = true
    },
    async fnUserAuthSave() {
      /* 등록 */
      if (this.resultListVo.length === 0) {
        onMessagePopup(this, '등록할 대상이 없습니다.')
        return
      }
      const target = { vue: this.$refs.content }
      let res
      try {
        this.openLoading(target)
        const tbUserAuthListVo = {
          tbUserAuthTxnSubVos: [],

          suserId: this.resultListVo[0].suserId,
          suserGradeCd: this.resultListVo[0].suserGradeCd,
        }

        this.resultListVo.forEach((item) => {
          const tbUserAuthVo = {
            suserNm: this.$store.state.user.info.suserNm,
            suserId: this.$store.state.user.info.suserId,
            suserGradeCd: item.suserGradeCd,
            smodifyId: this.$store.state.user.info.suserId,
            screateId: this.$store.state.user.info.suserId,
          }

          if (this.suserGradeCode !== 'UR0001') {
          tbUserAuthVo.tbLvlBasVo = {
            ssvcLineTypeCd: this.ssvcLineTypeNmOp.find(
              (opt) => opt.label === item.ssvcLineTypeNm
            )?.value || '000000', // 서비스망 코드
            ssvcGroupCd: this.ssvcGroupNmOp.find(
              (opt) => opt.label === item.ssvcGroupNm
            )?.value || '000000', // 본부 코드
            ssvcObjCd: this.ssvcObjNmOp.find(
              (opt) => opt.label === item.ssvcObjNm
            )?.value || '000000', // 노드 코드
          }
        }

          tbUserAuthListVo.tbUserAuthTxnSubVos.push(tbUserAuthVo)
        })
        res = await apiRequestJson(ipmsJsonApis.insertUserAuthTxnSub, tbUserAuthListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '권한 신청이 정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClose() {
      this.ssvcLineTypeCd = ''
      this.ssvcGroupNmOp = ''
      this.ssvcObjCd = ''
    },
  },
}
</script>
<style lang="scss" scoped></style>
