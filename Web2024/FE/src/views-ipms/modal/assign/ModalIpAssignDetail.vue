<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP배정 상세정보"
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
      <div ref="content" class="popupContentTableTitle">IP배정 상세정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr class="top">
            <th>계위</th>
            <td>
              {{ ipAssignVo.ssvcLineTypeNm }} - {{ ipAssignVo.ssvcGroupNm }} - {{ ipAssignVo.ssvcObjNm }}
            </td>
            <th>공인/사설</th>
            <td>
              {{ ipAssignVo.sipCreateTypeNm }}
            </td>
          </tr>
          <tr>
            <th>배정상태</th>
            <td>
              {{ ipAssignVo.sassignLevelNm }}
            </td>
            <th scope="row">서비스</th>
            <td>
              {{ ipAssignVo.sassignTypeNm }}
            </td>
          </tr>
          <tr>
            <th>IP 버전</th>
            <td>
              {{ ipAssignVo.sipVersionTypeNm }}
            </td>
            <th scope="row">IP 주소</th>
            <td>
              {{ ipAssignVo.pipPrefix }}
            </td>
          </tr>
          <tr>
            <th>비고</th>
            <td colspan="3">
              <textarea id="insertScomment" v-model="ipAssignVo.scomment" class="w98" rows="3" maxlength="4000"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="popupContentTableBottom">
        <el-button type="primary" size="small" icon="el-icon-edit-outline" round @click="fnScommentUpdateClick()">수정</el-button>
      </div>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP 블록 세부 정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr>
            <th>시작 IP</th>
            <td>
              {{ ipAssignVo.sfirstAddr }}
            </td>
            <th scope="row">끝 IP</th>
            <td>
              {{ ipAssignVo.slastAddr }}
            </td>
          </tr>
          <tr>
            <th>총 IP 수</th>
            <td>
              {{ ipAssignVo.ncnt }}
            </td>
            <th scope="row">단위블록수</th>
            <td>
              {{ ipAssignVo.nclassCnt }}
            </td>
          </tr>
          <tr>
            <th>사용 IP 수</th>
            <td>
              {{ ipAssignVo.nuseIpCnt }}
            </td>
            <th scope="row">가용 IP 수</th>
            <td>
              {{ ipAssignVo.nfreeIpCnt }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-checked" round @click.native="handleAssignIp()">배정</el-button>
      <el-button v-if="!disabledBtn" type="primary" size="small" icon="el-icon-refresh-left" round @click.native="fnRetUpdateAsgnIPMst()">반납</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
const routeName = 'ModalIpAssignDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipAssignVo: {},
      type: 'create',
      IpBlockDetail: [],
      tableDatas: [],
      ipBlockResult: '',
      description: '',
      viewType: '',
      scomment: '',
      ipAssignList: [],
    }
  },
  computed: {
    disabledBtn() {
      return this.ipAssignVo?.sassignLevelCd === 'IA0001' || this.ipAssignVo?.sassignLevelCd === 'IA0002'
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      this.$nextTick(() => {
        if (model.row) {
          this.fnViewDetailAsgnIPMst(model.row)
        }
        this.viewType = this.model.type
      })
    },
    async fnViewDetailAsgnIPMst(row) {
      const target = ({ vue: this.$refs.content })
        try {
          this.openLoading(target)
          const tbIpAssignMstVo = { nipAssignMstSeq: row.nipAssignMstSeq }
          const res = await apiRequestModel(ipmsModelApis.viewDetailAsgnIPMst, tbIpAssignMstVo)
          this.ipAssignVo = res?.result?.data
        } catch (error) {
          console.error(error)
        } finally {
          this.closeLoading(target)
        }
    },
    onClose() { /* this.ipAssignVo = []  */ },
    handleAssignIp() {
      this.$emit('assignCallBtnClick')
      this.close()
    },
     async fnRetUpdateAsgnIPMst() {
        const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sipCreateTypeCd } = this.ipAssignVo
        const srcIpAssignMstList = { ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd: 'SA0000' }
        if (sipCreateTypeCd === 'CT0005') {
          srcIpAssignMstList.ssvcGroupCd = ssvcGroupCd
          srcIpAssignMstList.ssvcObjCd = ssvcObjCd
          srcIpAssignMstList.sassignLevelCd = 'IA0001' // 미배정
        } else {
          if (ssvcGroupCd === '000000') { // 서비스 망만 있는 경우
            srcIpAssignMstList.ssvcGroupCd = ssvcGroupCd
            srcIpAssignMstList.ssvcObjCd = ssvcObjCd
            srcIpAssignMstList.sassignLevelCd = 'IA0002'
          } else {
            if (ssvcObjCd === '000000') { // 서비스망 / 본부까지 있는 경우
              srcIpAssignMstList.ssvcGroupCd = '000000'
              srcIpAssignMstList.ssvcObjCd = ssvcObjCd
              srcIpAssignMstList.sassignLevelCd = 'IA0002'
            } else { // 서비스망 / 본부 / 노드까지 있는 경우
              srcIpAssignMstList.ssvcGroupCd = ssvcGroupCd
              srcIpAssignMstList.ssvcObjCd = '000000'
              srcIpAssignMstList.sassignLevelCd = 'IA0003'
            }
          }
        }

        const srcIpAssignMstVo = Object.assign(
          { ssvcLineTypeCd }, srcIpAssignMstList
        )

        const tbIpAssignMstComplexVo = {
          srcIpAssignMstVo,
          destIpAssignMstVos: [
            {
              nipAssignMstSeq: this.ipAssignVo.nipAssignMstSeq,
              typeFlag: 'return'
            }
          ]
        }
        let res
       try {
        res = await apiRequestJson(ipmsJsonApis.updateAsgnIPMst, tbIpAssignMstComplexVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'IP블록 반납이 정상적으로 처리되었습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
       } catch (error) {
        console.error(error)
      }
    },
     async fnScommentUpdateClick() {
      const tbIpAssignMstComplexVo = {
        srcIpAssignMstVo: { scomment: this.ipAssignVo.scomment },
        destIpAssignMstVos: [{ nipAssignMstSeq: this.ipAssignVo.nipAssignMstSeq }]
      }
      let res
      try {
         res = await apiRequestJson(ipmsJsonApis.updateScommentAsgnIPMst, tbIpAssignMstComplexVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '비고 수정이 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
           onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
