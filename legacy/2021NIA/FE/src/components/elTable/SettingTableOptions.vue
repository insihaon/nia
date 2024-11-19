<template>
  <div>
    <transition :name="animation">
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
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-setting mr-1" />
          설정
          <hr>
        </span>
        <div>
          <ElTableDraggable class="h-100" handle=".el-icon-rank">
            <el-table
              ref="table"
              row-key="index"
              size="mini"
              fit
              highlight-current-row
              :data="settingColumnList"
              :height="400"
            >
              <el-table-column
                v-for="(item, index) in columns"
                :key="index"
                :prop="item.prop"
                :label="item.label"
                :align="item.align"
                :formatter="item.formatter"
              >
              </el-table-column>
              <el-table-column width="45" label="순서" :align="'center'">
                <i class="el-icon-rank" style="font-size: 16px" />
              </el-table-column>
            </el-table>
          </ElTableDraggable>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" type="primary" size="mini" round @click="reset">초기화</el-button>
          <el-button icon="el-icon-check" type="primary" size="mini" round @click="saveColumnState">저장</el-button>
          <el-button size="mini" type="primary" class="el-icon-close" round plain @click="close()">{{ $t('exit') }}</el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'

const routeName = 'SettingTableOptions'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  props: {
    prop_name: {
      type: String,
      default: ''
    },
    prop_columns: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      savedColumnState: null,
      settingColumnList: []
    }
  },
  computed: {
    columns() {
      return [
      { prop: 'label', label: '컬럼명', align: 'center' },
      { prop: 'show', label: 'S/H', align: 'center',
        formatter: (row, col, value, index) => {
            return this.$createElement('el-switch', {
              props: {
                value: row.show,
                'active-text': 'SHOW',
                'inactive-text': 'HIDE',
              },
              on: { change: () => { row.show = !row.show } }
            }
          )
        }
      },
      { prop: 'width', label: '너비', align: 'center',
        formatter: (row, col, value, index) => {
            return this.$createElement('el-input-number', {
              props: {
                value: row.width,
                min: 1,
                'controls-position': 'right'
              },
              on: { change: (value) => { row.width = value } }
            }
          )
        }
      }]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
      // this.animation = 'bounceUp'
    },
    onOpen(model, actionMode) {
      this.setColumnList()
    },
    onClose() { },
    setColumnList() {
      const name = this.prop_name
      let savedColumnState
      if (name) {
        savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')[name]
      }
      if (!savedColumnState) {
        savedColumnState = this.prop_columns
      }
      if (!savedColumnState || !name) {
        this.error('no order and visibility state to restore by, you must save order and visibility state first')
        return
      }
      this.settingColumnList = savedColumnState.map((col, index) => {
        if (col.prop.length === 0) {
          return null
        }
        const columnInfo = this.$parent.$refs.table.columns.find(v => v.property === col.prop)
        const defaultWidth = document.querySelector(`.${columnInfo.id}`).clientWidth
        return {
          index,
          prop: col.prop,
          label: col.label,
          show: col.columnVisible,
          width: col?.width ?? defaultWidth
        }
      })
      if (this.settingColumnList.includes(null)) {
        this.settingColumnList = []
        this.error('An empty value exists in prop.')
        this.close()
      }
    },
    reset() {
      const name = this.prop_name
      if (name) {
        this.confirm('초기화 하시겠습니까?', '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(() => {
        const savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')
        const currentColumnState = this._cloneDeep(this.prop_columns)
        savedColumnState[name] = currentColumnState
        window.localStorage['savedColumnState'] = JSON.stringify(savedColumnState)
        onMessagePopup(this, '설정이 초기화 되었습니다. 화면을 새로고침 합니다.')
        setTimeout(() => {
          window.location.reload()
        }, 200)
      })
      }
    },
    saveColumnState() {
      this.confirm('현재 상태를 저장 하시겠습니까?', '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(() => {
        const currentColumnState = this._cloneDeep(this.settingColumnList)

        currentColumnState.forEach((row, index) => {
          Object.assign(row, this.prop_columns.find(r => r.prop === row.prop))
          row.columnVisible = row.show
          row.index = index
        })
        const name = this.prop_name
        if (name) {
          const savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')
          savedColumnState[name] = currentColumnState
          window.localStorage['savedColumnState'] = JSON.stringify(savedColumnState)
          onMessagePopup(this, '설정이 저장 되었습니다. 화면을 새로고침 합니다.')
          setTimeout(() => {
            window.location.reload()
          }, 200)
        }
      })
      .catch(action => {
      })
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-input-number {
  width: 100px;
}
</style>
