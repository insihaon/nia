<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        화면명 검색
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <div class="optionBox">
          <el-row class="optionRow">
            <el-col :span="20" class="d-flex">
              <label>
                사용자명
              </label>
              <el-select
                v-model="searchTxtCd"
                collapse-tags
                size="mini"
                class="w-20"
              >
                <el-option
                  v-for="(option, i) in [
                    { label: '화면명', value: 'sscrnNm' },
                    { label: '화면ID', value: 'sscrnId' },
                  ]"
                  :key="i"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
              <el-input
                v-model="searchTxt"
                size="mini"
                clearable
                class="w-80"
                @keyup.enter.native="fnSelectSearchScrnBas()"
              />
            </el-col>
            <el-col :span="4">
              <el-button class="btn-r ml-2" type="info" size="mini" icon="el-icon-search" @click="fnSelectSearchScrnBas()">
                조회
              </el-button>
            </el-col>
          </el-row>
        </div>
        <el-col :span="24">
          <compTable
            ref="compTable"
            :prop-name="name"
            :prop-data="tableDatas"
            :prop-table-height="500"
            :prop-column="tableColumns"
            :prop-is-pagination="false"
            :prop-is-check-box="true"
            :prop-is-cell-click-check="true"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
            :prop-on-click="handleClickRow"
            :prop-on-dbl-click="handleDbClickRow"
          >
            <template slot="text-description">
              <span>
                화면정보 조회결과
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
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalSearchTbScrnBas'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      typeByAZ: null,
      searchTxt: '',
      searchTxtCd: 'sscrnNm',
      selectedRow: null,
      tableDatas: [],
      tableColumns: [
        { prop: 'sscrnId', label: `화면 ID`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnNm', label: `화면명`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnUrlAdr', label: `화면 URL`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
  },
  computed: {
  },
  mounted() {
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
        this.$emit('selected-value', this.selectedRow)
      }
        this.searchTxt = ''
        this.searchTxtCd = ''
        this.tableDatas = []
        this.selectedRow = null
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    async fnSelectSearchScrnBas() {
      if (this.searchTxt.length < 2) {
       this.$message('검색어는 두 글자 이상 입력하세요.')
       return
      }
      const tbScrnBasVo = { searchWrd: this.searchTxt, searchTxtCd: this.searchTxtCd }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchScrnBas, tbScrnBasVo)
        this.tableDatas = res.tbScrnBasVos
      } catch (error) {
        this.error(error)
      }
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
