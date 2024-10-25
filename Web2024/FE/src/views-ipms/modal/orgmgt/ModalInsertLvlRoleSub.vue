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
            <td>{{ resultListVo[0].ssvcLineTypeNm }}</td>
            <td>{{ resultListVo[0].slvlGroupNm }}</td>
            <td>{{ resultListVo[0].slvlHighNm }}</td>
            <td>{{ resultListVo[0].slvlNm }}</td>
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
      <div class="popupContentTableTitle">노드국 목록</div>
      <table>
        <caption>조직계위정보 목록</caption>
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

        <tbody>
          <!-- 조회된 결과 목록이 없을 때 -->
          <template v-if="resultVo.totalCount === 0">
            <tr>
              <td colspan="3" class="textcenter">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
          </template>
          <!-- 조회된 결과 목록이 있을 때 -->
          <template v-if="resultVo.totalCount > 0">
            <tr
              v-for="(item, index) in resultListNodeVo"
              :key="index"
              :class="{'subbg': index % 2 !== 0, 'last': index === resultListNodeVo.length - 1}"
            >
              <td :title="item.slofficecode">{{ item.ssvcOfficeCd }}</td>
              <td :title="item.slofficeNm">{{ item.ssvcOfficeNm }}</td>
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
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'
import ModalEntireOrgSearch from '@/views-ipms/modal/search/ModalEntireOrgSearch.vue'

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
      resultVo: null,
      resultListVo: [],
      resultListNodeVo: [],
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
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.resultVo = this._cloneDeep(model.row)
      this.resultListVo = this._cloneDeep(model.row.data)
      this.resultListNodeVo = this._cloneDeep(model.row.data)

      this.ssvcObjNm = ''
    },
     fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {
      this.ssvcObjNm = row?.slvlNm
      this.ssvcObjCd = row?.slvlCd
    },
    async fnDeleteRoleSub(item) { /* 노드국 목록 삭제 */
      let res
      try {
        const TbLvlRoleSubVo = {
          smodifyId: this.$store.state.user.info.suserId,
          ssvcLineType: item.ssvcLineTypeCd,
          ssvcObjCd: item.ssvcObjCd,
          ssvcLineTypeCd: item.ssvcLineTypeCd
        }
        res = await apiRequestJson(ipmsJsonApis.deleteLvlRoleSub, TbLvlRoleSubVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('정상처리 되었습니다.')
          const index = this.resultListNodeVo.indexOf(item)
            if (index > -1) {
              this.resultListNodeVo.splice(index, 1)
          }
        } else {
          return
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnInsertFmBtnClick() {
      this.resultListVo
      if (this.resultListVo[0].ssvcObjCd === this.ssvcObjCd) {
        this.$message('이미 등록된 국사입니다.')
        return
      }

      if (this.ssvcObjNm === '' || this.ssvcObjNm === null) {
        this.$message('등록 할 국사 정보가 없습니다.')
        return
      }

      let res
      try {
        const TbLvlRoleSubVo = {
          smodifyId: this.$store.state.user.info.suserId,
          ssvcObjCd: this.resultListVo[0].ssvcObjCd,
          ssvcLineTypeCd: this.resultListVo[0].ssvcLineTypeCd,
          ssvcOfficeCd: this.ssvcObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.insertLvlRoleSub, TbLvlRoleSubVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('국사 등록이 완료 되었습니다.')
          this.resultListNodeVo.push({
            ssvcOfficeCd: this.ssvcObjCd,
            ssvcOfficeNm: this.ssvcObjNm
          })
          this.ssvcObjCd = ''
          this.ssvcObjNm = ''
        } else {
          this.$message.error(`${res.commonMsg}`)
        }
      } catch (error) {
        console.error(error)
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
