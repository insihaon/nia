<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
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
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        주소 검색
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
                    <el-button size="mini" class="sc_btn_addr" @keyup.enter="fnSelectZipcode()" @click="fnSelectZipcode()">
                      <i class="el-icon-zoom-in"></i>
                    </el-button>
                  </div>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">상세주소</th>
                <td>
                  <!-- <input id="txtZipcodePrefix" type="hidden" value="" />
                  <input id="txtZipcodeSuffix" type="hidden" value="" />
                  <el-input id="txtEaddress" v-model="txtEaddress" size="mini" type="hidden" /> -->
                  <el-input v-model="txtAddress" type="text" size="mini" title="" disabled="disabled" />
                  <el-input v-model="detailAddress" type="text" size="mini" title="상세주소" />
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
                <tr v-if="resultListVo.length === 0" class="subbg last">
                  <td class="first" colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>

                <tr v-for="(item, index) in resultListVo" v-else :key="index">
                  <td class="first">
                    <a @click="fnZipcodeSelected(item.zipcode, item.newkaddr, item.eaddr)">
                      {{ item.zipcode }}
                    </a>
                  </td>
                  <td>
                    <a @click="fnZipcodeSelected(item.zipcode, item.newkaddr, item.eaddr)">
                      {{ item.newkaddr }}
                      <br>
                      {{ item.pastkaddr }}
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- <div class="page_num">
              <ul>
                <ui:pagination pagination-info="${paginationInfo}" type="image" js-function="fnSelectListPageInPop" />
              </ul>
            </div> -->
          </div>
        </template>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-check" @click="btnSaveSearchAddr()">{{ $t('저장') }}</el-button>
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
      viewType: '',
      txtAddress: '',
      detailAddress: '',
      zipcodePrefix: '',
      searchAddrVo: {}

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
      this.viewType = model.type
    },
   async fnSelectZipcode() { /* 주소 검색 */
      if (this.txtInputDongNm === '') {
        this.$message('검색할 주소를 입력하세요.')
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
    fnZipcodeSelected(zipcode, newkaddr, eaddr) { /* 주소 클릭  */
    this.txtAddress = newkaddr

      this.searchAddrVo = {
        zipcode: zipcode,
        newkaddr: newkaddr,
        eaddr: eaddr,
      }
    },
    btnSaveSearchAddr() {
      this.close()
    },
    onClose() {
      this._merge(this.searchAddrVo, { detailAddress: this.detailAddress })
      this.$emit('setAddrForm', this.searchAddrVo)
    }
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
