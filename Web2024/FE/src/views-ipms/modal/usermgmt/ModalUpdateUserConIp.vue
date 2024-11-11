<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사용자 단말 관리"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="true"
    :append-to-body="true"
    :modal="modal"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">IP 승인 현황</div>
      <table>
        <thead>
          <tr>
            <th>상태</th>
            <th>단말고유정보</th>
            <th>승인자</th>
            <th>승인일</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody v-loading="tableLoading">
          <tr v-if="resultListVo.length === 0">
            <td colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(row, index) in resultListVo" :key="index">
              <td>{{ row.shndsetUseSttusNm }}</td>
              <td>{{ row.suserHndsetId }}</td>
              <td>{{ row.shndsetApvUserNm }}</td>
              <td>{{ moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') }}</td>
              <td>
                <el-button type="danger" size="small" icon="el-icon-delete" circle @click="fnDeleteUserHndSet(row.suserId, row.nhndsetApySeq)" />
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">
        IP 등록<br />
        <span style="font-size: 13px;color: #b0b0b0;"> ※ 다중 IP 신청이 가능한 사용자는 승인된 IP가 3개 이상이 되는 경우, 기존 내역 중 일부를 삭제한 후에만 등록이 가능합니다.</span>
      </div>
      <table>
        <colgroup>
          <col width="15%" /><col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>등록 IP</th>
            <td>
              <el-input v-model="txtUserHndSetId" class="w-100" />
            </td>
          </tr>
          <tr>
            <th>변경사유</th>
            <td>
              <el-input v-model="txtHndSetComment" class="w-100" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-add" round @click="fnUserHndSetInsert()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson, ipmsModelApis, apiRequestModel } from '@/api/ipms'
import _ from 'lodash'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalUpdateUserConIp'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableLoading: false,
      resultVo: {},
      resultListVo: [],
      txtUserHndSetId: '',
      txtHndSetComment: '',
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
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.resultVo = _.cloneDeep(model.tbUserBasVo)
      this.fnViewUserHndSetList()
    },
    async fnViewUserHndSetList() { /* IP 승인 현황 조회 */
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewListUserHndSetTxn, { suserId: this.resultVo.suserId })
        this.resultListVo = res.result?.data ?? []
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async fnUserHndSetInsert() { /* 등록 */
      if (this.resultListVo.length >= 3) {
        onMessagePopup(this, '허용 가능 IP개수는 3개 입니다.<br> 현재 등록되어 있는 IP를 삭제 후 등록 해주세요.')
        return
      }

      if (this.txtUserHndSetId === '' || this.txtUserHndSetId === null) {
        onMessagePopup(this, '등록 IP를 입력하세요.')
        return
      }
      const userId = this.$store.state.user.info.suserId
      const hndSetVo = {
          suserId: this.resultVo.suserId,
          suserHndsetId: this.txtUserHndSetId,
          scomment: this.txtHndSetComment,
          shndsetUseSttusCd: 'UI0002',
          shndsetApvUserId: userId,
          screateId: userId,
          smodifyId: userId,
        }
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewInsertUserHndSetTxn, hndSetVo)
        const commonMsg = res.result.data.commonMsg
        if (commonMsg === 'SUCCESS') {
          onMessagePopup(this, '사용자 접속 IP를 정상적으로 등록했습니다.')
          this.resultListVo = res.result.data.tbUserHndsetApyTxnVos ?? []
        } else if (commonMsg === 'DUP') {
          onMessagePopup(this, res.result.data.sComment)
        } else {
          onMessagePopup(this, commonMsg)
          this.close()
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.tableLoading = false
        this.txtUserHndSetId = ''
        this.txtHndSetComment = ''
      }
    },
    async fnDeleteUserHndSet(suserId, nhndsetApySeq) { /* 삭제 */
      if (suserId === '' || suserId === null) {
        return
      }
      if (nhndsetApySeq === '' || nhndsetApySeq === null) {
        return
      }
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewDeleteUserHndSetTxn, { suserId, nhndsetApySeq })
        if (res.result.data.commonMsg === 'SUCCESS') {
         onMessagePopup(this, '사용자 접속 IP를 정상적으로 삭제 했습니다.')
         this.resultListVo = res.result.data.tbUserHndsetApyTxnVos ?? []
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
