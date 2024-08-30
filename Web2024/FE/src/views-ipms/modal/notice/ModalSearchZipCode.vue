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
        Whois 정보 변경 {{ onChangetitle }}
        <hr>
      </span>

      <div id="content" class="layer">

        <div class="content_result mt0">
          <table class="tbl_data entry" summary="주소입력">
            <caption>주소입력</caption>
            <colgroup>
              <col width="22%" /><col width="78%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">주소 찾기</th>
                <td>
                  <div class="search w99">
                    <el-input id="txtInputDongNm" v-model="txtInputDongNm" type="text" size="mini" class="w-90" value="" title="search" placeholder="SEARCH" @click="fnSearchEnter()" />
                    <el-button size="mini" class="sc_btn_addr" @click="fnSelectZipcode()">
                      <i class="el-icon-zoom-in"></i>
                    </el-button>
                  </div>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">상세주소</th>
                <td>
                  <input id="txtZipcodePrefix" type="hidden" value="" />
                  <input id="txtZipcodeSuffix" type="hidden" value="" />
                  <input id="txtEaddress" type="hidden" value="" />
                  <input id="txtAddress" type="text" class="txt" value="" title="" disabled="disabled" />
                  <input id="detailAddress" type="text" class="txt" value="" title="상세주소" onkeypress="fnSaveEnter();" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <template v-if="resultListVo.length > 0">
          <div id="searchResult" class="content_result">
            <h4>조회결과</h4>
            <table class="tbl_list mt5" summary="조회결과">
              <caption>조회결과</caption>
              <colgroup>
                <col width="20%" /><col width="80%" />
              </colgroup>
              <thead>
                <tr>
                  <th class="first" scope="col">우편번호</th>
                  <th scope="col">주소</th>
                </tr>
              </thead>
              <tbody>
                <c:if test="${resultListVo.totalCount == 0}">
                  <tr class="subbg last">
                    <td class="first" colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
                  </tr>
                </c:if>

                <!--  -->
              </tbody>
            </table>

            <div class="page_num">
              <ul>
                <ui:pagination pagination-info="${paginationInfo}" type="image" js-function="fnSelectListPageInPop" />
              </ul>
            </div>
          </div>
        </template>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-check" @click="fnApprRegWhoisModReqSubmit('R')">{{ $t('저장') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalSearchZipCode'

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
      resultListVo: [],
      txtInputDongNm: '',
      viewType: ''
    }
  },
  computed: {
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
      this.viewType = model.type
    },
   async fnSelectZipcode() { /* 주소 검색 */
      if (this.txtInputDongNm === '') {
        return
      }
      try {
        const searchVo = {
        dong: this.txtInputDongNm.replace(' ', '%'),
        pageType: this.viewType
      }
       const res = await apiRequestModel(ipmsModelApis.viewSearchZipCode, searchVo)
       this.resultListVo = res.result.data
      } catch (error) {
        console.log(error)
      }
    },

    /* ---------------------- */
    fnSearchEnter() {

    },
    fnApprRegWhoisModReqSubmit() { /* 주소 저장 */
      this.$confirm('신청정보가 삭제됩니다. 정말 신청취소 하시겠습니까?', '변경신청취소', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const tbWhoisModfiyVo = {
            nmodify_apply_seq: this.resultVo.nmodify_apply_seq,
          }
          const res = await apiRequestJson(ipmsJsonApis.viewDeleteWhoisModReq, tbWhoisModfiyVo)
          if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
            this.$message.success(`WHOIS 정보 변경 신청 내역이 정상적으로 취소 되었습니다.`)
          }
        } catch (error) {
          console.log(error)
        }
      })
    },

  },
}
</script>
<style lang="scss" scoped>
.ModalSearchZipCode{
  .el-input {
    width: 100%;
  }
}

</style>
