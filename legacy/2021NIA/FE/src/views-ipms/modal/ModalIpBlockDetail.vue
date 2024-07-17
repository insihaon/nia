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
        IP블록관리 상세정보
        <hr>
      </span>

      <h4>IP블록관리 상세정보</h4>
      <table class="form">
        <tr>
          <th>공인/사설</th>
          <td>{{ selectedRow.sipCreateTypeNm }}</td>
          <th>생성차수</th>
          <td>
            <span v-if="type === 'edit'">
              <el-input v-model="sipCreateSeqNm"></el-input>
            </span>
            <span v-else>{{ selectedRow.sipCreateSeqNm }}</span>
          </td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td>{{ selectedRow.sipServiceNetNm }}</td>
          <th>작업일시</th>
          <td>{{ selectedRow.dmodifyDt }}</td>
        </tr>
        <tr>
          <th>IP 버전</th>
          <td>{{ selectedRow.sipVersionTypeNm }}</td>
          <th>IP 주소</th>
          <td>{{ selectedRow.pipPrefix }}</td>
        </tr>
        <tr class="last">
          <th>비고</th>
          <td colspan="3">
            <span v-if="type === 'edit'">
              <el-input v-model="scomment" type="textarea"></el-input>
            </span>
            <span v-else>{{ selectedRow.scomment }}</span>
          </td>
        </tr>
      </table>

      <h4>IP 블록 세부 정보</h4>
      <table class="form">
        <tr>
          <th>시작 IP</th>
          <td>{{ selectedRow.sipCreateTypeNm }}</td>
          <th>끝 IP</th>
          <td>{{ selectedRow.sipCreateSeqNm }}</td>
        </tr>
        <tr>
          <th>총 IP 수</th>
          <td>{{ selectedRow.sipServiceNetNm }}</td>
          <th>단위블록수</th>
          <td>{{ selectedRow.dmodifyDt }}</td>
        </tr>
        <tr>
          <th>사용 IP 수</th>
          <td>{{ selectedRow.sipVersionTypeNm }}</td>
          <th>가용 IP 수</th>
          <td>{{ selectedRow.pipPrefix }}</td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="type !== 'edit'" size="mini" icon="el-icon-edit" @click="onChangeMode()">수정</el-button>
        <el-button v-if="type === 'edit'" size="mini" icon="el-icon-edit" @click="handleEditIpBlockData()">수정</el-button>
        <el-button v-if="type !== 'edit'" size="mini" icon="el-icon-edit" @click="handleDeleteIpBlockData()">삭제</el-button>
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

const routeName = 'ModalIpBlockDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      sipCreateSeqNm: '',
      scomment: '',
      type: 'create',
      IpBlockDetail: []
    }
  },
  mounted() {
    // this.onloadIpDetailList
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
     this.selectedRow = model.row
    },
    onClose() {
      this.type = 'create'
    },
    onChangeMode() {
      this.type = 'edit'
    },
    onloadIpDetailList() {
     /*  const { key: seq } = this.selectedRow
      const param = seq
      try {
        const res = await apiSelectIpDetailList(param)
        this.IpBlockDetail = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
    handleDeleteIpBlockData() {
      this.$confirm('데이터를 삭제 하시겠습니까?', '삭제 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          if (this.selectedRow.length > 0) {
          const param = { nipBlockMstSeq: this.selectedRow.nipBlockMstSeq }
          const res = await /* apiDeleteIpBlockList */(param)
           if (res.result) {
             this.$message.success({ message: `삭제되었습니다.` })
            }
            this.selectedData = []
            this.$emit('reloadData')
          }
          } catch (error) {
            this.$message.error({ message: `삭제에 실패했습니다.` })
            console.log(error)
          }
        })
    },
    handleEditIpBlockData(mode) {
      this.confirm('수정하시겠습니까?', '수정 메시지', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const param = {
            nipBlockMstSeq: this.selectedRow.nipBlockMstSeq,
            sipCreateSeqNm: this.selectedRow.sipCreateSeqNm,
            scomment: this.selectedRow.scomment,
          }
           if (mode) {
             this._merge(param, { commit: false })
           }
          const res = await /* apiEditIpBlockList */(param)
          if (res.success) {
            this.$message('수정 되었습니다.')
            this.$emit('reloadData')
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    }
  },
}
</script>
<style lang="scss" scoped>
.dynamic-container ::v-deep {
  .optionItem {
    display: flex;
  }
}
</style>
