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

        {{ title }}
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry" summary="조회조건선택">
            <caption>조회조건선택</caption>
            <colgroup>
              <col width="15%" /><col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">운용자</th>
                <td>
                  <el-input v-show="false" v-model="resultListVo.suserId" size="mini" onchange="fnUserIdChange();" />
                  <el-input v-show="false" v-model="resultListVo.suserGradeCd" size="mini" />
                  <div class="search w98">
                    <el-input v-model="suserNm" size="mini" type="text" class="txt w-100" readonly="readonly">
                      <template v-if="viewType === 'I'" #suffix>
                        <el-button
                          slot="trigger"
                          size="small"
                          style="font-size: larger; border: none; float: right"
                          icon="el-icon-search"
                          class="font-weight-bolder"
                          @click="fnViewSearchUser()"
                        />
                      </template>
                    </el-input>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <el-button v-if="viewType === 'I'" class="float-right" size="mini" @click="fnSelectUserAuth()">
            조회
          </el-button>
        </div>

        <div class="content_result">
          <h4>권한상세</h4>
          <table id="authTable" class="tbl_list mt5" summary="목록">
            <caption>목록</caption>
            <colgroup>
              <col width="20%" />
              <col width="20%" />
              <col width="20%" />
              <col width="20%" />
              <col width="20%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">사용자명</th>
                <th scope="col">권한등급</th>
                <th scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="resultListDetailVo === ''" class="subbg last">
                <td class="first" colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <template v-else>
                <tr
                  v-for="(item, index) in resultListDetailVo"
                  :key="index"
                  :class="{
                    last: index === resultListDetailVo - 1,
                    subbg: (index % 2) !== 0 || index === resultListDetailVo - 1
                  }"
                >
                  <td class="first">{{ item.suserNm }}</td>
                  <td class="ellipsis" :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                  <!-- 추후에 레거시 코드로 반영 -->
                  <!-- <td class="ellipsis" :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item?.tbLvlBasVo?.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.tbLvlBasVo.ssvcGroupNm">{{ item?.tbLvlBasVo?.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.tbLvlBasVo.ssvcObjNm">{{ item?.tbLvlBasVo?.ssvcObjNm }}</td> -->
                </tr>
              </template>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>권한입력</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="15%" />
              <col width="12%" />
              <col width="58%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">권한등급</th>
                <td>
                  <el-select v-model="suserGradeCode" class="w-100" size="mini">
                    <el-option
                      v-for="item in userGradeOp"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </td>
                <th scope="row">계위</th>
                <td>
                  <ul>
                    <li>
                      <el-select v-model="ssvcLineTypeCd" :disabled="isDisabledLvl1" size="mini" @change="handleChangeLvl1()">
                        <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
                        <el-option
                          v-for="item in ssvcLineTypeNmOp"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </li>
                    <li>
                      <el-select v-model="ssvcGroupCd" :disabled="isDisabledLvl2" name="ssvcGroupCd" size="mini" @change="handleChangeLvl2()">
                        <el-option
                          v-for="item in ssvcGroupNmOp"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </li>
                    <li>
                      <el-select v-model="ssvcObjCd" :disabled="isDisabledLvl3" size="mini">
                        <el-option
                          v-for="item in ssvcObjNmOp"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </li>
                  </ul>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area mt5">
            <el-button v-if="resultListRegistVo.length > 0" size="mini" @click="fnUserAuthDupCheck()">추가</el-button>
          </div>
        </div>

        <div class="content_result mt20">
          <div class="tit_group">
            <h4 class="mt5">등록예정 권한 정보</h4>
          </div>

          <table id="insertTable" class="tbl_list mt5" summary="등록예정정보">
            <caption>등록 예정 정보</caption>
            <colgroup>
              <col width="20%" />
              <col width="15%" />
              <col width="15%" />
              <col width="20%" />
              <col width="15%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">사용자명</th>
                <th scope="col">권한등급</th>
                <th scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
                <th scope="col">삭제</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in resultListRegistVo"
                :key="index"
                :class="{
                  last: index === resultListRegistVo.length - 1,
                  subbg: (index % 2) !== 0 || index === resultListRegistVo.length - 1
                }"
              >
                <td class="first">{{ item.suserNm }}</td>
                <td class="ellipsis" :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
                <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                <!-- <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item?.tbLvlBasVo?.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.ssvcGroupNm">{{ item?.tbLvlBasVo?.ssvcGroupNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm">{{ item?.tbLvlBasVo?.ssvcObjNm }}</td> -->
                <td class="ellipsis" :title="item.ssvcObjNm"> <el-button size="mini" @click="fnDeleteUserAuthPrev(index)">삭제</el-button> </td>
                <td v-show="false">{{ item.suserGradeCd }}</td>
                <!-- <td v-show="false">{{ item?.tbLvlBasVo?.ssvcLineTypeCd }}</td>
                <td v-show="false">{{ item?.tbLvlBasVo?.ssvcGroupCd }}</td>
                <td v-show="false">{{ item?.tbLvlBasVo?.ssvcObjCd }}</td> -->
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="fnUserAuthSave()"> {{ $t('등록') }}  </el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
      <ModalSearchUserId ref="ModalSearchUserId" @selected-value="setSelectedRow" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import ModalSearchUserId from '@/views-ipms/modal/grantmgmt/ModalSearchUserId.vue'
import { mapState } from 'vuex'
import _ from 'lodash'

const routeName = 'ModalMngUserAuth.'

export default {
  name: routeName,
  components: { ModalSearchUserId },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      resultListVo: {},
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
        suserNm: '', // 사용자명
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
      viewType: '',
      suserNm: '',
      suserId: ''
    }
  },
  computed: {
   ...mapState({
      username: (state) => state.user.name,
    }),
    title() {
      return this.viewType === 'U' ? '사용자 권한 정보 등록' : '사용자 권한 등록'
    },
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
      )?.label ?? ''
    },
    ssvcLineTypeCd(newVal) {
      this.selectedUserAuth.ssvcLineTypeNm = this.ssvcLineTypeNmOp.find(/* get value of selected label */
          (item) => item.value === this.ssvcLineTypeCd
      )?.label ?? ''
    },
    ssvcGroupCd(newVal) {
      this.selectedUserAuth.ssvcGroupNm = this.ssvcGroupNmOp.find(/* get value of selected label */
        (item) => item.value === this.ssvcGroupCd
      )?.label ?? ''
    },
    ssvcObjCd(newVal) {
       this.selectedUserAuth.ssvcObjNm = this.ssvcObjNmOp.find(/* get value of selected label */
        (item) => item.value === this.ssvcObjCd
      )?.label ?? ''
    }
  },
  mounted() {
  },
  methods: {
    setSelectedRow(row) {
      this.suserNm = row?.suserNm
      this.suserId = row?.suserId
    },
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
    sipCreate() {
      console.log(this.sipCreateTypeCd)
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      if (model.type === 'U') {
        this.viewModel = model.row
      } else {
        this.viewModel = ''
      }
      this.onSetValue()
    },
    onSetValue() {
      this.resultListVo = _.cloneDeep(this.viewModel?.resultListVo) ?? ''
      this.suserNm = this.resultListVo.suserNm ?? ''
      this.resultListDetailVo = _.cloneDeep(this.viewModel?.resultListVo?.tbUserAuthTxnVos) ?? ''
      this.resultListRegistVo = _.cloneDeep(this.viewModel?.resultListVo?.tbUserAuthTxnVos) ?? ''
      this.suserGradeCode = _.cloneDeep(this.viewModel?.resultListVo?.suserGradeCd) ?? ''
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
      // 권한 추가 로직
      if (this.suserGradeCode === 'UR0003') {
        if (!this.ssvcLineTypeCd) {
          this.$message('서비스망을 입력하세요')
          return
        }
      } else if (this.suserGradeCode === 'UR0004') {
        if (!this.ssvcLineTypeCd) {
          this.$message('서비스망을 입력하세요')
          return
        }
        if (!this.ssvcGroupCd) {
          this.$message('본부를 입력하세요')
          return
        }
      } else if (this.suserGradeCode === 'UR0005') {
        if (!this.ssvcLineTypeCd) {
          this.$message('서비스망을 입력하세요')
          return
        }
        if (!this.ssvcGroupCd) {
          this.$message('본부를 입력하세요')
          return
        }
        if (!this.ssvcObjCd) {
          this.$message('노드를 입력하세요')
          return
        }
      }

      const isCheck = this.resultListRegistVo.every(v =>
        v.suserGradeCd === this.suserGradeCode &&
        v.ssvcLineTypeCd === this.ssvcLineTypeCd &&
        v.ssvcGroupCd === this.ssvcGroupCd &&
        v.ssvcObjCd === this.ssvcObjCd
      )

      if (isCheck) {
        this.$message('기존에 추가된 권한입니다. 다시 선택하여 추가하세요.')
        return
      }

      /* 추가 데이터 */
      const newUserAuth = {
        suserNm: this.suserNm,
        suserGradeNm: this.userGradeOp.find(item => item.value === this.suserGradeCode)?.label ?? '',
        ssvcLineTypeNm: this.ssvcLineTypeNmOp.find(item => item.value === this.ssvcLineTypeCd)?.label ?? '',
        ssvcGroupNm: this.ssvcGroupNmOp.find(item => item.value === this.ssvcGroupCd)?.label ?? '',
        ssvcObjNm: this.ssvcObjNmOp.find(item => item.value === this.ssvcObjCd)?.label ?? '',
        suserGradeCd: this.suserGradeCode,
        ssvcLineTypeCd: this.ssvcLineTypeCd,
        ssvcGroupCd: this.ssvcGroupCd,
        ssvcObjCd: this.ssvcObjCd
      }

        this.$set(this.resultListRegistVo, 0, newUserAuth)

      this.$message('권한이 추가되었습니다.')
    },
    fnDeleteUserAuthPrev(index) { /* 등록예정 권한 정보 삭제 */
      this.resultListRegistVo.splice(index, 1)
      this.isDeleteMode = true
    },
    async fnUserAuthSave() { /* 등록 */
      if (this.resultListRegistVo.length === 0) {
        this.$message('등록할 대상이 없습니다.')
        return
      }
      let res
      try {
         const tbUserAuthListVo = {
          tbUserAuthTxnVos: [],
          suserId: this.resultListVo.suserId,
          suserGradeCd: this.resultListVo.suserGradeCd
        }

      this.resultListRegistVo.forEach((item) => {
        const tbUserAuthVo = {
          suserNm: this.resultListVo.suserNm,
          suserId: this.resultListVo.suserId,
          suserGradeCd: item.suserGradeCd,
          smodifyId: this.$store.state.user.info.suserId,
          screateId: this.$store.state.user.info.suserId
        }

        if (this.suserGradeCd !== 'UR0001') {
          tbUserAuthVo.tbLvlBasVo = {
            ssvcLineTypeCd: this.ssvcLineTypeCd ?? '000000',
            ssvcGroupCd: this.ssvcGroupCd ?? '000000',
            ssvcObjCd: this.ssvcObjCd ?? '000000'
          }
        }

      tbUserAuthListVo.tbUserAuthTxnVos.push(tbUserAuthVo)
    })
        res = await apiRequestJson(ipmsJsonApis.insertUserAuthTxn, tbUserAuthListVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('권한 신청이 정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.$message.error({ message: `${res.commonMsg}` })
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnViewSearchUser() { /* 유저 찾기 */
     this.$refs.ModalSearchUserId.open()
    },
    async fnSelectUserAuth() {
      if (this.suserNm === '') {
        this.$message('운용자를 먼저 선택하세요')
        return
      }

      const tbuserAuthVo = {
        suserId: this.suserId,
      }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertUserAuth, tbuserAuthVo)

        // if (res.commonMsg === 'SUCCESS') {
          this.resultListDetailVo = res.result.data.map((v) => {
            return {
              suserNm: this.suserNm,
              suserGradeNm: v.suserGradeNm ?? '',
              ssvcLineTypeNm: v.ssvcLineTypeNm ?? '',
              ssvcGroupNm: v.ssvcGroupNm ?? '',
              ssvcObjNm: v.ssvcObjNm ?? '',
            }
          })

          this.resultListRegistVo = res.result.data.map((v) => {
              return {
                suserNm: this.suserNm,
                suserGradeNm: v.suserGradeNm ?? '',
                ssvcLineTypeNm: v.ssvcLineTypeNm ?? '',
                ssvcGroupNm: v.ssvcGroupNm ?? '',
                ssvcObjNm: v.ssvcObjNm ?? '',
              }
            })

            this.suserGradeCode = this.userGradeOp.find((option) => option.label === res.result.data[0]?.suserGradeNm)?.value ?? ''

            this.ssvcLineTypeCd = this.ssvcLineTypeNmOp.find((option) => option.label === res.result.data[0]?.ssvcLineTypeNm)?.value ?? ''

            this.ssvcGroupCd = this.ssvcGroupNmOp.find((option) => option.label === res.result.data[0]?.ssvcGroupNm)?.value ?? ''

            this.ssvcObjCd = this.ssvcObjNmOp.find((option) => option.label === res.result.data[0]?.ssvcObjNm)?.value ?? ''

          this.$message('데이터가 성공적으로 조회되었습니다.')
            // } else {
              // this.$message(`${res.commonMsg}`)
            // }
      } catch (error) {
        console.error(error)
      }
    },
    onClose() {
      this.ssvcLineTypeCd = ''
      this.ssvcGroupCd = ''
      this.ssvcGroupNmOp = []
      this.ssvcObjCd = ''
      this.ssvcObjNmOp = []
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
