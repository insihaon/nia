<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="미배정 블록 상세"
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
        <tr>
          <th>
            <label>계위</label>
          </th>
          <td>
            <div v-if="tbData !== null">{{ tbData.ssvcLineTypeNm }} - {{ tbData.ssvcGroupNm }}</div>
          </td>
        </tr>
      </table>
    </div>
    <el-col :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="tableDatas"
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            미배정 블록 상세 정보
          </span>
        </template>
      </compTable>
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'

const routeName = 'ModalNotAssignDetail'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
       tableColumns: [
        { prop: 'sipCreateSeqCd', label: '생성차수', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nclassCnt', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: 'primary'
              },
              props: {
                size: 'mini'
              },
              on: { click: () => {
                this.close()
                this.$store.dispatch('ipms/setToParam', { value: row.pipPrefix })
                this.$router.push('/ipAssignMng/ipAssign')
              }
            } }, '배정')
          }
        },
      ],
      tableDatas: [],
      tbData: null
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.tableDatas = model.row
      this.tbData = this.tableDatas[0]
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
