<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="시설 수용국 관리(FM)"
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
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">조직계위</div>
      <table>
        <colgroup>
          <col width="25%" /><col width="25%" /><col width="25%" /><col width="25%" />
        </colgroup>
        <thead>
          <tr>
            <th>서비스망</th>
            <th>센터/지연본부</th>
            <th>주노드</th>
            <th>노드</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ resultVo.ssvcLineTypeNm }}</td>
            <td>{{ resultVo.ssvcGroupNm }}</td>
            <td>{{ resultVo.ssvchighObjNm }}</td>
            <td>{{ resultVo.ssvcObjNm }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">조회결과</div>
      <table>
        <tbody>
          <tr>
            <th>국사선택</th>
            <td class="textflex">
              <el-input v-model="ssvcObjNm" readonly size="small">
                <template #suffix>
                  <el-button
                    size="small"
                    icon="el-icon-search"
                    @click="fnViewSearchCenterLvlCd()"
                  />
                </template>
              </el-input>
              <el-button type="primary" size="small" round @click="fnInsertFmBtnClick()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>

    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">수용국 목록</div>
      <table>
        <colgroup>
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
        </colgroup>
        <thead>
          <tr>
            <th>계위코드</th>
            <th>국사명</th>
            <th>삭제</th>
          </tr>
        </thead>

        <tbody v-loading="tableLoading">
          <!-- 조회된 결과 목록이 없을 때 -->
          <template v-if="resultListVo.length === 0">
            <tr>
              <td colspan="3" class="textcenter">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
          </template>
          <!-- 조회된 결과 목록이 있을 때 -->
          <template v-if="resultListVo.length > 0">
            <tr v-for="(item, index) in resultListVo" :key="index">
              <td>{{ item.ssvcOfficeCd }}</td>
              <td>{{ item.ssvcOfficeNm }}</td>
              <td>
                <el-button type="danger" size="small" icon="el-icon-delete" circle @click="fnDeleteRoleSub(item)" />
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalEntireOrgSearch ref="ModalEntireOrgSearch" @selected-value="setSelectedRow" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson, ipmsModelApis, apiRequestModel } from '@/api/ipms'
import _ from 'lodash'
import ModalEntireOrgSearch from '@/views-ipms/modal/search/ModalEntireOrgSearch.vue'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalInsertLvlRoleSub'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableLoading: false,
      resultVo: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvchighObjNm: '',
        ssvcObjNm: '',
      },
      resultListVo: [],
      ssvcObjNm: '',
      ssvcObjCd: ''
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      if (model.row) {
        this.resultVo = this._cloneDeep(model.row)
        this.fnViewInsertLvlRoleSub()
      }
      this.ssvcObjNm = ''
      this.ssvcObjCd = ''
    },
    async fnViewInsertLvlRoleSub() { /* 수용국 조회 */
     const { ssvcLineTypeCd, ssvcLineTypeNm, ssvcGroupNm, ssvchighObjNm, ssvcObjCd, ssvcObjNm } = this.resultVo
      try {
        this.tableLoading = true
        const TbLvlRoleSubVo = { ssvcLineTypeCd, ssvcLineTypeNm, ssvcGroupNm, ssvchighObjNm, ssvcObjCd, ssvcObjNm }
        const res = await apiRequestModel(ipmsModelApis.viewInsertLvlRoleSub, TbLvlRoleSubVo)
        this.resultListVo = res.result?.data ?? []
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
     fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {
      this.ssvcObjNm = row?.slvlNm
      this.ssvcObjCd = row?.slvlCd
    },
    async fnDeleteRoleSub(item) { /* 노드국 목록 삭제 */
      const { ssvcLineTypeCd, ssvcObjCd } = this.resultVo
      const TbLvlRoleSubVo = { smodifyId: this.$store.state.user.info.suserId, ssvcLineTypeCd, ssvcObjCd, ssvcOfficeCd: item.ssvcOfficeCd }
      try {
        const res = await apiRequestJson(ipmsJsonApis.deleteLvlRoleSub, TbLvlRoleSubVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '국사 삭제 정상처리 되었습니다.')
          this.fnViewInsertLvlRoleSub()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertFmBtnClick() {
      if (this.resultListVo.map(row => row.ssvcOfficeCd).includes(this.ssvcObjCd)) {
        onMessagePopup(this, '이미 등록된 국사정보입니다.')
        return
      }
      if (this.ssvcObjNm === '' || this.ssvcObjNm === null) {
        onMessagePopup(this, '등록 할 국사 정보가 없습니다.')
        return
      }
      const { ssvcLineTypeCd, ssvcObjCd } = this.resultVo
      const TbLvlRoleSubVo = { smodifyId: this.$store.state.user.info.suserId, ssvcLineTypeCd, ssvcObjCd, ssvcOfficeCd: this.ssvcObjCd }
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertLvlRoleSub, TbLvlRoleSubVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '국사 등록이 완료 되었습니다.')
          this.fnViewInsertLvlRoleSub()
          this.ssvcObjCd = ''
          this.ssvcObjNm = ''
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    onClose() {
      this.$emit('reload')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
