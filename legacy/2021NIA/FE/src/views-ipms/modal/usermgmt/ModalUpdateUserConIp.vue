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
        <tbody>
          <tr v-if="tbUserBasVo.totalCount === 0">
            <td colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(item, index) in tbUserBasVoList" :key="index">
              <td>{{ item.shndsetUseSttusNm }}</td>
              <td>{{ item.suserHndsetId }}</td>
              <td>{{ item.shndsetApvUserNm }}</td>
              <td>{{ item.dmodifyDt }}</td>
              <td>
                <el-button type="danger" size="small" icon="el-icon-delete" circle @click="fnDeleteUserHndSet(item.suserId, item.nhndsetApySeq, index)" />
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
              <el-input v-model="txtUserHndSetId" placeholder="등록할 IP를 입력하세요" class="w-100" />
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
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'

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
      tbUserBasVo: {},
      tbUserBasVoList: [],
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
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      this.tbUserBasVo = _.cloneDeep(model.row)
      this.tbUserBasVoList = _.cloneDeep(model.row.data)
    },
    async fnUserHndSetInsert() { /* 등록 */
      if (this.tbUserBasVoList.length >= 3) {
        this.$message('허용 가능 IP개수는 3개 입니다.<br> 현재 등록되어 있는 IP를 삭제 후 등록 해주세요.')
        return
      }

      if (this.txtUserHndSetId === '' || this.txtUserHndSetId === null) {
        this.$message('등록 IP를 입력하세요.')
        return
      }

      try {
        const hndSetVo = {
          suserId: this.tbUserBasVoList[0].suserId,
          suserHndsetId: this.txtUserHndSetId,
          scomment: this.txtHndSetComment,
          shndsetUseSttusCd: 'UI0002',
          shndsetApvUserId: this.$store.state.user.info.suserId,
          screateId: this.$store.state.user.info.suserId,
          smodifyId: this.$store.state.user.info.suserId,
        }
        const res = await apiRequestJson(ipmsJsonApis.viewInsertUserHndSetTxn, hndSetVo)
        if (res.commonMsg === this.resultListVo.commonMsg) {
          this.$message('사용자 접속 IP를 정상적으로 등록했습니다.')
        } else if (res.commonMsg === 'DUP') {
          this.$message(`${this.resultListVo.scomment}`)
        } else {
          this.$message(`${res.commonMsg}`)
          this.close()
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnDeleteUserHndSet(suserId, nhndsetApySeq, index) { /* 삭제 */
      if (suserId === '' || suserId === null) {
        return
      }

      if (nhndsetApySeq === '' || nhndsetApySeq === null) {
        return
      }
      const hndSetVo = {
        suserId: suserId,
        nhndsetApySeq: nhndsetApySeq
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.viewDeleteUserHndSetTxn, hndSetVo)
        if (res.commonMsg === 'SUCCESS') {
         this.$message('사용자 접속 IP를 정상적으로 삭제 했습니다.')
         this.tbUserBasVoList.splice(index, 1)
        } else {
          this.$message(`${res.commonMsg}`)
          this.close()
        }
      } catch (error) {
        console.error(error)
      }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
