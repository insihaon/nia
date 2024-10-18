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
        사용자 권한 상세
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
                    <el-input v-model="resultListVo.suserNm" size="mini" type="text" class="txt w-100" readonly="readonly" />
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
                  v-for="(item, index) in resultListVo.tbUserAuthTxnVos"
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
                  <!-- 추후 레거시 코드로 반영 -->
                  <!-- <td class="ellipsis" :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.tbLvlBasVo.ssvcGroupNm">{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.tbLvlBasVo.ssvcObjNm">{{ item.tbLvlBasVo.ssvcObjNm }}</td> -->
                </tr>
              </template>
            </tbody>
          </table>
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
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in resultSubListVo.tbUserAuthTxnSubVos"
                :key="index"
                :class="{
                  last: index === resultSubListVo.length - 1,
                  subbg: (index % 2) !== 0 || index === resultSubListVo.length - 1
                }"
              >
                <td class="first">{{ item.suserNm }}</td>
                <td class="ellipsis" :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
                <td class="ellipsis" :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.tbLvlBasVo.ssvcGroupNm">{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
                <td class="ellipsis" :title="item.tbLvlBasVo.ssvcObjNm">{{ item.tbLvlBasVo.ssvcObjNm }}</td>
                <td style="display: none;">{{ item.suserGradeCd }}</td>
                <td style="display: none;">{{ item.tbLvlBasVo.ssvcLineTypeCd }}</td>
                <td style="display: none;">{{ item.tbLvlBasVo.ssvcGroupCd }}</td>
                <td style="display: none;">{{ item.tbLvlBasVo.ssvcObjCd }}</td>
              </tr>
            </tbody>
          </table>

        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <!-- <template v-if="isCheckGrade"> -->
        <el-button v-if="isAdmin" size="mini" @click.native="fnCancelBtnClick()"> {{ $t('반려') }}  </el-button>
        <el-button v-if="isAdmin" size="mini" @click.native="fnUpdateConfirmBtnClick()"> {{ $t('승인') }}  </el-button>
        <el-button v-if="adminYn === 'Y' || ownerYn === 'Y'" size="mini" @click.native="fnDeleteBtnClick()"> {{ $t('신청취소') }}  </el-button>
        <!-- </template> -->
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

const routeName = 'ModalDetailUserAuth'

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
      resultListVo: {},
      resultSubListVo: {},
      rowGrantSeq: ''

    }
  },
  computed: {
   ...mapState({
      adminYn: state => state.ipms.adminYn,
      ownerYn: state => state.ipms.ownerYn,
    }),
    isCheckGrade() {
      return this.resultListVo.nrequestTypeCd === 'nod001'
    },
    isAdmin() {
      return this.adminYn === 'Y'
    }
  },
  mounted() {
  },
  methods: {
    sipCreate() {
      console.log(this.sipCreateTypeCd)
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.resultListVo = model.row.resultListVo
      this.resultSubListVo = model.row.resultSubListVo
      this.rowGrantSeq = model.row.grantSeq
    },
    async fnCancelBtnClick() { /* 반려 */
      let res
      try {
          const tbUserAuthTxnListVo = {
            srcIpBlockMstVo: {
              nrequestTypeCd: 'node003',
              grantSeq: this.rowGrantSeq,
            }
          }
         res = await apiRequestJson(ipmsJsonApis.confirmGrantSub, tbUserAuthTxnListVo)
          // URL : /opermgmt/grantsubsmgmt/confirmGrantSub.ajax
          if (res.commonMsg === 'SUCCESS') {
           this.$message('권한 신청이 정상적으로 처리되었습니다.')
          }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
    fnDeleteBtnClick() { /* 신청취소 */
        this.$confirm('정말 삭제 하시겠습니까?', '신청취소 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
        const tbUserGrantVo = {
          grantSeq: this.rowGrantSeq
        }
         res = await apiRequestJson(ipmsJsonApis.viewDeleteGrant, tbUserGrantVo)
         // legacy URL : /opermgmt/grantsubsmgmt/viewDeleteGrant.ajax
        if (res.commonMsg === 'SUCCESS') {
          this.$message('권한 신청이 정상적으로 삭제되었습니다.')
          this.$emit('reload')
          this.close()
        }
        } catch (error) {
          this.$message.error({ message: `${res.commonMsg}` })
          console.log(error)
        }
      })
    },
    async fnUpdateConfirmBtnClick() { /* 승인 */
      let rCnt = 0
      let res
      try {
       const tbUserAuthTxnListVo = {
        suserId: this.resultListVo.suserId,
        suserGradeCd: this.resultListVo.suserGradeCd,
        nrequestTypeCd: 'nod002',
        grantSeq: this.rowGrantSeq,
       }

       const tbUserAuthTxnVos = this.resultSubListVo.tbUserAuthTxnSubVos.map(item => {
          const tbUserAuthTxnVo = {
            suserId: item.suserId,
            suserGradeCd: item.suserGradeCd,
            smodifyId: this.$store.state.user.info.suserId,
            screateId: this.$store.state.user.info.suserId
          }

          if (this.resultListVo.suserGradeCd === 'UR0001') {
            tbUserAuthTxnVo.tbLvlBasVo = {
              ssvcLineTypeCd: item.tbLvlBasVo.ssvcLineTypeCd,
              ssvcGroupCd: item.tbLvlBasVo.ssvcGroupCd,
              ssvcObjCd: item.tbLvlBasVo.ssvcObjCd
            }
          }
          return tbUserAuthTxnVo
        })

       tbUserAuthTxnListVo.tbUserAuthTxnVos = tbUserAuthTxnVos
       rCnt++

       if (rCnt === 0) {
        this.$message('등록 할 대상이 없습니다.')
        return
       }

       res = await apiRequestJson(ipmsJsonApis.confirmGrantSub, tbUserAuthTxnListVo)
       // legacy URL : /opermgmt/grantsubsmgmt/confirmGrantSub.ajax
       if (res.commonMsg === 'SUCCESS') {
        this.$message('권한 신청이 정상적으로 처리되었습니다.')
       }
      } catch (error) {
          this.$message.error({ message: `${res.commonMsg}` })
          console.log(error)
      }
    }
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
