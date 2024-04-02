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
            <el-input v-model="rowInfo.src_if_id" />
          </td>
          <th>I/F</th>
          <td class="disable">
            <el-input v-model="rowInfo.dest_if_id" />
          </td>
        </tr>
        <tr>
          <th>IF IP</th>
          <td class="disable">{{ rowInfo.src_ip_addr }}</td>
          <th>IF IP</th>
          <td class="disable">{{ rowInfo.dest_ip_addr }}</td>
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
              v-model="rowInfo.vlan"
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
              v-model="rowInfo.tag"
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
import { apiUpdateLinkList, apiDeleteLinkList, apiSelectlinkStartNode, apiSelectlinkEndNode } from '@/api/nia'

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
      bandwidth: '',
      link_desc: '',
      srcNodeList: [],
      destNodeList: [],
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
  }
  },
  watch: {
    // 'srcNodeList': function(newVal, oldVal) {
    //   if (newVal !== oldVal) {
    //     this.onloadLinkEndNode(newVal)
    //   }
    // }
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
      this.onloadLinkEndNode()
      if (this.viewType === 'OPEN') {
        this.rowInfo = ''
        this.src_node_id = ''
        this.dest_node_id = ''
        this.bandwidth = ''
        this.link_desc = ''
      } else {
        this.rowInfo = this._cloneDeep(model.row)
        this.src_node_id = this.rowInfo.src_node_id
        this.dest_node_id = this.rowInfo.dest_node_id
        this.bandwidth = this.rowInfo.bandwidth
        this.link_desc = this.rowInfo.link_desc
      }
    },
    async onloadLinkStartNode() {
      try {
        const res = await apiSelectlinkStartNode()
        this.selectCodeData = res.result.map(item => ({ label: item.node_id, value: item.node_id }))
        this.srcNodeList = this.selectCodeData
      } catch (error) {
          console.error(error)
        } finally {
          // this.closeLoading(target)
        }
    },
    async onloadLinkEndNode() {
      try {
        const param = {
          node_id: this.src_node_id
        }
        const res = await apiSelectlinkEndNode(param)
        this.selectCodeData = res.result.map(item => ({ label: item.node_id, value: item.node_id }))
        this.destNodeList = this.selectCodeData
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
            src_node_id: this.rowInfo.src_node_id,
            src_if_id: this.rowInfo.src_if_id,
            dest_node_id: this.rowInfo.dest_node_id,
            dest_if_id: this.rowInfo.dest_if_id,
            link_desc: this.link_desc,
            vlan: this.vlan,
            tag: this.tag
          }
          try {
              const res = await ''
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
            link_desc: this.link_desc,
            vlan: this.vlan,
            tag: this.tag
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
              const res = await apiUpdateLinkList(param)
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
      /* for Override */
    }
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalNodeDetail {
  .line-class {
    font-weight: bold;
    font-size: 15px;
  }
}
</style>
