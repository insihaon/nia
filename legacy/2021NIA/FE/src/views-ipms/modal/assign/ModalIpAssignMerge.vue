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
          <tr class="top">
            <th class="first" scope="row">계위</th>
            <td class="view">
              {{ resultComplexVo.srcIpAssignMstVo.ssvcLineTypeNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcGroupNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcObjNm }}
            </td>
            <th scope="row">공인/사설</th>
            <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sipCreateTypeNm }}</td>
          </tr>
          <tr>
            <th class="first" scope="row">IP 버전</th>
            <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sipVersionTypeNm }}</td>
            <th scope="row">IP 주소</th>
            <td class="view">{{ resultComplexVo.srcIpAssignMstVo.pipPrefix }}</td>
          </tr>
          <tr>
            <th class="first" scope="row">시작 IP</th>
            <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sfirstAddr }}</td>
            <th scope="row">끝 IP</th>
            <td class="view">{{ resultComplexVo.srcIpAssignMstVo.slastAddr }}</td>
          </tr>
          <tr class="last">
            <th class="first" scope="row">비고</th>
            <td colspan="3">
              <textarea id="mrgScomment" v-model="mrgScomment" class="w98" rows="4" name="scomment"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">병합 대상 정보</div>
      <div class="scroll_area">
        <table>
          <colgroup>
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="12%" />
            <col width="16%" />
            <col width="12%" />
            <col width="10%" />
          </colgroup>
          <thead>
            <tr>
              <th class="first" scope="col">서비스망</th>
              <th scope="col">본부</th>
              <th scope="col">노드</th>
              <th scope="col">공인/사설</th>
              <th scope="col">서비스</th>
              <th scope="col">IP블록</th>
              <th scope="col">배정범위</th>
              <th scope="col" title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
              <th scope="col">배정상태</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in destIpAssignMstVos"
              :key="index"
              :class="{'last': index === destIpAssignMstVos.length - 1, 'subbg': index % 2 !== 0}"
            >
              <td class="first ellipsis">{{ item.ssvcLineTypeNm }}</td>
              <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
              <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
              <td class="ellipsis" :title="item.sipCreateTypeNm">{{ item.sipCreateTypeNm }}</td>
              <td class="ellipsis" :title="item.sassignTypeNm">{{ item.sassignTypeNm }}</td>
              <td class="ellipsis" :title="item.pipPrefix">{{ item.pipPrefix }}</td>
              <td class="ellipsis" :title="`${item.sfirstAddr} ~ ${item.slastAddr}`">{{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
              <td class="ellipsis">
                {{ formatNumber(item.nclassCnt) }}
              </td>
              <td class="ellipsis" :title="item.sassignLevelNm">{{ item.sassignLevelNm }}</td>
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
        this.resultComplexVo = res.result.data
        this.destIpAssignMstVos = res.result.data?.destIpAssignMstVos
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
