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
        <i class="el-icon-user mr-2" style="font-size: 17px" />
        {{ title }}
        <hr>
      </span>
      <table class="basic">
        <th colspan="2" class="line-class">시작점</th>
        <th colspan="2" class="line-class">끝점</th>
        <tr>
          <th>노드</th>
          <td class="disable">
            <el-select
              v-model="src_node_id"
              :disabled="isDisable"
            >
              <el-option
                v-for="item in srcNodeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>노드</th>
          <td class="disable">
            <el-select
              v-model="dest_node_id"
              :disabled="isDisable"
            >
              <el-option
                v-for="item in destNodeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
        <tr>
          <th>I/F</th>
          <td class="disable">
            <el-select
              v-model="src_if_id"
              :disabled="isDisable"
            >
              <el-option
                v-for="item in srcIfList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>I/F</th>
          <td class="disable">
            <el-select
              v-model="dest_if_id"
              :disabled="isDisable"
            >
              <el-option
                v-for="item in destIfList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
        <tr>
          <th>IF IP</th>
          <td class="disable">
            <el-input
              v-model="src_ip_addr"
              :disabled="isDisable"
              placeholder="0.0.0.0"
            />
          </td>
          <th>IF IP</th>
          <td class="disable">
            <el-input
              v-model="dest_ip_addr"
              :disabled="isDisable"
              placeholder="0.0.0.0"
            />
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
            <el-select
              v-model="vlan"
            >
              <el-option
                v-for="item in vlanOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>TAG</th>
          <td>
            <el-select
              v-model="tag"
            >
              <el-option
                v-for="item in tagOptions"
                :key="item.value"
                :label="item.tag"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click.native="onChangeMode()">
          {{ changeText }}
        </el-button>
        <el-button v-if="viewType !== 'OPEN'" size="medium" @click.native="deleteLinkData()">
          {{ '삭제' }}
        </el-button>
        <el-button class="exit-btn" size="medium" @click.native="close()">
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
      src_node_id: '',
      dest_node_id: '',
      src_if_id: '',
      dest_if_id: '',
      bandwidth: '',
      link_desc: '',
      src_ip_addr: '',
      dest_ip_addr: '',
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
      tag: ''

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
  }
  },
  watch: {
    'src_node_id': function(newVal, oldVal) {
    if (newVal !== oldVal) {
      this.destNodeList = []
        this.onloadLinkIf(newVal, 'src')
        this.onloadLinkEndNode()
    }
  },
  'dest_node_id': function(newVal, oldVal) {
    if (newVal !== oldVal) {
        this.onloadLinkIf(newVal, 'dest')
        // this.onloadLinkEndNode()
    }
  }

  },
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.onloadLinkStartNode()
      if (this.viewType === 'OPEN') {
        this.rowInfo = ''
        this.src_node_id = ''
        this.dest_node_id = ''
        this.src_if_id = ''
        this.dest_if_id = ''
        this.src_ip_addr = ''
        this.dest_ip_addr = ''
        this.bandwidth = ''
        this.link_desc = ''
        this.vlan = ''
        this.tag = ''
      } else {
        this.rowInfo = this._cloneDeep(model.row)
        this.src_node_id = this.rowInfo.src_node_id
        this.dest_node_id = this.rowInfo.dest_node_id
        this.src_if_id = this.rowInfo.src_if_id
        this.dest_if_id = this.rowInfo.dest_if_id
        this.src_ip_addr = this.rowInfo.src_ip_addr
        this.dest_ip_addr = this.rowInfo.dest_ip_addr
        this.bandwidth = this.rowInfo.bandwidth
        this.link_desc = this.rowInfo.link_desc
        this.vlan = this.rowInfo.VLAN
        this.tag = this.rowInfo.TAG
      }
    },
    async onloadLinkStartNode() {
      try {
        const res = await apiSelectlinkStartNode()
        const selectCodeData = res.result.map(item => ({ label: item.node_id, value: item.node_id }))
        this.srcNodeList = selectCodeData
      } catch (error) {
          console.error(error)
        } finally {
          // this.closeLoading(target)
        }
    },
    async onloadLinkEndNode() {
      const nullCheck = this.src_node_id.trim() !== ''
      if (nullCheck) {
      try {
        const param = {
          node_id: this.src_node_id
        }
        const res = await apiSelectlinkEndNode(param)
        const selectCodeData = res.result.map(item => ({ label: item.node_id, value: item.node_id }))
        this.destNodeList = selectCodeData
      } catch (error) {
        console.error(error)
      } finally {
        // this.closeLoading(target)
      }
    }
    },
    async onloadLinkIf(newVal, type) {
      try {
        const param = {
          node_id: newVal
        }
        const res = await apiSelectlinkIfList(param)
        const selectCodeData = res.result.map(item => ({ label: item.if_id, value: item.if_id }))
        if (type === 'src') {
          this.srcIfList = selectCodeData
        } else {
          this.destIfList = selectCodeData
        }
      } catch (error) {
        console.error(error)
      } finally {
        // this.closeLoading(target)
      }
    },
    insertLinkData() {
       this.confirm('등록하시겠습니까?', '등록', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            src_node_id: this.src_node_id,
            src_if_id: this.src_if_id,
            dest_node_id: this.dest_node_id,
            dest_if_id: this.dest_if_id,
            src_ip_addr: this.src_ip_addr,
            dest_ip_addr: this.dest_ip_addr,
            bandwidth: this.bandwidth,
            link_desc: this.link_desc,
            vlan: this.vlan,
            tag: this.tag
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
    },
    updateLinkData() {
       this.confirm('수정하시겠습니까?', '수정', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            src_node_id: this.rowInfo.src_node_id,
            src_if_id: this.rowInfo.src_if_id,
            dest_node_id: this.rowInfo.dest_node_id,
            dest_if_id: this.rowInfo.dest_if_id,
            bandwidth: this.bandwidth,
            link_desc: this.link_desc,
            vlan: this.rowInfo.VLAN,
            tag: this.rowInfo.TAG
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
          type: 'success'
        }).then(async () => {
          const param = {
            src_node_id: this.rowInfo.src_node_id,
            src_if_id: this.rowInfo.src_if_id,
            dest_node_id: this.rowInfo.dest_node_id,
            dest_if_id: this.rowInfo.dest_if_id,
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
    },
    onClose() {
    }
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
