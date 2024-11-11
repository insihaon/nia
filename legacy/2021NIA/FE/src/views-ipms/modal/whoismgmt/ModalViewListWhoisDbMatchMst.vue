<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="DB 현행화"
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
    <div class="popupContentTable">
      <table>
        <colgroup>
          <col width="3%" /><col width="10%" />
          <col width="5%" /><col width="15%" />
          <col width="5%" /><col width="10%" />
          <col width="5%" /><col width="10%" />
          <col width="5%" /><col width="10%" />
        </colgroup>
        <tbody>
          <tr>
            <th>IP 주소</th>
            <td>
              <el-input
                v-model="searchWrdPop"
                placeholder="IP 주소 입력"
              />
            </td>

            <th>서비스</th>
            <td>
              <el-select
                v-model="searchVo.sassignTypeCd"
                placeholder="전체"
              >
                <el-option label="전체" value="" />
                <el-option
                  v-for="item in sassignTypeCdList"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </td>

            <th>사용기관명</th>
            <td>
              <el-input
                v-model="sorgnamePop"
                placeholder="사용기관명"
              />
            </td>

            <th>네트워크이름</th>
            <td>
              <el-input
                v-model="snetNmPop"
                placeholder="네트워크이름"
              />
            </td>

            <th>작업종류</th>
            <td>
              <el-select
                v-model="srequestCdPop"
                placeholder="전체"
              >
                <el-option label="전체" value="" />
                <el-option
                  v-for="item in listReqTypeCd"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </td>
          </tr>
        </tbody>
      </table>

    </div>

    <el-col :span="24" class="my-2">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="true"
        :text-des="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
      </compTable>
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click.native="fnKeywordDel()">DB 현행화 전송</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalViewListWhoisDbMatchMst'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
       tableColumns: [
        { prop: 'suserorggb', label: 'IP', width: 200, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '노드', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '서비스', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '기관명', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '네트워크이름', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '작업종류', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '변경일시', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '등록현황', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
      ],
      suserorggbOp: [
        { label: '고객명 일치', value: '10' },
        { label: '고객명 포함', value: '20' },
      ],
       searchWrdPop: '',
      sorgnamePop: '',
      snetNmPop: '',
      srequestCdPop: '',
      searchVo: {
        sassignTypeCd: ''
      },
      sassignTypeCdList: [], // 서비스 항목 리스트 (API로 받아온 데이터)
      listReqTypeCd: [] // 작업종류 리스트 (API로 받아온 데이터)
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1500
    },
    onOpen(model, actionMode) {
      setTimeout(() => {
        this.fnViewListWhoisKeywordMstNew()
      }, 10)
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhoisKeywordMstNew()
    },
    async fnViewListWhoisKeywordMstNew() {
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const param = { pageUnit, pageIndex }
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisDbMatchMst, param)
        this.pagination.data = res.resultListVo.tbWhoisKeywordVos ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnKeywordDel() {

    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
