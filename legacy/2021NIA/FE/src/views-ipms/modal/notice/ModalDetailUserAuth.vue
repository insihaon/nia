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
    <div ref="content" class="popupContentTable textcenter">
      <div class="popupContentTableTitle">권한상세</div>
      <table>
        <tr>
          <th>사용자명</th>
          <th>권한등급</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
        </tr>
        <tr v-if="totalCount === 0">
          <td colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
        </tr>
        <template v-else>
          <tr
            v-for="(item, index) in resultListVo.tbUserAuthTxnVos"
            :key="index"
            :class="{
              last: index === resultSubListVo.tbUserAuthTxnSubVos.length - 1,
              subbg: (index % 2) !== 0 || index === resultSubListVo.tbUserAuthTxnSubVos.length - 1
            }"
          >
            <td>{{ item.suserNm }}</td>
            <td :title="item.suserGradeNm">{{ item.suserGradeNm }}</td>
            <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
            <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
            <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
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
          <td :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
          <td :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
          <td :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <template v-if="isCheckGrade">
        <el-button v-if="isAdmin" type="primary" size="small" round @click.native="fnCancelBtnClick()">반려</el-button>
        <el-button v-if="isAdmin" type="primary" size="small" round @click.native="fnUpdateConfirmBtnClick()">승인</el-button>
        <el-button v-if="adminYn === 'Y' || ownerYn === 'Y'" type="primary" size="small" round @click.native="fnDeleteBtnClick()">신청취소</el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

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
      totalCount: 0,
      rowGrantSeq: '',
      adminYn: '',
      ownerYn: ''
    }
  },
  computed: {

    isCheckGrade() {
      return this.resultSubListVo.nrequestTypeCd === 'nod001'
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
      setTimeout(() => {
        if (model.row) {
         this.fnViewDetailGrant(model.row)
        }
      }, 10)
    },
    async fnViewDetailGrant(row) {
      const target = ({ vue: this.$refs.content })
      try {
         this.openLoading(target)
         const { suserId, grantSeq } = row
         const tbUserAuthVo = {
            suserId: suserId,
            grantSeq: grantSeq
         }
         const res = await apiRequestModel(ipmsModelApis.viewDetailUserAuthSubs, tbUserAuthVo)
            this.resultListVo = res.resultListVo
            this.totalCount = res.totalCount
            this.resultSubListVo = res.resultSubListVo
            this.rowGrantSeq = res.grant_seq
            this.adminYn = res.adminYn
            this.ownerYn = res.ownerYn
       } catch (error) {
         console.error(error)
       } finally {
         this.closeLoading(target)
       }
    },
    async fnCancelBtnClick() { /* 반려 */
      let res
      const target = ({ vue: this.$refs.content })
      try {
        this.openLoading(target)
        const tbUserAuthTxnListVo = {
          srcIpBlockMstVo: {
            nrequestTypeCd: 'node003',
            grantSeq: this.rowGrantSeq,
          }
        }
        res = await apiRequestModel(ipmsModelApis.confirmGrantSub, tbUserAuthTxnListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '권한 신청이 정상적으로 처리되었습니다.')
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
         this.closeLoading(target)
      }
    },
    fnDeleteBtnClick() { /* 신청취소 */
        this.$confirm('정말 삭제 하시겠습니까?', '신청취소 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
        }).then(async () => {
        const target = ({ vue: this.$refs.content })
        let res
        try {
          this.openLoading(target)
        const tbUserGrantVo = {
          grantSeq: this.rowGrantSeq
        }
         res = await apiRequestModel(ipmsModelApis.viewDeleteGrant, tbUserGrantVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '권한 신청이 정상적으로 삭제되었습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          console.log(error)
        } finally {
         this.closeLoading(target)
       }
      })
    },
    async fnUpdateConfirmBtnClick() { /* 승인 */
      let rCnt = 0
      let res
      const target = ({ vue: this.$refs.content })
      try {
        this.openLoading(target)
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
        onMessagePopup(this, '등록 할 대상이 없습니다.')
        return
       }

       res = await apiRequestModel(ipmsModelApis.confirmGrantSub, tbUserAuthTxnListVo)
       if (res.commonMsg === 'SUCCESS') {
        onMessagePopup(this, '권한 신청이 정상적으로 처리되었습니다.')
       } else {
         onMessagePopup(this, res.commonMsg)
       }
      } catch (error) {
        console.log(error)
      } finally {
        this.closeLoading(target)
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
