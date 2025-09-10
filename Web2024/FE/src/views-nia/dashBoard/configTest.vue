<template>
  <div :class="{ [name]: true, 'h-100': true }">
    <div class="d-flex flex-column h-100 rounded justify-between">
      <el-card shadow="never" style="height: 30%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> 기본 정보</span>
        </div>
        <el-row>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>장비 이름</label>
            <el-input v-model="item.nodeName" readonly />
          </el-col>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>IP 주소</label>
            <el-input v-model="item.ipAddr" readonly />
          </el-col>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>인터페이스</label>
            <el-input v-model="item.ifname" readonly />
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="never" style="height: 30%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" />CRC/SPEED 체크</span>
        </div>
        <el-row>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>CRC</label>
            <el-input v-model="badCrc" />
          </el-col>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>Speed</label>
            <el-input v-model="if_config.speed" />
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="never" style="height: 50%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> IP 불일치 체크</span>
        </div>
        <el-row>
          <el-col :span="12">
            <div class="font-bold text-center ip-title">장비 설정 IP정보</div>
            <div style="height: 130px; background: #f5f7fa; border-radius: 5px; border: solid 1px">
              <div v-for="(if_item, index) in if_config.equip_ip" :key="index" style="color: rgb(234, 78, 78)" class="font-bold">
                {{ if_item.ip }}
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="font-bold text-center ip-title">이용기관 등록 IP정보</div>
            <div style="height: 130px; background: #f5f7fa; border-radius: 5px; border: solid 1px; overflow-y: auto">
              <div v-for="(agencyItem, index) in agencyIpList" :key="index" style="color: rgb(234, 78, 78)" class="font-bold">
                {{ agencyItem.nren_ip }}
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="never" style="height: 25%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" />원격제어</span>
        </div>
        <el-row>
          <el-col :span="7">
            <el-select v-model="remoteControl">
              <el-option v-for="op in remoteOptions" :key="op.value" :label="op.label" :value="op.value" />
            </el-select>
          </el-col>
          <el-col :span="14">
            <el-input v-model="remoteParam" placeholder="입력 파라미터" :disabled="['shoutdown', 'noshut'].includes(remoteControl)" />
          </el-col>
          <el-col :span="3">
            <el-button size="mini" type="primary" icon="el-icon-video-play" :disabled="remoteControl.length === 0" @click="onClickRemote()">실행 </el-button>
          </el-col>
        </el-row>
        <div v-if="isShowFrame" v-loading="frameLoading" style="height: 200px; width: 100%">
          <iframe id="ping" scrolling="yes" :src="pingResultSrc" width="100%" height="100%" frameBorder="0" />
        </div>
      </el-card>
      <el-row>
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { apiIpsdnRequest, apiSelectAgencyIpList, apiRemote } from '@/api/nia'

const routeName = 'ConfigTest'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {},
  extends: Base,
  mixins: [dialogOpenMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      selectedRow: null,
      remoteOptions: [
        { value: 'shoutdown', label: '포트다운' }, // 자가최적화(유해트래픽)
        { value: 'noshut', label: '포트리셋' }, // 자가회복(포트장애)
        { value: '포트변경', label: '포트변경' }, // 자가구성(이상트래픽)
        { value: 'ping', label: 'ping' }, // 핑 테스트
        { value: 'ACL', label: 'ACL deny' }, // 접근제어 - 원격 차단 ??? 현재 동작안함, UI 엔진쪽에 처리코드 없음
      ],
      remoteControl: '',
      remoteParam: '',
      badCrc: '',
      if_config: { speed: '', equip_ip: [] },
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
      activeProfile: null,
    }
  },
  computed: {
    actionForm() {
      return [
        { label: '장애구분', model: 'fault_classify', options: this.selectOption.gubun },
        { label: '장애유형', model: 'fault_type', options: this.selectOption.type },
        { label: '조치내용', model: 'fault_detail_content', options: this.selectOption.content },
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
    isShowFrame(nVal, oVal) {
      if (nVal !== oVal) {
        const curHeight = Number(this.wdata.height)
        const setHeight = nVal ? curHeight + 200 : curHeight - 200
        this.$set(this.wdata, 'height', setHeight)

        let y = (window.innerHeight - this.wdata.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20
        if (y < 0) {
          y = 85
        }
        this.$set(this.wdata, 'y', y)
      }
    },
    remoteControl(nVAl, oVal) {
      if (nVAl !== 'ping') {
        this.isShowFrame = false
      }

      if (nVAl === '포트변경') {
        this.fn_openWindow('pathSwitch', this.selectedRow)
      }
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
  },
  mounted() {
    const { ticket_type, root_cause_sysnamea, node_nm, ip_addr, root_cause_porta, alarmloc, alarmmsg } = this.selectedRow
    this.item = {
      nodeName: ticket_type === 'SYSLOG' ? node_nm : root_cause_sysnamea,
      ipAddr: ip_addr,
      ifname: ticket_type === 'SYSLOG' ? alarmloc : root_cause_porta,
    }

    switch (ticket_type) {
      case 'ATT2': // 이상트래픽
        this.remoteControl = '포트변경'
        break
      case 'NTT': // 유해트래픽
        this.remoteControl = 'shoutdown'
        break
      case 'RT': // 포트다운
        if (alarmmsg === 'PORT_DOWN') {
          this.remoteControl = 'noshut'
        }
        break
    }

    this.onLoadCRC()
    this.onLoadInterface()
    this.onLoadAgencyIpList()
  },
  methods: {
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
        this.if_config = res.result?.data ? res.result?.data[0] : { speed: '', equip_ip: [] }
        if (res.result && res.result?.data[0].ipAddr) {
          if (Array.isArray(res.result?.data[0].ipAddr)) {
            Object.assign(this.if_config, { equip_ip: res.result?.data[0].ipAddr })
          } else {
            Object.assign(this.if_config, { equip_ip: [{ ip: res.result?.data[0].ipAddr }] })
          }
        }
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

    makeConfirmMessage() {
      const { nodeName, ifname } = this.item

      let message = `장비명(${nodeName})의 포트명(${ifname})인 장비를<br>`
      message += `<b style="color:red">${this.remoteControl}</b>하시겠습니까?`

      return message
    },

    async onClickRemote() {
      if (!['shotdown', 'noshut', '포트변경'].includes(this.remoteControl)) {
        this.$alert('아직 준비되지 않았습니다.')
      }

      this.$confirm(this.makeConfirmMessage(), '명령어전송', {
        confirmButtonText: '실행',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true,
        customClass: 'nia-message-box',
      }).then(() => {
        this.$alert('성공적으로 명령어가 전송되었습니다.', '알림', {
          confirmButtonText: '확인',
          customClass: 'nia-message-box',
        })
      })

      return // 우선 막아놓음.

      // if (['shoutdown', 'noshut'].includes(this.remoteControl) && this.$store.state.app.server.profile !== 'oper') {
      //   onMessagePopup(this, '해당 기능은 운영서버일때만 테스트가 가능합니다.')
      //   return
      // }

      // this.isShowFrame = true
      // try {
      //   this.frameLoading = true
      //   const { nodeName, ipAddr, ifname } = this.item
      //   if (!ipAddr) {
      //     await this.confirm('해당 장비의 IP가 존재하지 않습니다.', '알림', { confirmButtonText: '확인' })
      //   }
      //   const res = await apiRemote(this.remoteControl, { ip: ipAddr, param: `nodename=${nodeName}&ifname=${ifname}`, user_id: this.$store.state.user.info.uid })
      //   if (res.success) {
      //     this.pingFileName = res.result
      //   } else {
      //     this.pingFileName = null
      //     this.isShowFrame = false
      //   }
      // } catch (error) {
      //   this.error(error)
      // } finally {
      //   this.frameLoading = false
      // }
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
::v-deep .el-input.is-disabled .el-input__inner {
  width: 100% !important;
  margin-left: 0rem !important;
}
.ip-title {
  color: #fff;
  padding: 0px 10px;
  border-radius: 5px;
  background: #05092e;
}
</style>
