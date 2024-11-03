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
      <div class="popupContentTableTitle">조회조건 선택</div>
      <table>
        <colgroup>
          <col width="15%" /><col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>운용자</th>
            <td>
              <el-input v-model="resultListVo.suserNm" size="small" type="text" readonly="readonly" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
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
          <tr v-if="resultListDetailVo === 0">
            <td colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(item, index) in resultListDetailVo" :key="index">
              <td>{{ item.suserNm }}</td>
              <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
              <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
              <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
              <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
              <!-- 추후에 레거시 코드로 반영 -->
              <!-- <td :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
              <td :title="item.tbLvlBasVo.ssvcGroupNm">{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
              <td :title="item.tbLvlBasVo.ssvcObjNm">{{ item.tbLvlBasVo.ssvcObjNm }}</td> -->
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">권한 입력</div>
      <table>
        <tbody>
          <tr>
            <th>권한등급</th>
            <td>
              <el-select v-model="suserGradeCode" size="small">
                <el-option
                  v-for="item in userGradeOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
            <th>계위</th>
            <td class="textflex">
              <el-select v-model="ssvcLineTypeCd" :disabled="isDisabledLvl1" size="small" @change="handleChangeLvl1()">
                <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
                <el-option
                  v-for="item in ssvcLineTypeNmOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select v-model="ssvcGroupCd" :disabled="isDisabledLvl2" size="small" @change="handleChangeLvl2()">
                <el-option
                  v-for="item in ssvcGroupNmOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select v-model="ssvcObjCd" :disabled="isDisabledLvl3" size="small">
                <el-option
                  v-for="item in ssvcObjNmOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
            <td>
              <el-button type="primary" size="small" round @click="fnUserAuthDupCheck()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
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
          <tr v-for="(item, index) in resultListVo.tbUserAuthTxnVos" :key="index">
            <td>{{ item.suserNm }}</td>
            <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
            <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
            <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
            <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
            <!-- <td :title="item.ssvcLineTypeNm">{{ item?.tbLvlBasVo?.ssvcLineTypeNm }}</td>
            <td :title="item.ssvcGroupNm">{{ item?.tbLvlBasVo?.ssvcGroupNm }}</td>
            <td :title="item.ssvcObjNm">{{ item?.tbLvlBasVo?.ssvcObjNm }}</td> -->
            <td class="textcenter"><el-button class="el-icon-delete" type="danger" size="mini" circle /></td>
            <!-- <td v-show="false">{{ item?.tbLvlBasVo?.ssvcLineTypeCd }}</td>
            <td v-show="false">{{ item?.tbLvlBasVo?.ssvcGroupCd }}</td>
            <td v-show="false">{{ item?.tbLvlBasVo?.ssvcObjCd }}</td> -->
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
  components: { },
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
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcGroupNmOp: [],
      ssvcObjNmOp: [],
      ssvcLineTypeCd: null,
      ssvcGroupCd: null,
      ssvcObjCd: null,
      userGradeOp: [
        { label: '시스템 관리자', value: 'UR0001' },
        { label: '서비스망 관리자', value: 'UR0003' },
        { label: '본부운용자', value: 'UR0004' },
        { label: '노드운용자', value: 'UR0005' },
      ],
       selectedUserAuth: {
        suserNm: this.$store.state.user.info.suserNm, // 사용자명
        suserGradeNm: '', // 권한등급
        ssvcLineTypeNm: '', // 계위
        ssvcGroupNm: '', // 본부
        ssvcObjNm: '' // 노드
      },
      suserGradeCode: null,
      addTableData: false,
      resultListDetailVo: [],
      viewModel: null,
      isDeleteMode: false,
      totalCount: 0
    }
  },
  computed: {
   ...mapState({
      username: (state) => state.user.name,
    }),
    isDisabledLvl1() {
      return this.suserGradeCode === 'UR0001'
    },
    isDisabledLvl2() {
      return this.suserGradeCode === 'UR0001' || this.suserGradeCode === 'UR0003'
    },
    isDisabledLvl3() {
      return this.suserGradeCode === 'UR0001' || this.suserGradeCode === 'UR0003' || this.suserGradeCode === 'UR0004'
    }
  },
  watch: {
    suserGradeCode(newVal) {
      this.updateDisabledStates()
      this.selectedUserAuth.suserGradeNm = this.userGradeOp.find(
        (item) => item.value === this.suserGradeCode
      ).label
    },
    ssvcLineTypeCd(newVal) {
      this.selectedUserAuth.ssvcLineTypeNm = this.ssvcLineTypeNmOp.find(/* get value of selected label */
          (item) => item.value === this.ssvcLineTypeCd
      )?.label
    },
    ssvcGroupCd(newVal) {
      this.selectedUserAuth.ssvcGroupNm = this.ssvcGroupNmOp.find(/* get value of selected label */
        (item) => item.value === this.ssvcGroupCd
      )?.label
    },
    ssvcObjCd(newVal) {
       this.selectedUserAuth.ssvcObjNm = this.ssvcObjNmOp.find(/* get value of selected label */
        (item) => item.value === this.ssvcObjCd
      )?.label
    }
  },
  mounted() {
  },
  methods: {
    updateDisabledStates() {
      if (this.suserGradeCd === 'UR0001') {
        this.ssvcLineTypeCd = ''
        this.ssvcGroupCd = null
        this.ssvcObjCd = null
      } else if (this.suserGradeCd === 'UR0003') {
        this.ssvcGroupCd = null
        this.ssvcObjCd = null
      } else if (this.suserGradeCd === 'UR0004') {
        this.ssvcObjCd = null
      } else if (this.suserGradeCd === 'UR0005') {
        // v-model 값 유지
      }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
       setTimeout(() => {
        if (model.type) {
         this.fnViewInsertGrant(model.row)
        }
      }, 10)

      this.viewModel = model.row
    },
    async fnViewInsertGrant(row) {
      const target = ({ vue: this.$refs.content })
      try {
        this.openLoading(target)
         const tbUserAuthVo = {
            suserId: this.$store.state.user.info.suserId,
            typeFlag: 'U'
         }
         const res = await apiRequestModel(ipmsModelApis.viewInsertUserAuthSubs, tbUserAuthVo)
          this.resultListVo = res.result.data
          this.resultDetailListVo = res.result.data
          this.totalCount = res.result.totalCount
          this.suserGradeCode = res.result.data[0].suserGradeCd
       } catch (error) {
         console.error(error)
       } finally {
         this.closeLoading(target)
       }
    },
    async handleChangeLvl1() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd }

      this.ssvcGroupCd = null
      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체' && v.ssvcGroupNm !== '-------').map(v => {
        return { value: v.ssvcGroupCd, label: v.ssvcGroupNm }
      })

      this.ssvcObjNmOp = []
    },
    async handleChangeLvl2() {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.ssvcLineTypeCd,
        ssvcGroupCd: this.ssvcGroupCd,
      }

      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체' && v.ssvcObjNm !== '-------').map(v => {
        return { value: v.ssvcObjCd, label: v.ssvcObjNm }
      })
    },

   fnUserAuthDupCheck() {
    // 1. 테이블에 항목이 없는 경우
    if (this.resultListVo.length === 0) {
      const newRow = {
            suserNm: this.selectedUserAuth.suserNm,
            suserGradeNm: this.selectedUserAuth.suserGradeNm,
            ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
            ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
            ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
            suserGradeCd: this.suserGradeCode
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

    // 3. 중복 확인
    const isCheck = this.resultListVo.some(v =>
        (v.suserGradeCd ?? null) === (this.suserGradeCode ?? null) &&
        (v.ssvcLineTypeCd ?? null) === (this.ssvcLineTypeCd ?? null) &&
        (v.ssvcGroupCd ?? null) === (this.ssvcGroupCd ?? null) &&
        (v.ssvcObjCd ?? null) === (this.ssvcObjCd ?? null)
    )

    if (isCheck) {
        onMessagePopup(this, '기존에 추가된 권한입니다. 다시 선택하여 추가하세요.')
        return
    }

    // 4. 권한 등급이 다를 경우 사용자에게 확인
    if (this.resultDetailListVo[0].suserGradeCd !== this.suserGradeCode) {
        this.$confirm(
            `선택한 권한등급이 이전 권한등급과 다릅니다.
            다른 권한등급 선택 시 이전 권한이 모두 삭제됩니다.
            변경하시겠습니까?`,
            '확인',
            {
              confirmButtonText: '확인',
              cancelButtonText: '취소',
            }
        ).then(() => {
          this.resultListVo = []
           const newRow = {
              suserNm: this.selectedUserAuth.suserNm,
              suserGradeNm: this.selectedUserAuth.suserGradeNm,
              ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
              ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
              ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
              suserGradeCd: this.suserGradeCode
            }
            this.resultListVo.push(newRow)
        }).catch(() => {
            return
        })
    } else {
        // 권한 등급이 동일할 경우 바로 row 추가
        const newRow = {
          suserNm: this.selectedUserAuth.suserNm,
          suserGradeNm: this.selectedUserAuth.suserGradeNm,
          ssvcLineTypeNm: this.selectedUserAuth.ssvcLineTypeNm,
          ssvcGroupNm: this.selectedUserAuth.ssvcGroupNm,
          ssvcObjNm: this.selectedUserAuth.ssvcObjNm,
          suserGradeCd: this.suserGradeCode
        }
        this.resultListVo.push(newRow)
    }
},
    fnDeleteUserAuthPrev(index) { /* 등록예정 권한 정보 삭제 */
      this.resultListVo.splice(index, 1)
      this.isDeleteMode = true
    },
    async fnUserAuthSave() { /* 등록 */
      if (this.resultListVo.length === 0) {
        onMessagePopup(this, '등록할 대상이 없습니다.')
        return
      }
      const target = ({ vue: this.$refs.content })
      let res
      try {
        this.openLoading(target)
         const tbUserAuthListVo = {
          tbUserAuthTxnVos: [],
          suserId: this.resultListVo.suserId,
          suserGradeCd: this.resultListVo.suserGradeCd
        }

      this.resultListVo.forEach((item) => {
        const tbUserAuthVo = {
          suserNm: this.$store.state.user.info.suserNm,
          suserId: this.$store.state.user.info.suserId,
          suserGradeCd: item.suserGradeCd,
          smodifyId: this.$store.state.user.info.suserId,
          screateId: this.$store.state.user.info.suserId
        }

        // if (this.suserGradeCd !== 'UR0001') {
          tbUserAuthVo.tbLvlBasVo = {
            /* 추가한 row value set */
            // ssvcLineTypeCd: this.ssvcLineTypeNmOp.find((item) => item.value === this.resultListVo[0].ssvcLineTypeNm
            // )?.value ?? '000000',
            // 각 항목을 찾아 값이 없을 경우 기본값 '000000'으로 설정
             ssvcLineTypeCd: this.ssvcLineTypeNmOp.find(
                (item) => item.label === this.resultListVo[0].ssvcLineTypeNm
            )?.value ?? '000000',

             ssvcGroupCd: this.ssvcGroupNmOp.find(
                (item) => item.label === this.resultListVo[0].ssvcGroupNm
            )?.value ?? '000000',

             ssvcObjCd: this.ssvcObjNmOp.find(
                (item) => item.label === this.resultListVo[0].ssvcObjNm
            )?.value ?? '000000'
          }
        // }

      tbUserAuthListVo.tbUserAuthTxnVos.push(tbUserAuthVo)
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
    },
  }
}
</script>
<style lang="scss" scoped>

</style>
