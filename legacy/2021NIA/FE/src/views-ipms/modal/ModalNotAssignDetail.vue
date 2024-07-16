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
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        미배정 블록 상세
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <div class="optionBox">
          <el-row class="optionRow">
            <el-col class="d-flex">
              <label>
                계위
              </label>
              <div v-if="selectedRow !== null">
                {{ selectedRow.mang }} - {{ selectedRow.org }}
              </div>
            </el-col>
          </el-row>
        </div>
        <el-col :span="24">
          <compTable
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
      </el-row>
      <div slot="footer" class="dialog-footer">
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
import CompTable from '@/components/elTable/CompTable.vue'
import { onMessagePopup } from '@/utils/index'

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
        // { prop: '', label: '선택', width: 50, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'a', label: '생성차수', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'b', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'c', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'd', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'e', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'f', label: '배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              props: {
                size: 'mini'
              },
              on: { click: () => {
                this.close()
                this.$router.push({ path: '/ipAssignMng/ipAssign', query: { ipAddress: row.b } })
              }
            } }, '배정')
          }
        },
      ],
      tableDatas: [
        { a: 'K200013000', b: '61.74.143.0/24', c: '61.74.143.0', d: '61.74.143.255', e: '1', f: '' }
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.$set(this, 'selectedRow', model.row)
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
