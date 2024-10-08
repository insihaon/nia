<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
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
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        사용자 단말 관리
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>IP 승인 현황</h4>
          <table class="tbl_list mt5">
            <caption>IP 승인 현황</caption>
            <colgroup>
              <col width="10%" /><col width="40%" /><col width="15%" /><col width="20%" />
              <col width="15%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">상태</th>
                <th scope="col">단말고유정보</th>
                <th scope="col">승인자</th>
                <th scope="col">승인일</th>
                <th scope="col">삭제</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="tbUserBasVo.totalCount === 0" class="subbg last">
                <td class="first" colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <tr
                v-for="(item, index) in tbUserBasVoList"
                v-else
                :key="index"
                :class="index % 2 === 0 ? '' : 'subbg last'"
              >
                <td class="first">{{ item.shndsetUseSttusNm }}</td>
                <td class="left">{{ item.suserHndsetId }}</td>
                <td>{{ item.shndsetApvUserNm }}</td>
                <td>{{ item.dmodifyDt }}</td>
                <td class="btn_text">
                  <a @click="fnDeleteUserHndSet(item.suserId, item.nhndsetApySeq, index)">삭제</a>
                </td>
                <td style="display: none;">{{ item.suserId }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>
            IP 등록
            <span class="tbl_info ml10 mt5">다중 IP 신청이 가능한 사용자는 승인된 IP가 3개 이상이 되는 경우, 기존 내역 중 일부를 삭제한 후에만 등록이 가능합니다.</span>
          </h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="15%" /><col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">등록 IP</th>
                <td>
                  <el-input v-model="txtUserHndSetId" placeholder="등록할 IP를 입력하세요" class="w-100" />
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">변경사유</th>
                <td>
                  <el-input v-model="txtHndSetComment" class="w-100" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnUserHndSetInsert()">{{ '등록' }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
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
