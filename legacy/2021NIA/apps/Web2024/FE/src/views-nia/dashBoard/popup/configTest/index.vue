<template>
  <div :class="{ [name]: true, 'h-100': true }">
    <div class="d-flex flex-column h-100 rounded justify-between">
      <el-card shadow="never" style="height: 30%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> 기본 정보</span>
        </div>
        <el-row>
          <el-col v-loading="loading.ipsdnNodeLoading" :span="8" class="p-1 d-flex flex-column items-start">
            <label>장비 이름</label>
            <el-select v-model="item.nodeName" :disabled="selectedRow.ticket_type !== 'NTT_AI'" @change="changeDefaultInfoSelecter('NODENAME', $event)">
              <el-option v-for="(option, i) in options.node" :key="i" :label="option.label" :value="option.value" />
            </el-select>
          </el-col>
          <el-col v-loading="loading.ipsdnNodeLoading" :span="8" class="p-1 d-flex flex-column items-start">
            <label>IP 주소</label>
            <el-input v-model="item.ipAddr" disabled />
          </el-col>
          <el-col v-loading="loading.ipsdnInterfaceLoading" :span="8" class="p-1 d-flex flex-column items-start">
            <label>인터페이스</label>
            <el-select v-model="item.ifname" :disabled="selectedRow.ticket_type !== 'NTT_AI'" @change="changeDefaultInfoSelecter('IFNAME', $event)">
              <el-option v-for="(option, i) in options.interface" :key="i" :label="option.label" :value="option.value" />
            </el-select>
          </el-col>
        </el-row>
      </el-card>
      <el-card v-loading="loading.crcLoading" shadow="never" style="height: 30%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <el-tooltip class="item" effect="dark" content="[CRC]: 오류발생여부 0이면 정상 [Speed]: 1g이면 속도가 1Gbit(기가비트)" placement="top">
            <span><i class="el-icon-document" /> CRC/SPEED 체크</span>
          </el-tooltip>
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
          <el-col v-loading="loading.equipIpAndSpeedLoading" :span="12">
            <div class="font-bold text-center ip-title">장비 설정 IP정보</div>
            <div style="height: 130px; background: #f5f7fa; border-radius: 5px; border: solid 1px">
              <div v-for="(if_item, index) in if_config.equip_ip" :key="index" style="color: rgb(234, 78, 78)" class="font-bold">
                {{ if_item.ip }}
              </div>
            </div>
          </el-col>
          <el-col v-loading="loading.agencyIpLoading" :span="12">
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
            <el-select v-model="remoteControl" @change="changeRemoteControlSelector">
              <el-option v-for="op in remoteOptions" :key="op.value" :label="op.label" :value="op.value" />
            </el-select>
          </el-col>
          <el-col :span="remoteControl === 'chngport' ? 14 : 17">
            <el-input v-model="remoteParam" placeholder="입력 파라미터" :disabled="['shoutdown', 'noshut', 'chngport'].includes(remoteControl)" />
          </el-col>
          <el-col v-if="remoteControl === 'chngport'" :span="3">
            <el-button size="mini" type="primary" icon="el-icon-video-play" @click="openPathSwitchPopup">경로설정 </el-button>
          </el-col>
        </el-row>
      </el-card>
      <el-row>
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="primary" icon="el-icon-video-play" :disabled="remoteButtonDisable" @click="onClickRemote"> 실행 </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
    <pathSwitch ref="pathSwitch" :wdata="{ selectedRow: selectedRow, item: item }" @saveLocalStorage="saveLocalStorage" @actionPathSwitch="setpathSwitchRowData" />
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { apiIpsdnRequest, apiSelectAgencyIpList, apiRemote, apiSelectIpsdnNodeList, apiSelectIpsdnInterfaceList } from '@/api/nia'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, getInvisibleSpanParameter, getNiaRouterPathByName, makeOpenPopupNumberText, showAlertBox } from '@/views-nia/js/commonNiaFunction'
import { mapState } from 'vuex'
import niaObserverMixin from '@/mixin/niaObserverMixin'

const routeName = constants.nia.chatbotKeyMap.configTest.parameterKey

const defaultIfConfig = { speed: '', equip_ip: [] }

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {
    pathSwitch: () => import('@/views-nia/dashBoard/popup/configTest/popup/pathSwitch'),
  },
  extends: Base,
  mixins: [dialogOpenMixin, niaObserverMixin],
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
        { value: 'chngport', label: '포트변경' }, // 자가구성(이상트래픽)
        { value: 'ping', label: 'ping' }, // 핑 테스트
        { value: 'ACL', label: 'ACL deny' }, // 특정 트래픽 차단
      ],
      remoteControl: '',
      remoteParam: '',
      badCrc: '',
      if_config: _.cloneDeep(defaultIfConfig),
      item: {
        nodeName: '',
        ipAddr: '',
        ifname: '',
      },
      options: {
        node: [],
        interface: [],
      },
      pathSwitchRowData: {},
      nodeNameIpAddrMappingBox: [],
      agencyIpList: [],
      frameLoading: false,
      activeProfile: null,
      loading: {
        ipsdnNodeLoading: false,
        ipsdnInterfaceLoading: false,
        crcLoading: false,
        equipIpAndSpeedLoading: false,
        agencyIpLoading: false,
      },
    }
  },
  computed: {
    ...mapState({
      configTestEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.configTest.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },

    remoteButtonDisable() {
      return this.remoteControl.length === 0 || (this.remoteControl === 'chngport' && this.remoteParam.length === 0)
    }
  },
  watch: {
    configTestEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.remote.action:
            this.onClickRemote()
            break
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.configTest.parameterKey })
      }
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    changeRemoteControlSelector(val) {
      this.remoteParam = ''
    },

    setpathSwitchRowData(selectData) {
      this.pathSwitchRowData = selectData

      this.remoteParam = '노드 ID : ' + this.pathSwitchRowData.node_id + ' 포트 ID : ' + this.pathSwitchRowData.if_id
    },

    openPathSwitchPopup() {
      this.$refs.pathSwitch.setVisible()
    },

    async setIpsdnNodeList() {
      try {
        this.loading.ipsdnNodeLoading = true
        this.nodeNameIpAddrMappingBox = []
        this.options.node = []
        const res = await apiSelectIpsdnNodeList()
        res.result.forEach((v) => {
          this.nodeNameIpAddrMappingBox.push({ nodeName: v.node_name, addr: v.mgmt_addr })
          this.options.node.push({ label: v.node_name, value: v.node_name })
        })
      } catch (e) {
        console.error(e)
      } finally {
        this.loading.ipsdnNodeLoading = false
      }
    },

    async setIpsdnInterfaceList(param) {
      try {
        this.loading.ipsdnInterfaceLoading = true
        this.options.interface = []
        this.item.ifname = ''
        const res = await apiSelectIpsdnInterfaceList(param)
        res.result.forEach((v) => {
          this.options.interface.push({ label: v.if_name, value: v.if_name })
        })
      } catch (e) {
        console.error(e)
      } finally {
        this.loading.ipsdnInterfaceLoading = false
      }
    },

    changeDefaultInfoSelecter(selecterType, node_name) {
      switch (selecterType) {
        case 'NODENAME':
          {
            const x = this.nodeNameIpAddrMappingBox.find((v) => {
              return v.nodeName === node_name
            })
            this.item.ipAddr = x.addr
            this.setIpsdnInterfaceList({ nodeName: node_name })
            this.onLoadCRC()
          }
          break
        case 'IFNAME':
          this.onLoadEquipIpAndSpeed()
          this.onLoadAgencyIpList()
          break
      }
    },

    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      if (isSwitchingTicket) this.wdata.params.isChatbotGenerated = isSwitchingTicket
      const chatbotData = await getChatbotTicketData(this.wdata)
      if (chatbotData) {
        this.selectedRow = chatbotData
        this.$emit('update:wdataParams', chatbotData)
      }

      const { ticket_type, root_cause_sysnamea, node_nm, ip_addr, root_cause_porta, alarmloc, alarmmsg } = this.selectedRow

      switch (ticket_type) {
        case 'ATT2':
        case 'ATT2_AI':
          this.remoteControl = 'chngport' // 포트변경
          break
        case 'NTT':
        case 'NTT_AI': // 유해트래픽
          this.remoteControl = 'shoutdown' // 포트다운
          break
        case 'RT': // 장애
          if (alarmmsg === 'PORT_DOWN') {
            this.remoteControl = 'noshut' // 포트리셋
          }
          break
        case 'SYSLOG':
          this.remoteControl = 'noshut' // 포트리셋
          break
      }

      if (['NTT', 'NTT_AI'].includes(this.selectedRow.ticket_type)) {
        await this.setIpsdnNodeList()
        await this.setIpsdnInterfaceList()
      } else {
        this.item = {
          nodeName: ['SYSLOG', 'RT'].includes(ticket_type) ? node_nm : root_cause_sysnamea,
          ipAddr: ip_addr,
          ifname: ['SYSLOG', 'RT'].includes(ticket_type) ? alarmloc : root_cause_porta,
        }

        this.onLoadCRC()
        this.onLoadEquipIpAndSpeed()
        this.onLoadAgencyIpList()
      }
    },

    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
            '<div class="chatbot-command-header">조치 팝업 안내</div>' +
            '<div class="chatbot-message-body">' +
              '장애가 발생한 장비에 대하여 <b>원격으로 제어</b>할 수 있는 팝업입니다.' +
            '<br><br>' +
              (this.selectedRow.ticket_type !== 'NTT_AI' ? constants.nia.chatbotIcon.Information + '장비에 대한 정보를 자동으로 설정했습니다. 정보를 확인하신 후에 <b>원격제어</b>를 진행해 주시면 됩니다.'
                : constants.nia.chatbotIcon.Information + '유해트래픽의 경우 장비이름과 인터페이스를 직접 선택해주셔야 합니다.') +
              '<div class="chatbot-process">' +
                constants.nia.chatbotContent.processHeaderText + '<br><br>' +
                '1. <b>설정 정보</b> 확인 → 2. <b>설정 항목</b> 조정 → 3. <b>원격제어</b>' +
              '</div>' +
            '</div>' +
            (await getWindowActionList(constants.nia.chatbotKeyMap.configTest.dialogNm, constants.nia.chatbotKeyMap.configTest.popupName,
              makeOpenPopupNumberText(2, constants.nia.chatbotKeyMap.processFin.key) +
              makeOpenPopupNumberText(3, constants.nia.chatbotKeyMap.sopHistory.key) +
              makeOpenPopupNumberText(4, constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.key)
            )),
        })
      }
    },
    async onLoadCRC() {
      const { nodeName, ifname } = this.item
      try {
        this.loading.crcLoading = true
        const res = await apiIpsdnRequest({ servicePath: 'stat/badcrc', param: `nodename=${nodeName}&ifname=${ifname}` })
        this.badCrc = res?.result?.data ? res.result.data[0]?.ifStat?.badCrc : ''
      } catch (error) {
        this.error(error)
      } finally {
        this.loading.crcLoading = false
      }
    },
    async onLoadEquipIpAndSpeed() {
      const { nodeName, ifname } = this.item
      try {
        this.loading.equipIpAndSpeedLoading = true
        this.if_config = _.cloneDeep(defaultIfConfig)

        const res = await apiIpsdnRequest({ servicePath: 'config/interfaces', param: `nodename=${nodeName}&ifname=${ifname}` })
        if (res && res.result && res.result.data && res.result.data.length > 0) {
          this.if_config = res.result?.data[0]
          if (res.result.data[0].ipAddr) {
            if (Array.isArray(res.result?.data[0].ipAddr)) {
              Object.assign(this.if_config, { equip_ip: res.result?.data[0].ipAddr })
            } else {
              Object.assign(this.if_config, { equip_ip: [{ ip: res.result?.data[0].ipAddr }] })
            }
          }
        }

        if (this.if_config.speed && this.if_config.speed.length > 0) {
          this.if_config.speed = this.if_config.speed.toUpperCase()
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.loading.equipIpAndSpeedLoading = false
      }
    },
    async onLoadAgencyIpList() {
      const { nodeName } = this.item
      try {
        this.loading.agencyIpLoading = true
        const res = await apiSelectAgencyIpList({ node_id: nodeName })
        this.agencyIpList = res.result
      } catch (error) {
        this.error(error)
      } finally {
        this.loading.agencyIpLoading = false
      }
    },

    configNextAction() {
      this.$store.dispatch('chatbot/botPushAnswerMessage', {
        content:
          `<b>${constants.nia.chatbotCommand.configTest.label} 실행 후 추천명령어 입니다</b><br><br>` +
          makeOpenPopupNumberText(1, constants.nia.chatbotKeyMap.processFin.key) +
          makeOpenPopupNumberText(2, constants.nia.chatbotKeyMap.sopHistory.key)
      })
    },

    makeConfirmMessage() {
      const { nodeName, ifname } = this.item
      const currentRemoteOption = this.remoteOptions.find((map) => map.value === this.remoteControl)

      let message = `장비(${nodeName})의 포트(${ifname})인 장비를<br>`
      message += `<b style="color:red">${currentRemoteOption.label}</b>하시겠습니까?`

      return message
    },

    async onClickRemote() {
      if (this.remoteButtonDisable) {
        this.$alert('실행버튼이 활성화되어야합니다.', '경고', {
          customClass: 'nia-message-box',
        })

        return
      }

      this.$confirm(this.makeConfirmMessage(), '명령어전송', {
        confirmButtonText: '실행',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true,
        customClass: 'nia-message-box',
      }).then(async () => {
        switch (this.remoteControl) {
          case 'shoutdown': // 포트다운
          case 'noshut': // 포트리셋
            showAlertBox('실행이 완료되었습니다')
            // this.actionRemote(this.remoteControl)
            // this.remotePingTest()
            break
          case 'ping':
            this.remotePingTest()
            break
          case 'ACL':
            showAlertBox('실행이 완료되었습니다')
            // this.remotePingTest()
            break
          case 'chngport':
            showAlertBox('실행이 완료되었습니다')
            // this.actionPortSwitch()
            // this.remotePingTest()
            break
        }

        const param = {
          uid: this.$store.state.user.info.uid,
          remoteControl: this.remoteControl,
          nodeName: this.item.nodeName,
          ifname: this.item.ifname,
        }

        this.saveLocalStorage(param)
      })
    },

    async actionPortSwitch() {
      const { nodeName, ipAddr, ifname } = this.item
      if (!ipAddr) {
        this.$alert('해당 장비의 IP가 존재하지 않습니다.')
        return
      }

      const res = await apiRemote(this.remoteControl, {
        ip: ipAddr,
        param: `nodename=${nodeName}&ifname=${ifname}`,
        user_id: this.$store.state.user.info.uid,
      })

      if (res.success) {
        this.$alert('성공적으로 명령이 실행되었습니다.', '성공', {
          confirmButtonText: '확인',
        })

        const param = {
          uid: this.$store.state.user.info.uid,
          remoteControl: this.remoteControl,
          nodeName: this.item.nodeName,
          ifname: this.item.ifname,
        }

        this.$emit('saveLocalStorage', param)
      } else {
        this.$alert('명령 실행이 실패했습니다.', '실패', {
          confirmButtonText: '확인',
        })
      }
    },

    saveLocalStorage(param) {
      if (param.uid && ['shoutdown', 'noshut', 'chngport'].includes(param.remoteControl)) {
        localStorage.setItem('lastRemoteHistory' + '_' + param.uid + '_' + param.nodeName + '_' + param.ifname, JSON.stringify(param.remoteControl))
      }
    },

    async actionRemote(remoteControl) {
      const { nodeName, ipAddr, ifname } = this.item
      if (!ipAddr) {
        showAlertBox('해당 장비의 IP가 존재하지 않습니다.')
        return
      }

      const userId = this.$store.state.user.info.uid
      const res = await apiRemote(remoteControl, {
        ip: ipAddr,
        param: `nodename=${nodeName}&ifname=${ifname}`,
        user_id: userId,
      })

      if (res.success) {
        showAlertBox('성공적으로 명령이 실행되었습니다.', '성공')
      } else {
        showAlertBox('명령 실행이 실패했습니다.', '실패')
      }
    },

    async remotePingTest() {
      try {
        this.frameLoading = true
        const { nodeName, ipAddr, ifname } = this.item
        if (!ipAddr) {
          this.$alert('해당 장비의 IP가 존재하지 않습니다.')
          return
        }
        const res = await apiRemote('ping', {
          ip: ipAddr,
          param: `nodename=${nodeName}&ifname=${ifname}`,
          user_id: this.$store.state.user.info.uid,
        })
        if (res.success) {
          showAlertBox('성공적으로 ping 테스트가 진행되었습니다.', '성공')
        } else {
          showAlertBox('Ping 테스트가 실패했습니다.', '실패')
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.frameLoading = false
      }
    },

    onClose() {
      this.remoteControl = ''
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-input .el-input__inner {
  border: solid 0px;
  border-bottom: solid 1px lightgray;
  border-radius: 0px;

  &:disabled {
    color: black;
    font-weight: bold;
  }
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
