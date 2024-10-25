<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP 배정신청"
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
          <col width="14%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
        </colgroup>
        <tbody>
          <tr>
            <th>제목</th>
            <td colspan="5">
              <input
                v-model="stitle"
                type="text"
                maxlength="30"
              />
            </td>
          </tr>
          <tr>
            <th>서비스망</th>
            <td>
              <el-select v-model="ssvcLineTypeCd" @change="handleChangeLvl1">
                <el-option v-for="item in svcLineList" :key="item.ssvcLineTypeCd" :value="item.value">
                  {{ item.label }}
                </el-option>
              </el-select>
            </td>
            <th>본부</th>
            <td>
              <el-select v-model="ssvcGroupCd" @change="handleChangeLvl2">
                <el-option v-for="item in centerList" :key="item.ssvcGroupCd" :value="item.ssvcGroupCd">
                  {{ item.ssvcGroupNm }}
                </el-option>
              </el-select>
            </td>
            <th>노드</th>
            <td>
              <el-select v-model="ssvcObjCd">
                <el-option v-for="item in nodeList" :key="item.ssvcObjCd" :value="item.ssvcObjCd">
                  {{ item.ssvcObjNm }}
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>신청자</th>
            <td>{{ userInfo.suserNm }}</td>
            <th>소속부서</th>
            <td colspan="3">{{ userInfo.sposDeptOrgNm }}</td>
          </tr>
          <tr>
            <th>요청 IP개수 (/24)</th>
            <td colspan="5">
              <input
                v-model="napyIpCnt"
                type="text"
                maxlength="5"
              />
              개(/24 단위)
            </td>
          </tr>
          <tr>
            <th>신청내용</th>
            <td colspan="5">
              <textarea
                v-model="scontents"
                rows="10"
              ></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-check" round @click="fnInsertIpAssignApy()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalAssignApyInsert'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      stitle: '',
      ssvcLineTypeCd: '000000',
      ssvcGroupCd: '000000',
      ssvcObjCd: '000000',
      napyIpCnt: '',
      scontents: '',
      svcLineList: [
        { label: '-------', value: '000000' },
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      centerList: [{ ssvcGroupNm: '-------', ssvcGroupCd: '000000' }],
      nodeList: [{ ssvcObjNm: '-------', ssvcObjCd: '000000' }],
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.info
    }),

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
    },
    validateNumberInput() {
      const regExp = /[^0-9a-zA-Z]/g // 숫자만 허용하고 그 외의 모든 문자 제거 (유니코드 플래그 추가)
      this.napyIpCnt = this.napyIpCnt.replace(regExp, '')
    },
    async handleChangeLvl1() {
      this.ssvcGroupCd = '000000'
      this.ssvcObjCd = '000000'
      try {
        const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd }
        const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
        this.centerList = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체')
        this.nodeList = [{ ssvcObjNm: '-------', ssvcObjCd: '000000' }]
      } catch (error) {
        this.error(error)
      }
    },
    async handleChangeLvl2() {
      this.ssvcObjCd = '000000'
      try {
        const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd, ssvcGroupCd: this.ssvcGroupCd }
        const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
        this.nodeList = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체')
      } catch (error) {
        this.error(error)
      }
    },
    fnInsertIpAssignApy() {
      if (this.stitle === null || this.stitle === '') {
        onMessagePopup(this, '제목을 입력 하세요')
        return
      }

      if (this.napyIpCnt === null || this.napyIpCnt === '') {
        onMessagePopup(this, '요청 IP개수를 입력 하세요.')
        return
      }

      if (this.scontents === null || this.scontents === '') {
        onMessagePopup(this, '신청 내용을 입력 하세요.')
        return
      }

      if (this.ssvcLineTypeCd === '000000' || this.ssvcLineTypeCd === '') {
        onMessagePopup(this, '서비스망을 선택해 주세요.')
        return
      }

      if (this.ssvcGroupCd === '000000' || this.ssvcGroupCd === '') {
        onMessagePopup(this, '본부를 선택해 주세요')
        return
      }

      if (this.ssvcObjCd === '000000' || this.ssvcObjCd === '') {
        onMessagePopup(this, '노드를 선택해 주세요')
        return
      }
      this.$confirm('배정 신청 하시겠습니까?', '배정 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const TbRequestAssignMstVo = {
            stitle: this.stitle,
            napyIpCnt: this.napyIpCnt,
            scontents: this.scontents,
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            ssvcGroupCd: this.ssvcGroupCd,
            ssvcObjCd: this.ssvcObjCd,
            sapyUserId: this.$store.state.user.info.suserId,
            srequestAssignTypeCd: 'RS0301',
            screateId: this.$store.state.user.info.suserId,
            smodifyId: this.$store.state.user.info.suserId,
          }
          const res = await apiRequestJson(ipmsJsonApis.insertAssignApyTxn, TbRequestAssignMstVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'IP 배정 신청 내용을 정상적으로 등록 하였습니다.')
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
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
</style>
