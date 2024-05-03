<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="nia-edit-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i :class="{'el-icon-edit': viewType === 'OPEN', 'el-icon-document': viewType !== 'OPEN'}" class="mr-2" style="font-size: 17px" />
        {{ title }}
        <hr>
      </span>
      <el-form ref="ipForm" :model="ipForm" :rules="ipRules">
        <table class="basic">
          <th colspan="2" class="line-class">시작점</th>
          <th colspan="2" class="line-class">끝점</th>
          <tr>
            <th>노드</th>
            <td class="disable">
              <el-select v-model="ipForm.src_node_id" :disabled="isDisable">
                <el-option v-for="item in srcNodeList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <th>노드</th>
            <td class="disable">
              <el-select v-model="ipForm.dest_node_id" :disabled="isDisable || isSrcNodeId">
                <el-option v-for="item in destNodeList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>I/F</th>
            <td class="disable">
              <el-select v-model="ipForm.src_if_id" :disabled="isDisable || isSrcNodeId">
                <el-option v-for="item in srcIfList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <th>I/F</th>
            <td class="disable">
              <el-select v-model="ipForm.dest_if_id" :disabled="isDisable || isDestNodeId">
                <el-option v-for="item in destIfList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>IF IP</th>
            <td class="disable">
              <el-form-item prop="src_ip_addr">
                <el-input ref="src_ip_addr" v-model="ipForm.src_ip_addr" :disabled="isDisable" placeholder="0.0.0.0" />
              </el-form-item>
            </td>
            <th>IF IP</th>
            <td class="disable">
              <el-form-item prop="dest_ip_addr">
                <el-input ref="dest_ip_addr" v-model="ipForm.dest_ip_addr" :disabled="isDisable" placeholder="0.0.0.0" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <th colspan="1">대역폭</th>
            <td colspan="3" class="disable">
              <el-input v-model="bandwidth" />
            </td>
          </tr>
          <tr>
            <th colspan="1">설명</th>
            <td colspan="3" class="disable">
              <el-input v-model="link_desc" />
            </td>
          </tr>
          <tr>
            <th>VLAN</th>
            <td>
              <el-select v-model="vlan">
                <el-option v-for="item in vlanOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <th>TAG</th>
            <td>
              <el-select v-model="tag">
                <el-option v-for="item in tagOptions" :key="item.value" :label="item.tag" :value="item.value" />
              </el-select>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-edit" @click.native="onChangeMode()">
          {{ changeText }}
        </el-button>
        <el-button v-if="viewType !== 'OPEN'" type="danger" icon="el-icon-delete" size="mini" @click.native="deleteLinkData()">
          삭제
        </el-button>
        <el-button size="mini" type="info" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import { rulesIpCheck, rulesIp } from '@/utils/validate'
import { apiUpdateLinkList, apiSelectlinkIfList, apiDeleteLinkList, apiInsertLinkList, apiSelectlinkStartNode, apiSelectlinkEndNode } from '@/api/nia'

const routeName = 'ModalLinkDetail'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {},
      bandwidth: '',
      link_desc: '',
      ipForm: {
        src_ip_addr: '',
        dest_ip_addr: '',
        src_node_id: '',
        dest_node_id: '',
        src_if_id: '',
        dest_if_id: ''
      },
      srcNodeList: [],
      destNodeList: [],
      srcIfList: [],
      destIfList: [],
      linkData: [],
      vlanOptions: [
        {
          value: 'True',
          label: 'True',
        },
        {
          value: 'False',
          label: 'False',
        },
      ],
      tagOptions: [
        {
          value: 'True',
          label: 'True',
        },
        {
          value: 'False',
          label: 'False',
        },
      ],
      vlan: '',
      tag: '',
      selectLinkData: []
    }
  },
  computed: {
    ...mapState({
      viewport: (state) => state.app.viewport,
      username: (state) => state.user.name,
    }),
    changeText() {
      return this.viewType === 'OPEN' ? '저장' : '수정'
    },
    title() {
      return this.viewType === 'OPEN' ? '링크 등록' : '링크 상세보기'
    },
    isDisable() {
      return this.viewType === 'linkDetail'
    },
    isSrcNodeId() {
      return this.src_node_id === ''
    },
    isDestNodeId() {
      return this.dest_node_id === ''
    },
    ipRules() {
      return {
        src_ip_addr: rulesIpCheck(),
        dest_ip_addr: rulesIpCheck()
       }
    }
  },
  watch: {
    'ipForm.src_node_id': function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.destNodeList = []
        this.onloadLinkIf(newVal, 'src')
        this.onloadLinkEndNode()
      }
    },
    'ipForm.dest_node_id': function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.onloadLinkIf(newVal, 'dest')
      }
    },
    'ipForm.src_if_id': function(n, o) {
      if (n !== o) {
        this.setBandwidth()
     }
  }
  },
  mounted() {
    this.onloadLinkStartNode()
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 700
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
      this.onloadLinkStartNode()
      if (this.viewType === 'OPEN') {
        this.ipForm.src_node_id = ''
        this.ipForm.dest_node_id = ''
        this.ipForm.src_if_id = ''
        this.ipForm.dest_if_id = ''
        this.ipForm.src_ip_addr = ''
        this.ipForm.dest_ip_addr = ''
        this.bandwidth = ''
        this.link_desc = ''
        this.vlan = ''
        this.tag = ''
      } else {
        this.ipForm.src_node_id = this.rowInfo.src_node_id
        this.ipForm.dest_node_id = this.rowInfo.dest_node_id
        this.ipForm.src_if_id = this.rowInfo.src_if_id
        this.ipForm.dest_if_id = this.rowInfo.dest_if_id
        this.ipForm.src_ip_addr = this.rowInfo.src_ip_addr
        this.ipForm.dest_ip_addr = this.rowInfo.dest_ip_addr
        this.bandwidth = this.rowInfo.bandwidth
        this.link_desc = this.rowInfo.link_desc
        this.vlan = this.rowInfo.VLAN
        this.tag = this.rowInfo.TAG
      }
    },
    async onloadLinkStartNode() {
      try {
        const res = await apiSelectlinkStartNode()
        const selectCodeData = res.result.map((item) => ({ label: item.node_id, value: item.node_id }))
        this.srcNodeList = selectCodeData
      } catch (error) {
        console.error(error)
      }
    },
    async onloadLinkEndNode() {
      const nullCheck = this.ipForm.src_node_id?.trim() !== ''
      if (nullCheck) {
        try {
          const param = {
            node_id: this.ipForm.src_node_id,
          }
          const res = await apiSelectlinkEndNode(param)
          const selectCodeData = res.result.map((item) => ({ label: item.node_id, value: item.node_id }))
          this.destNodeList = selectCodeData
        } catch (error) {
          console.error(error)
        }
      }
    },
    async onloadLinkIf(newVal, type) {
      try {
        const param = {
          node_id: newVal,
        }
        const res = await apiSelectlinkIfList(param)
        this.selectLinkData = res.result.map((item) => ({ label: item.if_id, value: item.if_id, if_speed: item.if_speed }))
        if (type === 'src') {
          this.srcIfList = this.selectLinkData
        } else {
          this.destIfList = this.selectLinkData
        }
      } catch (error) {
        console.error(error)
      }
    },
     setBandwidth() {
    const selectedSrcIf = this.selectLinkData.find(item => item.value === this.ipForm.src_if_id)
    if (selectedSrcIf) {
      this.bandwidth = selectedSrcIf.if_speed
      }
    },
    checkFormData() {
      if (
        this.ipForm.src_node_id === '' ||
        this.ipForm.dest_node_id === '' ||
        this.ipForm.src_if_id === '' ||
        this.ipForm.dest_if_id === '' ||
        this.ipForm.src_ip_addr === '' ||
        this.ipForm.dest_ip_addr === '' ||
        this.bandwidth === '' ||
        this.link_desc === '' ||
        this.vlan === '' ||
        this.tag === ''
      ) {
        return false
      }
      return true
    },
    insertLinkData() {
      if (!this.checkFormData()) {
       this.$message('입력조건을 확인하세요')
       return
      }
      this.confirm('등록하시겠습니까?', '등록', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        this.$refs.ipForm.validate(async valid => {
        if (!valid) {
          this.$message('IP 주소 형식을 확인하세요')
          return false
        }
        const param = {
          src_node_id: this.ipForm.src_node_id,
          src_if_id: this.ipForm.src_if_id,
          dest_node_id: this.ipForm.dest_node_id,
          dest_if_id: this.ipForm.dest_if_id,
          src_ip_addr: this.ipForm.src_ip_addr,
          dest_ip_addr: this.ipForm.dest_ip_addr,
          bandwidth: this.bandwidth,
          link_desc: this.link_desc,
          vlan: this.vlan,
          tag: this.tag,
        }
        try {
          const res = await apiInsertLinkList(param)
          if (res.success) {
            this.$message('등록 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `등록에 실패했습니다.` })
          console.error(error)
        }
        })
      })
    },
    updateLinkData() {
      this.confirm('수정하시겠습니까?', '수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        const param = {
          src_node_id: this.ipForm.src_node_id,
          src_if_id: this.ipForm.src_if_id,
          dest_node_id: this.ipForm.dest_node_id,
          dest_if_id: this.ipForm.dest_if_id,
          bandwidth: this.bandwidth,
          link_desc: this.link_desc,
          vlan: this.rowInfo.VLAN,
          tag: this.rowInfo.TAG,
        }
        try {
          const res = await apiUpdateLinkList(param)
          if (res.success) {
            this.$message('수정 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    deleteLinkData() {
      this.confirm('삭제하시겠습니까?', '삭제', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }).then(async () => {
        const param = {
          src_node_id: this.ipForm.src_node_id,
          src_if_id: this.ipForm.src_if_id,
          dest_node_id: this.ipForm.dest_node_id,
          dest_if_id: this.ipForm.dest_if_id,
        }
        try {
          const res = await apiDeleteLinkList(param)
          if (res.success) {
            this.$message('삭제 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `삭제에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    onChangeMode() {
      if (this.viewType === 'OPEN') {
        this.insertLinkData()
      } else {
        this.updateLinkData()
      }
    },
    onClose() {
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalLinkDetail {
  .line-class {
    font-weight: bold;
    font-size: 15px;
  }
}
</style>
