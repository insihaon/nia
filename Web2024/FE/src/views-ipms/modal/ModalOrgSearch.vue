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
        운용팀 검색
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <div class="optionBox">
          <el-row class="optionRow">
            <el-col :span="20" class="d-flex">
              <label>
                운용조직 명
              </label>
              <el-input v-model="searchTxt" size="mini" clearable />
            </el-col>
            <el-col :span="4">
              <el-button class="btn-r ml-2" type="info" size="mini" icon="el-icon-search" @click="handleSearch()">
                조회
              </el-button>
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
            :prop-on-click="handleClickRow"
            :prop-on-dbl-click="handleDbClickRow"
          >
            <template slot="text-description">
              <span>
                운용팀 조회결과
              </span>
            </template>
          </compTable>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-edit" @click="handleSelect()">선택</el-button>
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

const routeName = 'ModalOrgSearch'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchTxt: '',
      selectedRow: null,
      componentList: [
         { key: 'InputType', props: { label: '운용조직 명', } },
      ],
       tableColumns: [
        // { prop: '', label: '선택', width: 50, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sktOrgId', label: '운용조직 ID', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sFullOrgNm', label: '운용조직 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: [
        { sktOrgId: '452333', sFullOrgNm: '주식회사 케이티-강남/서부1' },
        { sktOrgId: '452334', sFullOrgNm: '주식회사 케이티-강남/서부2' }
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
    },
    onClose() {
      if (this.selectedRow !== null) {
        // const keyValues = Object.keys(this.selectedRow).map(key=>{ return { key , value: this.selectedRow[v] }})
        // { label: this.selectedRow['sFullOrgNm'], value: this.selectedRow['sktOrgId'] }
        this.$emit('selected-value', this.selectedRow)
      }
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
    handleClickRow(row) {
      this.selectedRow = row
    },
    handleDbClickRow(row) {
      this.selectedRow = row
      this.close()
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
