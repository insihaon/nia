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
              <input v-model="sipCreateSeqCd">
            </span>
            <span v-else>{{ selectedRow.sipCreateSeqCd }}</span>
          </td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td>{{ selectedRow.ssvcLineTypeNm }}</td>
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
          <td>{{ selectedRow.sfirstAddr }}</td>
          <th>끝 IP</th>
          <td>{{ selectedRow.slastAddr }}</td>
        </tr>
        <tr>
          <th>총 IP 수</th>
          <td>{{ selectedRow.ncnt }}</td>
          <th>단위블록수</th>
          <td>{{ selectedRow.nclassCnt }}</td>
        </tr>
        <tr>
          <th>사용 IP 수</th>
          <td>{{ selectedRow.nuseIpCnt }}</td>
          <th>가용 IP 수</th>
          <td>{{ selectedRow.nfreeIpCnt }}</td>
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
      sipCreateSeqCd: '',
      scomment: '',
      type: 'create',
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
      this.$set(this, 'selectedRow', model.row)
       const { sipCreateSeqCd, scomment } = this.selectedRow
        this.sipCreateSeqCd = sipCreateSeqCd
        this.scomment = scomment

      if (model.type === 'edit') {
        this.type = 'edit'
      }
    },
    onClose() {
      this.type = 'create'
    },
    onChangeMode() {
      this.type = 'edit'
    },
   /*  onloadIpDetailList() {
      const { key: seq } = this.selectedRow
      const param = seq
      try {
        const res = await apiSelectIpDetailList(param)
        this.IpBlockDetail = res?.result.data
      } catch (error) {
        console.error(error)
      }
    }, */
    handleDeleteIpBlockData() { // IP 블럭 삭제
      this.$confirm('IP블럭을 삭제 하시겠습니까?', '삭제 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          if (this.selectedRow.length > 0) {
          const param = { nipBlockMstSeq: this.selectedRow.nipBlockMstSeq }
          const res = await /* apiDeleteIpBlockList */(param)
           if (res.data.commonMsg === 'SUCCESS') {
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
    async fnUpdateCrtIPMstCallback() { // IP 블럭 수정
      const sipCreateSeqCd = this.selectedRow.sipCreateSeqCd
      if (sipCreateSeqCd.length < 10) {
          this.$message.error('생성차수 수정정보가 잘못되었습니다.')
          return
      }

      try {
          await this.confirm('IP블럭을 수정하시겠습니까?', '수정 메시지', {
              confirmButtonText: 'OK',
              cancelButtonText: 'Cancel',
              type: 'success',
          })

          const tbIpBlockMstVo = {
              nipBlockMstSeq: this.selectedRow.nipBlockMstSeq,
              sipCreateTypeCd: this.selectedRow.sipCreateTypeCd,
              sipCreateSeqCd: this.selectedRow.sipCreateSeqCd,
              sipVersionTypeCd: this.selectedRow.sipVersionTypeCd,
              pipPrefix: this.selectedRow.pipPrefix,
              scomment: this.scomment,
          }

          // 1. 블록 정보 수정  /ipmgmt/createmgmt/selectListSipCreateSeqCd.json'
            const res = await /* apiEditIpBlockList*/(tbIpBlockMstVo)
            if (res.data.commonMsg === 'SUCCESS') {
            // 2. 생성차수 수정 /ipmgmt/createmgmt/updateCrtIPMst.json'

              const searchSipCreateSeqCd = tbIpBlockMstVo.sipCreateSeqCd

              const res2 = await/* apiCreatSeaCd */ (searchSipCreateSeqCd)

            if (res && res2) {
              this.$message('IP블록 수정이 정상적으로 처리되었습니다.')
              this.$emit('reloadData')
            }
          }
      } catch (error) {
          this.$message.error({ message: `IP블록 수정에 실패했습니다.` })
          console.error(error)
      }
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
