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
        IP배정 상세정보
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4 class="mt5">IP배정 상세정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td>
                  {{ ipAssignVo.ssvcLineTypeNm }} - {{ ipAssignVo.ssvcGroupNm }} - {{ ipAssignVo.ssvcObjNm }}
                </td>
                <th class="first" scope="row">공인/사설</th>
                <td>
                  {{ ipAssignVo.sipCreateTypeNm }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">배정상태</th>
                <td>
                  {{ ipAssignVo.sassignLevelNm }}
                </td>
                <th scope="row">서비스</th>
                <td>
                  {{ ipAssignVo.sassignTypeNm }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">IP 버전</th>
                <td>
                  {{ ipAssignVo.sipVersionTypeNm }}
                </td>
                <th scope="row">IP 주소</th>
                <td>
                  {{ ipAssignVo.pipPrefix }}
                </td>
              </tr>

              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea id="insertScomment" v-model="ipAssignVo.scomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>

            </tbody>
          </table>

          <div class="btn_area my-1">
            <span>
              <el-button class="el-icon-edit-outline" size="mini" @click="fnScommentUpdateClick()"> 수정</el-button>
            </span>
          </div>

        </div>

        <div class="content_result mt0">
          <h4 class="mt5">IP 블록 세부 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">시작 IP</th>
                <td>
                  {{ ipAssignVo.sfirstAddr }}
                </td>
                <th scope="row">끝 IP</th>
                <td>
                  {{ ipAssignVo.slastAddr }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">총 IP 수</th>
                <td>
                  {{ ipAssignVo.ncnt }}
                </td>
                <th scope="row">단위블록수</th>
                <td>
                  {{ ipAssignVo.nclassCnt }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">사용 IP 수</th>
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
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-document-checked" @click.native="handleAssignIp()">{{ '배정' }}</el-button>
        <el-button v-if="!disabledBtn" size="mini" class="el-icon-refresh-left" @click.native="fnRetUpdateAsgnIPMst()">{{ '반납' }}</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalIpAssign ref="ModalIpAssign" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import ModalIpAssign from '@/views-ipms/modal/ModalIpAssign.vue'
import { apiRequestJson, ipmsJsonApis } from '@/api/ipms'
const routeName = 'ModalIpAssignDetail'

export default {
  name: routeName,
  components: { ModalIpAssign },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipAssignVo: null,
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
      return this.ipAssignVo.sassignLevelCd === 'IA0001' || this.ipAssignVo.sassignLevelCd === 'IA0002'
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
    this.ipAssignVo = model.row
     this.viewType = this.model.type
    },
    onClose() { this.ipAssignVo = [] },
    handleAssignIp() {
      this.closeOnClickModal = true
      this.$refs.ModalIpAssign.open({ row: this.ipAssignVo, type: 'asgnRoute' })
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
            this.$message({ message: 'IP블록 반납이 정상적으로 처리되었습니다.', type: 'success' })
            this.$emit('reload')
            this.close()
          }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
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
          this.$message('비고 수정이 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
