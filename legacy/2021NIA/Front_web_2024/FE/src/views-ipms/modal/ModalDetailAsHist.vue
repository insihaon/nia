<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사설AS 이력 상세"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">사설AS 이력 정보</div>
      <table>
        <colgroup>
          <col width="15%"/>
          <col width="85%"/>
        </colgroup>
        <tr>
          <th>AS번호</th>
          <td>{{ nrequestAsSeq }}</td>
        </tr>
      </table>
      <div class="popupContentTableTitle">사설AS 이력 조회결과</div>
      <table v-loading="tableLoading" style="height: 200px">
        <thead>
          <tr>
            <th>고객명</th>
            <th>신청일</th>
            <th>요청자</th>
            <th>노드1 명</th>
            <th>노드1 전용번호</th>
            <th>노드2 명</th>
            <th>노드2 전용번호</th>
            <th>처리일시</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="pagination.data.length === 0">
            <td class="textcenter" colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(row, index) in pagination.data" :key="index">
              <td>{{ row.srequestAsCtm }}</td>
              <td>{{ row.dcreateDt ? moment(row.dcreateDt).format('YYYY-MM-DD') : '' }}</td>
              <td>{{ row.screateNm }}</td>
              <td>{{ row.srequestAsObjNm1 }}</td>
              <td>{{ row.srequestAsObjLlnum1 }}</td>
              <td>{{ row.srequestAsObjNm2 }}</td>
              <td>{{ row.srequestAsObjLlnum2 }}</td>
              <td>{{ row.dapvDt ? moment(row.dapvDt).format('YYYY-MM-DD') : '' }}</td>
              <td>{{ row.srequestAsTypeNm }}</td>
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
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalDetailAsHist'

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
      nrequestAsSeq: '',
      tableLoading: false,
      resultList: [],
      totalCount: 0
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.pagination.data = []
      if (model.row) {
        this.nrequestAsSeq = model.row.nrequestAsSeq
        this.fnViewDetailAsHist(model.row.nrequestAsSeq)
      }
    },
    onClose() {
    },
    async fnViewDetailAsHist(nrequestAsSeq = null) {
      if (nrequestAsSeq === null) return
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailAsHist, { nrequestAsSeq })
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewDetailAsHist()
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
