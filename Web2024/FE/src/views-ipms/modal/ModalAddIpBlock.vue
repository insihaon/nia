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
    <div ref="content" class="popupContentTable">
      <div class="popupContentTableTitle">IP 블록 생성</div>
      <table>
        <tr>
          <th>공인/사설</th>
          <td>
            <el-select v-model="sipCreateTypeCd" size="small" :disabled="isDisabled">
              <el-option v-for="option in sipCreateOptions" :key="option.value" :label="option.label" :value="option.value">
                {{ option.label }}
              </el-option>
            </el-select>
          </td>
          <th>생성차수</th>
          <td>
            <el-input v-model="sipCreateSeqCd" size="small" type="text" class="txt w95" readonly="readonly" disabled="disabled" />
          </td>
        </tr>

        <tr>
          <th>서비스망</th>
          <td>
            <el-select v-model="ssvcLineTypeCd" size="small" :disabled="isDisabled">
              <el-option v-for="option in ssvcLineOptions" :key="option.value" :label="option.label" :value="option.value">
                {{ option.label }}
              </el-option>
            </el-select>
          </td>
          <th>IP 버전</th>
          <td>
            <el-select v-model="sipVersionTypeCd" size="small" :disabled="isDisabled">
              <el-option v-for="option in sipVersionOptions" :key="option.value" :label="option.label" :value="option.value">
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
      <div ref="baseTable" class="popupContentTableTitle">목록</div>
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
              <td> <el-button type="primary" size="small" :disabled="btnDisabled" @click="fnRemoveBtnClick()">삭제</el-button> </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="popupContentTableBottom">
        <el-button class="el-icon-refresh" type="primary" size="small" round @click="fnInitBtnClick()"> 초기화</el-button>
        <el-button class="el-icon-document-add" type="primary" size="small" round @click="fnSaveBtnClick()"> 등록 </el-button>
      </div>
      <div class="popupContentTableTitle">IP블록 처리결과</div>
      <template v-if="commonMsg">
        <el-input v-model="commonMsg" readonly size="small" />
      </template>
    </div>

    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'
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
        { label: '공인', value: 'CT0001' },
        { label: 'Bogon', value: 'CT0003' },
        { label: '유/무선공용', value: 'CT0004' },
          //  { label: '사설', value: 'CT0005' }
      ],
      ssvcLineOptions: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'SCHOOLNET', value: 'CL0005' }
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
      commonMsg: '',
      btnStatus: false
    }
  },
  computed: {
    isDisabled() {
      return this.viewType !== 'create'
    },
    btnDisabled() {
      return this.btnStatus === true
    }
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
      this.commonMsg = ''
      this.viewType = this.model.type
      setTimeout(async() => {
        await this.fnViewInsertCrtIPMst(model.row)
      }, 10)
      this.onResetForm()
    },
    async fnViewInsertCrtIPMst(param) {
      const target = ({ vue: this.$refs.content })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewInsertCrtIPMst, param)
        this.resultListVo = res.result.data
        this.setValue()
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    setValue() {
      if (this.viewType === 'generate') {
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
    onResetForm() {
      this.ipBlockDetailList = []
      this.pipPrefix = ''
    },
    onClose() {
    },
    async fnSaveBtnClick() { /* IP 등록 */
      if (this.ipBlockDetailList.length === 0) {
        onMessagePopup(this, '등록할 목록이 없습니다.')
        return
      }

      this.confirm('등록하시겠습니까?', 'IP블록생성', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        let res
        const target = { vue: this.$refs.content }
        try {
          this.openLoading(target)

        // tbIpBlockListVo 초기화
        const tbIpBlockListVo = {
          tbIpBlockMstVos: []
        }

          this.ipBlockDetailList.forEach((ipBlock) => {
            tbIpBlockListVo.tbIpBlockMstVos.push({
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: ipBlock.pipPrefix,
              scomment: this.scomment || '',
            })
          })

          res = await apiRequestJson(ipmsJsonApis.insertListCrtIPMst, tbIpBlockListVo)

          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'IP블록 등록이 정상적으로 처리되었습니다.')
            this.commonMsg = 'IP블록이 정상적으로 등록되었습니다.'
            this.$emit('reloadData')
            this.btnStatus = true
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          console.error(error)
        } finally {
          this.closeLoading(target)
        }
      })
    },

    async fnAppendBtnClick() {
      // ip 추가

      if (this.sipCreateTypeCd === '') {
         onMessagePopup(this, '공인/사설이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.ssvcLineTypeCd === '') {
        onMessagePopup(this, '서비스망이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.sipVersionTypeCd === '') {
         onMessagePopup(this, 'IP버전이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
       }

      this.viewType = 'generate'
      let res
      const target = ({ vue: this.$refs.baseTable })
      try {
        this.openLoading(target)
        let resultData
        const ipBLockCheckVo = {
          srcIpBlockMstVo: {
            sipCreateTypeCd: this.sipCreateTypeCd,
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            sipCreateSeqCd: this.sipCreateSeqCd,
            sipVersionTypeCd: this.sipVersionTypeCd,
            pipPrefix: this.pipPrefix, // new pipPrefix
          },
          destIpBlockMstVos: []
        }

        // 기존 ip추가 목록이 있을시, 직전 pipPrefix
        if (this.ipBlockDetailList.length !== 0) {
          this.ipBlockDetailList.forEach((detail) => {
            ipBLockCheckVo.destIpBlockMstVos.push({
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: detail.pipPrefix ?? '', // old pipPrefix
            })
          })
        }

        res = await apiRequestJson(ipmsJsonApis.appendCrtIPMst, ipBLockCheckVo)

        if (res.commonMsg === 'SUCCESS') {
          resultData = res

          this.ipBlockDetailList.push({
            pipPrefix: resultData.pipPrefix,
            sfirstAddr: resultData.sfirstAddr,
            slastAddr: resultData.slastAddr,
            nclassCnt: resultData.nclassCnt,
            ncnt: resultData.ncnt,
          })
        } else {
           onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnInitBtnClick() { /* 초기화 */
      this.ipBlockDetailList = []
      this.viewType = 'create'
      if (this.viewType === 'create') {
        this.sipCreateTypeCd = '공인'
        this.ssvcLineTypeCd = 'KORNET'
        this.sipVersionTypeCd = 'CV0001'
      }
    },
    fnRemoveBtnClick(index) { /* 블럭 삭제 */
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
