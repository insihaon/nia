<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="공지사항 상세"
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
    <div class="popupContentTable">
      <table>
        <tr>
          <th class="flex">
            <span class="tableSubject">[[{{ notiDetail.sboardTypeSubNm }}]{{ notiDetail.sboardTitle }}</span>
            <span><span>작성자</span>{{ notiDetail.screateNm }}({{ notiDetail.screateEmail }})</span>
          </th>
        </tr>
        <tr>
          <th class="flex">
            <span><span>팝업게시기간</span>{{ moment(notiDetail.dnotiStartDt).format(dateFormat) }} ~ {{ moment(notiDetail.dnotiEndDt).format(dateFormat) }}</span>
          </th>
        </tr>
        <tr>
          <th class="flex">
            <span><span>등록일</span>{{ moment(notiDetail.dcreateDt).format(dateFormat) }}</span>
            <span><span>수정일</span>{{ moment(notiDetail.dmodifyDt).format(dateFormat) }}</span>
            <span><span>조회수</span>{{ notiDetail.nreadCnt }}</span>
          </th>
        </tr>
        <tr>
          <td>
            <textarea v-model="notiDetail.sboardContents" style="min-height: 360px;" readonly />
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <template v-if="!isDashBoard">
        <el-button type="primary" size="small" icon="el-icon-edit" round @click="handleClickUpdate">수정</el-button>
        <el-button type="primary" size="small" icon="el-icon-delete" round @click="fnDeleteNotice(notiDetail.seq)">삭제</el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalNoticeDetail'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      isDashBoard: false,
      dateFormat: 'YYYY-MM-DD HH:mm:ss',
      notiDetail: {
        sboardTitle: '',
        sboardTypeSubNm: '',
        screateNm: '',
        screateEmail: '',
        dnotiStartDt: '',
        dnotiEndDt: '',
        dcreateDt: '',
        dmodifyDt: '',
        nreadCnt: 0,
        sboardContents: ''
      },
      adminYn: ''
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model?.param) {
        this.fnViewDetailNotice(model?.param)
      }
      this.isDashBoard = model?.isDashboard ?? false
    },
    onClose() {
      this.notiDetail = {
        sboardTitle: '',
        sboardTypeSubNm: '',
        screateNm: '',
        screateEmail: '',
        dnotiStartDt: '',
        dnotiEndDt: '',
        dcreateDt: '',
        dmodifyDt: '',
        nreadCnt: 0,
        sboardContents: ''
      }
    },
    async fnViewDetailNotice(param) {
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailNotice, param)
        this.notiDetail = res.result.data
      } catch (error) {
       this.error(error)
      } finally {
        this.loading = false
      }
    },
    fnDeleteNotice(seq) {
      const THIS = this
      this.confirm('정말로 삭제 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
      }).then(async() => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteNotice, { seq })
          if (res.commonMsg === 'SUCCESS') {
            THIS.$emit('reload')
            onMessagePopup(THIS, `공지사항이 정상적으로 삭제되었습니다.`)
            THIS.close()
          } else {
            onMessagePopup(THIS, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    handleClickUpdate() {
      this.$emit('openUpdate', this.notiDetail.seq)
      this.close()
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
