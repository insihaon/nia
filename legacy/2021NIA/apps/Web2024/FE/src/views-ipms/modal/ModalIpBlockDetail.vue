<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="title"
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
      <div ref="content" class="popupContentTableTitle">IP블록관리 상세정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <th>공인/사설</th>
          <td>{{ resultVo.sipCreateTypeNm }}</td>
          <th>생성차수</th>
          <td>
            <span v-if="isEdit">
              <el-autocomplete
                v-model="sipCreateSeqCd"
                class="inline-input w-100"
                :fetch-suggestions="querySearch"
              >
              </el-autocomplete>
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
            <span v-if="isEdit">
              <el-input v-model="scomment" type="textarea"></el-input>
            </span>
            <span v-else>{{ resultVo.scomment }}</span>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP 블록 세부 정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
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
    </div>

    <div class="popupContentTableBottom">
      <el-button v-if="!isEdit" type="primary" size="small" icon="el-icon-edit" round @click="onChangeMode()">수정</el-button>
      <el-button v-if="isEdit" type="primary" size="small" icon="el-icon-edit-outline" round @click="fnUpdateCrtIPMstCallback()">저장</el-button>
      <el-button v-if="!isEdit" type="primary" size="small" icon=" el-icon-delete" round @click="fnDeleteBtnClick()">삭제</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="isClose()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

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
      resultVo: {},
      sipCreateSeqCd: '',
      sipCreateSeqOp: [],
      scomment: '',
      type: 'create',
      sipCreateSeqCdOptions: [],
      loading: false,
    }
  },
  computed: {
    title() {
      return 'IP블록관리 ' + (this.type === 'edit' ? '수정' : '상세정보')
    },
    isEdit() {
      return this.type === 'edit'
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    async querySearch(queryString, cb) {
    await this.fnSelectSipCd()
    const results = queryString ? this.sipCreateSeqCdOptions.filter(this.createFilter(queryString)) : []
    cb(results)
    },
    createFilter(queryString) { /* 검색어의 첫글자 부터 일치하는지에 대한 옵션  */
      return (link) => {
        return link.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
      }
    },
    async fnSelectSipCd() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectListSipCreateSeqCd, { sipCreateSeqCd: this.sipCreateSeqCd })
        this.sipCreateSeqCdOptions = res.map((item) => ({ label: item, value: item }))
      } catch (error) {
        console.error(error)
      }
    },
    remoteMethod(query) {
      if (query.length < 7) return
      setTimeout(() => {
        this.fnSelectSipCd()
      }, 30)
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model.type === 'edit') {
        this.type = 'edit'
      }
      if (model.row) {
        setTimeout(() => {
          this.fnViewDetailClick(model.row)
        }, 30)
      }
    },
    async fnViewDetailClick(row, type) {
      const { nipBlockMstSeq } = row
      const target = ({ vue: this.$refs.content })
      const param = {
        nipBlockMstSeq: nipBlockMstSeq ?? ''
      }
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewDetailCrtIPMst, param)
        if (res?.result?.data) {
          this.resultVo = res?.result?.data
          const { sipCreateSeqCd, scomment } = this.resultVo ?? {}
          this.sipCreateSeqCd = sipCreateSeqCd
          this.scomment = scomment
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
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
        let res
        try {
          const param = { nipBlockMstSeq: this.resultVo.nipBlockMstSeq }
           res = await apiRequestJson(ipmsJsonApis.deleteCrtIPMst, param)
           if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '삭제되었습니다')
            this.$emit('reload')
            this.close()
           } else {
            onMessagePopup(this, res.commonMsg)
           }
          } catch (error) {
            console.log(error)
          }
        })
    },
    async fnUpdateCrtIPMstCallback() { // IP 블럭 수정
      const sipCreateSeqCd = this.sipCreateSeqCd
      if (sipCreateSeqCd.length < 10) {
          onMessagePopup(this, '생성차수 수정정보가 잘못되었습니다.')
          return
      }
      let res
      try {
          const tbIpBlockVo = {
            sipCreateSeqCd: this.sipCreateSeqCd,
            scomment: this.resultVo.scomment,
            nipBlockMstSeq: this.resultVo.nipBlockMstSeq,
          }
         res = await apiRequestJson(ipmsJsonApis.updateCrtIPMst, tbIpBlockVo)

           if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, 'IP블록 수정이 정상적으로 처리되었습니다.')
              this.$emit('reload')
              this.close()
           } else {
            onMessagePopup(this, res.commonMsg)
           }
        } catch (error) {
            console.error(error)
        }
    },
    isClose() {
        if (this.type === 'edit') {
          this.$confirm('작성한 정보가 삭제됩니다. 팝업창을 닫겠습니까?', '신청 정보 저장 알림', {
            confirmButtonText: '확인',
            cancelButtonText: '취소'
          }).then(async () => {
            this.close() // 확인 버튼을 누르면 모달을 닫음
          }).catch(() => {

          })
        } else {
          this.close()
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
