<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    ref="assignModal"
    v-el-drag-dialog
    title="IP블록 할당 계위 설정"
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
    <div v-loading="vLoading">
      <div class="popupContentTable">
        <div class="popupContentTableTitle">할당 정보</div>
        <table>
          <colgroup>
            <col width="15%" /><col width="30%" /><col width="30%" /><col width="30%" />
          </colgroup>
          <tbody>
            <tr>
              <th>계위</th>
              <td>
                <el-select v-model="selectedRow.ssvcLineTypeCd" disabled class="w-100" size="mini">
                  <el-option
                    v-for="item in ssvcLineTypeNmOp"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </td>
              <td>
                <el-select v-model="selectedRow.ssvcGroupCd" class="w-100" size="mini" @change="handleChangeLvl2">
                  <el-option
                    v-for="item in ssvcGroupOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </td>
              <td>
                <el-select v-model="selectedRow.ssvcObjCd" class="w-100" size="mini" @change="handleChangeLvl3">
                  <el-option
                    v-for="item in ssvcObjOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </td>
            </tr>
            <tr>
              <th>배정상태</th>
              <td>
                <el-select v-model="selectedRow.sassignLevelCd" size="mini" disabled popper-class="sassignLevelCd">
                  <el-option
                    v-for="option in [{ label: '서비스배정[미할당]', value: 'IA0004' }]"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </el-option>
                </el-select>
              </td>
              <th>서비스</th>
              <td>
                <el-select v-model="selectedRow.sassignTypeCd" disabled size="mini">
                  <el-option v-for="option in sassignTypeOptions" :key="option.value" :label="option.label" :value="option.value">
                    {{ option.label }}
                  </el-option>
                </el-select>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td colspan="3">
                <textarea v-model="selectedRow.scomment" class="w98" rows="3" maxlength="4000"></textarea>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="popupContentTable">
        <div class="popupContentTableTitle">할당 대상 정보</div>
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
        <el-button type="primary" size="small" icon="el-icon-document-checked" round @click.native="fnViewDetailAlcIpMst()">할당</el-button>
        <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalIpAllocOrgSetting'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      vLoading: false,
      selectedRow: {
        ssvcLineTypeCd: null,
        ssvcLineTypeNm: '',
        ssvcGroupCd: null,
        ssvcGroupNm: null,
        ssvcObjCd: null,
        ssvcObjNm: null,
        sassignLevelCd: '',
        sassignTypeCd: '',
        scomment: '',
        pipPrefix: '',

      },
      ssvcGroupOptions: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'SCHOOLNET', value: 'CL0005' }
      ],
      isDisabledSassignLevelCd: false,
      sassignTypeLevelOptions: [
        { label: '서비스배정[미할당]', value: 'IA0004' },
      ],
      sassignTypeOptions: [],
      ssvcLineTypeNmOp: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcObjOptions: [{
        label: '-------',
        value: '000000'
      }],
      allocObj: {},
      tbIpAssignMstListVo: [],
    }
  },
  computed: {

  },
  mounted() {
  },
  methods: {
    /*
      TO-DO
      - v6 이고 '시설용 IP' 서비스 선택 시 배정상태는 '서비스배정[미할당]'으로 고정한다.
      - v6 일 때에는 시설용 IP서비스 유형은 집군화 하여 출력한다.
    */
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model.row) {
        this.selectedRow = this._cloneDeep(model.row)
        this.fnViewUpdateAsgnIPMst(model.row)
        this.fnSelectSassignType()
      }
    },
    async fnViewUpdateAsgnIPMst(row) {
      try {
        this.vLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewUpdateAsgnIPMst, { tbIpAssignMstVos: [{ nipAssignMstSeq: row.nipAssignMstSeq }] })
        this.tbIpAssignMstListVo = res.result.data
        this.checkMethod() /* set 계위  */
      } catch (error) {
        this.error(error)
      } finally {
        this.vLoading = false
      }
    },
    async checkMethod() {
      if (this.ssvcLineTypeCd !== null || this.ssvcLineTypeNm !== '000000') {
        await this.handleChangeLvl1()
      }
      if (this.ssvcGroupCd !== null || this.ssvcGroupCd !== '000000') {
        await this.handleChangeLvl2()
      }
      /* 값 null 일 경우  */
      if (this.ssvcGroupCd === null) {
        const selectedOption = this.ssvcGroupOptions.find((option) => option.label === '-------')

        if (selectedOption) {
          this.ssvcGroupCd = selectedOption.value
        }
      }
      if (this.ssvcObjCd === null) {
        this.ssvcObjCd = null
      }
    },
    async fnSelectSassignType() {
      const { ssvcLineTypeCd, sipVersionTypeCd } = this.selectedRow
      const tbLvlBasVo = { ssvcLineTypeCd, sipVersionTypeCd }

      const res = await apiRequestJson(ipmsJsonApis.selectOrgSassignTypeCdList, tbLvlBasVo)
      this.sassignTypeOptions = res?.tbIpAllocMstVos?.map(v => {
        return { value: v.sassignTypeCd, label: v.sassignTypeNm }
      })
      // this.ssvcObjOptions = []
    },
    async handleChangeLvl1() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.selectedRow.ssvcLineTypeCd }

      this.ssvcGroupCd = null
      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupOptions = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => {
        return { value: v.ssvcGroupCd, label: v.ssvcGroupNm }
      })
      this.ssvcObjOptions = []
    },
    async handleChangeLvl2(value) {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.selectedRow.ssvcLineTypeCd,
        ssvcGroupCd: this.selectedRow.ssvcGroupCd,
      }
      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjOptions = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체').map(v => {
        return { value: v.ssvcObjCd, label: v.ssvcObjNm }
      })
      this.selectedRow.ssvcGroupNm = this.ssvcGroupOptions.find(v => v.value === value)?.label
    },
    handleChangeLvl3(value) {
      this.selectedRow.ssvcObjNm = this.ssvcObjOptions.find(v => v.value === value)?.label
    },
    // 할당 버튼 > 할당 상세 (ip할당 > 할당상세 조회 데이터 참고)
    fnViewDetailAlcIpMst() {
      const row = this.selectedRow
      // row ssvcGroupCd, ssvcObjCd 변경
      this.$emit('alocDetailCallBtnClick', row)
      this.close()
      // this.$refs.ModalIpAllocDetail.open({ row: this.selectedRow })
    },

  },
}
</script>
<style lang="scss" scoped>
  .ModalIpAssign {
    .el-select {
      width: 100%;
    }
  }
</style>
