<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
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
        공지사항 상세
        <hr>
      </span>
      <div id="content" v-loading="loading" class="layer info">
        <div class="data_view">
          <div class="data_tit">
            <h3 :title="notiDetail.sboardTitle">
              <span>[{{ notiDetail.sboardTypeSubNm }}]</span>{{ notiDetail.sboardTitle }}
            </h3>
            <dl>
              <dt>작성자 :</dt>
              <dd>
                <ul>
                  <li>{{ notiDetail.screateNm }}<span>({{ notiDetail.screateEmail }})</span></li>
                </ul>
              </dd>
            </dl>
          </div>
          <div id="notiDate" class="data_tit_sub">
            <dl class="fl">
              <dt>팝업게시기간 :</dt>
              <dd>{{ moment(notiDetail.dnotiStartDt).format(dateFormat) }} ~ {{ moment(notiDetail.dnotiEndDt).format(dateFormat) }}</dd>
            </dl>
          </div>
          <div class="data_tit_sub last">
            <dl class="fl">
              <dt>등록일 :</dt>
              <dd>{{ moment(notiDetail.dcreateDt).format(dateFormat) }}</dd>
              <dt class="ml15">수정일 :</dt>
              <dd>{{ moment(notiDetail.dmodifyDt).format(dateFormat) }}</dd>
            </dl>
            <dl class="unit_fr">
              <dt>조회수 :</dt>
              <dd>{{ notiDetail.nreadCnt }}</dd>
            </dl>
          </div>
          <div class="data_con w-100">
            <textarea v-model="notiDetail.sboardContents" readonly />
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <template v-if="!isDashBoard">
          <el-button size="mini" class="el-icon-edit" @click="handleClickUpdate">수정</el-button>
          <el-button size="mini" class="el-icon-delete" @click="fnDeleteNotice(notiDetail.seq)">삭제</el-button>
        </template>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
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
