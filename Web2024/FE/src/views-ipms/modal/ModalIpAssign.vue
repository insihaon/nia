<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블록배정"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">배정 정보</div>
      <table>
        <colgroup>
          <col width="15%" /><col width="30%" /><col width="30%" /><col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>계위</th>
            <td>
              <el-select id="updSsvcLineTypeCd" v-model="ssvcLineTypeCd" size="mini">
                <el-option v-for="option in ssvcLineTypeOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </el-option>
              </el-select>
            </td>
            <td>
              <el-select id="updSsvcGroupCd" v-model="ssvcGroupCd" size="mini">
                <el-option v-for="option in ssvcGroupOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </el-option>
              </el-select>
            </td>
            <td>
              <el-select id="updSsvcObjCd" v-model="ssvcObjCd" size="mini">
                <el-option v-for="option in ssvcObjOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>배정상태</th>
            <td>
              <el-select id="updSassignLevelCd" v-model="sassignLevelCd" size="mini">
                <el-option v-for="option in sassignTypeLevelOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </el-option>
              </el-select>
            </td>
            <th>서비스</th>
            <td>
              <el-select id="updSassignTypeCd" v-model="sassignTypeCd" :disabled="sassignLevelCd !== 'IA0004'" size="mini">
                <el-option v-for="option in sassignTypeOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>비고</th>
            <td colspan="3">
              <textarea id="updScomment" v-model="scomment" class="w98" rows="3" maxlength="4000"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">배정 대상 정보</div>
      <div>
        <table id="baseTable" class="tbl_list my-3" summary="목록">
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
              <th scope="col">단위블록수</th>
              <th scope="col">배정상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in tbIpAssignMstListVo" :key="index">
              <td> {{ item.ssvcLineTypeNm }}</td>
              <td> {{ item.ssvcGroupNm }}</td>
              <td> {{ item.ssvcObjNm }}</td>
              <td> {{ item.sipCreateTypeNm }}</td>
              <td> {{ item.sassignTypeNm }}</td>
              <td> {{ item.pipPrefix }}</td>
              <td> {{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
              <td> {{ item.nclassCnt }}</td>
              <td> {{ item.sassignLevelNm }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-document-checked" round @click.native="fnUpdateConfirmBtnClick()">배정</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalIpAssign'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      ssvcLineTypeOptions: [
        { label: '공인', value: 'CL0003' },
        { label: 'Bogon', value: 'Bogon' },
        { label: '유/무선공용', value: '유/무선공용' },
      ],
      ssvcGroupOptions: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'SCHOOLNET', value: 'CL0005' }
      ],
      ssvcObjOptions: [
        { value: 'CV0001', label: 'IPv4' },
        { value: 'CV0002', label: 'IPv6' },
      ],
      sassignTypeLevelOptions: [
        { label: '미배정', value: 'IA0001' },
        { label: '예비배정', value: 'IA0002' },
        { label: '배정[미할당]', value: 'IA0003' },
        { label: '서비스배정[미할당]', value: 'IA0004' },
      ],
      sassignTypeOptions: [
        { label: '-------', value: '-------' },
        { label: '공통서비스', value: '공통서비스' },
        { label: 'KT사내망', value: 'KT사내망' },
        { label: 'POOL-LoT(고정)', value: 'POOL-LoT(고정)' },
        { label: 'POOL-M2M(고정)', value: 'POOL-M2M(고정)' },
        { label: 'IMS-SYSTEM', value: 'IMS-SYSTEM' },
      ],

      ssvcLineTypeCd: '',
      ssvcGroupCd: '',
      ssvcObjCd: '',
      sassignLevelCd: '',
      sassignTypeCd: '',
      scomment: '',
      ssvcLineTypeNm: '',
      pipPrefix: '',
      tbIpAssignMstListVo: [],
      resultAsgnList: null
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
      this.domElement.maxWidth = 1200
    },

    onOpen(model, actionMode) {
      if (model.type) {
        this.tbIpAssignMstListVo = [model.row]
        this.fnViewUpdateAsgnIPMst(this.tbIpAssignMstListVo)
      } else {
        this.tbIpAssignMstListVo = model.tbIpAssignMstListVo.tbIpAssignMstVos
        this.fnViewUpdateAsgnIPMst(model.tbIpAssignMstListVo.tbIpAssignMstVos)
      }

      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd, scomment } = this.tbIpAssignMstListVo[0]
      this.ssvcLineTypeCd = ssvcLineTypeCd
      this.ssvcGroupCd = ssvcGroupCd
      this.ssvcObjCd = ssvcObjCd
      this.sassignLevelCd = sassignLevelCd
      this.sassignTypeCd = sassignTypeCd
      this.scomment = scomment
    },
     async fnViewUpdateAsgnIPMst(tbIpAssignMstListVo) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewUpdateAsgnIPMst, tbIpAssignMstListVo ?? tbIpAssignMstListVo)
        this.tbIpAssignMstListVo = res?.result?.data
        this.resultAsgnList = this.tbIpAssignMstListVo[0]
      } catch (error) {
        console.error(error)
      }
    },

    /* 기존 사설(CT0004)은 유/무선 공용으로 사용, 신규 사설(CT0005)을 사설로 사용 */
    async fnUpdateConfirmBtnClick() { // IP 블록 배정
     const selectedrow = this.tbIpAssignMstListVo[0]
      var vOrgCreateTypeFlag = 'N'
      if (this.sipCreateTypeCd === 'CT0005') {
        vOrgCreateTypeFlag = 'Y'
      }

      /* 서비스 배정일경우 N/A 체크 처리 */
      if (this.sassignLevelCd === 'IA0004' && this.sassignTypeCd === '-------' /* SA0000 */) {
        onMessagePopup(this, '서비스 배정일 경우 서비스를 ------- 로 선택하실수 없습니다.')
        return
      }

      /* 사설일 경우 망변경 불가 */
      const { ssvcLineTypeCd } = selectedrow
      if (vOrgCreateTypeFlag === 'Y' && ssvcLineTypeCd !== this.ssvcLineTypeCd) {
        onMessagePopup(this, '배정 대상 블록 중 사설 블록은 망변경 배정을 할 수 없습니다.')
        return
      }

      if (vOrgCreateTypeFlag !== 'Y' && ssvcLineTypeCd !== this.ssvcLineTypeCd) {
        const confirmed = await this.$confirm(
          '기본 블록의 망정보와 배정 선택 망 정보가 다릅니다. 배정하시겠습니까?',
          'IP블록배정',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
        ).catch(() => false)

        if (!confirmed) {
          return
        }
      }

      /*  배정 처리 유형 처리 */
      try {
        const tbIpAssignMstComplexVo = {
        srcIpAssignMstVo: {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          sassignLevelCd: this.sassignLevelCd,
          sassignTypeCd: this.sassignTypeCd,
          scomment: this.scomment,
          typeFlag: '',

        },
        destIpAssignMstVos: []
      }

      let typeFlag = null
      if (this.sassignLevelCd === 'IA0004') {
        typeFlag = 'svcassign' // 배정 - 서비스배정
      } else {
        typeFlag = 'assign' // 배정 - 일반배정
      }

        this.tbIpAssignMstListVo.forEach(item => {
          tbIpAssignMstComplexVo.destIpAssignMstVos.push({
            nipAssignMstSeq: item.nipAssignMstSeq,
            pipPrefix: item.pipPrefix,
            sipVersionTypeCd: item.sipVersionTypeCd,
            typeFlag: typeFlag
          })
        })

        const res = await apiRequestJson(ipmsJsonApis.updateAsgnIPMst, tbIpAssignMstComplexVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('IP블록 배정이 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.$message.error({ message: `IP블록 배정에 실패했습니다.` })
        }
      } catch (error) {
        this.$message.error({ message: `IP블록 배정에 실패했습니다.` })
        console.error(error)
      }
    },
    onClose() { this.selectedRow = [] },
  },
}
</script>
<style lang="scss" scoped>
  .ModalIpAssign{
    .el-select {
      width: 100%;
    }
  }
</style>
