<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="오더 노드국 관리(SON)"
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
            <td>
              <el-input v-model="ssvcObjNm" readonly size="small" class="txt w-80">
                <template #suffix>
                  <el-button
                    slot="trigger"
                    size="small"
                    style="font-size: larger; border: none; float: right"
                    icon="el-icon-search"
                    class="font-weight-bolder"
                    @click="fnViewSearchCenterLvlCd()"
                  />
                </template>
              </el-input>
              <el-button type="primary" size="small" round @click="fnValidslofficeSubmit()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">노드국 목록</div>
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
              <td>{{ item.slofficecode }}</td>
              <td>{{ item.slofficeNm }}</td>
              <td><el-button type="danger" size="small" icon="el-icon-delete" circle @click="fnDeleteTbLvlSubCd(item)" /></td>
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
import { onMessagePopup } from '@/utils'
import ModalEntireOrgSearch from '@/views-ipms/modal/search/ModalEntireOrgSearch.vue'

const routeName = 'ModalInsertLvlSonMgmt'

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
  computed: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      if (model.row) {
        this.resultVo = this._cloneDeep(model.row)
        this.fnViewInsertLvlSon()
      }
      this.ssvcObjNm = ''
      this.ssvcObjCd = ''
    },
    fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {
      this.ssvcObjNm = row?.slvlNm
      this.ssvcObjCd = row?.slvlCd
    },
    async fnViewInsertLvlSon() { /* 노드국 목록조회 */
      const { ssvcLineTypeCd, ssvcLineTypeNm, ssvcGroupNm: slvlGroupNm, ssvchighObjNm: slvlHighNm, ssvcObjCd: slvlCd, ssvcObjNm: slvlNm } = this.resultVo
     try {
        this.tableLoading = true
        const TbLvlSubCdVo = { ssvcLineTypeCd, slvlCd, slvlGroupNm, slvlHighNm, slvlNm, ssvcLineTypeNm }
        const res = await apiRequestModel(ipmsModelApis.viewInsertLvlSonMgmtPop, TbLvlSubCdVo)
        this.resultListVo = res.result?.data ?? []
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async fnInsertSonSubmit() {
      try {
        const TbLvlSubCdVo = {
          slofficecode: this.ssvcObjCd,
          smodifyId: this.$store.state.user.info.suserId,
          ssvcLineTypeCd: this.resultVo.ssvcLineTypeCd,
          slvlCd: this.resultVo.ssvcObjCd
        }
        const res = await apiRequestJson(ipmsJsonApis.insertTbLvlSubCd, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
         onMessagePopup(this, '국사 등록이 정상처리 되었습니다.')
         this.fnViewInsertLvlSon()
         this.ssvcObjCd = ''
         this.ssvcObjNm = ''
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnDeleteTbLvlSubCd(item) { /* 노드국 목록 삭제 */
      try {
        const TbLvlSubCdVo = {
          slofficecode: item.slofficecode,
          slvlCd: this.resultVo.ssvcObjCd,
          ssvcLineTypeCd: this.resultVo.ssvcLineTypeCd
        }
        const res = await apiRequestJson(ipmsJsonApis.deleteTbLvlSubCd, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '국사 삭제 정상처리 되었습니다.')
          this.fnViewInsertLvlSon()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnValidslofficeSubmit() {
      if (this.ssvcObjNm === '' || this.ssvcObjNm === null) {
        onMessagePopup(this, '등록 할 국사 정보가 없습니다.')
        return
      }
      try {
        const TbLvlSubCdVo = {
          slofficecode: this.ssvcObjCd,
          ssvcLineTypeCd: this.resultVo.ssvcLineTypeCd
        }
        const res = await apiRequestJson(ipmsJsonApis.selectSloffice, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnInsertSonSubmit()
        } else {
          onMessagePopup(this, '이미 등록된 국사정보입니다.')
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
