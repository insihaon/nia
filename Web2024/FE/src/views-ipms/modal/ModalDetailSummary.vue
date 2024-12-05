<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="라우팅 중복 상세정보"
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
      <div class="popupContentTableTitle">라우팅 중복 상세정보</div>
      <div v-loading="loading" class="scroll_area" style="max-height: 500px">
        <table>
          <thead>
            <tr>
              <th>서비스망</th>
              <th>본부</th>
              <th>주노드</th>
              <th>노드</th>
              <th>IP블록</th>
              <th>As-Path</th>
              <th>Community</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="totalCount === 0">
              <td class="text-center" colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
            <template v-else>
              <tr v-for="(item, index) in resultList" :key="index">
                <td>{{ item.ssvcLineTypeNm }}</td>
                <td>{{ item.ssvcGroupNm }}</td>
                <td>{{ item.ssvcHighNm }}</td>
                <td>{{ item.ssvcObjNm }}</td>
                <td>{{ item.pipPrefix }}</td>
                <td>{{ item.asPath }}</td>
                <td>{{ item.scommunity }}</td>
              </tr>
            </template>
          </tbody>
        </table>
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

const routeName = 'ModalDetailSummary'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
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
      if (model.row) {
        const tbIpAssignMstVo = {
          ssvcLineTypeCd: model.row.ssvcLineTypeCd,
          nipAssignMstSeq: model.row.nipAssignMstSeq
        }
        this.loadData(tbIpAssignMstVo)
      }
    },
    onClose() {
      this.resultList = []
    },
    async loadData(tbIpAssignMstVo) {
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailSummary, tbIpAssignMstVo)
        this.resultList = res.result.data
        this.totalCount = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
