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
        {{ viewTitle }} 검색
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <div class="optionBox">
          <el-row class="optionRow">
            <el-col :span="20" class="d-flex">
              <label>
                {{ label }} 명
              </label>
              <el-input
                v-model="searchTxt"
                size="mini"
                clearable
                @keyup.enter.native="handleClickSearch()"
              />
            </el-col>
            <el-col :span="4">
              <el-button class="btn-r ml-2" type="info" size="mini" icon="el-icon-search" @click="handleClickSearch()">
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
            :prop-table-height="300"
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
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalEntireOrgSearch'

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
      selectedRow: null,
      tableDatas: [],
      viewTitle: null
    }
  },
  computed: {
    label() {
      let columnLabel = ''
        if (this.viewTitle === '노드' || this.viewTitle === '주노드') {
          columnLabel = '노드'
        } else {
          columnLabel = '센터/지역본부'
        }
        return columnLabel
      },
    tableColumns() {
        let labelFrifix = ''

        switch (this.viewTitle) {
          case '노드':
            labelFrifix = '국사'
            break
          case '주노드':
            labelFrifix = '국사'
            break
          default:
            labelFrifix = '센터/지역본부'
        }
      return [
        { prop: 'slvlCd', label: `${labelFrifix} ID`, width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slvlNm', label: `${labelFrifix} 명`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
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
      if (model.viewTitle) {
        this.viewTitle = model?.viewTitle
      }
    },
    onClose() {
      if (this.selectedRow !== null) {
        if (this.viewTitle !== null) {
          this.$emit('selected-value', this.selectedRow, this.viewTitle)
        } else {
          this.$emit('selected-value', this.selectedRow)
        }
        this.searchTxt = ''
        this.tableDatas = []
        this.selectedRow = null
      }
      this.viewTitle = null
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    handleClickSearch() {
      switch (this.viewTitle) {
        case '센터/지역본부':
          this.fnSelectSearchLvlCd('Y')
          break
        case '노드':
          this.fnSelectSearchLvlCd('N')
          break
        case '주노드':
          this.fnSelectSearchLvlCd('N')
          break
        default:
          this.fnSelectSearchLvlCd('N')
          break
      }
    },
    async fnSelectSearchLvlCd(flag) {
      let sorgOfficeFlagYn

      if (flag === 'Y') {
        sorgOfficeFlagYn = 'Y'
        } else {
        sorgOfficeFlagYn = 'N'
      }

      const param = { searchWrd: this.searchTxt, sorgOfficeFlagYn: sorgOfficeFlagYn }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchLvlCd, param)
        this.tableDatas = res.tbLvlCdVos
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
