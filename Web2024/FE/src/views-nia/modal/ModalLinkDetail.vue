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
        {{ '링크 상세보기' }}
        <hr>
      </span>
      <table class="basic">
        <th colspan="2" class="line-class">시작점</th>
        <th colspan="2" class="line-class">끝점</th>
        <tr>
          <th>노드</th>
          <td class="disable">{{ rowInfo.src_node_id }}</td>
          <th>노드</th>
          <td class="disable">{{ rowInfo.dest_node_id }}</td>
        </tr>
        <tr>
          <th>I/F</th>
          <td class="disable">{{ rowInfo.src_if_id }}</td>
          <th>I/F</th>
          <td class="disable">{{ rowInfo.dest_if_id }}</td>
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
            <el-input v-model="rowInfo.bandwidth">
              {{ rowInfo.bandwidth }}
            </el-input>
          </td>
        </tr>
        <tr>
          <th colspan="1">설명</th>
          <td colspan="3" class="disable">
            <el-input v-model="rowInfo.link_desc">
              {{ rowInfo.link_desc }}
            </el-input>
          </td>
        </tr>
        <tr>
          <th colspan="1">VLAN</th>
          <td colspan="3" class="disable">
            <el-select
              v-model="rowInfo.vlan"
              style="display: block; width : 400px"
            >
              <el-option
                v-for="item in vlanOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
        <tr>
          <th colspan="1">TAG</th>
          <td colspan="3" class="disable">
            <el-select
              v-model="rowInfo.tag"
              style="display: block;"
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
        <el-button size="medium" @click.native="close()">
          {{ '저장' }}
        </el-button>
        <el-button size="medium" @click.native="close()">
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
      title: '',
      rowInfo: {},
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
    loginUsername() {
      const userNM = this.username ? this.username.replace(/.$/, '*') : 'UNKONWN'
      return userNM
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
    },
    onClose() {
      /* for Override */
    },
    onSubmit() {
      console.log('submit!')
    },
  },
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
