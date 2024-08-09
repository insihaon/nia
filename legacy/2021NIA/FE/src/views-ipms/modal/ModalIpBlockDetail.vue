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
          <td>{{ resultVo.sipCreateTypeNm }}</td>
          <th>생성차수</th>
          <td>
            <span v-if="type === 'edit'">
              <input v-model="sipCreateSeqCd">
            </span>
            <span v-else>{{ resultVo.sipCreateSeqCd }}</span>
          </td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td>{{ resultVo.ssvcLineTypeNm }}</td>
          <th>작업일시</th>
          <td>{{ resultVo.dmodifyDt }}</td>
        </tr>
        <tr>
          <th>IP 버전</th>
          <td>{{ resultVo.sipVersionTypeNm }}</td>
          <th>IP 주소</th>
          <td>{{ resultVo.pipPrefix }}</td>
        </tr>
        <tr class="last">
          <th>비고</th>
          <td colspan="3">
            <span v-if="type === 'edit'">
              <el-input v-model="scomment" type="textarea"></el-input>
            </span>
            <span v-else>{{ resultVo.scomment }}</span>
          </td>
        </tr>
      </table>

      <h4>IP 블록 세부 정보</h4>
      <table class="form">
        <tr>
          <th>시작 IP</th>
          <td>{{ resultVo.sfirstAddr }}</td>
          <th>끝 IP</th>
          <td>{{ resultVo.slastAddr }}</td>
        </tr>
        <tr>
          <th>총 IP 수</th>
          <td>{{ resultVo.ncnt }}</td>
          <th>단위블록수</th>
          <td>{{ resultVo.nclassCnt }}</td>
        </tr>
        <tr>
          <th>사용 IP 수</th>
          <td>{{ resultVo.nuseIpCnt }}</td>
          <th>가용 IP 수</th>
          <td>{{ resultVo.nfreeIpCnt }}</td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="type !== 'edit'" size="mini" icon="el-icon-edit" @click="onChangeMode()">수정</el-button>
        <el-button v-if="type === 'edit'" size="mini" icon="el-icon-edit" @click="fnUpdateCrtIPMstCallback()">수정</el-button>
        <el-button v-if="type !== 'edit'" size="mini" icon="el-icon-edit" @click="fnDeleteBtnClick()">삭제</el-button>
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
import { ipmsJsonApis } from '@/api/ipms'

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
      resultVo: null,
      sipCreateSeqCd: '',
      scomment: '',
      type: 'create',
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.$set(this, 'resultVo', model.row)
       const { sipCreateSeqCd, scomment } = this.resultVo
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
    fnDeleteBtnClick() { // IP 블럭 삭제
      this.$confirm('IP블럭을 삭제 하시겠습니까?', '삭제 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          if (this.resultVo.length > 0) {
          const param = { nipBlockMstSeq: this.resultVo.nipBlockMstSeq }
          const res = await ipmsJsonApis(ipmsJsonApis.deleteCrtIPMst, param)
           if (res.data.commonMsg === 'SUCCESS') {
             this.$message.success({ message: `삭제되었습니다.` })
            }
            this.$emit('reloadData')
          }
          } catch (error) {
            this.$message.error({ message: `삭제에 실패했습니다.` })
            console.log(error)
          }
        })
    },
    async fnUpdateCrtIPMstCallback() { // IP 블럭 수정
      const sipCreateSeqCd = this.resultVo.sipCreateSeqCd
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

          const tbIpBlockVo = {
            sipCreateTypeCd: this.resultVo.sipCreateTypeCd,
            scomment: this.resultVo.scomment,
            nipBlockMstSeq: this.resultVo.nipBlockMstSeq,
          }

          /* 생성차수 수정 */
            //  const searchSipCreateSeqCd = tbIpBlockMstVo.sipCreateSeqCd
          // const res = await ipmsJsonApis(ipmsJsonApis.selectListSipCreateSeqCd, tbIpBlockVo)

          const res = await ipmsJsonApis(ipmsJsonApis.updateCrtIPMst, tbIpBlockVo)

           if (res.data.commonMsg === 'SUCCESS') {
              this.$message('IP블록 수정이 정상적으로 처리되었습니다.')
              this.$emit('reloadData')
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
