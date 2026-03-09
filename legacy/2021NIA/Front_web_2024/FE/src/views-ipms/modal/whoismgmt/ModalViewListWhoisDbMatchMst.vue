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
    <div class="popupContentTable mb-1">
      <table>
        <tbody>
          <tr>
            <th>IP 주소</th>
            <td>
              <el-input v-model="searchWrd" />
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
            <td><el-button class="float-right my-2" size="small" round type="primary" @click="handleClickSearch()"> 조회 </el-button></td>
          </tr>
        </tbody>
      </table>
    </div>
    <el-col :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="true"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            조회 결과
          </span>
        </template>
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
      selectedChecks: []
    }
  },
  mounted() {
    this.loadServiceCd()
    this.loadReqTypeCd()
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
      this.pagination = this.setDefaultPagination()
    },
    handleClickTableCheck(all, cur) {
      this.selectedChecks = all
    },
    handleClickSearch() {
      this.fnViewListWhoisKeywordMstNew()
    },
    async loadServiceCd() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectListCommonCode)
        this.sassignTypeCdList = res.result?.data
      } catch (error) {
        this.error(error)
      }
    },
    async loadReqTypeCd() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.listReqTypeCd)
        this.listReqTypeCd = res.result?.data
      } catch (error) {
        this.error(error)
      }
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
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
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
          onMessagePopup(this, '정상적으로 반영 하였습니다.')
        } else if (res.commonMsg === '03') {
          onMessagePopup(this, '반송되었습니다. 팝업창 변경버튼을 통하여 전송하시기 바랍니다.')
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
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
