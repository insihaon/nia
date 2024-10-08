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
        공지사항 등록
        <hr>
      </span>
      <div id="content" v-loading="loading" class="layer info">
        <table class="tbl_data entry">
          <colgroup>
            <col width="14%" />
            <col width="17%" />
            <col width="14%" />
            <col width="55%" />
          </colgroup>
          <tbody>
            <tr class="top">
              <th class="first" scope="row">등록자</th>
              <td class="view">{{ notice.screateNm }}</td>
              <th scope="row">이메일</th>
              <td class="view">{{ notice.screateEmail }}</td>
            </tr>
            <tr>
              <th class="first" scope="row">제목</th>
              <td colspan="3">
                <input v-model="notice.sboardTitle" type="text" class="txt" />
              </td>
            </tr>
            <tr>
              <th class="first" scope="row">공지유형</th>
              <td>
                <el-select v-model="notice.sboardTypeSubCd" size="mini" :disabled="viewType === 'U'">
                  <el-option
                    v-for="(typeItem, index) in boardTypes"
                    :key="index"
                    :label="typeItem.label"
                    :value="typeItem.value"
                  />
                </el-select>
              </td>
              <th class="first" scope="row">팝업게시기간</th>
              <td>
                <el-date-picker
                  v-model="notice.popup_date"
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
            <tr class="last">
              <th class="first" scope="row">내용</th>
              <td colspan="3">
                <textarea v-model="notice.sboardContents" class="w98" rows="20"></textarea>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="viewType === 'I'" size="mini" class="el-icon-edit" @click="handleClickNoticeProcess('insertNotice')"> 등록</el-button>
        <el-button v-if="viewType === 'U'" size="mini" class="el-icon-edit" @click="handleClickNoticeProcess('updateNotice')"> 수정</el-button>
        <el-button v-if="viewType === 'U'" size="mini" class="el-icon-delete" @click="handleClickNoticeProcess('deleteNotice')"> 삭제</el-button>
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

const routeName = 'ModalNoticeInsert'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
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
        popup_date: '',
        sboardContents: '',
        sboardTypeCd: ''
      },
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.notice.screateNm = this.$store.state.user.info.suserId
      this.notice.screateEmail = this.$store.state.user.info.Email
      this.loadOptions()
      this.viewType = model?.viewType ?? 'I'
      if (model?.notiSeq) {
        this.loadUpdateInfo(model.notiSeq)
      }
    },
    onClose() {
      this.notice = {
        screateNm: '',
        screateEmail: '',
        sboardTitle: '',
        sboardTypeSubCd: '',
        popup_date: '',
        sboardContents: '',
        sboardTypeCd: ''
      }
    },
    async loadUpdateInfo(seq = null) {
      if (seq === null) return
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewUpdateNotice, { seq })
        this._merge(this.notice, res.result.data)
        const dnotiStartDt = this.moment(this.notice.dnotiStartDt).format('YYYY-MM-DD HH:mm:ss')
        const dnotiEndDt = this.moment(this.notice.dnotiEndDt).format('YYYY-MM-DD HH:mm:ss')
        this.notice.popup_date = [dnotiStartDt, dnotiEndDt]
      } catch (error) {
       this.error(error)
      } finally {
        this.loading = false
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
        this.loading = true
        const res = await apiRequestJson(ipmsJsonApis[apiKey], this.getParameterByProcessType(apiKey))
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, `공지사항이 정상적으로 ${resMsg}되었습니다.`)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
       this.error(error)
      } finally {
        this.loading = false
      }
    },
    getParameterByProcessType(process) {
      let param = {}
      const { seq, sboardTypeSubCd, sboardTitle, sboardContents, sboardTypeCds } = this.notice
      switch (process) {
        case 'insertNotice':
          param = { suseTypeCd: 'CY0001', sboardTitle, sboardContents, sboardTypeSubCd, sboardTypeCds }
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
      if (process !== 'deleteNotice' && this.notice.popup_date !== null) {
        Object.assign(param, {
          dnotiStartDt: this.moment(this.notice.popup_date[0]).format('YYYY-MM-DD HH:mm:ss'),
          dnotiEndDt: this.moment(this.notice.popup_date[1].format('YYYY-MM-DD HH:mm:ss'))
        })
      }
      return param
    }
  },
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%
}
</style>
