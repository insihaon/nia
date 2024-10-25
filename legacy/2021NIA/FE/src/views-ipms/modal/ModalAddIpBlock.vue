<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블록생성"
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
      <div class="popupContentTableTitle">IP 블록 생성</div>
      <table>
        <tr>
          <th>공인/사설</th>
          <td>
            <el-select v-model="sipCreateTypeCd" size="small" :disabled="isDisabled">
              <el-option v-for="option in sipCreateOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </el-option>
            </el-select>
          </td>
          <th>생성차수</th>
          <td>
            <el-input v-model="sipCreateSeqCd" size="small" type="text" readonly="readonly" disabled />
          </td>
        </tr>

        <tr>
          <th>서비스망</th>
          <td>
            <el-select v-model="ssvcLineTypeCd" size="small" :disabled="isDisabled">
              <el-option v-for="option in ssvcLineOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </el-option>
            </el-select>
          </td>
          <th>IP 버전</th>
          <td>
            <el-select v-model="sipVersionTypeNm" size="small" :disabled="isDisabled">
              <el-option v-for="option in sipVersionOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </el-option>
            </el-select>
          </td>
        </tr>
        <tr>
          <th>비고</th>
          <td colspan="3">
            <textarea v-model="scomment" rows="3" maxlength="4000"></textarea>
          </td>
        </tr>
      </table>
      <table>
        <tbody>
          <tr>
            <th>IP 주소</th>
            <td class="textflex">
              <el-input v-model="pipPrefix" size="small" type="text" maxlength="40" style="width : 85%" />
              <el-button type="primary" size="small" round @click="fnAppendBtnClick()">추가</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">목록</div>
      <div>
        <table>
          <thead>
            <tr>
              <th>순번</th>
              <th>IP 블록</th>
              <th>시작 IP</th>
              <th>끝 IP</th>
              <th>단위블록수</th>
              <th>총 IP수</th>
              <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in ipBlockDetailList" :key="index">
              <td> {{ index + 1 }}</td>
              <td> {{ item.pipPrefix }}</td>
              <td> {{ item.sfirstAddr }}</td>
              <td> {{ item.slastAddr }}</td>
              <td> {{ item.nclassCnt }}</td>
              <td> {{ item.ncnt }}</td>
              <td> <el-button type="primary" size="small" round @click="fnRemoveBtnClick()">삭제</el-button> </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="popupContentTableBottom">
        <el-button type="primary" size="small" round @click="fnInitBtnClick()"> 초기화</el-button>
        <el-button type="primary" size="small" round @click="fnSaveBtnClick()"> 등록 </el-button>
      </div>
      <div class="popupContentTableTitle">IP블록 처리결과</div>
      <el-input v-model="commonMsg" size="small" readonly />
    </div>

    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'

const routeName = 'ModalAddIpBlock'

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
      sipCreateTypeCd: '',
      sipCreateTypeNm: '',
      sipCreateSeqCd: '',
      ssvcLineTypeCd: '',
      sipVersionTypeNm: '',
      sipVersionTypeCd: '',
      sipCreateOptions: [
        { label: '공인', value: '공인' },
        { label: 'Bogon', value: 'Bogon' },
        { label: '유/무선공용', value: '유/무선공용' },
      ],
      ssvcLineOptions: [
        { value: 'KORNET', label: 'KORNET' },
        { value: 'PREMIUM', label: 'PREMIUM' },
        { value: 'MOBILE', label: 'MOBILE' },
        { value: 'GNS', label: 'GNS' },
        { value: 'SCHOOLNET', label: 'SCHOOLNET' }
      ],
      sipVersionOptions: [
        { value: 'CV0001', label: 'IPv4' },
        { value: 'CV0002', label: 'IPv6' },
      ],
      sipCreateSeqNm: '',
      scomment: '',
      type: 'create',
      IpBlockDetail: [],
      tableDatas: [],
      ipBlockResult: '',
      description: '',
      viewType: '',
      pipPrefix: '',
      ipBlockDetailList: [],
      resultListVo: null,
      commonMsg: ''
    }
  },
  computed: {
    isDisabled() {
      return this.viewType !== 'create'
    }
  },
  mounted() {
  },
  methods: {
    sipCreate() {
      console.log(this.sipCreateTypeCd)
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.commonMsg = ''
      this.resultListVo = model?.row
      this.viewType = this.model.type
      this.fnViewUpdateCrtIPMstCallBack(model)
      if (model.type === 'generate') {
        const { sipCreateTypeCd, sipCreateTypeNm, sipCreateSeqCd, ssvcLineTypeCd, sipVersionTypeNm, sipVersionTypeCd } = this.resultListVo
        this.sipCreateTypeCd = sipCreateTypeCd
        this.sipCreateTypeNm = sipCreateTypeNm
        this.sipCreateSeqCd = sipCreateSeqCd
        this.ssvcLineTypeCd = ssvcLineTypeCd
        this.sipVersionTypeNm = sipVersionTypeNm
        this.sipVersionTypeCd = sipVersionTypeCd
      } else {
        this.sipCreateTypeCd = ''
        this.sipCreateTypeNm = ''
        this.sipCreateSeqCd = ''
        this.ssvcLineTypeCd = ''
        this.sipVersionTypeNm = ''
        this.sipVersionTypeCd = ''
      }
      },
      async fnViewUpdateCrtIPMstCallBack(model) {
        try {
          const res = await apiRequestModel(ipmsModelApis.viewInsertCrtIPMst, model)
          this.resultListVo = res?.result.data
        } catch (error) {
          console.error(error)
        }
      },
      onResetForm() {
        this.ipBlockDetailList = []
        this.pipPrefix = ''
      },

      onClose() { this.resultListVo = [] },
      async fnSaveBtnClick() {
        // ip 등록
        if (this.ipBlockDetailList.length === 0) {
          this.$message('등록할 목록이 없습니다.')
        return
      }

      this.confirm('등록하시겠습니까?', 'IP블록생성', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        let res
        try {
          const tbIpBlockMstVos = []

          for (let i = 0; i < this.ipBlockDetailList.length; i++) {
            const ipBlock = this.ipBlockDetailList[i]

            tbIpBlockMstVos.push({
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: ipBlock.pipPrefix,
              scomment: this.scomment || '',
            })
          }

          const tbIpBlockListVo = {
            tbIpBlockMstVos: tbIpBlockMstVos,
          }

           res = await apiRequestJson(ipmsJsonApis.insertListCrtIPMst, tbIpBlockListVo)

          if (res.commonMsg === 'SUCCESS') {
            this.$message('IP블록 등록이 정상적으로 처리되었습니다.')
            this.commonMsg = 'IP블록이 정상적으로 등록되었습니다.'
            this.onResetForm()
            this.$emit('reloadData')
            this.close()
          } else {
            this.$message.error({ message: 'IP블록 등록에 실패했습니다.' })
          }
        } catch (error) {
          this.$message.error({ message: `${res.commonMsg}` })
          console.error(error)
        }
      })
    },
    async fnAppendBtnClick() {
      // ip 추가
      if (this.sipCreateTypeCd === '') {
        this.$message('공인/사설이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.ssvcLineTypeCd === '') {
        this.$message('서비스망이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.sipVersionTypeNm === '') {
        this.$message('IP버전이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
      this.viewType = 'generate'
      let res
      try {
          const ipBLockCheckVo = {
            srcIpBlockMstVo: {
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: this.pipPrefix,
            },
            destIpBlockMstVos: []
          }
           res = await apiRequestJson(ipmsJsonApis.appendCrtIPMst, ipBLockCheckVo)
          if (res.commonMsg === 'SUCCESS') {
             const resultData = res
              this.ipBlockDetailList.push({
                pipPrefix: resultData.pipPrefix,
                sfirstAddr: resultData.sfirstAddr,
                slastAddr: resultData.slastAddr,
                nclassCnt: resultData.nclassCnt,
                ncnt: resultData.ncnt,
              })
          }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
    fnInitBtnClick() {
      // 초기화
      this.ipBlockDetailList = []
    },
    fnRemoveBtnClick(index) {
      // 블럭 삭제
      this.ipBlockDetailList.splice(index, 1)
    }
  },
}
</script>
<style lang="scss" scoped>
  .ModalAddIpBlock ::v-deep{
    .el-select {
      width: 100%;
    }
  }
</style>
