<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블록병합"
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
    <div v-loading="loading" class="popupContentTable">
      <div class="popupContentTableTitle">할당 병합 정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr>
            <th>계위</th>
            <td>
              {{ resultComplexVo.srcIpAssignMstVo.ssvcLineTypeNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcGroupNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcObjNm }}
            </td>
            <th>공인/사설</th>
            <td>{{ resultComplexVo.srcIpAssignMstVo.sipCreateTypeNm }}</td>
          </tr>
          <tr>
            <th>IP 버전</th>
            <td>{{ resultComplexVo.srcIpAssignMstVo.sipVersionTypeNm }}</td>
            <th>IP 주소</th>
            <td>{{ resultComplexVo.srcIpAssignMstVo.pipPrefix }}</td>
          </tr>
          <tr>
            <th>시작 IP</th>
            <td>{{ resultComplexVo.srcIpAssignMstVo.sfirstAddr }}</td>
            <th>끝 IP</th>
            <td>{{ resultComplexVo.srcIpAssignMstVo.slastAddr }}</td>
          </tr>
          <tr>
            <th>비고</th>
            <td colspan="3">
              <textarea v-model="mrgScomment" rows="4"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">병합 대상 정보</div>
      <div class="scroll_area">
        <table>
          <thead>
            <tr>
              <th>서비스망</th>
              <th>본부</th>
              <th>노드</th>
              <th>공인/사설</th>
              <th>서비스</th>
              <th>IP블록</th>
              <th>배정범위</th>
              <th>단위블록수</th>
              <th>배정상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in destIpAssignMstVos" :key="index">
              <td>{{ item.ssvcLineTypeNm }}</td>
              <td>{{ item.ssvcGroupNm }}</td>
              <td>{{ item.ssvcObjNm }}</td>
              <td>{{ item.sipCreateTypeNm }}</td>
              <td>{{ item.sassignTypeNm }}</td>
              <td>{{ item.pipPrefix }}</td>
              <td>{{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
              <td>
                {{ formatNumber(item.nclassCnt) }}
              </td>
              <td>{{ item.sassignLevelNm }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-menu" round @click="fnMergeConfirmBtnClick">병합</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalIpAssignMerge'

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
      resultComplexVo: {
        srcIpAssignMstVo: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        sipCreateTypeNm: '',
        sipVersionTypeNm: '',
        pipPrefix: '',
        sfirstAddr: '',
        slastAddr: ''
       },
      },
      mrgScomment: '',
      tbIpAssignMstListVo: [],
      destIpAssignMstVos: [],
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model.tbIpAssignMstListVo) {
        this.tbIpAssignMstListVo = model.tbIpAssignMstListVo
        this.fnViewInsertMrgAsgnIPMst(model.tbIpAssignMstListVo)
      }
    },
    onClose() {
    },
    async fnViewInsertMrgAsgnIPMst(tbIpAssignMstListVo) {
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewInsertMrgAsgnIPMst, tbIpAssignMstListVo)
        if (res.result.data.commonMsg === 'SUCCESS' || res.result.data.commonMsg === null) {
          this.resultComplexVo = res.result.data
          this.destIpAssignMstVos = res.result.data?.destIpAssignMstVos
        } else {
          onMessagePopup(this, res.result.data.commonMsg)
          this.close()
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
    formatNumber(value) {
      return new Intl.NumberFormat().format(value)
    },
    async fnMergeConfirmBtnClick() {
      // 병합 버튼 클릭 이벤트 처리
      const { pipPrefix, sipVersionTypeCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd } = this.resultComplexVo.srcIpAssignMstVo
      const srcIpAssignMstVo = { pipPrefix, sipVersionTypeCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd, scomment: this.mrgScomment }
      const tbIpAssignMstComplexVo = { srcIpAssignMstVo, destIpAssignMstVos: [], menuType: 'Aloc' }

      this.destIpAssignMstVos.forEach(row => {
        tbIpAssignMstComplexVo.destIpAssignMstVos.push({ nipAssignMstSeq: row.nipAssignMstSeq })
      })
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertMrgAsgnIPMst, tbIpAssignMstComplexVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, 'IP블록 병합이 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
