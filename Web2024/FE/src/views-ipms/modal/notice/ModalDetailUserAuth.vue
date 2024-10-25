<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사용자 권한 상세"
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
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">조회조건 선택</div>
      <table>
        <tr>
          <th>운용자</th>
          <td>
            <el-input v-model="resultListVo.suserNm" size="small" readonly />
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">권한상세</div>
      <table>
        <tr>
          <th>사용자명</th>
          <th>권한등급</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
        </tr>
        <tr v-if="resultListVo.totalCount === 0">
          <td colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
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
            <td>{{ item.suserNm }}</td>
            <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
            <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
            <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
            <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
            <!-- 추후 레거시 코드로 반영 -->
            <!-- <td :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
            <td :title="item.tbLvlBasVo.ssvcGroupNm">{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
            <td :title="item.tbLvlBasVo.ssvcObjNm">{{ item.tbLvlBasVo.ssvcObjNm }}</td> -->
          </tr>
        </template>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">등록예정 권한 정보</div>
      <table>
        <tr>
          <th>사용자명</th>
          <th>권한등급</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
        </tr>
        <tr v-for="(item, index) in resultSubListVo.tbUserAuthTxnSubVos" :key="index">
          <td>{{ item.suserNm }}</td>
          <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
          <td :title="item.tbLvlBasVo.ssvcLineTypeNm">{{ item.tbLvlBasVo.ssvcLineTypeNm }}</td>
          <td :title="item.tbLvlBasVo.ssvcGroupNm">{{ item.tbLvlBasVo.ssvcGroupNm }}</td>
          <td :title="item.tbLvlBasVo.ssvcObjNm">{{ item.tbLvlBasVo.ssvcObjNm }}</td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <!-- <template v-if="isCheckGrade"> -->
      <el-button v-if="isAdmin" type="primary" size="small" round @click.native="fnCancelBtnClick()">반려</el-button>
      <el-button v-if="isAdmin" type="primary" size="small" round @click.native="fnUpdateConfirmBtnClick()">승인</el-button>
      <el-button v-if="adminYn === 'Y' || ownerYn === 'Y'" type="primary" size="small" round @click.native="fnDeleteBtnClick()">신청취소</el-button>
      <!-- </template> -->
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
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
      this.domElement.maxWidth = 800
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
div.popupContentTable table td{
  text-align: center;
}
</style>
