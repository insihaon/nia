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
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록분할
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <el-row>
          <div class="title">IP 블록 기본 정보</div>
          <div class="optionBox">
            <el-row class="optionRow">
              <el-col
                v-for="item in ipBlockInfo"
                v-if="item.show"
                :key="item.key"
                class="d-flex"
                :span="12"
              >
                <label>{{ item.label }}</label>
                <el-input v-model="tempVal" readonly size="mini" />
              </el-col>
            </el-row>
          </div>
        </el-row>
        <el-row>
          <div class="title">분할 정보</div>
          <div class="optionBox">
            <el-row class="optionRow">
              <el-col class="d-flex items-center">
                <label>분할단위</label>
                <el-input v-model="unit" size="mini" :maxlength="3" style="width: 200px" /> BitMask
                <el-button size="mini">분할</el-button>
              </el-col>
              <el-col class="d-flex">
                <label>비고</label>
                <el-input v-model="etc" type="textarea" />
              </el-col>
            </el-row>
          </div>
        </el-row>
        <el-row>
          <el-col :span="24">
            <compTable
              :prop-data="tableDatas"
              :prop-table-height="200"
              :prop-column="tableColumns"
              :prop-is-pagination="false"
              :prop-is-check-box="false"
              prop-grid-menu-id="inputSpeed"
              :prop-grid-indx="1"
            >
              <template slot="text-description">
                <span>
                  분할 예정 정보
                </span>
              </template>
            </compTable>
          </el-col>
        </el-row>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini">병합</el-button>
        <el-button size="mini" icon="el-icon-menu">분할확정</el-button>
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

const routeName = 'ModalIpBlockDivision'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      isViewTypeSeonbeonjang: false,
      selectedRow: null,
      tempVal: '',
      etc: '',
      unit: 27,
      tableDatas: [
        // { unit: 28 }
      ]
    }
  },
  computed: {
    tableColumns() {
      return [
        { prop: '', label: this.isViewTypeSeonbeonjang ? '선번장블록' : 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '단위블록수', align: 'center', sortable: true, columnVisible: this.isViewTypeSeonbeonjang, showOverflow: true },
        { prop: '', label: 'BitMask', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'unit', label: '분할단위', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            return this.$createElement('el-input', {
              props: {
                size: 'mini',
                value: row.unit
              }
            })
          }
        },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    },
    ipBlockInfo() {
      return [
       { label: '계위', show: true },
       { label: this.isViewTypeSeonbeonjang ? '용도' : '할당상태', show: true },
       { label: 'IP 버전', show: true },
       { label: 'IP 주소', show: true },
       { label: '시작 IP', show: true },
       { label: '끝 IP', show: true },
       { label: '총 IP 수', show: true },
       { label: '단위블록수', show: !this.isViewTypeSeonbeonjang },
      ]
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.isViewTypeSeonbeonjang = model.isSeonbeonjang ?? false
      this.$set(this, 'selectedRow', model.row)
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
