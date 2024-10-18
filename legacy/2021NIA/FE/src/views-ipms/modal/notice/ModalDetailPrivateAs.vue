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
        사설 AS {{ getTitleType }}
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
                <td v-if="!isReadOnly"><el-input v-model="resultVo.srequestAsCtm" size="mini" /></td>
                <td v-else>{{ resultVo.srequestAsCtm }}</td>
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
                      {{ resultVo.dcreateDt }}
                    </td>
                  </tr>
                  <tr class="last">
                    <th class="first" scope="row">처리자</th>
                    <td colspan="1">{{ resultVo.sapvuserNm }}</td>
                    <th scope="row">처리일</th>
                    <td colspan="3">
                      {{ resultVo.dapvDt }}
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
                <td v-if="isReadOnly"> {{ resultVo.srequestAsObjNm1 }}</td>
                <td v-else> <el-input v-model="resultVo.srequestAsObjNm1" size="mini" /></td>
                <td v-if="isReadOnly"> {{ resultVo.srequestAsObjNm2 }}</td>
                <td v-else> <el-input v-model="resultVo.srequestAsObjNm2" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">전용번호</th>
                <td v-if="isReadOnly">{{ resultVo.srequestAsObjLlnum1 }}</td>
                <td v-else><el-input v-model="resultVo.srequestAsObjLlnum1" size="mini" /></td>
                <td v-if="isReadOnly">{{ resultVo.srequestAsObjLlnum2 }}</td>
                <td v-else><el-input v-model="resultVo.srequestAsObjLlnum2" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">개통일</th>
                <td v-if="isReadOnly">{{ resultVo.drequestAsObjOpenDt1 ? moment(resultVo.drequestAsObjOpenDt1).format('YYYY-MM-DD') : '' }}</td>
                <td v-else>
                  <el-date-picker v-model="resultVo.drequestAsObjOpenDt1" type="date" size="mini" format="yyyy-MM-dd" />
                </td>
                <td v-if="isReadOnly">{{ resultVo.drequestAsObjOpenDt2 ? moment(resultVo.drequestAsObjOpenDt2).format('YYYY-MM-DD') : '' }}</td>
                <td v-else>
                  <el-date-picker v-model="resultVo.drequestAsObjOpenDt2" type="date" size="mini" format="yyyy-MM-dd" />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">보유IP 주소 블록</th>
                <td v-if="isReadOnly">{{ resultVo.srequestAsObjIpBlock1 }}</td>
                <td v-else><el-input v-model="resultVo.srequestAsObjIpBlock1" size="mini" /></td>
                <td v-if="isReadOnly">{{ resultVo.srequestAsObjIpBlock2 }}</td>
                <td v-else><el-input v-model="resultVo.srequestAsObjIpBlock2" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기타사항</th>
                <td colspan="2">
                  <textarea
                    v-model="resultVo.scomment"
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
                <td v-if="isReadOnly">{{ resultVo.sasTpicNm }}</td>
                <td v-else><el-input v-model="resultVo.sasTpicNm" size="mini" /></td>
                <th scope="row">기관명</th>
                <td v-if="isReadOnly">{{ resultVo.sasTpicOrg }}</td>
                <td v-else><el-input v-model="resultVo.sasTpicOrg" size="mini" /></td>
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
                <td v-if="isReadOnly">{{ resultVo.sexistSpLine }}</td>
                <td v-else><el-input v-model="resultVo.sexistSpLine" size="mini" /></td>
                <td v-if="isReadOnly">{{ resultVo.saltSpLine }}</td>
                <td v-else><el-input v-model="resultVo.saltSpLine" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">매출액 (단위:만원)</th>
                <td v-if="isReadOnly">{{ resultVo.nexistSales }}</td>
                <td v-else><el-input v-model="resultVo.nexistSales" size="mini" @input="handleInput('nexistSales', $event)" /></td>
                <td v-if="isReadOnly">{{ resultVo.naltSales }}</td>
                <td v-else><el-input v-model="resultVo.naltSales" size="mini" @input="handleInput('naltSales', $event)" /></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <template v-if="isSuserGrade"></template>
        <el-button v-if="isCreated && viewType !== 'edit'" size="mini" class="el-icon-document-checked float-left" @click="fnSelectMinNrequestAsSeq('N')">할당</el-button>
        <el-button v-if="isCreated && viewType !== 'edit'" size="mini" class="float-left" @click="fnRejectPrvAsSubmit()">반려</el-button>
        <el-button v-if="resultVo.srequestAsTypeCd === 'RS0202'" size="mini" class="float-left" @click="fnReturnAsTxmSubmit()">반납</el-button>
        <template v-if="resultVo.screateId === userId">
          <el-button v-if="isCreated && viewType !== 'edit'" size="mini" @click="fnDeletePrvAsSubmit()">신청 취소</el-button>
          <template v-if="viewType === 'detail' && isCreated">
            <el-button size="mini" class="el-icon-edit" @click="onChangeMode()">수정</el-button>
          </template>
        </template>
        <template v-if="viewType === 'edit'">
          <el-button size="mini" class="el-icon-edit-outline" type="primary" @click="fnUpdatePrvAsSubmit()">수정</el-button>
        </template>
        <el-button v-if="viewType === 'create'" size="mini" type="primary" class="el-icon-edit-outline" @click="fnViewInsertPrvAs()">등록</el-button>
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
import { onMessagePopup } from '@/utils/index'
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
      userId: state => state.user.info.suserId,
      suserGradeCd: state => state.user.info.suserGradeCd,
    }),
    isReadOnly() {
      return !(this.viewType === 'create' || this.viewType === 'edit')
    },
    getTitleType() {
      if (this.viewType === 'create') {
        return '등록'
      } else {
        return this.viewType === 'edit' ? '수정' : '상세'
      }
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
      if (model.row) {
        this.resultVo = this._cloneDeep(model.row)
      } else {
        this.resultVo = {
          rowNo: 0,
          typeFlag: '',
          dcreateDt: '',
          screateId: '',
          screateNm: '',
          dmodifyDt: '',
          smodifyId: '',
          smodifyNm: '',
          commonMsg: null,
          screateEmail: null,
          smodifyEmail: null,
          moveType: '',
          moveSearchWrd: '',
          moveSipVersionTypeCd: null,
          url: null,
          menuType: null,
          paramMap: null,
          nrequestAsApyTxnSeq: null,
          nrequestAsSeq: null,
          srequestAsCtm: '',
          srequestAsObjNm1: '',
          srequestAsObjLlnum1: '',
          drequestAsObjOpenDt1: null,
          srequestAsObjIpBlock1: '',
          srequestAsObjNm2: '',
          srequestAsObjLlnum2: null,
          drequestAsObjOpenDt2: null,
          srequestAsObjIpBlock2: null,
          srequestAsTypeCd: '',
          srequestAsTypeNm: '',
          sasTpicNm: '',
          sasTpicOrg: '',
          sexistSpLine: '',
          nexistSales: '',
          saltSpLine: null,
          naltSales: null,
          dapvDt: null,
          sapvuserId: null,
          sapvuserNm: null,
          scomment: ''
        }
      }
      if (this.viewType === 'create') {
        this.resultVo.screateId = this.resultVo.screateId || ''
      }

      this.onSetValue()
    },
    onSetValue() {
    },
     onChangeMode() {
      this.viewType = 'edit'
    },
    async fnUpdatePrvAsSubmit() {
      if (this.srequestAsCtm === null || this.srequestAsCtm === '') {
        onMessagePopup(this, '고객명을 입력하세요')
        return
      }

      if (this.srequestAsObjNm1 === null || this.srequestAsObjNm1 === '') {
        onMessagePopup(this, '노드1명을 입력하세요')
        return
      }

      if (this.sasTpicNm === null || this.sasTpicNm === '') {
        onMessagePopup(this, '고객측 AS 담당자를 입력하세요')
        return
      }

      if (this.sasTpicOrg === null || this.sasTpicOrg === '') {
        onMessagePopup(this, '고객측 AS 기관을 입력하세요')
        return
      }

      if (this.sexistSpLine === null || this.sexistSpLine === '') {
        onMessagePopup(this, '기존 속도/가입 회선 수를 입력하세요')
        return
      }
      try {
        const {
          nrequestAsApyTxnSeq,
          smodifyId,
          srequestAsCtm,
          srequestAsObjNm1,
          srequestAsObjLlnum1,
          srequestAsObjIpBlock1,
          srequestAsObjNm2,
          srequestAsObjLlnum2,
          srequestAsObjIpBlock2,
          scomment,
          sasTpicNm,
          sasTpicOrg,
          sexistSpLine,
          saltSpLine,
          nexistSales,
        } = this.resultVo
        const tbRequestAsApyTxnVo = {
          nrequestAsApyTxnSeq,
          smodifyId,
          srequestAsCtm,
          srequestAsObjNm1,
          srequestAsObjLlnum1,
          srequestAsObjIpBlock1,
          srequestAsObjNm2,
          srequestAsObjLlnum2,
          srequestAsObjIpBlock2,
          scomment,
          sasTpicNm,
          sasTpicOrg,
          sexistSpLine,
          saltSpLine,
          nexistSales,
          naltSales: this.naltSales,
          ...(this.drequestAsObjOpenDt1 !== '' && { drequestAsObjOpenDt1: this.drequestAsObjOpenDt1 }),
          ...(this.drequestAsObjOpenDt2 !== '' && { drequestAsObjOpenDt2: this.drequestAsObjOpenDt2 }),
        }
        const res = await apiRequestJson(ipmsJsonApis.updatePrivateAs, tbRequestAsApyTxnVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '사설 AS정보가 정상적으로 수정 되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnReturnAsTxmSubmit() { /* TbRequestAsApyTxn 반납  */
      this.$confirm('반납 하시겠습니까?', '반납 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq,
            srequestAsTypeCd: 'RS0204',
            sapvuserId: this.$store.state.user.info.suserId
          }
          const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, tbRequestAsApyTxnVo)
            if (res.commonMsg === 'SUCCESS') {
             this.fnReturnAsMstSubmit(this.resultVo.nrequestAsSeq)
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
    },
    async fnReturnAsMstSubmit(nrequestAsSeq) { /* TbRequestAsMstVo 반납처리 */
      try {
        const tbRequestAsMstVo = {
          nrequestAsSeq,
          srequestAsTypeCd: 'N',
          srequestAsTypeNm: '미사용',
          smodifyId: this.$store.state.user.info.suserId
        }
        const res = await apiRequestJson(ipmsJsonApis.updateTbRequestAsMst, tbRequestAsMstVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnInsertAsHist()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
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
          if (res.commonMsg === 'SUCCESS') {
            const serNrequestAsSeq = res.nrequestAsSeq
            if (serNrequestAsSeq !== '' || serNrequestAsSeq !== null) {
              this.fnAllocAsTxnSubmit(this.resultVo.nrequestAsApyTxnSeq, serNrequestAsSeq)
            }
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
           this.error(error)
        }
      })
    },
   async fnAllocAsTxnSubmit(nrequestAsApyTxnSeq, serNrequestAsSeq) { /* TbRequestAsApyTxn 할당처리 */
      try {
        const TbRequestAsApyTxnVo = {
          nrequestAsApyTcnSeq: nrequestAsApyTxnSeq,
          nrequestAsSeq: serNrequestAsSeq,
          srequestAsTypeCd: 'RS0202',
          sapvuserId: this.$store.state.user.info.suserId,
          screateId: this.resultVo.screateId,
        }
        const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, TbRequestAsApyTxnVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnAllocAsMstSubmit(serNrequestAsSeq)
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnAllocAsMstSubmit(serNrequestAsSeq) { /* TbRequestAsMstVo 할당처리 */
      try {
        const tbRequestAsMstVo = {
          nrequestAsSeq: serNrequestAsSeq,
          srequestAsTypeCd: 'Y',
          srequestAsTypeNm: '사용',
          smodifyId: this.$store.state.user.info.suserId
        }
        const res = await apiRequestJson(ipmsJsonApis.updateTbRequestAsMst, tbRequestAsMstVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnInsertAsHist()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertAsHist() { /* 할당/반납시 이력저장  */
      try {
      const res = await apiRequestJson(ipmsJsonApis.insertAsHist, { nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq })
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '사설AS 상태가 정상적으로 변경되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewInsertPrvAs() { /* 사설 AS 등록 */
      if (this.srequestAsCtm === null || this.srequestAsCtm === '') {
        onMessagePopup(this, '고객명을 입력하세요.')
        return
      }
      if (this.srequestAsObjNm1 === null || this.srequestAsObjNm1 === '') {
        onMessagePopup(this, '노드1명을 입력하세요.')
        return
      }
      if (this.sasTpicNm === null || this.sasTpicNm === '') {
        onMessagePopup(this, '고객측 AS 담당자를 입력하세요.')
        return
      }
      if (this.sasTpicOrg === null || this.sasTpicOrg === '') {
        onMessagePopup(this, '고객측 AS 기관을 입력하세요.')
        return
      }
      if (this.sexistSpLine === null || this.sexistSpLine === '') {
        onMessagePopup(this, '기존 속도/가입 회선 수를 입력하세요.')
        return
      }
      try {
        const {
          srequestAsCtm,
          srequestAsObjNm1,
          srequestAsObjLlnum1,
          srequestAsObjIpBlock1,
          srequestAsObjNm2,
          srequestAsObjLlnum2,
          srequestAsObjIpBlock2,
          scomment,
          sasTpicNm,
          sasTpicOrg,
          sexistSpLine,
          saltSpLine,
          nexistSales,
          naltSales,
          drequestAsObjOpenDt1,
          drequestAsObjOpenDt2
        } = this.resultVo
        const tbRequestAsApyTxnVo = {
          smodifyId: this.$store.state.user.info.suserId,
          screateId: this.$store.state.user.info.suserId,
          srequestAsCtm,
          srequestAsObjNm1,
          srequestAsObjLlnum1,
          srequestAsObjIpBlock1,
          srequestAsObjNm2,
          srequestAsObjLlnum2,
          srequestAsObjIpBlock2,
          scomment,
          sasTpicNm,
          sasTpicOrg,
          sexistSpLine,
          saltSpLine,
          nexistSales,
          naltSales,
          ...(drequestAsObjOpenDt1 !== '' && { drequestAsObjOpenDt1 }),
          ...(drequestAsObjOpenDt2 !== '' && { drequestAsObjOpenDt2 }),
          srequestAsTypeCd: 'RS0201'
        }
        const res = await apiRequestJson(ipmsJsonApis.insertPrivateAs, tbRequestAsApyTxnVo)
        if (res.commonMsg === 'SUCCESS') {
          this.onMessagePopup(this, '사설AS정보가 정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
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
            sapvuserId: this.$store.state.user.info.suserId,
            screateId: this.resultVo.createId,
          }
          const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, tbRequestAsApyTxnVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '사설AS정보가 정상적으로 반려되었습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    fnDeletePrvAsSubmit() { /* 신청취소 */
      this.$confirm('신청정보가 삭제됩니다. 정말 신청취소 하시겠습니까?', '신청 정보 저장 알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소'
        }).then(async() => {
          try {
          const res = await apiRequestJson(ipmsJsonApis.deletePrivateAs, { nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq })
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '사설AS정보가 정상적으로 취소되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
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

  .el-input {
    width: 100%;
  }
}

</style>
