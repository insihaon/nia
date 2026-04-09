<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사용자 상세정보"
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
      <table>
        <colgroup>
          <col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>이름</th>
            <td>{{ tbUserBasVo.suserNm }}</td>
            <th>이메일</th>
            <td> {{ tbUserBasVo.suserEmailAdr }}</td>
          </tr>
          <tr>
            <th>소속부서</th>
            <td>{{ tbUserBasVo.sposDeptOrgNm }}</td>
            <th>직속상급자 명</th>
            <td>{{ tbUserBasVo.sdibelUclsUserNm }} </td>
          </tr>

          <tr>
            <th>사용자권한등급</th>
            <td colspan="3">
              {{ tbUserBasVo.suserGradeNm }}
            </td>
          </tr>
          <tr>
            <th>비고</th>
            <td colspan="3"> {{ tbUserBasVo.suserRmark }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom d-flex justify-between">
      <div>
        <el-button type="primary" size="small" round @click="fnLoginFailInit()">
          비밀번호 실패횟수 초기화
        </el-button>
        <el-button type="primary" size="small" round @click="onOpenModalUserHndSet()">
          사용자 접속 IP 변경
        </el-button>
      </div>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalUpdateUserConIp ref="ModalUpdateUserConIp" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'
import ModalUpdateUserConIp from '@/views-ipms/modal/usermgmt/ModalUpdateUserConIp.vue'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalDetailTbUserBas'

export default {

  name: routeName,
  components: { ModalUpdateUserConIp },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tbUserBasVo: {},
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      // this.isViewType = model.type
      this.tbUserBasVo = _.cloneDeep(model.row)
    },
    onOpenModalUserHndSet() { /* 사용자 접속 IP 변경 */
    this.$refs.ModalUpdateUserConIp.open({ tbUserBasVo: this.tbUserBasVo })
    },
    async fnLoginFailInit() { /* 비밀번호 초기화 */
      if (this.tbUserBasVo.suserId === '' || this.tbUserBasVo.suserId === null) {
        return
      }
      const userBasVo = {
        suserId: this.tbUserBasVo.suserId,
        nloginFailTmscnt: 0,
        smodifyId: this.$store.state.user.info.suserId,
      }

      try {
       const res = await apiRequestJson(ipmsJsonApis.updateTbUserBas, userBasVo)
       if (res.commonMsg === 'SUCCESS') {
        onMessagePopup(this, '로그인 실패 횟수를 초기화 하였습니다.')
       } else {
        onMessagePopup(this, res.commonMsg)
       }
      } catch (error) {
        this.error(error)
      }
    },

    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
