<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="조직계위정보 이동"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">변경 전 조직계위</div>
      <table>
        <colgroup>
          <col width="30%" /><col width="70%" />
        </colgroup>
        <tbody>
          <tr>
            <th>서비스망</th>
            <td>{{ resultVo.ssvcLineTypeNm }}</td>
          </tr>
          <tr>
            <th>센터/지역본부</th>
            <td>{{ resultVo.ssvcGroupNm }}</td>
          </tr>
          <tr>
            <th>노드</th>
            <td>{{ resultVo.ssvcObjNm }}</td>
          </tr>
          <tr class="last">
            <th>주노드</th>
            <td>{{ resultVo.ssvchighObjNm }}</td>

          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">변경 후 조직계위</div>
      <table class="tbl_data entry">
        <colgroup>
          <col width="30%" />
          <col width="70%" />
        </colgroup>
        <tbody>
          <tr class="top">
            <th>서비스망</th>
            <td>
              <el-select v-model="ssvcLineTypeCd" class="w-100" size="small" @change="handleChangeLvl1()">
                <el-option
                  v-for="item in svcLineListVo"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label "
                >
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>센터/지역본부</th>
            <td>
              <el-select v-model="ssvcGroupCd" :disabled="ssvcLineTypeCd === 'CL0000'" class="w-100" size="small" @change="handleChangeLvl2()">
                <el-option
                  v-for="item in centerListVo"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>노드</th>
            <td>
              <el-select v-model="ssvcObjCd" class="w-100" :disabled="ssvcLineTypeCd === 'CL0000' || ssvcGroupCd === ''" size="small">
                <el-option
                  v-for="item in nodeListVo"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>주노드</th>
            <td>
              <el-input
                v-model="ssvchighObjNm"
                size="small"
                readonly
              >
                <template #suffix>
                  <el-button
                    size="small"
                    icon="el-icon-search"
                    @click="fnViewSearchCenterLvlCd()"
                  />
                </template>
              </el-input>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" icon="el-icon-document-add" round @click="fnValidationLvlValue()">등록</el-button>
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

const routeName = 'ModalUpdateLvlBas'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      svcLineListVo: [],
      centerListVo: [],
      nodeListVo: [],
      resultVo: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        ssvchighObjNm: '',
      },
      ssvcLineTypeCd: 'CL0000',
      ssvcGroupCd: '',
      ssvcObjCd: '',
      ssvchighObjNm: '',
      ssvchighObjCd: '',
    }
  },
  computed: {
  },
  mounted() {
    this.loadSvcLineListVo()
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      // this.resultVo = this._cloneDeep(model.row)
      if (model.row) {
        this._merge(this.resultVo, model.row)
      }
      const { ssvcGroupNm, ssvcObjNm, ssvchighObjNm, ssvchighObjCd } = this.resultVo
      this.ssvcGroupCd = ssvcGroupNm // 센터지역본부
      this.ssvcObjCd = ssvcObjNm // 노드
      this.ssvchighObjNm = ssvchighObjNm // 주노드
      this.ssvchighObjCd = ssvchighObjCd //
    },
    async loadSvcLineListVo() { /* svcLineListVo 조회 */
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertLvlBas, {})
        this.svcLineListVo = res.result.svcLineListVo.map(v => { return { label: v.ssvcLineTypeNm, value: v.ssvcLineTypeCd } })
        this.svcLineListVo.unshift({ label: '-선택-', value: 'CL0000' })
      } catch (error) {
        this.error(error)
      }
    },
    async handleChangeLvl1() {
      this.ssvcGroupCd = ''
      this.ssvcObjCd = ''
      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
        this.centerListVo = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
        this.nodeListVo = []
      } catch (error) {
        this.error(error)
      }
    },
    async handleChangeLvl2() {
      this.ssvcObjCd = ''
      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd, ssvcGroupCd: this.ssvcGroupCd }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
        this.nodeListVo = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체').map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
      } catch (error) {
        this.error(error)
      }
    },
    fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(param) {
      const { row } = param
      this.ssvchighObjNm = row.slvlNm
      this.ssvchighObjCd = row.slvlCd
    },
    async fnValidationLvlValue() {
      if (this.ssvcLineTypeCd === 'CL0000' || this.ssvcLineTypeCd === '' || this.ssvcGroupCd === null || this.ssvcGroupCd === '' || this.ssvcObjCd === null || this.ssvcObjCd === '') {
        return this.$message('계위 정보가 없습니다.')
      }

        // 주노드 변경
        if ((this.resultVo.ssvcLineTypeCd === this.ssvcLineTypeCd) && (this.resultVo.ssvcGroupCd === this.ssvcGroupCd) && (this.resultVo.ssvcObjCd === this.ssvcObjCd) && (this.resultVo.ssvchighObjCd !== this.ssvchighObjCd)) {
            if (this.ssvcObjCd === '000000' || this.ssvcObjCd === null) {
                this.$message('노드정보가 올바르지 않습니다.')
                return
            }
        }
        // 노드 정보 변경
        if (this.resultVo.ssvcLineTypeCd !== this.ssvcLineTypeCd || this.resultVo.ssvcGroupCd !== this.ssvcGroupCd || this.resultVo.ssvcObjCd !== this.ssvcObjCd) {
            if (this.ssvcObjCd === '000000' || this.ssvcObjCd === null || this.ssvcObjCd === '') {
              this.$message('노드정보가 올바르지 않습니다.')
              return
            }
        }
        // 데이터 변경 없음
        if ((this.resultVo.ssvcLineTypeCd === this.ssvcLineTypeCd) && (this.resultVo.ssvcGroupCd === this.ssvcGroupCd) && (this.resultVo.ssvcObjCd === this.ssvcObjCd) && (this.resultVo.ssvchighObjCd === this.ssvchighObjCd)) {
           this.$message('변경할 데이터가 없습니다.')
           return
        }

      let res
      try {
        const TbLvlBasVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighObjCd: this.ssvchighObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.validTbLvlBas, TbLvlBasVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '이동할 계위정보가 없습니다.')
        } else {
          this.fnLvlMoveSubmit()
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnLvlMoveSubmit() {
      let res
      try {
        const TbLvlMstVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighCd: this.ssvchighObjCd,
          nlvlSeq: this.resultVo.nlvlBasSeq,
          bfsSsvcLineTypeCd: this.resultVo.ssvcLineTypeCd,
          bfsSsvcGroupCd: this.resultVo.ssvcGroupCd,
          bfSsvcObjCd: this.resultVo.ssvcObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.updateTbLvlMove, TbLvlMstVo)
         if (res.commonMsg === 'SUCCESS') {
          this.$message('정상처리 되었습니다.')
          this.$emit('reload')
          this.close()
         } else {
           this.$message.error({ message: `${res.message}` })
         }
      } catch (error) {
        console.error(error)
      }
    },
    onClose() {
      // this.ssvcLineTypeCd = 'CL0000'
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
