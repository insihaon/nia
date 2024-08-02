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
        IP블록병합
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4 class="mt5">배정 병합 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" /><col width="30%" /><col width="30%" /><col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td colspan="3">
                  {{ selectedRow.ssvcLineTypeCd }} - {{ selectedRow.ssvcGroupCd }} - {{ selectedRow.ssvcObjCd }}
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">배정상태</th>
                <td>
                  {{ selectedRow.sassignLevelCd }}
                </td>
                <th scope="row">서비스</th>
                <td>
                  {{ selectedRow.sassignTypeCd }}
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
                  <textarea id="updScomment" v-model="scomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4 class="mt5">병합 대상 정보</h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
          </table>

          <table id="baseTable" class="tbl_list my-3" summary="목록">
            <caption>목록</caption>
            <colgroup>
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="12%" />
              <col width="16%" />
              <col width="12%" />
              <col width="10%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">배정범위</th>
                <th scope="col">단위블록수</th>
                <th scope="col">배정상태</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in ipBlockDetailList" :key="index">
                <td>{{ item.ssvcLineTypeNm }}</td>
                <td>{{ item.ssvcGroupNm }}</td>
                <td>{{ item.ssvcObjNm }}</td>
                <td>{{ item.sipCreateTypeNm }}</td>
                <td>{{ item.sassignTypeNm }}</td>
                <td>{{ item.pipPrefix }}</td>
                <td>{{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
                <td>{{ item.nclassCnt }}</td>
                <td>{{ item.sassignLevelNm }}</td>
                <td v-if="false">{{ item.nipAssignMstSeq }}</td>
                <td v-if="false">{{ item.sassignLevelCd }}</td>
                <td v-if="false">{{ item.sipCreateTypeCd }}</td>
                <td v-if="false">{{ item.sipVersionTypeCd }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button icon="el-icon-document-checked" style="background: #2b5890;" type="primary" size="mini" @click.native="fnMergeConfirmBtnClick()">{{ '병합' }}</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'

const routeName = 'ModalIpMerge'

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
      scomment: '',
      ipBlockDetailList: []
    }
  },
  computed: {
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
      this.$set(this, 'selectedRow', model.row)
      this.ipBlockDetailList = this.selectedRow || []
    },

    /* 기존 사설(CT0004)은 유/무선 공용으로 사용, 신규 사설(CT0005)을 사설로 사용 */
    async fnUpdateConfirmBtnClick() { // IP 블록 병합
      var vOrgCreateTypeFlag = 'N'
      if (this.sipCreateTypeCd === 'CT0005') {
        vOrgCreateTypeFlag = 'Y'
      }

      /* 서비스 배정일경우 N/A 체크 처리 */
      if (this.sassignLevelCd === 'IA0004' && this.sassignTypeCd === '-------' /* SA0000 */) {
        onMessagePopup(this, '서비스 배정일 경우 서비스를 ------- 로 선택하실수 없습니다.')
        return
      }

      /* 사설일 경우 망변경 불가 */
      const { ssvcLineTypeCd } = this.selectedRow[0] || []
      if (vOrgCreateTypeFlag === 'Y' && ssvcLineTypeCd !== this.ssvcLineTypeCd) {
        onMessagePopup(this, '배정 대상 블록 중 사설 블록은 망변경 배정을 할 수 없습니다.')
        return
      }

      if (vOrgCreateTypeFlag !== 'Y' && ssvcLineTypeCd !== this.ssvcLineTypeCd) {
        const confirmed = await this.$confirm(
          '기본 블록의 망정보와 배정 선택 망 정보가 다릅니다. 배정하시겠습니까?',
          'IP블록배정',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
        ).catch(() => false)

        if (!confirmed) {
          return
        }
      }

      /*  배정 처리 유형 처리 */
      let typeFlag = null
      if (this.sassignLevelCd === 'IA0004') {
        typeFlag = 'svcassign' // 배정 - 서비스배정
      } else {
        typeFlag = 'assign' // 배정 - 일반배정
      }

      try {
        const param = {
          menuType: 'Aloc',
          srcIpAssignMstVo: {
          pipPrefix: this.selectedRow.pipPrefix,
          sipVersionTypeCd: this.selectedRow.sipVersionTypeCd,
          scomment: this.scomment,
          ssvcLineTypeCd: this.selectedRow.ssvcLineTypeCd,
          ssvcGroupCd: this.selectedRow.ssvcGroupCd,
          ssvcObjCd: this.selectedRow.ssvcObjCd,
          sassignLevelCd: this.selectedRow.sassignLevelCd,
          sassignTypeCd: this.selectedRow.sassignTypeCd,
        },

        destIpAssignMstVos: this.ipBlockDetailList.map(item => ({
          nipAssignMstSeq: item.nipAssignMstSeq,
        })),

        }

        const res = await /* apiIpAssign */(param)
        if (res.success) {
          this.$message('IP블록 병합이 정상적으로 처리되었습니다.')
          this.$emit('reloadData')
        } else {
          this.$message.error({ message: `IP블록 병합에 실패했습니다.` })
        }
      } catch (error) {
        this.$message.error({ message: `IP블록 병합에 실패했습니다.` })
        console.error(error)
      }
  },
    onClose() { this.selectedRow = [] },
  },
}
</script>
<style lang="scss" scoped>
</style>
