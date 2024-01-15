<template>
  <div>
    <transition :name="animation">
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
        class="datahub-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2" style="font-size: 17px;" />
          {{ title }}
          <hr>
        </span>
        <table class="basic">
          <tr>
            <th>테이블</th>
            <td v-if="grant === 'user'" colspan="3">
              {{ modalParam.table_nm }}
            </td>
            <td v-if="grant !== 'user'" colspan="3">
              <el-input v-model="modalParam.table_nm" />
            </td>
          </tr>

          <tr>
            <th>컬럼</th>
            <td v-if="grant === 'user'">
              {{ modalParam.column_nm }}
            </td>
            <td v-if="grant !== 'user'">
              <el-input v-model="modalParam.column_nm" />
            </td>
            <th>타입</th>
            <td v-if="grant === 'user'">
              {{ modalParam.data_type }}
            </td>
            <td v-if="grant !== 'user'">
              <el-select v-model="modalParam.data_type">
                <el-option label="character(1)" value="character(1)" />
                <el-option label="character(2)" value="character(2)" />
                <el-option label="character(3)" value="character(3)" />
                <el-option label="character(4)" value="character(4)" />
                <el-option label="character(5)" value="character(5)" />
                <el-option label="character(6)" value="character(6)" />
                <el-option label="character(8)" value="character(8)" />
                <el-option label="character(9)" value="character(9)" />
                <el-option label="character(10)" value="character(10)" />
                <el-option label="character(11)" value="character(11)" />
                <el-option label="character(12)" value="character(12)" />
                <el-option label="character(13)" value="character(13)" />
                <el-option label="character(17)" value="character(17)" />
                <el-option label="VARCHAR" value="VARCHAR" />
                <el-option label="INTEGER" value="INTEGER" />
                <el-option label="DATETIME" value="DATETIME" />
              </el-select>
            </td>
          </tr>

          <tr>
            <th>수정일시</th>
            <td v-if="grant === 'user'">
              {{ modalParam.update_time }}
            </td>
            <td v-if="grant !== 'user'" :class="isDisable">
              <el-date-picker
                v-model="modalParam.update_time"
                type="date"
                class="w-100"
              />
            </td>

            <th>편집자</th>
            <td>
              {{ modalParam.update_user }}
            </td>
          </tr>
          <tr>
            <th>설명</th>
            <td v-if="grant === 'user'" colspan="3">
              {{ modalParam.metadata_desc }}
            </td>
            <td v-if="grant !== 'user'" colspan="3">
              <el-input v-model="modalParam.metadata_desc" type="textarea" />
            </td>
          </tr>
        </table>

        <div slot="footer" class="dialog-footer">
          <el-button v-if="model.type !== 'detail'" size="medium" plain type="primary" class="aam-button" @click.native="isMode()">
            {{ btnText }}
          </el-button>
          <el-button size="medium" plain type="info" class="aam-button" @click.native="close()">
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
import moment from 'moment'
import { apiUpdateDataCatalogList, apiInsertDataCatalogList, } from '@/api/dataHub'

const routeName = 'CompApplyModal'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      modalParam: {},
      dataList: {},
      grant: '',
      row: ''
    }
  },
  computed: {
    isDisable() {
     const isDisabled = this.model.type === 'apply' ? 'disable-area' : ''
     return isDisabled
    },
    btnText() {
    if (this.model?.type === 'edit') {
      return '수정'
      } else {
       return '등록'
    }
    },
    title() {
      if (this.model?.type === 'apply') {
       return '등록'
      } else if (this.model?.type === 'edit') {
        return '수정'
      } else {
       return '상세보기'
     }
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    isMode() {
      if (this.model?.type === 'edit') {
        this.editMetaData()
      } else {
        this.applyMetaData()
      }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.grant = model?.grant
      if (model?.type === 'edit' || model?.type === 'detail') {
          this.modalParam = model?.row
      } else {
        this.modalParam = {}
      }
    },
    applyMetaData() {
        this.confirm('등록 하시겠습니까?', '등록', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            table_nm: this.modalParam.table_nm,
            column_nm: this.modalParam.column_nm,
            data_type: this.modalParam.data_type,
            update_time: moment(new Date().getTime()).format('YYYY-MM-DD HH:mm:00'),
            metadata_desc: this.modalParam.metadata_desc,
          }
          try {
              const res = await apiInsertDataCatalogList(param)
              if (res.success) {
                this.$emit('systemEdit')
                this.$message('등록 되었습니다.')
                this.close()
              }
          } catch (error) {
            this.$message.error({ message: `등록에 실패했습니다.` })
            console.error(error)
          }
        })
      },
        editMetaData() {
        this.confirm('수정하시겠습니까?', '수정', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            metadata_seq: this.modalParam.metadata_seq,
            table_nm: this.modalParam.table_nm,
            column_nm: this.modalParam.column_nm,
            data_type: this.modalParam.data_type,
            update_time: moment(new Date().getTime()).format('YYYY-MM-DD HH:mm:00'),
            metadata_desc: this.modalParam.metadata_desc,
          }
          try {
            if (this.model.type === 'edit') {
              const res = await apiUpdateDataCatalogList(param)
              if (res.success) {
                this.$message('수정 되었습니다.')
                this.$emit('systemEdit')
                this.close()
              }
            }
          } catch (error) {
            this.$message.error({ message: `수정에 실패했습니다.` })
            console.error(error)
          }
        })
      },
    },

  }
</script>

<style lang="scss" scope>

.CompApplyModal {
  font-family: "NanumSquare";
    .disable-area{
      color: #999;
      pointer-events: none;
      cursor: not-allowed;
    }
}
</style>

