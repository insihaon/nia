<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="운용정보 상세정보"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">링크정보</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="27%" />
          <col width="23%" />
          <col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>링크IP블록</th>
            <td colspan="3">{{ resultVo.pifSerialIp }}</td>
          </tr>
          <tr>
            <th>자국 수용국</th>
            <td>{{ resultVo.saofficescodeNm }}</td>
            <th>대국 수용국</th>
            <td>{{ resultVo.szofficescodeNm }}</td>
          </tr>
          <tr>
            <th>자국 장비명</th>
            <td>{{ resultVo.sanealias }}</td>
            <th>대국 장비명</th>
            <td>{{ resultVo.sznealias }}</td>
          </tr>
          <tr>
            <th>자국 장비IP</th>
            <td>{{ resultVo.samstip }}</td>
            <th>대국 장비IP</th>
            <td>{{ resultVo.szmstip }}</td>
          </tr>
          <tr>
            <th>자국 IF명</th>
            <td>{{ resultVo.saifname }}</td>
            <th>대국 IF명</th>
            <td>{{ resultVo.szifname }}</td>
          </tr>
          <tr>
            <th>SAID</th>
            <td>{{ resultVo.ssaid }}</td>
            <th>전용번호</th>
            <td>{{ resultVo.sllnum }}</td>
          </tr>
          <tr>
            <th>수용회선명</th>
            <td colspan="3">{{ resultVo.sconnalias }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="handleClickUpdate"> 수정</el-button> <!-- btnUpdateLinkInfo -->
      <el-button type="primary" size="small" icon="el-icon-delete" round @click="fnDeleteLinkIpMst"> 삭제</el-button> <!-- btnDeleteLinkInfo -->
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalIpLinkMstDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      defaultResultVo: {
        pifSerialIp: '',
        saofficescodeNm: '',
        szofficescodeNm: '',
        sanealias: '',
        sznealias: '',
        samstip: '',
        szmstip: '',
        saifname: '',
        szifname: '',
        ssaid: '',
        sllnum: '',
        sconnalias: '',
        allocCnt: ''
      },
      resultVo: {}
    }
  },
  mounted () {
    this.tbIpInfoVo = this._cloneDeep(this.defaultTbIpInfoVo)
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model?.nipLinkMstSeq) {
        this.fnViewDetailIPLinkMst(model.nipLinkMstSeq)
      }
    },
    onClose() {
    },
    async fnViewDetailIPLinkMst(nipLinkMstSeq) {
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailIPLinkMst, { nipLinkMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
       this.error(error)
      } finally {
        this.loading = false
      }
    },
    handleClickUpdate() {
      this.$emit('reOpenUpdate', this.resultVo.nipLinkMstSeq)
      this.close()
    },
    async fnDeleteLinkIpMst() {
      const row = this.resultVo
      if (row.allocCnt > 0) {
        onMessagePopup(this, `해당 정보로 할당 테이블에 ${row.allocCnt}건이 링크 할당이 되어 있으므로 삭제 불가합니다.`)
        return
      }
      this.confirm('해당 운용정보를 삭제 하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'info'
      }).then(async () => {
        try {
          this.loading = true
          const res = await apiRequestJson(ipmsJsonApis.deleteLinkIpMst, { nipLinkMstSeq: row.nipLinkMstSeq })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
            this.loading = true
            this.$emit('reload')
            this.resultVo = this._cloneDeep(this.defaultResultVo)
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
      .catch(action => {
      })
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
