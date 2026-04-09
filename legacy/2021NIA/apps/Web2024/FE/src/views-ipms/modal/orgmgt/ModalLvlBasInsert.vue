<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="조직계위 등록"
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
      <div class="popupContentTableTitle">조직계위</div>
      <table>
        <colgroup>
          <col width="30%" /><col width="70%" />
        </colgroup>
        <tbody>
          <tr>
            <th>서비스망</th>
            <!-- <td>KORNET - 강북운용단 - 혜화지사</td> -->
            <td>
              <el-select v-model="ssvcLineTypeCd" size="small" class="w-100">
                <el-option
                  v-for="item in svcLineListVo"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>센터/지역본부</th>
            <td>
              <el-input v-model="ssvcGroupNm" size="small" readonly>
                <template #suffix>
                  <el-button
                    size="small"
                    icon="el-icon-search"
                    @click="fnViewSearchCenterLvlCd('센터/지역본부')"
                  />
                </template>
              </el-input>
            </td>

          </tr>
          <tr>
            <th>노드</th>
            <td>
              <el-input v-model="ssvcObjNm" size="small" readonly>
                <template #suffix>
                  <el-button
                    slot="trigger"
                    size="small"
                    icon="el-icon-search"
                    @click="fnViewSearchCenterLvlCd('노드')"
                  />
                </template>
              </el-input>
            </td>
          </tr>
          <tr>
            <th>주노드</th>
            <td>
              <el-input v-model="ssvchighObjNm" size="small" readonly>
                <template #suffix>
                  <el-button
                    slot="trigger"
                    size="small"
                    icon="el-icon-search"
                    @click="fnViewSearchCenterLvlCd('주노드')"
                  />
                </template>
              </el-input>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-add" round @click="fnInsertValidTbLvlBas()">등록</el-button>
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

const routeName = 'ModalLvlBasInsert'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ssvcLineTypeCd: '',
      svcLineListVo: [],
      ssvcGroupNm: '-------',
      ssvcGroupCd: '000000',
      ssvcObjNm: '-------',
      ssvcObjCd: '000000',
      ssvchighObjNm: '-------',
      ssvchighObjCd: '000000'
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
      this.ssvcLineTypeCd = 'CL0001'
      this.ssvcGroupNm = ''
      this.ssvcGroupCd = ''
      this.ssvcObjNm = ''
      this.ssvcObjCd = ''
      this.ssvchighObjNm = ''
      this.ssvchighObjCd = ''
    },
    async loadSvcLineListVo() { /* svcLineListVo 조회 */
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertLvlBas, {})
        this.svcLineListVo = res.result.svcLineListVo.map(v => { return { label: v.ssvcLineTypeNm, value: v.ssvcLineTypeCd } })
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertSubmit() {
      if (this.resultVo.slvlNm === '') {
        onMessagePopup(this, '파라미터 오류, 계위명을 입력 하세요.')
        return
      }
      if (this.resultVo.sorgOfficeFlagYn === '') {
        onMessagePopup(this, '파라미터 오류, 계위구분을 입력 하세요.')
        return
      }
      try {
        const tbLvlCdVo = {
          smodifyId: this.$store.state.user.info.suserId,
          screateId: this.$store.state.user.info.suserId,
          slvlNm: this.resultVo.slvlNm,
          sexLinkUseTypeCd: 'CE0006',
          sorgOfficeFlagYn: this.resultVo.sorgOfficeFlagYn,
          scomment: this.resultVo.scomment,
        }
        const res = await apiRequestJson(ipmsJsonApis.insertTbLvlCdVo, tbLvlCdVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '계위를 정상적으로 등록하였습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnViewSearchCenterLvlCd(type) {
      if (type === '센터/지역본부') {
        if (this.ssvcLineTypeCd === '') {
          onMessagePopup(this, '서비스망을 선택하세요')
          return
        }
      } else if (type === '노드') {
        if (this.ssvcLineTypeNm === '' || this.ssvcGroupNm === '') {
          onMessagePopup(this, '센터/지역본부를 선택하세요')
          return
        }
      } else if (type === '주노드') {
        if (this.ssvcLineTypeNm === '' || this.ssvcGroupNm === '' || this.ssvcObjNm === '') {
          onMessagePopup(this, '노드를 선택하세요')
          return
        }
      }
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: type })
    },
    setSelectedRow(params) {
      const { row, viewTitle } = params
      if (viewTitle === '센터/지역본부') {
        this.ssvcGroupNm = row?.slvlNm
        this.ssvcGroupCd = row?.slvlCd
      } else if (viewTitle === '노드') {
        this.ssvcObjNm = row?.slvlNm
        this.ssvcObjCd = row?.slvlCd
      } else {
        this.ssvchighObjNm = row?.slvlNm
        this.ssvchighObjCd = row?.slvlCd
      }
    },
    async fnInsertValidTbLvlBas() { /* 유효성 체크 */
      try {
        const TbLvlBasVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
        }
        const res = await apiRequestJson(ipmsJsonApis.insertValidTbLvlBas, TbLvlBasVo)
        if (res.commonMsg === 'FAIL') {
          onMessagePopup(this, '사용중인 조직계위 정보입니다.')
        } else if (res.commonMsg === 'CENTERFAIL') {
          onMessagePopup(this, '해당 노드의 센터/지역본부 정보가 없습니다.')
        } else {
          this.fnInsertTbLvlBasSumit()
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertTbLvlBasSumit() { /* 조직계위등록 */
      try {
        const TbLvlRoleMstVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighCd: this.ssvchighObjCd,
          smodifyId: this.$store.state.user.info.suserId,
        }
        const res = await apiRequestJson(ipmsJsonApis.insertTbLvlBas, TbLvlRoleMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '조직계위등록이 정상처리 되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
