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
                  <template v-if="selectedRow.ssvcLineTypeCd !== 'CL0001'">
                    {{ selectedRow.ssvcLineTypeNm }} - {{ selectedRow.ssvcGroupNm }} - {{ selectedRow.ssvcObjNm }}
                  </template>
                </td>
                <th class="first" scope="row">공인/사설</th>
                <td>
                  {{ selectedRow.sipCreateTypeNm }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">배정상태</th>
                <td>
                  {{ selectedRow.sassignLevelNm }}
                </td>
                <th scope="row">서비스</th>
                <td>
                  {{ selectedRow.sassignTypeNm }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">IP 버전</th>
                <td>
                  {{ selectedRow.sipVersionTypeNm }}
                </td>
                <th scope="row">IP 주소</th>
                <td>
                  {{ selectedRow.pipPrefix }}
                </td>
              </tr>

              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea id="insertScomment" v-model="scomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>

            </tbody>
          </table>

          <div class="btn_area my-1">
            <span>
              <el-button class="el-icon-edit-outline" size="mini" @click="handleEditComment()"> 수정</el-button>
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
                  {{ selectedRow.sfirstAddr }}
                </td>
                <th scope="row">끝 IP</th>
                <td>
                  {{ selectedRow.slastAddr }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">총 IP 수</th>
                <td>
                  {{ selectedRow.ncnt }}
                </td>
                <th scope="row">단위블록수</th>
                <td>
                  {{ selectedRow.nclassCnt }}
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">사용 IP 수</th>
                <td>
                  {{ selectedRow.nuseIpCnt }}
                </td>
                <th scope="row">가용 IP 수</th>
                <td>
                  {{ selectedRow.nfreeIpCnt }}
                </td>
              </tr>

            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-document-checked" @click.native="handleAssignIp()">{{ '배정' }}</el-button>
        <el-button size="mini" class="el-icon-refresh-left" @click.native="handleReturnIp()">{{ '반납' }}</el-button>
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
      selectedRow: null,
      type: 'create',
      IpBlockDetail: [],
      tableDatas: [],
      ipBlockResult: '',
      description: '',
      viewType: '',
      scomment: ''
    }
  },
  computed: {
  },
  mounted() {
    // this.onloadIpDetailList
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
     this.$set(this, 'selectedRow', model.row)
     this.viewType = this.model.type
    },
    onClose() { this.selectedRow = [] },
    onloadIpDetailList() {
     /*  const { key: seq } = this.selectedRow
      const param = seq
      try {
        const res = await apiSelectIpDetailList(param)
        this.IpBlockDetail = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
    handleAssignIp() {
      // 새로운 모달 열기
      this.closeOnClickModal = true
      this.$refs.ModalIpAssign.open()
    },
     async handleReturnIp() {
        // 사용자에게 반납 확인 대화상자 표시
        this.$confirm('반납하시겠습니까?', 'IP블록반납', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success',
        })

        // 필요한 컬럼 값 설정
        const ssvcLineTypeCd = this.selectedRow.ssvcLineTypeCd
        const ssvcGroupCd = this.selectedRow.ssvcGroupCd
        const ssvcObjCd = this.selectedRow.ssvcObjCd
        const sipCreateTypeCd = this.selectedRow.sipCreateTypeCd

        const tbIpAssignMstComplexVo = {
          srcIpAssignMstVo: {
            ssvcLineTypeCd: ssvcLineTypeCd,
            sassignTypeCd: 'SA0000',
          },
          destIpAssignMstVos: [
            {
              nipAssignMstSeq: this.selectedRow.nipAssignMstSeq,
              typeFlag: 'return'
            }
          ]
        }

        // 조건에 따른 설정
        if (sipCreateTypeCd === 'CT0005') {
          tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcGroupCd = ssvcGroupCd
          tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcObjCd = ssvcObjCd
          tbIpAssignMstComplexVo.srcIpAssignMstVo.sassignLevelCd = 'IA0001' // 미배정
        } else {
          if (ssvcGroupCd === '000000') { // 서비스 망만 있는 경우
            tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcGroupCd = ssvcGroupCd
            tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcObjCd = ssvcObjCd
            tbIpAssignMstComplexVo.srcIpAssignMstVo.sassignLevelCd = 'IA0002'
          } else {
            if (ssvcObjCd === '000000') { // 서비스망 / 본부까지 있는 경우
              tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcGroupCd = '000000'
              tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcObjCd = ssvcObjCd
              tbIpAssignMstComplexVo.srcIpAssignMstVo.sassignLevelCd = 'IA0002'
            } else { // 서비스망 / 본부 / 노드까지 있는 경우
              tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcGroupCd = ssvcGroupCd
              tbIpAssignMstComplexVo.srcIpAssignMstVo.ssvcObjCd = '000000'
              tbIpAssignMstComplexVo.srcIpAssignMstVo.sassignLevelCd = 'IA0003'
            }
          }
        }

        // 서버에 반납 요청
        const res = await /* apiReturnIpAssignList */(tbIpAssignMstComplexVo)
        if (res.success) {
          this.$message({ message: 'IP블록 반납이 정상적으로 처리되었습니다.', type: 'success' })
          this.$emit('reloadData')
        } else {
          this.$message.error({ message: 'IP블록 반납에 실패했습니다.' })
        }
    },
    handleEditComment() {
      // 비고란 수정
        this.confirm('수정하시겠습니까?', 'IP블록반납', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const param = {
            srcIpAssignMstVo: {
              scomment: this.scomment
            },
            destIpAssignMstVos: [
              {
                nipAssignMstSeq: this.selectedRow.nipAssignMstSeq
              }
            ]
          }
          const res = await /* apiEditIpBlockList */(param)
          if (res.success) {
            this.$message('수정이 정상적으로 처리되었습니다.')
            this.$emit('reloadData')
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
