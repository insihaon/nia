<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.minHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          SOP 수정
          <hr />
        </span>
        <div class="d-flex flex-column h-100 rounded justify-center" style="border: solid 1px #1e293b">
          <el-form ref="sopEdit" :model="sopInfo" class="h-full border rounded px-3 py-4">
            <el-col>
              <el-form-item label="등록자" class="d-flex">
                <el-input v-model="sopInfo.USER_ID" readonly />
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="SOP구분" class="d-flex">
                <el-select v-model="sopInfo.FAULT_GB">
                  <el-option v-for="item in fault_gb_list" :key="item" :value="item">
                    {{ item }}
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="항목" class="d-flex">
                <el-input v-model="sopInfo.FAULT_TYPE" />
              </el-form-item>
            </el-col>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
          <hr />
          <el-button size="small" plain class="mt-2" @click.native="onClickSopEdit()">
            {{ textByProcessType }}
          </el-button>
          <el-button v-if="processType === 'modify'" size="small" plain class="mt-2" @click="onClickSopDelete()"> 삭제 </el-button>
          <el-button size="small" plain class="close-btn mt-2" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiInsertSop, apiUpdateSop, apiDeleteSop } from '@/api/nia'

const routeName = 'ModalSopMngEdit'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      processType: 'add',
      sopInfo: {
        USER_ID: '',
        FAULT_GB: '',
        FAULT_TYPE: '',
      },
      editSucc: false,
      fault_gb_list: ['장애구분', '장애유형', '조치내용'],
    }
  },
  computed: {
    sopEditGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        {
          type: '',
          prop: 'reg_time',
          name: '등록일',
          width: 100,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: false,
          formatter: (row) => {
            return this.formatterTimeStamp(row.reg_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
        { type: '', prop: 'user_id', name: '등록자', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_gb', name: '조치 SOP', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_type', name: '항목', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        {
          type: '',
          prop: 'edit',
          name: '편집',
          width: 100,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: false,
          cellRenderer: () => {
            return `<button class='el-icon-edit' />`
          },
        },
      ]
      return { options, columns, data: this.sopCodeList }
    },
    textByProcessType() {
      return this.processType === 'add' ? '등록' : '수정'
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 400
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.processType = model.type
      if (model.type === 'modify') {
        const { seq: SEQ, user_id: USER_ID, fault_gb: FAULT_GB, fault_type: FAULT_TYPE } = model?.row
        this.sopInfo = this._cloneDeep({ SEQ, USER_ID, FAULT_GB, FAULT_TYPE })
      } else {
        this.sopInfo = {
          USER_ID: this.$store.state.user.name,
          FAULT_GB: '',
          FAULT_TYPE: '',
        }
      }
    },
    onClickSopEdit() {
      this.confirm(`${this.textByProcessType} 하시겠습니까?`, '알림', {
        confirmButtonText: this.textByProcessType,
        cancelButtonText: '취소',
      }).then(async () => {
        try {
          let res
          if (this.textByProcessType === '등록') {
            res = await apiInsertSop(this.sopInfo)
          } else {
            res = await apiUpdateSop(this.sopInfo)
          }
          if (res.success) {
            this.$alert(`${this.textByProcessType} 되었습니다.`, '알림', {
              confirmButtonText: '확인',
            })
            this.editSucc = true
            this.close()
          }
        } catch (error) {
          this.$alert(`${this.textByProcessType} 실패 하였습니다.`, '알림', {
            confirmButtonText: '확인',
          })
          console.error(error)
        }
      })
    },
    onClickSopDelete() {
      this.confirm(`삭제 하시겠습니까?`, '알림', {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
      }).then(async () => {
        try {
          const res = await apiDeleteSop(this.sopInfo)
          if (res.success) {
            this.$alert(`삭제 되었습니다.`, '알림', {
              confirmButtonText: '확인',
            })
          }
        } catch (error) {
          this.$alert(`삭제 실패 하였습니다.`, '알림', {
            confirmButtonText: '확인',
          })
          console.error(error)
        }
      })
    },
    onClose() {
      this.$emit('onClose', this.editSucc)
      this.editSucc = false
    },
  },
}
</script>
<style lang="scss" scoped>
// .ModalSopDetail ::v-deep {
//   .el-dialog__body {
//     height: 400px;
//   }
// }
.el-select {
  width: 100%;
}
::v-deep .el-form-item__label {
  width: 90px;
  margin-left: 5px;
  line-height: 20px;
}
::v-deep .el-form-item__content {
  width: calc(100% - 90px);
}
</style>
