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
        사용자 권한 등록
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
                  <el-input v-show="false" v-model="resultListVo[0].suserId" size="mini" onchange="fnUserIdChange();" />
                  <el-input v-show="false" v-model="resultListVo[0].suserGradeCd" size="mini" />
                  <div class="search w98">
                    <el-input v-model="resultListVo[0].suserNm" size="mini" type="text" class="txt w-100" readonly="readonly" />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>

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
              <tr v-if="resultListVo.totalCount === 0" class="subbg last">
                <td class="first" colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <template v-else>
                <tr
                  v-for="(item, index) in resultListVo"
                  :key="index"
                  :class="{
                    last: index === resultListVo.length - 1,
                    subbg: (index % 2) !== 0 || index === resultListVo.length - 1
                  }"
                >
                  <td class="first">{{ item.suserNm }}</td>
                  <td class="ellipsis" :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
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
                  <el-select v-model="suserGradeCd" class="w-100" size="mini">
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
                        <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll()">전체</span></el-option>
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
            <el-button size="mini" @click="fnUserAuthDupCheck()">추가</el-button>
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
                v-for="(item, index) in resultListVo"
                :key="index"
                :class="{
                  last: index === resultListVo.length - 1,
                  subbg: (index % 2) !== 0 || index === resultListVo.length - 1
                }"
              >
                <td class="first">{{ item.suserNm }}</td>
                <td class="ellipsis" :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
                <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm"> <el-button size="mini">삭제</el-button> </td>
                <td style="display: none;">{{ item.suserGradeCd }}</td>
                <td style="display: none;">{{ item.ssvcLineTypeCd }}</td>
                <td style="display: none;">{{ item.ssvcGroupCd }}</td>
                <td style="display: none;">{{ item.ssvcObjCd }}</td>
              </tr>
            </tbody>
          </table>

        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="fnUpdateConfirmBtnClick()"> {{ $t('등록') }}  </el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { mapState } from 'vuex'

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
      userGradeOp: [
        { label: '시스템 관리자', value: 'UR0001' },
        { label: '서비스망 관리자', value: 'UR0003' },
        { label: '본부운용자', value: 'UR0004' },
        { label: '노드운용자', value: 'UR0005' },
      ],
      suserGradeCd: ''
    }
  },
  computed: {
   ...mapState({
      adminYn: state => state.ipms.adminYn,
      ownerYn: state => state.ipms.ownerYn,
    }),
    isDisabledLvl1() {
      return this.suserGradeCd === 'UR0001'
    },
    isDisabledLvl2() {
      return this.suserGradeCd === 'UR0001' || this.suserGradeCd === 'UR0003'
    },
    isDisabledLvl3() {
      return this.suserGradeCd === 'UR0001' || this.suserGradeCd === 'UR0003' || this.suserGradeCd === 'UR0004'
    }
  },
  watch: {
    suserGradeCd(newVal) {
      this.updateDisabledStates()
    }
  },
  mounted() {
  },
  methods: {
    updateDisabledStates() {
      if (this.suserGradeCd === 'UR0001') {
        this.ssvcLineTypeCd = null
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
      this.resultListVo = model.row
      this.suserGradeCd = this.resultListVo[0].suserGradeCd
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

    },

    /* --------------------- */
    async fnAppendBtnClick() {
      // ip 추가
      if (this.sipCreateTypeCd === '') {
        this.$message('공인/사설이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.ssvcLineTypeCd === '') {
        this.$message('서비스망이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.sipVersionTypeNm === '') {
        this.$message('IP버전이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
      this.viewType = 'generate'
      let res
      try {
          const ipBLockCheckVo = {
            srcIpBlockMstVo: {
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: this.pipPrefix,
            },
            destIpBlockMstVos: []
          }
           res = await apiRequestJson(ipmsJsonApis.appendCrtIPMst, ipBLockCheckVo)
          if (res.commonMsg === 'SUCCESS') {
             const resultData = res
              this.ipBlockDetailList.push({
                pipPrefix: resultData.pipPrefix,
                sfirstAddr: resultData.sfirstAddr,
                slastAddr: resultData.slastAddr,
                nclassCnt: resultData.nclassCnt,
                ncnt: resultData.ncnt,
              })
          }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
  .ModalDetailUserAuth ::v-deep{
    .el-select {
      width: 100%;
    }
  }
</style>
