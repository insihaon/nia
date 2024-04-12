<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.maxHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          자가 구성 시험
          <hr />
        </span>
        <div class="d-flex flex-column h-100 rounded justify-between p-3">
          <div class="shadow-sm p-1 mt-2">
            <span class="title"><i class="el-icon-tickets" />기본 정보</span>
            <div class="d-flex p-2">
              <div class="p-1">
                <label>장비 이름</label>
                <el-input v-model="item.nodeName" readonly />
              </div>
              <div class="p-1">
                <label>IP 주소</label>
                <el-input v-model="item.ipAddr" readonly />
              </div>
              <div class="p-1">
                <label>인터페이스</label>
                <el-input v-model="item.ifname" readonly />
              </div>
            </div>
          </div>
          <div class="shadow-sm p-1 mt-3">
            <span class="title"><i class="el-icon-tickets" />CRC/SPEED 체크</span>
            <div class="d-flex p-2">
              <div class="p-1">
                <label>CRC</label>
                <!-- REQUEST_NIA_API -->
                <el-input v-model="badCrc" />
              </div>
              <div class="p-1">
                <label>Speed</label>
                <el-input v-model="if_config.speed" />
              </div>
            </div>
          </div>
          <div class="shadow-sm p-1 mt-3 input">
            <span class="title"><i class="el-icon-tickets" />IP 불일치 체크</span>
            <div class="w-100 d-flex justify-around">
              <div class="w-50 d-flex justify-center items-center flex-column">
                <span class="font-bold text-center w-100 ip-title">장비 설정 IP정보</span>
                <div style="height: 130px" />
              </div>
              <div class="w-50 d-flex justify-center items-center flex-column">
                <span class="font-bold text-center w-100 ip-title">이용기관 등록 IP정보</span>
                <div class="overflow-y-auto w-100 text-center" style="height: 130px">
                  <div v-for="agencyItem in agencyIpList" :key="agencyItem.nren_ip" style="color: rgb(234, 78, 78)" class="font-bold">
                    {{ agencyItem.nren_ip }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="shadow-sm p-1 mt-3 input">
            <span class="title"><i class="el-icon-tickets" />원격제어</span>
            <div class="d-flex">
              <el-select v-model="remoteControl">
                <el-option v-for="op in remoteOptions" :key="op.value" :label="op.label" :value="op.value" />
              </el-select>
              <el-input v-model="remoteParam" placeholder="입력 파라미터" :disabled="['shoutdown', 'noshut'].includes(remoteControl)" />
              <el-button size="small" :disabled="remoteControl.length === 0" @click="onClickRemote()">실행 </el-button>
            </div>
            <div v-if="isShowFrame" v-loading="frameLoading" style="height: 200px; width: 100%">
              <iframe id="ping" scrolling="yes" :src="pingResultSrc" width="100%" height="100%" frameBorder="0" />
            </div>
            <!-- ng-show="controlSelect=='ping' && showPingTxt" -->
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import { apiIpsdnRequest, apiSelectAgencyIpList, apiRemote } from '@/api/nia'

const routeName = 'ModalConfigTest'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {},
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      selectedRow: null,
      remoteOptions: [
        { value: 'shoutdown', label: 'shoutdown' },
        { value: 'noshut', label: 'noshut' }, // port up
        { value: 'ping', label: 'ping' },
        // { value: 'ACL', label: 'ACL deny' }, // 접근제어 - 원격 차단
      ],
      remoteControl: '',
      remoteParam: '',
      badCrc: '',
      if_config: { speed: '' },
      item: {
        nodeName: '',
        ipAddr: '',
        ifname: '',
      },
      agencyIpList: [],
      isShowFrame: false,
      frameLoading: false,
      pingFileName: null,
      pingResultSrc: '',
    }
  },
  computed: {
    actionForm() {
      return [
        { label: '장애구분', model: 'faultClassify', options: this.selectOption.gubun },
        { label: '장애유형', model: 'faultType', options: this.selectOption.type },
        { label: '조치내용', model: 'faultDetail', options: this.selectOption.content },
      ]
    },
  },
  watch: {
    pingFileName(nVal, oVal) {
      if (nVal === null) {
        this.isShowFrame = false
        this.pingResultSrc = ''
      } else {
        const host = this.$store.getters.server.sshHost
        this.pingResultSrc = `http://${host}:18888/ping/${this.pingFileName}`
      }
    },
  },
  mounted() {
    // PARAM: "?nodename=suwon-5812&ifname=xe2"
    // SERVICE: "ipsdn/services/stat/badcrc"
    // PARAM: "?nodename=suwon-5812&ifname=xe2"
    // SERVICE: "ipsdn/services/config/interfaces"
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 750
      // this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
      const { ticket_type, root_cause_sysnamea, node_nm, ip_addr, root_cause_porta, alarmloc } = this.selectedRow
      this.item = {
        nodeName: ticket_type === 'SYSLOG' ? node_nm : root_cause_sysnamea,
        ipAddr: ip_addr,
        ifname: ticket_type === 'SYSLOG' ? alarmloc : root_cause_porta,
      }
      this.onLoadCRC()
      this.onLoadInterface() // speed 값 없음
      this.onLoadAgencyIpList()
    },
    async onLoadCRC() {
      const { nodeName, ifname } = this.item
      try {
        const res = await apiIpsdnRequest({ servicePath: 'stat/badcrc', param: `nodename=${nodeName}&ifname=${ifname}` })
        this.badCrc = res?.result?.data ? res.result.data[0]?.ifStat?.badCrc : ''
        console.log(res)
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadInterface() {
      const { nodeName, ifname } = this.item
      try {
        const res = await apiIpsdnRequest({ servicePath: 'config/interfaces', param: `nodename=${nodeName}&ifname=${ifname}` })
        this.if_config = res.result?.data ? res.result?.data[0] : { speed: '' }
        console.log(res)
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadAgencyIpList() {
      const { nodeName } = this.item
      try {
        const res = await apiSelectAgencyIpList({ node_id: nodeName })
        this.agencyIpList = res.result
      } catch (error) {
        this.error(error)
      }
    },
    async onClickRemote() {
      this.isShowFrame = true
      try {
        this.frameLoading = true
        const { nodeName, ipAddr, ifname } = this.item
        if (!ipAddr) {
          await this.confirm('해당 장비의 IP가 존재하지 않습니다.', '알림', { confirmButtonText: '확인' })
        }
        const res = await apiRemote(this.remoteControl, { ip: ipAddr, param: `nodename=${nodeName}&ifname=${ifname}`, user_id: this.$store.state.user.info.uid })
        if (res.success) {
          this.pingFileName = res.result
        } else {
          this.pingFileName = null
          this.isShowFrame = false
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.frameLoading = false
      }
    },
    onClose() {
      this.isShowFrame = false
      this.remoteControl = ''
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
  margin-top: 3vh !important;
}
::v-deep .el-input .el-input__inner {
  border: solid 0px;
  border-bottom: solid 1px lightgray;
  border-radius: 0px;
}
.ip-title {
  color: #fff;
  padding: 0px 10px;
  border-radius: 5px;
  background: #05092e;
}
</style>
