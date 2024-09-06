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
        {{ isTitle }}
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>AS번호 사용기관 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th scope="row">고객명</th>
                <td> <el-input v-model="srequestAsCtm" size="mini" />  </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="content_result">
          <h4>AS번호 정보</h4>
          <template v-if="viewType !== 'create'">
            <template v-if="viewType === 'detail' || viewType === 'edit'">
              <table class="tbl_data">
                <colgroup>
                  <col width="15%" />
                  <col width="20%" />
                  <col width="15%" />
                  <col width="15%" />
                  <col width="15%" />
                  <col width="20%" />
                </colgroup>
                <tbody>
                  <tr class="view top">
                    <th class="first" scope="row">상태</th>
                    <td>{{ resultVo.srequestAsTypeNm }}</td>
                    <th scope="row">요청자</th>
                    <td>{{ resultVo.screateNm }}</td>
                    <th scope="row">요청일</th>
                    <td>
                      <el-date-picker v-model="dcreateDt" type="datetime" readonly size="mini" format="yyyy-MM-dd" />
                    </td>
                  </tr>
                  <tr class="last">
                    <th class="first" scope="row">처리자</th>
                    <td colspan="1">{{ resultVo.sapvuserNm }}</td>
                    <th scope="row">처리일</th>
                    <td colspan="3">
                      <el-date-picker v-model="dapvDt" type="datetime" size="mini" readonly format="yyyy-MM-dd" />
                    </td>
                  </tr>
                </tbody>
              </table>
            </template>
          </template>

          <table class="tbl_list entry mt5 node_info my-1">
            <colgroup>
              <col width="15%" />
              <col width="42.5%" />
              <col width="42.5%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"></th>
                <th scope="col">노드1</th>
                <th scope="col">노드2</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">노드</th>
                <td> <el-input v-model="srequestAsObjNm1" :readonly="isReadOnly" size="mini" /></td>
                <td> <el-input v-model="srequestAsObjNm2" :readonly="isReadOnly" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">전용번호</th>
                <td> <el-input v-model="srequestAsObjLlnum1" :readonly="isReadOnly" size="mini" /></td>
                <td> <el-input v-model="srequestAsObjLlnum2" :readonly="isReadOnly" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">개통일</th>
                <td>
                  <el-date-picker v-model="drequestAsObjOpenDt1" :readonly="isReadOnly" type="datetime" size="mini" format="yyyy-MM-dd" />
                </td>
                <td>
                  <el-date-picker v-model="drequestAsObjOpenDt2" :readonly="isReadOnly" type="datetime" size="mini" format="yyyy-MM-dd" />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">보유IP 주소 블록</th>
                <td><el-input v-model="srequestAsObjIpBlock1" :readonly="isReadOnly" size="mini" /></td>
                <td><el-input v-model="srequestAsObjIpBlock2" :readonly="isReadOnly" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기타사항</th>
                <td colspan="2">
                  <textarea
                    v-model="scomment"
                    rows="5"
                    class="w98"
                    :readonly="isReadOnly"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="content_result">
          <h4>고객측 AS 담당자 정보</h4>
          <table class="tbl_data entry mt5 d">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">이름</th>
                <td> <el-input v-model="sasTpicNm" :readonly="isReadOnly" size="mini" /></td>
                <th scope="row">기관명</th>
                <td> <el-input v-model="sasTpicOrg" :readonly="isReadOnly" size="mini" /></td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>매출 효과</h4>
          <table class="tbl_list mt5">
            <colgroup>
              <col width="15%" />
              <col width="42.5%" />
              <col width="42.5%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"></th>
                <th scope="col">기존</th>
                <th scope="col">변경</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">속도/가입 회선 수</th>
                <td><el-input v-model="sexistSpLine" :readonly="isReadOnly" size="mini" /></td>
                <td><el-input v-model="saltSpLine" :readonly="isReadOnly" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">매출액 (단위:만원)</th>
                <td><el-input v-model="nexistSales" :readonly="isReadOnly" size="mini" @input="handleInput('nexistSales', $event)" /></td>
                <td><el-input v-model="naltSales" :readonly="isReadOnly" size="mini" @input="handleInput('naltSales', $event)" /></td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <template v-if="isSuserGrade"></template>
        <el-button v-if="isCreated" size="mini" class="el-icon-document-checked float-left" @click="fnSelectMinNrequestAsSeq('N')">{{ $t('할당') }}</el-button>
        <el-button v-if="isCreated" size="mini" class="float-left" @click="fnRejectPrvAsSubmit()">{{ $t('반려') }}</el-button>
        <el-button v-if="resultVo.srequestAsTypeCd === 'RS0202'" size="mini" class="float-left" @click="fnReturnAsTxmSubmit()">{{ $t('반납') }}</el-button>

        <template v-if="resultVo.screateId === userId">
          <el-button v-if="isCreated" size="mini" @click="fnDeletePrvAsSubmit()">{{ $t('신청 취소') }}</el-button>
          <template v-if="viewType === 'detail' && isCreated">
            <el-button size="mini" class="el-icon-edit" @click="onChangeMode()">{{ $t('수정') }}</el-button>
          </template>
        </template>

        <template v-if="viewType === 'edit'">
          <el-button size="mini" class="el-icon-edit-outline" style="background-color:#2e3574; color : #fff" @click="fnUpdatePrvAsSubmit()">{{ $t('수정') }}</el-button>
        </template>
        <el-button v-if="viewType === 'create'" size="mini" class="el-icon-edit-outline" @click="fnViewInsertPrvAs()">{{ $t('등록') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="isClose()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { mapState } from 'vuex'
const routeName = 'ModalDetailPrivateAs'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {},
      srequestAsCtm: '',
      dcreateDt: '',
      dapvDt: '',
      srequestAsObjNm1: '',
      srequestAsObjNm2: '',
      srequestAsObjLlnum1: '',
      srequestAsObjLlnum2: '',
      drequestAsObjOpenDt1: '',
      drequestAsObjOpenDt2: '',
      srequestAsObjIpBlock1: '',
      srequestAsObjIpBlock2: '',
      scomment: '',
      sasTpicNm: '',
      sasTpicOrg: '',
      sexistSpLine: '',
      saltSpLine: '',
      nexistSales: '',
      naltSales: '',
      viewType: null
    }
  },
  computed: {
    isCreated() {
      return this.resultVo.srequestAsTypeCd === 'RS0201'
    },
    isSuserGrade() {
      return this.suserGradeCd === 'UR0001'
    },
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      userId: state => state.user.info.Uid,
      suserGradeCd: state => state.ipms.suserGradeCd,
    }),
    isReadOnly() {
      return !(this.viewType === 'create' || this.viewType === 'edit')
    },
    isTitle() {
      return this.viewType === 'edit' ? '사설AS 상세 수정' : '사설AS 상세'
    }
  },
   watch: {
    nexistSales(val) {
      this.validateAndCleanInput('nexistSales', val)
    },
    naltSales(val) {
      this.validateAndCleanInput('naltSales', val)
    },
  },
  mounted() {
  },
  methods: {
    handleInput(field, event) {
      this[field] = event
    },
    validateAndCleanInput(field, val) {
      if (val) {
        const reg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z]/
        if (reg.exec(val) !== null) {
          this[field] = val.replace(/[^0-9]/g, '')
        }
      }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.resultVo = model.row || {}
      if (this.viewType === 'create') {
        this.resultVo.screateId = this.resultVo.screateId || ''
      }

      this.onSetValue()
    },
    onSetValue() {
      if (this.viewType === 'detail') {
        const { srequestAsCtm, dcreateDt, dapvDt, srequestAsObjNm1, srequestAsObjNm2,
        srequestAsObjLlnum1, srequestAsObjLlnum2, drequestAsObjOpenDt1,
        drequestAsObjOpenDt2, scomment, sasTpicNm, sasTpicOrg, sexistSpLine, saltSpLine, nexistSales, naltSales } = this.resultVo
        this.srequestAsCtm = srequestAsCtm
        this.dcreateDt = dcreateDt
        this.dapvDt = dapvDt
        this.srequestAsObjNm1 = srequestAsObjNm1
        this.srequestAsObjNm2 = srequestAsObjNm2
        this.srequestAsObjLlnum1 = srequestAsObjLlnum1
        this.srequestAsObjLlnum2 = srequestAsObjLlnum2
        this.drequestAsObjOpenDt1 = drequestAsObjOpenDt1
        this.drequestAsObjOpenDt2 = drequestAsObjOpenDt2
        this.scomment = scomment
        this.sasTpicNm = sasTpicNm
        this.sasTpicOrg = sasTpicOrg
        this.sexistSpLine = sexistSpLine
        this.saltSpLine = saltSpLine
        this.nexistSales = nexistSales
        this.naltSales = naltSales
      } else {
        this.srequestAsCtm = ''
        this.dcreateDt = ''
        this.dapvDt = ''
        this.srequestAsObjNm1 = ''
        this.srequestAsObjNm2 = ''
        this.srequestAsObjLlnum1 = ''
        this.srequestAsObjLlnum2 = ''
        this.drequestAsObjOpenDt1 = ''
        this.drequestAsObjOpenDt2 = ''
        this.scomment = ''
        this.sasTpicNm = ''
        this.sasTpicOrg = ''
        this.sexistSpLine = ''
        this.saltSpLine = ''
        this.nexistSales = ''
        this.naltSales = ''
      }
    },
     onChangeMode() {
      this.viewType = 'edit'
    },
    async fnUpdatePrvAsSubmit() {
      if (this.srequestAsCtm === null || this.srequestAsCtm === '') {
        this.$message('고객명을 입력하세요')
        return
      }

      if (this.srequestAsObjNm1 === null || this.srequestAsObjNm1 === '') {
        this.$message('노드1명을 입력하세요')
        return
      }

      if (this.sasTpicNm === null || this.sasTpicNm === '') {
        this.$message('고객측 AS 담당자를 입력하세요')
        return
      }

      if (this.sasTpicOrg === null || this.sasTpicOrg === '') {
        this.$message('고객측 AS 기관을 입력하세요')
        return
      }

      if (this.sexistSpLine === null || this.sexistSpLine === '') {
        this.$message('기존 속도/가입 회선 수를 입력하세요')
        return
      }
      let res
      try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq,
            smodifyId: this.resultVo.smodifyId,
            srequestAsCtm: this.srequestAsCtm,
            srequestAsObjNm1: this.srequestAsObjNm1,
            srequestAsObjLlnum1: this.srequestAsObjLlnum1,
            srequestAsObjIpBlock1: this.srequestAsObjIpBlock1,
            srequestAsObjNm2: this.srequestAsObjNm2,
            srequestAsObjLlnum2: this.srequestAsObjLlnum2,
            srequestAsObjIpBlock2: this.srequestAsObjIpBlock2,
            scomment: this.scomment,
            sasTpicNm: this.sasTpicNm,
            sasTpicOrg: this.sasTpicOrg,
            sexistSpLine: this.sexistSpLine,
            saltSpLine: this.saltSpLine,
            nexistSales: this.nexistSales,
            naltSales: this.naltSales,
            ...(this.drequestAsObjOpenDt1 !== '' && { drequestAsObjOpenDt1: this.drequestAsObjOpenDt1 }),
            ...(this.drequestAsObjOpenDt2 !== '' && { drequestAsObjOpenDt2: this.drequestAsObjOpenDt2 }),
          }
         res = await apiRequestJson(ipmsJsonApis.updatePrivateAs, tbRequestAsApyTxnVo)
          if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
          this.$message.success({ message: `사설 AS정보가 정상적으로 수정 되었습니다.` })
          this.$emit('reload')
          this.close()
          }
        } catch (error) {
          this.$message.error({ message: `${res.commonMsg}` })
          console.log(error)
        }
    },
    fnReturnAsTxmSubmit() { /* TbRequestAsApyTxn 반납  */
      this.$confirm('반납 하시겠습니까?', '반납 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: this.resultVo.nrequestAssignSeq,
            srequestAsTypeCd: 'RS0204',
            sapvuserId: this.$store.state.user.info.Uid
          }
          const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, tbRequestAsApyTxnVo)
            if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
             this.fnReturnAsMstSubmit(this.resultVo.nrequestAsSeq)
            }
          } catch (error) {
            console.log(error)
          }
        })
    },
    async fnReturnAsMstSubmit(nrequestAsSeq) { /* TbRequestAsMstVo 반납처리 */
      try {
        const tbRequestAsMstVo = {
          nrequestAsSeq: nrequestAsSeq,
          srequestAsTypeCd: 'N',
          srequestAsTypeNm: '미사용',
          smodifyId: this.$store.state.user.info.Uid
        }
        const res = await apiRequestJson(ipmsJsonApis.updateTbRequestAsMst, tbRequestAsMstVo)
        if (res.tbRequestAsMstVo.commonMsg === 'SUCCESS') {
          this.fnInsertAsHist()
        }
      } catch (error) {
        console.log(error)
      }
    },
    fnSelectMinNrequestAsSeq(type) { /* AS번호 조회 */
        this.$confirm('할당 하시겠습니까?', '할당 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
          try {
          const tbRequestAsMstVo = {
            srequestAsTypeCd: type
          }
          const res = await apiRequestJson(ipmsJsonApis.selectMinNrequestAsSeq, tbRequestAsMstVo)
          if (res.tbRequestAsMstVo.commonMsg === 'SUCCESS') {
            const requestSeq = res.tbRequestAsMstVo.nrequestAsSeq
            const serNrequestAsSeq = requestSeq
            if (requestSeq !== '' || requestSeq !== null) {
              this.fnAllocAsTxnSubmit(this.resultVo.nrequestAsApyTxnSeq, serNrequestAsSeq)
            }
          }
        } catch (error) {
           console.log(error)
        }
      })
    },
   async fnAllocAsTxnSubmit(nrequestAsApyTxnSeq, serNrequestAsSeq) { /* TbRequestAsApyTxn 할당처리 */
      try {
        const TbRequestAsApyTxnVo = {
          nrequestAsApyTcnSeq: nrequestAsApyTxnSeq,
          nrequestAsSeq: serNrequestAsSeq,
          srequestAsTypeCd: 'RS0202',
          sapvuserId: this.$store.state.user.info.Uid,
          screateId: this.resultVo.screateId,
        }
        const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, TbRequestAsApyTxnVo)
        if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
        this.fnAllocAsMstSubmit(serNrequestAsSeq)
        }
      } catch (error) {
        console.log(error)
      }
    },
    async fnAllocAsMstSubmit(serNrequestAsSeq) { /* TbRequestAsMstVo 할당처리 */
      try {
        const tbRequestAsMstVo = {
          nrequestAsSeq: serNrequestAsSeq,
          srequestAsTypeCd: 'Y',
          srequestAsTypeNm: '사용',
          smodifyId: this.$store.state.user.info.Uid
        }
        const res = await apiRequestJson(ipmsJsonApis.updateTbRequestAsMst, tbRequestAsMstVo)
        if (res.tbRequestAsMstVo.commonMsg === 'SUCCESS') {
          this.fnInsertAsHist()
        }
      } catch (error) {
        console.log(error)
      }
    },
    async fnInsertAsHist() { /* 할당/반납시 이력저장  */
      try {
        const tbRequestAsApyTxnVo = {
          nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq
        }
      const res = await apiRequestJson(ipmsJsonApis.insertAsHist, tbRequestAsApyTxnVo)
        if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
          this.$message('사설AS 상태가 정상적으로 변경되었습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        console.log(error)
      }
    },

    async fnViewInsertPrvAs() { /* 사설 AS 등록 */
      if (this.srequestAsCtm === null || this.srequestAsCtm === '') {
        this.$message('고객명을 입력하세요')
        return
      }

      if (this.srequestAsObjNm1 === null || this.srequestAsObjNm1 === '') {
        this.$message('노드1명을 입력하세요')
        return
      }

      if (this.sasTpicNm === null || this.sasTpicNm === '') {
        this.$message('고객측 AS 담당자를 입력하세요')
        return
      }

      if (this.sasTpicOrg === null || this.sasTpicOrg === '') {
        this.$message('고객측 AS 기관을 입력하세요')
        return
      }

      if (this.sexistSpLine === null || this.sexistSpLine === '') {
        this.$message('기존 속도/가입 회선 수를 입력하세요')
        return
      }

      try {
          const tbRequestAsApyTxnVo = {
            smodifyId: this.$store.state.user.info.Uid,
            screateId: this.$store.state.user.info.Uid,
            srequestAsCtm: this.srequestAsCtm,
            srequestAsObjNm1: this.srequestAsObjNm1,
            srequestAsObjLlnum1: this.srequestAsObjLlnum1,
            srequestAsObjIpBlock1: this.srequestAsObjIpBlock1,
            srequestAsObjNm2: this.srequestAsObjNm2,
            srequestAsObjLlnum2: this.srequestAsObjLlnum2,
            srequestAsObjIpBlock2: this.srequestAsObjIpBlock2,
            scomment: this.scomment,
            sasTpicNm: this.sasTpicNm,
            sasTpicOrg: this.sasTpicOrg,
            sexistSpLine: this.sexistSpLine,
            saltSpLine: this.saltSpLine,
            nexistSales: this.nexistSales,
            naltSales: this.naltSales,
            ...(this.drequestAsObjOpenDt1 !== '' && { drequestAsObjOpenDt1: this.drequestAsObjOpenDt1 }),
            ...(this.drequestAsObjOpenDt2 !== '' && { drequestAsObjOpenDt2: this.drequestAsObjOpenDt2 }),
            srequestAsTypeCd: 'RS0201'
          }
        const res = await apiRequestJson(ipmsJsonApis.insertPrivateAs, tbRequestAsApyTxnVo)
        if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
          this.$message('사설AS정보가 정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        this.$message.error({ message: '등록에 실패했습니다.' })
        console.log(error)
      }
    },
    fnRejectPrvAsSubmit() { /* 반려 */
     this.$confirm('반려 하시겠습니까?', '반려 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
        const tbRequestAsApyTxnVo = {
          nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq,
          srequestAsTypeCd: 'RS0203',
          sapvuserId: this.$store.state.user.info.Uid,
          screateId: this.resultVo.createId,
        }
        const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, tbRequestAsApyTxnVo)
        if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
          this.$message('사설AS정보가 정상적으로 반려되었습니다.')
          this.$emit('reload')
          this.close()
        }
        } catch (error) {
          this.$message.error({ message: '반려에 실패했습니다.' })
          console.log(error)
        }
      })
    },
    fnDeletePrvAsSubmit() { /* 신청취소 */
      this.$confirm('신청정보가 삭제됩니다. 정말 신청취소 하시겠습니까?', '신청 정보 저장 알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소'
        }).then(async() => {
          try {
            const tbRequestAsApyTxnVo = {
              nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq
            }
          const res = await apiRequestJson(ipmsJsonApis.deletePrivateAs, tbRequestAsApyTxnVo)
            if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
             this.$message('사설AS정보가 정상적으로 취소되었습니다.')
             this.$emit('reload')
             this.close()
            }
          } catch (error) {
            this.$message.error({ message: '취소에 실패했습니다.' })
            console.log(error)
          }
        })
      },
    isClose() {
        if (this.viewType === 'edit') {
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
.ModalDetailPrivateAs{
  .node_info {
    td {
      text-align: left !important;
    }
  }

  .el-input {
    width: 100%;
  }
}

</style>
