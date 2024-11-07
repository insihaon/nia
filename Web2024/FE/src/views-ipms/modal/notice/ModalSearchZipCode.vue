<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="주소 검색"
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
    <div v-loading="viewLoading">
      <div class="popupContentTable">
        <div class="popupContentTableTitle">주소입력</div>
        <table>
          <colgroup>
            <col width="22%" /><col width="78%" />
          </colgroup>
          <tbody>
            <tr>
              <th>주소 찾기</th>
              <td class="textflex">
                <el-input v-model="txtInputDongNm" type="text" size="small" placeholder="SEARCH" @keyup.enter.native="fnSelectZipcode()" />
                <el-button type="primary" size="small" icon="el-icon-search" round @click="fnSelectZipcode()" />
              </td>
            </tr>
            <tr>
              <th>상세주소</th>
              <td>
                <el-input v-model="txtAddress" type="text" size="mini" title="" disabled="disabled" />
                <el-input v-model="detailAddress" type="text" size="mini" title="상세주소" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-if="pagination.data.length > 0" class="popupContentTable textcenter">
        <div class="popupContentTableTitle">조회결과</div>
        <table>
          <colgroup>
            <col width="20%" /><col width="80%" />
          </colgroup>
          <thead>
            <tr>
              <th>우편번호</th>
              <th>주소</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="pagination.data.length === 0">
              <td colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
            <template v-else-if="pagination.data.length > 0">
              <tr v-for="(row, index) in pagination.data" :key="index">
                <td>
                  <a @click="fnZipcodeSelected(row.zipcode, row.newkaddr, row.eaddr)">
                    {{ row.zipcode }}
                  </a>
                </td>
                <td>
                  <a @click="fnZipcodeSelected(row.zipcode, row.newkaddr, row.eaddr)">
                    {{ row.newkaddr }}
                    <br>
                    {{ row.pastkaddr }}
                  </a>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
        <div v-if="pagination.data.length > 0" class="tableListWrap">
          <div class="tableListPaging" style="justify-content: center;">
            <el-pagination
              :current-page.sync="pagination.currentPage"
              :total="pagination.total"
              :page-size="pagination.pageSize"
              layout="prev, pager, next"
              @current-change="handleChangeCurPage"
              @size-change="handleChangeCurPage"
            />
          </div>
        </div>
        <!-- <div class="page_num">
          <ul>
            <ui:pagination pagination-info="${paginationInfo}" type="image" js-function="fnSelectListPageInPop" />
          </ul>
        </div> -->
      </div>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-check" round @click="btnSaveSearchAddr()">저장</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">
        {{ $t('exit') }}
      </el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

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
      pagination: this.setDefaultPagination(),
      viewLoading: false,
      txtInputDongNm: '',
      viewType: '',
      txtAddress: '',
      detailAddress: '',
      zipcodePrefix: '',
      searchAddrVo: {}
    }
  },
  computed: {
    getPageCount() {
      const { total, pageSize } = this.pagination
      if (total <= pageSize) {
        return 1
      } else {
        return (total % pageSize) > 0 ? parseInt(total / pageSize) + 1 : parseInt(total / pageSize)
      }
    }
  },
   methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      this.txtInputDongNm = ''
      this.viewType = model.type
    },
   async fnSelectZipcode() { /* 주소 검색 */
      if (this.txtInputDongNm === '') {
        onMessagePopup(this, '검색할 주소를 입력하세요.')
        return
      }
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const searchVo = { dong: this.txtInputDongNm.replace(' ', '%'), pageType: this.viewType, pageUnit, pageIndex }
      try {
        this.viewLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewSearchZipCode, searchVo)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.viewLoading = false
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnSelectZipcode()
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
</style>
