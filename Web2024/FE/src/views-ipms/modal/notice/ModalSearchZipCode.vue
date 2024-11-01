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

      <div id="content" ref="content" class="layer">

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
                    <el-input v-model="txtInputDongNm" type="text" size="mini" placeholder="SEARCH" @click="fnSearchEnter()">
                      <template #suffix>
                        <el-button
                          size="mini"
                          style="font-size: larger;border: none"
                          @keyup.enter.native="fnSelectZipcode"
                          @click="fnSelectZipcode()"
                        >
                          <i class="el-icon-search font-weight-bolder"></i>
                        </el-button>
                      </template>
                    </el-input>
                  </div>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">상세주소</th>
                <td>
                  <el-input v-model="txtAddress" type="text" size="mini" title="" disabled="disabled" />
                  <el-input v-model="detailAddress" type="text" size="mini" title="상세주소" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div id="searchResult" class="content_result">
          <h4>조회결과</h4>
          <div class="tbl_list mt5" su-mmary="조회결과">
            <el-col ref="tableContainer" :span="24">
              <compTable
                ref="compTable"
                :prop-name="name"
                :prop-table-height="'400px'"
                :prop-data="pagination.data"
                :prop-column="tableColumns"
                prop-grid-menu-id="inputSpeed"
                :prop-grid-indx="1"
                :text-des="false"
              >
              </compTable>
            </el-col>
          </div>
        </div>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-check" @click="btnSaveSearchAddr()">저장</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

const routeName = 'ModalSearchZipCode'

export default {

  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      // pagination: this.setDefaultPagination(),
        tableColumns: [
        {
          prop: 'zipcode', label: '우편번호', align: 'center', width: '250', sortable: false, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              type: 'info',
              on: { click: () => {
                this.fnZipcodeSelected(row.zipcode, row.newkaddr, row.eaddr)
            } } }, row.zipcode)
          }
         },
          {
            prop: 'address', label: '주소', align: 'center', width: '300', sortable: false, columnVisible: true, showOverflow: true,
            formatter: (row, col, value, index) => {
              return this.$createElement(
                'el-button',
                {
                  type: 'info',
                  on: {
                    click: () => {
                      this.fnZipcodeSelected(row.zipcode, row.newkaddr, row.eaddr)
                    },
                  },
                },
                [
                  this.$createElement('div', {}, row.newkaddr),
                  this.$createElement('br'),
                  this.$createElement('div', {}, row.pastkaddr),
                ]
              )
            }
          }
      ],
      txtInputDongNm: '',
      viewType: '',
      txtAddress: '',
      detailAddress: '',
      zipcodePrefix: '',
      searchAddrVo: {},
      resultListVo: [],
       pagination: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 아이템 수
        total: 0, // 전체 데이터 개수
        data: [] // 현재 페이지에 보여줄 데이터
      }
    }
  },
  computed: {
    pagedData() {
      return this.pagination.data
    }
  },
  mounted() {

  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.txtInputDongNm = ''
    },
     handlePageChange(page) {
      this.pagination.currentPage = page
      this.setPageData() // 페이지에 맞는 데이터 설정
    },
    setPageData() {
       const startIndex = (this.pagination.currentPage - 1) * this.pagination.pageSize
    const endIndex = startIndex + this.pagination.pageSize
    this.pagination.data = this.resultListVo.slice(startIndex, endIndex)
    },
    async fnSelectZipcode() { /* 주소 검색 */
      if (this.txtInputDongNm === '') {
         onMessagePopup(this, '검색할 주소를 입력하세요.')
        return
      }
      const searchVo = {
        dong: this.txtInputDongNm.replace(' ', '%'),
        pageType: this.viewType
      }
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewSearchZipCode, searchVo)
        this.resultListVo = res.result.data ?? []
        this.pagination.data = res.result.data ?? []
        this.pagination.total = this.resultListVo.length
         this.setPageData()
      } catch (error) {
        console.log(error)
      } finally {
        this.closeLoading(target)
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
