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
        <hr />
      </span>
      <table class="basic">
        <tr>
          <th>RULE ID</th>
          <td class="disable">
            <el-input v-model="syslog_rule_id" />
          </td>
          <th>RULE NAME</th>
          <td class="disable">
            <el-input
              v-model="syslog_rule_nm"
              style="width: 60%;"
            />
            <el-button size="small" style="float: right" plain round type="info">중복확인 </el-button>
          </td>
        </tr>
        <tr>
          <th colspan="2" class="line-class">발생</th>
          <th colspan="2" class="line-class">제외</th>
        </tr>
        <tr>
          <th>keyword1</th>
          <td class="disable">
            <el-input v-model="occur_str1" />
          </td>
          <th>keyword1</th>
          <td class="disable">
            <el-input v-model="occur_except_str1" />
          </td>
        </tr>
        <tr>
          <th>keyword2</th>
          <td class="disable">
            <el-input v-model="occur_str2" />
          </td>
          <th>keyword2</th>
          <td class="disable">
            <el-input v-model="occur_except_str2" />
          </td>
        </tr>
        <tr>
          <th>keyword3</th>
          <td class="disable">
            <el-input v-model="occur_str3" />
          </td>
          <th>keyword3</th>
          <td class="disable">
            <el-input v-model="occur_except_str3" />
          </td>
        </tr>
        <tr>
          <th colspan="1">사용여부</th>
          <td colspan="3" class="disable">
            <el-select v-model="use_yn">
              <el-option
                v-for="item in useMode"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="info" @click.native="onChangeMode()">
          {{ changeText }}
        </el-button>
        <el-button v-if="viewType !== 'OPEN'" plain type="danger" size="mini" @click.native="deleteLinkData()">
          {{ '삭제' }}
        </el-button>
        <el-button class="exit-btn" size="mini" @click.native="close()">
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
import { apiUpdateLinkList, apiDeleteLinkList, apiInsertLinkList, apiSelectlinkStartNode, } from '@/api/nia'

const routeName = 'ModalSyslogRules'

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
      syslog_rule_id: '',
      syslog_rule_nm: '',
      occur_str1: '',
      occur_except_str1: '',
      occur_str2: '',
      occur_except_str2: '',
      occur_str3: '',
      occur_except_str3: '',
      use_yn: '',
      useMode: [
        { value: '사용', label: '사용' },
        { value: '미사용', label: '미사용' },
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
      return this.viewType === 'OPEN' ? 'SYSLOG RULE 등록' : 'SYSLOG RULE 수정'
    },
    isDisable() {
      return this.viewType === 'linkDetail'
    },
  },
  watch: {},
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
      if (this.viewType === 'OPEN') {
      this.syslog_rule_id = ''
      this.syslog_rule_nm = ''
      this.occur_str1 = ''
      this.occur_except_str1 = ''
      this.occur_str2 = ''
      this.occur_except_str2 = ''
      this.occur_str3 = ''
      this.occur_except_str3 = ''
      this.use_yn = ''
      } else {
      this.syslog_rule_id = this.rowInfo.syslog_rule_id
      this.syslog_rule_nm = this.rowInfo.syslog_rule_nm
      this.occur_str1 = this.rowInfo.occur_str1
      this.occur_except_str1 = this.rowInfo.occur_except_str1
      this.occur_str2 = this.rowInfo.occur_str2
      this.occur_except_str2 = this.rowInfo.occur_except_str2
      this.occur_str3 = this.rowInfo.occur_str3
      this.occur_except_str3 = this.rowInfo.occur_except_str3
      this.use_yn = this.rowInfo.use_yn
      }
    },
    async onloadLinkStartNode() {
      try {
        const res = await apiSelectlinkStartNode()
        const selectCodeData = res.result.map((item) => ({ label: item.node_id, value: item.node_id }))
        this.srcNodeList = selectCodeData
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
        type: 'success',
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
    },
    updateLinkData() {
      this.confirm('수정하시겠습니까?', '수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        const param = {
          src_node_id: this.rowInfo.src_node_id,
          src_if_id: this.rowInfo.src_if_id,
          dest_node_id: this.rowInfo.dest_node_id,
          dest_if_id: this.rowInfo.dest_if_id,
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
        type: 'success',
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
  onClose() {},
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalSyslogRules {
  .line-class {
    font-weight: bold;
    font-size: 15px;
  }

   .el-dialog {
      min-width: 600px !important;
   }
}
</style>
