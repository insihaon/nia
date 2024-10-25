<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="공지사항 등록"
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
    <div v-loading="viewLoading" class="popupContentTable">
      <table>
        <tr>
          <th>등록자</th>
          <td>{{ notice.screateNm }}</td>
          <th>이메일</th>
          <td>{{ notice.screateEmail }}</td>
        </tr>
        <tr>
          <th>제목</th>
          <td colspan="3">
            <el-input v-model="notice.sboardTitle" size="mini" type="text" />
          </td>
        </tr>
        <tr>
          <th>공지유형</th>
          <td class="textflex">
            <el-select v-model="notice.sboardTypeSubCd" size="mini" :disabled="viewType === 'U'">
              <el-option
                v-for="(typeItem, index) in boardTypes"
                :key="index"
                :label="typeItem.label"
                :value="typeItem.value"
              />
            </el-select>
          </td>
          <th>팝업게시기간</th>
          <td>
            <el-date-picker
              v-model="notice.popup_date"
              popper-class="noti-popper"
              :default-time="['00:00:00', '23:59:59']"
              size="mini"
              :disabled="notice.sboardTypeSubCd === 'BM0001'"
              type="datetimerange"
              range-separator="~"
              start-placeholder="시작"
              end-placeholder="종료"
            />
          </td>
        </tr>
        <tr>
          <th>내용</th>
          <td colspan="3">
            <textarea v-model="notice.sboardContents" rows="15"></textarea>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="viewType === 'I'" type="primary" size="small" icon="el-icon-edit" round @click="handleClickNoticeProcess('insertNotice')"> 등록</el-button>
      <el-button v-if="viewType === 'U'" type="primary" size="small" icon="el-icon-edit" round @click="handleClickNoticeProcess('updateNotice')"> 수정</el-button>
      <el-button v-if="viewType === 'U'" type="primary" size="small" icon="el-icon-delete" round @click="handleClickNoticeProcess('deleteNotice')"> 삭제</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalNoticeInsert'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewLoading: false,
      viewType: 'I',
      boardTypes: [],
      sboardTypeSubCd: '',
      user: {
        suserNm: '', // Populate this with the session user's name
        suserEmailAdr: '', // Populate this with the session user's email
      },
      notice: {
        screateNm: '',
        screateEmail: '',
        sboardTitle: '',
        sboardTypeSubCd: '',
        popup_date: [],
        sboardContents: '',
        sboardTypeCd: ''
      },
    }
  },
  mounted() {
    this.loadOptions()
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.notice.screateNm = this.$store.state.user.info.suserId
      this.notice.screateEmail = this.$store.state.user.info.suserEmailAdr
      this.viewType = model?.viewType ?? 'I'
      if (model?.notiSeq) {
        this.loadUpdateInfo(model.notiSeq)
      }
      if (this.viewType === 'I') {
        const today = this.moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
        this.notice.popup_date = [today, today]
      }
    },
    onClose() {
      this.notice = {
        screateNm: '',
        screateEmail: '',
        sboardTitle: '',
        sboardTypeSubCd: '',
        popup_date: [null, null],
        sboardContents: '',
        sboardTypeCd: ''
      }
    },
    async loadUpdateInfo(seq = null) {
      if (seq === null) return
      try {
        this.viewLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewUpdateNotice, { seq })
        this._merge(this.notice, res.result.data)
        const dnotiStartDt = this.notice.dnotiStartDt ? this.moment(this.notice.dnotiStartDt).format('YYYY-MM-DD HH:mm:ss') : null
        const dnotiEndDt = this.notice.dnotiEndDt ? this.moment(this.notice.dnotiEndDt).format('YYYY-MM-DD HH:mm:ss') : null
        this.notice.popup_date = [dnotiStartDt, dnotiEndDt]
      } catch (error) {
       this.error(error)
      } finally {
        this.viewLoading = false
      }
    },
    async loadOptions() {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertNotice, {})
        this.boardTypes = res.result.data.map(d => { return { label: d.name, value: d.code } })
        this.sboardTypeSubCd = this.boardTypes[0].value

        this.sboardTypeCd = res.result.data
      } catch (error) {
       this.error(error)
      }
    },
    handleClickNoticeProcess(apiKey) {
      if (apiKey === 'deleteNotice') {
        this.confirm('정말로 삭제 하시겠습니까?', '알림', {
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then(() => {
          this.fnProcessNotice(apiKey, '삭제')
        })
      } else {
        this.fnProcessNotice(apiKey, this.viewType === 'I' ? '등록' : '수정')
      }
    },
    async fnProcessNotice(apiKey, resMsg) {
      if (!Object.keys(ipmsJsonApis).includes(apiKey)) return
      try {
        // this.viewLoading = true
        const params = this.getParameterByProcessType(apiKey)
        const res = await apiRequestJson(ipmsJsonApis[apiKey], params)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, `공지사항이 정상적으로 ${resMsg}되었습니다.`)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
          this.close()
        }
      } catch (error) {
       this.error(error)
      } finally {
        // this.viewLoading = false
      }
    },
    getParameterByProcessType(process) {
      let param = {}
      const { seq, sboardTypeSubCd, sboardTitle, sboardContents, sboardTypeCd } = this.notice
      switch (process) {
        case 'insertNotice':
          param = { suseTypeCd: 'CY0001', sboardTitle, sboardContents, sboardTypeSubCd, sboardTypeCd }
          break
        case 'updateNotice':
          param = { seq, sboardTypeSubCd, sboardTitle, sboardContents }
          break
        case 'deleteNotice':
          param = { seq }
          break
        default:
          break
      }
      if (process !== 'deleteNotice' && this.notice.popup_date[0] !== null && this.notice.popup_date[1] !== null) {
        Object.assign(param, {
          dnotiStartDt: this.moment(this.notice.popup_date[0]).format('YYYY-MM-DD HH:mm:ss'),
          dnotiEndDt: this.moment(this.notice.popup_date[1]).format('YYYY-MM-DD HH:mm:ss')
        })
      }
      return param
    }
  },
}
</script>
<style lang="scss">
.noti-popper {
  .el-picker-panel__body {
    .el-input {
      width: 140px;
    }
  }
}
</style>
