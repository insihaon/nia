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
                v-model="searchWrd"
                placeholder="IP 주소 입력"
              />
            </td>

            <th>서비스</th>
            <td>
              <el-select
                v-model="sassignTypeCd"
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
              <el-input v-model="sorgname" />
            </td>
            <th>네트워크이름</th>
            <td>
              <el-input v-model="snetNm" />
            </td>
            <th>작업종류</th>
            <td>
              <el-select
                v-model="srequestCd"
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
      <el-button class="float-right my-2" size="small" round type="primary" @click="handleClickSearch()"> 조회 </el-button>
    </div>
    <el-col :span="24" class="my-2">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-table-height="500"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="true"
        :text-des="false"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
      </compTable>
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click.native="fnDbMatch()">DB 현행화 전송</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import moment from 'moment'
import { onMessagePopup } from '@/utils/index'

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
        { prop: 'pipPrefix', label: 'IP', width: 200, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
         {
           prop: 'ssvcLineTypeNm', label: '노드', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => `${row.ssvcLineTypeNm} - ${row.ssvcGroupNm} - ${row.ssvcObjNm}`
        },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '기관명', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snetNm', label: '네트워크이름', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'swhoisRequestTypeNm', label: '작업종류', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '변경일시', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') },
        { prop: 'swhoisTranStatusNm', label: '등록현황', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
      ],
      suserorggbOp: [
        { label: '고객명 일치', value: '10' },
        { label: '고객명 포함', value: '20' },
      ],
      searchWrd: '',
      sorgname: '',
      snetNm: '',
      srequestCd: '',
      sassignTypeCd: '',
      sassignTypeCdList: [],
      listReqTypeCd: [],
      isFromOnOpen: false,
      selectedChecks: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1500
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhoisKeywordMstNew()
    },
    onOpen(model, actionMode) {
     this.isFromOnOpen = true // onOpen에서 호출됨을 표시
      setTimeout(() => {
        this.fnViewListWhoisKeywordMstNew()
      }, 10)
    },
    handleClickTableCheck(all, cur) {
      this.selectedChecks = all
    },
    handleClickSearch() {
      this.isFromOnOpen = false
      this.fnViewListWhoisKeywordMstNew()
    },
    async fnViewListWhoisKeywordMstNew() {
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const param = {
        loadSearchYn: 'Y',
        searchWrd: this.searchWrd,
        sassignTypeCd: this.sassignTypeCd,
        sorgname: this.sorgname,
        snetNm: this.snetNm,
        srequestCd: this.srequestCd,
      }
      Object.assign(param, { pageUnit, pageIndex })
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisDbMatchMst, param)
        if (this.isFromOnOpen !== true) {
          this.pagination.data = res.resultListVo.result.data ?? []
          this.pagination.total = res.resultListVo.result.totalCount
        }
        this.sassignTypeCdList = res.sassignTypeCdList
        this.listReqTypeCd = res.listReqTypeCd
        this.searchVo = res.searchVo
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnDbMatch() { /* DB 현행화 전송 */
      if (this.selectedChecks.length === 0) {
        onMessagePopup(this, 'DB 현행화할 정보를 선택 후 전송 가능합니다.')
        return
      }
      this.confirm('전송하시겠습니까?', 'DB 현행화 전송', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(async () => {
      const tbWhoisVo = {
        matchList: this.selectedChecks.map(item => item.nwhoisSeq)
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.dbMatchListTbWhoisVo, tbWhoisVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
        } else if (res.commonMsg === '03') {
          onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
        } else {
          onMessagePopup(this, '반영에 실패하였습니다.')
        }
      } catch (error) {
        console.error(error)
        }
      })
      .catch(action => {
      })
    },
    onClose() {
      this.isFromOnOpen = false
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
