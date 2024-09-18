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
        사용자 상세정보
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data">
            <colgroup>
              <col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">이름</th>
                <td>{{ tbUserBasVo.suserNm }}</td>
                <th scope="row">이메일</th>
                <td> {{ tbUserBasVo.suserEmailAdr }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">소속부서</th>
                <td>{{ tbUserBasVo.sposDeptOrgNm }}</td>
                <th scope="row">직속상급자 명</th>
                <td>{{ tbUserBasVo.sdibelUclsUserNm }} </td>
              </tr>

              <tr>
                <th class="first" scope="row">사용자권한등급</th>
                <td colspan="3">
                  {{ tbUserBasVo.suserGradeNm }}
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3"> {{ tbUserBasVo.suserRmark }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="float-left" @click="fnLoginFailInit()">
          비밀번호 실패횟수 초기화
        </el-button>
        <el-button size="mini" class="float-left" @click="fnViewUserHndSetList()">
          사용자 접속 IP 변경
        </el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalUpdateUserConIp ref="ModalUpdateUserConIp" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson, ipmsModelApis, apiRequestModel } from '@/api/ipms'
import _ from 'lodash'
import ModalUpdateUserConIp from '@/views-ipms/modal/usermgmt/ModalUpdateUserConIp.vue'

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
  computed: {
  },
  mounted() {
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
    async fnViewUserHndSetList() { /* 사용자 접속 IP 변경 */
      try {
        const hndSetVo = {
          suserId: this.tbUserBasVo.suserId
        }
        const res = await apiRequestModel(ipmsModelApis.viewListUserHndSetTxn, hndSetVo)
        this.$refs.ModalUpdateUserConIp.open({ row: res.result })
      } catch (error) {
        console.error(error)
      }
    },
    async fnLoginFailInit(tbUserBasVo) { /* 비밀번호 초기화 */
      if (tbUserBasVo.suserId === '' || tbUserBasVo.suserId === null) {
        return
      }
      const userBasVo = {
        suserId: tbUserBasVo.suserId,
        nloginFailTmscnt: 0,
        smodifyId: this.$store.state.user.Uid,
      }

      try {
       const res = await apiRequestJson(ipmsJsonApis.updateTbUserBas, userBasVo)
       if (res.commonMsg === 'SUCCESS') {
        this.$message('로그인 실패 횟수를 초기화 하였습니다.')
       } else {
        this.$message(res.commonMsg)
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
