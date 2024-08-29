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
      <!-- TACS관리 / IP주소 라우팅 비교/점검 > 장비별 명령어 정보관리 > 신규생성 -->
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        무선IP Summary 정보 관리
        <hr>
      </span>
      <div id="content" class="layer">
        <!-- 조회 Section -->
        <div class="content_result mt0">
          <h4>조회</h4>
          <table class="tbl_data entry" summary="Summary">
            <caption>Summary</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th scope="row">구분</th>
                <td>
                  <select v-model="searchSkind" class="w60">
                    <option value="">전체</option>
                    <option value="public">public</option>
                    <option value="private">private</option>
                    <option value="both">both</option>
                  </select>
                </td>
                <th scope="row">IP 블록</th>
                <td>
                  <input
                    v-model="searchSIpBlock"
                    type="text"
                    class="txt w80"
                    maxlength="250"
                  />
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area mt-2">
            <el-button size="mini" type="primary" @click="fnSelectSummuryList">조회</el-button>
          </div>
        </div>
        <!-- 등록 Section -->
        <div class="content_result mt0">
          <h4>등록</h4>
          <table class="tbl_data entry" summary="Summary">
            <caption>Summary</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th scope="row">구분</th>
                <td>
                  <select v-model="insertSkind" class="w60">
                    <option value="public">public</option>
                    <option value="private">private</option>
                    <option value="both">both</option>
                  </select>
                </td>
                <th scope="row">IP 블록</th>
                <td>
                  <input
                    v-model="insertSIpBlock"
                    type="text"
                    class="txt w80"
                    maxlength="250"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="btn_area mt-2">
          <el-button size="mini" type="primary" @click="fnInsertBtnClick">저장</el-button>
        </div>
        <!-- 조회결과 Section -->
        <div class="content_result">
          <compTable
            ref="compTable"
            :prop-table-height="350"
            :prop-is-check-box="true"
            :prop-data="resultList"
            :prop-is-pagination="true"
            :prop-column="tableColumns"
            @update:propSelected="handleClickCheck"
          >
            <template slot="text-description">
              <span>
                조회결과
              </span>
            </template>
          </compTable>
        </div>
        <!-- <div class="page_num">
          <ul>
            <li v-for="page in paginationInfo.totalPages" :key="page">
              <a href="#none" @click="changePage(page)">{{ page }}</a>
            </li>
          </ul>
        </div> -->
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="fnDeleteBtnClick">삭제</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalSummaryMst'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchSkind: '',
      searchSIpBlock: '',
      insertSkind: 'public',
      insertSIpBlock: '',
      resultList: [],
      tableColumns: [
        { prop: 'skindNm', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP 블록', align: 'center', columnVisible: true, showOverflow: true },
      ],
      delRows: null
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.fnSelectSummuryList()
    },
    onClose() { },
    async fnSelectSummuryList() {
      const params = { pageIndex: 1, pageUnit: 10, sKindCd: this.searchSkind, pipPrefix: this.searchSIpBlock }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewPopSummaryMst, params)
        this.resultList = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertBtnClick() {
      const params = { skindCd: this.insertSkind, skindNm: this.insertSkind, pipPrefix: this.insertSIpBlock }
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertMobileSummMst, params)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 등록되었습니다.')
          this.fnSelectSummuryList()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    handleClickCheck(rows) {
      this.delRows = rows
    },
    async fnDeleteBtnClick() {
      if (this.delRows === null || this.delRows.length === 0) {
        onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
        return
      }
      this.confirm('해당 무선IP Summary 정보를 정말 삭제하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        try {
          const delList = []
          this.delRows.forEach(row => { delList.push(row.nmobileIpSummSeq) })
          const res = await apiRequestJson(ipmsJsonApis.deleteMobileSummMst, { delList })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '정상적으로 삭제되었습니다.')
            this.fnSelectSummuryList()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.SsvcLineType ::v-deep {
  width: 100%;
  display: flex;
  label {
    margin-left: 0px !important;
  }
}
</style>
